package com.example.spotifywrappedinstagramgroup5;

// Android
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


/**
 * ReminderPage class for the fragment holding the task recycler view.
 */
public class PostPage extends Fragment {

    private RecyclerView recyclerView;

    /**
     * Required empty public constructor.
     */
    public PostPage() {}


    public static PostPage newInstance(String param1, String param2) {
        PostPage fragment = new PostPage();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_postpage, container, false);
    }
}
