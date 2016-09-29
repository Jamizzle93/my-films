package com.mysticwater.myfilms.network;

import com.mysticwater.myfilms.model.Film;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbService {

    @GET("movie/{id}?")
    Call<Film> film(
            @Path("id") String id,
            @Query("api_key") String apiKey);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

}
