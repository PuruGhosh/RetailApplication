package com.retailAplication.rewardSystem.Repository;


import com.retailAplication.rewardSystem.Entity.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    /**
     * Finds all the transactions for a customer after given date-time
     * @param customerId
     * @param date
     * @return
     */
    List<Transaction> findByCustomerIdAndTransactionDateAfter(UUID customerId, LocalDateTime date);
}
