package bookstoreapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Owner customers screen - manage registered customers.
 * Uses Owner.addCustomer() and Owner.deleteCustomer() from the class diagram.
 */
public class OwnerCustomersScreen extends JPanel {

    private BookstoreApp      app;
    private Owner             owner;
    private DefaultTableModel tableModel;
    private JTable            table;
    private JTextField        usernameField;
    private JTextField        passwordField;

    public OwnerCustomersScreen(BookstoreApp app) {
        this.app   = app;
        this.owner = new Owner();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // --- TOP: Table ---
        tableModel = new DefaultTableModel(new String[]{"Username", "Password", "Points"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(480, 190));
        add(scroll, BorderLayout.NORTH);

        // --- MIDDLE: Add form ---
        JPanel mid = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        mid.add(new JLabel("Username:"));
        usernameField = new JTextField(12);
        mid.add(usernameField);
        mid.add(new JLabel("Password:"));
        passwordField = new JTextField(10);
        mid.add(passwordField);
        JButton addBtn = new JButton("Add");
        mid.add(addBtn);
        add(mid, BorderLayout.CENTER);

        // --- BOTTOM: Delete & Back ---
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        JButton deleteBtn = new JButton("Delete");
        JButton backBtn   = new JButton("Back");
        bot.add(deleteBtn);
        bot.add(backBtn);
        add(bot, BorderLayout.SOUTH);

        addBtn.addActionListener(e    -> handleAdd());
        deleteBtn.addActionListener(e -> handleDelete());
        backBtn.addActionListener(e   -> app.showScreen(BookstoreApp.SCREEN_OWNER_START));
    }

    /** Reloads the table from BookStore's customer list. */
    public void refresh() {
        tableModel.setRowCount(0);
        for (Customer c : app.getBookStore().getCustomers()) {
            tableModel.addRow(new Object[]{c.getUsername(), c.getPassword(), c.getPoints()});
        }
    }

    private void handleAdd() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in both Username and Password.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (username.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this,
                    "Username 'admin' is reserved.", "Invalid Username", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Customer c : app.getBookStore().getCustomers()) {
            if (c.getUsername().equalsIgnoreCase(username)) {
                JOptionPane.showMessageDialog(this,
                        "A customer with that username already exists.", "Duplicate", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Use Owner.addCustomer() as per class diagram
        owner.addCustomer(username, password, app.getBookStore());
        tableModel.addRow(new Object[]{username, password, 0});
        usernameField.setText("");
        passwordField.setText("");
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Customer customerToDelete = app.getBookStore().getCustomers().get(row);
        // Use Owner.deleteCustomer() as per class diagram
        owner.deleteCustomer(customerToDelete, app.getBookStore());
        tableModel.removeRow(row);
    }
}