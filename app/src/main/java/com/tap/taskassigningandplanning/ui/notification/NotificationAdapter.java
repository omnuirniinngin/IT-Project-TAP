package com.tap.taskassigningandplanning.ui.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.ui.plan.Plan;
import com.tap.taskassigningandplanning.utils.team.Team;

public class NotificationAdapter extends FirestoreRecyclerAdapter <Team, NotificationAdapter.NotifHolder> {

    private static final String TAG = "NotificationAdapter";

    NotifListener listener;

    public NotificationAdapter(@NonNull FirestoreRecyclerOptions<Team> options, NotifListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull NotifHolder holder, int position, @NonNull Team team) {
        holder.tvTitle.setText(team.getPlan_name());
        holder.tvFrom.setText(team.getCreator());
    }

    @NonNull
    @Override
    public NotifHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_row_request, parent, false);
        return new NotifHolder(view);
    }

    class NotifHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvFrom;

        public NotifHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvFrom = itemView.findViewById(R.id.tvFrom);

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

        public void AcceptItem(){
            listener.handleAcceptItem(getSnapshots().getSnapshot(getAdapterPosition()));
        }
        public void IgnoreItem(){
            listener.handleIgnoreItem(getSnapshots().getSnapshot(getAdapterPosition()));
        }
    }

    public interface NotifListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void handleAcceptItem(DocumentSnapshot snapshot);
        void handleIgnoreItem(DocumentSnapshot snapshot);
    }
}
