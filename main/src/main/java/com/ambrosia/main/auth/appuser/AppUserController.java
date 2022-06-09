package com.ambrosia.main.auth.appuser;

import com.ambrosia.main.auth.appuser.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/app-user")
public class AppUserController {
    private AppUserService appUserService;

    @Autowired
    AppUserController(AppUserService service) {
        this.appUserService = service;
    }

    @GetMapping("/get-user-by-id")
    ResponseEntity<?> getUserById(@RequestParam("userid") Long userId) {
        return appUserService.getUserById(userId);
    }
}
