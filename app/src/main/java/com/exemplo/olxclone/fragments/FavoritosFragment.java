package com.exemplo.olxclone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exemplo.olxclone.R;
import com.exemplo.olxclone.activities.DetalhesAnuncioActivity;
import com.exemplo.olxclone.adapter.AnuncioAdapter;
import com.exemplo.olxclone.helper.FirebaseHelper;
import com.exemplo.olxclone.model.Anuncio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoritosFragment extends Fragment implements AnuncioAdapter.OnClickListener {

    private AnuncioAdapter anuncioAdapter;
    private List<Anuncio> anuncioList = new ArrayList<>();

    private ProgressBar progressBar;
    private TextView text_info;
    private Button btn_logar;
    private RecyclerView rv_anuncios;

    private List<String> favoritosList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        iniciaComponentes(view);

        configRV();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        recuperaFavoritos();
    }

    private void recuperaFavoritos() {
        favoritosList.clear();
        anuncioList.clear();

        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference favoritosRef = FirebaseHelper.getDatabaseReference()
                    .child("favoritos")
                    .child(FirebaseHelper.getUidFirebase());

            favoritosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    favoritosList.clear();
                    anuncioList.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        favoritosList.add(ds.getValue(String.class));
                    }

                    if(favoritosList.size() > 0) {
                        recuperaAnuncios();
                    } else {
                        text_info.setText("Nenhum anúncio marcado como favorito");
                        progressBar.setVisibility(View.GONE);

                        anuncioAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void recuperaAnuncios() {
        for (int i = 0; i < favoritosList.size(); i++) {
            DatabaseReference anunciosRef = FirebaseHelper.getDatabaseReference()
                    .child("anuncios_publicos")
                    .child(favoritosList.get(i));

            anunciosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Anuncio anuncio = snapshot.getValue(Anuncio.class);
                    anuncioList.add(anuncio);

                    if(anuncioList.size() == favoritosList.size()) {
                        text_info.setText("");

                        Collections.reverse(anuncioList);
                        anuncioAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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
        Intent intent = new Intent(requireContext(), DetalhesAnuncioActivity.class);
        intent.putExtra("anuncioSelecionado", anuncio);

        startActivity(intent);
    }

}