package com.ambrosia.main.order;

import com.ambrosia.main.order.dto.CreateOrderRequest;
import com.ambrosia.main.order.dto.CreatePaymentRequest;
import com.ambrosia.main.order.dto.UpdateOrderStatusRequest;
import com.ambrosia.main.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @PostMapping("/update-order-status")
    public ResponseEntity<?> updateOrderStatus(@RequestBody UpdateOrderStatusRequest request) {
        return orderService.updateOrderStatus(request);
    }

    @PostMapping("/add-payment-details")
    public ResponseEntity<?> addPaymentStatus(@RequestBody CreatePaymentRequest request) {
        return orderService.addPaymentDetails(request);
    }

    @GetMapping("/get-order-by-userid")
    public ResponseEntity<?> getOrderByUserId(@RequestParam("userid") Long userId) {
        return orderService.getOrderByUserId(userId);
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<?> getAllOrder() {
        return orderService.getAllOrders();
    }
}
