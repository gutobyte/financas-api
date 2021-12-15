package com.gustavo.financasapi.model.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "usuario")
@Data
@Builder
@AllArgsConstructor
public class Usuario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", length = 150)
    private String nome;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "senha", length = 20)
    private String senha;

    public Usuario(){};


}
