package bookstoreapp;

import javax.swing.*;
import java.awt.*;

/**
 * Owner start screen - shown after the owner (admin) logs in.
 */
public class OwnerStartScreen extends JPanel {

    public OwnerStartScreen(BookstoreApp app) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JButton booksBtn     = new JButton("Books");
        JButton customersBtn = new JButton("Customers");
        JButton logoutBtn    = new JButton("Logout");

        Dimension btnSize = new Dimension(180, 40);
        booksBtn.setPreferredSize(btnSize);
        customersBtn.setPreferredSize(btnSize);
        logoutBtn.setPreferredSize(btnSize);

        gbc.gridy = 0; add(booksBtn,     gbc);
        gbc.gridy = 1; add(customersBtn, gbc);
        gbc.gridy = 2; add(logoutBtn,    gbc);

        booksBtn.addActionListener(e     -> app.showScreen(BookstoreApp.SCREEN_OWNER_BOOKS));
        customersBtn.addActionListener(e -> app.showScreen(BookstoreApp.SCREEN_OWNER_CUSTOMERS));
        logoutBtn.addActionListener(e    -> app.showScreen(BookstoreApp.SCREEN_LOGIN));
    }
}