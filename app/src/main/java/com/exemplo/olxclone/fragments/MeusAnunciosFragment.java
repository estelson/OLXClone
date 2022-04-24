package com.exemplo.olxclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.exemplo.olxclone.R;
import com.exemplo.olxclone.adapter.AnuncioAdapter;
import com.exemplo.olxclone.helper.FirebaseHelper;
import com.exemplo.olxclone.model.Anuncio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeusAnunciosFragment extends Fragment implements AnuncioAdapter.OnClickListener {

    private AnuncioAdapter anuncioAdapter;

    private List<Anuncio> anuncioList = new ArrayList<>();

    private ProgressBar progressBar;

    private TextView text_info;

    private Button btn_logar;

    private SwipeableRecyclerView rv_anuncios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meus_anuncios, container, false);

        iniciaComponentes(view);

        configRV();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        recuperaAnuncios();
    }

    private void recuperaAnuncios() {
        if(FirebaseHelper.getAutenticado()) {
            DatabaseReference anunciosRef = FirebaseHelper.getDatabaseReference()
                    .child("meus_anuncios")
                    .child(FirebaseHelper.getUidFirebase());

            anunciosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            Anuncio anuncio = ds.getValue(Anuncio.class);
                            anuncioList.add(anuncio);
                        }

                        text_info.setText("");

                        Collections.reverse(anuncioList);
                        anuncioAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                    } else {
                        text_info.setText("Nenhum anúncio cadastrado");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            btn_logar.setVisibility(View.VISIBLE);
            text_info.setText("");

            progressBar.setVisibility(View.GONE);
        }
    }

    private void configRV() {
        rv_anuncios.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_anuncios.setHasFixedSize(true);

        anuncioAdapter = new AnuncioAdapter(anuncioList, this);
        rv_anuncios.setAdapter(anuncioAdapter);
    }

    private void iniciaComponentes(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        text_info = view.findViewById(R.id.text_info);
        btn_logar = view.findViewById(R.id.btn_logar);
        rv_anuncios = view.findViewById(R.id.rv_anuncios);
    }

    @Override
    public void OnClick(Anuncio anuncio) {

    }

}