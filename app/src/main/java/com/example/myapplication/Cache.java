package com.example.myapplication;

import android.app.Application;
import android.util.Log;

import com.example.myapplication.HTTPRequests.CollegeGetter;
import com.example.myapplication.Interfaces.ListCallback;
import com.example.myapplication.Models.Audience.Group;
import com.example.myapplication.Models.College;
import com.example.myapplication.Models.Course;
import com.example.myapplication.Models.Faculty;
import com.example.myapplication.Models.Teacher;

import java.util.List;

public class Cache {

    static public List<Teacher> teachers;
    static public List<College> colleges;
    static public List<Course> courses;
}
