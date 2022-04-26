package com.exemplo.olxclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exemplo.olxclone.R;
import com.exemplo.olxclone.adapter.EstadoAdapter;
import com.exemplo.olxclone.helper.EstadosList;
import com.exemplo.olxclone.helper.SPFiltro;
import com.exemplo.olxclone.model.Estado;

public class EstadosActivity extends AppCompatActivity implements EstadoAdapter.OnClickListener {

    private RecyclerView rv_estados;
    private EstadoAdapter estadoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estados);

        iniciaComponentes();

        configRV();

        configCliques();
    }


    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> {
            finish();
        });
    }

    private void configRV() {
        rv_estados.setLayoutManager(new LinearLayoutManager(this));
        rv_estados.setHasFixedSize(true);

        estadoAdapter = new EstadoAdapter(EstadosList.getList(), this);
        rv_estados.setAdapter(estadoAdapter);
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Estados");

        rv_estados = findViewById(R.id.rv_estados);
    }

    @Override
    public void OnCLick(Estado estado) {
        SPFiltro.setFiltro(this, "ufEstado", estado.getUf());
        SPFiltro.setFiltro(this, "nomeEstado", estado.getNome());

        startActivity(new Intent(this, RegioesActivity.class));
    }
}