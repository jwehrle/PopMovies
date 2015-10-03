package jwehrle.popmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Package: jwehrle.popmovies
 * Class: MovieDetailActivity extends ActionBarActivity
 * Author: John Wehrle
 * Date: 9/29/15
 * Purpose: Displays details about the selected movie such as original title, release year and
 * rating thumbnail poster image and movie synopsis. In the next installment links to reviews and
 * trailers will be added.
 * Disclosure: This app is an implementation of a Udacity course assignment.
 */
public class MovieDetailActivity extends ActionBarActivity {

    /**
     * Sets container layout to activity detail and commits MovieDetailFragment.
     * @param savedInstanceState The state of this Activity at Creation.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieDetailFragment()).commit();
        }
    }

    /**
     * Package: jwehrle.popmovies
     * Class: MovieDetailFragment (Inner Fragment)
     * Author: John Wehrle
     * Date: 9/29/15
     * Purpose: Display member values of the selected Movie object. In the next installment the
     * favorite button will set a boolean in the selected Movie object and links to reviews and
     * trailers will be displayed below.
     * Member:
     *  Movie selected: The Movie object to be detailed.
     */
    public static class MovieDetailFragment extends android.support.v4.app.Fragment {

        private final int DIGITS_IN_YEAR = 4; //The number of digits in a year (1985 has 4 digits)
        private final String DIVIDED_BY_TEN = "/10"; //Every rating is out of 10.
        Movie selected; //The Movie object to be detailed.

        /**
         * Default constructor, inflates menu.
         */
        public MovieDetailFragment () { }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {

            //Set layout
            View rootview = inflater.inflate(R.layout.movie_detail_layout, container, false);

            //Get Movie
            Bundle details = getActivity().getIntent().getExtras();
            selected = details.getParcelable("selected");

            //Set Title
            ((TextView)rootview.findViewById(R.id.movie_title_textview))
                    .setText(selected.getOriginalTitle());

            //Set Thumbnail
            Picasso
                    .with(getActivity())
                    .load(selected.getPosterPath())
                    .into((ImageView) rootview.findViewById(R.id.movie_thumbnail_imageview));

            //Set Release Date
            String release =
                    getString(R.string.released) + "  " +
                            selected.getReleaseDate().substring(0, DIGITS_IN_YEAR);
            ((TextView)rootview.findViewById(R.id.release_year_textview))
                    .setText(release);

            //Set Movie Rating
            String rating = getString(R.string.rating) + "  " +
                    String.valueOf(selected.getVoteAverage()) + getString(R.string.divided_by_ten);
            ((TextView)rootview.findViewById(R.id.movie_rating_textview))
                    .setText(rating);

            //Set Synopsis
            ((TextView)rootview.findViewById(R.id.movie_synopsis_textview))
                    .setText(selected.getOverview());

            return rootview;
        }
    }
}
