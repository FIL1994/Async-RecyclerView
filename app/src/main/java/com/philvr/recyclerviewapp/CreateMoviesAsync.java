package com.philvr.recyclerviewapp;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip Van Raalte
 * @date 2017-06-27
 */

                                                //input type, progress type, return type
public class CreateMoviesAsync extends AsyncTask<Integer, Integer, ArrayList<Movie>> {
    private static ArrayList<Movie> movies;
    private MoviesAdapter moviesAdapter;
    private RecyclerView recyclerView;
    private int updateFrequency = 5;
    private Boolean hasNotified = false;

    public CreateMoviesAsync(List<Movie> movies, MoviesAdapter moviesAdapter,
                             RecyclerView recyclerView){
        this.movies = new ArrayList<>(movies);
        this.moviesAdapter = moviesAdapter;
        this.recyclerView = recyclerView;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Integer... integers) {
        final int moviesToGenerate = integers[0];

        Movie movie;
        for(int i = 0; i < moviesToGenerate; i++){
            movie = new Movie("Movie " + i, i % 2 == 0 ? "Action" : "Comedy",
                    Integer.toString(2005 + i));
            movies.add(movie);
            int progress = (int) Math.floor(((double) i/ (double) moviesToGenerate) * 100.0);
            publishProgress(progress);
            //simulate method taking awhile to complete
            SystemClock.sleep(120);
        }

        return movies;
    }

    @Override
    protected void onPreExecute() {
        //precondition for task
        moviesAdapter = new MoviesAdapter(movies);
        recyclerView.setAdapter(moviesAdapter);
    }

    @Override
    protected void onProgressUpdate(Integer... values){
        super.onProgressUpdate(values);

        if(values[0] % updateFrequency == 0){
            if(!hasNotified) {
                moviesAdapter.notifyDataSetChanged();
                movies.trimToSize();
                Log.i("MoviesProgress", Integer.toString(values[0]) + "% completed");
                hasNotified = true;
            }
        }
        else{
            hasNotified = false;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies){
        super.onPostExecute(movies);
        this.movies = movies;
        moviesAdapter.notifyDataSetChanged();
        Log.i("CreateMoves", "Completed");
    }
}
