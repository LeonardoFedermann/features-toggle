package com.project.featurestoggle.controllers;

import com.project.featurestoggle.domains.User;
import com.project.featurestoggle.dtos.DataAuthentication;
import com.project.featurestoggle.dtos.TokenData;
import com.project.featurestoggle.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager manager;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid DataAuthentication data) {
        var tokenInfo = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var authentication = manager.authenticate(tokenInfo);
        var token = authenticationService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenData(token));
    }
}
