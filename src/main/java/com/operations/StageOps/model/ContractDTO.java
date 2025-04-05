package com.operations.StageOps.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractDTO {

    private final IntegerProperty contractId;
    private final IntegerProperty clientId;
    private final IntegerProperty eventId;
    private final StringProperty terms;
    private final StringProperty status;
    private final ObjectProperty<ZonedDateTime> contractDate;
    private final StringProperty venueAddress;
    private final StringProperty eventDescription;
    private final ObjectProperty<LocalDateTime> startDateTime;
    private final ObjectProperty<LocalDateTime> endDateTime;
    private final ObjectProperty<BigDecimal> totalAmount;
    private final ObjectProperty<BigDecimal> initialAmount;
    private final ObjectProperty<BigDecimal> finalAmount;
    private final ObjectProperty<LocalDate> finalPaymentDate;
    private final IntegerProperty gracePeriod;
    private final StringProperty lateFee;
    private final ObjectProperty<BigDecimal> depositAmount;
    private final IntegerProperty depositReturnDays;
    private final IntegerProperty cancellationNoticeDays;
    private final StringProperty cancellationFee;
    private final StringProperty ownerName;
    private final StringProperty renterName;

    // Constructor
    public ContractDTO(int contractId, int clientId, int eventId, String terms, String status,
                       ZonedDateTime contractDate, String venueAddress, String eventDescription,
                       LocalDateTime startDateTime, LocalDateTime endDateTime, BigDecimal totalAmount,
                       BigDecimal initialAmount, BigDecimal finalAmount, LocalDate finalPaymentDate,
                       int gracePeriod, String lateFee, BigDecimal depositAmount, int depositReturnDays,
                       int cancellationNoticeDays, String cancellationFee, String ownerName, String renterName) {
        this.contractId = new SimpleIntegerProperty(contractId);
        this.clientId = new SimpleIntegerProperty(clientId);
        this.eventId = new SimpleIntegerProperty(eventId);
        this.terms = new SimpleStringProperty(terms);
        this.status = new SimpleStringProperty(status);
        this.contractDate = new SimpleObjectProperty<>(contractDate);
        this.venueAddress = new SimpleStringProperty(venueAddress);
        this.eventDescription = new SimpleStringProperty(eventDescription);
        this.startDateTime = new SimpleObjectProperty<>(startDateTime);
        this.endDateTime = new SimpleObjectProperty<>(endDateTime);
        this.totalAmount = new SimpleObjectProperty<>(totalAmount);
        this.initialAmount = new SimpleObjectProperty<>(initialAmount);
        this.finalAmount = new SimpleObjectProperty<>(finalAmount);
        this.finalPaymentDate = new SimpleObjectProperty<>(finalPaymentDate);
        this.gracePeriod = new SimpleIntegerProperty(gracePeriod);
        this.lateFee = new SimpleStringProperty(lateFee);
        this.depositAmount = new SimpleObjectProperty<>(depositAmount);
        this.depositReturnDays = new SimpleIntegerProperty(depositReturnDays);
        this.cancellationNoticeDays = new SimpleIntegerProperty(cancellationNoticeDays);
        this.cancellationFee = new SimpleStringProperty(cancellationFee);
        this.ownerName = new SimpleStringProperty(ownerName);
        this.renterName = new SimpleStringProperty(renterName);
    }

    // Getters and Property methods
    public int getContractId() {
        return contractId.get();
    }

    public IntegerProperty contractIdProperty() {
        return contractId;
    }

    public int getClientId() {
        return clientId.get();
    }

    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    public int getEventId() {
        return eventId.get();
    }

    public IntegerProperty eventIdProperty() {
        return eventId;
    }

    public String getTerms() {
        return terms.get();
    }

    public StringProperty termsProperty() {
        return terms;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public ZonedDateTime getContractDate() {
        return contractDate.get();
    }

    public ObjectProperty<ZonedDateTime> contractDateProperty() {
        return contractDate;
    }

    public String getVenueAddress() {
        return venueAddress.get();
    }

    public StringProperty venueAddressProperty() {
        return venueAddress;
    }

    public String getEventDescription() {
        return eventDescription.get();
    }

    public StringProperty eventDescriptionProperty() {
        return eventDescription;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime.get();
    }

    public ObjectProperty<LocalDateTime> startDateTimeProperty() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime.get();
    }

    public ObjectProperty<LocalDateTime> endDateTimeProperty() {
        return endDateTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount.get();
    }

    public ObjectProperty<BigDecimal> totalAmountProperty() {
        return totalAmount;
    }

    public BigDecimal getInitialAmount() {
        return initialAmount.get();
    }

    public ObjectProperty<BigDecimal> initialAmountProperty() {
        return initialAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount.get();
    }

    public ObjectProperty<BigDecimal> finalAmountProperty() {
        return finalAmount;
    }

    public LocalDate getFinalPaymentDate() {
        return finalPaymentDate.get();
    }

    public ObjectProperty<LocalDate> finalPaymentDateProperty() {
        return finalPaymentDate;
    }

    public int getGracePeriod() {
        return gracePeriod.get();
    }

    public IntegerProperty gracePeriodProperty() {
        return gracePeriod;
    }

    public String getLateFee() {
        return lateFee.get();
    }

    public StringProperty lateFeeProperty() {
        return lateFee;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount.get();
    }

    public ObjectProperty<BigDecimal> depositAmountProperty() {
        return depositAmount;
    }

    public int getDepositReturnDays() {
        return depositReturnDays.get();
    }

    public IntegerProperty depositReturnDaysProperty() {
        return depositReturnDays;
    }

    public int getCancellationNoticeDays() {
        return cancellationNoticeDays.get();
    }

    public IntegerProperty cancellationNoticeDaysProperty() {
        return cancellationNoticeDays;
    }

    public String getCancellationFee() {
        return cancellationFee.get();
    }

    public StringProperty cancellationFeeProperty() {
        return cancellationFee;
    }

    public String getOwnerName() {
        return ownerName.get();
    }

    public StringProperty ownerNameProperty() {
        return ownerName;
    }

    public String getRenterName() {
        return renterName.get();
    }

    public StringProperty renterNameProperty() {
        return renterName;
    }
}
