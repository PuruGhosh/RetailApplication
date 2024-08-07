package com.retailAplication.rewardSystem.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BulkSubTransaction {
    private BigDecimal amount;
    private LocalDate transactionDate;
}
