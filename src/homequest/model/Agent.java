package homequest.model;

import homequest.transaction.*;
import homequest.util.PropertyStatus;
import java.util.ArrayList;
import java.util.List;

public class Agent {
    private String name;
    private String licenseNumber;
    private List<Property> listings;
    private double totalCommission;

    public Agent(String name, String licenseNumber) {
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.listings = new ArrayList<>();
        this.totalCommission = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
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

    public void removeListing(Property property) {
        this.listings.remove(property);
    }

    public List<Property> getReservedProperties(List<Property> allProperties) {
        List<Property> reservedProps = new ArrayList<>();
        for (Property prop : allProperties) {
            if (prop.getStatus() == PropertyStatus.RESERVED && prop.getPendingBuyer() != null) {
                reservedProps.add(prop);
            }
        }
        return reservedProps;
    }

    public boolean approveTransaction(Property property) {
        if (property.getPendingBuyer() == null) {
            return false;
        }
        finalizeTransaction(property, property.getPendingBuyer(), this);
        return true;
    }

    public boolean rejectTransaction(Property property) {
        if (property.getStatus() != PropertyStatus.RESERVED) {
            return false;
        }
        property.setStatus(PropertyStatus.AVAILABLE);
        property.clearPendingRequest();
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
        return "Agent: " + name + " (License: " + licenseNumber + ")";
    }
}
