package com.retailAplication.rewardSystem.Dto;


import java.util.List;
import lombok.Data;

@Data
public class CustomerRewardResponse {
    private CustomerResponse customer;
    private int totalPoints;
    private List<RewardResponse> Rewards;
}
