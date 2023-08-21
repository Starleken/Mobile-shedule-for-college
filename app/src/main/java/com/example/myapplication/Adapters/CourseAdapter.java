package com.example.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Course;
import com.example.myapplication.Models.Faculty;
import com.example.myapplication.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder>{

    public interface OnCourseClickListener{
        void onCourseClick(Course course);
    }

    public int selectedPosition = 0;

    private final OnCourseClickListener onClickListener;

    private List<Course> courses;
    private Context context;
    public CourseAdapter(List<Course> courses, Context context, OnCourseClickListener clickListener){
        this.courses = courses;
        this.context = context;
        this.onClickListener = clickListener;
    }

    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false);

        return new CourseAdapter.CourseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        Course course = courses.get(position);

        holder.groupText.setText(Integer.toString(course.name));
        Log.d("11111", Integer.toString(selectedPosition));
        int colorId = selectedPosition == course.id ? R.color.CollegeGreen : R.color.purple_500;
        holder.background.setBackgroundTintList(ContextCompat.getColorStateList(context, colorId));
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView background;
        final TextView groupText;

        public CourseViewHolder(View itemView) {
            super(itemView);

            background = itemView.findViewById(R.id.GroupBackground);
            groupText = itemView.findViewById(R.id.GroupText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            notifyItemChanged(selectedPosition);
            selectedPosition = courses.get(getAdapterPosition()).id;
            notifyItemChanged(selectedPosition);
            onClickListener.onCourseClick(courses.get(getAdapterPosition()));
        }
    }
}
