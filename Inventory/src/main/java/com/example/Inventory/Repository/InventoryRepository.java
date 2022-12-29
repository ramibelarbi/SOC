package com.example.Inventory.Repository;

import com.example.Inventory.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;
@EnableJpaRepositories

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByCodeIn(List<String> code);
}
