package br.dev.marcelosousa.userservice.rest.controller;

import br.dev.marcelosousa.userservice.dto.UserDTO;
import br.dev.marcelosousa.userservice.exceptions.EmailUniquenessException;
import br.dev.marcelosousa.userservice.model.User;
import br.dev.marcelosousa.userservice.repository.UserRepository;
import br.dev.marcelosousa.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/sign-up")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDTO userDTO){
        User user = userService.criarUsuario(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
