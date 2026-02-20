package com.homequest.util;

import java.util.ArrayList;
import java.util.List;

public class PaymentSchedule {
    private List<Double> payments;
    private List<String> dueDates;
    
    public PaymentSchedule() {
        this.payments = new ArrayList<>();
        this.dueDates = new ArrayList<>();
    }
    
    public void addPayment(double amount, String dueDate) {
        this.payments.add(amount);
        this.dueDates.add(dueDate);
    }
    
    public List<Double> getPayments() {
        return payments;
    }
    
    public List<String> getDueDates() {
        return dueDates;
    }
}
