package com.app_spring.order_service.controller;

import com.app_spring.order_service.dto.OrderDto;
import com.app_spring.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderDto orderDto){
        orderService.placeOrder(orderDto);

        return "Order Placed successfully";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAllOrders(){
        List<OrderDto> orderDto = orderService.getAllOrders();
        return orderDto;
    }
}
