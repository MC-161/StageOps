package com.operations.StageOps.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

// Enhanced Contract Model for Venue Rental

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contract {
    private int contractId;
    private int clientId;
    private int eventId;
    private String terms;
    private String status; // "draft", "to_sign", "signed", "canceled", "expired"
    private ZonedDateTime contractDate;

    // Venue information
    private String venueAddress;
    private String eventDescription;

    // Rental period details
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    // Payment details
    private BigDecimal totalAmount;
    private BigDecimal initialAmount;
    private BigDecimal finalAmount;
    private LocalDate finalPaymentDate;
    private int gracePeriod; // days
    private String lateFee; // amount or percentage

    // Security deposit
    private BigDecimal depositAmount;
    private int depositReturnDays;

    // Cancellation terms
    private int cancellationNoticeDays;
    private String cancellationFee; // amount or percentage

    // Contract parties
    private String ownerName;
    private String renterName;

    // Default constructor
    public Contract() {
    }

    // Minimal constructor with original fields
    public Contract(int contractId, int clientId, int eventId, String terms, String status, ZonedDateTime contractDate) {
        this.contractId = contractId;
        this.clientId = clientId;
        this.eventId = eventId;
        this.terms = terms;
        this.status = status;
        this.contractDate = contractDate;
    }

    // Full constructor
    public Contract(int contractId, int clientId, int eventId, String terms, String status,
                    ZonedDateTime contractDate, String venueAddress, String eventDescription,
                    LocalDateTime startDateTime, LocalDateTime endDateTime, BigDecimal totalAmount,
                    BigDecimal initialAmount, BigDecimal finalAmount, LocalDate finalPaymentDate,
                    int gracePeriod, String lateFee, BigDecimal depositAmount, int depositReturnDays,
                    int cancellationNoticeDays, String cancellationFee, String ownerName, String renterName) {
        this.contractId = contractId;
        this.clientId = clientId;
        this.eventId = eventId;
        this.terms = terms;
        this.status = status;
        this.contractDate = contractDate;
        this.venueAddress = venueAddress;
        this.eventDescription = eventDescription;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.totalAmount = totalAmount;
        this.initialAmount = initialAmount;
        this.finalAmount = finalAmount;
        this.finalPaymentDate = finalPaymentDate;
        this.gracePeriod = gracePeriod;
        this.lateFee = lateFee;
        this.depositAmount = depositAmount;
        this.depositReturnDays = depositReturnDays;
        this.cancellationNoticeDays = cancellationNoticeDays;
        this.cancellationFee = cancellationFee;
        this.ownerName = ownerName;
        this.renterName = renterName;
    }

    // Getters and setters for original fields
    public int getContractId() { return contractId; }
    public void setContractId(int contractId) { this.contractId = contractId; }
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    public String getTerms() { return terms; }
    public void setTerms(String terms) { this.terms = terms; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public ZonedDateTime getContractDate() { return contractDate; }
    public void setContractDate(ZonedDateTime contractDate) { this.contractDate = contractDate; }

    // Getters and setters for new fields
    public String getVenueAddress() { return venueAddress; }
    public void setVenueAddress(String venueAddress) { this.venueAddress = venueAddress; }
    public String getEventDescription() { return eventDescription; }
    public void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }

    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    // Convenience methods for PDF generation
    public String getStartDate() {
        return startDateTime != null ? startDateTime.toLocalDate().toString() : "";
    }
    public String getStartTime() {
        return startDateTime != null ? startDateTime.toLocalTime().toString() : "";
    }
    public String getEndDate() {
        return endDateTime != null ? endDateTime.toLocalDate().toString() : "";
    }
    public String getEndTime() {
        return endDateTime != null ? endDateTime.toLocalTime().toString() : "";
    }
    public String getEventDates() {
        return getStartDate() + (getStartDate().equals(getEndDate()) ? "" : " to " + getEndDate());
    }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getInitialAmount() { return initialAmount; }
    public void setInitialAmount(BigDecimal initialAmount) { this.initialAmount = initialAmount; }
    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }
    public LocalDate getFinalPaymentDate() { return finalPaymentDate; }
    public void setFinalPaymentDate(LocalDate finalPaymentDate) { this.finalPaymentDate = finalPaymentDate; }
    public int getGracePeriod() { return gracePeriod; }
    public void setGracePeriod(int gracePeriod) { this.gracePeriod = gracePeriod; }
    public String getLateFee() { return lateFee; }
    public void setLateFee(String lateFee) { this.lateFee = lateFee; }

    public BigDecimal getDepositAmount() { return depositAmount; }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }
    public int getDepositReturnDays() { return depositReturnDays; }
    public void setDepositReturnDays(int depositReturnDays) { this.depositReturnDays = depositReturnDays; }

    public int getCancellationNoticeDays() { return cancellationNoticeDays; }
    public void setCancellationNoticeDays(int cancellationNoticeDays) { this.cancellationNoticeDays = cancellationNoticeDays; }
    public String getCancellationFee() { return cancellationFee; }
    public void setCancellationFee(String cancellationFee) { this.cancellationFee = cancellationFee; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getRenterName() { return renterName; }
    public void setRenterName(String renterName) { this.renterName = renterName; }

    public String getEffectiveDate() {
        return contractDate != null ? contractDate.toLocalDate().toString() : "";
    }
}