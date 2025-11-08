package com.creechy.site.user.controller;

import com.creechy.site.user.request.UserRequest;
import com.creechy.site.user.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody UserRequest request, HttpServletRequest httpRequest) {
        var authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(), request.getPassword()));
        var securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Persist the security context into the session
        httpRequest
                .getSession(true)
                .setAttribute(
                        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        securityContext);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        return ResponseEntity.ok(service.createUser(request));
    }

    @GetMapping("/secret")
    @RolesAllowed({"USER"})
    public ResponseEntity<?> secretEndpoint() {
        return ResponseEntity.ok("WELCOME!!");
    }
}
