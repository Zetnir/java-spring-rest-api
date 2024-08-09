package com.app_spring.order_service.service;

import com.app_spring.order_service.dto.InventoryDto;
import com.app_spring.order_service.dto.OrderDto;
import com.app_spring.order_service.dto.OrderLineItemsDto;
import com.app_spring.order_service.model.Order;
import com.app_spring.order_service.model.OrderLineItems;
import com.app_spring.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
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
    private final WebClient webClient;

    public void placeOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList =  orderDto.getOrderLineItemsDtoList()
                .stream()
                .map(orderLineItemsDto -> mapToOrderLineItems(orderLineItemsDto))
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodeList = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        InventoryDto[] inventoryDtoArray = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodeList).build()
                )
                .retrieve()
                .bodyToMono(InventoryDto[].class)
                .block();

        boolean allProductInStock = Arrays.stream(inventoryDtoArray)
                .allMatch(InventoryDto::isInStock);

        if(allProductInStock && inventoryDtoArray.length == skuCodeList.size()){
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product not in stock, try again later");
        }

    }

    public List<OrderDto> getAllOrders(){
        List<OrderDto> orderDtoList = orderRepository.findAll()
                .stream()
                .map(order -> mapToOrderDto(order))
                .toList();

        return orderDtoList;
    }

    private OrderDto mapToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        List<OrderLineItemsDto> orderLineItemsDtoList = order.getOrderLineItemsList()
                        .stream()
                        .map(orderLineItems -> mapToOrderLineItemsDto(orderLineItems))
                        .toList();
        orderDto.setOrderLineItemsDtoList(orderLineItemsDtoList);
        return orderDto;
    }

    private OrderLineItemsDto mapToOrderLineItemsDto(OrderLineItems orderLineItems){
        OrderLineItemsDto orderLineItemsDto = new OrderLineItemsDto();
        orderLineItemsDto.setId(orderLineItems.getId());
        orderLineItemsDto.setQuantity(orderLineItems.getQuantity());
        orderLineItemsDto.setPrice(orderLineItems.getPrice());
        orderLineItemsDto.setSkuCode(orderLineItems.getSkuCode());
        return orderLineItemsDto;
    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
