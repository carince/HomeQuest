package homequest.model;

import homequest.transaction.Transaction;
import homequest.util.PropertyStatus;
import java.util.ArrayList;
import java.util.List;

public class Buyer {
    private String name;
    private double walletBalance;
    private List<Transaction> purchaseHistory;

    public Buyer(String name, double initialBalance) {
        this.name = name;
        this.walletBalance = initialBalance;
        this.purchaseHistory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public double getWalletBalance() {
        return walletBalance;
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

    public List<Property> getAvailableProperties(List<Property> allProperties) {
        List<Property> availableProps = new ArrayList<>();
        for (Property prop : allProperties) {
            if (prop.getStatus() == PropertyStatus.AVAILABLE) {
                availableProps.add(prop);
            }
        }
        return availableProps;
    }

    public boolean createPurchaseRequest(Property property, int paymentMethod, String bankName, int loanTerm) {
        if (property.getStatus() != PropertyStatus.AVAILABLE) {
            return false;
        }
        property.setPendingBuyer(this);
        property.setPendingPaymentMethod(paymentMethod);
        property.setPendingBankName(bankName);
        property.setPendingLoanTerm(loanTerm);
        property.setStatus(PropertyStatus.RESERVED);
        return true;
    }

    @Override
    public String toString() {
        return "Buyer: " + name + " (Balance: ₱" + String.format("%.2f", walletBalance) + ")";
    }
}
