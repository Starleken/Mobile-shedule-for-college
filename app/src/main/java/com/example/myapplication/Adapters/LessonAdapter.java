package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Lesson;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {
    private ArrayList<Lesson> lessons;
    private Context context;
    public LessonAdapter(ArrayList<Lesson> lessons, Context context){
        this.lessons = lessons;
        this.context = context;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.lesson_item, parent, false);

        return new LessonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        holder.lessonName.setText("Название лекции");
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        final TextView lessonName;

        public LessonViewHolder(View itemView) {
            super(itemView);

            lessonName = itemView.findViewById(R.id.LessonName);
        }
    }
}
