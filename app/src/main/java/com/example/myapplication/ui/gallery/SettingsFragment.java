package com.example.myapplication.ui.gallery;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.CourseAdapter;
import com.example.myapplication.Adapters.FacultyAdapter;
import com.example.myapplication.Adapters.GroupAdapter;
import com.example.myapplication.Cache;
import com.example.myapplication.Models.Audience.Group;
import com.example.myapplication.Models.Course;
import com.example.myapplication.TestGetGroup;
import com.example.myapplication.databinding.FragmentSettingsBinding;

import java.util.List;

public class SettingsFragment extends Fragment {
    private RecyclerView groupRecyclerView;
    private RecyclerView facultyRecyclerView;
    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setSwitchByTheme();
        settingSwitch();

        setViews();

        setFaculties();

        return root;
    }

    private void setViews(){
        groupRecyclerView = binding.GroupRecyclerView;
        facultyRecyclerView = binding.FacultyRecyclerView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setFaculties(){
        TestGetGroup getGroup = new TestGetGroup();

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        CourseAdapter.OnCourseClickListener courseClickListener = new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                if (TestGetGroup.selectedCourseId != course.id){
                    getGroup.SaveGroup(-1, getContext());
                }
                getGroup.SaveCourse(course.id, getContext());

                ((FacultyAdapter)facultyRecyclerView.getAdapter()).UpdateData();
                setGroups(course.groups);
            }
        };

        try {
            Handler mHandler = new Handler(Looper.getMainLooper());

            facultyRecyclerView.setLayoutManager(verticalLayoutManager);
                            FacultyAdapter facultyAdapter = new FacultyAdapter(Cache.colleges.get(0).faculties, getContext(), TestGetGroup.selectedCourseId, courseClickListener);
                            facultyRecyclerView.setAdapter(facultyAdapter);

            setGroups(Cache.courses.stream().filter(course -> course.id == TestGetGroup.selectedCourseId).findFirst().get().groups);
        }
        catch(Exception e){
            Log.d("DDDD", e.getMessage());
        }
    }

    private void setGroups(List<Group> groups){
        TestGetGroup getGroup = new TestGetGroup();
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        GroupAdapter.OnGroupClickListener groupClickListener = new GroupAdapter.OnGroupClickListener() {
            @Override
            public void OnGroupClick(Group group) {
                getGroup.SaveGroup(group.id, getContext());
            }
        };

        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                groupRecyclerView.setLayoutManager(horizontalLayoutManager);
                GroupAdapter groupAdapter = new GroupAdapter(groups, getContext(), groupClickListener);
                groupRecyclerView.setAdapter(groupAdapter);
            }
        });


    }

    private void settingSwitch(){
        binding.switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    private void setSwitchByTheme(){
        int themeMask = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (themeMask == Configuration.UI_MODE_NIGHT_YES) {
            binding.switch3.setChecked(true);
        }
    }
}