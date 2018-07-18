package com.example.baris.moviesappfinal.Movie;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private int ID;
    private String originalTitle;
    private String imagePath;
    private String backdropPath;
    private String overview;
    private String releaseDate;
    private double voteAverage;

    public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185/";
    public static final String BACKDROP_PATH = "http://image.tmdb.org/t/p/w780/";

    public Movie
            (int ID, String imagePath, String backdropPath, String originalTitle, String overview, String releaseDate,
             Double voteAverage) {
        this.ID = ID;
        this.imagePath = imagePath;
        this.backdropPath = backdropPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    public Movie() {
    }

    public int getId() {
        return ID;
    }

    public void setId(int id) {
        ID = id;
    }

    public String getImg() {
        return imagePath;
    }

    public void setImg(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getBackdrop() {
        return backdropPath;
    }

    public void setBackdrop(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOrgTitle() {
        return originalTitle;
    }

    public void setOrgTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String synopsis) {
        overview = synopsis;
    }

    public String getReleaseDate(){
        return releaseDate;
    }

    public void setReleaseDate (String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getAverage() {
        return voteAverage;
    }

    public void setAverage(double rating) {
        voteAverage = rating;
    }

    public static Uri posterPath(Movie movie) {
        Uri uri = null;
        try {
            uri = Uri.parse(POSTER_PATH
                    + movie.getImg());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }


    public static Uri backdropPath(Movie movie) {
        Uri uri = null;
        try {
            uri = Uri.parse(BACKDROP_PATH
                    + movie.getBackdrop());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    private Movie(Parcel in) {
        ID = in.readInt();
        originalTitle = in.readString();
        imagePath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return ID + "--" + imagePath + "--" + backdropPath + "--" + originalTitle + "--"
                + overview + "--" + releaseDate + "--" + voteAverage;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(originalTitle);
        dest.writeString(imagePath);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
