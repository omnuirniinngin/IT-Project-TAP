package com.tap.taskassigningandplanning.utils.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.tap.taskassigningandplanning.R;

public class ActivitiesAdapter extends FirestoreRecyclerAdapter <Activities, ActivitiesAdapter.ActivityHolder> {

    public ActivitiesAdapter(@NonNull FirestoreRecyclerOptions<Activities> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ActivityHolder holder, int position, @NonNull Activities activities) {
        holder.tvTitle.setText(activities.getTitle());
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_activities, parent, false);
        return new ActivityHolder(view);
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.layout_list_activities, parent, false);
//        return new ActivityHolder(view);
    }

    class ActivityHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;

        public ActivityHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
