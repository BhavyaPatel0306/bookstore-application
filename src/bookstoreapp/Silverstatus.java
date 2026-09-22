package bookstoreapp;

/**
 * silverStatus - Concrete State for customers with fewer than 1000 points.
 * Part of the State Design Pattern.
 */
public class Silverstatus implements Status {

    @Override
    public String getStatus() {
        return "Silver";
    }

    @Override
    public void updateStatus(Customer customer) {
        // Transition to Gold if points reach 1000 or more
        if (customer.getPoints() >= 1000) {
            customer.setStatus(new Goldstatus());
        }
    }
}