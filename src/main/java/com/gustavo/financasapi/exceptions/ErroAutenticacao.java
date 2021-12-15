package com.gustavo.financasapi.exceptions;

public class ErroAutenticacao extends RuntimeException{

    public ErroAutenticacao(String msg){
        super(msg);
    }
}
