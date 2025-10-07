package shopping_cart;

import client.Client;
import common.Product;
import common.Utils;
import java.awt.*;
import javax.swing.*;

public class CartGUI extends JFrame {

    private final ShoppingCart shoppingCart;
    private final JPanel tablePanel;
    private final JLabel totalLabel;
    private final Client client;

    public CartGUI(ShoppingCart cart, Client client) {
        this.shoppingCart = cart;
        this.client = client;

        setTitle("Carrito de Compras");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        southPanel.add(new JLabel("Total:"));
        totalLabel = new JLabel();
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        southPanel.add(totalLabel);
        this.add(southPanel, BorderLayout.SOUTH);

        tablePanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(tablePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(scrollPane, BorderLayout.CENTER);

        refreshUI();

        setVisible(true);
    }

    private void drawTable() {
        tablePanel.removeAll();
        tablePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridy = 0;
        addCartHeader(tablePanel, gbc);

        int rowIndex = 1;
        for (CartItem item : shoppingCart.getProductList().values()) {
            gbc.gridy = rowIndex;
            addProductRow(tablePanel, item, gbc);
            rowIndex++;
        }
        
        gbc.gridy = rowIndex;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.VERTICAL;
        tablePanel.add(new JPanel(), gbc); 
    }
    
    private void addCartHeader(JPanel panel, GridBagConstraints gbc) {
        String[] headers = {"Producto", "Cantidad", "Precio Unitario", "Subtotal", "Acciones"};
        Font headerFont = new Font("SansSerif", Font.BOLD, 14);

        for (int i = 0; i < headers.length; i++) {
            gbc.gridx = i;
            gbc.weightx = (i == 0) ? 0.40 : (i == 4) ? 0.25 : 0.15;
            
            JLabel label = new JLabel(headers[i], SwingConstants.CENTER);
            label.setFont(headerFont);
            panel.add(label, gbc);
        }
    }

    private void addProductRow(JPanel panel, CartItem item, GridBagConstraints gbc) {
        final Product product = item.getProduct();
        final int quantity = item.getQuantity();
        // Nombre del Producto
        gbc.gridx = 0;
        panel.add(new JLabel(product.getName()), gbc);

        // Cantidad
        gbc.gridx = 1;
        panel.add(new JLabel(String.valueOf(quantity), SwingConstants.CENTER), gbc);

        // Precio
        gbc.gridx = 2;
        panel.add(new JLabel(Utils.formatPrice(product.getPrice()), SwingConstants.CENTER), gbc);

        // Subtotal
        gbc.gridx = 3;
        final double subtotal = product.getPrice() * quantity;
        panel.add(new JLabel(Utils.formatPrice(subtotal), SwingConstants.CENTER), gbc);

        // Botones de Acción
        gbc.gridx = 4;
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        JButton addBtn = new JButton("+");
        JButton reduceBtn = new JButton("-");
        JButton removeBtn = new JButton("X");

        Dimension btnSize = new Dimension(45, 25);
        addBtn.setPreferredSize(btnSize);
        reduceBtn.setPreferredSize(btnSize);
        removeBtn.setPreferredSize(btnSize);
        removeBtn.setForeground(Color.RED);

        // --- Action Listeners para los botones ---
        addBtn.addActionListener(l -> {
            if (client.addProduct(product)) {
                shoppingCart.addProduct(product);
                refreshUI();
            } else {
                showStockError();
            }
        });

        reduceBtn.addActionListener(l -> {
            if (client.reduceProduct(product)) {
                shoppingCart.reduceProductQuantity(product); 
                refreshUI();
            }
        });

        removeBtn.addActionListener(l -> {
            if (client.removeProduct(item)) {
                shoppingCart.removeProduct(product);
                refreshUI();
            }
        });

        actionsPanel.add(reduceBtn);
        actionsPanel.add(addBtn);
        actionsPanel.add(removeBtn);
        panel.add(actionsPanel, gbc);
    }
    
    private void displayEmptyCartMessage() {
        tablePanel.removeAll();
        tablePanel.setLayout(new BorderLayout());
        JLabel label = new JLabel("Tu carrito está vacío", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.ITALIC, 20));
        label.setForeground(Color.GRAY);
        tablePanel.add(label, BorderLayout.CENTER);
    }

    public void refreshUI() {
        if (shoppingCart.isEmpty()) {
            displayEmptyCartMessage();
        } else {
            drawTable();
        }
        totalLabel.setText(Utils.formatPrice(shoppingCart.getTotal()));
        
        tablePanel.revalidate();
        tablePanel.repaint();
    }
    
    private void showStockError() {
        JOptionPane.showMessageDialog(
                this,
                "No se pudo agregar el producto. ¡Sin stock disponible!",
                "Producto no disponible",
                JOptionPane.WARNING_MESSAGE
        );
    }
}