package br.dev.marcelosousa.userservice.service;

import br.dev.marcelosousa.userservice.dto.UserDTO;
import br.dev.marcelosousa.userservice.model.User;

public interface UserService {

    void enviarMensagem(String mensagem);

    User criarUsuario(UserDTO userDTO);
}
