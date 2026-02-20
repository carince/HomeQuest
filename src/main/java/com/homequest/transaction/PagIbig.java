package com.homequest.transaction;

import com.homequest.model.Buyer;
import com.homequest.model.Property;
import com.homequest.util.FinancialEngine;

public class PagIbig extends InstallmentPayment {
    private String name;
    
    public PagIbig(Property targetProperty, Buyer client, int termYears) {
        super(targetProperty, client, termYears);
        this.name = "Pag-IBIG Fund";
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public void finalizeTransaction() {
        System.out.println("\n=== PAG-IBIG FINANCING ===");
        System.out.println("Financing through: " + name);
        System.out.println("Interest Rate: " + (FinancialEngine.BANK_RATE * 100) + "%");
        super.finalize();
    }
    
    @Override
    public String toString() {
        return "Pag-IBIG Financing [" + super.toString() + 
               ", Monthly: ₱" + String.format("%.2f", monthlyInstallment) + "]";
    }
}
