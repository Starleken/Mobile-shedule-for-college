package com.example.myapplication.ui.gallery;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.CourseAdapter;
import com.example.myapplication.Adapters.FacultyAdapter;
import com.example.myapplication.Adapters.GroupAdapter;
import com.example.myapplication.Adapters.LessonAdapter;
import com.example.myapplication.HTTPRequests.CollegeGetter;
import com.example.myapplication.Interfaces.CollegeCallback;
import com.example.myapplication.Models.Audience.Group;
import com.example.myapplication.Models.College;
import com.example.myapplication.Models.Course;
import com.example.myapplication.Models.Faculty;
import com.example.myapplication.TestGetGroup;
import com.example.myapplication.databinding.FragmentGalleryBinding;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {
    private RecyclerView groupRecyclerView;
    private RecyclerView facultyRecyclerView;

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        groupRecyclerView = binding.GroupRecyclerView;
        facultyRecyclerView = binding.FacultyRecyclerView;

        setFaculties();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setFaculties(){
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        GroupAdapter.OnGroupClickListener groupClickListener = new GroupAdapter.OnGroupClickListener() {
            @Override
            public void OnGroupClick(Group group) {
                TestGetGroup.group = group;
                Log.d("1111", group.name);
            }
        };
        CourseAdapter.OnCourseClickListener courseClickListener = new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                groupRecyclerView.setLayoutManager(horizontalLayoutManager);
                GroupAdapter groupAdapter = new GroupAdapter(course.groups, getContext(), groupClickListener);
                groupRecyclerView.setAdapter(groupAdapter);
            }
        };


        try {
            Handler mHandler = new Handler(Looper.getMainLooper());
            CollegeGetter collegeGetter = new CollegeGetter();
            collegeGetter.GetAll(new CollegeCallback() {
                @Override
                public void OnSuccess(List<College> colleges) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            facultyRecyclerView.setLayoutManager(verticalLayoutManager);
                            FacultyAdapter facultyAdapter = new FacultyAdapter(colleges.get(0).faculties, getContext(), courseClickListener);
                            facultyRecyclerView.setAdapter(facultyAdapter);
                        }
                    });
                }
            });
        }
        catch(Exception e){
            Log.d("DDDD", e.getMessage());
        }
    }
}