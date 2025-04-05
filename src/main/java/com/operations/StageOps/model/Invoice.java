package com.operations.StageOps.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

// Invoice Model
public class Invoice {
    private int invoiceId;
    private int bookingId;
    private int clientId;
    @JsonProperty("amount")
    private double totalAmount;
    private String status; // "paid", "pending"
    private ZonedDateTime issueDate;
    private ZonedDateTime dueDate;

    // Default constructor
    public Invoice() {
    }
    // Constructor
    public Invoice(int invoiceId, int bookingId, int clientId, double totalAmount, String status, ZonedDateTime issueDate, ZonedDateTime dueDate) {
        this.invoiceId = invoiceId;
        this.bookingId = bookingId;
        this.clientId = clientId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    // Getters and setters
    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public ZonedDateTime getIssueDate() { return issueDate; }
    public void setIssueDate(ZonedDateTime issueDate) { this.issueDate = issueDate; }
    public ZonedDateTime getDueDate() { return dueDate; }
    public void setDueDate(ZonedDateTime dueDate) { this.dueDate = dueDate; }
}


