package homequest.util;

public class FinancialEngine {
    public static final double VAT_RATE = 0.12;
    public static final double HOUSE_VAT_THRESHOLD = 3_600_000;
    public static final double RESERVATION_FEE = 25_000;
    public static final double BANK_RATE = 0.07;
    public static final double AGENT_COMMISSION_RATE = 0.10;

    public static double calculateVAT(double price, boolean isLot) {
        if (isLot) {
            return 0;
        }

        if (price >= HOUSE_VAT_THRESHOLD) {
            return price * VAT_RATE;
        }
        return 0;
    }

    public static double computeOtherCharges(double netPrice) {
        return netPrice * 0.04;
    }

    public static double calculateAgentCommission(double amount) {
        return amount * AGENT_COMMISSION_RATE;
    }

    public static double getMonthlyAmortization(double principal, int years) {
        if (years <= 0) {
            return principal;
        }

        int months = years * 12;
        double monthlyRate = BANK_RATE / 12;

        double factor = Math.pow(1 + monthlyRate, months);
        double monthlyPayment = principal * (monthlyRate * factor) / (factor - 1);

        return monthlyPayment;
    }
}
