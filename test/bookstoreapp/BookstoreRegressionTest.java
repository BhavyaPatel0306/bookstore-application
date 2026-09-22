package bookstoreapp;

import java.util.ArrayList;

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

    public static void main(String[] args) {
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

        System.out.println("Passed " + checks + " regression checks.");
    }
}
