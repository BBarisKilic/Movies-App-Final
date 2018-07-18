package com.example.baris.moviesappfinal.Models;

import android.net.Uri;

public class Video {
    private static final String YOUTUBE_PATH = "https://www.youtube.com/watch";
    private static final String BASE_THUMBNAIL_PATH = "https://i1.ytimg.com/vi";
    private static final String THUMBNAIL_PATH = "0.jpg";

    private String videoName;
    private String videoKey;


    public Video (String videoName, String videoKey) {
        this.videoName = videoName;
        this.videoKey = videoKey;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    @Override
    public String toString() {
        return videoName + "--" + videoKey + "--";
    }


    public static Uri buildVideoThumbnailPath (Video vid) {
        Uri uri = null;
        String keySort = vid.getVideoKey();
        try {
            uri = Uri.parse(BASE_THUMBNAIL_PATH)
                    .buildUpon()
                    .appendPath(keySort)
                    .appendPath(THUMBNAIL_PATH)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    public static Uri buildYouTubePath (Video vid) {
        Uri uri = null;
        String keySort = vid.getVideoKey();
        try {
            uri = Uri.parse(YOUTUBE_PATH)
                    .buildUpon()
                    .appendQueryParameter("v", keySort)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }
}
