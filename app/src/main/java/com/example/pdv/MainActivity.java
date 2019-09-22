package com.example.pdv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pdv.adapter.ListProdutoAdapter;
import com.example.pdv.model.Produto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static ListProdutoAdapter prodAdapter;
    RecyclerView recyclerView;
    CriarBanco databaseHelper;
    FloatingActionButton btNew;
    EditText txtSearch;
    List<Produto> allProdutos = new ArrayList<>();

    public static void notifyAdapter() {
        prodAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SyncDB.backupDB(this);

        recyclerView = (RecyclerView) findViewById(R.id.rvListProdutos);
        databaseHelper = new CriarBanco(this);

        btNew = (FloatingActionButton) findViewById(R.id.btNew);
        btNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddProduto.class);
                startActivity(intent);
                finish();
            }
        });

        txtSearch = (EditText)findViewById(R.id.txtSearch);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                allProdutos.clear();
                allProdutos.addAll(databaseHelper.filterProduto(txtSearch.getText().toString(), java.util.Arrays.asList(1,2,3,4)));
                notifyAdapter();
            }
        });

        allProdutos.addAll(databaseHelper.getAllProdutos());
        prodAdapter = new ListProdutoAdapter(this, allProdutos, databaseHelper);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(prodAdapter);
    }
}
