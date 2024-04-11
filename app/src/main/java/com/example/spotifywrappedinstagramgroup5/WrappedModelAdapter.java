package com.example.spotifywrappedinstagramgroup5;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.spotifywrappedinstagramgroup5.R;
import java.util.List;

public class WrappedModelAdapter extends RecyclerView.Adapter<WrappedModelAdapter.ViewHolder> {

    private List<WrappedModel> wrappedModelList;
    private Context context;

    public WrappedModelAdapter(Context context, List<WrappedModel> wrappedModelList) {
        this.context = context;
        this.wrappedModelList = wrappedModelList;
    }

    @NonNull
    @Override
    public WrappedModelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.homepage_cards, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WrappedModelAdapter.ViewHolder holder, int position) {
        WrappedModel wrappedModel = wrappedModelList.get(position);
        holder.textUsername.setText(wrappedModel.getUserId()); // Assuming the userId is the username
        holder.textDescription.setText(wrappedModel.getDescription());
        holder.textTracks.setText(wrappedModel.getTracks());
        holder.textArtists.setText(wrappedModel.getArtists());
        holder.textGenres.setText(wrappedModel.getGenres());
        Log.d("e", wrappedModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return wrappedModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagePost;
        public TextView textUsername;
        public TextView textDescription;
        public TextView textArtists;
        public TextView textTracks;
        public TextView textGenres;

        public ViewHolder(View itemView) {
            super(itemView);
            imagePost = itemView.findViewById(R.id.image_post);
            textUsername = itemView.findViewById(R.id.text_username);
            textDescription = itemView.findViewById(R.id.text_description);
            textArtists = itemView.findViewById(R.id.text_artists);
            textGenres = itemView.findViewById(R.id.text_genres);
            textTracks = itemView.findViewById(R.id.text_tracks);
        }
    }
}

