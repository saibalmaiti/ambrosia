package com.ambrosia.main.auth;

import com.ambrosia.main.auth.appuser.AppUser;
import com.ambrosia.main.auth.appuser.AppUserService;
import com.ambrosia.main.auth.jwtauthentication.dto.AuthenticationRequest;
import com.ambrosia.main.auth.jwtauthentication.dto.AuthenticationResponse;
import com.ambrosia.main.auth.jwtauthentication.JwtUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/authenticate")
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AppUserService userDetailsService;
    private final JwtUtil jwtUtil;
    @Autowired
    public AuthenticationController(AuthenticationManager manager, AppUserService userDetailsService, JwtUtil util) {
        this.authenticationManager = manager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = util;
    }

    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        String firebaseToken;
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        try {
            firebaseToken = FirebaseAuth.getInstance().createCustomToken(authenticationRequest.getUsername());
            if(!userDetails.isEnabled()) {
                return ResponseEntity.status(403).body("Please verify the email first");
            }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Incorrect Username or Password");
        }
        catch (FirebaseAuthException e) {
            return ResponseEntity.status(500).body("Failed to create firebase auth token");
        }

        final String jwt = jwtUtil.generateToken(userDetails);
        final AppUser user = (AppUser)userDetails;
        AuthenticationResponse response = new AuthenticationResponse(
                jwt,
                firebaseToken,
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                String.valueOf(user.getAppUserRole())
        );
        return ResponseEntity.ok().body(response);
    }
}
