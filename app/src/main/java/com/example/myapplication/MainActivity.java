package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.example.myapplication.HTTPRequests.CollegeGetter;
import com.example.myapplication.HTTPRequests.CourseGetter;
import com.example.myapplication.HTTPRequests.TeacherGetter;
import com.example.myapplication.Interfaces.ListCallback;
import com.example.myapplication.Models.College;
import com.example.myapplication.Models.Course;
import com.example.myapplication.Models.Teacher;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_schedule, R.id.nav_settings, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        TestGetGroup getGroup = new TestGetGroup();
        getGroup.LoadData(this);

        getCache();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
                }
            });
        }
        catch(Exception e){
            Log.d("11111", e.getMessage());
        }

    }
}