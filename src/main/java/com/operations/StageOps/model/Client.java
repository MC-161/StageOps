package com.operations.StageOps.model;

/**
 * Represents a client in the system.
 */
public class Client {
    private int clientId;
    private String name;
    private String contactInfo;

    /**
     * Constructs a new client with the specified details:
     *
     * @param clientId      The unique identifier for the client.
     * @param name          The name of the client.
     * @param contactInfo   The contact information of the client.
     */
    public Client(int clientId, String name, String contactInfo) {
        this.clientId = clientId;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    /**
     * Gets the client ID
     *
     * @return the client ID
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets the client ID
     *
     * @param clientId The client ID to set.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets the client's name.
     *
     * @return The name of the client.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the client's name.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the client's contact information.
     *
     * @return The contact information.
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the client's contact information.
     *
     * @param contactInfo The contact information to set.
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

}
