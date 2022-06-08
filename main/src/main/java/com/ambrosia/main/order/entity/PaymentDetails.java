package com.ambrosia.main.order.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"razorpayOrderId", "paymentId"}) })
public class PaymentDetails {

    @Id
    @SequenceGenerator(
            name = "paymentDetailsSequence",
            sequenceName = "paymentDetailsSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "paymentDetailsSequence"
    )
    private Long id;

    @Column(unique = true)
    private String paymentId;
    private String signature;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "razorpayOrderId",
            referencedColumnName = "razorpayOrderId",
            unique = true
    )
    private Order order;


}
