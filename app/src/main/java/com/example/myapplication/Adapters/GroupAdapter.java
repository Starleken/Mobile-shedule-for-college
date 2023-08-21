package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Audience.Group;
import com.example.myapplication.Models.Pair;
import com.example.myapplication.R;
import com.example.myapplication.VerticalTextView;

import org.w3c.dom.Text;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    public interface OnGroupClickListener{
        public void OnGroupClick(Group group);
    }

    private OnGroupClickListener onGroupClickListener;
    private List<Group> groups;
    private Context context;
    public GroupAdapter(List<Group> groups, Context context, OnGroupClickListener listener){
        this.groups = groups;
        this.context = context;
        this.onGroupClickListener = listener;
    }

    @NonNull
    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false);

        return new GroupAdapter.GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.groupText.setText(group.name);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView groupText;
        public GroupViewHolder(View itemView)
        {
            super(itemView);
            groupText = itemView.findViewById(R.id.GroupText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onGroupClickListener.OnGroupClick(groups.get(getAdapterPosition()));
        }
    }
}
