package homequest.transaction;

import homequest.model.Agent;
import homequest.model.Buyer;
import homequest.model.Property;
import homequest.util.FinancialEngine;
import homequest.util.PropertyStatus;

public class Bank extends Transaction {
    private static final double RESERVATION_FEE = 25_000;
    private static final double BANK_RATE = 0.07;

    private String name;
    private int termMonths;
    private double monthlyInstallment;
    private Agent receivedBy;

    public Bank(Property targetProperty, Buyer client, int termYears, String bankName, Agent agent) {
        super(targetProperty, client);
        this.name = bankName;
        this.receivedBy = agent;
        this.termMonths = termYears * 12;

        double tcp = targetProperty.getTCP();
        double principal = tcp - RESERVATION_FEE;

        int months = termYears * 12;
        double monthlyRate = BANK_RATE / 12;
        double factor = Math.pow(1 + monthlyRate, months);
        this.monthlyInstallment = principal * (monthlyRate * factor) / (factor - 1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public double getMonthlyInstallment() {
        return monthlyInstallment;
    }

    @Override
    public void finalizeTransaction() {
        // Deduct reservation fee if available; proceed with RESERVED status regardless
        client.deductFunds(RESERVATION_FEE);
        if (receivedBy != null) {
            receivedBy.addCommission(FinancialEngine.calculateAgentCommission(targetProperty.getTCP()));
        }
        targetProperty.setStatus(PropertyStatus.RESERVED);
        client.addTransaction(this);
    }

    @Override
    public String toString() {
        return "Bank Financing [" + super.toString() + ", Bank: " + name +
                ", Monthly: ₱" + String.format("%.2f", monthlyInstallment) + "]";
    }
}
