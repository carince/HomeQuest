package com.homequest.model;

import com.homequest.util.FinancialEngine;

public class HouseAndLot extends Property {
    private String modelName;
    private double floorArea;

    public HouseAndLot(String blockLot, double lotArea, double basePrice, String modelName, double floorArea) {
        super(blockLot, lotArea, basePrice);
        this.modelName = modelName;
        this.floorArea = floorArea;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public double getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(double floorArea) {
        this.floorArea = floorArea;
    }

    @Override
    public double getTCP() {
        double vat = FinancialEngine.calculateVAT(basePrice, false);
        double otherCharges = FinancialEngine.computeOtherCharges(basePrice);
        return basePrice + vat + otherCharges;
    }

    @Override
    public String toString() {
        return "House and Lot [Model: " + modelName + ", Block/Lot: " + blockLot +
                ", Lot Area: " + lotArea + " sqm, Floor Area: " + floorArea + " sqm, " +
                "Base Price: ₱" + String.format("%.2f", basePrice) +
                ", TCP: ₱" + String.format("%.2f", getTCP()) + ", Status: " + status + "]";
    }
}
