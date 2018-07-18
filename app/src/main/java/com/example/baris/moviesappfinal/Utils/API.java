package com.example.baris.moviesappfinal.Utils;

import android.net.Uri;
import java.net.MalformedURLException;
import java.net.URL;


public class API {
    private static final String baseURL = "http://api.themoviedb.org/3/movie";
    private static final String api = "api_key";
    private static final String apiKey = "<<<<<<<<<<<<<<<<<<<PUT API KEY>>>>>>>>>>>>>>>>>>";
    private static final String logTAG = API.class.getSimpleName();
    public static final String video = "videos";
    public static final String review = "reviews";

    public static URL buildVidRevUrl(String movId, String vidRevSort) {
        Uri uri = Uri.parse(baseURL)
                .buildUpon()
                .appendPath(movId)
                .appendPath(vidRevSort)
                .appendQueryParameter(api, apiKey)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static URL buildMovUrl(String sortType) {
        Uri uri = Uri.parse(baseURL)
                .buildUpon()
                .appendPath(sortType)
                .appendQueryParameter(api, apiKey)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}



