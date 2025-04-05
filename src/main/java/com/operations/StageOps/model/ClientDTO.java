package com.operations.StageOps.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClientDTO {
    private final IntegerProperty clientId;
    private final StringProperty name;
    private final StringProperty address;
    private final StringProperty email;
    private final StringProperty telephoneNumber;

    public ClientDTO(int clientId, String name, String address, String email, String telephoneNumber) {
        this.clientId = new SimpleIntegerProperty(clientId);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.email = new SimpleStringProperty(email);
        this.telephoneNumber = new SimpleStringProperty(telephoneNumber);
    }

    public IntegerProperty clientIdProperty() { return clientId; }
    public StringProperty nameProperty() { return name; }
    public StringProperty addressProperty() { return address; }
    public StringProperty emailProperty() { return email; }
    public StringProperty telephoneNumberProperty() { return telephoneNumber; }

    // Optionally: getters for easier access
    public int getClientId() { return clientId.get(); }
    public String getName() { return name.get(); }
    public String getAddress() { return address.get(); }
    public String getEmail() { return email.get(); }
    public String getTelephoneNumber() { return telephoneNumber.get(); }
}
