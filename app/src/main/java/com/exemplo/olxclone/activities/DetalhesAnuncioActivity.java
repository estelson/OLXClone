package com.exemplo.olxclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.exemplo.olxclone.R;
import com.exemplo.olxclone.adapter.SliderAdapter;
import com.exemplo.olxclone.autenticacao.LoginActivity;
import com.exemplo.olxclone.helper.FirebaseHelper;
import com.exemplo.olxclone.helper.GetMask;
import com.exemplo.olxclone.model.Anuncio;
import com.exemplo.olxclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class DetalhesAnuncioActivity extends AppCompatActivity {

    private ImageButton ib_voltar;

    private TextView text_toolbar;

    private ImageButton ib_favorito;
    private ImageButton ib_ligar;

    private SliderView sliderView;

    private TextView text_titulo_anuncio;
    private TextView text_valor_anuncio;
    private TextView text_data_publicacao;

    private TextView text_descricao_anuncio;

    private TextView text_categoria_anuncio;

    private TextView text_cep_anuncio;
    private TextView text_municipio_anuncio;
    private TextView text_bairro;

    private Anuncio anuncio;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_anuncio);

        iniciaComponentes();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            anuncio = (Anuncio) bundle.getSerializable("anuncioSelecionado");

            configDados();

            recuperaUsuario();
        }

        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> {
            finish();
        });

        findViewById(R.id.ib_ligar).setOnClickListener(v -> {
            ligarAnunciante();
        });
    }

    private void ligarAnunciante() {
        if(FirebaseHelper.getAutenticado()) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", usuario.getTelefone(), null));

            startActivity(intent);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void recuperaUsuario() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(anuncio.getIdUsuario());

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados() {
        sliderView.setSliderAdapter(new SliderAdapter(anuncio.getUrlImagens()));
        sliderView.startAutoCycle();
        sliderView.setScrollTimeInSec(4);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

        text_titulo_anuncio.setText(anuncio.getTitulo());
        text_valor_anuncio.setText(getString(R.string.valor_anuncio, GetMask.getValor(anuncio.getValor())));
        text_data_publicacao.setText(getString(R.string.data_publicacao, GetMask.getDate(anuncio.getDataPublicacao(), 3)));

        text_descricao_anuncio.setText(anuncio.getDescricao());
        text_categoria_anuncio.setText(anuncio.getCategoria());

        text_cep_anuncio.setText(anuncio.getLocal().getCep());
        text_municipio_anuncio.setText(anuncio.getLocal().getLocalidade());
        text_bairro.setText(anuncio.getLocal().getBairro());
    }

    private void iniciaComponentes() {
        ib_voltar = findViewById(R.id.ib_voltar);

        text_toolbar = findViewById(R.id.text_toolbar);

        ib_favorito = findViewById(R.id.ib_favorito);
        ib_ligar = findViewById(R.id.ib_ligar);

        sliderView = findViewById(R.id.sliderView);

        text_titulo_anuncio = findViewById(R.id.text_titulo_anuncio);
        text_valor_anuncio = findViewById(R.id.text_valor_anuncio);
        text_data_publicacao = findViewById(R.id.text_data_publicacao);

        text_descricao_anuncio = findViewById(R.id.text_descricao_anuncio);
        text_categoria_anuncio = findViewById(R.id.text_categoria_anuncio);

        text_cep_anuncio = findViewById(R.id.text_cep_anuncio);
        text_municipio_anuncio = findViewById(R.id.text_municipio_anuncio);
        text_bairro = findViewById(R.id.text_bairro);
    }

}