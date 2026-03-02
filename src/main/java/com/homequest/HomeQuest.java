package com.homequest;

import com.homequest.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HomeQuest {
    private Scanner input;
    private List<Property> properties;
    private Agent agent;
    private Buyer buyer;
    private Owner owner;

    public HomeQuest() {
        this.input = new Scanner(System.in);
        this.properties = new ArrayList<>();
        initializeSampleData();
    }

    private void initializeSampleData() {
        agent = new Agent("Maria Santos", "LIC-2024-001");
        owner = new Owner("ABC Development Corp");
        buyer = new Buyer("Pedro Reyes", 5_000_000);

        HouseAndLot prop1 = new HouseAndLot("B1L5", 120, 2_500_000, "Camella Classic", 80);
        HouseAndLot prop2 = new HouseAndLot("B2L10", 150, 4_200_000, "Vista Grande", 120);
        HouseAndLot prop3 = new HouseAndLot("B3L7", 100, 1_800_000, "Urban Deca Homes", 50);
        properties.add(prop1);
        properties.add(prop2);
        properties.add(prop3);

        agent.addListing(prop1);
        agent.addListing(prop2);
        agent.addListing(prop3);

        owner.addProperty(prop1);
        owner.addProperty(prop2);
        owner.addProperty(prop3);
    }

    public void viewAllProperties() {
        System.out.println("\n--- ALL PROPERTIES ---");
        if (properties.isEmpty()) {
            System.out.println("No properties available.");
        } else {
            for (int i = 0; i < properties.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + properties.get(i));
            }
        }
    }

    public void run() {
        System.out.println("\n=== HOMEQUEST SYSTEM ===");
        boolean running = true;
        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Login as Agent");
            System.out.println("2. Login as Owner");
            System.out.println("3. Login as Buyer");
            System.out.println("4. View All Properties");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            try {
                int choice = input.nextInt();
                input.nextLine();
                switch (choice) {
                    case 1:
                        agent.workspace(input, properties, buyer);
                        break;
                    case 2:
                        owner.workspace(input, properties, agent);
                        break;
                    case 3:
                        buyer.workspace(input, properties, agent);
                        break;
                    case 4:
                        viewAllProperties();
                        break;
                    case 0:
                        System.out.println("\nThank you for using HomeQuest!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input.");
                input.nextLine();
            }
        }
        input.close();
    }

    public static void main(String[] args) {
        HomeQuest app = new HomeQuest();
        app.run();
    }
}
