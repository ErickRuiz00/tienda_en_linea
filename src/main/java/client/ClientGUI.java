package client;

import common.Product;
import common.Utils;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import shopping_cart.CartGUI;
import shopping_cart.CartItem;
import shopping_cart.ShoppingCart;

public class ClientGUI {

    private final JFrame frame;
    private final JTextField searchField;
    private final JButton btnSearch, btnViewCart, btnCheckout;
    private CartGUI cartDialog;
    private final ShoppingCart shoppingCart;
    private final Client client;
    private final JPanel productPanel;

    public ClientGUI(ArrayList<Product> products, Client client) {
        this.shoppingCart = new ShoppingCart();
        this.client = client;

        frame = new JFrame("Tienda en Línea");
        
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.closeConnection();
                super.windowClosing(e); 
            }
        });
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel searchAndActionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));

        searchField = new JTextField(20);
        searchField.putClientProperty("JTextField.placeholderText", "Buscar producto...");
        btnSearch = new JButton("Buscar");

        btnViewCart = new JButton("🛒 Ver Carrito");
        btnCheckout = new JButton("✔ Finalizar Compra");

        searchAndActionsPanel.add(new JLabel("Buscar:"));
        searchAndActionsPanel.add(searchField);
        searchAndActionsPanel.add(btnSearch);
        searchAndActionsPanel.add(btnViewCart);
        searchAndActionsPanel.add(btnCheckout);

        topPanel.add(searchAndActionsPanel);
        frame.add(topPanel, BorderLayout.NORTH);

        productPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        frame.add(scrollPane, BorderLayout.CENTER);

        setupActionListeners();

        drawProductList(products);
        frame.setVisible(true);
    }

    private void drawProductList(ArrayList<Product> productList) {
        productPanel.removeAll();
        productPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        addHeaderRow(gbc);

        int rowIndex = 1;
        if (productList == null || productList.isEmpty()) {
            displayNoProductsMessage(rowIndex);
        } else {
            for (Product p : productList) {
                gbc.gridy = rowIndex;
                addProductRow(p, rowIndex, gbc);
                rowIndex++;
            }
        }

        gbc.gridy = rowIndex;
        gbc.weighty = 1.0;
        productPanel.add(new JPanel(), gbc);

        productPanel.revalidate();
        productPanel.repaint();
    }

    private void addHeaderRow(GridBagConstraints gbc) {
        String[] headers = {"NOMBRE", "TIPO", "PRECIO", ""};
        Font headerFont = new Font("SansSerif", Font.BOLD, 14);

        for (int i = 0; i < headers.length; i++) {
            gbc.gridx = i;
            gbc.weightx = (i == 0) ? 0.4 : (i == 1) ? 0.3 : (i == 2) ? 0.2 : 0.1;
            JLabel label = new JLabel(headers[i]);
            label.setFont(headerFont);
            productPanel.add(label, gbc);
        }
    }

    private void addProductRow(Product product, int row, GridBagConstraints gbc) {
        // Columna 0: Nombre
        gbc.gridx = 0;
        productPanel.add(new JLabel(product.getName()), gbc);

        // Columna 1: Tipo
        gbc.gridx = 1;
        productPanel.add(new JLabel(product.getType()), gbc);

        // Columna 2: Precio
        gbc.gridx = 2;
        productPanel.add(new JLabel(Utils.formatPrice(product.getPrice())), gbc);

        // Columna 3: Botón
        gbc.gridx = 3;
        JButton addButton = new JButton("Añadir");
        addButton.addActionListener(l -> {
            if (client.addProduct(product)) {
                shoppingCart.addProduct(product);
            } else {
                JOptionPane.showMessageDialog(frame, "No se pudo agregar. ¡Sin stock disponible!", "Producto no disponible", JOptionPane.WARNING_MESSAGE);
            }
        });
        productPanel.add(addButton, gbc);
    }

    private void displayNoProductsMessage(int rowIndex) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = rowIndex;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(20, 10, 20, 10);

        JLabel label = new JLabel("No se encontraron productos", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.ITALIC, 16));
        label.setForeground(Color.GRAY);
        productPanel.add(label, gbc);
    }

    private void setupActionListeners() {
        btnViewCart.addActionListener(l -> {
            if (cartDialog != null) {
                cartDialog.dispose();
            }
            cartDialog = new CartGUI(shoppingCart, this.client);
        });

        btnSearch.addActionListener(l -> {
            String term = searchField.getText();
            ArrayList<Product> matches = client.searchProduct(term.trim());
            drawProductList(matches);
        });

        btnCheckout.addActionListener(l -> {
            if (shoppingCart.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Tu carrito está vacío.", "Carrito Vacío", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder confirmationMessage = new StringBuilder("¿Deseas finalizar tu compra?\n\nResumen:\n");
            for (CartItem item : shoppingCart.getProductList().values()) {
                Product p = item.getProduct();
                int quantity = item.getQuantity();
                confirmationMessage.append("- ").append(quantity).append("x ").append(p.getName())
                        .append(" (").append(Utils.formatPrice(p.getPrice())).append(" c/u)\n");
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
    }
}
