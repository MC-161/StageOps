package com.operations.StageOps.model;

/**
 * Represents a client in the system.
 * This class contains information about the client, including their client ID, name, and contact information.
 */
public class Client {

    private int clientId;
    private String name;
    private String contactInfo;

    /**
     * Default constructor for the Client class.
     */
    public Client() {}

    /**
     * Parameterized constructor for creating a Client instance.
     *
     * @param clientId    The unique ID of the client
     * @param name        The name of the client
     * @param contactInfo The contact information of the client
     */
    public Client(int clientId, String name, String contactInfo) {
        this.clientId = clientId;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    /**
     * Gets the unique client ID.
     *
     * @return The client ID
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets the client ID.
     *
     * @param clientId The client ID to set
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets the name of the client.
     *
     * @return The name of the client
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the client.
     *
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the contact information of the client.
     *
     * @return The contact information of the client
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the contact information of the client.
     *
     * @param contactInfo The contact information to set
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
