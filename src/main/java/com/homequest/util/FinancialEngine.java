package com.homequest.util;

public class FinancialEngine {
    public static final double VAT_RATE = 0.12;
    public static final double HOUSE_VAT_THRESHOLD = 3_600_000;
    public static final double RESERVATION_FEE = 25_000;
    public static final double BANK_RATE = 0.07;
    
    /**
     * Calculate VAT for a property
     * @param price The base price of the property
     * @param isLot True if it's a lot only (no house)
     * @return The VAT amount
     */
    public static double calculateVAT(double price, boolean isLot) {
        if (isLot) {
            // Lots don't have VAT
            return 0;
        }
        
        // Houses have VAT only if above threshold
        if (price >= HOUSE_VAT_THRESHOLD) {
            return price * VAT_RATE;
        }
        return 0;
    }
    
    /**
     * Compute other charges (transfer fees, documentation fees, etc.)
     * Typically 3-5% of net price
     * @param netPrice The net selling price
     * @return Other charges amount
     */
    public static double computeOtherCharges(double netPrice) {
        return netPrice * 0.04; // 4% for other charges
    }
    
    /**
     * Calculate monthly amortization using simple formula
     * @param principal The loan amount
     * @param years The loan term in years
     * @return Monthly amortization amount
     */
    public static double getMonthlyAmortization(double principal, int years) {
        if (years <= 0) {
            return principal;
        }
        
        int months = years * 12;
        double monthlyRate = BANK_RATE / 12;
        
        // Using standard amortization formula: P * [r(1+r)^n] / [(1+r)^n - 1]
        double factor = Math.pow(1 + monthlyRate, months);
        double monthlyPayment = principal * (monthlyRate * factor) / (factor - 1);
        
        return monthlyPayment;
    }
}
