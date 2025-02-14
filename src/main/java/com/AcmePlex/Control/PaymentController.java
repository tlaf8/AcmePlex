package com.AcmePlex.Control;

public class PaymentController {

    /**
     * Simulates payment processing.
     * @param email User's email.
     * @param amount Amount to be charged.
     * @param cardInfo Card information of the user.
     * @return true if payment succeeds, false otherwise.
     */
    public boolean processPayment(String email, double amount, String cardInfo) {
        try {
            // Simulate payment processing (e.g., call a payment API)
            System.out.println("Processing payment for " + email);
            System.out.println("Amount: $" + amount);
            System.out.println("Card Info: " + cardInfo);

            // Assume payment always succeeds for simulation
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}