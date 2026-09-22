package bookstoreapp;

import java.util.ArrayList;

/**
 * Customer - extends User. Context class in the State Design Pattern.
 * Holds a Status (goldStatus or silverStatus) that changes based on points.
 */
public class Customer extends User {

    private int points;
    private Status status;

    public Customer() {
        super();
        this.points = 0;
        this.status = new Silverstatus();
    }

    public Customer(String username, String password) {
        super(username, password);
        this.points = 0;
        this.status = new Silverstatus();
    }

    public Customer(String username, String password, int points) {
        super(username, password);
        this.points = points;
        // Set correct initial state based on saved points
        if (points >= 1000) {
            this.status = new Goldstatus();
        } else {
            this.status = new Silverstatus();
        }
    }

    @Override
    public void login() {
        // Handled by the GUI
    }

    @Override
    public void logout() {
        // Handled by the GUI
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Buy selected books. Earns 10 points per $1 CAD spent.
     * Updates status after purchase.
     *
     * @selectedBooks list of books to buy
     * @return total cost of the purchase
     */
    public double buyBooks(ArrayList<Book> selectedBooks) {
        double totalCost = 0;
        for (Book b : selectedBooks) {
            totalCost += b.getPrice();
        }
        // Earn 10 points per $1 spent
        int pointsEarned = (int)(totalCost * 10);
        points += pointsEarned;
        updateStatus();
        return totalCost;
    }

    /**
     * Redeem all accumulated points (100 pts = $1 discount), then buy.
     * Transaction cost cannot go below $0.
     * Earns 10 points per $1 of the final amount paid.
     * Updates status after purchase.
     *
     * @param totalCost original total cost before redemption
     * @return final cost after redemption
     */
    public double redeemPointsandBuy(double totalCost) {
        // Calculate maximum discount from current points
        double discount = points / 100.0; // every 100 pts = $1
        double finalCost = totalCost - discount;
        if (finalCost < 0) finalCost = 0;

        // Calculate actual discount applied (capped so cost >= 0)
        // Calculates the discount in dollars if the points > totalcost
        double actualDiscount = totalCost - finalCost;
        int pointsRedeemed = (int)(actualDiscount * 100);
        int remainingPoints = points - pointsRedeemed;

        // Earn 10 points per $1 of final cost paid including the discount
        int pointsEarned = (int)(finalCost * 10);
        points = remainingPoints + pointsEarned;

        updateStatus();
        return finalCost;
    }

    /**
     * updates the current Status object.
     * Part of the State Design Pattern.
     */
    public void updateStatus() {
        status.updateStatus(this);
    }

    @Override
    public String toString() {
        return getUsername() + "," + getPassword() + "," + points;
    }
}