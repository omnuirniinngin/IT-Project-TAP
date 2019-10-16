package com.tap.taskassigningandplanning.utils.team;

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

public class TeamAdapter extends FirestoreRecyclerAdapter <Team, TeamAdapter.TeamHolder> {
    private static final String TAG = "TeamAdapter";

    private TeamListener listener;

    public TeamAdapter(@NonNull FirestoreRecyclerOptions<Team> options, TeamListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull TeamHolder holder, int position, @NonNull Team team) {
        //Temporary holder
        holder.tvName.setText(team.getEmail());
    }

    @NonNull
    @Override
    public TeamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_list_team, parent, false);
        return new TeamHolder(view);
    }

    class TeamHolder extends RecyclerView.ViewHolder{

        TextView tvName;

        public TeamHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);

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