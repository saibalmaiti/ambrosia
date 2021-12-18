package com.ambrosia.main.auth;

import com.ambrosia.main.auth.appuser.AppUser;
import com.ambrosia.main.auth.appuser.AppUserService;
import com.ambrosia.main.auth.jwtauthentication.model.AuthenticationRequest;
import com.ambrosia.main.auth.jwtauthentication.model.AuthenticationResponse;
import com.ambrosia.main.auth.jwtauthentication.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final AuthenticationManager authenticationManager;
    private final AppUserService userDetailsService;
    private final JwtUtil jwtUtil;
    @Autowired
    public HomeController(AuthenticationManager manager, AppUserService userDetailsService, JwtUtil util) {
        this.authenticationManager = manager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = util;
    }

    @GetMapping("/")
    public String home() {
        return ("<h1>Common Home</h1>");
    }
    @GetMapping("/user")
    public String userHome() {
        return ("<h1>User Home</h1>");
    }
    @GetMapping("/admin")
    public String adminHome() {
        return ("<h1>Admin Home</h1>");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Incorrect Username or Password");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        final AppUser user = (AppUser)userDetails;
        return ResponseEntity.ok(new AuthenticationResponse(jwt, String.valueOf(user.getAppUserRole())));
    }
}
