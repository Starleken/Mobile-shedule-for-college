package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.LessonAdapter;
import com.example.myapplication.HTTPRequests.StudyGetter;
import com.example.myapplication.HTTPRequests.TeacherGetter;
import com.example.myapplication.Models.Lesson;
import com.example.myapplication.Models.Study;
import com.example.myapplication.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        TeacherGetter getter = new TeacherGetter();
        Study study;
        try {
             getter.GetAll();
        }
        catch(Exception e){
            Log.d("GGGGG", e.getMessage());
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState)
        ;
        RecyclerView lessonsRecyclerView = binding.MondayRecyclerView;
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Lesson> lessons = new ArrayList<Lesson>();
        lessons.add(new Lesson("sdvsdvsd"));
        lessons.add(new Lesson("sdvsdvsd"));
        lessons.add(new Lesson("sdvsdvsd"));

        LessonAdapter adapter = new LessonAdapter(lessons, getContext());

        lessonsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}