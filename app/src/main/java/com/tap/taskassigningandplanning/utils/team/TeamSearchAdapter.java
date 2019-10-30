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

public class TeamSearchAdapter extends FirestoreRecyclerAdapter <Team, TeamSearchAdapter.TeamViewHolder> {

    private TeamListener listener;

    public TeamSearchAdapter(@NonNull FirestoreRecyclerOptions<Team> options) {
        super(options);
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_row_designate, parent, false);
        return new TeamSearchAdapter.TeamViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull TeamViewHolder holder, int position, @NonNull Team team) {
        holder.tvName.setText(team.getName());
    }

    class TeamViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);

        }
    }

    public interface TeamListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
}
