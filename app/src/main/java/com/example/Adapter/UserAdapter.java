package com.example.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.User;
import com.example.spotifywrappedinstagramgroup5.ProfilePageSearchPopOut;
import com.example.spotifywrappedinstagramgroup5.R;
import com.example.spotifywrappedinstagramgroup5.SpotifyWrappedPopUp;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private ArrayList<User> list;

    public UserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = list.get(position);
        holder.username.setText(user.getUsername());

        // Set the click listener on the RelativeLayout
        holder.searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfilePageSearchPopOut.class);
                intent.putExtra("username", user.getUsername());
                intent.putExtra("faveArtists", user.getFaveArtists());
                intent.putExtra("mostPlayed", user.getMostPlayed());
                context.startActivity(intent);
                Toast.makeText(context, "Profile opened for " + user.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public RelativeLayout searchCard; // This is the layout for the entire item

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.search_item_username);
            searchCard = itemView.findViewById(R.id.profilePagePopUpLayout); // Assume this ID for your RelativeLayout
        }
    }
}
