package com.example.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

public class UILoadHandler {
    public void setLoadUI(View[] viewsToHide, ProgressBar progressBar){
        for (View view : viewsToHide){
            view.setVisibility(View.GONE);
        }

        progressBar.setVisibility(View.VISIBLE);
    }

    public void onDataUILoaded(View[] viewsToShow, ProgressBar progressBar){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (View view : viewsToShow){
                    view.setVisibility(View.VISIBLE);
                }

                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
