package com.homequest.controller;

import com.homequest.model.*;
import com.homequest.util.*;

import java.util.List;
import java.util.Scanner;

public class OwnerController {
    private Scanner input;
    private List<Owner> owners;
    private List<Agent> agents;
    private List<Property> properties;
    private Owner currentOwner;
    
    public OwnerController(Scanner input, List<Owner> owners, List<Agent> agents, List<Property> properties) {
        this.input = input;
        this.owners = owners;
        this.agents = agents;
        this.properties = properties;
    }
    
    public void loginAsOwner() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│         OWNER LOGIN                    │");
        System.out.println("└────────────────────────────────────────┘");
        
        if (owners.isEmpty()) {
            System.out.println("No owners registered.");
            return;
        }
        
        System.out.println("\nSelect Owner:");
        for (int i = 0; i < owners.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + owners.get(i));
        }
        System.out.print("Enter choice: ");
        int choice = input.nextInt() - 1;
        input.nextLine();
        
        if (choice >= 0 && choice < owners.size()) {
            currentOwner = owners.get(choice);
            System.out.println("\n✓ Logged in as: " + currentOwner.getName());
            ownerWorkspace();
        } else {
            System.out.println("✗ Invalid selection.");
        }
    }
    
    public void ownerWorkspace() {
        boolean inWorkspace = true;
        while (inWorkspace) {
            displayOwnerMenu();
            
            try {
                int choice = input.nextInt();
                input.nextLine();
                
                switch (choice) {
                    case 1:
                        viewOwnerProperties();
                        break;
                    case 2:
                        addNewProperty();
                        break;
                    case 3:
                        viewAllAgents();
                        break;
                    case 4:
                        assignPropertyToAgent();
                        break;
                    case 5:
                        viewPropertySalesStatus();
                        break;
                    case 0:
                        System.out.println("\n✓ Logged out successfully.");
                        currentOwner = null;
                        inWorkspace = false;
                        break;
                    default:
                        System.out.println("\n✗ Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("\n✗ Invalid input.");
                input.nextLine();
            }
        }
    }
    
    private void displayOwnerMenu() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│       OWNER WORKSPACE                  │");
        System.out.println("│  Logged in: " + currentOwner.getName() + String.format("%" + (26 - currentOwner.getName().length()) + "s", "") + "│");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. View My Properties                  │");
        System.out.println("│ 2. Add New Property                    │");
        System.out.println("│ 3. View All Agents                     │");
        System.out.println("│ 4. Assign Property to Agent            │");
        System.out.println("│ 5. View Property Sales Status          │");
        System.out.println("│ 0. Logout                              │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("Enter your choice: ");
    }
    
    private void viewOwnerProperties() {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("         MY PROPERTIES - " + currentOwner.getName());
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        if (currentOwner.getProperties().isEmpty()) {
            System.out.println("No properties owned.");
        } else {
            for (int i = 0; i < currentOwner.getProperties().size(); i++) {
                System.out.println("\n[" + (i + 1) + "] " + currentOwner.getProperties().get(i));
            }
        }
        System.out.println("\n═══════════════════════════════════════════════════════════════");
    }
    
    private void addNewProperty() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│        ADD NEW PROPERTY                │");
        System.out.println("└────────────────────────────────────────┘");
        
        System.out.print("Enter Block/Lot: ");
        String blockLot = input.nextLine();
        
        System.out.print("Enter lot area (sqm): ");
        double lotArea = input.nextDouble();
        input.nextLine();
        
        System.out.print("Enter base price (₱): ");
        double basePrice = input.nextDouble();
        input.nextLine();
        
        System.out.print("Enter house model name: ");
        String modelName = input.nextLine();
        
        System.out.print("Enter floor area (sqm): ");
        double floorArea = input.nextDouble();
        input.nextLine();
        
        HouseAndLot newProperty = new HouseAndLot(blockLot, lotArea, basePrice, modelName, floorArea);
        properties.add(newProperty);
        currentOwner.addProperty(newProperty);
        
        System.out.println("\n✓ Property added successfully!");
        System.out.println("  " + newProperty);
    }
    
    private void viewAllAgents() {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("                    ALL AGENTS");
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        if (agents.isEmpty()) {
            System.out.println("No agents registered.");
        } else {
            for (Agent agent : agents) {
                System.out.println("  • " + agent);
                System.out.println("    Listings: " + agent.getListings().size());
            }
        }
        System.out.println("\n═══════════════════════════════════════════════════════════════");
    }
    
    private void assignPropertyToAgent() {
        if (currentOwner.getProperties().isEmpty()) {
            System.out.println("\n✗ You don't have any properties to assign.");
            return;
        }
        
        if (agents.isEmpty()) {
            System.out.println("\n✗ No agents available.");
            return;
        }
        
        System.out.println("\nSelect Property:");
        for (int i = 0; i < currentOwner.getProperties().size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + currentOwner.getProperties().get(i).getBlockLot());
        }
        System.out.print("Enter choice: ");
        int propChoice = input.nextInt() - 1;
        input.nextLine();
        
        if (propChoice < 0 || propChoice >= currentOwner.getProperties().size()) {
            System.out.println("✗ Invalid property selection.");
            return;
        }
        
        Property selectedProperty = currentOwner.getProperties().get(propChoice);
        
        System.out.println("\nSelect Agent:");
        for (int i = 0; i < agents.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + agents.get(i));
        }
        System.out.print("Enter choice: ");
        int agentChoice = input.nextInt() - 1;
        input.nextLine();
        
        if (agentChoice < 0 || agentChoice >= agents.size()) {
            System.out.println("✗ Invalid agent selection.");
            return;
        }
        
        Agent selectedAgent = agents.get(agentChoice);
        selectedAgent.addListing(selectedProperty);
        
        System.out.println("\n✓ Property " + selectedProperty.getBlockLot() + " assigned to " + selectedAgent.getName());
    }
    
    private void viewPropertySalesStatus() {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("       PROPERTY SALES STATUS - " + currentOwner.getName());
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        if (currentOwner.getProperties().isEmpty()) {
            System.out.println("No properties owned.");
        } else {
            int available = 0, reserved = 0, sold = 0;
            
            for (Property prop : currentOwner.getProperties()) {
                System.out.println("\n• " + prop.getBlockLot() + " - Status: " + prop.getStatus());
                
                switch (prop.getStatus()) {
                    case AVAILABLE: available++; break;
                    case RESERVED: reserved++; break;
                    case SOLD: sold++; break;
                }
            }
            
            System.out.println("\n───────────────────────────────────────────────────────────────");
            System.out.println("Total Properties: " + currentOwner.getProperties().size());
            System.out.println("Available: " + available + " | Reserved: " + reserved + " | Sold: " + sold);
        }
        System.out.println("\n═══════════════════════════════════════════════════════════════");
    }
}
