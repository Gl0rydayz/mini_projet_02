package com.example.mini_projet_02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mini_projet_02.adapters.FavoriteQuotesAdapter;
import com.example.mini_projet_02.db.FavoriteQuotesDbOpenHelper;
import com.example.mini_projet_02.models.Quote;

import java.util.ArrayList;

public class AllFavoriteQuotesActivity extends AppCompatActivity {
    RecyclerView rv_allFavoriteQuotes;
    FavoriteQuotesDbOpenHelper db;
    TextView tv_allFavoriteQuoteChoseLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_favorite_quotes);

        rv_allFavoriteQuotes = findViewById(R.id.rv_allFavoriteQuotes);
        tv_allFavoriteQuoteChoseLay = findViewById(R.id.tv_allFavoriteQuotesChoseLay);
        db = new FavoriteQuotesDbOpenHelper(this);

        FavoriteQuotesAdapter favoriteQuotesAdapter = new FavoriteQuotesAdapter(db.getAll());
        rv_allFavoriteQuotes.setAdapter(favoriteQuotesAdapter);
//        rv_allFavoriteQuotes.setLayoutManager(new GridLayoutManager(this, 2));

        registerForContextMenu(tv_allFavoriteQuoteChoseLay);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Layout type");
        menu.add(0, v.getId(), 0, "List");
        menu.add(0, v.getId(), 0, "Grid");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle() == "List") {
            rv_allFavoriteQuotes.setLayoutManager(new LinearLayoutManager(this));
        } else {
            rv_allFavoriteQuotes.setLayoutManager(new GridLayoutManager(this, 2));
        }

        return true;
    }
}