package jwehrle.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jwehrle on 9/16/15.
 */
public class Movie implements Parcelable {

    private final String IMAGE_URL_BASE = "http://image.tmdb.org/t/p/w185/";
    private boolean favorite;
    private boolean adult;
    private String backdropPath;
    private List<Integer> genreIds;
    private int id;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private int popularity;
    private String title;
    private boolean video;
    private int voteAverage;
    private int voteCount;
    //private List<String> reviews;

    Movie(String movieJSONString) {
        try {
            genreIds = new ArrayList<>();
            //reviews = new ArrayList<>();
            parseJSONString(movieJSONString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Movie (Movie orig) {
        favorite = orig.favorite;
        adult = orig.adult;
        backdropPath = orig.backdropPath;
        genreIds = orig.genreIds;
        id = orig.id;
        originalLanguage = orig.originalLanguage;
        originalTitle = orig.originalTitle;
        overview = orig.overview;
        releaseDate = orig.releaseDate;
        posterPath = orig.posterPath;
        popularity = orig.popularity;
        title = orig.title;
        video = orig.video;
        voteAverage = orig.voteAverage;
        voteCount = orig.voteCount;
    }

    private void parseJSONString(String JSONString) throws JSONException {
        favorite = false;
        JSONObject movie = new JSONObject(JSONString);
        adult = movie.getBoolean("adult");
        backdropPath = movie.getString("backdrop_path");
        JSONArray genres = movie.getJSONArray("genre_ids");
        for(int i = 0; i < genres.length(); i++) {
            genreIds.add(genres.getInt(i));
        }
        id = movie.getInt("id");
        originalLanguage = movie.getString("original_language");
        originalTitle = movie.getString("original_title");
        overview = movie.getString("overview");
        releaseDate = movie.getString("release_date");
        posterPath = IMAGE_URL_BASE + movie.getString("poster_path");
        popularity = movie.getInt("popularity");
        title = movie.getString("title");
        video = movie.getBoolean("video");
        voteAverage = movie.getInt("vote_average");
        voteCount = movie.getInt("vote_count");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(backdropPath);
        dest.writeList(genreIds);
        dest.writeInt(id);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeInt(popularity);
        dest.writeString(title);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeInt(voteAverage);
        dest.writeInt(voteCount);
    }

    public static final Parcelable.Creator<Movie> CREATOR =
            new Parcelable.Creator<Movie>() {

                public Movie createFromParcel(Parcel in) {
                  return new Movie(in);
                }
                public Movie[] newArray(int size) {
                    return new Movie[size];
                }
            };

    private Movie(Parcel in) {
        favorite = in.readByte() != 0;
        adult = in.readByte() != 0;
        backdropPath = in.readString();
        genreIds = new ArrayList<>();
        in.readList(genreIds, Integer.class.getClassLoader());
        id = in.readInt();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        popularity = in.readInt();
        title = in.readString();
        video = in.readByte() != 0;
        voteAverage = in.readInt();
        voteCount = in.readInt();
    }

//    public void addReviewURL(String reviewURL) {
//        reviews.add(reviewURL);
//    }

    public boolean isFavorite() {
        return favorite;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public int getId() {
        return id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getTitle() {
        return title;
    }

    public boolean isVideo() {
        return video;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }
}
