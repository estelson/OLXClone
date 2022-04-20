package com.exemplo.olxclone.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.exemplo.olxclone.R;
import com.exemplo.olxclone.helper.FirebaseHelper;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private EditText edt_email;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        iniciaComponentes();

        configCliques();
    }

    public void validaDados(View view) {
        String email = edt_email.getText().toString().trim();

        if(!email.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);

            enviarEmail(email);
        } else {
            edt_email.requestFocus();
            edt_email.setError("Informe seu e-Mail");
        }
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> {
            finish();
        });
    }

    private void enviarEmail(String email) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(this, "E-mail enviado com sucesso. Verifique sua caixa de entrada.", Toast.LENGTH_LONG).show();
            } else {
                Log.i("INFOTESTE", "logar: " + task.getException().getMessage());

                String erro = FirebaseHelper.validaErros(task.getException().getMessage());
                Toast.makeText(this, erro, Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.GONE);
        });
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Recuperação de senha");

        edt_email = findViewById(R.id.edt_email);
        progressBar = findViewById(R.id.progressBar);
    }
}