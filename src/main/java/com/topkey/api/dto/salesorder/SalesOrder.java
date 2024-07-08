package com.topkey.api.dto.salesorder;

import java.util.List;

import lombok.Data;

@Data
public class SalesOrder {
    private String orderId;
    private String customerName;
    private List<OrderItem> items;

    // Getters and setters
}
