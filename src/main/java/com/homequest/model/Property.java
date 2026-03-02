package com.homequest.model;

import com.homequest.util.PropertyStatus;

public abstract class Property {
    protected String blockLot;
    protected double lotArea;
    protected double basePrice;
    protected PropertyStatus status;

    public Property(String blockLot, double lotArea, double basePrice) {
        this.blockLot = blockLot;
        this.lotArea = lotArea;
        this.basePrice = basePrice;
        this.status = PropertyStatus.AVAILABLE;
    }

    public String getBlockLot() {
        return blockLot;
    }

    public void setBlockLot(String blockLot) {
        this.blockLot = blockLot;
    }

    public double getLotArea() {
        return lotArea;
    }

    public void setLotArea(double lotArea) {
        this.lotArea = lotArea;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public abstract double getTCP();

    @Override
    public String toString() {
        return "Property [Block/Lot: " + blockLot + ", Lot Area: " + lotArea + " sqm, Base Price: ₱" +
                String.format("%.2f", basePrice) + ", Status: " + status + "]";
    }
}
