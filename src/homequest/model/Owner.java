package homequest.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    public void setName(String name) {
        this.name = name;
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

    public void workspace(Scanner input, List<Property> allProperties, Agent agent) {
        System.out.println("\nLogged in as: " + name);
        boolean inWorkspace = true;
        while (inWorkspace) {
            System.out.println("\n=== OWNER WORKSPACE ===");
            System.out.println("1. View My Properties");
            System.out.println("2. Add New Property");
            System.out.println("3. View Agent Information");
            System.out.println("4. Assign Property to Agent");
            System.out.println("5. View Property Sales Status");
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            try {
                int choice = input.nextInt();
                input.nextLine();
                switch (choice) {
                    case 1:
                        viewMyProperties();
                        break;
                    case 2:
                        addNewProperty(input, allProperties);
                        break;
                    case 3:
                        System.out.println("\n" + agent);
                        System.out.println("Listings: " + agent.getListings().size());
                        break;
                    case 4:
                        assignPropertyToAgent(input, agent);
                        break;
                    case 5:
                        viewPropertySalesStatus();
                        break;
                    case 0:
                        System.out.println("Logged out.");
                        inWorkspace = false;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input.");
                input.nextLine();
            }
        }
    }

    private void viewMyProperties() {
        System.out.println("\n--- MY PROPERTIES ---");
        if (properties.isEmpty()) {
            System.out.println("No properties owned.");
        } else {
            for (int i = 0; i < properties.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + properties.get(i));
            }
        }
    }

    private void addNewProperty(Scanner input, List<Property> allProperties) {
        System.out.println("\n--- ADD NEW PROPERTY ---");
        System.out.print("Block/Lot: ");
        String blockLot = input.nextLine();
        System.out.print("Lot area (sqm): ");
        double lotArea = input.nextDouble();
        input.nextLine();
        System.out.print("Base price: ");
        double basePrice = input.nextDouble();
        input.nextLine();
        System.out.print("House model name: ");
        String modelName = input.nextLine();
        System.out.print("Floor area (sqm): ");
        double floorArea = input.nextDouble();
        input.nextLine();
        HouseAndLot newProperty = new HouseAndLot(blockLot, lotArea, basePrice, modelName, floorArea);
        allProperties.add(newProperty);
        addProperty(newProperty);
        System.out.println("Property added: " + newProperty);
    }

    private void assignPropertyToAgent(Scanner input, Agent agent) {
        if (properties.isEmpty()) {
            System.out.println("You don't have any properties to assign.");
            return;
        }
        System.out.println("\nSelect Property:");
        for (int i = 0; i < properties.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + properties.get(i).getBlockLot());
        }
        System.out.print("Choice: ");
        int propChoice = input.nextInt() - 1;
        input.nextLine();
        if (propChoice < 0 || propChoice >= properties.size()) {
            System.out.println("Invalid property selection.");
            return;
        }
        Property selectedProperty = properties.get(propChoice);
        agent.addListing(selectedProperty);
        System.out.println("Property " + selectedProperty.getBlockLot() + " assigned to " + agent.getName());
    }

    private void viewPropertySalesStatus() {
        System.out.println("\n--- PROPERTY SALES STATUS ---");
        if (properties.isEmpty()) {
            System.out.println("No properties owned.");
        } else {
            int available = 0, reserved = 0, sold = 0;
            for (Property prop : properties) {
                System.out.println(prop.getBlockLot() + " - Status: " + prop.getStatus());
                switch (prop.getStatus()) {
                    case AVAILABLE:
                        available++;
                        break;
                    case RESERVED:
                        reserved++;
                        break;
                    case SOLD:
                        sold++;
                        break;
                }
            }
            System.out.println("Total: " + properties.size() + " | Available: " + available + " | Reserved: " + reserved + " | Sold: " + sold);
        }
    }

    @Override
    public String toString() {
        return "Owner: " + name + " (" + properties.size() + " properties)";
    }
}
