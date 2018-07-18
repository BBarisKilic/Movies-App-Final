package com.example.baris.moviesappfinal.Models;

public class Review
{
    private String reviewUrl;
    private String reviewContent;
    private String reviewWriter;
    private String reviewId;

    public Review (String reviewId, String reviewWriter, String reviewContent, String reviewUrl) {
        this.reviewId = reviewId;
        this.reviewWriter = reviewWriter;
        this.reviewContent = reviewContent;
        this.reviewUrl = reviewUrl;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getReviewWriter() {
        return reviewWriter;
    }

    public void setReviewWriter(String ReviewWriter) {
        reviewWriter = ReviewWriter;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId (String reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public String toString() {
        return reviewId + "--" + reviewWriter + "--" + reviewContent + "--" + reviewUrl + "--";
    }
}
