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
    
    public boolean processInstallmentStart() {
        if (!client.deductFunds(FinancialEngine.RESERVATION_FEE)) {
            return false;
        }

        targetProperty.setStatus(PropertyStatus.RESERVED);
        client.addTransaction(this);
        return true;
    }
    
    @Override
    public boolean finalizeTransaction() {
        return processInstallmentStart();
    }
}
