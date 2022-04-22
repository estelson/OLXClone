package com.exemplo.olxclone.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.exemplo.olxclone.R;
import com.exemplo.olxclone.helper.FirebaseHelper;
import com.exemplo.olxclone.model.Categoria;
import com.exemplo.olxclone.model.Endereco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class FormAnuncioActivity extends AppCompatActivity {

    private final int REQUEST_CATEGORIA = 100;

    private CurrencyEditText edt_valor;

    private Button btn_categoria;

    private EditText edt_cep;

    private String categoriaSelecionada = "";

    private Endereco enderecoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio);

        iniciaComponentes();

        recuperaEndereco();
    }

    public void selecionarCategoria(View view) {
        Intent intent = new Intent(this, CategoriasActivity.class);
        startActivityForResult(intent, REQUEST_CATEGORIA);
    }

    private void recuperaEndereco() {
        configCep();

        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(FirebaseHelper.getUidFirebase());

        enderecoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                enderecoUsuario = snapshot.getValue(Endereco.class);
                edt_cep.setText(enderecoUsuario.getCep());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configCep() {
        edt_cep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            /**
             * Faz a consulta de CEP no webservice ViaCEP (https://viacep.com.br/) e recupera o
             * endereço a partir do CEP do cadastro do usuário.
             * @param charSequence
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Toast.makeText(FormAnuncioActivity.this, "Endereço recuperado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Novo anúncio");

        edt_valor = findViewById(R.id.edit_valor);
        edt_valor.setLocale(new Locale("PT", "br"));

        edt_cep = findViewById(R.id.edt_cep);

        btn_categoria = findViewById(R.id.btn_categoria);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_CATEGORIA) {
                Categoria categoria = (Categoria) data.getSerializableExtra("categoriaSelecionada");
                categoriaSelecionada = categoria.getNome();

                btn_categoria.setText(categoriaSelecionada);
            } else if(true) { // Controla a seleção de imagens da câmera

            } else { // Controla a seleção de imagens da galeria do dispositivo

            }
        }
    }
}