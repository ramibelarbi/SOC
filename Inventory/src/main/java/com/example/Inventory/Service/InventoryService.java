package com.example.Inventory.Service;

import com.example.Inventory.Repository.InventoryRepository;
import com.example.Inventory.dto.InventoryResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> code)
    {
        return inventoryRepository.findByCodeIn(code).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .Code(inventory.getCode())
                            .isInStock(inventory.getQuantity() > 0)
                            .build()
                ).toList();

    }
}