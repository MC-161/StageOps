package com.operations.StageOps.repository;

import com.operations.StageOps.model.Client;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClientRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor to initialize the ClientRepository with JdbcTemplate.
     *
     * @param jdbcTemplate JdbcTemplate for interacting with the database.
     */
    public ClientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new client to the database.
     *
     * @param client The client object containing the details of the client to be saved.
     * @return The number of rows affected by the insert operation.
     */
    public int save(Client client) {
        String sql = "INSERT INTO clients (name, contact_info) VALUES (?, ?)";
        return jdbcTemplate.update(sql, client.getName(), client.getContactInfo());
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return A list of all clients.
     */
    public List<Client> getAllClients() {
        String sql = "SELECT * FROM clients";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Client(
                rs.getInt("client_id"),
                rs.getString("name"),
                rs.getString("contact_info")
        ));
    }

    /**
     * Retrieves a client by their ID.
     *
     * @param clientId The ID of the client to retrieve.
     * @return The client with the specified ID.
     */
    public Client getClientById(int clientId) {
        String sql = "SELECT * FROM clients WHERE client_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{clientId}, (rs, rowNum) -> new Client(
                rs.getInt("client_id"),
                rs.getString("name"),
                rs.getString("contact_info")
        ));
    }

    /**
     * Updates an existing client in the database.
     *
     * @param client The updated client object.
     * @return The updated client if the update is successful, or null if no rows were affected.
     */
    public Client update(Client client) {
        String sql = "UPDATE clients SET name = ?, contact_info = ? WHERE client_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, client.getName(), client.getContactInfo(), client.getClientId());

        // If the update is successful, retrieve the updated client from the database
        if (rowsAffected > 0) {
            String selectSql = "SELECT * FROM clients WHERE client_id = ?";
            return jdbcTemplate.queryForObject(selectSql, new Object[]{client.getClientId()},
                    (rs, rowNum) -> new Client(
                            rs.getInt("client_id"),
                            rs.getString("name"),
                            rs.getString("contact_info")
                    ));
        }
        return null; // If no rows were affected, return null
    }

    /**
     * Deletes a client by their ID.
     *
     * @param clientId The ID of the client to delete.
     * @return The number of rows affected by the delete operation.
     */
    public int delete(int clientId) {
        String sql = "DELETE FROM clients WHERE client_id = ?";
        return jdbcTemplate.update(sql, clientId);
    }
}
