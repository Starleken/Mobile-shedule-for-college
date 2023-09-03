package com.example.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Audience.Audience;
import com.example.myapplication.Models.Pair;
import com.example.myapplication.Models.Teacher;
import com.example.myapplication.R;
import com.example.myapplication.VerticalTextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {
    public interface OnTeacherClickListener{
        public void onTeacherClick(Teacher teacher);
    }
    public interface OnAudienceClickListener{
        public void onAudienceClick(Audience audience);
    }

    private List<Pair> pairs;
    private Context context;
    private OnTeacherClickListener teacherClickListener;
    private OnAudienceClickListener audienceClickListener;


    public LessonAdapter(List<Pair> pairs, Context context, OnTeacherClickListener teacherListener, OnAudienceClickListener audienceListener){
        this.pairs = pairs;

        Collections.sort(this.pairs, new Comparator<Pair>() {
            @Override
            public int compare(Pair pair, Pair t1) {
                int firstDate = Integer.parseInt(pair.time.name.split(":")[0]);
                int secondtDate = Integer.parseInt(t1.time.name.split(":")[0]);
                return Integer.compare(firstDate, secondtDate);
            }
        });

        this.context = context;
        this.teacherClickListener = teacherListener;
        this.audienceClickListener = audienceListener;
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

        String colorString = getColorStringByTheme(pair);
        holder.background.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorString)));
        holder.lessonName.setText(pair.teacherSubject.Subject.Name.equals("") ? pair.teacherSubject.Subject.FullName : pair.teacherSubject.Subject.Name);
        holder.teacherName.setText(MessageFormat.format("{0} {1}. {2}.", pair.teacherSubject.Teacher.LastName, pair.teacherSubject.Teacher.Name.charAt(0), pair.teacherSubject.Teacher.Patronymic.charAt(0)));
        holder.audienceText.setText(MessageFormat.format("ауд. {0} к. {1}", pair.audience.name, pair.audience.corpu.name));
        holder.timeText.setText(pair.time.name);
        holder.dateBetweenText.setText(MessageFormat.format("{0}-{1}", formatter.format(pair.dateStart), formatter.format(pair.dateEnd)));

        if (teacherClickListener != null){
            holder.teacherName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    teacherClickListener.onTeacherClick(pair.teacherSubject.Teacher);
                }
            });
        }

        if (audienceClickListener != null){
            holder.audienceText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    audienceClickListener.onAudienceClick(pair.audience);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return pairs.size();
    }

    private String getColorStringByTheme(Pair pair){
        int nightModeFlags =  context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES){
            return MessageFormat.format("#BF{0}", pair.typeOfPair.color.substring(1));
        }
        else return pair.typeOfPair.color;
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
