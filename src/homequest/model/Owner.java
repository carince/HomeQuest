package homequest.model;

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

    @Override
    public String toString() {
        return "Owner: " + name + " (" + properties.size() + " properties)";
    }
}
