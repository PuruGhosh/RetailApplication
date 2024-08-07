package com.RetailApplication.RewardSystem.Dto;

import java.util.UUID;
import lombok.Data;

@Data
public class CustomerResponse {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
}
