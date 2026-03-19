package homequest.transaction;

import homequest.model.Buyer;
import homequest.model.Property;
import homequest.util.PropertyStatus;

public abstract class CashPayment extends Transaction {
    protected double totalAmount;
    
    public CashPayment(Property targetProperty, Buyer client) {
        super(targetProperty, client);
        this.totalAmount = targetProperty.getTCP();
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public boolean processCashPayment() {
        if (!client.deductFunds(totalAmount)) {
            return false;
        }

        targetProperty.setStatus(PropertyStatus.SOLD);
        client.addTransaction(this);
        return true;
    }
    
    @Override
    public boolean finalizeTransaction() {
        return processCashPayment();
    }
}
