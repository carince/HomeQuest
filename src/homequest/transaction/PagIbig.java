package homequest.transaction;

import homequest.model.Agent;
import homequest.model.Buyer;
import homequest.model.Property;
import homequest.util.FinancialEngine;
import homequest.util.PropertyStatus;

public class PagIbig extends Transaction {
    private static final double RESERVATION_FEE = 25_000;
    private static final double BANK_RATE = 0.07;

    private String name;
    private int termMonths;
    private double monthlyInstallment;
    private Agent receivedBy;

    public PagIbig(Property targetProperty, Buyer client, int termYears, Agent agent) {
        super(targetProperty, client);
        this.name = "Pag-IBIG Fund";
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

    public int getTermMonths() {
        return termMonths;
    }

    public double getMonthlyInstallment() {
        return monthlyInstallment;
    }

    public boolean payNextDue() {
        if (payments.size() >= termMonths) {
            return false;
        }

        if (!client.deductFunds(monthlyInstallment)) {
            return false;
        }

        int monthNumber = payments.size() + 1;
        addPayment(monthlyInstallment, "Month " + monthNumber);

        if (payments.size() >= termMonths) {
            targetProperty.setStatus(PropertyStatus.SOLD);
        }

        return true;
    }

    @Override
    public boolean finalizeTransaction() {
        if (!client.deductFunds(RESERVATION_FEE)) {
            return false;
        }

        if (receivedBy != null) {
            receivedBy.addCommission(FinancialEngine.calculateAgentCommission(targetProperty.getTCP()));
        }
        // Keep as RESERVED while installments are being paid
        targetProperty.setStatus(PropertyStatus.RESERVED);
        client.addTransaction(this);
        return true;
    }

    @Override
    public String toString() {
        return "Pag-IBIG Financing [" + super.toString() +
                ", Monthly: ₱" + String.format("%.2f", monthlyInstallment) + "]";
    }
}
