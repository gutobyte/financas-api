package com.gustavo.financasapi.model.repository;

import com.gustavo.financasapi.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("teste")
public class UsuarioRepositoryTest {


    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    public void deveVerificarAExistenciaDeUmEmail(){
        //cenário
        Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
        usuarioRepository.save(usuario);
        //ação/execução
        boolean resultado =  usuarioRepository.existsByEmail("usuario@email.com");

        //verificação
        Assertions.assertThat(resultado).isTrue();
    }


    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComEmail(){
        //cenario
        usuarioRepository.deleteAll();

        //acao/execução
        boolean resultado = usuarioRepository.existsByEmail("usuario@email.com");

        //verificação
        Assertions.assertThat(resultado).isFalse();
    }

}
