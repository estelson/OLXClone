package com.exemplo.olxclone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exemplo.olxclone.R;
import com.exemplo.olxclone.activities.EnderecoActivity;
import com.exemplo.olxclone.activities.MainActivity;
import com.exemplo.olxclone.activities.PerfilActivity;
import com.exemplo.olxclone.autenticacao.LoginActivity;
import com.exemplo.olxclone.helper.FirebaseHelper;

public class MinhaContaFragment extends Fragment {

    private TextView text_conta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_minha_conta, container, false);

        iniciaComponentes(view);

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

        if(FirebaseHelper.getAutenticado()) {
            text_conta.setText("Sair");
        } else {
            text_conta.setText("Clique aqui");
        }

        text_conta.setOnClickListener(v -> {
            if(FirebaseHelper.getAutenticado()) {
                FirebaseHelper.getAuth().signOut();

                startActivity(new Intent(getActivity(), MainActivity.class));
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }

    private void iniciaComponentes(View view) {
        text_conta = view.findViewById(R.id.text_conta);
    }

}