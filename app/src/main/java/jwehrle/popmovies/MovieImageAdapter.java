package jwehrle.popmovies;

/**
 * Created by jwehrle on 9/15/15.
 */
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieImageAdapter extends BaseAdapter {

    private final long MOVIE_NOT_FOUND = -1;
    private Context mContext;
    private String [] mURLs;
    private List<String> mUrlList;
    private List<Movie> movieList;

    public MovieImageAdapter(Context c, List<Movie> movieList) {
        mContext = c;
        this.movieList = movieList;
    }

    @Override
    public int getCount() {
        return (movieList == null) ? 0 : movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return (movieList == null) ? null : movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (movieList == null) ? MOVIE_NOT_FOUND : movieList.get(position).getId();
    }

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

    public void clear() {
        if(movieList != null) {
            movieList.clear();
            notifyDataSetChanged();
        }
    }

    public void add(Movie movieToAdd) {
        if(movieList == null) { movieList = new ArrayList<>(); }
        movieList.add(movieToAdd);
        notifyDataSetChanged();
    }

    public void addAll(List<Movie> moviesToAdd) {
        if(movieList == null) { movieList = new ArrayList<>(); }
        movieList.addAll(moviesToAdd);
        notifyDataSetChanged();
    }

}
