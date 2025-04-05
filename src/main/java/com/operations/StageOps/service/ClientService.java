package com.operations.StageOps.service;

import com.operations.StageOps.model.Client;
import com.operations.StageOps.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service class for managing clients.
 * It provides methods to save, retrieve, update, and delete clients.
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Constructor to initialize ClientService with a ClientRepository.
     *
     * @param clientRepository The repository to interact with the client data.
     */
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Saves a new client in the repository.
     *
     * @param client The client object to be saved.
     * @return The ID of the saved client.
     */
    public int saveClient(Client client) {
        return clientRepository.save(client);
    }

    /**
     * Retrieves all clients from the repository.
     *
     * @return A list of all clients.
     */
    public List<Client> getAllClients() {
        return clientRepository.getAllClients();
    }

    /**
     * Retrieves a client by their ID.
     *
     * @param clientId The ID of the client to retrieve.
     * @return The client object associated with the provided ID.
     */
    public Client getClientById(int clientId) {
        return clientRepository.getClientById(clientId);
    }

    /**
     * Updates the details of an existing client.
     *
     * @param clientId The ID of the client to update.
     * @param client   The updated client object.
     * @return The updated client object.
     */
    public Client updateClient(int clientId, Client client) {
        client.setClientId(clientId);  // Make sure the clientId is set in the updated client object
        return clientRepository.update(client);  // This will return the updated client object
    }


    /**
     * Deletes a client based on the provided client ID.
     *
     * @param clientId The ID of the client to delete.
     * @return The number of rows affected by the deletion.
     */
    public int deleteClient(int clientId) {
        return clientRepository.delete(clientId);
    }
}

