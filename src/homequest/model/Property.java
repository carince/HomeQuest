package homequest.model;

import homequest.util.PropertyStatus;
import homequest.transaction.Transaction;

public abstract class Property {
    protected String blockLot;
    protected String name;
    protected double lotArea;
    protected double basePrice;
    protected PropertyStatus status;
    protected Buyer pendingBuyer;
    protected Transaction pendingTransaction;
    protected int pendingPaymentMethod;
    protected String pendingBankName;
    protected int pendingLoanTerm;

    public Property(String blockLot, double lotArea, double basePrice) {
        this.blockLot = blockLot;
        this.name = blockLot;
        this.lotArea = lotArea;
        this.basePrice = basePrice;
        this.status = PropertyStatus.AVAILABLE;
        this.pendingBuyer = null;
        this.pendingTransaction = null;
    }

    public Property(String blockLot, String name, double lotArea, double basePrice) {
        this.blockLot = blockLot;
        this.name = name;
        this.lotArea = lotArea;
        this.basePrice = basePrice;
        this.status = PropertyStatus.AVAILABLE;
        this.pendingBuyer = null;
        this.pendingTransaction = null;
    }

    public String getBlockLot() {
        return blockLot;
    }

    public void setBlockLot(String blockLot) {
        this.blockLot = blockLot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLotArea() {
        return lotArea;
    }

    public void setLotArea(double lotArea) {
        this.lotArea = lotArea;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public Buyer getPendingBuyer() {
        return pendingBuyer;
    }

    public void setPendingBuyer(Buyer pendingBuyer) {
        this.pendingBuyer = pendingBuyer;
    }

    public Transaction getPendingTransaction() {
        return pendingTransaction;
    }

    public void setPendingTransaction(Transaction pendingTransaction) {
        this.pendingTransaction = pendingTransaction;
    }

    public int getPendingPaymentMethod() {
        return pendingPaymentMethod;
    }

    public void setPendingPaymentMethod(int pendingPaymentMethod) {
        this.pendingPaymentMethod = pendingPaymentMethod;
    }

    public String getPendingBankName() {
        return pendingBankName;
    }

    public void setPendingBankName(String pendingBankName) {
        this.pendingBankName = pendingBankName;
    }

    public int getPendingLoanTerm() {
        return pendingLoanTerm;
    }

    public void setPendingLoanTerm(int pendingLoanTerm) {
        this.pendingLoanTerm = pendingLoanTerm;
    }

    public void clearPendingRequest() {
        this.pendingBuyer = null;
        this.pendingTransaction = null;
        this.pendingPaymentMethod = 0;
        this.pendingBankName = null;
        this.pendingLoanTerm = 0;
    }

    public abstract double getTCP();

    @Override
    public String toString() {
        return "Property [Block/Lot: " + blockLot + ", Lot Area: " + lotArea + " sqm, Base Price: ₱" +
                String.format("%.2f", basePrice) + ", Status: " + status + "]";
    }
}
