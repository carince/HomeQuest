package homequest;

import homequest.jframe.Main;
import homequest.model.*;
import java.util.ArrayList;
import java.util.List;

public class HomeQuest {
    private static Agent agent;
    private static Buyer buyer;
    private static Owner owner;
    private static List<Property> allProperties;
    
    static {
        initializeSampleData();
    }
    
    private static void initializeSampleData() {
        allProperties = new ArrayList<>();
        
        agent = new Agent("Sean Serrano");
        owner = new Owner("Asiana Homes");
        buyer = new Buyer("De Guzman", 15_000_000);

        String[] modelNames = {"Nadine", "Elaine", "Eunice"};

        // Seed 10 properties.
        for (int index = 0; index < 10; index++) {
            int block = (index / 5) + 1;
            int lot = (index % 5) + 1;
            String modelName = modelNames[index % modelNames.length];
            String blockLot = "B" + block + "L" + lot;

            // Deterministic variety so sample data feels realistic.
            double lotArea = 90 + (block * 8) + ((lot % 5) * 6);
            double floorArea = 45 + (block * 4) + ((lot % 6) * 5);
            double basePrice = 1_600_000 + (block * 180_000) + (lot * 42_000) + ((index % 3) * 120_000);

            HouseAndLot property = new HouseAndLot(blockLot, lotArea, basePrice, modelName, floorArea);
            allProperties.add(property);
            agent.addListing(property);
            owner.addProperty(property);
        }
    }

    public static Agent getAgent() {
        return agent;
    }

    public static Buyer getBuyer() {
        return buyer;
    }

    public static Owner getOwner() {
        return owner;
    }

    public static List<Property> getAllProperties() {
        return allProperties;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}

