package com.operations.StageOps.repository;

import com.operations.StageOps.model.Client;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for managing CRUD operations related to {@link Client} using JDBC.
 * Handles common SQL edge cases such as duplicate entries, missing data, and not-found records.
 */
@Repository
public class ClientRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor to initialize the ClientRepository with a JdbcTemplate.
     *
     * @param jdbcTemplate the JdbcTemplate to use for database access.
     */
    public ClientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new client to the database.
     * Handles duplicate key violations (e.g., email or telephone number already exists).
     *
     * @param client the client to be saved.
     * @return number of rows affected, or -1 if duplicate key error occurs, or 0 on general error.
     */
    public int save(Client client) {
        String sql = "INSERT INTO clients (name, address, email, telephone_number) VALUES (?, ?, ?, ?)";
        try {
            return jdbcTemplate.update(sql, client.getName(), client.getAddress(), client.getEmail(), client.getTelephoneNumber());
        } catch (DuplicateKeyException e) {
            System.err.println("Duplicate entry detected (email or telephone): " + e.getMessage());
            return -1;
        } catch (DataAccessException e) {
            System.err.println("Failed to save client: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return a list of all clients; returns an empty list if a data access error occurs.
     */
    public List<Client> getAllClients() {
        String sql = "SELECT * FROM clients";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> new Client(
                    rs.getInt("client_id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("email"),
                    rs.getString("telephone_number")
            ));
        } catch (DataAccessException e) {
            System.err.println("Error retrieving clients: " + e.getMessage());
            return List.of(); // Return empty list on failure
        }
    }

    /**
     * Retrieves a specific client by their ID.
     *
     * @param clientId the ID of the client to retrieve.
     * @return the client if found; null if no such client exists or a data error occurs.
     */
    public Client getClientById(int clientId) {
        String sql = "SELECT * FROM clients WHERE client_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{clientId}, (rs, rowNum) -> new Client(
                    rs.getInt("client_id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("email"),
                    rs.getString("telephone_number")
            ));
        } catch (EmptyResultDataAccessException e) {
            System.err.println("No client found with ID: " + clientId);
            return null;
        } catch (DataAccessException e) {
            System.err.println("Database error retrieving client: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing client’s information in the database.
     * Handles duplicate key errors and checks for rows affected to confirm the update.
     *
     * @param client the client with updated information.
     * @return the updated client if successful; null if the update failed or a duplicate exists.
     */
    public Client update(Client client) {
        String sql = "UPDATE clients SET name = ?, address = ?, email = ?, telephone_number = ? WHERE client_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    client.getName(), client.getAddress(), client.getEmail(),
                    client.getTelephoneNumber(), client.getClientId());

            if (rowsAffected > 0) {
                return getClientById(client.getClientId());
            } else {
                System.err.println("No rows affected during update for client ID: " + client.getClientId());
                return null;
            }
        } catch (DuplicateKeyException e) {
            System.err.println("Duplicate entry on update: " + e.getMessage());
            return null;
        } catch (DataAccessException e) {
            System.err.println("Error updating client: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes a client by their ID.
     *
     * @param clientId the ID of the client to delete.
     * @return the number of rows affected; 0 if no such client exists or deletion fails.
     */
    public int delete(int clientId) {
        String sql = "DELETE FROM clients WHERE client_id = ?";
        try {
            return jdbcTemplate.update(sql, clientId);
        } catch (DataAccessException e) {
            System.err.println("Error deleting client with ID " + clientId + ": " + e.getMessage());
            return 0;
        }
    }
}
