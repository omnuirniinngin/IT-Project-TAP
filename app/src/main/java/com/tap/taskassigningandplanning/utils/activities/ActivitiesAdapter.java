package com.tap.taskassigningandplanning.utils.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tap.taskassigningandplanning.R;

public class ActivitiesAdapter extends FirestoreRecyclerAdapter <Activities, ActivitiesAdapter.ActivityHolder> {

    private static final String TAG = "ActivitiesAdapter";
    private ActivitiesListener listener;

    public ActivitiesAdapter(@NonNull FirestoreRecyclerOptions<Activities> options, ActivitiesListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_list_activities, parent, false);
        return new ActivityHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ActivityHolder holder, int position, @NonNull Activities activities) {
        holder.tvTitle.setText(activities.getTitle());
        holder.tvNotes.setText(activities.getNotes());

    }

    class ActivityHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvNotes;
        ImageButton btnDelete;

        public ActivityHolder(final View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvNotes = itemView.findViewById(R.id.tvNotes);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    listener.handleDelete(snapshot);
                }
            });
        }

    }

    public interface ActivitiesListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void handleDelete(DocumentSnapshot snapshot);
    }
}
