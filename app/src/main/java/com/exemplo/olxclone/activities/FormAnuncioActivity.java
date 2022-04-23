package com.exemplo.olxclone.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.exemplo.olxclone.R;
import com.exemplo.olxclone.api.CEPService;
import com.exemplo.olxclone.helper.FirebaseHelper;
import com.exemplo.olxclone.model.Categoria;
import com.exemplo.olxclone.model.Endereco;
import com.exemplo.olxclone.model.Local;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.santalu.maskara.widget.MaskEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormAnuncioActivity extends AppCompatActivity {

    private final int REQUEST_CATEGORIA = 100;

    private ImageView imagem0;
    private ImageView imagem1;
    private ImageView imagem2;

    private EditText edt_titulo;
    private EditText edt_descricao;

    private CurrencyEditText edt_valor;

    private Button btn_categoria;

    private MaskEditText edt_cep;
    private Retrofit retrofit;

    private String categoriaSelecionada = "";

    private ProgressBar progressBar;

    private TextView txt_local;

    private Endereco enderecoUsuario;

    private Local local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio);

        iniciaComponentes();

        iniciaRetrofit();

        recuperaEndereco();

        configCliques();
    }

    public void validaDados(View view) {
        String titulo = edt_titulo.getText().toString().trim();
        double valor = (double) edt_valor.getRawValue() / 100;
        String descricao = edt_descricao.getText().toString().trim();

        if (!titulo.isEmpty()) {
            if (valor > 0) {
                if (!categoriaSelecionada.isEmpty()) {
                    if (!descricao.isEmpty()) {
                        if (local != null) {
                            if (local.getLocalidade() != null) {
                                Toast.makeText(this, "Tudo certo!", Toast.LENGTH_SHORT).show();
                            } else {
                                edt_cep.requestFocus();
                                Toast.makeText(this, "Informe um CEP válido", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            edt_cep.requestFocus();
                            Toast.makeText(this, "Informe um CEP válido", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        edt_descricao.requestFocus();
                        edt_descricao.setError("Informe a descrição");
                    }
                } else {
                    Toast.makeText(this, "Selecione uma categoria", Toast.LENGTH_SHORT).show();
                }
            } else {
                edt_valor.requestFocus();
                edt_valor.setError("Valor inválido");
            }
        } else {
            edt_titulo.requestFocus();
            edt_titulo.setError("Informe o título");
        }
    }

    private void configCliques() {
        imagem0.setOnClickListener(v -> {
            showBottomDialog(0);
        });

        imagem1.setOnClickListener(v -> {
            showBottomDialog(1);
        });

        imagem2.setOnClickListener(v -> {
            showBottomDialog(2);
        });
    }

    public void selecionarCategoria(View view) {
        Intent intent = new Intent(this, CategoriasActivity.class);
        startActivityForResult(intent, REQUEST_CATEGORIA);
    }

    private void recuperaEndereco() {
        configCep();

        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(FirebaseHelper.getUidFirebase());

        enderecoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                enderecoUsuario = snapshot.getValue(Endereco.class);
                edt_cep.setText(enderecoUsuario.getCep());

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showBottomDialog(int requestCode) {
        View modalBottomSheet = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(modalBottomSheet);
        bottomSheetDialog.show();

        modalBottomSheet.findViewById(R.id.btn_camera).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            verificaPermissaoCamera(requestCode);
        });

        modalBottomSheet.findViewById(R.id.btn_galeria).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            verificaPermissaoGaleria(requestCode);
        });

        modalBottomSheet.findViewById(R.id.btn_close).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Toast.makeText(this, "Fechando", Toast.LENGTH_SHORT).show();
        });
    }

    private void verificaPermissaoCamera(int requestCode) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirCamera(requestCode);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormAnuncioActivity.this, "Permissão negada", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissao(
                permissionListener,
                new String[]{Manifest.permission.CAMERA},
                "Permissão de acesso à câmera do dispositivo negada. Deseja ativar agora?"
        );
    }

    private void verificaPermissaoGaleria(int requestCode) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria(requestCode);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormAnuncioActivity.this, "Permissão negada", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissao(
                permissionListener,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                "Permissão de acesso à galeria do dispositivo negada. Deseja ativar agora?"
        );
    }

    private void abrirCamera(int requestCode) {

    }

    private void abrirGaleria(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, requestCode);
    }

    private void showDialogPermissao(PermissionListener permissionListener, String[] permissoes, String message) {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedTitle("Permissão negada")
                .setDeniedMessage(message)
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permissoes)
                .check();
    }

    private void configCep() {
        edt_cep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            /**
             * Faz a consulta de CEP no webservice ViaCEP (https://viacep.com.br/) e recupera o
             * endereço a partir do CEP do cadastro do usuário.
             * @param charSequence
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String cep = charSequence.toString().replaceAll("_", "").replace("-", "");
                if (cep.length() == 8) {
                    buscarEndereco(cep);
                } else {
                    local = null;
                    configEndereco();

                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void buscarEndereco(String cep) {
        progressBar.setVisibility(View.VISIBLE);

        CEPService cepService = retrofit.create(CEPService.class);
        Call<Local> call = cepService.recuperaCEP(cep);

        call.enqueue(new Callback<Local>() {
            @Override
            public void onResponse(Call<Local> call, Response<Local> response) {
                if (response.isSuccessful()) {
                    local = response.body();
                    if (local.getLocalidade() == null) {
                        Toast.makeText(FormAnuncioActivity.this, "CEP inválido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FormAnuncioActivity.this, "Serviço de busca de CEP indisponível.\nTente novamente mais tarde.", Toast.LENGTH_LONG).show();
                }

                configEndereco();
            }

            @Override
            public void onFailure(Call<Local> call, Throwable t) {
                Toast.makeText(FormAnuncioActivity.this, "Serviço de busca de CEP indisponível.\nTente novamente mais tarde.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void configEndereco() {
        if (local != null) {
            if (local.getLocalidade() != null) {
                String endereco = local.getLocalidade() + ", " + local.getBairro() + " - DDD  " + local.getDdd();
                txt_local.setText(endereco);
            } else {
                txt_local.setText("");
            }
        } else {
            txt_local.setText("");
        }

        progressBar.setVisibility(View.GONE);
    }

    private void iniciaRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Novo anúncio");

        imagem0 = findViewById(R.id.imagem0);
        imagem1 = findViewById(R.id.imagem1);
        imagem2 = findViewById(R.id.imagem2);

        edt_titulo = findViewById(R.id.edt_titulo);
        edt_descricao = findViewById(R.id.edt_descricao);

        edt_valor = findViewById(R.id.edit_valor);
        edt_valor.setLocale(new Locale("PT", "br"));

        edt_cep = findViewById(R.id.edt_cep);

        btn_categoria = findViewById(R.id.btn_categoria);

        progressBar = findViewById(R.id.progressBar);

        txt_local = findViewById(R.id.txt_local);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap bitmap0;
            Bitmap bitmap1;
            Bitmap bitmap2;

            Uri imagemSelecionada = data.getData();
            String caminhoImagem;

            if (requestCode == REQUEST_CATEGORIA) {
                Categoria categoria = (Categoria) data.getSerializableExtra("categoriaSelecionada");
                categoriaSelecionada = categoria.getNome();

                btn_categoria.setText(categoriaSelecionada);
            } else if (requestCode <= 2) { // Controla a seleção de imagens da galeria do dispositivo
                caminhoImagem = imagemSelecionada.toString();

                try {
                    switch (requestCode) {
                        case 0:
                            if (Build.VERSION.SDK_INT < 28) {
                                bitmap0 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                            } else {
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);

                                bitmap0 = ImageDecoder.decodeBitmap(source);
                            }

                            imagem0.setImageBitmap(bitmap0);

                            break;
                        case 1:
                            if (Build.VERSION.SDK_INT < 28) {
                                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                            } else {
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);

                                bitmap1 = ImageDecoder.decodeBitmap(source);
                            }

                            imagem1.setImageBitmap(bitmap1);

                            break;
                        case 2:
                            if (Build.VERSION.SDK_INT < 28) {
                                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                            } else {
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemSelecionada);

                                bitmap2 = ImageDecoder.decodeBitmap(source);
                            }

                            imagem2.setImageBitmap(bitmap2);

                            break;
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "Erro ao carregar a imagem selecionada. Motivo: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else { // Controla a seleção de imagens da câmera do dispositivo

            }
        }
    }
}