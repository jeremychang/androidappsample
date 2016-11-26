package com.example.jeremy.appsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
 
import java.util.List;
 
import com.example.jeremy.appsample.R;
import com.example.jeremy.appsample.adapter.MoviesAdapter;
import com.example.jeremy.appsample.model.Movie;
import com.example.jeremy.appsample.model.MoviesResponse;
import com.example.jeremy.appsample.rest.ApiClient;
import com.example.jeremy.appsample.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
 
public class RetrofitDemoActivity extends AppCompatActivity {
 
    private static final String TAG = RetrofitDemoActivity.class.getSimpleName();
 
 
    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "5dd7ee78cc957682650044bc08c6ece5";
 
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_demo);
 
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
            return;
        }
 
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
 
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
 
        Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                List<Movie> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext()));
            }
 
            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}

/*
public class RetrofitDemoActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
 
    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }
 
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
 
        Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {
                List<Movie> movies = response.body().getResults();
                Log.d(TAG, "Number of movies received: " + movies.size());
            }
 
            @Override
            public void onFailure(Call<MoviesResponse>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}
*/
