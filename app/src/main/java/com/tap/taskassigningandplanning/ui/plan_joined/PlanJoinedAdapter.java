package com.tap.taskassigningandplanning.ui.plan_joined;

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
import com.tap.taskassigningandplanning.ui.plan.Plan;
import com.tap.taskassigningandplanning.utils.team.Team;

public class PlanJoinedAdapter extends FirestoreRecyclerAdapter <Team, PlanJoinedAdapter.PlanHolder> {
    private static final String TAG = "PlanJoinedAdapter";

    PlanListener listener;

    public PlanJoinedAdapter(@NonNull FirestoreRecyclerOptions<Team> options, PlanListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull PlanHolder holder, int position, @NonNull Team team) {
        holder.tvTitle.setText(team.getPlan_name());
    }

    @NonNull
    @Override
    public PlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_row_plan, parent, false);
        return new PlanHolder(view);
    }

    class PlanHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;

        public PlanHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);

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

    public interface PlanListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
}
