package client;

import common.Product;
import common.Utils;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import shopping_cart.*;

public class ClientGUI {

    private final JFrame frame;
    private final JTextField searchField;
    private final JButton btnSearch, btnViewCart, btnCheckout;
    private CartGUI cartDialog;
    private ShoppingCart shoppingCart;
    private Client client;

    private JPanel productPanel;

    public ClientGUI(ArrayList<Product> products, Client client) {
        shoppingCart = new ShoppingCart();
        this.client = client;

        frame = new JFrame("Tienda en línea");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        // --- Top panel: búsqueda y acciones ---
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(20);
        btnSearch = new JButton("Buscar");
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnViewCart = new JButton("Ver carrito");
        btnCheckout = new JButton("Finalizar compra");
        actionPanel.add(btnViewCart);
        actionPanel.add(btnCheckout);

        topPanel.add(searchPanel);
        topPanel.add(actionPanel);
        frame.add(topPanel, BorderLayout.NORTH);

        productPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(productPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Se llama para el dibujado inicial
        drawProductTable(products);

        btnViewCart.addActionListener(l -> {
            if (cartDialog == null || !cartDialog.isDisplayable()) {
                cartDialog = new CartGUI(shoppingCart, this.client);
            } else {
                cartDialog.requestFocus();
                cartDialog.toFront();
            }
        });

        btnSearch.addActionListener(l -> {
            String term = searchField.getText();
            if (term == null) {
                term = "";
            }

            final ArrayList<Product> matches = client.searchProduct(term.trim());

            drawProductTable(matches);
        });

        btnCheckout.addActionListener(l -> {
            if (shoppingCart.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Tu carrito esta vacio", "Carrito vacio", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder confirmationMessage = new StringBuilder("¿Deseas finalizar tu compra?\n\nResumen:\n");
            for (CartItem item : shoppingCart.getProductList().values()) {
                final Product p = item.getProduct();
                confirmationMessage.append("- ").append(item.getQuantity() + "ud(s). ").append(p.getName()).append(" (" + Utils.formatPrice(p.getPrice()) + "c/u)\n");
            }
            confirmationMessage.append("\nTOTAL: ").append(Utils.formatPrice(shoppingCart.getTotal()));

            int response = JOptionPane.showConfirmDialog(
                    frame,
                    confirmationMessage.toString(),
                    "Confirmar Compra",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                boolean purchaseSuccess = client.goToCheckout();

                if (purchaseSuccess) {
                    JOptionPane.showMessageDialog(frame, "¡Gracias por tu compra!", "Compra Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    shoppingCart.clearCart(); 
                } else {
                    JOptionPane.showMessageDialog(frame, "Hubo un error al procesar tu compra. Es posible que el stock haya cambiado.", "Error en la Compra", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    private void drawProductTable(ArrayList<Product> productList) {
        productPanel.removeAll();
        productPanel.setLayout(new GridBagLayout());
        GridBagConstraints constrains = new GridBagConstraints();

        constrains.fill = GridBagConstraints.HORIZONTAL;
        constrains.weighty = 1;
        constrains.insets = new Insets(5, 10, 5, 10);

        Font headerFont = new Font("SansSerif", Font.BOLD, 16);

        String[] headers = {"NOMBRE", "TIPO", "PRECIO", ""};
        for (int i = 0; i < headers.length; i++) {
            constrains.gridx = i;
            constrains.gridy = 0;
            constrains.weightx = (i == 3) ? 0.1 : 0.3;
            JLabel label = new JLabel(headers[i]);
            label.setFont(headerFont);
            productPanel.add(label, constrains);
        }

        constrains.gridx = 0;
        constrains.gridy = 1;
        constrains.gridwidth = 4;
        JSeparator sep = new JSeparator();
        productPanel.add(sep, constrains);
        constrains.gridwidth = 1;

        int rowIndex = 2;
        for (Product p : productList) {
            addProductRow(productPanel, p, rowIndex++);
        }

        productPanel.revalidate();
        productPanel.repaint();
    }

    private void addProductRow(JPanel panel, Product product, int row) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 10, 5, 10);
        c.weighty = 1;

        c.gridx = 0;
        c.gridy = row;
        c.weightx = 0.3;
        panel.add(new JLabel(product.getName()), c);

        c.gridx = 1;
        c.weightx = 0.3;
        panel.add(new JLabel(product.getType()), c);

        c.gridx = 2;
        c.weightx = 0.3;
        final double price = product.getPrice();
        panel.add(new JLabel(Utils.formatPrice(price)), c);

        c.gridx = 3;
        c.weightx = 0.1;
        JButton btn = new JButton("Añadir");

        btn.addActionListener(l -> {
            if (client.addProduct(product)) {
                shoppingCart.addProduct(product);
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "No se pudo agregar el producto. ¡Sin stock Disponible!",
                        "Producto no disponible",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        btn.setPreferredSize(new Dimension(100, 25));
        panel.add(btn, c);
    }
}
