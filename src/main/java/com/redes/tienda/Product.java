package com.redes.tienda;

import java.io.Serializable;

public class Product implements Serializable {

    final private int productId;
    final private String name;
    final private String type;
    final private double price;

    public Product(int productId, String name, String type, double price) {
        this.productId = productId;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }
}
