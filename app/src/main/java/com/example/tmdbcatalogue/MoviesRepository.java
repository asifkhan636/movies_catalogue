package com.example.tmdbcatalogue;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepository {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static MoviesRepository repository;
    private TmdbApi api;

    private MoviesRepository(TmdbApi api){
        this.api = api;
    }

    public static MoviesRepository getInstance() {
        if(repository == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesRepository(retrofit.create(TmdbApi.class));
        }
        return repository;
    }

    public void getMovies (int pages, String sortBy, final OnGetMoviesCallback callback) {
        Log.d("MoviesRepository","Next Page = "+ pages);

        api.getPopularMovies(BuildConfig.tmdbApi, pages).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if(response.isSuccessful()){
                    MovieResponse movieResponse = response.body();
                    if(movieResponse != null && movieResponse.getMovies() !=null){
                        callback.onSuccess(movieResponse.getPage(), movieResponse.getMovies());
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callback.onError();
            }
        });

//        switch (sortBy) {
//            case TOP_RATED:
//                api.getTopRatedMovies(BuildConfig.tmdbApi, pages).enqueue(call);
//                break;
//            case POPULAR:
//            default:
//                api.getPopularMovies(BuildConfig.tmdbApi, pages).enqueue(call);
//                break;
//        }
    }
}
