package com.example.order.Service;

import com.example.order.Event.OrderPlacedEvent;
import com.example.order.Model.Order;
import com.example.order.Model.OrderLineItems;
import com.example.order.Repository.OrderRepository;
import com.example.order.dto.InventoryResponse;
import com.example.order.dto.OrderLineItemsDto;
import com.example.order.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String , OrderPlacedEvent> kafkaTemplate;
    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
                .map(this::mapToDTo)
                .toList();
        order.setOrderLineItemsList(orderLineItems);
        List<String> codes=order.getOrderLineItemsList().stream().map(OrderLineItems::getCode).toList();
        InventoryResponse[] inventoryResponses=webClientBuilder.build().get()
                .uri("http://Inventory/api/inventory4",
                        uriBuilder -> uriBuilder.queryParam("code",codes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        Boolean allProductsInStock=Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
        if (allProductsInStock) {
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return "order place successfully";
        }else {
            throw new IllegalArgumentException("Product is not in stock ");
        }
    }

    private OrderLineItems mapToDTo(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setCode(orderLineItemsDto.getCode());
        return orderLineItems;
    }

}
