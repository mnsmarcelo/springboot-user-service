package br.dev.marcelosousa.userservice.rest.controller;

import br.dev.marcelosousa.userservice.dto.AuthDTO;
import br.dev.marcelosousa.userservice.dto.AuthResponseDTO;
import br.dev.marcelosousa.userservice.service.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("")
    public ResponseEntity<Object> authenticate(
            @Valid @RequestBody AuthDTO authDTO
            ) {
        try {
            AuthResponseDTO authenticatedUser = authenticationService.authenticate(authDTO);
            log.info("Authenticating user {}", authDTO.getEmail());
            return ResponseEntity.ok(authenticatedUser);
        } catch (Exception e) {
            log.error("Error authenticating user", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
