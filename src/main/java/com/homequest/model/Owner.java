package com.homequest.model;

import java.util.ArrayList;
import java.util.List;

public class Owner extends User {
    private List<Property> properties;
    
    public Owner(String name) {
        super(name);
        this.properties = new ArrayList<>();
    }
    
    public List<Property> getProperties() {
        return properties;
    }
    
    public void addProperty(Property property) {
        this.properties.add(property);
    }
    
    public void removeProperty(Property property) {
        this.properties.remove(property);
    }
    
    @Override
    public String toString() {
        return "Owner: " + name + " (" + properties.size() + " properties)";
    }
}
