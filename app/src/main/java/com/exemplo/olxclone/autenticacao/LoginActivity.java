package com.exemplo.olxclone.autenticacao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.exemplo.olxclone.MainActivity;
import com.exemplo.olxclone.R;
import com.exemplo.olxclone.helper.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText edt_email;
    private EditText edt_senha;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciaComponentes();
    }

    public void validaDados(View view) {
        String email = edt_email.getText().toString().trim();
        String senha = edt_senha.getText().toString().trim();

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);

                logar(email, senha);
            } else {
                edt_senha.requestFocus();
                edt_senha.setError("Informe sua senha");
            }
        } else {
            edt_email.requestFocus();
            edt_email.setError("Informe seu e-Mail");
        }
    }

    public void criarConta(View view) {
        startActivity(new Intent(this, CriarContaActivity.class));
    }

    public void recuperarSenha(View view) {
        startActivity(new Intent(this, RecuperarSenhaActivity.class));
    }

    private void logar(String email, String senha) {
        FirebaseHelper.getAuth().signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(this, MainActivity.class));

                finish();
            } else {
                String erro = FirebaseHelper.validaErros(task.getException().getMessage());
                Toast.makeText(this, erro, Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.GONE);
        });
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Login");

        edt_email = findViewById(R.id.edt_email);
        edt_senha = findViewById(R.id.edt_senha);

        progressBar = findViewById(R.id.progressBar);
    }
}