package com.gustavo.financasapi.service.impl;

import com.gustavo.financasapi.exceptions.RegraNegocioException;
import com.gustavo.financasapi.model.entity.Lancamento;
import com.gustavo.financasapi.model.entity.enums.StatusLancamento;
import com.gustavo.financasapi.model.entity.enums.TipoLancamento;
import com.gustavo.financasapi.model.repository.LancamentoRepository;
import com.gustavo.financasapi.service.LancamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LancamentoServiceImpl implements LancamentoService {

    private final LancamentoRepository lancamentoRepository;

    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        validar(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return lancamentoRepository.save(lancamento);
    }

    @Override
    @Transactional
    public Lancamento atualizar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        validar(lancamento);
        return lancamentoRepository.save(lancamento);
    }

    @Override
    @Transactional
    public void deletar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        lancamentoRepository.delete(lancamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {

        Example example =
                Example.of(lancamentoFiltro,
                        ExampleMatcher.matching()
                                .withIgnoreCase()
                                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return lancamentoRepository.findAll(example);
    }

    @Override
    public void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento) {
        lancamento.setStatus(statusLancamento);
        atualizar(lancamento);
    }

    @Override
    public void validar(Lancamento lancamento) {
        if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")){
            throw new RegraNegocioException("Informe uma DESCRIÇÃO válida");
        }
        if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12){
            throw new RegraNegocioException("Informe um MÊS válido.");
        }
        if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4 ){
            throw new RegraNegocioException("Informe um ANO válido.");
        }
        if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null){
            throw new RegraNegocioException("Informe um USUÁRIO.");
        }
        if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
            throw new RegraNegocioException("Informe um Valor válido.");
        }
        if(lancamento.getTipo() == null){
            throw new RegraNegocioException("Informe um tipo de LANÇAMENTO");
        }
    }

    @Override
    public Optional<Lancamento> obterPorId(Long id) {
        return lancamentoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal obterSaldoPorUsuario(Long id) {
       BigDecimal receitas = lancamentoRepository.obterSaldoPorTipoLancamentoEUSUARIO(id, TipoLancamento.RECEITA);
       BigDecimal despesas = lancamentoRepository.obterSaldoPorTipoLancamentoEUSUARIO(id, TipoLancamento.DESPESA);

       if(receitas == null){
           receitas = BigDecimal.ZERO;
       }
       if(despesas == null){
           receitas = BigDecimal.ZERO;
       }
       return receitas.subtract(despesas);
    }


}
