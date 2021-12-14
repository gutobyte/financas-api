package com.gustavo.financasapi.service.impl;

import com.gustavo.financasapi.exceptions.RegraNegocioException;
import com.gustavo.financasapi.model.entity.Usuario;
import com.gustavo.financasapi.model.repository.UsuarioRepository;
import com.gustavo.financasapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;


    @Override
    public Usuario autenticar(String email, String senha) {
        return null;
    }

    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public void validarEmail(String email) {

       boolean existe = usuarioRepository.existsByEmail(email);
       if(existe){
           throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
       }

    }
}
