package com.redes.tienda;

import common.Product;
import client.ClientGUI;
import client.Client;
import java.util.ArrayList;

public class Tienda {
  public static void main(String[] args) {      
      Client client = new Client("127.0.0.1", 8000);
      
      final Product dummyProduct = new Product(0, "Product", "Type", 0);
      final Product dummyProduct2 = new Product(0, "Product222222", "Type2222", 10000);

      client.listProducts();
      client.addProduct(dummyProduct);
      client.reduceProduct(dummyProduct);
      client.removeProduct(dummyProduct);
      client.clearCart(dummyProduct);
      client.goToCheckout(dummyProduct);
      
      final ArrayList<Product> productList = new ArrayList<>();
      productList.add(dummyProduct);
      productList.add(dummyProduct2);
      productList.add(dummyProduct);
      productList.add(dummyProduct2);
      productList.add(dummyProduct);
            productList.add(dummyProduct);
      productList.add(dummyProduct);
      productList.add(dummyProduct);
      productList.add(dummyProduct);
      productList.add(dummyProduct);
      productList.add(dummyProduct);
      productList.add(dummyProduct);
      productList.add(dummyProduct);
      productList.add(dummyProduct);
      productList.add(dummyProduct);
      productList.add(dummyProduct);
      productList.add(dummyProduct);
      productList.add(dummyProduct);
      productList.add(dummyProduct);

      

      new ClientGUI(productList);
  }  
}
