package com.exemplo.olxclone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.exemplo.olxclone.R;

import java.util.Locale;

public class FormAnuncioActivity extends AppCompatActivity {
    private CurrencyEditText edt_valor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio);

        iniciaComponentes();
    }

    public void selecionarCategoria(View view) {
        startActivity(new Intent(this, CategoriasActivity.class));
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Novo anúncio");

        edt_valor = findViewById(R.id.edit_valor);
        edt_valor.setLocale(new Locale("PT", "br"));
    }

}