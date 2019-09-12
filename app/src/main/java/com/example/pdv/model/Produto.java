package com.example.pdv.model;

import java.io.Serializable;

public class Produto implements Serializable {
    private String CODIGO;
    private String DESCRICAO;
    private Integer UNIDADE;
    private Integer TIPO;
    private Float PRECO;


    public String getCODIGO() {
        return CODIGO;
    }

    public void setCODIGO(String CODIGO) {
        this.CODIGO = CODIGO;
    }

    public String getDESCRICAO() {
        return DESCRICAO;
    }

    public void setDESCRICAO(String DESCRICAO) {
        this.DESCRICAO = DESCRICAO;
    }

    public Integer getUNIDADE() {
        return UNIDADE;
    }

    public void setUNIDADE(Integer UNIDADE) {
        this.UNIDADE = UNIDADE;
    }

    public Integer getTIPO() {
        return TIPO;
    }

    public void setTIPO(Integer TIPO) {
        this.TIPO = TIPO;
    }

    public Float getPRECO() {
        return PRECO;
    }

    public void setPRECO(Float PRECO) {
        this.PRECO = PRECO;
    }

    @Override
    public String toString() {
        return DESCRICAO;
    }
}
