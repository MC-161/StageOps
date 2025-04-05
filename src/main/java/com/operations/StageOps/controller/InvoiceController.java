package com.operations.StageOps.controller;

import com.operations.StageOps.model.Invoice;
import com.operations.StageOps.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Invoice Controller
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @PostMapping
    public String createInvoice(@RequestBody Invoice invoice) {
        int result = invoiceRepository.createInvoice(invoice);
        return result > 0 ? "Invoice created successfully!" : "Error creating invoice.";
    }

    @GetMapping("/{invoiceId}")
    public Invoice getInvoice(@PathVariable int invoiceId) {
        return invoiceRepository.getInvoiceById(invoiceId);
    }

    @GetMapping("/client/{clientId}")
    public List<Invoice> getInvoicesByClient(@PathVariable int clientId) {
        return invoiceRepository.getInvoicesByClientId(clientId);
    }

    @PutMapping("/{invoiceId}")
    public String updateInvoiceStatus(@PathVariable int invoiceId, @RequestParam String status) {
        int result = invoiceRepository.updateInvoiceStatus(invoiceId, status);
        return result > 0 ? "Invoice status updated successfully!" : "Error updating invoice status.";
    }
}

