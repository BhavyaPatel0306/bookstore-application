package bookstoreapp;

import javax.swing.*;
import java.awt.*;

/**
 * Login screen - first screen the user sees.
 * Uses BookStore.authenticate() to verify credentials.
 */
public class LoginScreen extends JPanel {

    private BookstoreApp app;
    private JTextField    usernameField;
    private JPasswordField passwordField;

    public LoginScreen(BookstoreApp app) {
        this.app = app;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Title
        JLabel title = new JLabel("Welcome to the BookStore App");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        // Username
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(18);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(18);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        // Login button
        JButton loginBtn = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginBtn, gbc);

        loginBtn.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Delegate authentication to BookStore
        User user = app.getBookStore().authenticate(username, password);

        if (user == null) {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        clearFields();

        if (user instanceof Owner) {
            app.showScreen(BookstoreApp.SCREEN_OWNER_START);
        } else if (user instanceof Customer) {
            app.setLoggedInCustomer((Customer) user);
            app.showScreen(BookstoreApp.SCREEN_CUSTOMER_START);
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}