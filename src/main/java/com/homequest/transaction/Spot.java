package com.homequest.transaction;

import com.homequest.model.Agent;
import com.homequest.model.Buyer;
import com.homequest.model.Property;
import com.homequest.util.PropertyStatus;

public class Spot extends Transaction {
    private Buyer signedBy;
    private Agent receivedBy;
    private double totalAmount;

    public Spot(Property targetProperty, Buyer client, Agent agent) {
        super(targetProperty, client);
        this.signedBy = client;
        this.receivedBy = agent;
        this.totalAmount = targetProperty.getTCP();
    }

    public Buyer getSignedBy() {
        return signedBy;
    }

    public Agent getReceivedBy() {
        return receivedBy;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public void finalizeTransaction() {
        System.out.println("\n=== SPOT CASH PAYMENT ===");
        System.out.println("Cash paid by: " + signedBy.getName());
        System.out.println("Received by Agent: " + receivedBy.getName());

        if (client.deductFunds(totalAmount)) {
            targetProperty.setStatus(PropertyStatus.SOLD);
            client.addTransaction(this);
            System.out.println("✓ Transaction finalized successfully!");
            System.out.println("  Transaction ID: " + transactionID);
            System.out.println("  Total Amount Paid: ₱" + String.format("%.2f", totalAmount));
            System.out.println("  Remaining Balance: ₱" + String.format("%.2f", client.getWalletBalance()));
        } else {
            System.out.println("✗ Insufficient funds! Transaction failed.");
            System.out.println("  Required: ₱" + String.format("%.2f", totalAmount));
            System.out.println("  Available: ₱" + String.format("%.2f", client.getWalletBalance()));
        }
    }

    @Override
    public String toString() {
        return "Spot Cash Payment [" + super.toString() + ", Amount: ₱" + String.format("%.2f", totalAmount) + "]";
    }
}
