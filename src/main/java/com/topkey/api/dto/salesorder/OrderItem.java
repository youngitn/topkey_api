package com.topkey.api.dto.salesorder;

import lombok.Data;

@Data
public class OrderItem {
    private String productId;
    private int quantity;

    // Getters and setters
}