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
import android.widget.TextView;

import com.example.myapplication.Adapters.LessonAdapter;
import com.example.myapplication.HTTPRequests.PairGetter;
import com.example.myapplication.Interfaces.ListCallback;
import com.example.myapplication.Models.Pair;
import com.example.myapplication.Models.Teacher;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
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
    private Teacher teacher;

    private ImageView teacherImage;
    private TextView teacherName;

    private RecyclerView mondayRecyclerView;
    private RecyclerView tuesdayRecyclerView;
    private RecyclerView wednesdayRecyclerView;
    private RecyclerView thursdayRecyclerView;
    private RecyclerView fridayRecyclerView;
    private RecyclerView saturdayRecyclerView;

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

        mondayRecyclerView = binding.TeacherMondayRecyclerView;
        mondayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tuesdayRecyclerView = binding.TeacherTuesdayRecyclerView;
        tuesdayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        wednesdayRecyclerView = binding.TeacherWednesdayRecyclerView;
        wednesdayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        thursdayRecyclerView = binding.TeacherThursdayRecyclerView;
        thursdayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fridayRecyclerView = binding.TeacherFridayRecyclerView;
        fridayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        saturdayRecyclerView = binding.TeacherSaturdayRecyclerView;
        saturdayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        teacherName.setText(MessageFormat.format("{0} {1} {2}", teacher.LastName, teacher.Name , teacher.Patronymic));

        setPairs();

        return root;
    }

    private void setPairs(){
        PairGetter getter = new PairGetter();

        try {
            Picasso.get().load(MessageFormat.format("http://185.250.44.61:5002/avatars/{0}", teacher.Image)).into(teacherImage);
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
                                LessonAdapter dayAdapter = new LessonAdapter(entry.getValue(), getContext(), null);

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
                        }
                    });
                }
            });
        }
        catch(Exception e){
            Log.d("11111", e.getMessage());
        }

    }
}