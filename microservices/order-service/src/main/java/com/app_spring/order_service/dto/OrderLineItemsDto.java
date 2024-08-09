package com.app_spring.order_service.dto;


import jakarta.annotation.Nullable;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDto {
    @Nullable
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
