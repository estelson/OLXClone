package com.exemplo.olxclone.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.exemplo.olxclone.R;
import com.exemplo.olxclone.helper.FirebaseHelper;
import com.exemplo.olxclone.model.Endereco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskara.widget.MaskEditText;

public class EnderecoActivity extends AppCompatActivity {

    private MaskEditText edt_cep;
    private EditText edt_uf;
    private EditText edt_municipio;
    private EditText edt_bairro;

    private ProgressBar progressBar;
    
    private Endereco endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco);

        iniciaComponentes();

        recuperaEndereco();

        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> {
            finish();
        });
    }

    public void validaDados(View view) {
        String cep = edt_cep.getMasked().trim();
        String uf = edt_uf.getText().toString().trim();
        String minicipio = edt_municipio.getText().toString().trim();
        String bairro = edt_bairro.getText().toString().trim();

        if(!cep.isEmpty()) {

            if(cep.length() == 9) {
                if(!uf.isEmpty()) {
                    if(!minicipio.isEmpty()) {
                        if(!bairro.isEmpty()) {
                            progressBar.setVisibility(View.VISIBLE);

                            if(endereco == null) {
                                endereco = new Endereco();
                            }

                            endereco.setCep(cep);
                            endereco.setUf(uf);
                            endereco.setMinicipio(minicipio);
                            endereco.setBairro(bairro);

                            endereco.salvar(FirebaseHelper.getUidFirebase(), getBaseContext(), progressBar);
                        } else {
                            edt_bairro.requestFocus();
                            edt_bairro.setError("Informe o bairro");
                        }
                    } else {
                        edt_municipio.requestFocus();
                        edt_municipio.setError("Informe o município");
                    }
                } else {
                    edt_uf.requestFocus();
                    edt_uf.setError("Informe a UF");
                }
            } else {
                edt_cep.requestFocus();
                edt_cep.setError("Informe um CEP válido");
            }
        } else {
            edt_cep.requestFocus();
            edt_cep.setError("Informe o CEP");
        }
    }

    private void recuperaEndereco() {
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(FirebaseHelper.getUidFirebase());

        enderecoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    endereco = snapshot.getValue(Endereco.class);

                    configEndereco(endereco);
                } else {
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(EnderecoActivity.this, "Nenhum endereço cadastrado", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configEndereco(Endereco endereco) {
        edt_cep.setText(endereco.getCep());
        edt_uf.setText(endereco.getUf());
        edt_municipio.setText(endereco.getMinicipio());
        edt_bairro.setText(endereco.getBairro());

        progressBar.setVisibility(View.GONE);
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Endereço");

        edt_cep = findViewById(R.id.edt_cep);
        edt_uf = findViewById(R.id.edt_uf);
        edt_municipio = findViewById(R.id.edt_municipio);
        edt_bairro = findViewById(R.id.edt_bairro);

        progressBar = findViewById(R.id.progressBar);
    }

}