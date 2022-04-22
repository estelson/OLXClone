package com.exemplo.olxclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.exemplo.olxclone.R;
import com.exemplo.olxclone.api.CEPService;
import com.exemplo.olxclone.helper.FirebaseHelper;
import com.exemplo.olxclone.model.Categoria;
import com.exemplo.olxclone.model.Endereco;
import com.exemplo.olxclone.model.Local;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskara.widget.MaskEditText;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormAnuncioActivity extends AppCompatActivity {

    private final int REQUEST_CATEGORIA = 100;

    private CurrencyEditText edt_valor;

    private Button btn_categoria;

    private MaskEditText edt_cep;
    private Retrofit retrofit;

    private String categoriaSelecionada = "";

    private ProgressBar progressBar;

    private TextView txt_local;

    private Endereco enderecoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio);

        iniciaComponentes();

        iniciaRetrofit();

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

                progressBar.setVisibility(View.GONE);
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
                String cep = charSequence.toString().replaceAll("_", "").replace("-", "");
                if(cep.length() == 8) {
                    buscarEndereco(cep);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void buscarEndereco(String cep) {
        progressBar.setVisibility(View.VISIBLE);

        CEPService cepService = retrofit.create(CEPService.class);
        Call<Local> call = cepService.recuperaCEP(cep);

        call.enqueue(new Callback<Local>() {
            @Override
            public void onResponse(Call<Local> call, Response<Local> response) {

            }

            @Override
            public void onFailure(Call<Local> call, Throwable t) {
                Toast.makeText(FormAnuncioActivity.this, "Serviço de busca de CEP indisponível.\nTente novamente mais tarde.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciaRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Novo anúncio");

        edt_valor = findViewById(R.id.edit_valor);
        edt_valor.setLocale(new Locale("PT", "br"));

        edt_cep = findViewById(R.id.edt_cep);

        btn_categoria = findViewById(R.id.btn_categoria);

        progressBar = findViewById(R.id.progressBar);

        txt_local = findViewById(R.id.txt_local);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CATEGORIA) {
                Categoria categoria = (Categoria) data.getSerializableExtra("categoriaSelecionada");
                categoriaSelecionada = categoria.getNome();

                btn_categoria.setText(categoriaSelecionada);
            } else if (true) { // Controla a seleção de imagens da câmera

            } else { // Controla a seleção de imagens da galeria do dispositivo

            }
        }
    }
}