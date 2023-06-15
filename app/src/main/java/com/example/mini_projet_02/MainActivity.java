package com.example.mini_projet_02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView tv_startActivityQuote, tv_startActivityAuthor;
    Button btn_startActivityPass;
    ToggleButton tb_startActivityPinUnpin;
    SharedPreferences sharedPreferences;
    ImageView iv_startActivityFavourite;
    boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_startActivityQuote = findViewById(R.id.tv_startActivityQuote);
        tv_startActivityAuthor = findViewById(R.id.tv_startActivityAuthor);
        btn_startActivityPass = findViewById(R.id.btn_startActivityPass);
        tb_startActivityPinUnpin = findViewById(R.id.tb_startActivityPinUnpin);
        iv_startActivityFavourite = findViewById(R.id.iv_startActivityFavorite);

        //region Pin Unpin Quote
        sharedPreferences = getSharedPreferences("pinned_quote", MODE_PRIVATE);

        String quote = sharedPreferences.getString("quote", null);

        if (quote == null) {
            getRandomQuote();
        } else {
            String author = sharedPreferences.getString("author", null);

            tv_startActivityQuote.setText(quote);
            tv_startActivityAuthor.setText(author);

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
                if(b) {
                    editor.putString("quote", tv_startActivityQuote.getText().toString());
                    editor.putString("author", tv_startActivityAuthor.getText().toString());
                    //Store quote somewhere
                    //the quote is pinned
                } else {
                    editor.putString("quote", null);
                    editor.putString("author", null);
                    getRandomQuote();
                    //remove the stored quote
                    //The quote is unpinned
                }
                editor.commit();
            }
        });
        //endregion


        iv_startActivityFavourite.setOnClickListener(v -> {
            if (isFavorite) {
                iv_startActivityFavourite.setImageResource(R.drawable.dislike);
            } else {
                iv_startActivityFavourite.setImageResource(R.drawable.like);
            }

            isFavorite = !isFavorite;
        });

        btn_startActivityPass.setOnClickListener(v -> {
            finish();
        });

    }

    private void getRandomQuote() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dummyjson.com/quotes/random";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tv_startActivityQuote.setText(response.getString("quote"));
                    tv_startActivityAuthor.setText(response.getString("author"));
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
}