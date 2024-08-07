package com.retailAplication.rewardSystem.Service;

import com.retailAplication.rewardSystem.Dto.*;
import com.retailAplication.rewardSystem.Entity.Customer;
import com.retailAplication.rewardSystem.Entity.Transaction;
import com.retailAplication.rewardSystem.Repository.CustomerRepository;
import com.retailAplication.rewardSystem.Repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RewardService {

  @Autowired private TransactionRepository transactionRepository;

  @Autowired private CustomerRepository customerRepository;

  public int calculatePoints(BigDecimal amount) {
    int points = 0;
    int amountInt = amount.intValue(); // Discard the decimal part

    if (amountInt > 100) {
      points += (amountInt - 100) * 2;
      amountInt = 100;
    }
    if (amountInt > 50) {
      points += (amountInt - 50);
    }
    return points;
  }

  public CustomerRewardResponse getCustomerRewards(UUID customerId, int months) {
    Customer customer =
        customerRepository
            .findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    log.info("Finding rewards for customer {} for last {} months", customer, months);
    LocalDateTime monthsAgo = LocalDateTime.now().minusMonths(months);
    List<Transaction> transactions =
        transactionRepository.findByCustomerIdAndTransactionDateAfter(customerId, monthsAgo);

    int totalPoints =
        transactions.stream()
            .mapToInt(transaction -> calculatePoints(transaction.getAmount()))
            .sum();

    CustomerResponse customerResponse = new CustomerResponse();
    customerResponse.setId(customer.getId());
    customerResponse.setName(customer.getName());
    customerResponse.setEmail(customer.getEmail());
    customerResponse.setPhoneNumber(customer.getPhoneNumber());

    List<RewardResponse> rewardResponses =
        transactions.stream()
            .map(
                transaction -> {
                  RewardResponse rewardResponse = new RewardResponse();
                  rewardResponse.setTransactionId(transaction.getId());
                  rewardResponse.setTransactionAmount(transaction.getAmount());
                  rewardResponse.setPoints(calculatePoints(transaction.getAmount()));
                  rewardResponse.setAwardedDate(transaction.getTransactionDate());
                  return rewardResponse;
                })
            .collect(Collectors.toList());

    CustomerRewardResponse response = new CustomerRewardResponse();
    response.setCustomer(customerResponse);
    response.setTotalPoints(totalPoints);
    response.setRewards(rewardResponses);
    log.info("Successfully reward processed. {}", response);
    return response;
  }

  @Transactional
  public void handleTransaction(UUID customerId, BigDecimal amount, LocalDateTime transactionDate) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Transaction amount must be greater than zero");
    }

    Customer customer =
        customerRepository
            .findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    log.info(
        "Transaction is being processed for customer {} of amount {} on date {}",
        customer,
        amount,
        transactionDate);

    Transaction transaction = new Transaction();
    transaction.setId(UUID.randomUUID());
    transaction.setCustomer(customer);
    transaction.setAmount(amount);
    transaction.setTransactionDate(transactionDate);

    // Save the transaction
    transactionRepository.save(transaction);
  }

  @Transactional
  public void handleTransaction(
      Customer customer, BigDecimal amount, LocalDateTime transactionDate) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Transaction amount must be greater than zero");
    }
    log.info(
        "Transaction is being processed for customer {} of amount {} on date {}",
        customer,
        amount,
        transactionDate);

    Transaction transaction = new Transaction();
    transaction.setId(UUID.randomUUID());
    transaction.setCustomer(customer);
    transaction.setAmount(amount);
    transaction.setTransactionDate(transactionDate);

    // Save the transaction
    transactionRepository.save(transaction);
  }

  public BulkTransactionRequest handleBulkTransactions(
      UUID customerId, List<BulkSubTransaction> transactionRequests) {
    Customer customer =
        customerRepository
            .findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    BulkTransactionRequest response = new BulkTransactionRequest();
    response.setCustomerId(customerId);
    List<BulkSubTransaction> responseSubTransactions = new ArrayList<>();
    transactionRequests.forEach(
        transactionRequest -> {
          try {
            handleTransaction(
                customer,
                transactionRequest.getAmount(),
                transactionRequest.getTransactionDate().atTime(LocalTime.of(10, 0)));
            responseSubTransactions.add(transactionRequest);
          } catch (Exception e) {
            log.warn(
                "Transaction failed for customer {} transaction {}. Error: ",
                customerId,
                transactionRequest,
                e);
          }
        });
    response.setTransactions(responseSubTransactions);
    return response;
  }
}
