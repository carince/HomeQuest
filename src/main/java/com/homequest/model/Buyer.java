package com.homequest.model;

import com.homequest.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class Buyer extends User {
    private double walletBalance;
    private List<Transaction> purchaseHistory;
    
    public Buyer(String name, double initialBalance) {
        super(name);
        this.walletBalance = initialBalance;
        this.purchaseHistory = new ArrayList<>();
    }
    
    public double getWalletBalance() {
        return walletBalance;
    }
    
    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }
    
    public void addFunds(double amount) {
        this.walletBalance += amount;
    }
    
    public boolean deductFunds(double amount) {
        if (this.walletBalance >= amount) {
            this.walletBalance -= amount;
            return true;
        }
        return false;
    }
    
    public List<Transaction> getPurchaseHistory() {
        return purchaseHistory;
    }
    
    public void addTransaction(Transaction transaction) {
        this.purchaseHistory.add(transaction);
    }
    
    @Override
    public String toString() {
        return "Buyer: " + name + " (Balance: ₱" + String.format("%.2f", walletBalance) + ")";
    }
}
