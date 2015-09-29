package jwehrle.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

/**
 * Package: jwehrle.popmovies
 * Class: MainActivity
 * Author: John Wehrle
 * Date: 9/29/15
 * Purpose: Driver for a movie app that uses The Movie Data Base API with a student API key. This
 * app displays either the 20 most popular movies or the 20 highest rated movies (as determined by
 * themoviedb.com) Users can set their sorting preference for popularity or rating and selecting a
 * movie image takes the user to a movie detail screen.
 * Much attention was given toward achieving an aesthetic user experience.
 * Disclosure: This app is an implementation of a Udacity course assignment.
 * Members:
 * List<Movie> mMovies: A list of movie objects (initially null) passed to the adapter.
 * GridView mGridView: A GridView to hold the movie images retrieved from FetchMoviesTask.
 * MovieImageAdapter mMovieImageAdapter: A custom Base Adapter to adapt Movie objects to GridViews.
 */
public class MainActivity extends ActionBarActivity {

    List<Movie> mMovies;    //A list of movie objects (initially null) passed to the adapter.
    GridView mGridView;     //A GridView to hold the movie images retrieved from FetchMoviesTask.
    //A custom Base Adapter to adapt Movie objects to GridViews.
    MovieImageAdapter mMovieImageAdapter;

    /**
     * Sets container layout to grid_layout and fills it with mGridView. Then mMovieImageAdapter is
     * assigned to mMovies (which is null).
     * Calls fetchMovies() to populate the Movie ArrayList in mMovieImageAdapter which updates
     * mGridView with movie poster images.
     * Finally, we set a clickListener on mGridView and handle that event with an Intent. We
     * pass a Movie object along with that intent to a new MovieDetailActivity and start it.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_layout);
        mGridView = (GridView) findViewById(R.id.grid_view);
        mMovieImageAdapter = new MovieImageAdapter(this, mMovies);
        fetchMovies();
        mGridView.setAdapter(mMovieImageAdapter);
        final Intent intent = new Intent(this,MovieDetailActivity.class);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                intent.putExtra("selected", (Movie)mGridView.getAdapter().getItem(position));
                startActivity(intent);
            }
        });
    }

    /**
     * Inflate main_menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * If the user selects "settings" we use an intent to display a SettingsActivity.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fetch fresh movie images to display in case the user preferences have changed or the
     * themoviedb.com has changed its listings.
     */
    @Override
    protected void onResume() {
        mMovieImageAdapter.clear();
        fetchMovies();
        super.onResume();
    }

    /**
     * Constructs a new FetchMoviesTask AsyncTask with our mMovieImageAdapter and then executes
     * that task with the base url, user sort by preference, and my student api key.
     */
    private void fetchMovies() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(this, mMovieImageAdapter);
        fetchMoviesTask.execute(
                getString(R.string.theMovieDbBaseURL),
                preferences.getString(
                        getString(R.string.pref_sort_key), getString(R.string.default_sort)),
                getString(R.string.themoviedb_student_api_key));
    }
}
