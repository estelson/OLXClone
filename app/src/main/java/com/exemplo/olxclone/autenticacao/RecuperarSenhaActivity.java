package com.exemplo.olxclone.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    }

    public void validaDados(View view) {
        String email = edt_email.getText().toString().trim();

        if(!email.isEmpty()) {
            enviarEmail(email);
        } else {
            edt_email.requestFocus();
            edt_email.setError("Informe seu e-Mail");
        }
    }

    private void enviarEmail(String email) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(this, "E-mail enviado com sucesso. Verifique sua caixa de entrada.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Erro ao enviar o e-Mail. Motivo: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void iniciaComponentes() {
        edt_email = findViewById(R.id.edt_email);
        progressBar = findViewById(R.id.progressBar);
    }
}