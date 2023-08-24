package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.LessonAdapter;
import com.example.myapplication.HTTPRequests.GroupGetter;
import com.example.myapplication.HTTPRequests.PairGetter;
import com.example.myapplication.Interfaces.ElementCallback;
import com.example.myapplication.Interfaces.ListCallback;
import com.example.myapplication.Models.Audience.Group;
import com.example.myapplication.Models.Pair;
import com.example.myapplication.TestGetGroup;
import com.example.myapplication.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView mondayRecyclerView;
    private RecyclerView tuesdayRecyclerView;
    private RecyclerView wednesdayRecyclerView;
    private RecyclerView thursdayRecyclerView;
    private RecyclerView fridayRecyclerView;
    private RecyclerView saturdayRecyclerView;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mondayRecyclerView = binding.MondayRecyclerView;
        mondayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tuesdayRecyclerView = binding.TuesdayRecyclerView;
        tuesdayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        wednesdayRecyclerView = binding.WednesdayRecyclerView;
        wednesdayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        thursdayRecyclerView = binding.ThursdayRecyclerView;
        thursdayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fridayRecyclerView = binding.FridayRecyclerView;
        fridayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        saturdayRecyclerView = binding.SaturdayRecyclerView;
        saturdayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressBar = binding.AwaitProgressBar;

        setGroup();
        SetPairs();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void SetPairs(){
        PairGetter getter = new PairGetter();
        try {
            getter.GetGroupPairs(TestGetGroup.selectedGroupId,new ListCallback<Pair>() {
                @Override
                public void onSuccess(List<Pair> pairs) {
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
                                LessonAdapter dayAdapter = new LessonAdapter(entry.getValue(), getContext());

                                if (entry.getKey().equals("Понедельник")){
                                    mondayRecyclerView.setAdapter(dayAdapter);
                                }
                                else if(entry.getKey().equals("Вторник")){
                                    tuesdayRecyclerView.setAdapter(dayAdapter);
                                }
                                else if(entry.getKey().equals("Среда")){
                                    wednesdayRecyclerView.setAdapter(dayAdapter);
                                }
                                else if(entry.getKey().equals("Четверг")){
                                    thursdayRecyclerView.setAdapter(dayAdapter);
                                }
                                else if(entry.getKey().equals("Пятница")){
                                    fridayRecyclerView.setAdapter(dayAdapter);
                                }
                                else if(entry.getKey().equals("Суббота")){
                                    saturdayRecyclerView.setAdapter(dayAdapter);
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
        catch(Exception e){
            Log.d("GGGGG", e.getMessage());
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setGroup(){
        GroupGetter getter = new GroupGetter();
        try {
            getter.GetGroupById(TestGetGroup.selectedGroupId, new ElementCallback<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.ScheduleText.setText(group.name);
                        }
                    });
                }
            });
        }
        catch(Exception e){
            Log.d("GGGGG", e.getMessage());
        }
    }
}