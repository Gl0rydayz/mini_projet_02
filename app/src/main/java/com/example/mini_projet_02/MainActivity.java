package com.example.mini_projet_02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mini_projet_02.db.ColorsAndSettingDbOpenHelper;
import com.example.mini_projet_02.db.FavoriteQuotesDbOpenHelper;
import com.example.mini_projet_02.models.Color;
import com.example.mini_projet_02.models.Quote;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private final static int INVALID_ID = -1;
    TextView tv_startActivityQuote, tv_startActivityAuthor;
    Button btn_startActivityPass;
    ToggleButton tb_startActivityPinUnpin;
    SharedPreferences sharedPreferences;
    ImageView iv_startActivityFavourite;
    TextView tv_startActivityId;
    FavoriteQuotesDbOpenHelper db;
    Spinner spinner_startActivityColors;
    ColorsAndSettingDbOpenHelper colorsAndSettingDbOpenHelper;
    Color color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_startActivityQuote = findViewById(R.id.tv_startActivityQuote);
        tv_startActivityAuthor = findViewById(R.id.tv_startActivityAuthor);
        btn_startActivityPass = findViewById(R.id.btn_startActivityPass);
        tb_startActivityPinUnpin = findViewById(R.id.tb_startActivityPinUnpin);
        iv_startActivityFavourite = findViewById(R.id.iv_startActivityFavorite);
        tv_startActivityId = findViewById(R.id.tv_startActivityId);
        spinner_startActivityColors = findViewById(R.id.spinner_startActivityColors);

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

        btn_startActivityPass.setOnClickListener(v -> {
            finish();
        });

        //region Handle Colors Spinner
        colorsAndSettingDbOpenHelper = new ColorsAndSettingDbOpenHelper(this);

        ArrayList<String > colors  = new ArrayList<>(Arrays.asList("Default", "LightSalmon", "Pulm", "PaleGreen", "CornflowerBlue"));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, colors);
        spinner_startActivityColors.setAdapter(adapter);

        spinner_startActivityColors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        color = new Color( "Default", "#FFFFFF");
                        colorsAndSettingDbOpenHelper.addSetting(color);
                        getWindow().getDecorView().setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"));
                        break;
                    case 1:
                        color = new Color( "LightSalmon", "#FFA07A");
                        colorsAndSettingDbOpenHelper.addSetting(color);
                        getWindow().getDecorView().setBackgroundColor(android.graphics.Color.parseColor("#FFA07A"));
                        break;
                    case 2:
                        color = new Color( "Plum", "#DDA0DD");
                        colorsAndSettingDbOpenHelper.addSetting(color);
                        getWindow().getDecorView().setBackgroundColor(android.graphics.Color.parseColor("#DDA0DD"));
                        break;
                    case 3:
                        color = new Color( "PaleGreen", "#98FB98");
                        colorsAndSettingDbOpenHelper.addSetting(color);
                        getWindow().getDecorView().setBackgroundColor(android.graphics.Color.parseColor("#98FB98"));
                        break;
                    case 4:
                        color = new Color( "CornflowerBlue", "#6495ED");
                        colorsAndSettingDbOpenHelper.addSetting(color);
                        getWindow().getDecorView().setBackgroundColor(android.graphics.Color.parseColor("#6495ED"));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //endregion
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