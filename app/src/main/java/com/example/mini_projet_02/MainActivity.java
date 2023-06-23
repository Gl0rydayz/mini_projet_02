package com.example.mini_projet_02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mini_projet_02.db.FavoriteQuotesDbOpenHelper;
import com.example.mini_projet_02.models.Quote;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private final static int INVALID_ID = -1;
    TextView tv_startActivityQuote, tv_startActivityAuthor;
    Button btn_startActivityShowFQ;
    ToggleButton tb_startActivityPinUnpin;
    SharedPreferences sharedPreferences;
    ImageView iv_startActivityFavourite;
    TextView tv_startActivityId;
    FavoriteQuotesDbOpenHelper db;
    ImageView iv_startActivityColorLens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_startActivityQuote = findViewById(R.id.tv_startActivityQuote);
        tv_startActivityAuthor = findViewById(R.id.tv_startActivityAuthor);
        btn_startActivityShowFQ = findViewById(R.id.btn_startActivityShowFQ);
        tb_startActivityPinUnpin = findViewById(R.id.tb_startActivityPinUnpin);
        iv_startActivityFavourite = findViewById(R.id.iv_startActivityFavorite);
        tv_startActivityId = findViewById(R.id.tv_startActivityId);
        iv_startActivityColorLens = findViewById(R.id.iv_startActivityColorLens);

        //region Persistence Objects
        db = new FavoriteQuotesDbOpenHelper(this);
        sharedPreferences = getSharedPreferences("pinned_quote", MODE_PRIVATE);
        //endregion

        //region Pin Unpin Quote
        int pinnedQuoteId = sharedPreferences.getInt("id", INVALID_ID);

        if (pinnedQuoteId == INVALID_ID) {
            getRandomQuote();
        } else {
            String quote = sharedPreferences.getString("quote", null);
            String author = sharedPreferences.getString("author", null);

            tv_startActivityId.setText(String.format("#%d", pinnedQuoteId));
            tv_startActivityQuote.setText(quote);
            tv_startActivityAuthor.setText(author);

            iv_startActivityFavourite.setImageResource(db.isFavorite(pinnedQuoteId) ? R.drawable.like : R.drawable.dislike);

            tb_startActivityPinUnpin.setChecked(true);
        }

        tb_startActivityPinUnpin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                /*
                checked -> PIN
                unchecked -> Unpin
                 */
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (b) {
                    editor.putInt("id", Integer.parseInt(tv_startActivityId.getText().toString().substring(1)));
                    editor.putString("quote", tv_startActivityQuote.getText().toString());
                    editor.putString("author", tv_startActivityAuthor.getText().toString());
                    //Store quote somewhere
                    //the quote is pinned

                    if(!db.isFavorite(Integer.parseInt(tv_startActivityId.getText().toString().substring(1)))) {
                        iv_startActivityFavourite.setImageResource(R.drawable.like);
                        db.add(new Quote(Integer.parseInt(tv_startActivityId.getText().toString().substring(1)),
                                tv_startActivityQuote.getText().toString(),
                                tv_startActivityAuthor.getText().toString()));
                    }
                } else {
                    editor.putInt("id", INVALID_ID);
                    editor.putString("quote", null);
                    editor.putString("author", null);
//                    getRandomQuote();
                    //remove the stored quote
                    //The quote is unpinned
                }
                editor.commit();
            }
        });
        //endregion


        //region Like | Dslike Quote
        iv_startActivityFavourite.setOnClickListener(v -> {
            int id = Integer.parseInt(tv_startActivityId.getText().toString().substring(1));
            boolean isFavorite = db.isFavorite(id);
            if (isFavorite) {
                //Unpin the quote
                tb_startActivityPinUnpin.setChecked(false);

                iv_startActivityFavourite.setImageResource(R.drawable.dislike);

                db.delete(id);
            } else {
                iv_startActivityFavourite.setImageResource(R.drawable.like);

                String quote = tv_startActivityQuote.getText().toString();
                String author = tv_startActivityAuthor.getText().toString();
                db.add(new Quote(id, quote, author));
            }
        });
        //endregion

        btn_startActivityShowFQ.setOnClickListener(v -> {
            startActivity(new Intent(this, AllFavoriteQuotesActivity.class));
        });

        registerForContextMenu(iv_startActivityColorLens);

        iv_startActivityColorLens.setOnClickListener(v -> {
            openContextMenu(v);
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Colors");
        String[] colorNames = getResources().getStringArray(R.array.color_names);
        for (int i = 0; i < colorNames.length; i++) {
            menu.add(0, i, 0, colorNames[i]);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        String[] colorCodes = getResources().getStringArray(R.array.color_codes);
        int itemId = item.getItemId();
        if (itemId >= 0 && itemId < colorCodes.length) {
            getWindow().getDecorView().setBackgroundColor(Color.parseColor(colorCodes[itemId]));
        }
        return true;
    }

    private void getRandomQuote() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dummyjson.com/quotes/random";

//        int range = new Random().nextInt(4) + 1;
//        String url = "https://dummyjson.com/quotes/" + range;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int id = response.getInt("id");
                    String quote = response.getString("quote");
                    String author = response.getString("author");

                    if(db.isFavorite(id)) {
                        iv_startActivityFavourite.setImageResource(R.drawable.like);
                    } else {
                        iv_startActivityFavourite.setImageResource(R.drawable.dislike);
                    }

                    tv_startActivityQuote.setText(quote);
                    tv_startActivityAuthor.setText(author);
                    tv_startActivityId.setText("#" + id);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}