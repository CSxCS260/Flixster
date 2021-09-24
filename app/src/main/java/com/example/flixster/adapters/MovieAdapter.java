package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.R;
import com.example.flixster.models.DetailActivity;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import kotlin.sequences.ConstrainedOnceSequence;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }


    //Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    //Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder"+position);
        //Get the movie at the passed in position
        Movie movie = movies.get(position);
        //Bind the movie data in the VH
        holder.bind(movie);
    }

    //Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTV;
        TextView descriptionTV;
        ImageView posterIV;
        ConstraintLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.titleTV);
            descriptionTV = itemView.findViewById(R.id.descriptionTV);
            posterIV = itemView.findViewById(R.id.posterIV);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(Movie movie) {
            titleTV.setText(movie.getTitle());
            int titleLines = (movie.getTitle().length()/17) + 1;
            int descriptionLines = 12 - (2*titleLines);
            descriptionTV.setMaxLines(descriptionLines);
            descriptionTV.setText(movie.getOverview());
            String imageUrl;
            //If phone is in landscape, the imageUrl = backdrop
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
            }
            //If phone is in portrait, the imageUrl = poster image
            else {
                imageUrl = movie.getPosterPath();
            }

            int radius = 10;
            int margin = 10;
            int height = posterIV.getHeight();
            Glide.with(context)
                    .load(imageUrl)
                    .fitCenter()
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(posterIV);
            posterIV.setMaxHeight(height);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("movies", Parcels.wrap(movie));
                    context.startActivity(i);
                }
            });
        }
    }
}
