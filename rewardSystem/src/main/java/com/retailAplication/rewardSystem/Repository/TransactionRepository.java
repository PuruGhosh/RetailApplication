package com.retailAplication.rewardSystem.Repository;


import com.retailAplication.rewardSystem.Entity.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByCustomerIdAndTransactionDateAfter(UUID customerId, LocalDateTime date);
}
