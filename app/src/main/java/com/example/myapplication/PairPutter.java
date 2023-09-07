package com.example.myapplication;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.myapplication.Adapters.LessonAdapter;
import com.example.myapplication.Models.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PairPutter {
    private Context context;
    private UILoadHandler uiLoadHandler;

    public PairPutter(UILoadHandler uiLoadHandler, Context context){
        this.uiLoadHandler = uiLoadHandler;
        this.context = context;
    }


    public void putPairs(List<Pair> pairs, HashMap<String, RecyclerViewContainer> recyclerViewContainerHashMap, LessonAdapter.OnTeacherClickListener teacherListener, LessonAdapter.OnAudienceClickListener audienceListener){
        Handler mHandler = new Handler(Looper.getMainLooper());

        HashMap<String, ArrayList<Pair>> dayPairsMap = new HashMap<>();

        for(Pair pair : pairs){
            String dayName = pair.dayOfWeek.name;
            if(!dayPairsMap.containsKey(dayName)){
                dayPairsMap.put(dayName, new ArrayList<Pair>());
            }
            dayPairsMap.get(dayName).add(pair);
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for(Map.Entry<String, ArrayList<Pair>> entry : dayPairsMap.entrySet()){
                    LessonAdapter dayAdapter = new LessonAdapter(entry.getValue(), context, teacherListener, audienceListener);

                    RecyclerViewContainer recyclerView = recyclerViewContainerHashMap.get(entry.getKey());
                    recyclerView.getRecyclerView().setAdapter(dayAdapter);
                }

                uiLoadHandler.onDataUILoaded();

                for (RecyclerViewContainer container : recyclerViewContainerHashMap.values()){
                    if(container.getRecyclerView().getAdapter() == null){
                        container.hideVisibility();
                    }
                }
            }
        });
    }
}
