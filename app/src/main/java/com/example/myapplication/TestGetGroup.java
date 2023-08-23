package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.Models.Course;

import java.io.FileOutputStream;

public class TestGetGroup {
    static public int selectedGroupId;
    static public int selectedCourseId;

    private String fileKey = "Settings";
    private String groupKey = "groupId";
    private String courseKey = "courseId";
    private FileOutputStream outputStream;

    public void SaveGroup(int groupId, Context context){
        selectedGroupId = groupId;


//        SharedPreferences sharedPref = context.getSharedPreferences(fileKey,Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt(groupKey, selectedGroupId);

//        editor.commit();
    }

    public void SaveCourse(int courseId, Context context){
        selectedCourseId = courseId;

        SharedPreferences sharedPref = context.getSharedPreferences(fileKey,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(courseKey, selectedCourseId);

        editor.commit();
    }

    public void LoadData(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(fileKey,Context.MODE_PRIVATE);
        selectedCourseId = sharedPref.getInt(courseKey, -1);
        selectedGroupId = sharedPref.getInt(groupKey, -1);
    }
}
