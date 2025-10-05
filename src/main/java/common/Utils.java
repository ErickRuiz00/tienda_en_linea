package common;

public class Utils {
    public static String formatPrice(double price) {
        return String.format("$%.2f", price);
    }
}
