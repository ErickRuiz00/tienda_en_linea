package com.redes.tienda;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class ClientGUI {

    private final JFrame frame;
    private final JTextField searchField;
    private final JButton btnSearch, btnViewCart, btnCheckout;

    public ClientGUI(ArrayList<Product> products) {
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

        drawProductTable(products);

        frame.setVisible(true);
    }

    private void drawProductTable(ArrayList<Product> productList) {
        JPanel tablePanel = new JPanel(new GridBagLayout());
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
            tablePanel.add(label, constrains);
        }

        constrains.gridx = 0;
        constrains.gridy = 1;
        constrains.gridwidth = 4;
        JSeparator sep = new JSeparator();
        tablePanel.add(sep, constrains);
        constrains.gridwidth = 1;

        int rowIndex = 2;
        for (Product p : productList) {
            addProductRow(tablePanel, p, rowIndex++);
        }

        JScrollPane scrollPane = new JScrollPane(tablePanel);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    private void addProductRow(JPanel tablePanel, Product product, int row) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 10, 5, 10);
        c.weighty = 1;

        c.gridx = 0;
        c.gridy = row;
        c.weightx = 0.3;
        tablePanel.add(new JLabel(product.getName()), c);

        c.gridx = 1;
        c.weightx = 0.3;
        tablePanel.add(new JLabel(product.getType()), c);

        c.gridx = 2;
        c.weightx = 0.3;
        tablePanel.add(new JLabel(product.getFormattedPrice()), c);

        c.gridx = 3;
        c.weightx = 0.1;
        JButton btn = new JButton("Añadir");
        btn.setPreferredSize(new Dimension(100, 25)); 
        tablePanel.add(btn, c);
    }

    
}
