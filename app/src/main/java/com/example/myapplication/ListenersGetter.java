package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;

import com.example.myapplication.Adapters.CourseAdapter;
import com.example.myapplication.Adapters.FacultyAdapter;
import com.example.myapplication.Adapters.LessonAdapter;
import com.example.myapplication.Models.Audience.Audience;
import com.example.myapplication.Models.Course;
import com.example.myapplication.Models.Teacher;

public class ListenersGetter {
    private View view;
    public ListenersGetter(View view){
        this.view = view;
    }

    public LessonAdapter.OnTeacherClickListener getTeacherListener(){
        return new LessonAdapter.OnTeacherClickListener() {
            @Override
            public void onTeacherClick(Teacher teacher) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Teacher", teacher);
                Navigation.findNavController(view).navigate(R.id.teacherFragment, bundle);
            }
        };
    }

    public LessonAdapter.OnAudienceClickListener getAudienceListener(){
        return new LessonAdapter.OnAudienceClickListener() {
            @Override
            public void onAudienceClick(Audience audience) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Audience", audience);
                Navigation.findNavController(view).navigate(R.id.audienceFragment, bundle);
            }
        };
    }
}
