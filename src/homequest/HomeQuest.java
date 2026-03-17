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
        
        agent = new Agent("Asiana Homes", "LIC-2024-001");
        owner = new Owner("Sean Serrano");
        buyer = new Buyer("De Guzman", 5_000_000);

        HouseAndLot prop1 = new HouseAndLot("B1L5", 120, 2_500_000, "Nadine", 80);
        HouseAndLot prop2 = new HouseAndLot("B2L10", 150, 4_200_000, "Elaine", 120);
        HouseAndLot prop3 = new HouseAndLot("B3L7", 100, 1_800_000, "Eunice", 50);
        allProperties.add(prop1);
        allProperties.add(prop2);
        allProperties.add(prop3);

        agent.addListing(prop1);
        agent.addListing(prop2);
        agent.addListing(prop3);

        owner.addProperty(prop1);
        owner.addProperty(prop2);
        owner.addProperty(prop3);
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

