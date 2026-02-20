package com.homequest.transaction;

import com.homequest.model.Buyer;
import com.homequest.model.Property;
import com.homequest.util.FinancialEngine;
import com.homequest.util.PaymentSchedule;

import java.util.UUID;

public abstract class Transaction {
    protected FinancialEngine engine;
    protected String transactionID;
    protected Property targetProperty;
    protected Buyer client;
    protected PaymentSchedule schedule;
    
    public Transaction(Property targetProperty, Buyer client) {
        this.engine = new FinancialEngine();
        this.transactionID = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.targetProperty = targetProperty;
        this.client = client;
        this.schedule = new PaymentSchedule();
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
    
    public PaymentSchedule getSchedule() {
        return schedule;
    }
    
    /**
     * Finalize the transaction - to be implemented by subclasses
     */
    public abstract void finalizeTransaction();
    
    @Override
    public String toString() {
        return "Transaction [ID: " + transactionID + ", Property: " + targetProperty.getBlockLot() + 
               ", Client: " + client.getName() + "]";
    }
}
