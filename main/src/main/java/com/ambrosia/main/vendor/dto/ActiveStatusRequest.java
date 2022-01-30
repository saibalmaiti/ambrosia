package com.ambrosia.main.vendor.dto;

import com.ambrosia.main.vendor.entity.ActiveStatusValues;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ActiveStatusRequest {
    private Long appUserId;
    private Date date;
    private ActiveStatusValues status;  // OPEN / CLOSE
}
