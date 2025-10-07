package common;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Product implements Serializable {

    @SerializedName("product_id")
    private int productId;
    private String name;
    private String type;
    private double price;
    
    public Product(){}

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

    @Override
    public String toString() {
        return productId + " " + name + " " + type + " " + price; 
    }
    
    
}
