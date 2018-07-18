package com.example.baris.moviesappfinal.Adapters;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import com.example.baris.moviesappfinal.R;
import com.example.baris.moviesappfinal.Models.Video;



public class VidAdapter extends RecyclerView.Adapter<VidAdapter.VideoHolder>{

    private Context context;
    private ArrayList<Video> videos;
    private final VideoAdapterOnClickHandler videoClickHandler;

    public interface VideoAdapterOnClickHandler {
        void onClickVideo(Video currentVideo);
    }

    public VidAdapter(Context context, ArrayList<Video> videos, VideoAdapterOnClickHandler videoClickHandler){
        this.context = context;
        this.videos = videos;
        this.videoClickHandler = videoClickHandler;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForVideoItem = R.layout.video;
        LayoutInflater mInflater = LayoutInflater.from(context);
        View mItemView = mInflater.inflate(layoutIdForVideoItem, parent, false);
        return new VideoHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(VideoHolder videoViewHolder, int pos) {
        context = videoViewHolder.videoImageView.getContext();
        Video mCurrentVideo = videos.get(pos);
        Picasso.with(context).load(Video.buildVideoThumbnailPath(mCurrentVideo))
                .placeholder(R.drawable.video_holder)
                .error(R.drawable.video_holder)
                .into(videoViewHolder.videoImageView);
        videoViewHolder.videoTitleView.setText(mCurrentVideo.getVideoName());
    }

    @Override
    public int getItemCount() {
        if (videos == null) return 0;
        return videos.size();
    }

    public void setVideoDatabase(ArrayList<Video> videoDatabase) {
        videos = videoDatabase;
        notifyDataSetChanged();
    }

    public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView videoImageView;
        public final TextView videoTitleView;

        public VideoHolder(View v) {
            super(v);
            videoTitleView = (TextView)v.findViewById(R.id.tv_videoTitle);
            videoImageView = (ImageView)v.findViewById(R.id.iv_video);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPos = getAdapterPosition();
            Video currentVideo = videos.get(adapterPos);
            videoClickHandler.onClickVideo(currentVideo);
        }
    }
}
