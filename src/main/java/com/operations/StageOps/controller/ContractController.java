package com.operations.StageOps.controller;

import com.operations.StageOps.model.Contract;
import com.operations.StageOps.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Contract Controller
@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    @Autowired
    private ContractRepository contractRepository;

    @PostMapping
    public String createContract(@RequestBody Contract contract) {
        int result = contractRepository.createContract(contract);
        return result > 0 ? "Contract created successfully!" : "Error creating contract.";
    }

    @GetMapping("/{contractId}")
    public Contract getContract(@PathVariable int contractId) {
        return contractRepository.getContractById(contractId);
    }

    @GetMapping("/client/{clientId}")
    public List<Contract> getContractsByClient(@PathVariable int clientId) {
        return contractRepository.getContractsByClientId(clientId);
    }

    @PutMapping("/{contractId}")
    public String updateContractStatus(@PathVariable int contractId, @RequestParam String status) {
        int result = contractRepository.updateContractStatus(contractId, status);
        return result > 0 ? "Contract status updated successfully!" : "Error updating contract status.";
    }
}