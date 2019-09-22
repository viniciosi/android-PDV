package com.example.pdv.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdv.MainActivity;
import com.example.pdv.R;
import com.example.pdv.TelaVenda;
import com.example.pdv.model.Produto;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListProdutoVendaAdapter extends RecyclerView.Adapter<ListProdutoVendaAdapter.MyViewHolder> {

    Locale myLocale = new Locale("pt", "BR");
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(myLocale);

    public static class ItemVenda{
        private Produto produto;
        private Integer qtd;
        private Float preco;

        public Produto getProduto() {
            return produto;
        }

        public void setProduto(Produto produto) {
            this.produto = produto;
        }

        public Integer getQtd() {
            return qtd;
        }

        public void setQtd(Integer qtd) {
            this.qtd = qtd;
        }

        public Float getPreco() {
            return preco;
        }

        public void setPreco(Float preco) {
            this.preco = preco;
        }
    }

    private List<ItemVenda> list;


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView txtCodigo, txtDescricao, txtPreco, txtQtd, txtSubTotal;
        public ImageButton btDelete;

        public MyViewHolder(View view){
            super(view);

            txtCodigo = view.findViewById(R.id.txtCodigo);
            txtDescricao = view.findViewById(R.id.txtDescricao);
            txtPreco = view.findViewById(R.id.txtPreco);
            txtQtd = view.findViewById(R.id.txtQtd);
            txtSubTotal = view.findViewById(R.id.txtSubTotal);
            btDelete = view.findViewById(R.id.btDelete);
        }
    }

    public ListProdutoVendaAdapter(List<ItemVenda> listaVenda){
        this.list = listaVenda;
    }

    @NonNull
    @Override
    public ListProdutoVendaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produto_venda, parent, false);
        return new ListProdutoVendaAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListProdutoVendaAdapter.MyViewHolder holder, final int position) {
        ItemVenda itemVenda = list.get(position);

        if (itemVenda.getProduto() == null){
            holder.txtCodigo.setText("0");
            holder.txtDescricao.setText("Prod. Desconhecido");
        } else {
            holder.txtCodigo.setText(itemVenda.getProduto().getCODIGO());
            holder.txtDescricao.setText(itemVenda.getProduto().getDESCRICAO());
        }
        holder.txtPreco.setText(numberFormat.format(itemVenda.getPreco()));
        holder.txtQtd.setText(itemVenda.getQtd().toString());
        holder.txtSubTotal.setText(numberFormat.format ((Float)(itemVenda.getPreco() * itemVenda.getQtd())));
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                TelaVenda.notifyAdapter();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<ItemVenda> getItemVenda(){return list;}

    public void clear(){list.clear();}

    public Float getTotal(){
        Float total = Float.valueOf(0);
        for (ItemVenda item:list ) {
            total += item.getPreco() * item.getQtd();
        }
        return total;
    }
}
