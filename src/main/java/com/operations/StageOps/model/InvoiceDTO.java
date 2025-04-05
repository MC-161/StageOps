package com.operations.StageOps.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.*;

import java.time.ZonedDateTime;

public class InvoiceDTO {
    private final IntegerProperty invoiceId;
    private final IntegerProperty bookingId;
    private final IntegerProperty clientId;
    @JsonProperty("amount")
    private final SimpleDoubleProperty totalAmount;
    private final StringProperty status;
    private final ObjectProperty<ZonedDateTime> issueDate;
    private final ObjectProperty<ZonedDateTime> dueDate;

    public InvoiceDTO(int invoiceId, int bookingId, int clientId, double totalAmount, String status,
                      ZonedDateTime issueDate, ZonedDateTime dueDate) {
        this.invoiceId = new SimpleIntegerProperty(invoiceId);
        this.bookingId = new SimpleIntegerProperty(bookingId);
        this.clientId = new SimpleIntegerProperty(clientId);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.status = new SimpleStringProperty(status);
        this.issueDate = new SimpleObjectProperty<>(issueDate);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
    }

    public int getInvoiceId() {
        return invoiceId.get();
    }

    public IntegerProperty invoiceIdProperty() {
        return invoiceId;
    }

    public int getBookingId() {
        return bookingId.get();
    }

    public IntegerProperty bookingIdProperty() {
        return bookingId;
    }

    public int getClientId() {
        return clientId.get();
    }

    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    public double getTotalAmount() {
        return (totalAmount.get());
    }

    public SimpleDoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public ZonedDateTime getIssueDate() {
        return issueDate.get();
    }

    public ObjectProperty<ZonedDateTime> issueDateProperty() {
        return issueDate;
    }

    public ZonedDateTime getDueDate() {
        return dueDate.get();
    }

    public ObjectProperty<ZonedDateTime> dueDateProperty() {
        return dueDate;
    }
}
