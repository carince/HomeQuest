package com.homequest;

import com.homequest.model.*;
import com.homequest.transaction.*;
import com.homequest.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HomeQuest {
    private Scanner input;
    private List<User> users;
    private List<Property> properties;
    private List<Agent> agents;
    private List<Buyer> buyers;
    private List<Owner> owners;
    
    // Current logged in user context
    private Agent currentAgent;
    private Buyer currentBuyer;
    private Owner currentOwner;
    
    public HomeQuest() {
        this.input = new Scanner(System.in);
        this.users = new ArrayList<>();
        this.properties = new ArrayList<>();
        this.agents = new ArrayList<>();
        this.buyers = new ArrayList<>();
        this.owners = new ArrayList<>();
        
        // Initialize with sample data
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        // Create sample agents
        Agent agent1 = new Agent("Maria Santos", "LIC-2024-001");
        Agent agent2 = new Agent("Juan dela Cruz", "LIC-2024-002");
        agents.add(agent1);
        agents.add(agent2);
        users.add(agent1);
        users.add(agent2);
        
        // Create sample owners
        Owner owner1 = new Owner("ABC Development Corp");
        Owner owner2 = new Owner("XYZ Realty Inc");
        owners.add(owner1);
        owners.add(owner2);
        users.add(owner1);
        users.add(owner2);
        
        // Create sample buyers
        Buyer buyer1 = new Buyer("Pedro Reyes", 5_000_000);
        Buyer buyer2 = new Buyer("Ana Lopez", 8_000_000);
        buyers.add(buyer1);
        buyers.add(buyer2);
        users.add(buyer1);
        users.add(buyer2);
        
        // Create sample properties
        HouseAndLot prop1 = new HouseAndLot("B1L5", 120, 2_500_000, "Camella Classic", 80);
        HouseAndLot prop2 = new HouseAndLot("B2L10", 150, 4_200_000, "Vista Grande", 120);
        HouseAndLot prop3 = new HouseAndLot("B3L7", 100, 1_800_000, "Urban Deca Homes", 50);
        
        properties.add(prop1);
        properties.add(prop2);
        properties.add(prop3);
        
        agent1.addListing(prop1);
        agent1.addListing(prop2);
        agent2.addListing(prop3);
        
        owner1.addProperty(prop1);
        owner1.addProperty(prop2);
        owner2.addProperty(prop3);
    }
    
    public void displayHeader() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         🏠 HOMEQUEST SYSTEM 🏠        ║");
        System.out.println("║    Real Estate Management Platform    ║");
        System.out.println("╚════════════════════════════════════════╝");
    }
    
    // ============ LOGIN METHODS ============
    
    public void loginAsAgent() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│         AGENT LOGIN                    │");
        System.out.println("└────────────────────────────────────────┘");
        
        if (agents.isEmpty()) {
            System.out.println("No agents registered.");
            return;
        }
        
        System.out.println("\nSelect Agent:");
        for (int i = 0; i < agents.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + agents.get(i));
        }
        System.out.print("Enter choice: ");
        int choice = input.nextInt() - 1;
        input.nextLine();
        
        if (choice >= 0 && choice < agents.size()) {
            currentAgent = agents.get(choice);
            System.out.println("\n✓ Logged in as: " + currentAgent.getName());
            agentWorkspace();
        } else {
            System.out.println("✗ Invalid selection.");
        }
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
    
    public void loginAsBuyer() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│         BUYER LOGIN                    │");
        System.out.println("└────────────────────────────────────────┘");
        
        if (buyers.isEmpty()) {
            System.out.println("No buyers registered. Creating new buyer...");
            createNewBuyer();
            if (!buyers.isEmpty()) {
                currentBuyer = buyers.get(buyers.size() - 1);
                buyerWorkspace();
            }
            return;
        }
        
        System.out.println("\nSelect Buyer:");
        for (int i = 0; i < buyers.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + buyers.get(i));
        }
        System.out.println("  [" + (buyers.size() + 1) + "] Create New Buyer");
        System.out.print("Enter choice: ");
        int choice = input.nextInt() - 1;
        input.nextLine();
        
        if (choice >= 0 && choice < buyers.size()) {
            currentBuyer = buyers.get(choice);
            System.out.println("\n✓ Logged in as: " + currentBuyer.getName());
            buyerWorkspace();
        } else if (choice == buyers.size()) {
            createNewBuyer();
            if (!buyers.isEmpty()) {
                currentBuyer = buyers.get(buyers.size() - 1);
                buyerWorkspace();
            }
        } else {
            System.out.println("✗ Invalid selection.");
        }
    }
    
    // ============ WORKSPACES ============
    
    public void agentWorkspace() {
        boolean inWorkspace = true;
        while (inWorkspace) {
            displayAgentMenu();
            
            try {
                int choice = input.nextInt();
                input.nextLine();
                
                switch (choice) {
                    case 1:
                        viewAgentListings();
                        break;
                    case 2:
                        viewAllProperties();
                        break;
                    case 3:
                        processSaleAsAgent();
                        break;
                    case 4:
                        viewAllBuyers();
                        break;
                    case 5:
                        financialCalculator();
                        break;
                    case 0:
                        System.out.println("\n✓ Logged out successfully.");
                        currentAgent = null;
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
    
    public void buyerWorkspace() {
        boolean inWorkspace = true;
        while (inWorkspace) {
            displayBuyerMenu();
            
            try {
                int choice = input.nextInt();
                input.nextLine();
                
                switch (choice) {
                    case 1:
                        browseAvailableProperties();
                        break;
                    case 2:
                        purchasePropertyAsBuyer();
                        break;
                    case 3:
                        viewMyPurchaseHistory();
                        break;
                    case 4:
                        checkWalletBalance();
                        break;
                    case 5:
                        addFundsToWallet();
                        break;
                    case 6:
                        financialCalculator();
                        break;
                    case 0:
                        System.out.println("\n✓ Logged out successfully.");
                        currentBuyer = null;
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
    
    // ============ AGENT METHODS ============
    
    public void viewAgentListings() {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("          MY LISTINGS - " + currentAgent.getName());
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        if (currentAgent.getListings().isEmpty()) {
            System.out.println("No properties in your listings.");
        } else {
            for (int i = 0; i < currentAgent.getListings().size(); i++) {
                System.out.println("\n[" + (i + 1) + "] " + currentAgent.getListings().get(i));
            }
        }
        System.out.println("\n═══════════════════════════════════════════════════════════════");
    }
    
    public void processSaleAsAgent() {
        purchaseProperty(); // Uses existing purchase method but in agent context
    }
    
    public void viewAllBuyers() {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("                    ALL BUYERS");
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        if (buyers.isEmpty()) {
            System.out.println("No buyers registered.");
        } else {
            for (Buyer buyer : buyers) {
                System.out.println("  • " + buyer);
            }
        }
        System.out.println("\n═══════════════════════════════════════════════════════════════");
    }
    
    // ============ OWNER METHODS ============
    
    public void viewOwnerProperties() {
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
    
    public void addNewProperty() {
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
    
    public void viewAllAgents() {
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
    
    public void assignPropertyToAgent() {
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
    
    public void viewPropertySalesStatus() {
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
    
    // ============ BUYER METHODS ============
    
    public void browseAvailableProperties() {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("              BROWSE AVAILABLE PROPERTIES");
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        List<Property> availableProps = new ArrayList<>();
        for (Property prop : properties) {
            if (prop.getStatus() == PropertyStatus.AVAILABLE) {
                availableProps.add(prop);
            }
        }
        
        if (availableProps.isEmpty()) {
            System.out.println("No available properties at the moment.");
        } else {
            for (int i = 0; i < availableProps.size(); i++) {
                System.out.println("\n[" + (i + 1) + "] " + availableProps.get(i));
            }
        }
        System.out.println("\n═══════════════════════════════════════════════════════════════");
    }
    
    public void purchasePropertyAsBuyer() {
        List<Property> availableProps = new ArrayList<>();
        for (Property prop : properties) {
            if (prop.getStatus() == PropertyStatus.AVAILABLE) {
                availableProps.add(prop);
            }
        }
        
        if (availableProps.isEmpty()) {
            System.out.println("\n✗ No available properties.");
            return;
        }
        
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│       PURCHASE PROPERTY                │");
        System.out.println("└────────────────────────────────────────┘");
        
        System.out.println("\nSelect Property:");
        for (int i = 0; i < availableProps.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + availableProps.get(i));
        }
        System.out.print("Enter choice: ");
        int propChoice = input.nextInt() - 1;
        input.nextLine();
        
        if (propChoice < 0 || propChoice >= availableProps.size()) {
            System.out.println("✗ Invalid property selection.");
            return;
        }
        Property selectedProperty = availableProps.get(propChoice);
        
        if (agents.isEmpty()) {
            System.out.println("✗ No agents available.");
            return;
        }
        
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
        
        processPayment(selectedProperty, currentBuyer, selectedAgent);
    }
    
    public void viewMyPurchaseHistory() {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("     MY PURCHASE HISTORY - " + currentBuyer.getName());
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        if (currentBuyer.getPurchaseHistory().isEmpty()) {
            System.out.println("No purchase history.");
        } else {
            for (int i = 0; i < currentBuyer.getPurchaseHistory().size(); i++) {
                System.out.println("\n[" + (i + 1) + "] " + currentBuyer.getPurchaseHistory().get(i));
            }
        }
        System.out.println("\n═══════════════════════════════════════════════════════════════");
    }
    
    public void checkWalletBalance() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│       WALLET BALANCE                   │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.println("\nBuyer: " + currentBuyer.getName());
        System.out.println("Current Balance: ₱" + String.format("%,.2f", currentBuyer.getWalletBalance()));
    }
    
    public void addFundsToWallet() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│       ADD FUNDS                        │");
        System.out.println("└────────────────────────────────────────┘");
        
        System.out.println("\nCurrent Balance: ₱" + String.format("%,.2f", currentBuyer.getWalletBalance()));
        System.out.print("Enter amount to add (₱): ");
        double amount = input.nextDouble();
        input.nextLine();
        
        if (amount > 0) {
            currentBuyer.addFunds(amount);
            System.out.println("\n✓ Funds added successfully!");
            System.out.println("  New Balance: ₱" + String.format("%,.2f", currentBuyer.getWalletBalance()));
        } else {
            System.out.println("✗ Invalid amount.");
        }
    }
    
    // ============ SHARED METHODS ============
    
    public void processPayment(Property selectedProperty, Buyer buyer, Agent agent) {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│       PAYMENT METHOD                   │");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. Spot Cash                           │");
        System.out.println("│ 2. Check                               │");
        System.out.println("│ 3. Bank Financing                      │");
        System.out.println("│ 4. Pag-IBIG Financing                  │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("Enter choice: ");
        int paymentChoice = input.nextInt();
        input.nextLine();
        
        Transaction transaction = null;
        
        switch (paymentChoice) {
            case 1:
                transaction = new Spot(selectedProperty, buyer, agent);
                break;
            case 2:
                transaction = new Check(selectedProperty, buyer, agent);
                break;
            case 3:
                System.out.print("Enter bank name: ");
                String bankName = input.nextLine();
                System.out.print("Enter loan term (years): ");
                int bankTerm = input.nextInt();
                input.nextLine();
                transaction = new Bank(selectedProperty, buyer, bankTerm, bankName);
                break;
            case 4:
                System.out.print("Enter loan term (years): ");
                int pagibigTerm = input.nextInt();
                input.nextLine();
                transaction = new PagIbig(selectedProperty, buyer, pagibigTerm);
                break;
            default:
                System.out.println("✗ Invalid payment method.");
                return;
        }
        
        if (transaction != null) {
            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("           TRANSACTION SUMMARY");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Property: " + selectedProperty.getBlockLot());
            System.out.println("TCP: ₱" + String.format("%.2f", selectedProperty.getTCP()));
            System.out.println("Buyer: " + buyer.getName());
            System.out.println("Agent: " + agent.getName());
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            transaction.finalizeTransaction();
        }
    }
    
    public void displayMainMenu() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│         ROLE SELECTION                 │");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. Login as Agent                      │");
        System.out.println("│ 2. Login as Owner                      │");
        System.out.println("│ 3. Login as Buyer                      │");
        System.out.println("│ 4. View All Properties (Guest)         │");
        System.out.println("│ 5. Financial Calculator (Guest)        │");
        System.out.println("│ 0. Exit                                │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("Enter your choice: ");
    }
    
    public void displayAgentMenu() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│       AGENT WORKSPACE                  │");
        System.out.println("│  Logged in: " + currentAgent.getName() + String.format("%" + (26 - currentAgent.getName().length()) + "s", "") + "│");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. View My Listings                    │");
        System.out.println("│ 2. View All Available Properties       │");
        System.out.println("│ 3. Process Sale Transaction            │");
        System.out.println("│ 4. View All Buyers                     │");
        System.out.println("│ 5. Financial Calculator                │");
        System.out.println("│ 0. Logout                              │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("Enter your choice: ");
    }
    
    public void displayOwnerMenu() {
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
    
    public void displayBuyerMenu() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│       BUYER WORKSPACE                  │");
        System.out.println("│  Logged in: " + currentBuyer.getName() + String.format("%" + (26 - currentBuyer.getName().length()) + "s", "") + "│");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. Browse Available Properties         │");
        System.out.println("│ 2. Purchase Property                   │");
        System.out.println("│ 3. View My Purchase History            │");
        System.out.println("│ 4. Check My Wallet Balance             │");
        System.out.println("│ 5. Add Funds to Wallet                 │");
        System.out.println("│ 6. Financial Calculator                │");
        System.out.println("│ 0. Logout                              │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("Enter your choice: ");
    }
    
    public void viewAllProperties() {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("                    AVAILABLE PROPERTIES");
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        if (properties.isEmpty()) {
            System.out.println("No properties available.");
        } else {
            for (int i = 0; i < properties.size(); i++) {
                System.out.println("\n[" + (i + 1) + "] " + properties.get(i));
            }
        }
        System.out.println("\n═══════════════════════════════════════════════════════════════");
    }
    
    public void viewAllUsers() {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("                      ALL USERS");
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        System.out.println("\n--- AGENTS ---");
        for (Agent agent : agents) {
            System.out.println("  • " + agent);
            System.out.println("    Listings: " + agent.getListings().size());
        }
        
        System.out.println("\n--- BUYERS ---");
        for (Buyer buyer : buyers) {
            System.out.println("  • " + buyer);
            System.out.println("    Purchases: " + buyer.getPurchaseHistory().size());
        }
        
        System.out.println("\n--- OWNERS ---");
        for (Owner owner : owners) {
            System.out.println("  • " + owner);
        }
        
        System.out.println("\n═══════════════════════════════════════════════════════════════");
    }
    
    public void createNewBuyer() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│        CREATE NEW BUYER                │");
        System.out.println("└────────────────────────────────────────┘");
        
        System.out.print("Enter buyer name: ");
        String name = input.nextLine();
        
        System.out.print("Enter initial wallet balance (₱): ");
        double balance = input.nextDouble();
        input.nextLine(); // consume newline
        
        Buyer newBuyer = new Buyer(name, balance);
        buyers.add(newBuyer);
        users.add(newBuyer);
        
        System.out.println("\n✓ Buyer created successfully!");
        System.out.println("  " + newBuyer);
    }
    
    public void purchaseProperty() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│       PURCHASE PROPERTY                │");
        System.out.println("└────────────────────────────────────────┘");
        
        // Select buyer
        if (buyers.isEmpty()) {
            System.out.println("No buyers available. Please create a buyer first.");
            return;
        }
        
        System.out.println("\nSelect Buyer:");
        for (int i = 0; i < buyers.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + buyers.get(i));
        }
        System.out.print("Enter choice: ");
        int buyerChoice = input.nextInt() - 1;
        input.nextLine();
        
        if (buyerChoice < 0 || buyerChoice >= buyers.size()) {
            System.out.println("Invalid buyer selection.");
            return;
        }
        Buyer selectedBuyer = buyers.get(buyerChoice);
        
        // Select property
        List<Property> availableProps = new ArrayList<>();
        for (Property prop : properties) {
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
            System.out.println("  [" + (i + 1) + "] " + availableProps.get(i));
        }
        System.out.print("Enter choice: ");
        int propChoice = input.nextInt() - 1;
        input.nextLine();
        
        if (propChoice < 0 || propChoice >= availableProps.size()) {
            System.out.println("Invalid property selection.");
            return;
        }
        Property selectedProperty = availableProps.get(propChoice);
        
        // Select agent
        if (agents.isEmpty()) {
            System.out.println("No agents available.");
            return;
        }
        
        System.out.println("\nSelect Agent:");
        for (int i = 0; i < agents.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + agents.get(i));
        }
        System.out.print("Enter choice: ");
        int agentChoice = input.nextInt() - 1;
        input.nextLine();
        
        if (agentChoice < 0 || agentChoice >= agents.size()) {
            System.out.println("Invalid agent selection.");
            return;
        }
        Agent selectedAgent = agents.get(agentChoice);
        
        // Select payment method
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│       PAYMENT METHOD                   │");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. Spot Cash                           │");
        System.out.println("│ 2. Check                               │");
        System.out.println("│ 3. Bank Financing                      │");
        System.out.println("│ 4. Pag-IBIG Financing                  │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("Enter choice: ");
        int paymentChoice = input.nextInt();
        input.nextLine();
        
        Transaction transaction = null;
        
        switch (paymentChoice) {
            case 1:
                transaction = new Spot(selectedProperty, selectedBuyer, selectedAgent);
                break;
            case 2:
                transaction = new Check(selectedProperty, selectedBuyer, selectedAgent);
                break;
            case 3:
                System.out.print("Enter bank name: ");
                String bankName = input.nextLine();
                System.out.print("Enter loan term (years): ");
                int bankTerm = input.nextInt();
                input.nextLine();
                transaction = new Bank(selectedProperty, selectedBuyer, bankTerm, bankName);
                break;
            case 4:
                System.out.print("Enter loan term (years): ");
                int pagibigTerm = input.nextInt();
                input.nextLine();
                transaction = new PagIbig(selectedProperty, selectedBuyer, pagibigTerm);
                break;
            default:
                System.out.println("Invalid payment method.");
                return;
        }
        
        if (transaction != null) {
            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("           TRANSACTION SUMMARY");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Property: " + selectedProperty.getBlockLot());
            System.out.println("TCP: ₱" + String.format("%.2f", selectedProperty.getTCP()));
            System.out.println("Buyer: " + selectedBuyer.getName());
            System.out.println("Agent: " + selectedAgent.getName());
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            transaction.finalizeTransaction();
        }
    }
    
    public void viewBuyerHistory() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│    BUYER PURCHASE HISTORY              │");
        System.out.println("└────────────────────────────────────────┘");
        
        if (buyers.isEmpty()) {
            System.out.println("No buyers available.");
            return;
        }
        
        System.out.println("\nSelect Buyer:");
        for (int i = 0; i < buyers.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + buyers.get(i));
        }
        System.out.print("Enter choice: ");
        int choice = input.nextInt() - 1;
        input.nextLine();
        
        if (choice < 0 || choice >= buyers.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        
        Buyer selectedBuyer = buyers.get(choice);
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("Purchase History for: " + selectedBuyer.getName());
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        if (selectedBuyer.getPurchaseHistory().isEmpty()) {
            System.out.println("No purchase history.");
        } else {
            for (int i = 0; i < selectedBuyer.getPurchaseHistory().size(); i++) {
                System.out.println("\n[" + (i + 1) + "] " + selectedBuyer.getPurchaseHistory().get(i));
            }
        }
        System.out.println("\n═══════════════════════════════════════════════════════════════");
    }
    
    public void financialCalculator() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│      FINANCIAL CALCULATOR              │");
        System.out.println("└────────────────────────────────────────┘");
        
        System.out.print("Enter base price (₱): ");
        double basePrice = input.nextDouble();
        input.nextLine();
        
        System.out.print("Is this a lot only? (y/n): ");
        String isLotStr = input.nextLine();
        boolean isLot = isLotStr.equalsIgnoreCase("y");
        
        double vat = FinancialEngine.calculateVAT(basePrice, isLot);
        double otherCharges = FinancialEngine.computeOtherCharges(basePrice);
        double tcp = basePrice + vat + otherCharges;
        
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("                    FINANCIAL BREAKDOWN");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Base Price:        ₱" + String.format("%,15.2f", basePrice));
        System.out.println("VAT (12%):         ₱" + String.format("%,15.2f", vat));
        System.out.println("Other Charges:     ₱" + String.format("%,15.2f", otherCharges));
        System.out.println("───────────────────────────────────────────────────────────────");
        System.out.println("Total (TCP):       ₱" + String.format("%,15.2f", tcp));
        System.out.println("═══════════════════════════════════════════════════════════════");
        
        System.out.print("\nCalculate monthly amortization? (y/n): ");
        String calcAmort = input.nextLine();
        
        if (calcAmort.equalsIgnoreCase("y")) {
            System.out.print("Enter loan term (years): ");
            int years = input.nextInt();
            input.nextLine();
            
            double monthlyPayment = FinancialEngine.getMonthlyAmortization(tcp - FinancialEngine.RESERVATION_FEE, years);
            
            System.out.println("\n───────────────────────────────────────────────────────────────");
            System.out.println("LOAN DETAILS:");
            System.out.println("Reservation Fee:   ₱" + String.format("%,15.2f", FinancialEngine.RESERVATION_FEE));
            System.out.println("Loan Amount:       ₱" + String.format("%,15.2f", (tcp - FinancialEngine.RESERVATION_FEE)));
            System.out.println("Interest Rate:     " + (FinancialEngine.BANK_RATE * 100) + "% per annum");
            System.out.println("Term:              " + years + " years (" + (years * 12) + " months)");
            System.out.println("───────────────────────────────────────────────────────────────");
            System.out.println("Monthly Payment:   ₱" + String.format("%,15.2f", monthlyPayment));
            System.out.println("═══════════════════════════════════════════════════════════════");
        }
    }
    
    public void run() {
        displayHeader();
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            
            try {
                int choice = input.nextInt();
                input.nextLine(); // consume newline
                
                switch (choice) {
                    case 1:
                        loginAsAgent();
                        break;
                    case 2:
                        loginAsOwner();
                        break;
                    case 3:
                        loginAsBuyer();
                        break;
                    case 4:
                        viewAllProperties();
                        break;
                    case 5:
                        financialCalculator();
                        break;
                    case 0:
                        System.out.println("\n╔════════════════════════════════════════╗");
                        System.out.println("║  Thank you for using HomeQuest! 🏠    ║");
                        System.out.println("╚════════════════════════════════════════╝\n");
                        running = false;
                        break;
                    default:
                        System.out.println("\n✗ Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("\n✗ Invalid input. Please try again.");
                input.nextLine(); // clear buffer
            }
        }
        
        input.close();
    }
    
    public static void main(String[] args) {
        HomeQuest app = new HomeQuest();
        app.run();
    }
}
