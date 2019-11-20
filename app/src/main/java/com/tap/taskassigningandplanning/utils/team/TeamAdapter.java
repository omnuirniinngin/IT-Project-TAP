package com.tap.taskassigningandplanning.utils.team;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tap.taskassigningandplanning.R;

public class TeamAdapter extends FirestoreRecyclerAdapter <Team, TeamAdapter.TeamHolder> {
    private static final String TAG = "TeamAdapter";

    private TeamListener listener;

    public TeamAdapter(@NonNull FirestoreRecyclerOptions<Team> options, TeamListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final TeamHolder holder, int position, @NonNull Team team) {
        holder.tvName.setText(team.getName());
        holder.tvStatus.setText(team.getRequest());
        holder.tvTaskCompleted.setText(String.valueOf(team.getTask_completed()));
        holder.tvTotalActivity.setText(String.valueOf(team.getTotal_activity()));
    }

    @NonNull
    @Override
    public TeamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_list_team, parent, false);

        return new TeamHolder(view);
    }

    class TeamHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvStatus, tvTaskCompleted, tvTotalActivity;

        public TeamHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTaskCompleted = itemView.findViewById(R.id.tvTaskCompleted);
            tvTotalActivity = itemView.findViewById(R.id.tvTotalActivity);

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

    public interface TeamListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
}