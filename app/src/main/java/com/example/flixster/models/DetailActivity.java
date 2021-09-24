package com.example.flixster.models;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.BuildConfig;
import com.example.flixster.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {


    public static final String MY_YOUTUBE_API_KEY = BuildConfig.ApiKey;
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    TextView descriptionTV;
    TextView releaseDateTV;
    TextView adultMovieTV;
    RatingBar ratingBar;
    TextView ratingText;
    TextView titleTV;
    YouTubePlayerView youTubePlayerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        descriptionTV = findViewById(R.id.descriptionTV);
        releaseDateTV = findViewById(R.id.releaseDateTV);
        adultMovieTV = findViewById(R.id.adultMovieTV);
        ratingBar = findViewById(R.id.ratingBar);
        ratingText = findViewById(R.id.ratingText);
        youTubePlayerView = findViewById(R.id.player);
        titleTV = findViewById(R.id.titleTV);

        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movies"));

        float ratingNumber = (float) movie.getRating();
        String description = movie.getOverview();
        String releaseDate = "Release Date: " + movie.getReleaseDate();
        String adultMovie;
        String ratingTextValue = String.valueOf(movie.getRating());
        if (movie.getAdultMovie()) {
            adultMovie = "Adult Movie: Yes";
        } else {
            adultMovie = "Adult Movie: No";
        }
        String title = movie.getTitle();

        ratingBar.setRating(ratingNumber);
        descriptionTV.setText(description);
        releaseDateTV.setText(releaseDate);
        adultMovieTV.setText(adultMovie);
        ratingText.setText(ratingTextValue);
        titleTV.setText(title);


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movie.getMovieID()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if(results.length() == 0){
                        return;
                    }
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    Log.d("DetailActivity", youtubeKey);
                    initializeYoutube(youtubeKey, (float) movie.getRating());
                } catch (JSONException e) {
                    Log.e("DetailActivity", "Failed to parse JSON", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {

            }
        });
    }

    private void initializeYoutube(final String youtubeKey, float movieRating) {
        youTubePlayerView.initialize(MY_YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("DetailActivity", "onInitializationSuccess");
                if (movieRating > 5.0) {
                    youTubePlayer.loadVideo(youtubeKey);
                }
                else{
                    youTubePlayer.cueVideo(youtubeKey);
                }
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("DetailActivity", "onInitializationFailure");
            }
        });
    }
}