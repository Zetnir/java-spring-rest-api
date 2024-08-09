package com.app_spring.order_service.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}
