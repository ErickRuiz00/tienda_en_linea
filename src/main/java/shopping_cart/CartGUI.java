package shopping_cart;

import common.Product;
import common.Utils;
import java.awt.*;
import javax.swing.*;

public class CartGUI {

    private ShoppingCart shoppingCart;
    private JFrame frame;
    private JPanel tablePanel;
    private JLabel totalLabel;

    public CartGUI(ShoppingCart cart) {
        this.shoppingCart = cart;
        drawFrame();
        drawTable();
        frame.setVisible(true);
    }

    private void drawFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout());
        final double total = shoppingCart.getTotal();
        totalLabel = new JLabel(Utils.formatPrice(total));
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        frame.add(totalLabel, BorderLayout.SOUTH);

        tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(tablePanel);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    private void drawTable() {
        tablePanel.removeAll();
        tablePanel.add(cartHeader());

        for (CartItem item : shoppingCart.getProductList().values()) {
            tablePanel.add(cartRow(item));
        }

        tablePanel.revalidate();
        tablePanel.repaint();
    }

    private JPanel cartHeader() {
        JPanel header = new JPanel(new GridLayout(1, 5));

        header.add(new JLabel("Producto"));
        header.add(new JLabel("Cantidad"));
        header.add(new JLabel("Precio"));
        header.add(new JLabel("Total"));
        header.add(new JLabel(""));

        return header;
    }

    private JPanel cartRow(CartItem item) {
        JPanel row = new JPanel(new GridLayout(1, 5));

        final Product product = item.getProduct();
        final int quantity = item.getQuantity();
        final double price = product.getPrice();
        final double total = quantity * price;

        row.add(new JLabel(product.getName()));
        row.add(new JLabel(String.valueOf(quantity)));
        row.add(new JLabel(Utils.formatPrice(price)));
        row.add(new JLabel(Utils.formatPrice(total)));

        JPanel actions = new JPanel();
        JButton addBtn = new JButton("+");
        JButton reduceBtn = new JButton("-");
        JButton removeBtn = new JButton("Eliminar");
        
        actions.add(addBtn);
        actions.add(reduceBtn);
        actions.add(removeBtn);
        
        return row;
    }
}
