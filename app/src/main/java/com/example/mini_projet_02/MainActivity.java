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
    TextView tv_startActivityQuote, tv_startActivityAuthor;
    Button btn_startActivityPass;
    ToggleButton tb_startActivityPinUnpin;
    SharedPreferences sharedPreferences;
    Spinner spinner_startActivityColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_startActivityQuote = findViewById(R.id.tv_startActivityQuote);
        tv_startActivityAuthor = findViewById(R.id.tv_startActivityAuthor);
        btn_startActivityPass = findViewById(R.id.btn_startActivityPass);
        tb_startActivityPinUnpin = findViewById(R.id.tb_startActivityPinUnpin);
        spinner_startActivityColors = findViewById(R.id.spinner_startActivityColors);

        sharedPreferences = getSharedPreferences("pinned_quote", MODE_PRIVATE);

        String quote = sharedPreferences.getString("quote", null);
        int defaultColor = Color.WHITE; // Set a default color value
        int color = sharedPreferences.getInt("color", defaultColor);
        getWindow().getDecorView().setBackgroundColor(color);

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

        ArrayList<String> colors = new ArrayList<>(Arrays.asList("Default", "LightSalmon", "Plum", "PaleGreen", "CornflowerBlue"));
        ArrayList<String> colorsHex = new ArrayList<>(Arrays.asList("#FFFFFF", "#FFA07A", "#DDA0DD", "#98FB98", "#6495ED"));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, colors);

        spinner_startActivityColors.setAdapter(adapter);

        spinner_startActivityColors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
//                int color = Color.parseColor(colorsHex.get(i));
//                if (colors.get(i).equals("Default")) {
//                    color = Color.parseColor(colorsHex.get(0));
//                } else {
//                    color = Color.parseColor(colorsHex.get(i));
//                }
                int color = 0;
                switch (colors.get(i)){
                    case "Default" :
                        color = Color.parseColor(colorsHex.get(0));
                        break;
                    case "LightSalmon" :
                        color = Color.parseColor(colorsHex.get(1));
                        break;
                    case "Plum" :
                        color = Color.parseColor(colorsHex.get(2));
                        break;
                    case "PaleGreen" :
                        color = Color.parseColor(colorsHex.get(3));
                        break;
                    case "CornflowerBlue" :
                        color = Color.parseColor(colorsHex.get(4));
                        break;
                }

                getWindow().getDecorView().setBackgroundColor(color);

                int backgroundColor = ((ColorDrawable) getWindow().getDecorView().getBackground()).getColor();

                editor.putInt("color", backgroundColor);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
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