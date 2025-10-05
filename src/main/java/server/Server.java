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
import java.io.InputStream;
import java.lang.reflect.Type;

public class Server {
    static private ArrayList<ProductStock> stock;
    
    public static void main(String[] args) {
        try {
            int pto = 8000;
            ServerSocket server = new ServerSocket(pto);
            System.out.println("Servidor iniciado en el puerto" + pto);
            
            loadProducts();
            
            for(;;) {
                Socket client = server.accept();

                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                
                while (true) {    
                    String msg = bufferReader.readLine();
                    if (msg == null) continue;
                    
                    switch (msg) {
                        case Constants.LIST_PRODUCTS:
                            System.out.println("Listing Products");
                            break;
                        case Constants.ADD_PRODUCT:
                            System.out.println("Checking Stock");
                            break;
                        case Constants.REDUCE_PRODUCT:
                            System.out.println("Updating Stock");
                            break;
                        case Constants.REMOVE_PRODUCT:
                            System.out.println("Deleting Product");
                            break;
                        case Constants.CLEAR:
                            System.out.println("Cleaning Cart");
                            break;
                        case Constants.CHECKOUT:
                            System.out.println("Go to Checkout");
                            break;
                        default:
                            System.out.println("Solicitud no valida");
                    }
                    printWriter.flush();
                }
                
            }
        } catch (Exception e) {
            System.out.println("Something went wrong in server");
            e.printStackTrace();
        }
    }
    
    static private void loadProducts() {
        try {
            Gson gson = new Gson();

            InputStream is = Server.class.getResourceAsStream("/products.json");
            Type listType = new TypeToken<ArrayList<ProductStock>>() {
            }.getType();

            stock = gson.fromJson(new InputStreamReader(is), listType);
        } catch (Exception e) {
        }
    }
}
