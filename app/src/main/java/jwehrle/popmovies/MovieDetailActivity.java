package jwehrle.popmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by jwehrle on 9/24/15.
 */
public class MovieDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieDetailFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public static class MovieDetailFragment extends android.support.v4.app.Fragment {

        Movie selected;

        public MovieDetailFragment () { setHasOptionsMenu(true); }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {

            //View rootview = inflater.inflate(R.layout.fragment_detail, container, false);
            View rootview = inflater.inflate(R.layout.movie_detail_layout, container, false);

            Bundle details = getActivity().getIntent().getExtras();
            selected = details.getParcelable("selected");

            //Set Title
            ((TextView)rootview.findViewById(R.id.movie_title_textview))//R.id.detail_text
                    .setText(selected.getOriginalTitle());

            //Set Thumbnail
            Picasso
                    .with(getActivity())
                    .load(selected.getPosterPath())
                    .into((ImageView) rootview.findViewById(R.id.movie_thumbnail_imageview));//thumbnail

            //Set Release Date
            ((TextView)rootview.findViewById(R.id.release_year_textview))
                    .setText(selected.getReleaseDate().substring(0, 3));

            //Set Movie Length
            ((TextView)rootview.findViewById(R.id.movie_rating_textview))
                    .setText(String.valueOf(selected.getVoteAverage()) + "/10");

            //Set Synopsis
            ((TextView)rootview.findViewById(R.id.movie_synopsis_textview))
                    .setText(selected.getOverview());

            return rootview;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
        }
    }
}
