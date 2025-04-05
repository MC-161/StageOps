package com.operations.StageOps.model;

public class Client {
    private int clientId;
    private String name;
    private String address;
    private String email;
    private String telephoneNumber;

    /**
     * Represents a client who makes bookings.
     */
    public Client() {
    }

    /**
     * Constructs a Client with the specified ID, name, address, email, and telephone number.
     *
     * @param clientId        The unique identifier for the client.
     * @param name            The name of the client.
     * @param address         The address of the client.
     * @param email           The email address of the client.
     * @param telephoneNumber The telephone number of the client.
     */
    public Client(int clientId, String name, String address, String email, String telephoneNumber) {
        this.clientId = clientId;
        this.name = name;
        this.address = address;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * Gets the client ID.
     *
     * @return The unique identifier of the client.
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets the client ID.
     *
     * @param clientId The unique identifier of the client.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets the name of the client.
     *
     * @return The name of the client.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the client.
     *
     * @param name The name of the client.
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
