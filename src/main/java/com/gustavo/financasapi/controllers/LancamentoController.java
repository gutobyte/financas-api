package com.gustavo.financasapi.controllers;


import com.gustavo.financasapi.dto.AtualizaStatusDTO;
import com.gustavo.financasapi.dto.LancamentoDTO;
import com.gustavo.financasapi.exceptions.RegraNegocioException;
import com.gustavo.financasapi.model.entity.Lancamento;
import com.gustavo.financasapi.model.entity.Usuario;
import com.gustavo.financasapi.model.entity.enums.StatusLancamento;
import com.gustavo.financasapi.model.entity.enums.TipoLancamento;
import com.gustavo.financasapi.service.LancamentoService;
import com.gustavo.financasapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/financas/lancamentos")
public class LancamentoController {

    private final LancamentoService lancamentoService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO lancamentoDTO){

        try{
            Lancamento entidade =  converter(lancamentoDTO);
            entidade = lancamentoService.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody LancamentoDTO lancamentoDTO){
      return  lancamentoService.obterPorId(id).map( entity -> {

          try{
              Lancamento lancamento = converter(lancamentoDTO);
              lancamento.setId(entity.getId());
              lancamentoService.atualizar(lancamento);

              return ResponseEntity.ok(lancamento);

          }catch (RegraNegocioException e){
              return ResponseEntity.badRequest().body(e.getMessage());
          }

        }).orElseGet( () -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable Long id){
        return lancamentoService.obterPorId(id).map( entity -> {
            lancamentoService.deletar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet( () -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity buscar(
            //@RequestParam java.util.Map<String, String> params
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario){

        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);
        Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
        if(usuario.isEmpty()){
            return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado");
        }else{
            lancamentoFiltro.setUsuario(usuario.get());
        }
        List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);

    }

    private Lancamento converter (LancamentoDTO lancamentoDTO){

        Lancamento lancamento = new Lancamento();

        if(lancamentoDTO.getId() != null) {
            lancamento.setId(lancamentoDTO.getId());
        }
        lancamento.setDescricao(lancamentoDTO.getDescricao());
        lancamento.setAno(lancamentoDTO.getAno());
        lancamento.setMes(lancamentoDTO.getMes());
        lancamento.setValor(lancamentoDTO.getValor());

        Usuario usuarioExiste = usuarioService.obterPorId(lancamentoDTO.getUsuario())
                .orElseThrow( () -> new RegraNegocioException("Usuário não encontrado"));

        lancamento.setUsuario(usuarioExiste);
        if(lancamentoDTO.getTipo() != null) {
            lancamento.setTipo(TipoLancamento.valueOf(lancamentoDTO.getTipo()));
        }
        if(lancamentoDTO.getStatus() != null) {
            lancamento.setStatus(StatusLancamento.valueOf(lancamentoDTO.getStatus()));
        }

        return lancamento;
    }

    @PutMapping("{id}/atualiza-status")
    public ResponseEntity atualizarStatus(@PathVariable Long id, @RequestBody AtualizaStatusDTO atualizaStatusDTO){
            return lancamentoService.obterPorId(id).map( entity -> {
                StatusLancamento statusSelecionado = StatusLancamento.valueOf(atualizaStatusDTO.getStatus());
                if(statusSelecionado == null){
                    return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um status válido");
                }

                try{
                    entity.setStatus(statusSelecionado);
                    lancamentoService.atualizar(entity);
                    return ResponseEntity.ok(entity);

                }catch (RegraNegocioException e){

                    return ResponseEntity.badRequest().body(e.getMessage());
                }

            }).orElseGet( () -> new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
    }


}
