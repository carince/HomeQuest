package com.homequest.transaction;

import com.homequest.model.Buyer;
import com.homequest.model.Property;
import com.homequest.util.FinancialEngine;

public class Bank extends InstallmentPayment {
    private String name;
    
    public Bank(Property targetProperty, Buyer client, int termYears, String bankName) {
        super(targetProperty, client, termYears);
        this.name = bankName;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public void finalizeTransaction() {
        System.out.println("\n=== BANK FINANCING ===");
        System.out.println("Bank: " + name);
        System.out.println("Interest Rate: " + (FinancialEngine.BANK_RATE * 100) + "%");
        super.finalize();
    }
    
    @Override
    public String toString() {
        return "Bank Financing [" + super.toString() + ", Bank: " + name + 
               ", Monthly: ₱" + String.format("%.2f", monthlyInstallment) + "]";
    }
}
