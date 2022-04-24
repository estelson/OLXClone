package com.exemplo.olxclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.exemplo.olxclone.R;
import com.exemplo.olxclone.adapter.CategoriaAdapter;
import com.exemplo.olxclone.helper.CategoriaList;
import com.exemplo.olxclone.model.Categoria;

public class CategoriasActivity extends AppCompatActivity implements CategoriaAdapter.OnClickListener {

    private RecyclerView rv_categorias;
    
    private CategoriaAdapter categoriaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        iniciaComponentes();

        configCliques();

        iniciaRV();
    }

    private void iniciaRV() {
        rv_categorias.setLayoutManager(new LinearLayoutManager(this));
        rv_categorias.setHasFixedSize(true);
        
        categoriaAdapter = new CategoriaAdapter(CategoriaList.getList(false), this);

        rv_categorias.setAdapter(categoriaAdapter);
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> {
            finish();
        });
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Categorias");

        rv_categorias = findViewById(R.id.rv_categorias);
    }

    @Override
    public void OnClick(Categoria categoria) {
        Intent intent = new Intent();
        intent.putExtra("categoriaSelecionada", categoria);

        setResult(RESULT_OK, intent);

        finish();
    }
}