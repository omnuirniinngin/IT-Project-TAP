package com.tap.taskassigningandplanning.utils.activities;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tap.taskassigningandplanning.R;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class ActivitiesAdapter extends FirestoreRecyclerAdapter <Activities, ActivitiesAdapter.ActivityHolder> {

    private static final String TAG = "ActivitiesAdapter";
    ActivitiesListener activitiesListener;

    public ActivitiesAdapter(@NonNull FirestoreRecyclerOptions<Activities> options, ActivitiesListener activitiesListener) {
        super(options);
        this.activitiesListener = activitiesListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ActivityHolder holder, int position, @NonNull Activities activities) {
        holder.tvTitle.setText(activities.getTitle());
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_activities, parent, false);
//        return new ActivityHolder(view);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_list_activities, parent, false);
        return  new ActivityHolder(view);
    }

    class ActivityHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        public ActivityHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View view) {
                    Log.d(TAG, "onClick: Clicked item");
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
//                    activitiesListener.handleDeleteItem(snapshot);
                    activitiesListener.handleEditActivity(snapshot);
            }
        });
        }
        public void deleteItem(){
//            Log.d(TAG, "deleteItem: " + getAdapterPosition());
//            Log.d(TAG, "deleteItem: " + getSnapshots().getSnapshot(getAdapterPosition()));
            activitiesListener.handleDeleteItem(getSnapshots().getSnapshot(getAdapterPosition()));
        }
    }

    interface ActivitiesListener{
        public void handleDeleteItem(DocumentSnapshot snapshot);
        public void handleEditActivity(DocumentSnapshot snapshot);
    }


}
