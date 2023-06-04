package com.kvitka.postsApp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kvitka.postsApp.PostsItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "posts.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCategoriesTableQuery = "CREATE TABLE Categories (id INTEGER PRIMARY KEY, name TEXT)";
        db.execSQL(createCategoriesTableQuery);

        String createPostsTableQuery = "CREATE TABLE Posts (id INTEGER PRIMARY KEY, title TEXT, description TEXT, image_url TEXT, category_id INTEGER, source TEXT, FOREIGN KEY (category_id) REFERENCES Categories(id))";
        db.execSQL(createPostsTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropCategoriesTableQuery = "DROP TABLE IF EXISTS Categories";
        db.execSQL(dropCategoriesTableQuery);
        String dropPostsTableQuery = "DROP TABLE IF EXISTS Posts";
        db.execSQL(dropPostsTableQuery);
        onCreate(db);
    }

    public List<PostsItem> loadAllPosts() {
        List<PostsItem> posts = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT Posts.id, Posts.title, Posts.source, Posts.image_url, Posts.description, Categories.name AS category_name " +
                "FROM Posts " +
                "JOIN Categories ON Posts.category_id = Categories.id ";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String source = cursor.getString(cursor.getColumnIndex("source"));
                String imageUrl = cursor.getString(cursor.getColumnIndex("image_url"));
                String categoryName = cursor.getString(cursor.getColumnIndex("category_name"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                PostsItem item = new PostsItem(id, title, categoryName, source, imageUrl, description);
                posts.add(item);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return posts;
    }

    public void deleteArticleById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(id)};
        db.delete("Posts", selection, selectionArgs);
    }

    public void addPosts(String title, String description, String imageUrl, int categoryId, String source) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("image_url", imageUrl);
        values.put("category_id", categoryId);
        values.put("source", source);
        db.insert("Posts", null, values);
    }

    public List<String> getAllCategories() {
        SQLiteDatabase db = getReadableDatabase();
        List<String> categories = new ArrayList<>();
        String[] projection = {"name"};
        Cursor cursor = db.query("Categories", projection, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndex("name"));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return categories;
    }

    public void addCategory(String categoryName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", categoryName);
        db.insert("Categories", null, values);
    }

    public int getCategoryIdByName(String categoryName) {
        SQLiteDatabase db = getReadableDatabase();
        int categoryId = -1;
        String[] projection = {"id"};
        String selection = "name = ?";
        String[] selectionArgs = {categoryName};
        Cursor cursor = db.query("Categories", projection, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            categoryId = cursor.getInt(cursor.getColumnIndex("id"));
        }
        if (cursor != null) {
            cursor.close();
        }
        return categoryId;
    }

    public List<PostsItem> getPostsByTitle(String searchTitle) {
        List<PostsItem> postsList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query =
                "SELECT Posts.id, Posts.title, Posts.source, Posts.image_url, Posts.description, Categories.name AS category_name " +
                "FROM Posts " +
                "JOIN Categories ON Posts.category_id = Categories.id " +
                "WHERE Posts.title LIKE ?";
        String[] selectionArgs = {"%" + searchTitle + "%"};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String imageUrl = cursor.getString(cursor.getColumnIndex("image_url"));
                String categoryName = cursor.getString(cursor.getColumnIndex("category_name"));
                String source = cursor.getString(cursor.getColumnIndex("source"));
                PostsItem postsItem = new PostsItem(id, title, categoryName, source, imageUrl, description);
                postsList.add(postsItem);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return postsList;
    }
}