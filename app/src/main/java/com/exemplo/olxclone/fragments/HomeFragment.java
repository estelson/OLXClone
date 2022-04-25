package com.exemplo.olxclone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exemplo.olxclone.R;
import com.exemplo.olxclone.activities.CategoriasActivity;
import com.exemplo.olxclone.activities.FormAnuncioActivity;
import com.exemplo.olxclone.adapter.AnuncioAdapter;
import com.exemplo.olxclone.autenticacao.LoginActivity;
import com.exemplo.olxclone.helper.FirebaseHelper;
import com.exemplo.olxclone.model.Anuncio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements AnuncioAdapter.OnClickListener {

    private Button btn_regioes;
    private Button btn_categorias;
    private Button btn_filtros;

    private AnuncioAdapter anuncioAdapter;
    private List<Anuncio> anuncioList = new ArrayList<>();

    private ProgressBar progressBar;
    private TextView text_info;

    private RecyclerView rv_anuncios;

    private Button btn_novo_anuncio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        iniciaComponentes(view);

        configRV();

        configCliques();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        recuperaAnuncios();
    }

    private void recuperaAnuncios() {
        DatabaseReference anunciosRef = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos");

        anunciosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    anuncioList.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
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
    }

    private void configRV() {
        rv_anuncios.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_anuncios.setHasFixedSize(true);

        anuncioAdapter = new AnuncioAdapter(anuncioList, this);
        rv_anuncios.setAdapter(anuncioAdapter);
    }

    private void configCliques() {
        btn_novo_anuncio.setOnClickListener(v -> {
            if (FirebaseHelper.getAutenticado()) {
                startActivity(new Intent(getActivity(), FormAnuncioActivity.class));
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        btn_categorias.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), CategoriasActivity.class);
            intent.putExtra("todas", true);

            startActivity(intent);
        });
    }

    private void iniciaComponentes(View view) {
        btn_regioes = view.findViewById(R.id.btn_regioes);
        btn_categorias = view.findViewById(R.id.btn_categorias);
        btn_filtros = view.findViewById(R.id.btn_filtros);

        text_info = view.findViewById(R.id.text_info);
        rv_anuncios = view.findViewById(R.id.rv_anuncios);

        progressBar = view.findViewById(R.id.progressBar);

        btn_novo_anuncio = view.findViewById(R.id.btn_novo_anuncio);
    }

    @Override
    public void OnClick(Anuncio anuncio) {
        Toast.makeText(requireContext(), anuncio.getTitulo(), Toast.LENGTH_LONG).show();
    }
}