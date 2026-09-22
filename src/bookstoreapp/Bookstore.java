package bookstoreapp;

import java.io.*;
import java.util.ArrayList;

/**
 * Bookstore 
 * Holds books (ArrayList<Book>) and customers (ArrayList<Customer>).
 * Handles file I/O (books.txt, customers.txt) and authentication.
 */
public class Bookstore {

    private ArrayList<Book> books;
    private ArrayList<Customer> customers;

    private static final String BOOKS_FILE     = "books.txt";
    private static final String CUSTOMERS_FILE = "customers.txt";

    public Bookstore() {
        books     = new ArrayList<>();
        customers = new ArrayList<>();
    }



    public ArrayList<Book> getBooks() {
        return books;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    /**
     * Finds a customer by username.
     * @return the Customer, or null if not found
     * Validates for unique username
     */
    public Customer findCustomer(String username) {
        for (Customer c : customers) {
            if (c.getUsername().equals(username)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Authenticates a login attempt.
     * @param username entered username
     * @param password entered password
     * @return User (Owner or Customer) if valid, null otherwise
     */
    public User authenticate(String username, String password) {
        // Check owner
        if (username.equals("admin") && password.equals("admin")) {
            return new Owner();
        }
        // Check customers
        for (Customer c : customers) {
            if (c.getUsername().equals(username) && c.getPassword().equals(password)) {
                return c;
            }
        }
        return null;
    }
    /**
     * Loads books and customers from their respective text files.
     */
    public void loadData() {
        loadBooks();
        loadCustomers();
    }

    /**
     * Saves books and customers to their respective text files.
     */
    public void saveData() {
        saveBooks();
        saveCustomers();
    }
    // Reads file line by line and splits each into 2 parts and creates books objects 
    private void loadBooks() {
        File f = new File(BOOKS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) { //reads the file in chunks instead of characters
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name  = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    if (!name.isEmpty() && Double.isFinite(price) && price >= 0) {
                        books.add(new Book(name, price));
                    }
                }
            }
        } catch (IOException | NumberFormatException e) { //checks if file exists or readable
            System.err.println("Error loading books: " + e.getMessage());
        }
    }
        
   //Overwrites the file 
    private void saveBooks() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BOOKS_FILE, false))) {
            for (Book b : books) {
                pw.println(b.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }

    private void loadCustomers() {
        File f = new File(CUSTOMERS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String uname  = parts[0].trim();
                    String pwd    = parts[1].trim();
                    int    pts    = Integer.parseInt(parts[2].trim()); //Converts string to int
                    if (!uname.isEmpty() && pts >= 0) {
                        customers.add(new Customer(uname, pwd, pts));
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
    }
    //Overwrites the file 
    private void saveCustomers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CUSTOMERS_FILE, false))) {
            for (Customer c : customers) {
                pw.println(c.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }
}