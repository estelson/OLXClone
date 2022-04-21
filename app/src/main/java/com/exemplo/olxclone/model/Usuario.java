package com.exemplo.olxclone.model;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.exemplo.olxclone.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private String imagemPerfil;

    public Usuario() {
    }

    public void salvar(ProgressBar progressBar, String message, Context context) {
        DatabaseReference usuariosRef = FirebaseHelper.getDatabaseReference();
        usuariosRef.child("usuarios")
                .child(this.getId())
                .setValue(this)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Erro ao salvar o usuário. Motivo: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }

                    progressBar.setVisibility(View.GONE);
                });
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Exclude // Inibe a criação deste campo na tabela de usuários no Firebase Realtime Database
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getImagemPerfil() {
        return imagemPerfil;
    }

    public void setImagemPerfil(String imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }
}
