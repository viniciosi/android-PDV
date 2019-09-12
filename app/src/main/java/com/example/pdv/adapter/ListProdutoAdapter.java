package com.example.pdv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdv.AddProduto;
import com.example.pdv.CriarBanco;
import com.example.pdv.MainActivity;
import com.example.pdv.R;
import com.example.pdv.model.Produto;
import com.example.pdv.model.Tipos;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListProdutoAdapter extends RecyclerView.Adapter<ListProdutoAdapter.MyViewHolder> {

    private Context context;
    private List<Produto> list;
    private CriarBanco databaseHelper;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        //public TextView txtCodigo;
        public TextView txtDescricao;
        public TextView txtTipo;
        public TextView txtPreco;
        public ImageButton edit, delete;

        public MyViewHolder(View view){
            super(view);

            //txtCodigo = view.findViewById(R.id.txtCodigo);
            txtDescricao = view.findViewById(R.id.txtDescricao);
            txtTipo = view.findViewById(R.id.txtTipo);
            txtPreco = view.findViewById(R.id.txtPreco);
            edit = view.findViewById(R.id.btEditar);
            delete = view.findViewById(R.id.btExcluir);
        }
    }

    public ListProdutoAdapter(Context context, List<Produto> list, CriarBanco dbhelper){
        this.context = context;
        this.list = list;
        this.databaseHelper = dbhelper;
    }

    public void setList(List<Produto> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ListProdutoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produtos, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListProdutoAdapter.MyViewHolder holder, final int position) {

        Produto produto = list.get(position);
        //holder.txtCodigo.setText(produto.getCODIGO());
        holder.txtDescricao.setText(produto.getDESCRICAO());
        //holder.txtTipo.setText(produto.getTIPO().toString());
        holder.txtTipo.setText(Tipos.getTipo(produto.getTIPO()).getDescricao());

        Locale myLocale = new Locale("pt", "BR");

        holder.txtPreco.setText(NumberFormat.getCurrencyInstance(myLocale).format(Float.parseFloat(produto.getPRECO().toString())));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduto(position);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddProduto.class);
                intent.putExtra("produto", (Serializable) list.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    private void deleteProduto(int position){
        databaseHelper.deleteProduto(list.get(position));
        list.remove(position);
        MainActivity.notifyAdapter();
    }
}
