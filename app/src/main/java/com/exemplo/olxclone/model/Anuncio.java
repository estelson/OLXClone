package com.exemplo.olxclone.model;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.exemplo.olxclone.activities.MainActivity;
import com.exemplo.olxclone.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Anuncio implements Serializable {

    private String id;
    private String idUsuario;
    private String titulo;
    private double valor;
    private String categoria;
    private String descricao;
    private Local local;
    private long dataPublicacao;
    private List<String> urlImagens = new ArrayList<>();

    public Anuncio() {
        DatabaseReference anuncioRef = FirebaseHelper.getDatabaseReference();

        this.setId(anuncioRef.push().getKey());
    }

    public void salvar(Activity activity, boolean novoAnuncio) {
        DatabaseReference anuncioPublicoRef = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos")
                .child(this.getId());

        anuncioPublicoRef.setValue(this);

        DatabaseReference meusAnunciosRef = FirebaseHelper.getDatabaseReference()
                .child("meus_anuncios")
                .child(FirebaseHelper.getUidFirebase())
                .child(this.getId());

        meusAnunciosRef.setValue(this);

        if(novoAnuncio) {
            DatabaseReference dataAnuncioPublicoRef = anuncioPublicoRef
                    .child("dataPublicacao");

            dataAnuncioPublicoRef.setValue(ServerValue.TIMESTAMP);

            DatabaseReference dataMeusAnunciosRef = meusAnunciosRef
                    .child("dataPublicacao");

            dataMeusAnunciosRef.setValue(ServerValue.TIMESTAMP).addOnCompleteListener(task ->  {
                activity.finish();

                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("id", 2);
                activity.startActivity(intent);
            });

            Toast.makeText(activity, "Anúncio incluído com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            activity.finish();

            Toast.makeText(activity, "Anúncio alterado com sucesso", Toast.LENGTH_SHORT).show();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public long getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(long dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public List<String> getUrlImagens() {
        return urlImagens;
    }

    public void setUrlImagem(List<String> urlImagens) {
        this.urlImagens = urlImagens;
    }

}
