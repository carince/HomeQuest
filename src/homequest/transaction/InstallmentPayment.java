package homequest.transaction;

import homequest.model.Buyer;
import homequest.model.Property;
import homequest.util.FinancialEngine;
import homequest.util.PropertyStatus;

public abstract class InstallmentPayment extends Transaction {
    protected int termMonths;
    protected double monthlyInstallment;
    protected double finalLumpsum;
    
    public InstallmentPayment(Property targetProperty, Buyer client, int termYears) {
        super(targetProperty, client);
        this.termMonths = termYears * 12;
        
        double tcp = targetProperty.getTCP();
        double principal = tcp - FinancialEngine.RESERVATION_FEE;
        
        this.monthlyInstallment = FinancialEngine.getMonthlyAmortization(principal, termYears);
        this.finalLumpsum = 0;
    }
    
    public int getTermMonths() {
        return termMonths;
    }
    
    public double getMonthlyInstallment() {
        return monthlyInstallment;
    }
    
    public double getFinalLumpsum() {
        return finalLumpsum;
    }
    
    public void finalize() {
        if (client.deductFunds(FinancialEngine.RESERVATION_FEE)) {
            targetProperty.setStatus(PropertyStatus.RESERVED);
            client.addTransaction(this);
            
            System.out.println("✓ Installment transaction initiated successfully!");
            System.out.println("  Transaction ID: " + transactionID);
            System.out.println("  Reservation Fee Paid: ₱" + String.format("%.2f", FinancialEngine.RESERVATION_FEE));
            System.out.println("  Monthly Installment: ₱" + String.format("%.2f", monthlyInstallment));
            System.out.println("  Term: " + (termMonths / 12) + " years (" + termMonths + " months)");
            System.out.println("  Remaining Balance: ₱" + String.format("%.2f", client.getWalletBalance()));
        } else {
            System.out.println("✗ Insufficient funds for reservation fee!");
            System.out.println("  Required: ₱" + String.format("%.2f", FinancialEngine.RESERVATION_FEE));
            System.out.println("  Available: ₱" + String.format("%.2f", client.getWalletBalance()));
        }
    }
    
    @Override
    public void finalizeTransaction() {
        finalize();
    }
}
