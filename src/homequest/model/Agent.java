package homequest.model;

import homequest.transaction.*;
import homequest.util.PropertyStatus;
import homequest.util.PurchaseRequestStatus;
import homequest.util.ReservationStatus;
import java.util.ArrayList;
import java.util.List;

public class Agent {
    private String name;
    private List<Property> listings;
    private double totalCommission;

    public Agent(String name) {
        this.name = name;
        this.listings = new ArrayList<>();
        this.totalCommission = 0;
    }

    public String getName() {
        return name;
    }

    public List<Property> getListings() {
        return listings;
    }

    public double getTotalCommission() {
        return totalCommission;
    }

    public void addCommission(double amount) {
        if (amount > 0) {
            this.totalCommission += amount;
        }
    }

    public void addListing(Property property) {
        this.listings.add(property);
    }

    public List<Property> getReservedProperties(List<Property> allProperties) {
        List<Property> reservedProps = new ArrayList<>();
        for (Property prop : allProperties) {
            if (prop.getStatus() == PropertyStatus.RESERVED && 
                prop.getReservedBy() != null && 
                prop.getReservationStatus() == ReservationStatus.ACTIVE) {
                reservedProps.add(prop);
            }
        }
        return reservedProps;
    }

    public List<Property> getPendingPurchaseRequests(List<Property> allProperties) {
        List<Property> pendingProps = new ArrayList<>();
        for (Property prop : allProperties) {
            if (prop.getStatus() == PropertyStatus.PURCHASE_PENDING && 
                prop.getPendingBuyer() != null &&
                prop.getPurchaseRequestStatus() == PurchaseRequestStatus.PENDING) {
                pendingProps.add(prop);
            }
        }
        return pendingProps;
    }

    public boolean approveTransaction(Property property) {
        if (property.getPendingBuyer() == null || 
            property.getStatus() != PropertyStatus.PURCHASE_PENDING) {
            return false;
        }
        property.setPurchaseRequestStatus(PurchaseRequestStatus.APPROVED);
        finalizeTransaction(property, property.getPendingBuyer(), this);
        return true;
    }

    public boolean rejectTransaction(Property property) {
        if (property.getStatus() != PropertyStatus.PURCHASE_PENDING) {
            return false;
        }
        property.setPurchaseRequestStatus(PurchaseRequestStatus.REJECTED);
        property.clearPendingRequest();
        
        // If buyer had a reservation, keep it as RESERVED. Otherwise make AVAILABLE.
        if (property.getReservedBy() != null && property.getReservationStatus() == ReservationStatus.ACTIVE) {
            property.setStatus(PropertyStatus.RESERVED);
        } else {
            property.setStatus(PropertyStatus.AVAILABLE);
        }
        return true;
    }

    public void finalizeTransaction(Property property, Buyer buyer, Agent agent) {
        Transaction transaction = null;
        switch (property.getPendingPaymentMethod()) {
            case 1:
                transaction = new Spot(property, buyer, agent);
                break;
            case 2:
                transaction = new Check(property, buyer, agent);
                break;
            case 3:
                transaction = new Bank(property, buyer, property.getPendingLoanTerm(), property.getPendingBankName(), agent);
                break;
            case 4:
                transaction = new PagIbig(property, buyer, property.getPendingLoanTerm(), agent);
                break;
        }
        if (transaction != null) {
            transaction.finalizeTransaction();
            property.clearPendingRequest();
        }
    }

    @Override
    public String toString() {
        return "Agent: " + name;
    }
}
