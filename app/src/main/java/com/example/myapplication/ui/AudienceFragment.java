package com.example.myapplication.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.Adapters.LessonAdapter;
import com.example.myapplication.HTTPRequests.PairGetter;
import com.example.myapplication.Interfaces.ListCallback;
import com.example.myapplication.Models.Audience.Audience;
import com.example.myapplication.Models.Pair;
import com.example.myapplication.Models.Teacher;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentAudienceBinding;
import com.example.myapplication.databinding.FragmentTeacherBinding;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AudienceFragment extends Fragment {

    private FragmentAudienceBinding binding;
    private View[] viewsToHide;
    private ProgressBar progressBar;
    private Audience audience;
    private TextView audienceName;
    private TextView corpusName;
    private TextView addressText;

    private HashMap<String, RecyclerView> recyclerViewHashMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            audience = (Audience) getArguments().getSerializable("Audience");
        }
        catch(Exception e){
            Log.d("1111111", e.getMessage()); //TODO
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAudienceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        audienceName = binding.AudienceName;
        corpusName = binding.CorpusName;
        addressText = binding.AddressText;

        setViewsToHide();
        progressBar = binding.AwaitProgressBar;

        recyclerViewHashMap = new HashMap<String, RecyclerView>();
        recyclerViewHashMap.put("Понедельник", binding.AudienceMondayRecyclerView);
        recyclerViewHashMap.put("Вторник", binding.AudienceTuesdayRecyclerView);
        recyclerViewHashMap.put("Среда", binding.AudienceWednesdayRecyclerView);
        recyclerViewHashMap.put("Четверг", binding.AudienceThursdayRecyclerView);
        recyclerViewHashMap.put("Пятница", binding.AudienceFridayRecyclerView);
        recyclerViewHashMap.put("Суббота", binding.AudienceSaturdayRecyclerView);

        for (RecyclerView recyclerView : recyclerViewHashMap.values()){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        audienceName.setText(MessageFormat.format("{0} аудитория", audience.name));
        corpusName.setText(MessageFormat.format("корпус {0}", audience.corpu.name));
        addressText.setText(MessageFormat.format("Адрес: {0}, {1}", audience.corpu.street.name, audience.corpu.numberOfHome));

        setPairs();

        return root;
    }

    private void setPairs(){
        PairGetter getter = new PairGetter();

        try {
            getter.GetAllPairs(new ListCallback<Pair>() {
                @Override
                public void onSuccess(List<Pair> result) {
                    Handler mHandler = new Handler(Looper.getMainLooper());

                    HashMap<String, ArrayList<Pair>> dayPairsMap = new HashMap<>();

                    List<Pair> audiencePairs = result.stream().filter(pair-> pair.audience.id == audience.id).collect(Collectors.toList());

                    for(Pair pair : audiencePairs){
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
                                LessonAdapter dayAdapter = new LessonAdapter(entry.getValue(), getContext(), null, null);

                                recyclerViewHashMap.get(entry.getKey()).setAdapter(dayAdapter);
                            }
                            onDataUILoaded();
                        }
                    });
                }
            });
            setLoadUI();
        }
        catch(Exception e){
            Log.d("11111", e.getMessage());
        }
    }

    private void setLoadUI(){
        for (View view : viewsToHide){
            view.setVisibility(View.GONE);
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    private void onDataUILoaded(){
        for (View view : viewsToHide){
            view.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    private void setViewsToHide(){
        viewsToHide = new View[]{
                binding.AudienceName,
                binding.AudienceMondayText,
                binding.AudienceTuesdayText,
                binding.AudienceWednesdayText,
                binding.AudienceThursdayText,
                binding.AudienceFridayText,
                binding.AudienceSaturdayText,
                binding.InfoLayout,
                binding.CorpusName,
                binding.AudienceImage
        };
    }
}