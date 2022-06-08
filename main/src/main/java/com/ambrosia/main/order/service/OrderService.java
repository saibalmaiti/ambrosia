package com.ambrosia.main.order.service;

import com.ambrosia.main.order.dto.CreateOrderRequest;
import com.ambrosia.main.order.dto.CreatePaymentRequest;
import com.ambrosia.main.order.dto.UpdateOrderStatusRequest;
import com.ambrosia.main.order.entity.Order;
import com.ambrosia.main.order.entity.OrderStatus;
import com.ambrosia.main.order.entity.PaymentDetails;
import com.ambrosia.main.order.repository.OrderRepository;
import com.ambrosia.main.order.repository.PaymentRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    private OrderRepository orderRepository;
    private PaymentRepository paymentRepository;

    @Autowired
    OrderService(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
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
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Razorpay Order");
        }
        catch (Exception e) {
            order.setOrderStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            log.error(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Order");
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

    public ResponseEntity<?> updateOrderStatus(UpdateOrderStatusRequest request) {
        Optional<Order> orderOptional = orderRepository.findByRazorpayOrderId(request.getRazorpayOrderId());
        if(!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid order id");
        }
        Order order = orderOptional.get();
        switch (request.getStatus()) {
            case "SUCCESSFUL" : order.setOrderStatus(OrderStatus.SUCCESSFUL); break;
            case "FAILED": order.setOrderStatus(OrderStatus.FAILED); break;
            case "PROCESSING": order.setOrderStatus(OrderStatus.PROCESSING); break;
            case "DELIVERED": order.setOrderStatus(OrderStatus.DELIVERED); break;
        }
        orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    public ResponseEntity<?> addPaymentDetails(CreatePaymentRequest request) {
        Optional<Order> orderOptional = orderRepository.findByRazorpayOrderId(request.getRazorpayOrderId());
        if(!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid order id");
        }
        Order order = orderOptional.get();
        order.setOrderStatus(OrderStatus.SUCCESSFUL);
        orderRepository.save(order);

        PaymentDetails paymentDetails = PaymentDetails.builder()
                .order(order)
                .paymentId(request.getPaymentId())
                .signature(request.getSignature())
                .build();

        paymentRepository.save(paymentDetails);

        return ResponseEntity.status(HttpStatus.OK).body(paymentDetails);
    }

    public ResponseEntity<?> getOrderByUserId(Long userId) {
        Optional<List<Order>> orderList = orderRepository.findAllByUserIdOrderByCreatedAt(userId);
        if(orderList.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(orderList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No order found");
    }

    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderRepository.findAll());
    }
}
