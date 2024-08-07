package com.retailAplication.rewardSystem.Controller;

import com.retailAplication.rewardSystem.Dto.BulkTransactionRequest;
import com.retailAplication.rewardSystem.Dto.CustomerRewardResponse;
import com.retailAplication.rewardSystem.Dto.TransactionRequest;
import com.retailAplication.rewardSystem.Service.RewardService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Controller {
  @Autowired private RewardService rewardService;

  @GetMapping("reward/customer/{customerId}")
  public ResponseEntity<CustomerRewardResponse> getCustomerRewards(
      @PathVariable UUID customerId,
      @RequestParam(value = "months", defaultValue = "3") int months) {
    if (months < 1 || months > 12) {
      throw new RuntimeException("Invalid months. Months must be between 1 and 12.");
    }
    CustomerRewardResponse response = rewardService.getCustomerRewards(customerId, months);
    return ResponseEntity.ok(response);
  }

  @PostMapping("transaction/createTransaction")
  public ResponseEntity<String> handleTransaction(
      @RequestBody TransactionRequest transactionRequest) {
    if (transactionRequest == null
        || transactionRequest.getAmount() == null
        || transactionRequest.getCustomerId() == null) {
      throw new RuntimeException("Invalid transaction Request");
    }

    rewardService.handleTransaction(
        transactionRequest.getCustomerId(), transactionRequest.getAmount(), LocalDateTime.now());
    return ResponseEntity.ok("Transaction processed and rewards awarded");
  }

  @PostMapping("transaction/createBulkTransaction")
  public ResponseEntity<BulkTransactionRequest> handleBulkTransaction(
      @RequestBody BulkTransactionRequest bulkTransactionRequest) {
    if (bulkTransactionRequest.getCustomerId() == null
        || bulkTransactionRequest.getTransactions().isEmpty()) {
      throw new RuntimeException("Invalid transaction Request");
    }
    return ResponseEntity.ok(
        rewardService.handleBulkTransactions(
            bulkTransactionRequest.getCustomerId(), bulkTransactionRequest.getTransactions()));
  }
}
