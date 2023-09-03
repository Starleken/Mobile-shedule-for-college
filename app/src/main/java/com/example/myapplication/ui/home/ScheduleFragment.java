package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.LessonAdapter;
import com.example.myapplication.Adapters.SearchableAdapter;
import com.example.myapplication.Cache;
import com.example.myapplication.HTTPRequests.CollegeGetter;
import com.example.myapplication.HTTPRequests.CourseGetter;
import com.example.myapplication.HTTPRequests.GroupGetter;
import com.example.myapplication.HTTPRequests.PairGetter;
import com.example.myapplication.HTTPRequests.TeacherGetter;
import com.example.myapplication.Interfaces.ElementCallback;
import com.example.myapplication.Interfaces.ListCallback;
import com.example.myapplication.Models.Audience.Audience;
import com.example.myapplication.Models.Audience.Group;
import com.example.myapplication.Models.College;
import com.example.myapplication.Models.Course;
import com.example.myapplication.Models.Pair;
import com.example.myapplication.Models.Teacher;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerViewContainer;
import com.example.myapplication.TestGetGroup;
import com.example.myapplication.databinding.FragmentScheduleBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleFragment extends Fragment {

    private FragmentScheduleBinding binding;

    private View[] viewsToHide;

    private HashMap<String, RecyclerViewContainer> recyclerViewHashMap;

    private ProgressBar progressBar;

    private AutoCompleteTextView searchTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setViewsToHide();

        recyclerViewHashMap = new HashMap<String, RecyclerViewContainer>();

        recyclerViewHashMap.put("Понедельник", new RecyclerViewContainer(binding.MondayRecyclerView, binding.MondayText));
        recyclerViewHashMap.put("Вторник", new RecyclerViewContainer(binding.TuesdayRecyclerView, binding.TuesdayText));
        recyclerViewHashMap.put("Среда", new RecyclerViewContainer(binding.WednesdayRecyclerView, binding.WednesdayText));
        recyclerViewHashMap.put("Четверг", new RecyclerViewContainer(binding.ThursdayRecyclerView, binding.ThursdayText));
        recyclerViewHashMap.put("Пятница", new RecyclerViewContainer(binding.FridayRecyclerView, binding.FridayText));
        recyclerViewHashMap.put("Суббота", new RecyclerViewContainer(binding.SaturdayRecyclerView, binding.SaturdayText));

        for (RecyclerViewContainer recyclerViewContainer : recyclerViewHashMap.values()){
            recyclerViewContainer.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));
        }

        progressBar = binding.AwaitProgressBar;

        searchTextView = binding.autoCompleteTextView;

        TestGetGroup getGroup = new TestGetGroup();
        getGroup.LoadData(getContext());

        getCache();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setGroup();
        SetPairs();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void SetPairs(){
        LessonAdapter.OnTeacherClickListener teacherListener = new LessonAdapter.OnTeacherClickListener() {
            @Override
            public void onTeacherClick(Teacher teacher) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Teacher", teacher);
                Navigation.findNavController(getView()).navigate(R.id.teacherFragment, bundle);
            }
        };
        LessonAdapter.OnAudienceClickListener audienceListener = new LessonAdapter.OnAudienceClickListener() {
            @Override
            public void onAudienceClick(Audience audience) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Audience", audience);
                Navigation.findNavController(getView()).navigate(R.id.audienceFragment, bundle);
            }
        };

        PairGetter getter = new PairGetter();
        try {
            setLoadUI();

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
                                LessonAdapter dayAdapter = new LessonAdapter(entry.getValue(), getContext(), teacherListener, audienceListener);

                                RecyclerViewContainer recyclerView = recyclerViewHashMap.get(entry.getKey());
                                recyclerView.getRecyclerView().setAdapter(dayAdapter);
                            }
                            searchTextView.setText(null);

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
        }
        catch(Exception e){
            Log.d("GGGGG", e.getMessage());
        }
    }

    private void setGroup(){
        GroupGetter getter = new GroupGetter();
        try {
            if(TestGetGroup.selectedGroupId == -1){
                binding.ScheduleText.setText("Выберите группу");
            }

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
                binding.ScheduleText,
                binding.MondayText,
                binding.TuesdayText,
                binding.WednesdayText,
                binding.ThursdayText,
                binding.FridayText,
                binding.SaturdayText,
                binding.autoCompleteTextView
        };
    }

    public void getCache(){
        CollegeGetter collegeGetter = new CollegeGetter();
        try {
            collegeGetter.GetAll(new ListCallback<College>() {
                @Override
                public void onSuccess(List<College> result) {
                    Cache.colleges = result;
                }
            });
            CourseGetter courseGetter = new CourseGetter();
            courseGetter.GetAll(new ListCallback<Course>() {
                @Override
                public void onSuccess(List<Course> result) {
                    Cache.courses = result;
                }
            });
            TeacherGetter getter = new TeacherGetter();
            getter.GetAll(new ListCallback<Teacher>() {
                @Override
                public void onSuccess(List<Teacher> result) {
                    Cache.teachers = result;
                    setSearching();
                }
            });
        }
        catch(Exception e){
            Log.d("11111", e.getMessage());
        }

    }

    private void setSearching(){
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    SearchableAdapter adapter = new SearchableAdapter(Cache.teachers, getContext());
                    searchTextView.setAdapter(adapter);
                    searchTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Teacher", adapter.getItem(i));
                            Navigation.findNavController(getView()).navigate(R.id.teacherFragment, bundle);
                        }
                    });
                }
            });
        }
        catch(Exception e){
            Log.d("1111111111",e.getMessage());
        }
    }
}