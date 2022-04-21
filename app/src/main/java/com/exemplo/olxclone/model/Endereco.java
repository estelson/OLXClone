package com.exemplo.olxclone.model;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.exemplo.olxclone.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

public class Endereco {

    private String cep;
    private String uf;
    private String minicipio;
    private String bairro;

    public Endereco() {
    }

    public void salvar(String idUser, Context context, ProgressBar progressBar) {
        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(idUser);

        enderecoRef.setValue(this).addOnCompleteListener(task -> {
            String msg = "";
            if(task.isSuccessful()) {
                msg = "Endereço salvo com sucesso";
            } else {
                msg = "Erro ao salvar endereço. Motivo: " + task.getException().getMessage();
            }

            progressBar.setVisibility(View.GONE);

            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        });
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getMinicipio() {
        return minicipio;
    }

    public void setMinicipio(String minicipio) {
        this.minicipio = minicipio;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
}
