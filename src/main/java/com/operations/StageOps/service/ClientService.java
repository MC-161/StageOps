package com.operations.StageOps.service;

import com.operations.StageOps.model.Client;
import com.operations.StageOps.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Save a new client
    public int saveClient(Client client) {
        return clientRepository.save(client);
    }

    // Get all clients
    public List<Client> getAllClients() {
        return clientRepository.getAllClients();
    }

    // Get client by ID
    public Client getClientById(int clientId) {
        return clientRepository.getClientById(clientId);
    }

    // Update client
    public Client updateClient(int clientId, Client client) {
        client.setClientId(clientId);  // Make sure the clientId is set in the updated client object
        return clientRepository.update(client);  // This will return the updated client object
    }


    // Delete client
    public int deleteClient(int clientId) {
        return clientRepository.delete(clientId);
    }
}

