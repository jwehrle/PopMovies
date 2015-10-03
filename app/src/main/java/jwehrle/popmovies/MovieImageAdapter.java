package jwehrle.popmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Package: jwehrle.popmovies
 * Class: MovieImageAdapter extends BaseAdapter
 * Author: John Wehrle
 * Date: 9/29/15
 * Purpose: A custom Base Adapter for displaying movie poster images using a List of Movie objects
 * and the Picasso image loader.
 * Disclosure: This app is an implementation of a Udacity course assignment.
 * Members:
 * Context mContext; the parent Activity
 * List<Movie> movieList: The internal List of Movie objects to be adapted.
 */
public class MovieImageAdapter extends BaseAdapter {

    private Context mContext;   //the parent Activity
    private List<Movie> movieList;  //The internal List of Movie objects to be adapted.

    /**
     * MovieImageAdapter()
     * Constructor sets mContext to c and movieList to param.movieList
     * @param c: the parent Activity
     * @param movieList: The List of Movie objects to adapt
     */
    public MovieImageAdapter(Context c, List<Movie> movieList) {
        mContext = c;
        this.movieList = movieList;
    }

    /**
     * getCount()
     * Returns the size of movieList (returns 0 if movieList is null)
     * @return size of movieList.
     */
    @Override
    public int getCount() {
        return (movieList == null) ? 0 : movieList.size();
    }

    /**
     * getItem()
     * Returns the Movie object at position. Returns null if movieList is null.
     * @param position: index of an object in movieList
     * @return the Movie object at position
     */
    @Override
    public Object getItem(int position) {
        return (movieList == null) ? null : movieList.get(position);
    }

    /**
     * getItemId()
     * Returns the movie id of the Movie object at position (returns MOVIE_NOT_FOUND if movieList
     * is null).
     * Member: MOVIE_NOT_FOUND is -1 since this number is our of range for movie ids found in
     * themoviedb.com. This is a value I came up with to signify that movieList is empty.
     * @param position: The index of the movie id to return
     * @return The movie if of the Movie object requested
     */
    @Override
    public long getItemId(int position) {
        final long MOVIE_NOT_FOUND = -1;
        return (movieList == null) ? MOVIE_NOT_FOUND : movieList.get(position).getId();
    }

    /**
     * getView()
     * Loads and returns an ImageView with a movie poster image using Picasso image loader.
     * @param position the index of the Movie object whose poster image will be loaded
     * @param convertView the View to be loaded with the poster image
     * @param parent the ViewGroup that contains the convertView
     * @return an ImageView loaded with the movie's poster image.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if(convertView == null) { imageView = new ImageView(mContext); }
        else { imageView = (ImageView) convertView; }

        imageView.setAdjustViewBounds(true);

        Picasso
                .with(mContext)
                .load(movieList.get(position).getPosterPath())
                .into(imageView);

        return imageView;
    }

    /**
     * clear()
     * Empties movieList and notifies the listening View of this change.
     */
    public void clear() {
        if(movieList != null) {
            movieList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * add()
     * Adds a Movie object to movieList and notifies the listening View of this change. If
     * movieList is null movieList is first initialized.
     * @param movieToAdd the Movie object to add to movieList
     */
    public void add(Movie movieToAdd) {
        if(movieList == null) { movieList = new ArrayList<>(); }
        movieList.add(movieToAdd);
        notifyDataSetChanged();
    }

    /**
     * addAll()
     * Adds a List of Movie objects to movieList and notifies the listening View of this change. If
     * movieList is null movieList is first initialized.
     * @param moviesToAdd the List of Movie objects to be added.
     */
    public void addAll(List<Movie> moviesToAdd) {
        if(movieList == null) { movieList = new ArrayList<>(); }
        movieList.addAll(moviesToAdd);
        notifyDataSetChanged();
    }

}
