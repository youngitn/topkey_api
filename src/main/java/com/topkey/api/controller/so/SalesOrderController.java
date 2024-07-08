package com.topkey.api.controller.so;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topkey.api.dto.salesorder.OrderItem;
import com.topkey.api.dto.salesorder.SalesOrder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Sales Orders", description = "接受多筆銷售單資料修改" )
public class SalesOrderController {

	
    @Operation(summary = "多筆銷售單資料修改")
    @PostMapping("/sales-orders")
    public ResponseEntity<String> receiveSalesOrders(
            @RequestBody ArrayList<SalesOrder> salesOrders) {
    
        for (SalesOrder salesOrder : salesOrders) {
      
            System.out.println("Received Sales Order: " + salesOrder.getOrderId());
            System.out.println("Customer Name: " + salesOrder.getCustomerName());
            for (OrderItem item : salesOrder.getItems()) {
                System.out.println("Product ID: " + item.getProductId() + ", Quantity: " + item.getQuantity());
            }
        }


        return new ResponseEntity<>("Received Sales Orders successfully", HttpStatus.OK);
    }
}