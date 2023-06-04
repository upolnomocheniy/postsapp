package com.kvitka.postsApp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kvitka.postsApp.PostsItem;
import com.kvitka.postsApp.R;

import static com.bumptech.glide.Glide.*;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.*;
import static com.kvitka.postsApp.R.id.*;

public class PostsDetailsFragment extends Fragment {
    private PostsItem item;
    public PostsDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_details, container, false);
        Bundle args = getArguments();
        if (args != null) {
            this.item = new PostsItem();
            item.setTitle(args.getString("title"));
            item.setCategory(args.getString("category"));
            item.setSource(args.getString("source"));
            item.setUrl(args.getString("url"));
            item.setDescription(args.getString("description"));

            TextView title = view.findViewById(posts_title);
            title.setText(item.getTitle());
            TextView category = view.findViewById(R.id.category);
            category.setText(item.getCategory());
            TextView source = view.findViewById(R.id.source);
            source.setText(item.getSource());
            TextView description = view.findViewById(R.id.description);
            description.setText(item.getDescription());

            ImageView image = view.findViewById(R.id.image);

            with(this)
                    .load(item.getUrl())
                    .diskCacheStrategy(RESOURCE)
                    .into(image);
        }

        return view;
    }
}