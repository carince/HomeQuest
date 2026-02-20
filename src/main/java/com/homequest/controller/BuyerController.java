package com.homequest.controller;

import com.homequest.model.*;
import com.homequest.transaction.*;
import com.homequest.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BuyerController {
    private Scanner input;
    private List<Buyer> buyers;
    private List<Agent> agents;
    private List<Property> properties;
    private List<User> users;
    private Buyer currentBuyer;
    
    public BuyerController(Scanner input, List<Buyer> buyers, List<Agent> agents, List<Property> properties, List<User> users) {
        this.input = input;
        this.buyers = buyers;
        this.agents = agents;
        this.properties = properties;
        this.users = users;
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
    
    private void displayBuyerMenu() {
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
    
    private void createNewBuyer() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│        CREATE NEW BUYER                │");
        System.out.println("└────────────────────────────────────────┘");
        
        System.out.print("Enter buyer name: ");
        String name = input.nextLine();
        
        System.out.print("Enter initial wallet balance (₱): ");
        double balance = input.nextDouble();
        input.nextLine();
        
        Buyer newBuyer = new Buyer(name, balance);
        buyers.add(newBuyer);
        users.add(newBuyer);
        
        System.out.println("\n✓ Buyer created successfully!");
        System.out.println("  " + newBuyer);
    }
    
    private void browseAvailableProperties() {
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
    
    private void purchasePropertyAsBuyer() {
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
    
    private void viewMyPurchaseHistory() {
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
    
    private void checkWalletBalance() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│       WALLET BALANCE                   │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.println("\nBuyer: " + currentBuyer.getName());
        System.out.println("Current Balance: ₱" + String.format("%,.2f", currentBuyer.getWalletBalance()));
    }
    
    private void addFundsToWallet() {
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
    
    private void financialCalculator() {
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
    
    private void processPayment(Property selectedProperty, Buyer buyer, Agent agent) {
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
}
