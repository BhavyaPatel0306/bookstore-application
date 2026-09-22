package bookstoreapp;

/**
 * goldStatus - Concrete State for customers with 1000+ points.
 * Part of the State Design Pattern.
 */
public class Goldstatus implements Status {

    @Override
    public String getStatus() {
        return "Gold";
    }

    @Override
    public void updateStatus(Customer customer) {
        // Transition to Silver if points drop below 1000
        if (customer.getPoints() < 1000) {
            customer.setStatus(new Silverstatus());
        }
    }
}