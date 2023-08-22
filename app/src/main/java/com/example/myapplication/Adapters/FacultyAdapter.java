package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Audience.Group;
import com.example.myapplication.Models.Course;
import com.example.myapplication.Models.Faculty;
import com.example.myapplication.R;

import java.util.List;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.FacultyViewHolder> {
    private List<Faculty> faculties;
    private Context context;
    private CourseAdapter.OnCourseClickListener clickListener;
    private Course selectedCourse;

    public FacultyAdapter(List<Faculty> faculties, Context context, Course course, CourseAdapter.OnCourseClickListener listener){
        this.faculties = faculties;
        this.context = context;
        this.clickListener = listener;
        this.selectedCourse = course;
    }

    @NonNull
    @Override
    public FacultyAdapter.FacultyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.faculty_item, parent, false);

        return new FacultyAdapter.FacultyViewHolder(v);
    }

    public void UpdateData(){
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyAdapter.FacultyViewHolder holder, int position) {
        CourseAdapter adapter = new CourseAdapter(faculties.get(position).courses, context, selectedCourse,clickListener);

        holder.facultyText.setText(faculties.get(position).name);
        holder.coursesRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.coursesRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return faculties.size();
    }

    public static class FacultyViewHolder extends RecyclerView.ViewHolder {
        final TextView facultyText;
        final RecyclerView coursesRecyclerView;

        public FacultyViewHolder(View itemView) {
            super(itemView);
            facultyText = itemView.findViewById(R.id.FacultyText);
            coursesRecyclerView = itemView.findViewById(R.id.CourseRecyclerView);
        }
    }
}
