package com.operations.StageOps.controller;

import com.operations.StageOps.model.Client;
import com.operations.StageOps.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public String createClient(@RequestBody Client client) {
        int result = clientService.saveClient(client);
        return result > 0 ? "Client created successfully!" : "Error creating Client!";
    }

    @GetMapping("/{clientId}")
    public Client getClient(@PathVariable int clientId) {
        return clientService.getClientById(clientId);
    }

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @PutMapping("/{clientId}")
    public Client updateClient(@PathVariable int clientId, @RequestBody Client client) {
        return clientService.updateClient(clientId, client);
    }

    @DeleteMapping("/{clientId}")
    public void deleteClient(@PathVariable int clientId) {
        clientService.deleteClient(clientId);
    }
}
