package com.exemplo.olxclone.api;

import com.exemplo.olxclone.model.Local;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CEPService {

    /**
     * // Obtém informações no webservice ViaCEP utilizando a url "https://viacep.com.br/ws/<cep>/json"
     * @param cep
     * @return
     */
    @GET("{cep}/json/")
    Call<Local> recuperaCEP(@Path("cep") String cep);

}
