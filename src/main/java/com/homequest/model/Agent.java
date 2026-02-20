package com.homequest.model;

import java.util.ArrayList;
import java.util.List;

public class Agent extends User {
    private String licenseNumber;
    private List<Property> listings;
    
    public Agent(String name, String licenseNumber) {
        super(name);
        this.licenseNumber = licenseNumber;
        this.listings = new ArrayList<>();
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
    
    public void addListing(Property property) {
        this.listings.add(property);
    }
    
    public void removeListing(Property property) {
        this.listings.remove(property);
    }
    
    @Override
    public String toString() {
        return "Agent: " + name + " (License: " + licenseNumber + ")";
    }
}
