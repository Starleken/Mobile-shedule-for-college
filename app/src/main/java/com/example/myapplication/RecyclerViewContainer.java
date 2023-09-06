package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewContainer {
    private final RecyclerView recyclerView;
    private final TextView textView;

    public RecyclerView getRecyclerView(){
        return recyclerView;
    }

    public TextView getTextView(){
        return textView;
    }

    public RecyclerViewContainer(RecyclerView recyclerView, TextView textView){
        this.recyclerView = recyclerView;
        this.textView = textView;
    }

    public void hideVisibility(){
        recyclerView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
    }
}
