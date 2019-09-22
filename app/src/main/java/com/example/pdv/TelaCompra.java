package com.example.pdv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdv.adapter.ListProdutoVendaAdapter;
import com.example.pdv.model.Produto;
import com.example.pdv.model.TiposPagamentos;

import java.util.ArrayList;
import java.util.List;

public class TelaCompra extends AppCompatActivity {

    public static ListProdutoVendaAdapter prodAdapter;
    RecyclerView rvProdutos;
    EditText txtCodigo, txtQtd, txtPreco, txtSubTotal;
    ImageButton btSearchProd, btAdd, btFechar;
    TextView txtProduto;
    static TextView txtTotal;

    CriarBanco databaseHelper;

    PopupWindow pw;
    LinearLayout layout, mainLayout;
    LayoutInflater inflater;

    List<ListProdutoVendaAdapter.ItemVenda> lItemVenda;

    public static void notifyAdapter() {
        prodAdapter.notifyDataSetChanged();
        txtTotal.setText(prodAdapter.getTotal().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tela_venda);

        rvProdutos = (RecyclerView) findViewById(R.id.rvProdutos);
        txtCodigo = (EditText) findViewById(R.id.txtCodigo);
        txtQtd = (EditText) findViewById(R.id.txtQtd);
        txtPreco = (EditText)findViewById(R.id.txtPreco);
        txtSubTotal = (EditText)findViewById(R.id.txtSubTotal);
        txtTotal = (TextView)findViewById(R.id.txtTotal);
        txtProduto  = (TextView)findViewById(R.id.txtProduto);
        btSearchProd = (ImageButton) findViewById(R.id.btSearchProd);
        btAdd = (ImageButton) findViewById(R.id.btAdd);
        btFechar = (ImageButton)findViewById(R.id.btFechar);

        lItemVenda = new ArrayList<>();
        prodAdapter = new ListProdutoVendaAdapter(lItemVenda);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvProdutos.setLayoutManager(mLayoutManager);
        rvProdutos.setItemAnimator(new DefaultItemAnimator());
        rvProdutos.setAdapter(prodAdapter);

        databaseHelper = new CriarBanco(this);

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        pw = new PopupWindow(inflater.inflate(R.layout.fechar_venda, null), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        ((Spinner)pw.getContentView().findViewById(R.id.spTpPagamento)).setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, new TiposPagamentos().getTipos()));

        ((TextView)pw.getContentView().findViewById(R.id.txtValor)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                TextView tTotal = ((TextView)pw.getContentView().findViewById(R.id.txtTotal));
                TextView tValor = ((TextView)pw.getContentView().findViewById(R.id.txtValor));
                TextView tTroco = ((TextView)pw.getContentView().findViewById(R.id.txtTroco));
                tTroco.setText(String.valueOf(Float.parseFloat(tValor.getText().toString()) - Float.parseFloat(tTotal.getText().toString())));
            }
        });

        ((ImageButton)pw.getContentView().findViewById(R.id.btVoltar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });

        ((ImageButton)pw.getContentView().findViewById(R.id.btFechar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.insertVenda(prodAdapter.getItemVenda());
                prodAdapter.clear();
                prodAdapter.notifyDataSetChanged();
                pw.dismiss();
            }
        });

        btFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView)pw.getContentView().findViewById(R.id.txtTotal)).setText(txtTotal.getText().toString());
                ((TextView)pw.getContentView().findViewById(R.id.txtTotal)).setEnabled(false);
                ((TextView)pw.getContentView().findViewById(R.id.txtValor)).setText(txtTotal.getText().toString());
                ((TextView)pw.getContentView().findViewById(R.id.txtValor)).requestFocus();
                ((TextView)pw.getContentView().findViewById(R.id.txtTroco)).setText("0");
                ((TextView)pw.getContentView().findViewById(R.id.txtTroco)).setEnabled(false);
                pw.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });

        btSearchProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaCompra.this, PesquisarProduto.class);
                startActivityForResult(intent, 0);
            }
        });

        txtCodigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()){
                    txtCodigo.removeTextChangedListener(this);
                    Produto produto = databaseHelper.getProduto(editable.toString());
                    if (produto != null){
                        preencheItem(produto);
                    }else {
                        limparItem();
                    }
                    txtCodigo.addTextChangedListener(this);
                }else{
                    limparItem();
                }
            }
        });

        txtCodigo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    txtQtd.requestFocus();
                    return true;
                }
                return false;
            }
        });

        txtQtd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (txtQtd.hasFocus()) {
                    if (txtQtd.getText().toString().isEmpty() || txtPreco.getText().toString().isEmpty()) {
                        txtSubTotal.setText("0");
                    }else{
                        txtSubTotal.setText(String.valueOf(Integer.parseInt(txtQtd.getText().toString()) * Float.parseFloat(txtPreco.getText().toString())));
                    }
                }
            }
        });

        txtQtd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    txtPreco.requestFocus();
                    return true;
                }
                return false;
            }
        });

        txtPreco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (txtPreco.hasFocus()) {
                    if (txtQtd.getText().toString().isEmpty() || txtPreco.getText().toString().isEmpty()) {
                        txtSubTotal.setText("0");
                    }else{
                        txtSubTotal.setText(String.valueOf(Integer.parseInt(txtQtd.getText().toString()) * Float.parseFloat(txtPreco.getText().toString())));
                    }
                }
            }
        });

        txtPreco.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    btAdd.requestFocus();
                    return true;
                }
                return false;
            }
        });

        txtSubTotal.setEnabled(false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0){
            if (resultCode == Activity.RESULT_OK) {
                //Log.d("resultado", ((Produto)data.getSerializableExtra("Produto")).getDESCRICAO());
                txtCodigo.setText(((Produto)data.getSerializableExtra("Produto")).getCODIGO());
                txtQtd.requestFocus();
                //preencheItem(((Produto)data.getSerializableExtra("Produto")));
            }
        }
    }

    protected void preencheItem(Produto produto){
        //txtCodigo.setText(produto.getCODIGO());
        txtProduto.setText(produto.getDESCRICAO());
        txtPreco.setText(produto.getPRECO().toString());

        txtSubTotal.setText(String.valueOf (Integer.parseInt(txtQtd.getText().toString()) * produto.getPRECO()));
    }

    protected void limparItem(){
        //txtCodigo.setText("");
        txtProduto.setText("");
        txtPreco.setText("");
        txtSubTotal.setText("");
    }

    protected void addItem(){

        if(!(txtCodigo.getText().toString().isEmpty() || txtPreco.getText().toString().isEmpty() || txtQtd.getText().toString().isEmpty())) {

            ListProdutoVendaAdapter.ItemVenda itemVenda = new ListProdutoVendaAdapter.ItemVenda();

            itemVenda.setProduto(databaseHelper.getProduto(txtCodigo.getText().toString()));
            itemVenda.setPreco(Float.parseFloat(txtPreco.getText().toString()));
            itemVenda.setQtd(Integer.parseInt(txtQtd.getText().toString()));

            lItemVenda.add(itemVenda);

            prodAdapter.notifyDataSetChanged();

            limparItem();
            txtCodigo.setText("");
            txtCodigo.requestFocus();
            txtQtd.setText("1");

            txtTotal.setText(prodAdapter.getTotal().toString());
        }

    }
}
