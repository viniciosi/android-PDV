package com.example.pdv.model;

import java.util.ArrayList;
import java.util.List;

public class TiposPagamentos {

    public class TipoPagamento{
        private Integer id;
        private String descricao;

        public TipoPagamento(Integer id, String descricao){
            this.id = id;
            this.descricao = descricao;
        }

        public Integer getId() {
            return id;
        }

        public String getDescricao() {
            return descricao;
        }

        @Override
        public String toString(){
            return descricao;
        }

        @Override
        public boolean equals(Object obj){
            if (obj instanceof TipoPagamento) {
                TipoPagamento t = (TipoPagamento) obj;
                if(t.getDescricao().equals(descricao) && t.getId()==id) return true;
            }
            return  false;
        }
    }

    private List<TipoPagamento> tipos;

    public TiposPagamentos(){
        tipos = new ArrayList<>();

        tipos.add(new TipoPagamento(1, "Dinheiro"));
        tipos.add(new TipoPagamento(2, "Cartão"));
    }

    public List<TipoPagamento> getTipos(){
        return this.tipos;
    }

    public static TipoPagamento getTipo(Integer id){
        TipoPagamento findTipo = null;
        for (TipoPagamento tipo:
                new TiposPagamentos().getTipos()) {
            if (tipo.getId().equals(id)){
                findTipo = tipo;
            }
        }
        return findTipo;
    }
}
