package homequest.model;

import homequest.transaction.*;
import homequest.util.PropertyStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Agent {
    private String name;
    private String licenseNumber;
    private List<Property> listings;

    public Agent(String name, String licenseNumber) {
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.listings = new ArrayList<>();
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

    public void addListing(Property property) {
        this.listings.add(property);
    }

    public void removeListing(Property property) {
        this.listings.remove(property);
    }

    public void workspace(Scanner input, List<Property> allProperties, Buyer buyer) {
        System.out.println("\nLogged in as: " + name);
        boolean inWorkspace = true;
        while (inWorkspace) {
            System.out.println("\n=== AGENT WORKSPACE ===");
            System.out.println("1. View My Listings");
            System.out.println("2. View All Available Properties");
            System.out.println("3. Process Sale Transaction");
            System.out.println("4. View Buyer Information");
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            try {
                int choice = input.nextInt();
                input.nextLine();
                switch (choice) {
                    case 1:
                        viewListings();
                        break;
                    case 2:
                        viewAllProperties(allProperties);
                        break;
                    case 3:
                        processSale(input, allProperties, buyer, this);
                        break;
                    case 4:
                        System.out.println("\n" + buyer);
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

    private void viewListings() {
        System.out.println("\n--- MY LISTINGS ---");
        if (listings.isEmpty()) {
            System.out.println("No properties in your listings.");
        } else {
            for (int i = 0; i < listings.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + listings.get(i));
            }
        }
    }

    private void viewAllProperties(List<Property> allProperties) {
        System.out.println("\n--- ALL PROPERTIES ---");
        if (allProperties.isEmpty()) {
            System.out.println("No properties available.");
        } else {
            for (int i = 0; i < allProperties.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + allProperties.get(i));
            }
        }
    }

    private void processSale(Scanner input, List<Property> allProperties, Buyer buyer, Agent agent) {
        System.out.println("\n--- PROCESS SALE ---");
        System.out.println("Buyer: " + buyer);
        List<Property> availableProps = new ArrayList<>();
        for (Property prop : allProperties) {
            if (prop.getStatus() == PropertyStatus.AVAILABLE) {
                availableProps.add(prop);
            }
        }
        if (availableProps.isEmpty()) {
            System.out.println("No available properties.");
            return;
        }
        System.out.println("\nSelect Property:");
        for (int i = 0; i < availableProps.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + availableProps.get(i));
        }
        System.out.print("Choice: ");
        int propChoice = input.nextInt() - 1;
        input.nextLine();
        if (propChoice < 0 || propChoice >= availableProps.size()) {
            System.out.println("Invalid property selection.");
            return;
        }
        Property selectedProperty = availableProps.get(propChoice);
        processPayment(input, selectedProperty, buyer, agent);
    }

    private void processPayment(Scanner input, Property property, Buyer buyer, Agent agent) {
        System.out.println("\n--- PAYMENT METHOD ---");
        System.out.println("1. Spot Cash");
        System.out.println("2. Check");
        System.out.println("3. Bank Financing");
        System.out.println("4. Pag-IBIG Financing");
        System.out.print("Choice: ");
        int paymentChoice = input.nextInt();
        input.nextLine();
        Transaction transaction = null;
        switch (paymentChoice) {
            case 1:
                transaction = new Spot(property, buyer, agent);
                break;
            case 2:
                transaction = new Check(property, buyer, agent);
                break;
            case 3:
                System.out.print("Bank name: ");
                String bankName = input.nextLine();
                System.out.print("Loan term (years): ");
                int bankTerm = input.nextInt();
                input.nextLine();
                transaction = new Bank(property, buyer, bankTerm, bankName);
                break;
            case 4:
                System.out.print("Loan term (years): ");
                int pagibigTerm = input.nextInt();
                input.nextLine();
                transaction = new PagIbig(property, buyer, pagibigTerm);
                break;
            default:
                System.out.println("Invalid payment method.");
                return;
        }
        if (transaction != null) {
            System.out.println("\n--- TRANSACTION SUMMARY ---");
            System.out.println("Property: " + property.getBlockLot());
            System.out.println("TCP: P" + String.format("%.2f", property.getTCP()));
            System.out.println("Buyer: " + buyer.getName());
            System.out.println("Agent: " + agent.getName());
            transaction.finalizeTransaction();
        }
    }

    @Override
    public String toString() {
        return "Agent: " + name + " (License: " + licenseNumber + ")";
    }
}
