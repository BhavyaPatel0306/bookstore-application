package bookstoreapp;

import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.UncheckedIOException;

/**
 * Dependency-free regression checks. Run with:
 * java -ea -cp out bookstoreapp.BookstoreRegressionTest
 */
public final class BookstoreRegressionTest {
    private static int checks;

    private static void check(boolean condition, String message) {
        checks++;
        if (!condition) throw new AssertionError(message);
    }

    private static void closeTo(double actual, double expected) {
        check(Math.abs(actual - expected) < 0.000001,
                "Expected " + expected + " but got " + actual);
    }

    private static ArrayList<Book> books(double... prices) {
        ArrayList<Book> selected = new ArrayList<>();
        for (double price : prices) selected.add(new Book("Test", price));
        return selected;
    }

    public static void main(String[] args) throws Exception {
        Customer buyer = new Customer("buyer", "demo");
        closeTo(buyer.buyBooks(books(0.10, 0.20)), 0.30);
        check(buyer.getPoints() == 3, "Decimal purchase should earn three points");

        Customer boundary = new Customer("boundary", "demo", 999);
        closeTo(boundary.buyBooks(books(0.10)), 0.10);
        check(boundary.getPoints() == 1000, "Should reach 1000 points");
        check("Gold".equals(boundary.getStatus().getStatus()), "Should become Gold");

        Customer redeem = new Customer("redeem", "demo", 1000);
        closeTo(redeem.redeemPointsandBuy(3.00), 0.00);
        check(redeem.getPoints() == 700, "Redeem only the cost of purchase");
        check("Silver".equals(redeem.getStatus().getStatus()), "Should become Silver");

        Customer partial = new Customer("partial", "demo", 50);
        closeTo(partial.redeemPointsandBuy(1.00), 0.50);
        check(partial.getPoints() == 5, "Earn points on the final paid amount");

        try {
            new Customer("bad", "demo", -1);
            throw new AssertionError("Negative starting points accepted");
        } catch (IllegalArgumentException expected) {
            checks++;
        }

        try {
            buyer.buyBooks(books(Double.NaN));
            throw new AssertionError("NaN price accepted");
        } catch (IllegalArgumentException expected) {
            checks++;
        }

        Path directory = Files.createTempDirectory("bookstore-regression-");
        try {
            Bookstore store = new Bookstore(directory);
            store.getBooks().add(new Book("Book One", 12.50));
            store.getCustomers().add(new Customer("sample", "demo", 1000));
            store.saveData();

            Bookstore restored = new Bookstore(directory);
            restored.loadData();
            check(restored.getBooks().size() == 1, "Book did not persist");
            closeTo(restored.getBooks().get(0).getPrice(), 12.50);
            check(restored.getCustomers().size() == 1, "Customer did not persist");
            check(restored.findCustomer("sample").getPoints() == 1000, "Points did not persist");
            check("Gold".equals(restored.findCustomer("sample").getStatus().getStatus()),
                    "Restored status should be Gold");
            restored.loadData();
            check(restored.getBooks().size() == 1, "Repeated load duplicated books");

            Files.writeString(directory.resolve("books.txt"),
                    "Broken,not-a-price\nValid,4.25\nInvalid,NaN\n", StandardCharsets.UTF_8);
            Files.writeString(directory.resolve("customers.txt"),
                    "bad,demo,not-a-number\nvalid,demo,15\n", StandardCharsets.UTF_8);
            restored.loadData();
            check(restored.getBooks().size() == 1, "Valid book after bad record lost");
            check("Valid".equals(restored.getBooks().get(0).getName()), "Wrong book restored");
            check(restored.findCustomer("valid") != null, "Valid customer after bad record lost");

            // An invalid data directory must report a failure rather than silently losing data.
            Bookstore cannotSave = new Bookstore(directory.resolve("missing-directory"));
            try {
                cannotSave.saveData();
                throw new AssertionError("Expected save failure");
            } catch (UncheckedIOException expected) {
                checks++;
            }
        } finally {
            Files.deleteIfExists(directory.resolve("books.txt"));
            Files.deleteIfExists(directory.resolve("customers.txt"));
            Files.deleteIfExists(directory);
        }

        System.out.println("Passed " + checks + " regression checks.");
    }
}
