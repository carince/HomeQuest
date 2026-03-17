package homequest.transaction;

import homequest.model.Agent;
import homequest.model.Buyer;
import homequest.model.Property;
import homequest.util.FinancialEngine;
import homequest.util.PropertyStatus;

public class Check extends Transaction {
    private Buyer signedBy;
    private Agent receivedBy;
    private double totalAmount;

    public Check(Property targetProperty, Buyer client, Agent agent) {
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
        System.out.println("\n=== CHECK PAYMENT ===");
        System.out.println("Check signed by: " + signedBy.getName());
        System.out.println("Received by Agent: " + receivedBy.getName());

        if (client.deductFunds(totalAmount)) {
            targetProperty.setStatus(PropertyStatus.SOLD);
            client.addTransaction(this);
            receivedBy.addCommission(FinancialEngine.calculateAgentCommission(totalAmount));
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
        return "Check Payment [" + super.toString() + ", Amount: ₱" + String.format("%.2f", totalAmount) + "]";
    }
}
