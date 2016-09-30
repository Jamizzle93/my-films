package com.mysticwater.myfilms.network;

import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.model.FilmResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbService {

    //EXAMPLE FULL URL: https://api.themoviedb.org/3/movie/550?api_key=0201f736e8e671997dfcc9003c16faac


    @GET("movie/{id}?")
    Call<Film> film(
            @Path("id") String id,
            @Query("api_key") String apiKey);

    @GET("discover/movie?primary_release_date.gte=2014-09-15&primary_release_date.lte=2014-10-22")
    Call<FilmResults> upcomingReleases(
            @Query("api_key") String apiKey,
            @Query("primary_release_date.gte") String startDate, //YYYY-MM-DD
            @Query("primary_release_date.lte") String endDate //YYYY-MM-DD
    );

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

}
