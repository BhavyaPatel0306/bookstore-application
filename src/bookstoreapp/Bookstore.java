package bookstoreapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/** Book and customer storage for the academic bookstore application. */
public class Bookstore {
    private final ArrayList<Book> books = new ArrayList<>();
    private final ArrayList<Customer> customers = new ArrayList<>();
    private final Path dataDirectory;

    public Bookstore() {
        this(Path.of("."));
    }

    /** Allows isolated persistence tests without touching the user's data. */
    public Bookstore(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public ArrayList<Book> getBooks() { return books; }
    public ArrayList<Customer> getCustomers() { return customers; }

    public Customer findCustomer(String username) {
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) return customer;
        }
        return null;
    }

    public User authenticate(String username, String password) {
        if ("admin".equals(username) && "admin".equals(password)) return new Owner();
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)
                    && customer.getPassword().equals(password)) return customer;
        }
        return null;
    }

    /** Repeated loads replace in-memory data instead of duplicating records. */
    public void loadData() {
        books.clear();
        customers.clear();
        loadBooks();
        loadCustomers();
    }

    private void loadBooks() {
        Path file = dataDirectory.resolve("books.txt");
        if (!Files.exists(file)) return;
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            int number = 0;
            while ((line = reader.readLine()) != null) {
                number++;
                if (line.isBlank()) continue;
                String[] fields = line.split(",", -1);
                if (fields.length != 2) {
                    warn(file, number);
                    continue;
                }
                try {
                    String name = fields[0].trim();
                    double price = Double.parseDouble(fields[1].trim());
                    if (name.isEmpty() || !Double.isFinite(price) || price < 0) {
                        warn(file, number);
                    } else {
                        books.add(new Book(name, price));
                    }
                } catch (NumberFormatException exception) {
                    warn(file, number);
                }
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to load " + file, exception);
        }
    }

    private void loadCustomers() {
        Path file = dataDirectory.resolve("customers.txt");
        if (!Files.exists(file)) return;
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            int number = 0;
            while ((line = reader.readLine()) != null) {
                number++;
                if (line.isBlank()) continue;
                String[] fields = line.split(",", -1);
                if (fields.length != 3) {
                    warn(file, number);
                    continue;
                }
                try {
                    String username = fields[0].trim();
                    String password = fields[1].trim();
                    int points = Integer.parseInt(fields[2].trim());
                    if (username.isEmpty() || points < 0) {
                        warn(file, number);
                    } else {
                        customers.add(new Customer(username, password, points));
                    }
                } catch (NumberFormatException exception) {
                    warn(file, number);
                }
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to load " + file, exception);
        }
    }

    private static void warn(Path file, int line) {
        System.err.println("Skipping invalid record at " + file + ":" + line);
    }

    /**
     * Write each file to a sibling temporary file, then replace the original.
     * A failure is surfaced to the caller instead of being silently ignored.
     * Each file is replaced independently; this is not a two-file transaction.
     */
    public void saveData() {
        try {
            saveFile(dataDirectory.resolve("books.txt"), bookLines());
            saveFile(dataDirectory.resolve("customers.txt"), customerLines());
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to save bookstore data", exception);
        }
    }

    private List<String> bookLines() {
        List<String> lines = new ArrayList<>();
        for (Book book : books) lines.add(book.toString());
        return lines;
    }

    private List<String> customerLines() {
        List<String> lines = new ArrayList<>();
        for (Customer customer : customers) lines.add(customer.toString());
        return lines;
    }

    private static void saveFile(Path destination, List<String> lines) throws IOException {
        Path parent = destination.toAbsolutePath().getParent();
        Path temporary = Files.createTempFile(parent, ".bookstore-", ".tmp");
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(temporary, StandardCharsets.UTF_8)) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            try {
                Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}
