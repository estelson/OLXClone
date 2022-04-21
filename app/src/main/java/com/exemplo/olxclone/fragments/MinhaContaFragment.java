package com.exemplo.olxclone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exemplo.olxclone.R;
import com.exemplo.olxclone.activities.EnderecoActivity;
import com.exemplo.olxclone.activities.PerfilActivity;

public class MinhaContaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_minha_conta, container, false);

        configCliques(view);

        return view;
    }

    private void configCliques(View view) {
        view.findViewById(R.id.menu_perfil).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PerfilActivity.class));
        });

        view.findViewById(R.id.menu_endereco).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), EnderecoActivity.class));
        });
    }

}