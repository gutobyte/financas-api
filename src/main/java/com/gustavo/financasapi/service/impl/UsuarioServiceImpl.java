package com.gustavo.financasapi.service.impl;

import com.gustavo.financasapi.exceptions.ErroAutenticacao;
import com.gustavo.financasapi.exceptions.RegraNegocioException;
import com.gustavo.financasapi.model.entity.Usuario;
import com.gustavo.financasapi.model.repository.UsuarioRepository;
import com.gustavo.financasapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;


    @Override
    public Usuario autenticar(String email, String senha) {
       Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
       if(!usuario.isPresent()){
            throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
       }
       if(!usuario.get().getSenha().equals(senha)){
           throw new ErroAutenticacao("Senha inválida.");
       }

        return usuario.get();
    }

    @Override
    @Transactional //criar na base de dados uma transação, executará metodo salvar usuário e logo depois commitar
    public Usuario salvarUsuario(Usuario usuario) {

        validarEmail(usuario.getEmail());
        return usuarioRepository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {

       boolean existe = usuarioRepository.existsByEmail(email);
       if(existe){
           throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
       }

    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}
