package com.exemplo.olxclone.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.exemplo.olxclone.R;
import com.exemplo.olxclone.helper.FirebaseHelper;
import com.exemplo.olxclone.model.Endereco;

public class EnderecoActivity extends AppCompatActivity {

    private EditText edt_cep;
    private EditText edt_uf;
    private EditText edt_municipio;
    private EditText edt_bairro;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco);

        iniciaComponentes();
    }

    public void validaDados(View view) {
        String cep = edt_cep.getText().toString().trim();
        String uf = edt_uf.getText().toString().trim();
        String minicipio = edt_municipio.getText().toString().trim();
        String bairro = edt_bairro.getText().toString().trim();

        if(!cep.isEmpty()) {
            if(!uf.isEmpty()) {
                if(!minicipio.isEmpty()) {
                    if(!bairro.isEmpty()) {
                        Endereco endereco = new Endereco();
                        endereco.setCep(cep);
                        endereco.setUf(uf);
                        endereco.setMinicipio(minicipio);
                        endereco.setBairro(bairro);

                        endereco.salvar(FirebaseHelper.getUidFirebase());

                        Toast.makeText(this, "Endereço salvo com sucesso", Toast.LENGTH_SHORT).show();
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
            edt_cep.setError("Informe o CEP");
        }
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Endereço");

        edt_cep = findViewById(R.id.id_cep);
        edt_uf = findViewById(R.id.edt_uf);
        edt_municipio = findViewById(R.id.edt_municipio);
        edt_bairro = findViewById(R.id.edt_bairro);

        progressBar = findViewById(R.id.progressBar);
    }

}