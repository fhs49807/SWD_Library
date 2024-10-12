package at.ac.fhsalzburg.swd.spring.model;

public class Invoice {
    private double amount;
    private String invoiceID;
    private boolean paymentStatus;

    public Invoice() { }

    public Invoice(double amount, String invoiceID, boolean paymentStatus) {
        this.amount = amount;
        this.invoiceID = invoiceID;
        this.paymentStatus = paymentStatus;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
