package bookstoreapp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/** Customer context for the Silver/Gold State Design Pattern. */
public class Customer extends User {
    private int points;
    private Status status;

    public Customer() {
        super();
        this.points = 0;
        this.status = new Silverstatus();
    }

    public Customer(String username, String password) {
        this(username, password, 0);
    }

    public Customer(String username, String password, int points) {
        super(username, password);
        if (points < 0) throw new IllegalArgumentException("Points cannot be negative");
        this.points = points;
        this.status = points >= 1000 ? new Goldstatus() : new Silverstatus();
    }

    @Override public void login() { /* Handled by GUI. */ }
    @Override public void logout() { /* Handled by GUI. */ }

    public int getPoints() { return points; }

    public void setPoints(int points) {
        if (points < 0) throw new IllegalArgumentException("Points cannot be negative");
        this.points = points;
        updateStatus();
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    private static BigDecimal money(double amount) {
        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException("Amount must be finite and non-negative");
        }
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
    }

    /** Earn 10 whole points per dollar paid; fractional points are rounded down. */
    public double buyBooks(ArrayList<Book> selectedBooks) {
        BigDecimal total = BigDecimal.ZERO;
        for (Book book : selectedBooks) total = total.add(money(book.getPrice()));
        int earned = total.multiply(BigDecimal.TEN)
                .setScale(0, RoundingMode.DOWN).intValueExact();
        points = Math.addExact(points, earned);
        updateStatus();
        return total.doubleValue();
    }

    /** Redeem at 100 points per dollar, capped at the purchase cost. */
    public double redeemPointsandBuy(double totalCost) {
        BigDecimal original = money(totalCost);
        BigDecimal discount = BigDecimal.valueOf(points, 2).min(original);
        BigDecimal finalCost = original.subtract(discount);
        int redeemed = discount.movePointRight(2).intValueExact();
        int earned = finalCost.multiply(BigDecimal.TEN)
                .setScale(0, RoundingMode.DOWN).intValueExact();
        points = Math.addExact(points - redeemed, earned);
        updateStatus();
        return finalCost.doubleValue();
    }

    public void updateStatus() { status.updateStatus(this); }

    @Override
    public String toString() {
        return getUsername() + "," + getPassword() + "," + points;
    }
}
