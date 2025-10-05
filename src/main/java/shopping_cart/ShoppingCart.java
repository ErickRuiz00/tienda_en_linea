package shopping_cart;


import common.Product;
import shopping_cart.CartItem;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private Map<Integer, CartItem> productList;
    private double total;

    public ShoppingCart() {
        productList = new HashMap<>();
        total = 0;
    }

    public Map<Integer, CartItem> getProductList() {
        return productList;
    }
        
    public double getTotal() {
        return total;
    }

    public void addProduct(Product product) {
        final int id = product.getProductId();
        
        if (productList.containsKey(id)) {
            CartItem item = productList.get(id);
            int currentQuantity = item.getQuantity();
            item.setQuantity(currentQuantity + 1);
        } else {
            CartItem item = new CartItem(product, 1);
            productList.put(id, item);
        }
        
        total += product.getPrice(); 
    }

    public void reduceProductQuantity(Product product) {
        final int id = product.getProductId();
        final CartItem item = productList.get(id);
        int itemQuantity = item.getQuantity();

        if (itemQuantity == 1) {
            productList.remove(id);
        } else {
            item.setQuantity(itemQuantity - 1);
        }
        
        total -= item.getProduct().getPrice();
    }

    public void removeProduct(Product product) {
        final int id = product.getProductId();
        int quantity = productList.get(id).getQuantity();
        productList.remove(id);
        total -= (quantity * product.getPrice()); 
    }

    public void clearCart() {
        productList.clear();
        total = 0;
    }
}
