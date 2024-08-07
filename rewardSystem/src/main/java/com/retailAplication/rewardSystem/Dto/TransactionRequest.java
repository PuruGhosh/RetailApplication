package com.retailAplication.rewardSystem.Dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class TransactionRequest {
    private UUID customerId;
    private BigDecimal amount;
}
