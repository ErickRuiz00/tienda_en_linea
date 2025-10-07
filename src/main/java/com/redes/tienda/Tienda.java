package com.redes.tienda;

import common.Product;
import client.ClientGUI;
import client.Client;
import java.util.ArrayList;

public class Tienda {
  public static void main(String[] args) {      
      Client client = new Client("127.0.0.1", 8000);
      

      final ArrayList<Product> productList = client.listProducts();

      new ClientGUI(productList);
  }  
}
