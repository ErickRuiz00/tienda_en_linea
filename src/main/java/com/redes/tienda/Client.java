package com.redes.tienda;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    public Socket client;
    public ShoppingCart cart;

    private PrintWriter pw;
    private BufferedReader bf;

    public Client(String host, int port) {
        try {
            this.client = new Socket(host, port);
            this.cart = new ShoppingCart();

            this.pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
            this.bf = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (Exception e) {
            System.out.println("Cannot start socket " + e);
        }
    }

    public ArrayList<Product> listProducts() {
        try {
            pw.println(Constants.LIST_PRODUCTS);
            pw.flush();

            Thread.sleep(2000);

            return new ArrayList<Product>();
        } catch (Exception e) {
            System.out.println("Exception");
            return new ArrayList<Product>();
        }
    }

    public void addProduct(Product product) {
        try {
            pw.println(Constants.ADD_PRODUCT);
            pw.flush();
            Thread.sleep(2000);

        } catch (Exception e) {
        }
    }

    public void reduceProduct(Product product) {
        try {
            pw.println(Constants.REDUCE_PRODUCT);
            pw.flush();
        } catch (Exception e) {
        }
    }

    public void removeProduct(Product product) {
        try {
            pw.println(Constants.REMOVE_PRODUCT);
            pw.flush();
            Thread.sleep(2000);

        } catch (Exception e) {
        }
    }

    public void clearCart(Product product) {
        try {
            pw.println(Constants.CLEAR);
            pw.flush();
            Thread.sleep(2000);

        } catch (Exception e) {
        }
    }

    public void goToCheckout(Product product) {
        try {
            pw.println(Constants.CHECKOUT);
            pw.flush();
            Thread.sleep(2000);

        } catch (Exception e) {
        }
    }
}
