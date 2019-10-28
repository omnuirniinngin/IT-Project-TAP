package com.tap.taskassigningandplanning.utils.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tap.taskassigningandplanning.R;

import java.util.ArrayList;

public class TaskParentAdapter extends RecyclerView.Adapter<TaskParentAdapter.TaskParentHolder>{

    ArrayList<String> parentArrayList;
    Context context;

    public TaskParentAdapter(ArrayList<String> parentArrayList, Context context) {
        this.parentArrayList = parentArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskParentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task_parent_recycler, parent, false);
        return new TaskParentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskParentHolder holder, int position) {
        holder.tvItem.setText(parentArrayList.get(position));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.recycler_view_child.setLayoutManager(layoutManager);
        holder.recycler_view_child.setHasFixedSize(true);
    }

    @Override
    public int getItemCount() {
        return parentArrayList.size();
    }

    class TaskParentHolder extends RecyclerView.ViewHolder{
        TextView tvItem;
        RecyclerView recycler_view_child;

        public TaskParentHolder(@NonNull View itemView) {
            super(itemView);

            tvItem = itemView.findViewById(R.id.tvItem);
            recycler_view_child = itemView.findViewById(R.id.recycler_view_child);
        }
    }
}
