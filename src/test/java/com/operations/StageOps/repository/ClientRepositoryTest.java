package com.operations.StageOps.repository;

import com.operations.StageOps.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ClientRepository clientRepository;

    private Client testClient;

    @BeforeEach
    void setUp() {
        testClient = new Client(
                1,
                "Test Client",
                "123 Test St",
                "test@example.com",
                "1234567890"
        );
    }

    @Test
    void save_ValidClient_ReturnsOne() {
        // Arrange
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Act
        int result = clientRepository.save(testClient);

        // Assert
        assertEquals(1, result);
        verify(jdbcTemplate).update(anyString(), 
            eq(testClient.getName()),
            eq(testClient.getAddress()),
            eq(testClient.getEmail()),
            eq(testClient.getTelephoneNumber())
        );
    }

    @Test
    void save_DuplicateClient_ReturnsMinusOne() {
        // Arrange
        when(jdbcTemplate.update(anyString(), any(Object[].class)))
            .thenThrow(new DuplicateKeyException("Duplicate entry"));

        // Act
        int result = clientRepository.save(testClient);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    void getAllClients_ReturnsListOfClients() {
        // Arrange
        List<Client> expectedClients = List.of(testClient);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
            .thenReturn(expectedClients);

        // Act
        List<Client> clients = clientRepository.getAllClients();

        // Assert
        assertNotNull(clients);
        assertEquals(1, clients.size());
        assertEquals(testClient.getClientId(), clients.get(0).getClientId());
        assertEquals(testClient.getName(), clients.get(0).getName());
    }

    @Test
    void getClientById_ExistingId_ReturnsClient() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class)))
            .thenReturn(testClient);

        // Act
        Client result = clientRepository.getClientById(1);

        // Assert
        assertNotNull(result);
        assertEquals(testClient.getClientId(), result.getClientId());
        assertEquals(testClient.getName(), result.getName());
    }

    @Test
    void getClientById_NonExistingId_ReturnsNull() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class)))
            .thenThrow(new EmptyResultDataAccessException(1));

        // Act
        Client result = clientRepository.getClientById(999);

        // Assert
        assertNull(result);
    }

    @Test
    void update_ExistingClient_ReturnsUpdatedClient() {
        // Arrange
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), any(RowMapper.class)))
            .thenReturn(testClient);

        // Act
        Client result = clientRepository.update(testClient);

        // Assert
        assertNotNull(result);
        assertEquals(testClient.getClientId(), result.getClientId());
        assertEquals(testClient.getName(), result.getName());
    }

    @Test
    void update_NonExistingClient_ReturnsNull() {
        // Arrange
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(0);

        // Act
        Client result = clientRepository.update(testClient);

        // Assert
        assertNull(result);
    }

    @Test
    void update_DuplicateEmail_ReturnsNull() {
        // Arrange
        when(jdbcTemplate.update(anyString(), any(Object[].class)))
            .thenThrow(new DuplicateKeyException("Duplicate email"));

        // Act
        Client result = clientRepository.update(testClient);

        // Assert
        assertNull(result);
    }

    @Test
    void delete_ExistingClient_ReturnsOne() {
        // Arrange
        when(jdbcTemplate.update(anyString(), eq(1))).thenReturn(1);

        // Act
        int result = clientRepository.delete(1);

        // Assert
        assertEquals(1, result);
        verify(jdbcTemplate).update(anyString(), eq(1));
    }

    @Test
    void delete_NonExistingClient_ReturnsZero() {
        // Arrange
        when(jdbcTemplate.update(anyString(), eq(999))).thenReturn(0);

        // Act
        int result = clientRepository.delete(999);

        // Assert
        assertEquals(0, result);
    }
} 