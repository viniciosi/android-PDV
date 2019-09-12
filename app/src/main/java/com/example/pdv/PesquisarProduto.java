package com.example.pdv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pdv.model.Produto;
import com.google.android.material.chip.Chip;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PesquisarProduto extends AppCompatActivity {

    EditText txtSearch;
    ListView lvProdutos;
    CriarBanco databaseHelper;
    List<Produto> produtosView = new ArrayList<>();
    ImageButton btVoltar;
    CheckBox cbCachorro, cbGato, cbPassaro, cbOutros;
    ArrayAdapter<Produto> produtoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pesquisar_produto);

        databaseHelper = new CriarBanco(this);

        produtosView.addAll(databaseHelper.getAllProdutos());

        txtSearch = (EditText) findViewById(R.id.txtSearch);
        lvProdutos = (ListView) findViewById(R.id.lvProdutos);

        btVoltar = (ImageButton) findViewById(R.id.btVoltar);

        cbCachorro = (CheckBox) findViewById(R.id.cbCachorro);
        cbGato = (CheckBox) findViewById(R.id.cbGato);
        cbPassaro = (CheckBox) findViewById(R.id.cbPassaro);
        cbOutros = (CheckBox) findViewById(R.id.cbOutros);

        produtoAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, produtosView);

        lvProdutos.setAdapter(produtoAdapter);

        lvProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d("teste", ((Produto)adapterView.getItemAtPosition(i)).getCODIGO());
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Produto", (Produto)adapterView.getItemAtPosition(i));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pesquisar(editable);
            }
        });

        cbCachorro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                pesquisar(txtSearch.getText());
            }
        });

        cbGato.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                pesquisar(txtSearch.getText());
            }
        });

        cbPassaro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                pesquisar(txtSearch.getText());
            }
        });

        cbOutros.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                pesquisar(txtSearch.getText());
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    protected void pesquisar(Editable editable){
        List<Integer> tipo = new ArrayList<>();
        if (cbOutros.isChecked()){
            tipo.add(1);
        }
        if (cbCachorro.isChecked()){
            tipo.add(2);
        }
        if (cbGato.isChecked()){
            tipo.add(3);
        }
        if (cbPassaro.isChecked()){
            tipo.add(4);
        }

        if (tipo.size() == 0){
            tipo.addAll(java.util.Arrays.asList(1,2,3,4));
        }

        produtosView.clear();
        produtosView.addAll(databaseHelper.filterProduto(editable.toString(), tipo));

        produtoAdapter.notifyDataSetChanged();
    }
}
