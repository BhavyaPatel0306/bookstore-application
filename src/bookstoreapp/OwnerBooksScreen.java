package bookstoreapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Owner books screen - manage the bookstore's book inventory.
 * Uses Owner.addBook() and Owner.deleteBook() from the class diagram.
 */
public class OwnerBooksScreen extends JPanel {

    private BookstoreApp       app;
    private Owner              owner;
    private DefaultTableModel  tableModel;
    private JTable             table;
    private JTextField         nameField;
    private JTextField         priceField;

    public OwnerBooksScreen(BookstoreApp app) {
        this.app   = app;
        this.owner = new Owner();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // --- TOP: Table ---
        tableModel = new DefaultTableModel(new String[]{"Book Name", "Book Price"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(480, 190));
        add(scroll, BorderLayout.NORTH);

        // --- MIDDLE: Add form ---
        JPanel mid = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        mid.add(new JLabel("Name:"));
        nameField = new JTextField(12);
        mid.add(nameField);
        mid.add(new JLabel("Price:"));
        priceField = new JTextField(7);
        mid.add(priceField);
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

    /** Reloads the table from BookStore's book list. */
    public void refresh() {
        tableModel.setRowCount(0);
        for (Book b : app.getBookStore().getBooks()) {
            tableModel.addRow(new Object[]{b.getName(), b.getPrice()});
        }
    }

    private void handleAdd() {
        String name      = nameField.getText().trim();
        String priceText = priceField.getText().trim();

        if (name.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in both Name and Price.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
            if (!Double.isFinite(price) || price < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Price must be a valid non-negative number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Only one copy of each book allowed
        for (Book b : app.getBookStore().getBooks()) {
            if (b.getName().equalsIgnoreCase(name)) {
                JOptionPane.showMessageDialog(this,
                        "A book with that name already exists.", "Duplicate Book", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Use Owner.addBook() as per class diagram
        owner.addBook(name, price, app.getBookStore());
        tableModel.addRow(new Object[]{name, price});
        nameField.setText("");
        priceField.setText("");
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Book bookToDelete = app.getBookStore().getBooks().get(row);
        // Use Owner.deleteBook() as per class diagram
        owner.deleteBook(bookToDelete, app.getBookStore());
        tableModel.removeRow(row);
    }
}