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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/authenticate")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserService userDetailsService;
    private final JwtUtil jwtUtil;
    @Autowired
    public AuthController(AuthenticationManager manager, AppUserService userDetailsService, JwtUtil util) {
        this.authenticationManager = manager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = util;
    }

    @PostMapping
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
