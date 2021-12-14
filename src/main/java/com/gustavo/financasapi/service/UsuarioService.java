package com.gustavo.financasapi.service;


import com.gustavo.financasapi.model.entity.Usuario;


public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validarEmail(String email);
}
