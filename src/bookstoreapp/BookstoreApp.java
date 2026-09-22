package bookstoreapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * BookstoreApp - the main JFrame.
 * Uses CardLayout to swap screens inside a single window.
 */
public class BookstoreApp extends JFrame {

    // Screen name constants
    public static final String SCREEN_LOGIN            = "LOGIN";
    public static final String SCREEN_OWNER_START      = "OWNER_START";
    public static final String SCREEN_OWNER_BOOKS      = "OWNER_BOOKS";
    public static final String SCREEN_OWNER_CUSTOMERS  = "OWNER_CUSTOMERS";
    public static final String SCREEN_CUSTOMER_START   = "CUSTOMER_START";
    public static final String SCREEN_CUSTOMER_COST    = "CUSTOMER_COST";

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Central data object (from class diagram)
    private Bookstore bookStore;

    // Currently logged-in customer (null when owner is logged in)
    private Customer loggedInCustomer;

    // Screens
    private LoginScreen           loginScreen;
    private OwnerStartScreen      ownerStartScreen;
    private OwnerBooksScreen      ownerBooksScreen;
    private OwnerCustomersScreen  ownerCustomersScreen;
    private CustomerStartScreen   customerStartScreen;
    private CustomerCostScreen    customerCostScreen;

    public BookstoreApp() {
        setTitle("Bookstore App");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(540, 430);
        setLocationRelativeTo(null);

        // Create BookStore and load data from files
        bookStore = new Bookstore();
        bookStore.loadData();

        // Save data and exit when [x] is clicked
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                bookStore.saveData();
                System.exit(0);
            }
        });

        // Setup CardLayout panel
        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);

        // Create all screens
        loginScreen          = new LoginScreen(this);
        ownerStartScreen     = new OwnerStartScreen(this);
        ownerBooksScreen     = new OwnerBooksScreen(this);
        ownerCustomersScreen = new OwnerCustomersScreen(this);
        customerStartScreen  = new CustomerStartScreen(this);
        customerCostScreen   = new CustomerCostScreen(this);

        // Add screens to CardLayout
        mainPanel.add(loginScreen,          SCREEN_LOGIN);
        mainPanel.add(ownerStartScreen,     SCREEN_OWNER_START);
        mainPanel.add(ownerBooksScreen,     SCREEN_OWNER_BOOKS);
        mainPanel.add(ownerCustomersScreen, SCREEN_OWNER_CUSTOMERS);
        mainPanel.add(customerStartScreen,  SCREEN_CUSTOMER_START);
        mainPanel.add(customerCostScreen,   SCREEN_CUSTOMER_COST);

        add(mainPanel);
        showScreen(SCREEN_LOGIN);
        setVisible(true);
    }

    /** Switches the visible screen, refreshing data-driven screens before display. */
    public void showScreen(String screenName) {
        switch (screenName) {
            case SCREEN_OWNER_BOOKS:
                ownerBooksScreen.refresh();
                break;
            case SCREEN_OWNER_CUSTOMERS:
                ownerCustomersScreen.refresh();
                break;
            case SCREEN_CUSTOMER_START:
                customerStartScreen.refresh();
                break;
        }
        cardLayout.show(mainPanel, screenName);
    }

    /** Called after a purchase to show the cost screen with result data. */
    public void showCostScreen(double totalCost) {
        customerCostScreen.setData(totalCost, loggedInCustomer.getPoints(),
                loggedInCustomer.getStatus().getStatus());
        cardLayout.show(mainPanel, SCREEN_CUSTOMER_COST);
    }

    public Bookstore getBookStore(){ 
        return bookStore; }
    public Customer getLoggedInCustomer(){
        return loggedInCustomer; }
    public void setLoggedInCustomer(Customer c){
        this.loggedInCustomer = c; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookstoreApp());
    }
}