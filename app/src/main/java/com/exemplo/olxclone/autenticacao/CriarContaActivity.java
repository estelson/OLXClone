package com.exemplo.olxclone.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.exemplo.olxclone.R;

public class CriarContaActivity extends AppCompatActivity {

    private EditText edt_nome;
    private EditText edt_email;
    private EditText edt_telefone;
    private EditText edt_senha;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);

        iniciaComponentes();
    }

    public void validaDados(View view) {
        String nome = edt_nome.getText().toString();
        String email = edt_email.getText().toString();
        String telefone = edt_telefone.getText().toString();
        String senha = edt_senha.getText().toString();

        if(!nome.isEmpty()) {
            if(!email.isEmpty()) {
                if(!telefone.isEmpty()) {
                    if(!senha.isEmpty()) {
                        Toast.makeText(this, "Tudo certo", Toast.LENGTH_SHORT).show();
                    } else {
                        edt_senha.requestFocus();
                        edt_senha.setError("Informe a senha");
                    }
                } else {
                    edt_telefone.requestFocus();
                    edt_telefone.setError("Informe o telefone");
                }
            } else {
                edt_email.requestFocus();
                edt_email.setError("Informe o e-Mail");
            }
        } else {
            edt_nome.requestFocus();
            edt_nome.setError("Informe o nome");
        }
    }

    private void iniciaComponentes() {
        edt_nome = findViewById(R.id.edt_nome);
        edt_email = findViewById(R.id.edt_email);
        edt_telefone = findViewById(R.id.edt_telefone);
        edt_senha = findViewById(R.id.edt_senha);

        progressBar = findViewById(R.id.progressBar);
    }

}