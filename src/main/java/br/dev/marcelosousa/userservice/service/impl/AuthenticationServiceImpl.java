package br.dev.marcelosousa.userservice.service.impl;

import br.dev.marcelosousa.userservice.config.JwtTokenProvider;
import br.dev.marcelosousa.userservice.dto.AuthDTO;
import br.dev.marcelosousa.userservice.dto.AuthResponseDTO;
import br.dev.marcelosousa.userservice.model.User;
import br.dev.marcelosousa.userservice.repository.UserRepository;
import br.dev.marcelosousa.userservice.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthResponseDTO authenticate(AuthDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow();
        String token = jwtTokenProvider.generateToken(user);

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setName(user.getName());

        return response;
    }

}
