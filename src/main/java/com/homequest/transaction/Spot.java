package com.homequest.transaction;

import com.homequest.model.Agent;
import com.homequest.model.Buyer;
import com.homequest.model.Property;

public class Spot extends CashPayment {
    private Buyer signedBy;
    private Agent receivedBy;
    
    public Spot(Property targetProperty, Buyer client, Agent agent) {
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
        System.out.println("\n=== SPOT CASH PAYMENT ===");
        System.out.println("Cash paid by: " + signedBy.getName());
        System.out.println("Received by Agent: " + receivedBy.getName());
        super.finalize();
    }
    
    @Override
    public String toString() {
        return "Spot Cash Payment [" + super.toString() + ", Amount: ₱" + String.format("%.2f", totalAmount) + "]";
    }
}
