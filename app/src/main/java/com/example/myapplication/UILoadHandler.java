package com.example.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

public class UILoadHandler {
    View[] viewsToHide;
    ProgressBar progressBar;

    public UILoadHandler(View[] viewsToHide, ProgressBar progressBar){
        this.viewsToHide = viewsToHide;
        this.progressBar = progressBar;
    }

    public void setLoadUI(){
        for (View view : viewsToHide){
            view.setVisibility(View.GONE);
        }

        progressBar.setVisibility(View.VISIBLE);
    }

    public void onDataUILoaded(){
        for (View view : viewsToHide){
            view.setVisibility(View.VISIBLE);
        }

        progressBar.setVisibility(View.GONE);
    }
}
