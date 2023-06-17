package com.example.mini_projet_02.models;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.NonNull;

public class Quote {
    private int id;
    private String quote;
    private String author;

    //region Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    //endregion

    //region Constructor
    public Quote(int id, String quote, String author) {
        this.id = id;
        this.quote = quote;
        this.author = author;
    }
    //endregion


    @NonNull
    @Override
    public String toString() {
        return String.format("%d %s %s", getId(), getQuote(), getAuthor());
    }

    public Spannable infos() {
        SpannableStringBuilder spannableQuote = new SpannableStringBuilder(quote);
        Spannable spannableAuthor = new SpannableString("\n>>>" + author);

        spannableQuote.setSpan(
                new ForegroundColorSpan(Color.BLUE),
                0,
                spannableQuote.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannableAuthor.setSpan(
                new BackgroundColorSpan(Color.GRAY),
                4,
                spannableAuthor.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannableQuote.insert(spannableQuote.length(), spannableAuthor);

        return spannableQuote;
    }
}
