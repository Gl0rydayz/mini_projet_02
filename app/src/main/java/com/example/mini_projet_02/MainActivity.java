package com.example.mini_projet_02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    //region Declare Attributes
    TextView tv_startActivityQuote, tv_startActivityAuthor;
    Button btn_startActivityPass;
    ToggleButton tb_startActivityPinUnpin;
    SharedPreferences sharedPreferences;
    Spinner spinner_startActivityColors;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region Initialize Attributes
        tv_startActivityQuote = findViewById(R.id.tv_startActivityQuote);
        tv_startActivityAuthor = findViewById(R.id.tv_startActivityAuthor);
        btn_startActivityPass = findViewById(R.id.btn_startActivityPass);
        tb_startActivityPinUnpin = findViewById(R.id.tb_startActivityPinUnpin);
        spinner_startActivityColors = findViewById(R.id.spinner_startActivityColors);
        //endregion

        sharedPreferences = getSharedPreferences("pinned_quote", MODE_PRIVATE);

        ArrayList<String> colors = new ArrayList<>(Arrays.asList("Default", "LightSalmon", "Plum", "PaleGreen", "CornflowerBlue"));
        ArrayList<String> colorsHex = new ArrayList<>(Arrays.asList("#FFFFFF", "#FFA07A", "#DDA0DD", "#98FB98", "#6495ED"));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, colors);

        spinner_startActivityColors.setAdapter(adapter);

        int bgColorPosition = sharedPreferences.getInt("bgColorPosition", 0);
        if (bgColorPosition != 0) {
            getWindow().getDecorView().setBackgroundColor(Color.parseColor(colorsHex.get(bgColorPosition)));
            spinner_startActivityColors.setSelection(bgColorPosition, true);
        }

        spinner_startActivityColors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                String backgroundColor = colorsHex.get(i);
                getWindow().getDecorView().setBackgroundColor(Color.parseColor(backgroundColor));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("bgColorPosition", i);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //region Pin or Unpin Quote
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
                if (b) {
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

        //region OnBackPress Method
        btn_startActivityPass.setOnClickListener(v -> {
            finish();
        });
        //endregion

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