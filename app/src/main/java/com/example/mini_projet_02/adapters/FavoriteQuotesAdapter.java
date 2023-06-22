package com.example.mini_projet_02.adapters;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mini_projet_02.R;
import com.example.mini_projet_02.models.Quote;

import java.util.ArrayList;

public class FavoriteQuotesAdapter extends RecyclerView.Adapter<FavoriteQuotesAdapter.ViewHolder> {
    private ArrayList<Quote> quotes = new ArrayList<>();

    public FavoriteQuotesAdapter(ArrayList<Quote> quotes) {
        this.quotes = quotes;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_favQuoteItemQ;
        private final TextView tv_favQuoteItemA;
        private final ImageView iv_favQuoteItemQuotations;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_favQuoteItemQ = itemView.findViewById(R.id.tv_favQuoteItemQ);
            tv_favQuoteItemA = itemView.findViewById(R.id.tv_favQuoteItemA);
            iv_favQuoteItemQuotations = itemView.findViewById(R.id.iv_favQuoteItemQuotaions);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_quotes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SpannableStringBuilder spannableQuote = new SpannableStringBuilder(quotes.get(position).getQuote());
        spannableQuote.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableQuote.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableQuote.setSpan(new RelativeSizeSpan(1.5f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder spannableAuthor = new SpannableStringBuilder(quotes.get(position).getAuthor());
        spannableAuthor.setSpan(new UnderlineSpan(), 0, spannableAuthor.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.tv_favQuoteItemQ.setText(spannableQuote);
        holder.tv_favQuoteItemA.setText(spannableAuthor);
        holder.iv_favQuoteItemQuotations.setImageResource(R.drawable.quotations);
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }
}
