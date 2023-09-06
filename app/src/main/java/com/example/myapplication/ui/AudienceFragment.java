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
import com.example.myapplication.ListenersGetter;
import com.example.myapplication.Models.Audience.Audience;
import com.example.myapplication.Models.Pair;
import com.example.myapplication.Models.Teacher;
import com.example.myapplication.PairPutter;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerViewContainer;
import com.example.myapplication.UILoadHandler;
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

    private HashMap<String, RecyclerViewContainer> recyclerViewHashMap;

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

        setViews();
        setStartData();
        setPairs();

        return root;
    }

    private void setViews(){
        audienceName = binding.AudienceName;
        corpusName = binding.CorpusName;
        addressText = binding.AddressText;

        setViewsToHide();
        setRecyclerViewHashMap();
        progressBar = binding.AwaitProgressBar;
    }

    private void setStartData(){
        audienceName.setText(MessageFormat.format("{0} аудитория", audience.name));
        corpusName.setText(MessageFormat.format("корпус {0}", audience.corpu.name));
        addressText.setText(MessageFormat.format("Адрес: {0}, {1}", audience.corpu.street.name, audience.corpu.numberOfHome));
    }

    private void setPairs() {
        PairGetter getter = new PairGetter();

        UILoadHandler uiLoadHandler = new UILoadHandler();
        PairPutter pairPutter = new PairPutter(getContext());
        ListenersGetter listenersGetter = new ListenersGetter(getView());

        try {
            getter.GetAllPairs(new ListCallback<Pair>() {
                @Override
                public void onSuccess(List<Pair> result) {
                    List<Pair> audiencePairs = result.stream().filter(pair -> pair.audience.id == audience.id).collect(Collectors.toList());
                    pairPutter.putPairs(audiencePairs, recyclerViewHashMap, listenersGetter.getTeacherListener(), null);

                    uiLoadHandler.onDataUILoaded(viewsToHide, progressBar);
                }
            });
            uiLoadHandler.setLoadUI(viewsToHide, progressBar);
        } catch (Exception e) {
            Log.d("11111", e.getMessage());
        }
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

    private void setRecyclerViewHashMap(){
        recyclerViewHashMap = new HashMap();
        recyclerViewHashMap.put("Понедельник", new RecyclerViewContainer(binding.AudienceMondayRecyclerView, binding.AudienceMondayText));
        recyclerViewHashMap.put("Вторник", new RecyclerViewContainer(binding.AudienceTuesdayRecyclerView, binding.AudienceTuesdayText));
        recyclerViewHashMap.put("Среда", new RecyclerViewContainer(binding.AudienceWednesdayRecyclerView, binding.AudienceWednesdayText));
        recyclerViewHashMap.put("Четверг", new RecyclerViewContainer(binding.AudienceThursdayRecyclerView, binding.AudienceThursdayText));
        recyclerViewHashMap.put("Пятница", new RecyclerViewContainer(binding.AudienceFridayRecyclerView, binding.AudienceFridayText));
        recyclerViewHashMap.put("Суббота", new RecyclerViewContainer(binding.AudienceSaturdayRecyclerView, binding.AudienceSaturdayText));

        for (RecyclerViewContainer recyclerView : recyclerViewHashMap.values()){
            recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }
}