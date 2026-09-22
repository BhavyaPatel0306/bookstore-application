package bookstoreapp;

/**
 * Status interface - part of the State Design Pattern.
 * Implemented by goldStatus and silverStatus.
 */
public interface Status {
    String getStatus();
    void updateStatus(Customer customer);
}