package bookstoreapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;

/**
 * Customer start screen - shown after a customer logs in.
 * Uses Customer.buyBooks() and Customer.redeemPointsandBuy() from the class diagram.
 */
public class CustomerStartScreen extends JPanel {

    private BookstoreApp      app;
    private JLabel            welcomeLabel;
    private DefaultTableModel tableModel;
    private JTable            table;

    public CustomerStartScreen(BookstoreApp app) {
        this.app = app;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // --- TOP: Welcome message ---
        welcomeLabel = new JLabel(" ");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 13));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        // --- MIDDLE: Books table with checkboxes ---
        tableModel = new DefaultTableModel(new String[]{"Book Name", "Book Price", "Select"}, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 2 ? Boolean.class : Object.class;
            }
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 2; // Only the checkbox column is editable
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        TableColumn selectCol = table.getColumnModel().getColumn(2);
        selectCol.setPreferredWidth(60);
        selectCol.setMaxWidth(75);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(480, 200));
        add(scroll, BorderLayout.CENTER);

        // --- BOTTOM: Buttons ---
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton buyBtn    = new JButton("Buy");
        JButton redeemBtn = new JButton("Redeem points and Buy");
        JButton logoutBtn = new JButton("Logout");
        bot.add(buyBtn);
        bot.add(redeemBtn);
        bot.add(logoutBtn);
        add(bot, BorderLayout.SOUTH);

        buyBtn.addActionListener(e    -> handlePurchase(false));
        redeemBtn.addActionListener(e -> handlePurchase(true));
        logoutBtn.addActionListener(e -> {
            app.setLoggedInCustomer(null);
            app.showScreen(BookstoreApp.SCREEN_LOGIN);
        });
    }

    /** Refreshes the welcome label and book table with current data. */
    public void refresh() {
        Customer c = app.getLoggedInCustomer();
        if (c != null) {
            welcomeLabel.setText("Welcome " + c.getUsername() + ". You have "
                    + c.getPoints() + " points. Your status is "
                    + c.getStatus().getStatus() + ".");
        }

        tableModel.setRowCount(0);
        for (Book b : app.getBookStore().getBooks()) {
            tableModel.addRow(new Object[]{b.getName(), b.getPrice(), false});
        }
    }

    private void handlePurchase(boolean redeem) {
        // Collect selected books
        ArrayList<Book> selectedBooks = new ArrayList<>();
        ArrayList<Book> allBooks = app.getBookStore().getBooks();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean checked = (Boolean) tableModel.getValueAt(i, 2);
            if (checked != null && checked) {
                selectedBooks.add(allBooks.get(i));
            }
        }

        if (selectedBooks.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select at least one book.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Customer customer = app.getLoggedInCustomer();
        double finalCost;

        if (redeem) {
            // Calculate total first, then delegate to Customer.redeemPointsandBuy()
            double total = 0;
            for (Book b : selectedBooks) total += b.getPrice();
            finalCost = customer.redeemPointsandBuy(total);
        } else {
            // Delegate to Customer.buyBooks() which returns total cost
            finalCost = customer.buyBooks(selectedBooks);
        }

        app.showCostScreen(finalCost);
    }
}