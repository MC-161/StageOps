package com.operations.StageOps.repository;

import com.operations.StageOps.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

// Invoice Repository
@Repository
public class InvoiceRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int createInvoice(Invoice invoice) {
        String sql = "INSERT INTO invoices (booking_id, client_id, amount, status, issue_date, due_date) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, invoice.getBookingId(), invoice.getClientId(), invoice.getTotalAmount(),
            invoice.getStatus(), invoice.getIssueDate(), invoice.getDueDate());
    }

    public Invoice getInvoiceById(int invoiceId) {
        String sql = "SELECT invoice_id, booking_id, client_id, amount AS totalAmount, status, issue_date, due_date FROM invoices WHERE invoice_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Invoice.class), invoiceId);
    }

    public List<Invoice> getInvoicesByClientId(int clientId) {
        String sql = "SELECT invoice_id, booking_id, client_id, amount AS totalAmount, status, issue_date, due_date FROM invoices WHERE client_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Invoice.class), clientId);
    }


    public int updateInvoiceStatus(int invoiceId, String status) {
        String sql = "UPDATE invoices SET status = ? WHERE invoice_id = ?";
        return jdbcTemplate.update(sql, status, invoiceId);
    }
}
