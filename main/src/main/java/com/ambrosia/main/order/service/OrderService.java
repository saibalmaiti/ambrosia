package com.ambrosia.main.order.service;

import com.ambrosia.main.order.dto.CreateOrderRequest;
import com.ambrosia.main.order.entity.Order;
import com.ambrosia.main.order.entity.OrderStatus;
import com.ambrosia.main.order.repository.OrderRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    @Autowired
    OrderService(OrderRepository repository) {
        this.orderRepository = repository;
    }

    @Value(value = "${razorpay.key}")
    private String key;
    @Value(value = "${razorpay.secret}")
    private String secret;

    public ResponseEntity<?> createOrder(CreateOrderRequest request) {
        Order order = Order.builder()
                .orderStatus(OrderStatus.PROCESSING)
                .orderItemList(request.getItemList())
                .totalPrice(request.getTotalPrice())
                .userId(request.getUserId())
                .createdAt(request.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        orderRepository.save(order);

        try {
            com.razorpay.Order rzpOrder = this.createRazorpayOrder(order.getTotalPrice() * 100);
            order.setRazorpayOrderId(rzpOrder.get("id"));
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);

            return ResponseEntity.status(HttpStatus.OK).body(order);
        }
        catch (RazorpayException e) {
            order.setOrderStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            System.out.println(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Razorpay order");
        }
        catch (Exception e) {
            order.setOrderStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            System.out.println(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    private com.razorpay.Order createRazorpayOrder(Double amount) throws RazorpayException {

        RazorpayClient client = new RazorpayClient(key, secret);
        UUID receiptId = UUID.randomUUID();
        JSONObject options = new JSONObject();
        options.put("amount", amount);
        options.put("currency", "INR");
        options.put("receipt", receiptId.toString());
        com.razorpay.Order rzpOrder = client.Orders.create(options);
        return rzpOrder;
    }
}
