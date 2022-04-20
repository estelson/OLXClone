package com.exemplo.olxclone.model;

import com.exemplo.olxclone.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;

    public Usuario() {
    }

    public void salvar() {
        DatabaseReference usuariosRef = FirebaseHelper.getDatabaseReference();
        usuariosRef.child("usuarios")
                .child(this.getId())
                .setValue(this);

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

}
