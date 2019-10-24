package com.tap.taskassigningandplanning.utils.progress;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.utils.activities.Activities;

public class ProgressAdapter extends FirestoreRecyclerAdapter <Activities, ProgressAdapter.ProgressHolder> {
    private static final String TAG = "ProgressAdapter";

    private ProgressList listener;

    public ProgressAdapter(@NonNull FirestoreRecyclerOptions<Activities> options, ProgressList listener) {
        super(options);
        this.listener = listener;
    }
    @Override
    public ProgressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_list_progress, parent, false);
        return new ProgressHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProgressHolder holder, int position, @NonNull Activities progress) {
        holder.tvTitle.setText(progress.getTitle());
    }


    class ProgressHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvPercent, tvDaysLeftPlan;

        public ProgressHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvActivityTitle);
            tvDaysLeftPlan = itemView.findViewById(R.id.tvDaysLeftPlan);
            tvPercent = itemView.findViewById(R.id.tvPercent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface ProgressList{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
}
