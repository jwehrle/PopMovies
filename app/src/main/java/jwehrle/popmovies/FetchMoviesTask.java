package jwehrle.popmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Package: jwehrle.popmovies
 * Class: FetchMoviesTask extends AsyncTas
 * Author: John Wehrle
 * Date: 9/29/15
 * Purpose: This AsyncTask retrieves a JSON String from www.themoviedb.com, parses that String into
 * an ArrayList of Movie objects, and, resets its MovieImageAdapter to these new Movie objects - all
 * of which has the result of filling the MainActivity UI GridView with the downloaded movies and
 * their poster images.
 * Disclosure: This app is an implementation of a Udacity course assignment.
 * Members:
 * String LOG_TAG: for logging errors.
 * Context context: a reference to the parent Activity.
 * MovieImageAdapter movieImageAdapter: A custom Base Adapter to adapt Movie objects to GridViews.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>>{

    private String LOG_TAG = FetchMoviesTask.class.getSimpleName(); //Aids error tracking
    Context context;    //a reference to the parent Activity
    MovieImageAdapter movieImageAdapter;    //custom Base Adapter for Movie objects

    /**
     * FetchMoviesTask()
     * Constructor sets Context and MovieImageAdapter
     * @param context   //the Context of the parent Activity
     * @param movieImageAdapter //the MovieImageAdapter to be filled
     */
    FetchMoviesTask(Context context, MovieImageAdapter movieImageAdapter){
        this.context = context;
        this.movieImageAdapter = movieImageAdapter;
    }

    /**
     * getMovieListFromJSON()
     * Parses JSON String into a List of Movie objects.
     * @param moviesJSONString: The JSON String containing all of the movie information
     * @return moviesList: The List of Movie objects
     * @throws JSONException: JSON parsing may fail
     */
    private List<Movie> getMovieListFromJSON(String moviesJSONString) throws JSONException {
        final String JSON_ARRAY_KEY = "results";    //key for JSON Array of movies
        JSONObject popularMovies = new JSONObject(moviesJSONString);
        JSONArray posterArray = popularMovies.getJSONArray(JSON_ARRAY_KEY);
        List<Movie> moviesList = new ArrayList<>();
        JSONObject movieJSON;
        Movie movie;
        for(int i = 0; i < posterArray.length(); i++) {
            movieJSON = posterArray.getJSONObject(i);
            movie = new Movie(movieJSON.toString());
            moviesList.add(movie);
        }
        return moviesList;
    }

    /**
     * fetchJSONString()
     * Downloads and returns the JSON String of movie information.
     * @param urlString:    The url source
     * @param urlConnection:    The HttpURLConnection used to download the JSON String
     * @param reader:   The BufferReader used to read the downloaded content
     * @return StringBuffer.toString(): The downloaded JSON String
     * @throws IOException: The HttpURLConnection may fail.
     */
    private String fetchJSONString(String urlString, HttpURLConnection urlConnection,
                                   BufferedReader reader ) throws IOException {
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        InputStream inputStream = urlConnection.getInputStream();
        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) { return null; }
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) { buffer.append(line).append("\n"); }
        if (buffer.length() == 0) { return null; }
        return buffer.toString();
    }

    /**
     * doInBackground()
     * Downloads and parses a JSON String into a List of Movie objects based on the params passed.
     * Calls:
     *  fetchJSONString()
     *  getMovieListFromJSON()
     * @param params : Array of 3 elements {base url, sorting preference, api key}
     * @return a List of Movie objects
     */
    @Override
    protected List<Movie> doInBackground(String... params) {
        if (params.length != 3) { return null; }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJSONString = null;
        try{
            String urlString = params[0] + "?sort_by=" + params[1] + "&api_key=" + params[2];
            moviesJSONString = fetchJSONString(urlString, urlConnection, reader);
        }catch (IOException e){
            Log.e(LOG_TAG, "Error fetching movie data", e);
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return getMovieListFromJSON(moviesJSONString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * onPostExecute()
     * Resets FetchMoviesTask.movieImageAdapter with the new List of Movie objects.
     * @param movieList:    The new List of Movie objects
     */
    @Override
    protected void onPostExecute(List<Movie> movieList) {
        movieImageAdapter.clear();
        movieImageAdapter.addAll(movieList);
    }
}
