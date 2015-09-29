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
 * Created by jwehrle on 9/16/15.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>>{

    private String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private final String JSON_ARRAY_KEY = "results";
    Context context;
    MovieImageAdapter movieImageAdapter;

    FetchMoviesTask(Context context, MovieImageAdapter movieImageAdapter){
        this.context = context;
        this.movieImageAdapter = movieImageAdapter;
    }

    private List<Movie> getMovies(String moviesJSONString) throws JSONException {
        JSONObject popularMovies = new JSONObject(moviesJSONString);
        JSONArray posterArray = popularMovies.getJSONArray(JSON_ARRAY_KEY);
        List<Movie> movies = new ArrayList<>();
        JSONObject movieJSON;
        Movie movie;
        for(int i = 0; i < posterArray.length(); i++) {
            movieJSON = posterArray.getJSONObject(i);
            movie = new Movie(movieJSON.toString());
            movies.add(movie);
        }
        return movies;
    }

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
        while ((line = reader.readLine()) != null) { buffer.append(line + "\n"); }
        if (buffer.length() == 0) { return null; }
        return buffer.toString();
    }

    /**
     *
     * @param params : Array of 3 elements {base url, sorting preference, api key}
     * @return
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
            return getMovies(moviesJSONString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        movieImageAdapter.clear();
        movieImageAdapter.addAll(movies);
    }
}
