package com.ambrosia.main.order.dto;

import com.ambrosia.main.order.entity.OrderItem;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;

@Data
public class CreateOrderRequest {
    private Long userId;
    private Double totalPrice;
    private Timestamp createdAt;
    private ArrayList<OrderItem> itemList;
}
