package com.form;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

public class FormOrder extends JPanel {

    private JTextField searchField;
    private JComboBox<String> categoryCombo;
    private JPanel productPanel;
    private JTable orderTable;
    private JTable deliveryTable;
    private DefaultTableModel orderTableModel;
    private DefaultTableModel deliveryTableModel;

    // Form fields
    
    private JTextArea addressTextArea;
    private JTextField totalAmountField;
    private JTextField discountField;

    private JTextField customerPayField;
    private JTextField changeField;
    private JComboBox<String> statusCombo;
    private DefaultTableModel productOrderModel;

    // Product data
    private String[][] products = {
        {"Cà Phê Đen Đá", "24.500 đ", "coffee1.jpg"},
        {"Cà Phê Sữa Đá", "24.500 đ", "coffee2.jpg"},
        {"Bạc Sỉu", "20.300 đ", "coffee3.jpg"},
        {"Caramel Macchiato", "54.000 đ", "coffee4.jpg"},
        {"Trà Đào Cam Sả", "51.000 đ", "tea1.jpg"},
        {"Trà Hạt Sen", "51.000 đ", "tea2.jpg"},
        {"Trà Đen Macchiato", "49.000 đ", "tea3.jpg"},
        {"Hồng Trà Sữa", "54.000 đ", "tea4.jpg"},
        {"Hồng Trà Sữa", "54.000 đ", "tea4.jpg"},
        {"Trà Hạt Sen", "51.000 đ", "tea2.jpg"}
            
    };

