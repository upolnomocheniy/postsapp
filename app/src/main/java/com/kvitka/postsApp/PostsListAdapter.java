package com.kvitka.postsApp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.Glide;
import com.kvitka.postsApp.databinding.PostsItemBinding;
import com.kvitka.postsApp.db.DatabaseHelper;
import com.kvitka.postsApp.list.OnRecyclerViewItemClickListener;
import com.kvitka.postsApp.list.OnRemoveButtonClickListener;

import java.util.List;

import static android.view.LayoutInflater.from;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.*;
import static com.kvitka.postsApp.databinding.PostsItemBinding.inflate;
import static java.lang.String.valueOf;
import static java.util.Collections.*;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostsViewHolder> {
    private List<PostsItem> mPostsList = emptyList();
    private DatabaseHelper databaseHelper;
    private OnRecyclerViewItemClickListener itemClickListener;
    private OnRemoveButtonClickListener removeButtonClickListener;

    class PostsViewHolder extends ViewHolder {
       private PostsItemBinding postsItemBinding;
       private PostsItem postsItem;

        public PostsViewHolder(PostsItemBinding binding) {
            super(binding.getRoot());
            postsItemBinding = binding;
        }

        public void bind(PostsItem postsItem) {
            this.postsItem = postsItem;
            postsItemBinding.postsTitle.setText(postsItem.getTitle());
            postsItemBinding.category.setText(postsItem.getCategory());
            postsItemBinding.source.setText(postsItem.getSource());
            postsItemBinding.removeButton.setContentDescription(valueOf(postsItem.getId()));

            Glide.with(postsItemBinding.postsImage.getContext())
                    .load(postsItem.getUrl())
                    //.placeholder(R.mipmap.poster_tmp)
                    //.error(R.drawable.ic_baseline_error_outline_24)
                    .diskCacheStrategy(RESOURCE)
                    .into(postsItemBinding.image);
        }

        public PostsItem getPostsItem() {
            return postsItem;
        }
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = from(parent.getContext());
        PostsItemBinding postsItemBinding = inflate(inflater, parent, false);
        return new PostsViewHolder(postsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostsViewHolder holder, final int position) {
        holder.bind(mPostsList.get(position));

        holder.postsItemBinding.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (removeButtonClickListener != null) {
                    removeButtonClickListener.onRemoveBtnClick(v);
                }
            }
        });

        holder.postsItemBinding.postsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(holder.getPostsItem(), view);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPostsList.size();
    }

    public void setPostsList(List<PostsItem> posts) {
        this.mPostsList = posts;
        Log.e(PostsListAdapter.class.getCanonicalName(), this.mPostsList.toString());
    }

    public List<PostsItem> getPostsList() {
        return mPostsList;
    }

    public void setDatabaseHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void setItemClickListener(OnRecyclerViewItemClickListener mClickListener) {
        this.itemClickListener = mClickListener;
    }

    public void setRemoveButtonClickListener(OnRemoveButtonClickListener removeButtonClickListener) {
        this.removeButtonClickListener = removeButtonClickListener;
    }
}
