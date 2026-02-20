package com.homequest.transaction;

import com.homequest.model.Buyer;
import com.homequest.model.Property;
import com.homequest.util.PropertyStatus;

public abstract class CashPayment extends Transaction {
    protected double totalAmount;
    
    public CashPayment(Property targetProperty, Buyer client) {
        super(targetProperty, client);
        this.totalAmount = targetProperty.getTCP();
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    /**
     * Finalize the cash payment
     */
    public void finalize() {
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
    public void finalizeTransaction() {
        finalize();
    }
}
