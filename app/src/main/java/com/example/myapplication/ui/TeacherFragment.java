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
import com.example.myapplication.ListenersGetter;
import com.example.myapplication.Models.Pair;
import com.example.myapplication.Models.Teacher;
import com.example.myapplication.PairPutter;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerViewContainer;
import com.example.myapplication.UILoadHandler;
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

        setViews();
        setStartData();
        setPairs();

        return root;
    }

    private void setViews(){
        teacherImage = binding.TeacherImage;
        teacherName = binding.TeacherName;

        setViewsToHide();
        setRecyclerViewHashMap();
        progressBar = binding.AwaitProgressBar;
    }

    private void setStartData(){
        Picasso.get().load(MessageFormat.format("http://185.250.44.61:5002/avatars/{0}", teacher.Image)).error(R.drawable.teacher_photo).into(teacherImage);
        teacherName.setText(MessageFormat.format("{0} {1} {2}", teacher.LastName, teacher.Name , teacher.Patronymic));
    }

    private void setPairs(){
        PairGetter getter = new PairGetter();
        UILoadHandler uiLoadHandler = new UILoadHandler();
        PairPutter pairPutter = new PairPutter(getContext());
        ListenersGetter listenersGetter = new ListenersGetter(getView());

        try {
            getter.GetAllPairs(new ListCallback<Pair>() {
                @Override
                public void onSuccess(List<Pair> result) {
                    List<Pair> teacherPairs = result.stream().filter(pair-> pair.teacherSubject.Teacher.id == teacher.id).collect(Collectors.toList());

                    pairPutter.putPairs(teacherPairs, recyclerViewHashMap, null, null);
                    uiLoadHandler.onDataUILoaded(viewsToHide, progressBar);
                }
            });
            uiLoadHandler.setLoadUI(viewsToHide, progressBar);
        }
        catch(Exception e){
            Log.d("11111", e.getMessage());
        }
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

    private void setRecyclerViewHashMap(){
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
    }
}