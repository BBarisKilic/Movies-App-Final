package com.example.baris.moviesappfinal.Utils;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.baris.moviesappfinal.Movie.Movie;
import com.example.baris.moviesappfinal.Models.Review;
import com.example.baris.moviesappfinal.Models.Video;
import java.util.ArrayList;

public class JSONParser {

    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final String POSTER_PATH = "poster_path";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String TAG = JSONParser.class.getSimpleName();
    private static final String VIDEO_NAME = "name";
    private static final String VIDEO_KEY = "key";
    private static final String REVIEW_ID = "id";
    private static final String REVIEW_URL = "url";
    private static final String REVIEW_WRITER = "writer";
    private static final String REVIEW_CONTENT = "content";

    private JSONParser() {
    }
    public static ArrayList<Video> parseVidJson(String json) {
        ArrayList<Video> videos = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(json);
            if (rootObject.has(RESULTS)) {
                JSONArray videoArray = rootObject.getJSONArray(RESULTS);
                for (int i = 0; i < videoArray.length(); i++) {
                    JSONObject currentVideo = videoArray.getJSONObject(i);
                    String videoName = currentVideo.optString(VIDEO_NAME);
                    String videoKey = currentVideo.optString(VIDEO_KEY);
                    Video video = new Video(videoName, videoKey);
                    videos.add(video);
                }
            }
        } catch (JSONException je) {
            je.printStackTrace();
            Log.e(TAG, "parseJSON Videos: ", je);
            return null;
        }
        return videos;
    }

    public static ArrayList<Review> parseRevJson(String json) {
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(json);
            if (rootObject.has(RESULTS)) {
                JSONArray reviewArray = rootObject.getJSONArray(RESULTS);
                for (int i = 0; i < reviewArray.length(); i++) {
                    JSONObject currentReview = reviewArray.getJSONObject(i);
                    String reviewId = currentReview.optString(REVIEW_ID);
                    String reviewAuthor = currentReview.optString(REVIEW_WRITER);
                    String reviewContent = currentReview.optString(REVIEW_CONTENT);
                    String reviewUrl = currentReview.optString(REVIEW_URL);
                    Review review = new Review(reviewId, reviewAuthor, reviewContent, reviewUrl);
                    reviews.add(review);
                }
            }
        } catch (JSONException je) {
            je.printStackTrace();
            Log.e(TAG, "parseJSON Reviews: ", je);
            return null;
        }
        return reviews;
    }

    public static ArrayList<Movie> parseMovJson(String json) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(json);
            if (rootObject.has(RESULTS)) {
                JSONArray movieArray = rootObject.getJSONArray(RESULTS);
                for (int i = 0; i < movieArray.length(); i++) {
                    JSONObject currentMovie = movieArray.getJSONObject(i);
                    int id = currentMovie.optInt(ID);
                    String imagePath = currentMovie.optString(POSTER_PATH);
                    String backdropPath = currentMovie.optString(BACKDROP_PATH);
                    String originalTitle = currentMovie.optString(ORIGINAL_TITLE);
                    String synopsis = currentMovie.optString(OVERVIEW);
                    String releaseDate = currentMovie.optString(RELEASE_DATE);
                    Double rating = currentMovie.optDouble(VOTE_AVERAGE);
                    Movie movie = new Movie(id, imagePath, backdropPath, originalTitle, synopsis, releaseDate, rating);
                    movies.add(movie);
                }
            }
        } catch (JSONException je) {
            je.printStackTrace();
            Log.e(TAG, "parseJSON Movies: ",je);
            return null;
        }
        return movies;
    }
}


