package com.example.order.Repository;

import com.example.order.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface OrderRepository extends JpaRepository<Order, Long> {
}
