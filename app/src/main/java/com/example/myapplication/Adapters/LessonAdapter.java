package com.example.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Pair;
import com.example.myapplication.R;
import com.example.myapplication.VerticalTextView;

import org.w3c.dom.Text;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {
    private List<Pair> pairs;
    private Context context;
    public LessonAdapter(List<Pair> pairs, Context context){
        this.pairs = pairs;
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
        Pair pair = pairs.get(position);
        holder.background.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pair.typeOfPair.color)));
        holder.lessonName.setText(pair.teacherSubject.Subject.Name);
        holder.teacherName.setText(MessageFormat.format("{0} {1}. {2}.", pair.teacherSubject.Teacher.LastName, pair.teacherSubject.Teacher.Name.charAt(0), pair.teacherSubject.Teacher.Patronymic.charAt(0)));
        holder.audienceText.setText(MessageFormat.format("ауд. {0} к. {1}", pair.audience.name, pair.audience.corpu.name));
        holder.timeText.setText(pair.time.name);
    }

    @Override
    public int getItemCount() {
        return pairs.size();
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        final ImageView background;
        final TextView lessonName;
        final TextView teacherName;
        final TextView audienceText;
        final VerticalTextView timeText;

        public LessonViewHolder(View itemView) {
            super(itemView);

            background = itemView.findViewById(R.id.Background);
            lessonName = itemView.findViewById(R.id.LessonName);
            teacherName = itemView.findViewById(R.id.TeacherNameTextView);
            audienceText = itemView.findViewById(R.id.AudienceText);
            timeText = itemView.findViewById(R.id.TimeText);
        }
    }
}
