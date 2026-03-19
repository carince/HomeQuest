package homequest.transaction;

import homequest.model.Agent;
import homequest.model.Buyer;
import homequest.model.Property;
import homequest.util.FinancialEngine;
import homequest.util.PropertyStatus;

public class PagIbig extends Transaction {
    public static final double RESERVATION_FEE = 25_000;
    private static final double BANK_RATE = 0.07;
    private static final double MIN_DOWNPAYMENT_PERCENT = 5.0;
    private static final double MAX_DOWNPAYMENT_PERCENT = 15.0;

    private String name;
    private int termMonths;
    private double monthlyInstallment;
    private double downPaymentPercent;
    private double downPaymentAmount;
    private Agent receivedBy;

    public PagIbig(Property targetProperty, Buyer client, int termYears, double downPaymentPercent, Agent agent) {
        super(targetProperty, client);

        if (Double.isNaN(downPaymentPercent) || Double.isInfinite(downPaymentPercent)
                || downPaymentPercent < MIN_DOWNPAYMENT_PERCENT
                || downPaymentPercent > MAX_DOWNPAYMENT_PERCENT) {
            throw new IllegalArgumentException("Downpayment must be between 5% and 15% for Pag-IBIG financing.");
        }

        this.name = "Pag-IBIG Fund";
        this.receivedBy = agent;
        this.termMonths = termYears * 12;
        this.downPaymentPercent = downPaymentPercent;

        double tcp = targetProperty.getTCP();
        this.downPaymentAmount = tcp * (downPaymentPercent / 100.0);
        double principal = tcp - RESERVATION_FEE - downPaymentAmount;

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

    public double getDownPaymentPercent() {
        return downPaymentPercent;
    }

    public double getDownPaymentAmount() {
        return downPaymentAmount;
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
        double upfrontDue = RESERVATION_FEE + downPaymentAmount;
        if (client.getWalletBalance() < upfrontDue) {
            return false;
        }

        if (!client.deductFunds(upfrontDue)) {
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
                ", Downpayment: " + String.format("%.2f", downPaymentPercent) + "% (₱" + String.format("%.2f", downPaymentAmount) + ")" +
                ", Monthly: ₱" + String.format("%.2f", monthlyInstallment) + "]";
    }
}
