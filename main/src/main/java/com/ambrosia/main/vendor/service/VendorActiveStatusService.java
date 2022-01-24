package com.ambrosia.main.vendor.service;

import com.ambrosia.main.vendor.entity.VendorActiveStatus;
import com.ambrosia.main.vendor.models.ActiveStatusRequest;
import com.ambrosia.main.vendor.repository.VendorActiveStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VendorActiveStatusService {
    private VendorActiveStatusRepository activeStatusRepository;

    public ResponseEntity<?> setActiveStatus(ActiveStatusRequest request) throws Exception {
        Optional<VendorActiveStatus> fetchedStatus = activeStatusRepository.findByActiveDate(request.getDate());
        if(fetchedStatus.isEmpty()) {
            VendorActiveStatus vendorActiveStatus = VendorActiveStatus.builder()
                    .lastUpdated(LocalDateTime.now())
                    .activeDate(request.getDate())
                    .appUserId(request.getAppUserId())
                    .activeStatus(request.getStatus())
                    .build();
            activeStatusRepository.save(vendorActiveStatus);
        }
        else {
            activeStatusRepository.updateStatus(
                    request.getDate(), LocalDateTime.now(), request.getStatus(), request.getAppUserId()
            );
        }

        return ResponseEntity.ok().body("Vendor Status Successfully Updated");
    }

    public ResponseEntity<?> getActiveStatus(Date date) throws Exception{
        Optional<VendorActiveStatus> fetchedStatus = activeStatusRepository.findByActiveDate(date);
        if(fetchedStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Shop is closed");
        }

        return ResponseEntity.status(HttpStatus.OK).body(fetchedStatus.get().getActiveStatus());
    }
}
