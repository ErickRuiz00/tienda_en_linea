package server;

import common.Product;
import java.io.Serializable;

public class ProductStock implements Serializable {

    private int stock;
    private Product product;

    public ProductStock(){};
    
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

    public void decreaseStock() {
        if (stock > 0) stock--;
    }
    
    public void increaseStock() {
        stock++;
    }
    
    public void increaseStock(int quantity) {
        stock += quantity;
    }
    
    public Boolean isAvailable() {
        return stock > 0;
    }    
}
