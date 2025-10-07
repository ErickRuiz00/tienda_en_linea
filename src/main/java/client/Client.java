package client;

import common.Constants;
import common.Product;
import shopping_cart.ShoppingCart;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    public Socket client;
    public ShoppingCart cart;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public Client(String host, int port) {
        try {
            this.client = new Socket(host, port);
            this.cart = new ShoppingCart();

            this.oos = new ObjectOutputStream(client.getOutputStream());
            this.ois = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println("Cannot start socket " + e);
        }
    }

    public ArrayList<Product> listProducts() {
        try {
            oos.writeObject(Constants.LIST_PRODUCTS);
            oos.flush();

            ArrayList<Product> products = (ArrayList<Product>)ois.readObject();

            return products != null ? products : new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Exception");
            return new ArrayList<>();
        }
    }

    public void addProduct(Product product) {
        try {
            oos.writeObject(Constants.ADD_PRODUCT);
            oos.flush();
            Thread.sleep(2000);

        } catch (Exception e) {
        }
    }

    public void reduceProduct(Product product) {
        try {
            oos.writeObject(Constants.REDUCE_PRODUCT);
            oos.flush();
        } catch (Exception e) {
        }
    }

    public void removeProduct(Product product) {
        try {
            oos.writeObject(Constants.REMOVE_PRODUCT);
            oos.flush();
            Thread.sleep(2000);

        } catch (Exception e) {
        }
    }

    public void clearCart(Product product) {
        try {
            oos.writeObject(Constants.CLEAR);
            oos.flush();
            Thread.sleep(2000);

        } catch (Exception e) {
        }
    }

    public void goToCheckout(Product product) {
        try {
            oos.writeObject(Constants.CHECKOUT);
            oos.flush();
            Thread.sleep(2000);

        } catch (Exception e) {
        }
    }
}
