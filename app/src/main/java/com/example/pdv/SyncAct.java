package com.example.pdv;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SyncAct extends AppCompatActivity {

    TextView txtUltimoBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sync_act);

        txtUltimoBack = (TextView) findViewById(R.id.txtUltBackup);
        SyncDB.getUltimoBackup(SyncAct.this, txtUltimoBack);

        Button btBackup = (Button) findViewById(R.id.btBackup);
        Button btRestore = (Button) findViewById(R.id.btRestore);

        btBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SyncDB.backupDB(SyncAct.this, txtUltimoBack);
            }
        });

        btRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SyncDB.restoreDB(SyncAct.this);
            }
        });

    }
}
