package com.retailAplication.rewardSystem.Repository;


import com.retailAplication.rewardSystem.Entity.Customer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

}
