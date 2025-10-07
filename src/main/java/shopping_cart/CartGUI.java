package shopping_cart;

import client.Client;
import common.Product;
import common.Utils;
import java.awt.*;
import javax.swing.*;

public class CartGUI extends JFrame {

    private final ShoppingCart shoppingCart;
    private JPanel tablePanel;
    private JLabel totalLabel;
    private Client client;

    public CartGUI(ShoppingCart cart, Client client) {
        this.shoppingCart = cart;
        this.client = client;
        drawFrame();
        if (cart.getProductList().isEmpty()) {
            displayEmptyAlert();
        } else {
            drawTable();
        }

        setVisible(true);
    }

    private void drawFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        final double total = shoppingCart.getTotal();
        totalLabel = new JLabel(Utils.formatPrice(total));
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        this.add(totalLabel, BorderLayout.SOUTH);

        tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(tablePanel);
        this.add(scrollPane, BorderLayout.CENTER);
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

        JPanel actions = new JPanel(new GridLayout(3, 1));
        JButton addBtn = new JButton("+");
        JButton reduceBtn = new JButton("-");
        JButton removeBtn = new JButton("Eliminar");

        actions.add(addBtn);
        actions.add(reduceBtn);
        actions.add(removeBtn);

        addBtn.addActionListener(l -> {
            if (client.addProduct(product)) {
                shoppingCart.addProduct(product);
                refreshTable();
                return;
            }

            JOptionPane.showMessageDialog(
                    null,
                    "No se pudo agregar el producto. ¡Sin stock Disponible!",
                    "Producto no disponible",
                    JOptionPane.WARNING_MESSAGE
            );
        });

        reduceBtn.addActionListener(l -> {
            if (client.reduceProduct(product)) {
                shoppingCart.reduceProductQuantity(product);
                refreshTable();
            }
        });

        removeBtn.addActionListener(l -> {
            if (client.removeProduct(item)) {
                shoppingCart.removeProduct(product);
                refreshTable();
            }
        });

        row.add(actions);

        return row;
    }

    private void displayEmptyAlert() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Carrito vacio", SwingConstants.CENTER);

        label.setFont(new Font("SansSerif", Font.PLAIN, 24));
        label.setForeground(Color.LIGHT_GRAY);

        panel.add(label, BorderLayout.CENTER);

        this.add(panel, BorderLayout.CENTER);
    }

    public void refreshTable() {
        if (shoppingCart.getProductList().isEmpty()) {
            tablePanel.removeAll();
            displayEmptyAlert();
        } else {
            drawTable();
        }
        totalLabel.setText(Utils.formatPrice(shoppingCart.getTotal()));
        tablePanel.revalidate();
        tablePanel.repaint();
    }
}
