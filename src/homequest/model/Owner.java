package homequest.model;

import homequest.util.PropertyStatus;
import homequest.util.ReservationStatus;
import java.util.ArrayList;
import java.util.List;

public class Owner {
    private String name;
    private List<Property> properties;

    public Owner(String name) {
        this.name = name;
        this.properties = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }

    public HouseAndLot addNewProperty(String blockLot, double lotArea, double basePrice, 
                                      String modelName, double floorArea, List<Property> allProperties) {
        HouseAndLot newProperty = new HouseAndLot(blockLot, lotArea, basePrice, modelName, floorArea);
        allProperties.add(newProperty);
        addProperty(newProperty);
        return newProperty;
    }

    public boolean assignPropertyToAgent(Property property, Agent agent) {
        if (!properties.contains(property)) {
            return false;
        }
        agent.addListing(property);
        return true;
    }

    public String getRemovalBlockReason(Property property) {
        if (property == null || !properties.contains(property)) {
            return "Property does not belong to this owner.";
        }

        if (property.getStatus() == PropertyStatus.SOLD) {
            return "Cannot remove a property that is already sold.";
        }

        if (property.getStatus() == PropertyStatus.PURCHASE_PENDING || property.getPendingBuyer() != null) {
            return "Cannot remove a property with a pending purchase request.";
        }

        if (property.getStatus() == PropertyStatus.RESERVED &&
            !(property.getReservedBy() != null && property.getReservationStatus() == ReservationStatus.ACTIVE)) {
            return "Cannot remove this reserved property because it is already under installment/processing.";
        }

        return null;
    }

    public boolean removeProperty(Property property, List<Property> allProperties, Agent agent) {
        String reason = getRemovalBlockReason(property);
        if (reason != null) {
            return false;
        }

        boolean removed = this.properties.remove(property);
        if (!removed) {
            return false;
        }

        if (property.getReservedBy() != null && property.getReservationStatus() == ReservationStatus.ACTIVE) {
            property.getReservedBy().addFunds(Buyer.RESERVATION_FEE);
            property.clearReservation();
        }

        if (allProperties != null) {
            allProperties.remove(property);
        }
        if (agent != null) {
            agent.removeListing(property);
        }

        return true;
    }

    @Override
    public String toString() {
        return "Owner: " + name + " (" + properties.size() + " properties)";
    }
}
