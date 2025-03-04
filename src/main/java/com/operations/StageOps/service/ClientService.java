package com.operations.StageOps.service;

import com.operations.StageOps.model.Client;
import com.operations.StageOps.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Client entities.
 * It provides methods for CRUD operations (Create, Read, Update, Delete) on client records.
 * The class interacts with the `ClientRepository` for database operations.
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Constructor for initializing the ClientRepository.
     *
     * @param clientRepository the ClientRepository used for interacting with the database.
     */
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Saves a new client record into the database.
     *
     * @param client the Client object containing the data to be saved.
     * @return the number of rows affected by the insert query (usually 1 if successful).
     */
    public int saveClient(Client client) {
        return clientRepository.save(client);
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return a list of Client objects representing all clients in the database.
     */
    public List<Client> getAllClients() {
        return clientRepository.getAllClients();
    }

    /**
     * Retrieves a specific client by its ID from the database.
     *
     * @param clientId the ID of the client to retrieve.
     * @return the Client object corresponding to the given client ID.
     */
    public Client getClientById(int clientId) {
        return clientRepository.getClientById(clientId);
    }

    /**
     * Updates an existing client record in the database.
     *
     * @param clientId the ID of the client to update.
     * @param client   the Client object containing the updated data.
     * @return the updated Client object after the database update.
     */
    public Client updateClient(int clientId, Client client) {
        client.setClientId(clientId);  // Ensure the clientId is set in the updated client object
        return clientRepository.update(client);  // Returns the updated client object
    }

    /**
     * Deletes a client record from the database based on the client ID.
     *
     * @param clientId the ID of the client to be deleted.
     * @return the number of rows affected by the delete query (usually 1 if successful).
     */
    public int deleteClient(int clientId) {
        return clientRepository.delete(clientId);
    }
}
