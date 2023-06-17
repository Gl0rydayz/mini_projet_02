package com.example.mini_projet_02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.mini_projet_02.adapters.FavoriteQuotesAdapter;
import com.example.mini_projet_02.db.FavoriteQuotesDbOpenHelper;
import com.example.mini_projet_02.models.Quote;

import java.util.ArrayList;

public class AllFavoriteQuotesActivity extends AppCompatActivity {
    RecyclerView rv_allFavoriteQuotes;
    FavoriteQuotesDbOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_favorite_quotes);

        rv_allFavoriteQuotes = findViewById(R.id.rv_allFavoriteQuotes);
        db = new FavoriteQuotesDbOpenHelper(this);

        FavoriteQuotesAdapter favoriteQuotesAdapter = new FavoriteQuotesAdapter(db.getAll());
        rv_allFavoriteQuotes.setAdapter(favoriteQuotesAdapter);
        rv_allFavoriteQuotes.setLayoutManager(new LinearLayoutManager(this));
    }
}