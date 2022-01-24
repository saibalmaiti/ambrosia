package com.ambrosia.main.vendor;

import com.ambrosia.main.vendor.models.ActiveStatusRequest;
import com.ambrosia.main.vendor.service.VendorActiveStatusService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("api/v1/vendor")
@AllArgsConstructor
public class VendorController {

    private VendorActiveStatusService activeStatusService;

    @PostMapping("/set-active-status")
    public ResponseEntity<?> setVendorActiveStatus(@RequestBody ActiveStatusRequest request) {

        try {
            return activeStatusService.setActiveStatus(request);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to set vendor active status");
        }
    }

    @GetMapping("/get-active-status")
    public ResponseEntity<?> getVendorActiveStatus(@RequestParam("date") String date) {
        try {
            Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(date);
            return activeStatusService.getActiveStatus(date1);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to fetch vendor active status");
        }
    }

}
