package com.exemplo.olxclone.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.exemplo.olxclone.MainActivity;
import com.exemplo.olxclone.R;
import com.exemplo.olxclone.helper.FirebaseHelper;
import com.exemplo.olxclone.model.Usuario;

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

        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> {
            finish();
        });
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
                        progressBar.setVisibility(View.VISIBLE);

                        Usuario usuario = new Usuario();
                        usuario.setNome(nome.trim());
                        usuario.setEmail(email.trim());
                        usuario.setTelefone(telefone.trim());
                        usuario.setSenha(senha.trim());

                        cadastrarUsuario(usuario);
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

    private void cadastrarUsuario(Usuario usuario) {
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                String id = task.getResult().getUser().getUid();

                usuario.setId(id);

                usuario.salvar();

                // Redirecionar o usuário para a tela home do app
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
        text_toolbar.setText("Criar conta");

        edt_nome = findViewById(R.id.edt_nome);
        edt_email = findViewById(R.id.edt_email);
        edt_telefone = findViewById(R.id.edt_telefone);
        edt_senha = findViewById(R.id.edt_senha);

        progressBar = findViewById(R.id.progressBar);
    }

}