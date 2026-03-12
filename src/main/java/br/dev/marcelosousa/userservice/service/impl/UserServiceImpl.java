package br.dev.marcelosousa.userservice.service.impl;

import br.dev.marcelosousa.userservice.dto.UserDTO;
import br.dev.marcelosousa.userservice.exceptions.EmailUniquenessException;
import br.dev.marcelosousa.userservice.model.User;
import br.dev.marcelosousa.userservice.repository.UserRepository;
import br.dev.marcelosousa.userservice.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private KafkaTemplate<String, String> kafkaTemplate;

    public void enviarMensagem(String mensagem) {
        kafkaTemplate.send("user-topic", mensagem);
    }

    @Override
    public User criarUsuario(UserDTO userDTO) {
        System.out.print(userRepository.existsByEmail(userDTO.getEmail()));
        if(userRepository.existsByEmail(userDTO.getEmail())) {
            log.error("Email already in use {}", userDTO.getEmail());
            throw new EmailUniquenessException("Email already in use");
        } else {
            var user = new User();
            user.setActive(false);
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getName());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userRepository.save(user);
            enviarMensagem("User created: " + user.getEmail());
            log.info("User created {}", user);
            return user;
        }
    }
}