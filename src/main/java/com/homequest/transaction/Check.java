package com.homequest.transaction;

import com.homequest.model.Agent;
import com.homequest.model.Buyer;
import com.homequest.model.Property;

public class Check extends CashPayment {
    private Buyer signedBy;
    private Agent receivedBy;
    
    public Check(Property targetProperty, Buyer client, Agent agent) {
        super(targetProperty, client);
        this.signedBy = client;
        this.receivedBy = agent;
    }
    
    public Buyer getSignedBy() {
        return signedBy;
    }
    
    public Agent getReceivedBy() {
        return receivedBy;
    }
    
    @Override
    public void finalizeTransaction() {
        System.out.println("\n=== CHECK PAYMENT ===");
        System.out.println("Check signed by: " + signedBy.getName());
        System.out.println("Received by Agent: " + receivedBy.getName());
        super.finalize();
    }
    
    @Override
    public String toString() {
        return "Check Payment [" + super.toString() + ", Amount: ₱" + String.format("%.2f", totalAmount) + "]";
    }
}
