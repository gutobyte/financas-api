package com.gustavo.financasapi.controllers;


import com.gustavo.financasapi.dto.AutenticarUsuarioDTO;
import com.gustavo.financasapi.dto.UsuarioDTO;
import com.gustavo.financasapi.exceptions.ErroAutenticacao;
import com.gustavo.financasapi.exceptions.RegraNegocioException;
import com.gustavo.financasapi.model.entity.Usuario;
import com.gustavo.financasapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/financas/usuarios")
public class UsuarioController {


    private final UsuarioService usuarioService;


/*    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody UsuarioDTO usuarioDTO){

        Usuario usuario = Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .build();

        usuarioService.validarEmail(usuario.getEmail());

        return usuarioService.salvarUsuario(usuario);
    }*/

    @PostMapping
    public ResponseEntity salvar(@RequestBody UsuarioDTO usuarioDTO){

        Usuario usuario = Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .build();
        try{
            Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody AutenticarUsuarioDTO autenticarUsuarioDTO){

        try {
           Usuario usuarioAutenticado = usuarioService.autenticar(autenticarUsuarioDTO.getEmail(), autenticarUsuarioDTO.getSenha());

           return ResponseEntity.ok(usuarioAutenticado);
        } catch (ErroAutenticacao e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
