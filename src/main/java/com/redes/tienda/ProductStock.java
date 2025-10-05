package com.redes.tienda;

import java.io.Serializable;

public class ProductStock implements Serializable {

    private int stock;
    private Product product;

    public ProductStock(int stock, Product product) {
        this.stock = stock;
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public int getStock() {
        return stock;
    }

    public void decreaseStock(int quantity) {
        if (quantity > stock) {
            stock = 0;
        } else {
            stock -= quantity;
        }
    }
}
