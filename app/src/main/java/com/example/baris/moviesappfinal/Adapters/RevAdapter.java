package com.example.baris.moviesappfinal.Adapters;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import com.example.baris.moviesappfinal.R;
import com.example.baris.moviesappfinal.Models.Review;



public class RevAdapter extends RecyclerView.Adapter<RevAdapter.ReviewHolder>{

    private ArrayList<Review> reviews;
    private Context context;
    private final ReviewAdapterOnClickHandler reviewClickHandler;

    public interface ReviewAdapterOnClickHandler {
        void onClickReview(Review currentReview);
    }

    public RevAdapter(Context context, ArrayList<Review> reviews,
                      ReviewAdapterOnClickHandler reviewClickHandler) {
        this.context = context;
        this.reviews = reviews;
        this.reviewClickHandler = reviewClickHandler;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForReviewItem = R.layout.review;
        LayoutInflater mInflater = LayoutInflater.from(context);
        View mItemView = mInflater.inflate(layoutIdForReviewItem, parent, false);
        return new ReviewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(ReviewHolder reviewViewHolder, int pos) {
        context = reviewViewHolder.reviewWriterView.getContext();
        Review currentReview = reviews.get(pos);
        reviewViewHolder.reviewWriterView.setText(currentReview.getReviewWriter());
        reviewViewHolder.reviewContentView.setText(currentReview.getReviewContent());
    }

    @Override
    public int getItemCount() {
        if (reviews == null) return 0;
        return reviews.size();
    }

    public void setReviewDatabase(ArrayList<Review> reviewDatabase) {
        reviews = reviewDatabase;
        notifyDataSetChanged();
    }


    public class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView reviewWriterView;
        public final TextView reviewContentView;

        public ReviewHolder(View v) {
            super(v);
            reviewContentView = (TextView) v.findViewById(R.id.tv_review);
            reviewWriterView = (TextView) v.findViewById(R.id.tv_writerName);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPos = getAdapterPosition();
            Review currentReview = reviews.get(adapterPos);
            reviewClickHandler.onClickReview(currentReview);
        }
    }
}
