package com.operations.StageOps.repository;

import com.operations.StageOps.model.Invoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private InvoiceRepository invoiceRepository;

    private Invoice testInvoice;

    @BeforeEach
    void setUp() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime dueDate = now.plusDays(30);

        testInvoice = new Invoice(
                1,
                1,
                1,
                100.0,
                "pending",
                now,
                dueDate
        );
    }

    @Test
    void createInvoice_ValidInvoice_ReturnsRowsAffected() {
        // Arrange
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Act
        int rowsAffected = invoiceRepository.createInvoice(testInvoice);

        // Assert
        assertEquals(1, rowsAffected);
        verify(jdbcTemplate).update(anyString(), eq(testInvoice.getBookingId()), eq(testInvoice.getClientId()),
                eq(testInvoice.getTotalAmount()), eq(testInvoice.getStatus()), eq(testInvoice.getIssueDate()),
                eq(testInvoice.getDueDate()));
    }

    @Test
    void getInvoiceById_ValidId_ReturnsInvoice() {
        // Arrange
        when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class), any(Object[].class)))
                .thenReturn(testInvoice);

        // Act
        Invoice foundInvoice = invoiceRepository.getInvoiceById(1);

        // Assert
        assertNotNull(foundInvoice);
        assertEquals(testInvoice.getInvoiceId(), foundInvoice.getInvoiceId());
        assertEquals(testInvoice.getTotalAmount(), foundInvoice.getTotalAmount());
    }

    @Test
    void getInvoicesByClientId_ValidClientId_ReturnsInvoices() {
        // Arrange
        List<Invoice> expectedInvoices = List.of(testInvoice);
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class), any(Object[].class)))
                .thenReturn(expectedInvoices);

        // Act
        List<Invoice> invoices = invoiceRepository.getInvoicesByClientId(1);

        // Assert
        assertNotNull(invoices);
        assertEquals(1, invoices.size());
        assertEquals(testInvoice.getClientId(), invoices.get(0).getClientId());
    }

    @Test
    void updateInvoiceStatus_ValidParams_ReturnsRowsAffected() {
        // Arrange
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Act
        int rowsAffected = invoiceRepository.updateInvoiceStatus(1, "paid");

        // Assert
        assertEquals(1, rowsAffected);
        verify(jdbcTemplate).update(anyString(), eq("paid"), eq(1));
    }
} 