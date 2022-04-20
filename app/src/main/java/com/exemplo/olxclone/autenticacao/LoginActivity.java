package com.exemplo.olxclone.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.exemplo.olxclone.R;
import com.exemplo.olxclone.helper.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;

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

    private void logar(String email, String senha) {
        FirebaseHelper.getAuth().signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao efetuar login. Motivo: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
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