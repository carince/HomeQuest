package com.homequest.model;

import com.homequest.transaction.*;
import com.homequest.util.PropertyStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Buyer {
    private String name;
    private double walletBalance;
    private List<Transaction> purchaseHistory;

    public Buyer(String name, double initialBalance) {
        this.name = name;
        this.walletBalance = initialBalance;
        this.purchaseHistory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public void addFunds(double amount) {
        this.walletBalance += amount;
    }

    public boolean deductFunds(double amount) {
        if (this.walletBalance >= amount) {
            this.walletBalance -= amount;
            return true;
        }
        return false;
    }

    public List<Transaction> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void addTransaction(Transaction transaction) {
        this.purchaseHistory.add(transaction);
    }

    public void workspace(Scanner input, List<Property> allProperties, Agent agent) {
        System.out.println("\nLogged in as: " + name);
        boolean inWorkspace = true;
        while (inWorkspace) {
            System.out.println("\n=== BUYER WORKSPACE ===");
            System.out.println("1. Browse Available Properties");
            System.out.println("2. Purchase Property");
            System.out.println("3. View My Purchase History");
            System.out.println("4. Wallet (Balance: P" + String.format("%.2f", walletBalance) + ")");
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            try {
                int choice = input.nextInt();
                input.nextLine();
                switch (choice) {
                    case 1:
                        browseAvailableProperties(allProperties);
                        break;
                    case 2:
                        purchaseProperty(input, allProperties, agent);
                        break;
                    case 3:
                        viewPurchaseHistory();
                        break;
                    case 4:
                        manageWallet(input);
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

    private void browseAvailableProperties(List<Property> allProperties) {
        System.out.println("\n--- AVAILABLE PROPERTIES ---");
        List<Property> availableProps = new ArrayList<>();
        for (Property prop : allProperties) {
            if (prop.getStatus() == PropertyStatus.AVAILABLE) {
                availableProps.add(prop);
            }
        }
        if (availableProps.isEmpty()) {
            System.out.println("No available properties.");
        } else {
            for (int i = 0; i < availableProps.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + availableProps.get(i));
            }
        }
    }

    private void purchaseProperty(Scanner input, List<Property> allProperties, Agent agent) {
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
        System.out.println("\n--- PURCHASE PROPERTY ---");
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
        System.out.println("\nAgent: " + agent);
        processPayment(input, selectedProperty, this, agent);
    }

    private void viewPurchaseHistory() {
        System.out.println("\n--- MY PURCHASE HISTORY ---");
        if (purchaseHistory.isEmpty()) {
            System.out.println("No purchase history.");
        } else {
            for (int i = 0; i < purchaseHistory.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + purchaseHistory.get(i));
            }
        }
    }

    private void manageWallet(Scanner input) {
        System.out.println("\n--- WALLET MANAGER ---");
        System.out.println("Current Balance: P" + String.format("%.2f", walletBalance));
        System.out.print("Enter amount to add (0 to cancel): ");
        double amount = input.nextDouble();
        input.nextLine();
        if (amount > 0) {
            addFunds(amount);
            System.out.println("Funds added. New Balance: P" + String.format("%.2f", walletBalance));
        }
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
        return "Buyer: " + name + " (Balance: ₱" + String.format("%.2f", walletBalance) + ")";
    }
}
