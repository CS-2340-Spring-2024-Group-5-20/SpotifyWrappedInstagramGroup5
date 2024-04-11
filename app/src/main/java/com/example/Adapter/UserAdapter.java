//package com.example.Adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.Models.User;
//import com.example.spotifywrappedinstagramgroup5.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
//    private Context context;
//    private ArrayList<User> list;
//
//
//    public UserAdapter(Context mContext, List<User> mUsers) {
//        this.mContext = mContext;
//        this.mUsers = mUsers;
//    }
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.user_search_item, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        fireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        final User user = mUsers.get(position);
//
//        holder.userID.setText(user.getUserID());
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView userID, description;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            userID = itemView.findViewById(R.id.user_id);
//            description = itemView.findViewById(R.id.text_description);
//        }
//    }
//}
