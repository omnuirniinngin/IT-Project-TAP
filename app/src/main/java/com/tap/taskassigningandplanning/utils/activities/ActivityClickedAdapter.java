package com.tap.taskassigningandplanning.utils.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.utils.team.Team;

public class ActivityClickedAdapter extends FirestoreRecyclerAdapter<Team, ActivityClickedAdapter.TeamHolder> implements Filterable {

    private static final String TAG = "ActivityClickedAdapter";
    TeamListener listener;

    public ActivityClickedAdapter(@NonNull FirestoreRecyclerOptions<Team> options, TeamListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull TeamHolder holder, int position, @NonNull Team team) {
        holder.tvName.setText(team.getName());
    }

    @NonNull
    @Override
    public TeamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_row_designate, parent, false);
        return new TeamHolder(view);
    }

    @Override
    public Filter getFilter() {
        // To be continued
        return null;
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
        public void deleteItem(){
            listener.handleDeleteItem(getSnapshots().getSnapshot(getAdapterPosition()));
        }
    }

    public interface TeamListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void handleDeleteItem(DocumentSnapshot snapshot);
    }
}
