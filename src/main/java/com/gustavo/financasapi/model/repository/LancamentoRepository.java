package com.gustavo.financasapi.model.repository;

import com.gustavo.financasapi.model.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
