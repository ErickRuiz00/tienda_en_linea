package server;

import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import common.Constants;
import common.Product;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.Map;

public class Server {

    private static ArrayList<ProductStock> stock;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private static Socket client;

    public static void main(String[] args) {
        try {
            int pto = 8000;
            ServerSocket server = new ServerSocket(pto);
            System.out.println("Servidor iniciado en el puerto " + pto);

            loadProducts();
            for (;;) {
                client = server.accept();

                oos = new ObjectOutputStream(client.getOutputStream());
                ois = new ObjectInputStream(client.getInputStream());

                handleRequests();
            }
        } catch (Exception e) {
            System.out.println("Something went wrong in server");
        }
    }

    private static void loadProducts() {
        try {
            Gson gson = new Gson();

            try (InputStream is = Server.class.getResourceAsStream("/products.json")) {
                Type listType = new TypeToken<ArrayList<ProductStock>>() {
                }.getType();

                stock = gson.fromJson(new InputStreamReader(is), listType);

                if (stock == null) {
                    stock = new ArrayList<>();
                    System.out.println("No hay productos disponibles");
                }
            } catch (Exception e) {
                System.out.println("Fallo al cargar archivo " + e.toString());
            }
        } catch (Exception e) {
            System.out.println("No se pudieron cargar los productos " + e.toString());
            stock = new ArrayList<>();
        }
    }

    private static void handleRequests() throws Exception {
        while (true) {
            String msg = (String) ois.readObject();
            if (msg == null) {
                continue;
            }

            switch (msg) {
                case Constants.LIST_PRODUCTS -> {
                    System.out.println("Handle Listing...");
                    sendProductList();
                }
                case Constants.ADD_PRODUCT -> {
                    System.out.println("Handle Adding...");
                    handleAdd();
                }
                case Constants.REDUCE_PRODUCT -> {
                    System.out.println("Updating Stock");
                    handleReduce();
                }
                case Constants.REMOVE_PRODUCT -> {
                    System.out.println("Deleting Product");
                    handleDelete();
                }
                case Constants.CLEAR -> {
                    System.out.println("Cleaning Cart");
                }
                case Constants.CHECKOUT -> {
                    System.out.println("Go to Checkout");
                    handleCheckout();
                }
                case Constants.SEARCH -> {
                    System.out.println("Searching product...");
                    handleSearch();
                }
                case Constants.CLOSE -> {
                    System.out.println("Finalizando comunicacion");
                    handleClose();
                }

                default ->
                    System.out.println("Solicitud no valida");
            }
        }
    }

    private static void sendProductList() {
        try {
            ArrayList<Product> products = new ArrayList<>();

            for (ProductStock ps : stock) {
                products.add(ps.getProduct());
            }

            oos.writeObject(products);
            oos.flush();

            System.out.println("Lista de productos enviada!");
        } catch (IOException e) {
            System.out.println("Ocurrio un error al enviar los productos");
        }

    }

    private static void handleAdd() {
        try {
            final int productId = (Integer) ois.readObject();

            final ProductStock ps = getProductById(productId);

            if (ps == null || !ps.isAvailable()) {
                oos.writeObject(Constants.DENY);
                oos.flush();
                return;
            }

            ps.decreaseStock();
            oos.writeObject(Constants.APPROVE);
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error en manejador de adicion");
        }
    }

    private static void handleReduce() {
        try {
            final int productId = (Integer) ois.readObject();
            final ProductStock ps = getProductById(productId);

            if (ps == null) {
                oos.writeObject(Constants.DENY);
                oos.flush();
                return;
            }

            ps.increaseStock();
            oos.writeObject(Constants.APPROVE);
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error en el manejador de reduccion");
        }
    }

    private static void handleDelete() {
        try {
            final Map<String, Object> map = (Map<String, Object>) ois.readObject();
            final int productId = (Integer) map.get("productId");
            final int quantity = (Integer) map.get("quantity");
            final ProductStock ps = getProductById(productId);

            if (ps == null) {
                oos.writeObject(Constants.DENY);
                oos.flush();
                return;
            }

            ps.increaseStock(quantity);
            oos.writeObject(Constants.APPROVE);
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error en el manejador de delete");
        }
    }

    private static void handleCheckout() {
        try {
            System.out.println("Stock actualizado correctamente");

            oos.writeObject(Constants.APPROVE);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Ocurrio un error al actualizar el stock" + e);
        }
    }

    private static void handleSearch() {
        try {
            final String term = (String) ois.readObject();
            final String lowerCaseTerm = term.toLowerCase();
            final ArrayList<Product> matches = new ArrayList<>();

            System.out.println("Buscando producto con termino " + lowerCaseTerm);

            for (ProductStock ps : stock) {
                final Product p = ps.getProduct();
                final String name = p.getName().toLowerCase();
                final String type = p.getType().toLowerCase();

                if (name.contains(lowerCaseTerm) || type.contains(lowerCaseTerm)) {
                    matches.add(p);
                }
            }

            System.out.println("Contador de coincidencias = " + matches.size());
            oos.writeObject(matches);
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error en manejador de busqueda");
        }
    }

    private static void handleClose() {
        try {
            oos.close();
            ois.close();
            client.close();
        } catch (Exception e) {
        }
    }
    
    private static ProductStock getProductById(int productId) {
        for (ProductStock ps : stock) {
            final int id = ps.getProduct().getProductId();

            if (id == productId) {
                return ps;
            }
        }

        return null;
    }
}
