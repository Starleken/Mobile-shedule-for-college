package com.example.myapplication.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Interfaces.ISearchable;
import com.example.myapplication.Models.Teacher;
import com.example.myapplication.R;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchableAdapter extends BaseAdapter implements Filterable {
    private List<Teacher> allTeachers;
    private List<Teacher> teachers;
    private Context context;

    public SearchableAdapter(List<Teacher> teachers, Context context){
        Log.d("TeachersSize", MessageFormat.format("GG {0}", teachers.size()));
        this.teachers = new ArrayList<>(teachers);
        this.allTeachers = new ArrayList<>(teachers);
        this.context = context;
    }

    @Override
    public int getCount() {
        return teachers.size();
    }

    @Override
    public Teacher getItem(int i) {
        return teachers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        try {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.searchable_item, viewGroup, false);
            }
            Teacher teacher = getItem(i);
            TextView text = (TextView) view.findViewById(R.id.SearchableText);
            text.setText(teacher.GetText());
        } catch (Exception e) {
            Log.d("11111111111", e.getMessage());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((Teacher) resultValue).GetText();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<Teacher> teachersSuggestion = new ArrayList<>();
                if (constraint != null) {
                    for (Teacher teacher : teachers) {
                        if (teacher.GetText().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            teachersSuggestion.add(teacher);
                        }
                    }
                    filterResults.values = teachersSuggestion;
                    filterResults.count = teachersSuggestion.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                teachers.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    for (Object object : (List<?>) results.values) {
                        if (object instanceof Teacher) {
                            teachers.add((Teacher) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    teachers.addAll(allTeachers);
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
