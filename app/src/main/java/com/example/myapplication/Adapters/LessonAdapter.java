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
import com.example.myapplication.Models.Teacher;
import com.example.myapplication.R;
import com.example.myapplication.VerticalTextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {
    public interface OnTeacherClickListener{
        public void onTeacherClick(Teacher teacher);
    }

    private List<Pair> pairs;
    private Context context;
    private OnTeacherClickListener teacherClickListener;


    public LessonAdapter(List<Pair> pairs, Context context, OnTeacherClickListener listener){
        this.pairs = pairs;
        this.context = context;
        this.teacherClickListener = listener;
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

        DateFormat formatter = new SimpleDateFormat("dd.MM.yy");

        holder.background.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(pair.typeOfPair.color)));
        holder.lessonName.setText(pair.teacherSubject.Subject.Name.equals("") ? pair.teacherSubject.Subject.FullName : pair.teacherSubject.Subject.Name);
        holder.teacherName.setText(MessageFormat.format("{0} {1}. {2}.", pair.teacherSubject.Teacher.LastName, pair.teacherSubject.Teacher.Name.charAt(0), pair.teacherSubject.Teacher.Patronymic.charAt(0)));
        holder.audienceText.setText(MessageFormat.format("ауд. {0} к. {1}", pair.audience.name, pair.audience.corpu.name));
        holder.timeText.setText(pair.time.name);
        holder.dateBetweenText.setText(MessageFormat.format("{0}-{1}", formatter.format(pair.dateStart), formatter.format(pair.dateEnd)));
        holder.teacherName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teacherClickListener.onTeacherClick(pair.teacherSubject.Teacher);
            }
        });
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
        final TextView dateBetweenText;

        public LessonViewHolder(View itemView) {
            super(itemView);

            background = itemView.findViewById(R.id.Background);
            lessonName = itemView.findViewById(R.id.LessonName);
            teacherName = itemView.findViewById(R.id.TeacherNameTextView);
            audienceText = itemView.findViewById(R.id.AudienceText);
            timeText = itemView.findViewById(R.id.TimeText);
            dateBetweenText = itemView.findViewById(R.id.DateBetweenText);;
        }
    }
}
