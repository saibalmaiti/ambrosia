package com.ambrosia.main.auth.resetPassword;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/reset_password")
@AllArgsConstructor
public class ResetPasswordController {


    private ResetPasswordService resetPasswordService;

    @PostMapping("/request")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email) {
        return resetPasswordService.sendForgotPasswordOTP(email);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePassword(@RequestParam("email") String email,
                                            @RequestParam("password") String newPassword) {
       return resetPasswordService.updatePassword(email, newPassword);

    }

}
