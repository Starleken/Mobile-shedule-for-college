package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.LessonAdapter;
import com.example.myapplication.Adapters.SearchableAdapter;
import com.example.myapplication.Cache;
import com.example.myapplication.HTTPRequests.GroupGetter;
import com.example.myapplication.HTTPRequests.PairGetter;
import com.example.myapplication.Interfaces.ElementCallback;
import com.example.myapplication.Interfaces.ListCallback;
import com.example.myapplication.Models.Audience.Group;
import com.example.myapplication.Models.Pair;
import com.example.myapplication.Models.Teacher;
import com.example.myapplication.R;
import com.example.myapplication.TestGetGroup;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.ui.TeacherFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private View[] viewsToHide;

    private HashMap<String, RecyclerView> recyclerViewHashMap;

    private ProgressBar progressBar;

    private AutoCompleteTextView searchTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setViewsToHide();

        recyclerViewHashMap = new HashMap<String, RecyclerView>();

        recyclerViewHashMap.put("Понедельник", binding.MondayRecyclerView);
        recyclerViewHashMap.put("Вторник", binding.TuesdayRecyclerView);
        recyclerViewHashMap.put("Среда", binding.WednesdayRecyclerView);
        recyclerViewHashMap.put("Четверг", binding.ThursdayRecyclerView);
        recyclerViewHashMap.put("Пятница", binding.FridayRecyclerView);
        recyclerViewHashMap.put("Суббота", binding.SaturdayRecyclerView);

        for (RecyclerView recyclerView : recyclerViewHashMap.values()){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        progressBar = binding.AwaitProgressBar;

        searchTextView = binding.autoCompleteTextView;

        try {
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
        catch(Exception e){
            Log.d("1111111111",e.getMessage());
        }

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
        LessonAdapter.OnTeacherClickListener listener = new LessonAdapter.OnTeacherClickListener() {
            @Override
            public void onTeacherClick(Teacher teacher) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Teacher", teacher);
                Navigation.findNavController(getView()).navigate(R.id.teacherFragment, bundle);
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
                                LessonAdapter dayAdapter = new LessonAdapter(entry.getValue(), getContext(), listener);

                                recyclerViewHashMap.get(entry.getKey()).setAdapter(dayAdapter);
                            }
                            searchTextView.setText(null);

                            onDataUILoaded();
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
}