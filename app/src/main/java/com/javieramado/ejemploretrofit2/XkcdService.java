package com.javieramado.ejemploretrofit2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface XkcdService {
    @GET("{idComic}/info.0.json")
    Call<Comic> getComic(@Path("idComic") int idComic);
}
