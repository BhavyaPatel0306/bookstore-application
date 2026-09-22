package bookstoreapp;

import javax.swing.*;
import java.awt.*;

/**
 * Customer cost screen - shown after a purchase completes.
 * Displays total cost, updated points, and updated status.
 */
public class CustomerCostScreen extends JPanel {

    private BookstoreApp app;
    private JLabel       costLabel;
    private JLabel       pointsStatusLabel;

    public CustomerCostScreen(BookstoreApp app) {
        this.app = app;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(22, 22, 22, 22);
        gbc.gridx  = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        costLabel = new JLabel("Total Cost: $0.00");
        costLabel.setFont(new Font("Arial", Font.BOLD, 17));
        gbc.gridy = 0;
        add(costLabel, gbc);

        pointsStatusLabel = new JLabel("Points: 0, Status: Silver");
        pointsStatusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        add(pointsStatusLabel, gbc);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setPreferredSize(new Dimension(130, 36));
        gbc.gridy = 2;
        add(logoutBtn, gbc);

        logoutBtn.addActionListener(e -> {
            app.setLoggedInCustomer(null);
            app.showScreen(BookstoreApp.SCREEN_LOGIN);
        });
    }

    /** Updates the labels with the transaction result. */
    public void setData(double totalCost, int points, String status) {
        costLabel.setText(String.format("Total Cost: $%.2f", totalCost));
        pointsStatusLabel.setText("Points: " + points + ", Status: " + status);
    }
}