    public FormOrder() {
        initializeComponents();
        setupLayout();
        populateData();
        setupEventHandlers();
        statusCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) statusCombo.getSelectedItem();
                if ("Chuyển khoản".equalsIgnoreCase(selected)) {
                    // Lấy tổng tiền và gán cho customerPayField
                    String totalStr = totalAmountField.getText().replace(".", "").replace(" đ", "");
                    try {
                        int total = Integer.parseInt(totalStr);
                        String formatted = String.format("%,d đ", total).replace(",", ".");
                        customerPayField.setText(formatted);
                        calculateChange(); // Tính tiền trả lại
                    } catch (NumberFormatException ex) {
                        System.err.println("Lỗi parse tổng tiền: " + ex.getMessage());
                    }
                }
            }
        });
    }

    private void initializeComponents() {
        // Search components
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Tìm");
        categoryCombo = new JComboBox<>(new String[]{"TẤT CẢ", "Cà phê", "Trà", "Nước ép"});

        // Product panel
        productPanel = new JPanel(new GridLayout(2, 4, 10, 10));

        // Tables
        String[] orderColumns = {"Mã HĐ", "Người tạo", "Khách hàng", "TG Tạo", "Trạng Thái", "Ghi chú"};
        orderTableModel = new DefaultTableModel(orderColumns, 0);
        orderTable = new JTable(orderTableModel);

        String[] deliveryColumns = {"Mã HĐ", "Người tạo", "Khách hàng", "TG Tạo", "Trạng Thái", "Ghi chú"};
        deliveryTableModel = new DefaultTableModel(deliveryColumns, 0);
        deliveryTable = new JTable(deliveryTableModel);

        // Form fields
        totalAmountField = new JTextField("0 đ");
        discountField = new JTextField("0 đ");
        customerPayField = new JTextField("");
        changeField = new JTextField("0 đ");
        statusCombo = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản"});

        // Make amount fields read-only
        totalAmountField.setEditable(false);
        changeField.setEditable(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Left panel - Products and Orders
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(700, 600));

        // Product search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tên sản phẩm:"));
        searchPanel.add(searchField);
        searchPanel.add(new JButton("Tìm"));
        searchPanel.add(new JLabel("Loại sản phẩm:"));
        searchPanel.add(categoryCombo);

        // Product display panel
        JPanel productDisplayPanel = new JPanel(new BorderLayout());
        productDisplayPanel.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));
        createProductGrid();
        JScrollPane productScrollPane = new JScrollPane(productPanel);
        productScrollPane.setPreferredSize(new Dimension(680, 300));
        productDisplayPanel.add(productScrollPane, BorderLayout.CENTER);

        // Orders panel
        JPanel ordersPanel = new JPanel(new BorderLayout());

        // Current orders
        JPanel currentOrdersPanel = new JPanel(new BorderLayout());
        currentOrdersPanel.setBorder(BorderFactory.createTitledBorder("Hóa đơn chờ:"));
        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        orderScrollPane.setPreferredSize(new Dimension(680, 350));
        currentOrdersPanel.add(orderScrollPane, BorderLayout.CENTER);

        ordersPanel.add(currentOrdersPanel, BorderLayout.NORTH);

        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(productDisplayPanel, BorderLayout.CENTER);
        leftPanel.add(ordersPanel, BorderLayout.SOUTH);

        // Right panel - Order details and payment
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(650, 600));

        // Order details panel
        JPanel orderDetailsPanel = createOrderDetailsPanel();

        // Payment panel
        JPanel paymentPanel = createPaymentPanel();

        rightPanel.add(orderDetailsPanel, BorderLayout.CENTER);
        rightPanel.add(paymentPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private void createProductGrid() {
        productPanel.removeAll();

        for (String[] product : products) {
            JPanel productCard = createProductCard(product[0], product[1], product[2]);
            productPanel.add(productCard);
        }

        productPanel.revalidate();
        productPanel.repaint();
    }

    private JPanel createProductCard(String name, String price, String imageFile) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createRaisedBevelBorder());
        card.setPreferredSize(new Dimension(150, 120));
        card.setBackground(new Color(139, 69, 19)); // Brown color

        // Product image placeholder
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(120, 80));
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setOpaque(true);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/com/icon/" + imageFile));
            Image img = icon.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
            imageLabel.setText(null);
        } catch (Exception e) {
            imageLabel.setIcon(null);
            imageLabel.setText("Không tìm thấy ảnh");
        }

        // Product info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(139, 69, 19));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 11));

        JLabel priceLabel = new JLabel(price);
        priceLabel.setForeground(Color.YELLOW);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 10));

        infoPanel.add(nameLabel, BorderLayout.NORTH);
        infoPanel.add(priceLabel, BorderLayout.SOUTH);

        card.add(imageLabel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        // Add click listener
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addProductToOrder(name, price);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createLoweredBevelBorder());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createRaisedBevelBorder());
            }
        });

        return card;
    }

    private JPanel createOrderDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Chi tiết hóa đơn"));

        // Top section with customer info
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Right column
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0;
        topPanel.add(new JLabel("Tổng tiền:"), gbc);
        gbc.gridx = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        topPanel.add(totalAmountField, gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        topPanel.add(new JLabel("Tiền khách trả:"), gbc);
        gbc.gridx = 4;
        topPanel.add(customerPayField, gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        topPanel.add(new JLabel("Tiền thừa:"), gbc);
        gbc.gridx = 4;
        topPanel.add(changeField, gbc);

        gbc.gridx = 3;
        gbc.gridy = 4;
        topPanel.add(new JLabel("Loại TT:"), gbc);
        gbc.gridx = 4;
        topPanel.add(statusCombo, gbc);

        gbc.gridx = 3;
        gbc.gridy = 5;
        topPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 4;
        topPanel.add(new JLabel("Chờ order"), gbc);

        // Product list table
        String[] columns = {"Mã SP", "Tên SP", "Đơn giá", "Số lượng", "Thành tiền", "Ghi chú"};
        productOrderModel = new DefaultTableModel(columns, 0);
        JTable productTable = new JTable(productOrderModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 350));

        panel.add(tableScrollPane, BorderLayout.NORTH);
        panel.add(topPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createEtchedBorder());

        JButton cancelButton = new JButton("Hủy");
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.setBackground(Color.LIGHT_GRAY);

        JButton paymentButton = new JButton("Thanh toán");
        paymentButton.setPreferredSize(new Dimension(120, 50));
        paymentButton.setBackground(Color.RED);
        paymentButton.setForeground(Color.WHITE);
        paymentButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Add action listeners
        cancelButton.addActionListener(e -> {
            Component parentComponent = SwingUtilities.getWindowAncestor(this);
            int result = JOptionPane.showConfirmDialog(parentComponent,
                    "Bạn có chắc chắn muốn hủy hóa đơn này?",
                    "Xác nhận hủy",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                clearForm();
            }
        });

        paymentButton.addActionListener(e -> {
            Component parentComponent = SwingUtilities.getWindowAncestor(this);
            JOptionPane.showMessageDialog(parentComponent,
                    "Thanh toán thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        });

        panel.add(cancelButton);
        panel.add(paymentButton);

        return panel;
    }

    private void populateData() {
        orderTableModel.addRow(new Object[]{"HD001", "Nguyễn Huy Hoàng", "", "15:25 20-04-...", "Chờ order", ""});
    }

    private void clearForm() {
        totalAmountField.setText("0 đ");
        customerPayField.setText("");
        changeField.setText("0 đ");
        productOrderModel.setRowCount(0);
    }

    private void setupEventHandlers() {
        // Search functionality
        searchField.addActionListener(e -> performSearch());
        categoryCombo.addActionListener(e -> filterByCategory());

        // Customer payment calculation
        customerPayField.addActionListener(e -> calculateChange());
        customerPayField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                calculateChange();
            }
        });
    }

    private void performSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            createProductGrid();
            return;
        }

        productPanel.removeAll();
        for (String[] product : products) {
            if (product[0].toLowerCase().contains(searchText)) {
                JPanel productCard = createProductCard(product[0], product[1], product[2]);
                productPanel.add(productCard);
            }
        }
        productPanel.revalidate();
        productPanel.repaint();
    }

    private void filterByCategory() {
        String selectedCategory = (String) categoryCombo.getSelectedItem();
        if ("TẤT CẢ".equals(selectedCategory)) {
            createProductGrid();
            return;
        }

        productPanel.removeAll();
        for (String[] product : products) {
            boolean shouldShow = false;
            if ("Cà phê".equals(selectedCategory) && product[0].contains("Cà Phê")) {
                shouldShow = true;
            } else if ("Trà".equals(selectedCategory) && product[0].contains("Trà")) {
                shouldShow = true;
            }

            if (shouldShow) {
                JPanel productCard = createProductCard(product[0], product[1], product[2]);
                productPanel.add(productCard);
            }
        }
        productPanel.revalidate();
        productPanel.repaint();
    }

    private void addProductToOrder(String productName, String priceRange) {
        // Extract first price from range (e.g., "24.500 đ" from "24.500 đ - 35.000 đ")
        String price = priceRange.split(" - ")[0];
        String numericPrice = price.replace(".", "").replace(" đ", "");

        // Check if product already exists in order
        for (int i = 0; i < productOrderModel.getRowCount(); i++) {
            if (productOrderModel.getValueAt(i, 1).equals(productName)) {
                Object value = productOrderModel.getValueAt(i, 3);
                int currentQty = (value instanceof Integer)
                        ? (Integer) value
                        : Integer.parseInt(value.toString());
                productOrderModel.setValueAt(currentQty + 1, i, 3);
                updateRowTotal(i);
                calculateTotalAmount();
                return;
            }
        }

        // Add new product
        String productCode = "SP" + String.format("%03d", productOrderModel.getRowCount() + 1);
        productOrderModel.addRow(new Object[]{
            productCode,
            productName,
            price,
            1,
            price,
            ""
        });

        calculateTotalAmount();

        JOptionPane.showMessageDialog(this,
                "Đã thêm " + productName + " vào hóa đơn",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateRowTotal(int row) {
        String priceStr = (String) productOrderModel.getValueAt(row, 2);
        int quantity = (Integer) productOrderModel.getValueAt(row, 3);
        int price = Integer.parseInt(priceStr.replace(".", "").replace(" đ", ""));
        int total = price * quantity;

        String totalStr = String.format("%,d đ", total).replace(",", ".");
        productOrderModel.setValueAt(totalStr, row, 4);
    }

    private void calculateTotalAmount() {
        int total = 0;
        for (int i = 0; i < productOrderModel.getRowCount(); i++) {
            String totalStr = (String) productOrderModel.getValueAt(i, 4);
            int amount = Integer.parseInt(totalStr.replace(".", "").replace(" đ", ""));
            total += amount;
        }

        String totalFormatted = String.format("%,d đ", total).replace(",", ".");
        totalAmountField.setText(totalFormatted);
    }

    private void calculateChange() {
        try {
            String totalStr = totalAmountField.getText().replace(".", "").replace(" đ", "");
            String paidStr = customerPayField.getText().replace(".", "").replace(" đ", "");

            if (paidStr.isEmpty()) {
                changeField.setText("0 đ");
                return;
            }

            int total = Integer.parseInt(totalStr);
            int paid = Integer.parseInt(paidStr);
            int change = paid - total;

            String changeFormatted = String.format("%,d đ", change).replace(",", ".");
            changeField.setText(changeFormatted);

            if (change < 0) {
                changeField.setForeground(Color.RED);
            } else {
                changeField.setForeground(Color.BLACK);
            }
        } catch (NumberFormatException e) {
            changeField.setText("0 đ");
        }
    }

    // Method để test khi chạy độc lập
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            
//            JFrame frame = new JFrame("Test FormOrder");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.add(new FormOrder());
//            frame.setSize(1400, 800);
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
}
