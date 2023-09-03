package com.example.myapplication.ui;

import android.graphics.drawable.Drawable;
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
import com.example.myapplication.Models.Pair;
import com.example.myapplication.Models.Teacher;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerViewContainer;
import com.example.myapplication.databinding.FragmentTeacherBinding;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeacherFragment extends Fragment {

    private FragmentTeacherBinding binding;

    private View[] viewsToHide;
    private ProgressBar progressBar;

    private Teacher teacher;

    private ImageView teacherImage;
    private TextView teacherName;

    private HashMap<String, RecyclerViewContainer> recyclerViewHashMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            teacher = (Teacher) getArguments().getSerializable("Teacher");
        }
        catch(Exception e){
            Log.d("1111111", e.getMessage()); //TODO
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTeacherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        teacherImage = binding.TeacherImage;
        teacherName = binding.TeacherName;

        setViewsToHide();
        progressBar = binding.AwaitProgressBar;

        recyclerViewHashMap = new HashMap();
        recyclerViewHashMap.put("Понедельник", new RecyclerViewContainer(binding.TeacherMondayRecyclerView, binding.TeacherMondayText));
        recyclerViewHashMap.put("Вторник", new RecyclerViewContainer(binding.TeacherTuesdayRecyclerView, binding.TeacherTuesdayText));
        recyclerViewHashMap.put("Среда", new RecyclerViewContainer(binding.TeacherWednesdayRecyclerView, binding.TeacherWednesdayText));
        recyclerViewHashMap.put("Четверг", new RecyclerViewContainer(binding.TeacherThursdayRecyclerView, binding.TeacherThursdayText));
        recyclerViewHashMap.put("Пятница", new RecyclerViewContainer(binding.TeacherFridayRecyclerView, binding.TeacherFridayText));
        recyclerViewHashMap.put("Суббота", new RecyclerViewContainer(binding.TeacherSaturdayRecyclerView, binding.TeacherSaturdayText));

        for (RecyclerViewContainer recyclerView : recyclerViewHashMap.values()){
            recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));
        }

        teacherName.setText(MessageFormat.format("{0} {1} {2}", teacher.LastName, teacher.Name , teacher.Patronymic));

        setPairs();

        return root;
    }

    private void setPairs(){
        PairGetter getter = new PairGetter();

        try {
            Picasso.get().load(MessageFormat.format("http://185.250.44.61:5002/avatars/{0}", teacher.Image)).error(R.drawable.teacher_photo).into(teacherImage);
            getter.GetAllPairs(new ListCallback<Pair>() {
                @Override
                public void onSuccess(List<Pair> result) {
                    Handler mHandler = new Handler(Looper.getMainLooper());

                    HashMap<String, ArrayList<Pair>> dayPairsMap = new HashMap<>();

                    List<Pair> teacherPairs = result.stream().filter(pair-> pair.teacherSubject.Teacher.id == teacher.id).collect(Collectors.toList());

                    for(Pair pair : teacherPairs){
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

                                recyclerViewHashMap.get(entry.getKey()).getRecyclerView().setAdapter(dayAdapter);
                            }
                            onDataUILoaded();

                            for (RecyclerViewContainer container : recyclerViewHashMap.values()){
                                if(container.getRecyclerView().getAdapter() == null){
                                    container.HideVisibility();
                                }
                            }
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
                binding.TeacherImage,
                binding.TeacherName,
                binding.TeacherMondayText,
                binding.TeacherTuesdayText,
                binding.TeacherWednesdayText,
                binding.TeacherThursdayText,
                binding.TeacherFridayText,
                binding.TeacherSaturdayText
        };
    }
}