package homequest.transaction;

import homequest.model.Buyer;
import homequest.model.Property;
import homequest.util.PropertyStatus;

public class PagIbig extends Transaction {
    private static final double RESERVATION_FEE = 25_000;
    private static final double BANK_RATE = 0.07;

    private String name;
    private int termMonths;
    private double monthlyInstallment;

    public PagIbig(Property targetProperty, Buyer client, int termYears) {
        super(targetProperty, client);
        this.name = "Pag-IBIG Fund";
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

    @Override
    public void finalizeTransaction() {
        System.out.println("\n=== PAG-IBIG FINANCING ===");
        System.out.println("Financing through: " + name);
        System.out.println("Interest Rate: " + (BANK_RATE * 100) + "%");

        if (client.deductFunds(RESERVATION_FEE)) {
            targetProperty.setStatus(PropertyStatus.SOLD);
            client.addTransaction(this);

            System.out.println("✓ Installment transaction initiated successfully!");
            System.out.println("  Transaction ID: " + transactionID);
            System.out.println("  Reservation Fee Paid: ₱" + String.format("%.2f", RESERVATION_FEE));
            System.out.println("  Monthly Installment: ₱" + String.format("%.2f", monthlyInstallment));
            System.out.println("  Term: " + (termMonths / 12) + " years (" + termMonths + " months)");
            System.out.println("  Remaining Balance: ₱" + String.format("%.2f", client.getWalletBalance()));
        } else {
            System.out.println("✗ Insufficient funds for reservation fee!");
            System.out.println("  Required: ₱" + String.format("%.2f", RESERVATION_FEE));
            System.out.println("  Available: ₱" + String.format("%.2f", client.getWalletBalance()));
        }
    }

    @Override
    public String toString() {
        return "Pag-IBIG Financing [" + super.toString() +
                ", Monthly: ₱" + String.format("%.2f", monthlyInstallment) + "]";
    }
}
