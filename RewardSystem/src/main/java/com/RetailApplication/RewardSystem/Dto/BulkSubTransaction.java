package com.RetailApplication.RewardSystem.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BulkSubTransaction {
    private BigDecimal amount;
    private LocalDate transactionDate;
}
