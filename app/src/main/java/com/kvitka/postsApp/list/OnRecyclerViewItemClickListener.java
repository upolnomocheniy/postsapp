package com.kvitka.postsApp.list;

import android.view.View;

import com.kvitka.postsApp.PostsItem;

public interface OnRecyclerViewItemClickListener {
    void onItemClick(PostsItem item, View view);
}
