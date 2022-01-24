package com.ambrosia.main.vendor.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorActiveStatus {

    @Id
    @SequenceGenerator(
            name = "vendor_active_status_sequence",
            sequenceName = "vendor_active_status_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "vendor_active_status_sequence"
    )
    private Long Id;

    @Column(unique = true)
    @Temporal(TemporalType.DATE)
    private Date activeDate;

    private LocalDateTime lastUpdated;

    @Enumerated(EnumType.STRING)
    private ActiveStatusValues activeStatus;

    private Long appUserId;
}
