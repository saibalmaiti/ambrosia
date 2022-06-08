package com.ambrosia.main.order.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest {
    private String paymentId;
    private String signature;
    private String razorpayOrderId;
}
