package com.ambrosia.main.order.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequest {
    private String razorpayOrderId;
    private String status;
}
