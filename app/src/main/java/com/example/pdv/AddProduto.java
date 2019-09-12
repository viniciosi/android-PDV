package com.example.pdv;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pdv.model.Produto;
import com.example.pdv.model.Tipos;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class AddProduto extends AppCompatActivity {

    public TextView txtCodigo;
    public TextView txtDescricao;
    public Spinner spTipo;
    public RadioButton rbUnidade, rbKg;
    public EditText txtPreco;
    public ImageButton salvar, cancelar;

    CriarBanco db;

    Produto produtoEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_produto);

        txtCodigo = findViewById(R.id.txtCodigo);
        txtDescricao = findViewById(R.id.txtDescricao);

        spTipo = findViewById(R.id.spTipo);
        ArrayAdapter<Tipos.Tipo> tipoAdapter = new ArrayAdapter<Tipos.Tipo>(this, R.layout.support_simple_spinner_dropdown_item, new Tipos().getTipos());
        spTipo.setAdapter(tipoAdapter);

        rbUnidade = findViewById(R.id.rbUnidade);
        rbKg = findViewById(R.id.rbKg);
        txtPreco = findViewById(R.id.txtPreco);

        txtPreco.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            private String current = "";
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(current)) {
                    Locale myLocale = new Locale("pt", "BR");
                    //Nesse bloco ele monta a maskara para money
                    txtPreco.removeTextChangedListener(this);
                    String cleanString = charSequence.toString().replaceAll("[R$,.]", "");
                    Float parsed = Float.parseFloat(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance(myLocale).format((parsed / 100));
                    current = formatted;
                    txtPreco.setText(formatted);
                    txtPreco.setSelection(formatted.length());

                    txtPreco.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        salvar = findViewById(R.id.btSalvar);
        cancelar = findViewById(R.id.btCancelar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            produtoEdit = (Produto) bundle.getSerializable("produto");

        if (produtoEdit != null){
            txtCodigo.setText(produtoEdit.getCODIGO());
            txtDescricao.setText(produtoEdit.getDESCRICAO());
            spTipo.setSelection(tipoAdapter.getPosition(Tipos.getTipo(produtoEdit.getTIPO())));
            if (produtoEdit.getUNIDADE() == 1){
                rbUnidade.setChecked(true);
                rbKg.setChecked(false);
            }else{
                rbUnidade.setChecked(false);
                rbKg.setChecked(true);
            }
            Locale myLocale = new Locale("pt", "BR");
            txtPreco.setText(NumberFormat.getCurrencyInstance(myLocale).format(Float.parseFloat(produtoEdit.getPRECO().toString())));
        }

        db = new CriarBanco(this);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProduto.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtCodigo.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Código é obrigatório.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (txtDescricao.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Descrição é obrigatória.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (txtPreco.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Preço é obrigatório.", Toast.LENGTH_LONG).show();
                    return;
                }

                Produto produto = new Produto();
                produto.setCODIGO(txtCodigo.getText().toString());
                produto.setDESCRICAO(txtDescricao.getText().toString());
                produto.setTIPO (((Tipos.Tipo) spTipo.getSelectedItem()).getId());
                if (rbUnidade.isChecked()){
                    produto.setUNIDADE(1);
                }else {
                    produto.setUNIDADE(2);
                }
                produto.setPRECO(Float.parseFloat(txtPreco.getText().toString().replaceAll("[R$.]", "").replace(",", ".")));

                long id;
                if (produtoEdit != null){
                    id = db.updateData(produto);
                }else{
                    id = db.insertProduto(produto);
                }

                MainActivity.notifyAdapter();
                Intent intent = new Intent(AddProduto.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
