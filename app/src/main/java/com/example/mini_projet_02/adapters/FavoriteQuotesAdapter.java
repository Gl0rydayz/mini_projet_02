package com.example.mini_projet_02.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        private final TextView tv_favQuoteItemInfos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_favQuoteItemInfos = itemView.findViewById(R.id.tv_favQuoteItemInfos);
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
        holder.tv_favQuoteItemInfos.setText(quotes.get(position).infos());
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }
}