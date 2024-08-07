package com.RetailApplication.RewardSystem.Dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRewardResponse {
    private CustomerResponse customer;
    private int totalPoints;
    private List<RewardResponse> Rewards;
}
