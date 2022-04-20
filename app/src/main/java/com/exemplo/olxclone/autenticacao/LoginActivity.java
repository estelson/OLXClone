package com.exemplo.olxclone.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.exemplo.olxclone.R;

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

        if(!email.isEmpty()) {
            if(!senha.isEmpty()) {
                Toast.makeText(this, "Tudo certo", Toast.LENGTH_SHORT).show();
            } else {
                edt_senha.requestFocus();
                edt_senha.setError("Informe sua senha");
            }
        } else {
            edt_email.requestFocus();
            edt_email.setError("Informe seu e-Mail");
        }
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Login");

        edt_email = findViewById(R.id.edt_email);
        edt_senha = findViewById(R.id.edt_senha);

        progressBar = findViewById(R.id.progressBar);
    }
}