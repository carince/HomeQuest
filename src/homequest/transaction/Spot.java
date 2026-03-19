package homequest.transaction;

import homequest.model.Agent;
import homequest.model.Buyer;
import homequest.model.Property;
import homequest.util.FinancialEngine;
import homequest.util.PropertyStatus;

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
    public boolean finalizeTransaction() {
        if (!client.deductFunds(totalAmount)) {
            return false;
        }

        targetProperty.setStatus(PropertyStatus.SOLD);
        client.addTransaction(this);
        receivedBy.addCommission(FinancialEngine.calculateAgentCommission(totalAmount));
        return true;
    }

    @Override
    public String toString() {
        return "Spot Cash Payment [" + super.toString() + ", Amount: ₱" + String.format("%.2f", totalAmount) + "]";
    }
}
