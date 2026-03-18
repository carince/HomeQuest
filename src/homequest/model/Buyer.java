package homequest.model;

import homequest.transaction.Transaction;
import homequest.util.PropertyStatus;
import homequest.util.ReservationStatus;
import homequest.util.PurchaseRequestStatus;
import java.util.ArrayList;
import java.util.List;

public class Buyer {
    public static final double RESERVATION_FEE = 5000.0;
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

    public List<Property> getReservedProperties(List<Property> allProperties) {
        List<Property> reservedProps = new ArrayList<>();
        for (Property prop : allProperties) {
            if (prop.getStatus() == PropertyStatus.RESERVED && 
                prop.getReservedBy() == this && 
                prop.getReservationStatus() == ReservationStatus.ACTIVE) {
                reservedProps.add(prop);
            }
        }
        return reservedProps;
    }

    public boolean reserveProperty(Property property) {
        if (property.getStatus() != PropertyStatus.AVAILABLE) {
            return false;
        }
        if (!deductFunds(RESERVATION_FEE)) {
            return false; // Insufficient funds
        }
        property.setReservedBy(this);
        property.setReservationStatus(ReservationStatus.ACTIVE);
        property.setStatus(PropertyStatus.RESERVED);
        return true;
    }

    public boolean cancelReservation(Property property) {
        if (property.getStatus() != PropertyStatus.RESERVED || property.getReservedBy() != this) {
            return false;
        }
        property.clearReservation();
        property.setStatus(PropertyStatus.AVAILABLE);
        addFunds(RESERVATION_FEE); // Refund reservation fee
        return true;
    }

    public boolean createPurchaseRequest(Property property, int paymentMethod, String bankName, int loanTerm) {
        // Property must be RESERVED or AVAILABLE, and if reserved, must be reserved by this buyer
        if (property.getStatus() == PropertyStatus.RESERVED) {
            if (property.getReservedBy() != this) {
                return false; // Property is reserved by another buyer
            }
        } else if (property.getStatus() != PropertyStatus.AVAILABLE) {
            return false;
        }
        
        property.setPendingBuyer(this);
        property.setPendingPaymentMethod(paymentMethod);
        property.setPendingBankName(bankName);
        property.setPendingLoanTerm(loanTerm);
        property.setPurchaseRequestStatus(PurchaseRequestStatus.PENDING);
        property.setStatus(PropertyStatus.PURCHASE_PENDING);
        return true;
    }

    @Override
    public String toString() {
        return "Buyer: " + name + " (Balance: ₱" + String.format("%.2f", walletBalance) + ")";
    }
}
