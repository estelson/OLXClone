package com.exemplo.olxclone.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exemplo.olxclone.R;
import com.exemplo.olxclone.adapter.RegiaoAdapter;
import com.exemplo.olxclone.helper.RegioesList;
import com.exemplo.olxclone.helper.SPFiltro;

import java.util.ArrayList;
import java.util.List;

public class RegioesActivity extends AppCompatActivity implements RegiaoAdapter.OnClickListener {

    private RecyclerView rv_regioes;

    private RegiaoAdapter regiaoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regioes);

        iniciaComponentes();

        configCliques();

        configRV();
    }

    private void configRV() {
        rv_regioes.setLayoutManager((new LinearLayoutManager(this)));
        rv_regioes.setHasFixedSize(true);

        regiaoAdapter = new RegiaoAdapter(RegioesList.getList(SPFiltro.getFiltro(this).getEstado().getUf()), this);

        rv_regioes.setAdapter(regiaoAdapter);
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> {
            finish();
        });
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Selecione a região");

        rv_regioes = findViewById(R.id.rv_regioes);
    }

    @Override
    public void OnCLick(String regiao) {
        Toast.makeText(this, regiao, Toast.LENGTH_SHORT).show();
    }
}