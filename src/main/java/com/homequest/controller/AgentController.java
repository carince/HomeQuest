package com.homequest.controller;

import com.homequest.model.*;
import com.homequest.transaction.*;
import com.homequest.util.*;

import java.util.List;
import java.util.Scanner;

public class AgentController {
    private Scanner input;
    private List<Agent> agents;
    private List<Buyer> buyers;
    private List<Property> properties;
    private Agent currentAgent;
    
    public AgentController(Scanner input, List<Agent> agents, List<Buyer> buyers, List<Property> properties) {
        this.input = input;
        this.agents = agents;
        this.buyers = buyers;
        this.properties = properties;
    }
    
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
    
    private void displayAgentMenu() {
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
    
    private void viewAgentListings() {
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
    
    private void viewAllProperties() {
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
    
    private void processSaleAsAgent() {
        if (buyers.isEmpty()) {
            System.out.println("No buyers available.");
            return;
        }
        
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│       PROCESS SALE TRANSACTION         │");
        System.out.println("└────────────────────────────────────────┘");
        
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
        
        java.util.List<Property> availableProps = new java.util.ArrayList<>();
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
        
        processPayment(selectedProperty, selectedBuyer, currentAgent);
    }
    
    private void viewAllBuyers() {
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
