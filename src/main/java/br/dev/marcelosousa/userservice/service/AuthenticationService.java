package br.dev.marcelosousa.userservice.service;

import br.dev.marcelosousa.userservice.dto.AuthDTO;
import br.dev.marcelosousa.userservice.dto.AuthResponseDTO;

public interface AuthenticationService {

    AuthResponseDTO authenticate(AuthDTO input);
}
