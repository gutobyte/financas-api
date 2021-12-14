package com.gustavo.financasapi.model.repository;

import com.gustavo.financasapi.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

   boolean existsByEmail(String email);
}
