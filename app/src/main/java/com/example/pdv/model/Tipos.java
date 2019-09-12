package com.example.pdv.model;

import java.util.ArrayList;
import java.util.List;

public class Tipos {

    public class Tipo{
        private Integer id;
        private String descricao;

        public Tipo(Integer id, String descricao){
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
            if (obj instanceof Tipo) {
                Tipo t = (Tipo) obj;
                if(t.getDescricao().equals(descricao) && t.getId()==id) return true;
            }
            return  false;
        }
    }

    private List<Tipo> tipos;

    public Tipos(){
        tipos = new ArrayList<>();

        tipos.add(new Tipo(1, "Outros"));
        tipos.add(new Tipo(2, "Ração Cachorro"));
        tipos.add(new Tipo(3, "Ração Gato"));
        tipos.add(new Tipo(4, "Ração Pássaro"));
    }

    public List<Tipo> getTipos(){
        return this.tipos;
    }

    public static Tipo getTipo(Integer id){
        Tipo findTipo = null;
        for (Tipo tipo:
                new Tipos().getTipos()) {
            if (tipo.getId().equals(id)){
                findTipo = tipo;
            }
        }
        return findTipo;
    }
}
