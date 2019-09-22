package com.example.pdv;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReportAct extends AppCompatActivity {

    CriarBanco db = new CriarBanco(this);
    ArrayAdapter<String> itemsRelatorio;
    ArrayList<String> vendas;
    ListView grid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.report_act);

        grid = findViewById(R.id.lv);

        vendas = db.getVendas("");

        itemsRelatorio = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, vendas);
        grid.setAdapter(itemsRelatorio);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String item = grid.getItemAtPosition(i).toString().split(" - ")[0];

                ArrayList<String> novasVendas = null;

                if (item.length() <= 7 ) {

                    vendas.clear();

                    if (item.length() == 7) {
                        novasVendas = db.getVendas(item);
                        vendas.add("...");
                    } else if (item.equals("...")) {
                        novasVendas = db.getVendas("");
                    }

                    vendas.addAll(novasVendas);
                    itemsRelatorio.notifyDataSetChanged();
                }
            }
        });

    }
}
