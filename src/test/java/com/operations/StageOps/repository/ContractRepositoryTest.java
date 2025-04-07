package com.operations.StageOps.repository;

import com.operations.StageOps.model.Contract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ContractRepository contractRepository;

    private Contract testContract;

    @BeforeEach
    void setUp() {
        testContract = new Contract();
        testContract.setContractId(1);
        testContract.setClientId(100);
        testContract.setEventId(200);
        testContract.setTerms("Test Terms");
        testContract.setStatus("DRAFT");
        testContract.setContractDate(ZonedDateTime.now());
        testContract.setVenueAddress("123 Test Venue");
        testContract.setEventDescription("Test Event");
        testContract.setStartDateTime(LocalDateTime.now());
        testContract.setEndDateTime(LocalDateTime.now().plusHours(2));
        testContract.setTotalAmount(new BigDecimal("1000.00"));
        testContract.setInitialAmount(new BigDecimal("200.00"));
        testContract.setFinalAmount(new BigDecimal("800.00"));
        testContract.setFinalPaymentDate(LocalDate.now().plusDays(30));
        testContract.setGracePeriod(7);
        testContract.setLateFee("50.00");
        testContract.setDepositAmount(new BigDecimal("100.00"));
        testContract.setDepositReturnDays(14);
        testContract.setCancellationNoticeDays(30);
        testContract.setCancellationFee("200.00");
        testContract.setOwnerName("Test Owner");
        testContract.setRenterName("Test Renter");
    }

    @Test
    void createContract_ValidContract_ReturnsOne() {
        // Arrange
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Act
        int result = contractRepository.createContract(testContract);

        // Assert
        assertEquals(1, result);
        verify(jdbcTemplate).update(anyString(), 
            eq(testContract.getClientId()),
            eq(testContract.getEventId()),
            eq(testContract.getTerms()),
            eq(testContract.getStatus()),
            any(), // contract date
            eq(testContract.getVenueAddress()),
            eq(testContract.getEventDescription()),
            any(), // start date time
            any(), // end date time
            eq(testContract.getTotalAmount()),
            eq(testContract.getInitialAmount()),
            eq(testContract.getFinalAmount()),
            any(), // final payment date
            eq(testContract.getGracePeriod()),
            eq(testContract.getLateFee()),
            eq(testContract.getDepositAmount()),
            eq(testContract.getDepositReturnDays()),
            eq(testContract.getCancellationNoticeDays()),
            eq(testContract.getCancellationFee()),
            eq(testContract.getOwnerName()),
            eq(testContract.getRenterName())
        );
    }

    @Test
    void getContractById_ExistingId_ReturnsContract() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(1)))
            .thenReturn(testContract);

        // Act
        Contract result = contractRepository.getContractById(1);

        // Assert
        assertNotNull(result);
        assertEquals(testContract.getContractId(), result.getContractId());
        assertEquals(testContract.getClientId(), result.getClientId());
        assertEquals(testContract.getEventId(), result.getEventId());
    }

    @Test
    void getContractById_NonExistingId_ReturnsNull() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(999)))
            .thenThrow(new EmptyResultDataAccessException(1));

        // Act
        Contract result = contractRepository.getContractById(999);

        // Assert
        assertNull(result);
    }

    @Test
    void getContractsByClientId_ExistingClientId_ReturnsContracts() {
        // Arrange
        List<Contract> expectedContracts = List.of(testContract);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(100)))
            .thenReturn(expectedContracts);

        // Act
        List<Contract> results = contractRepository.getContractsByClientId(100);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testContract.getContractId(), results.get(0).getContractId());
        assertEquals(testContract.getClientId(), results.get(0).getClientId());
    }

    @Test
    void updateContractStatus_ValidStatus_ReturnsOne() {
        // Arrange
        when(jdbcTemplate.update(anyString(), eq("SIGNED"), eq(1))).thenReturn(1);

        // Act
        int result = contractRepository.updateContractStatus(1, "SIGNED");

        // Assert
        assertEquals(1, result);
        verify(jdbcTemplate).update(anyString(), eq("SIGNED"), eq(1));
    }

    @Test
    void updateContract_ValidContract_ReturnsOne() {
        // Arrange
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Act
        int result = contractRepository.updateContract(testContract);

        // Assert
        assertEquals(1, result);
        verify(jdbcTemplate).update(anyString(), 
            eq(testContract.getClientId()),
            eq(testContract.getEventId()),
            eq(testContract.getTerms()),
            eq(testContract.getStatus()),
            any(), // contract date
            eq(testContract.getVenueAddress()),
            eq(testContract.getEventDescription()),
            any(), // start date time
            any(), // end date time
            eq(testContract.getTotalAmount()),
            eq(testContract.getInitialAmount()),
            eq(testContract.getFinalAmount()),
            any(), // final payment date
            eq(testContract.getGracePeriod()),
            eq(testContract.getLateFee()),
            eq(testContract.getDepositAmount()),
            eq(testContract.getDepositReturnDays()),
            eq(testContract.getCancellationNoticeDays()),
            eq(testContract.getCancellationFee()),
            eq(testContract.getOwnerName()),
            eq(testContract.getRenterName()),
            eq(testContract.getContractId())
        );
    }
} 