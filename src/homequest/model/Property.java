package homequest.model;

import homequest.util.PropertyStatus;
import homequest.util.ReservationStatus;
import homequest.util.PurchaseRequestStatus;

public abstract class Property {
    protected String blockLot;
    protected String name;
    protected double lotArea;
    protected double basePrice;
    protected PropertyStatus status;
    protected Buyer reservedBy;
    protected ReservationStatus reservationStatus;
    protected Buyer pendingBuyer;
    protected PurchaseRequestStatus purchaseRequestStatus;
    protected int pendingPaymentMethod;
    protected String pendingBankName;
    protected int pendingLoanTerm;

    public Property(String blockLot, double lotArea, double basePrice) {
        this.blockLot = blockLot;
        this.name = blockLot;
        this.lotArea = lotArea;
        this.basePrice = basePrice;
        this.status = PropertyStatus.AVAILABLE;
        this.reservedBy = null;
        this.reservationStatus = null;
        this.pendingBuyer = null;
        this.purchaseRequestStatus = null;
    }

    public Property(String blockLot, String name, double lotArea, double basePrice) {
        this.blockLot = blockLot;
        this.name = name;
        this.lotArea = lotArea;
        this.basePrice = basePrice;
        this.status = PropertyStatus.AVAILABLE;
        this.reservedBy = null;
        this.reservationStatus = null;
        this.pendingBuyer = null;
        this.purchaseRequestStatus = null;
    }

    public String getBlockLot() {
        return blockLot;
    }

    public String getBlock() {
        if (blockLot == null || !blockLot.contains("L")) {
            return blockLot;
        }
        return blockLot.substring(0, blockLot.indexOf("L")).replaceAll("[^0-9]", "");
    }

    public String getLot() {
        if (blockLot == null || !blockLot.contains("L")) {
            return blockLot;
        }
        return blockLot.substring(blockLot.indexOf("L") + 1).replaceAll("[^0-9]", "");
    }

    public String getName() {
        return name;
    }

    public double getLotArea() {
        return lotArea;
    }

    public double getBasePrice() {
        return basePrice;
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

    public Buyer getReservedBy() {
        return reservedBy;
    }

    public void setReservedBy(Buyer reservedBy) {
        this.reservedBy = reservedBy;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public PurchaseRequestStatus getPurchaseRequestStatus() {
        return purchaseRequestStatus;
    }

    public void setPurchaseRequestStatus(PurchaseRequestStatus purchaseRequestStatus) {
        this.purchaseRequestStatus = purchaseRequestStatus;
    }

    public void clearPendingRequest() {
        this.pendingBuyer = null;
        this.purchaseRequestStatus = null;
        this.pendingPaymentMethod = 0;
        this.pendingBankName = null;
        this.pendingLoanTerm = 0;
    }

    public void clearReservation() {
        this.reservedBy = null;
        this.reservationStatus = null;
    }

    public abstract double getTCP();

    @Override
    public String toString() {
        return "Property [Block/Lot: " + blockLot + ", Lot Area: " + lotArea + " sqm, Base Price: ₱" +
                String.format("%.2f", basePrice) + ", Status: " + status + "]";
    }
}
