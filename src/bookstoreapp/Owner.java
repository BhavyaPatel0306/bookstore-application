package bookstoreapp;

import java.util.ArrayList;

/**
 * Owner - extends User. Has one fixed account (admin/admin).
 */
public class Owner extends User {

    public Owner() {
        super("admin", "admin");
    }

    @Override
    public void login() {
        // Handled by the GUI (BookstoreApp / LoginScreen)
    }

    @Override
    public void logout() {
        // Handled by the GUI
    }

    public void addBook(String name, double price, Bookstore store) {
        Book book = new Book(name, price);
        store.getBooks().add(book);
    }

    public void deleteBook(Book book, Bookstore store) {
        store.getBooks().remove(book);
    }

    public void addCustomer(String username, String password, Bookstore store) {
        Customer customer = new Customer(username, password);
        store.getCustomers().add(customer);
    }

    public void deleteCustomer(Customer customer, Bookstore store) {
        store.getCustomers().remove(customer);
    }
}