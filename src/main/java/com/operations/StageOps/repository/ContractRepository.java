package com.operations.StageOps.repository;

import com.operations.StageOps.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;

/**
 * Repository class responsible for handling database operations related to contracts.
 */
@Repository
public class ContractRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Creates a new contract in the database.
     *
     * @param contract The contract object containing all the details to be inserted.
     * @return The number of rows affected by the insert operation.
     */
    public int createContract(Contract contract) {
        String sql = "INSERT INTO contracts (client_id, event_id, terms, status, contract_date, " +
                "venue_address, event_description, start_date_time, end_date_time, " +
                "total_amount, initial_amount, final_amount, final_payment_date, " +
                "grace_period, late_fee, deposit_amount, deposit_return_days, " +
                "cancellation_notice_days, cancellation_fee, owner_name, renter_name) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql,
                contract.getClientId(),
                contract.getEventId(),
                contract.getTerms(),
                contract.getStatus(),
                contract.getContractDate(),
                contract.getVenueAddress(),
                contract.getEventDescription(),
                contract.getStartDateTime(),
                contract.getEndDateTime(),
                contract.getTotalAmount(),
                contract.getInitialAmount(),
                contract.getFinalAmount(),
                contract.getFinalPaymentDate(),
                contract.getGracePeriod(),
                contract.getLateFee(),
                contract.getDepositAmount(),
                contract.getDepositReturnDays(),
                contract.getCancellationNoticeDays(),
                contract.getCancellationFee(),
                contract.getOwnerName(),
                contract.getRenterName()
        );
    }

    /**
     * Retrieves a contract by its ID.
     *
     * @param contractId The ID of the contract to retrieve.
     * @return The contract with the specified ID, or null if no contract is found.
     */
    public Contract getContractById(int contractId) {
        String sql = "SELECT * FROM contracts WHERE contract_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new ContractRowMapper(), contractId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Retrieves a list of contracts associated with a specific client.
     *
     * @param clientId The client ID for which to retrieve the contracts.
     * @return A list of contracts associated with the specified client.
     */
    public List<Contract> getContractsByClientId(int clientId) {
        String sql = "SELECT * FROM contracts WHERE client_id = ?";
        return jdbcTemplate.query(sql, new ContractRowMapper(), clientId);
    }

    /**
     * Updates the status of a contract.
     *
     * @param contractId The ID of the contract to update.
     * @param status The new status to set for the contract.
     * @return The number of rows affected by the update operation.
     */
    public int updateContractStatus(int contractId, String status) {
        String sql = "UPDATE contracts SET status = ? WHERE contract_id = ?";
        return jdbcTemplate.update(sql, status, contractId);
    }

    /**
     * Updates all the fields of an existing contract.
     *
     * @param contract The contract object containing the updated details.
     * @return The number of rows affected by the update operation.
     */
    public int updateContract(Contract contract) {
        String sql = "UPDATE contracts SET " +
                "client_id = ?, event_id = ?, terms = ?, status = ?, contract_date = ?, " +
                "venue_address = ?, event_description = ?, start_date_time = ?, end_date_time = ?, " +
                "total_amount = ?, initial_amount = ?, final_amount = ?, final_payment_date = ?, " +
                "grace_period = ?, late_fee = ?, deposit_amount = ?, deposit_return_days = ?, " +
                "cancellation_notice_days = ?, cancellation_fee = ?, owner_name = ?, renter_name = ? " +
                "WHERE contract_id = ?";

        return jdbcTemplate.update(sql,
                contract.getClientId(),
                contract.getEventId(),
                contract.getTerms(),
                contract.getStatus(),
                contract.getContractDate(),
                contract.getVenueAddress(),
                contract.getEventDescription(),
                contract.getStartDateTime(),
                contract.getEndDateTime(),
                contract.getTotalAmount(),
                contract.getInitialAmount(),
                contract.getFinalAmount(),
                contract.getFinalPaymentDate(),
                contract.getGracePeriod(),
                contract.getLateFee(),
                contract.getDepositAmount(),
                contract.getDepositReturnDays(),
                contract.getCancellationNoticeDays(),
                contract.getCancellationFee(),
                contract.getOwnerName(),
                contract.getRenterName(),
                contract.getContractId()
        );
    }

    // Custom RowMapper to handle the new fields
    private static class ContractRowMapper implements RowMapper<Contract> {
        @Override
        public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
            Contract contract = new Contract();
            contract.setContractId(rs.getInt("contract_id"));
            contract.setClientId(rs.getInt("client_id"));
            contract.setEventId(rs.getInt("event_id"));
            contract.setTerms(rs.getString("terms"));
            contract.setStatus(rs.getString("status"));

            // Handle timestamp/datetime conversion safely
            Timestamp contractDateTimestamp = rs.getTimestamp("contract_date");
            if (contractDateTimestamp != null) {
                contract.setContractDate(contractDateTimestamp.toInstant().atZone(ZoneId.systemDefault()));
            }

            // New fields
            contract.setVenueAddress(rs.getString("venue_address"));
            contract.setEventDescription(rs.getString("event_description"));

            // Handle datetime fields
            Timestamp startDateTimeTimestamp = rs.getTimestamp("start_date_time");
            if (startDateTimeTimestamp != null) {
                contract.setStartDateTime(startDateTimeTimestamp.toLocalDateTime());
            }

            Timestamp endDateTimeTimestamp = rs.getTimestamp("end_date_time");
            if (endDateTimeTimestamp != null) {
                contract.setEndDateTime(endDateTimeTimestamp.toLocalDateTime());
            }

            // Handle numeric fields
            contract.setTotalAmount(rs.getBigDecimal("total_amount"));
            contract.setInitialAmount(rs.getBigDecimal("initial_amount"));
            contract.setFinalAmount(rs.getBigDecimal("final_amount"));

            // Handle date field
            Date finalPaymentDate = rs.getDate("final_payment_date");
            if (finalPaymentDate != null) {
                contract.setFinalPaymentDate(finalPaymentDate.toLocalDate());
            }

            contract.setGracePeriod(rs.getInt("grace_period"));
            contract.setLateFee(rs.getString("late_fee"));
            contract.setDepositAmount(rs.getBigDecimal("deposit_amount"));
            contract.setDepositReturnDays(rs.getInt("deposit_return_days"));
            contract.setCancellationNoticeDays(rs.getInt("cancellation_notice_days"));
            contract.setCancellationFee(rs.getString("cancellation_fee"));
            contract.setOwnerName(rs.getString("owner_name"));
            contract.setRenterName(rs.getString("renter_name"));

            return contract;
        }
    }
}