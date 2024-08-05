import javax.swing.*;
import java.util.ArrayList;

public class InventoryManagementSystem extends JFrame {
    // Sample user credentials
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";

    private final ArrayList<Product> products;
    private JTextArea displayArea;
    private JTextField userField;
    private JPasswordField passField;
    private JPanel loginPanel, mainPanel;

    public InventoryManagementSystem() {
        products = new ArrayList<>();
        createLoginPanel();
        createMainPanel();
        switchToLoginPanel();

        setTitle("Inventory Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        userField = new JTextField(20);
        passField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(_ -> authenticate(userField.getText(), new String(passField.getPassword())));

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(userField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passField);
        loginPanel.add(loginButton);
    }

    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        displayArea = new JTextArea(15, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(_ -> addProduct());

        JButton editButton = new JButton("Edit Product");
        editButton.addActionListener(_ -> editProduct());

        JButton deleteButton = new JButton("Delete Product");
        deleteButton.addActionListener(_ -> deleteProduct());

        JButton reportButton = new JButton("Generate Report");
        reportButton.addActionListener(_ -> generateReport());

        mainPanel.add(scrollPane);
        mainPanel.add(addButton);
        mainPanel.add(editButton);
        mainPanel.add(deleteButton);
        mainPanel.add(reportButton);
    }

    private void switchToLoginPanel() {
        setContentPane(loginPanel);
        revalidate();
        repaint();
    }

    private void switchToMainPanel() {
        setContentPane(mainPanel);
        revalidate();
        repaint();
    }

    private void authenticate(String username, String password) {
        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            switchToMainPanel();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Try again.");
        }
    }

    private void addProduct() {
        String name = JOptionPane.showInputDialog(this, "Enter product name:");
        if (name == null || name.trim().isEmpty()) return;

        String quantityStr = JOptionPane.showInputDialog(this, "Enter product quantity:");
        if (quantityStr == null || !quantityStr.matches("\\d+")) return;
        int quantity = Integer.parseInt(quantityStr);

        products.add(new Product(name, quantity));
        updateDisplay();
    }

    private void editProduct() {
        String name = JOptionPane.showInputDialog(this, "Enter product name to edit:");
        if (name == null || name.trim().isEmpty()) return;

        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)) {
                String newQuantityStr = JOptionPane.showInputDialog(this, "Enter new quantity:");
                if (newQuantityStr != null && newQuantityStr.matches("\\d+")) {
                    product.setQuantity(Integer.parseInt(newQuantityStr));
                    updateDisplay();
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Product not found.");
    }

    private void deleteProduct() {
        String name = JOptionPane.showInputDialog(this, "Enter product name to delete:");
        if (name == null || name.trim().isEmpty()) return;

        products.removeIf(product -> product.getName().equalsIgnoreCase(name));
        updateDisplay();
    }

    private void generateReport() {
        StringBuilder report = new StringBuilder("Low Stock Products:\n");
        for (Product product : products) {
            if (product.getQuantity() < 5) {
                report.append(product.getName()).append(": ").append(product.getQuantity()).append("\n");
            }
        }
        if (report.toString().equals("Low Stock Products:\n")) {
            report.append("No low stock products.");
        }
        JOptionPane.showMessageDialog(this, report.toString());
    }

    private void updateDisplay() {
        StringBuilder displayText = new StringBuilder();
        for (Product product : products) {
            displayText.append(product).append("\n");
        }
        displayArea.setText(displayText.toString());
    }

    public static void main(String[] ignoredArgs) {
        SwingUtilities.invokeLater(InventoryManagementSystem::new);
    }
}

class Product {
    private final String name;
    private int quantity;

    public Product(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    @Override
    public String toString() {
        return "Product{name='" + name + "', quantity=" + quantity + "}";
    }
}