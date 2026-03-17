package homequest.transaction;

import homequest.model.Buyer;
import homequest.model.Property;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Transaction {
    protected String transactionID;
    protected Property targetProperty;
    protected Buyer client;
    protected List<Double> payments;
    protected List<String> dueDates;

    public Transaction(Property targetProperty, Buyer client) {
        this.transactionID = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.targetProperty = targetProperty;
        this.client = client;
        this.payments = new ArrayList<>();
        this.dueDates = new ArrayList<>();
    }

    public String getTransactionID() {
        return transactionID;
    }

    public Property getTargetProperty() {
        return targetProperty;
    }

    public Buyer getClient() {
        return client;
    }

    public List<Double> getPayments() {
        return payments;
    }

    public List<String> getDueDates() {
        return dueDates;
    }

    public void addPayment(double amount, String dueDate) {
        this.payments.add(amount);
        this.dueDates.add(dueDate);
    }

    public abstract void finalizeTransaction();

    @Override
    public String toString() {
        return "Transaction [ID: " + transactionID + ", Property: " + targetProperty.getName() +
                ", Client: " + client.getName() + "]";
    }
}
