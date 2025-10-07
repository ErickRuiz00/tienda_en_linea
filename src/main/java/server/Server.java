package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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

public class Server {

    static private ArrayList<ProductStock> stock;
    static private ObjectOutputStream oos;
    static private ObjectInputStream ois;

    public static void main(String[] args) {
        try {
            int pto = 8000;
            ServerSocket server = new ServerSocket(pto);
            System.out.println("Servidor iniciado en el puerto " + pto);

            loadProducts();
            for (;;) {
                Socket client = server.accept();

                oos = new ObjectOutputStream(client.getOutputStream());
                ois = new ObjectInputStream(client.getInputStream());

                handleRequests();
            }
        } catch (Exception e) {
            System.out.println("Something went wrong in server");
        }
    }

    static private void loadProducts() {
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
            String msg =  (String) ois.readObject();
            if (msg == null) {
                continue;
            }

            switch (msg) {
                case Constants.LIST_PRODUCTS -> {
                    System.out.println("Sending Products");
                    sendProductList();
                }
                case Constants.ADD_PRODUCT -> {
                    System.out.println("Checking Stock");
                }
                case Constants.REDUCE_PRODUCT -> {
                    System.out.println("Updating Stock");
                }
                case Constants.REMOVE_PRODUCT -> {
                    System.out.println("Deleting Product");
                }
                case Constants.CLEAR -> {
                    System.out.println("Cleaning Cart");
                }
                case Constants.CHECKOUT ->
                    System.out.println("Go to Checkout");
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
}
