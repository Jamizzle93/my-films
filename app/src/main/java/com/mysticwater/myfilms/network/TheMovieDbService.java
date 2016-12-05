package com.mysticwater.myfilms.network;

import com.mysticwater.myfilms.model.Film;
import com.mysticwater.myfilms.model.FilmResults;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbService {

    //EXAMPLE FULL URL: https://api.themoviedb.org/3/movie/550?api_key=0201f736e8e671997dfcc9003c16faac

    @GET("movie/{id}?")
    Call<Film> film(
            @Path("id") String id,
            @Query("api_key") String apiKey);

    @GET("discover/movie?")
    Call<FilmResults> upcomingReleases(
            @Query("api_key") String apiKey,
            @Query("region") String region,
            @Query("primary_release_date.gte") String startDate, //YYYY-MM-DD
            @Query("primary_release_date.lte") String endDate //YYYY-MM-DD
    );

    @GET("movie/now_playing?")
    Call<FilmResults> nowPlaying(
            @Query("api_key") String apiKey,
            @Query("region") String region,
            @Query("primary_release_date") String date
    );

    // Search example
    //https://api.themoviedb.org/3/search/movie?api_key=0201f736e8e671997dfcc9003c16faac&language
    // =en-UK&query=Doctor
    @GET("search/movie?")
    Call<FilmResults> searchFilms(
            @Query("api_key") String apiKey,
            @Query("query") String query
    );


//    @GET("movie/upcoming?")
//    Call<FilmResults> upcomingReleases(
//            @Query("api_key") String apiKey,
//            @Query("primary_release_date") String primaryDate
//    );

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
