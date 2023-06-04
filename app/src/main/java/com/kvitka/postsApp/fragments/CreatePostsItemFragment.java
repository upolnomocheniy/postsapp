package com.kvitka.postsApp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.kvitka.postsApp.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_spinner_dropdown_item;
import static com.kvitka.postsApp.R.id.*;
import static com.kvitka.postsApp.R.layout.fragment_posts_create;
import static com.kvitka.postsApp.R.layout.spinner_item_layout;


public class CreatePostsItemFragment extends Fragment {
    private DatabaseHelper databaseHelper;

    public CreatePostsItemFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.databaseHelper = new DatabaseHelper(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(fragment_posts_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Spinner spinner = view.findViewById(spinnerCategory);
        List<String> categories = new ArrayList<>(databaseHelper.getAllCategories());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this.getContext(),
                spinner_item_layout,
                categories
        );
        adapter.setDropDownViewResource(simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Button submitButton = view.findViewById(buttonSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title = view.findViewById(editTextTitle);
                EditText description = view.findViewById(editTextDescription);
                Spinner category = view.findViewById(spinnerCategory);
                EditText imgUrl = view.findViewById(editTextImgUrl);
                EditText source = view.findViewById(editTextSource);
                String selectedItemValue = category.getSelectedItem().toString();
                int categoryId = databaseHelper.getCategoryIdByName(selectedItemValue);
                databaseHelper.addPosts(
                        title.getText().toString(),
                        description.getText().toString(),
                        imgUrl.getText().toString(),
                        categoryId,
                        source.getText().toString()
                );
                title.clearFocus();
                title.clearComposingText();
                title.clearComposingText();
                description.clearComposingText();
                imgUrl.clearComposingText();
                source.clearComposingText();
                requireActivity().onBackPressed();
            }
        });
    }
}