package com.tap.taskassigningandplanning.ui.plan_joined;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    protected void onBindViewHolder(@NonNull final PlanHolder holder, int position, @NonNull Team team) {
        holder.tvTitle.setText(team.getPlan_name());
        holder.tvFrom.setText(team.getCreator());

        FirebaseFirestore.getInstance().collection("Plan")
                .whereEqualTo("plan_id", team.getPlan_id())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "onComplete: " + document.getData());
                                holder.tvDateStart.setText(document.get("dateStart").toString());
                                holder.tvDateEnd.setText(document.get("dateEnd").toString());
                            }
                        }
                    }
                });
    }

    @NonNull
    @Override
    public PlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_row_plan, parent, false);
        return new PlanHolder(view);
    }

    class PlanHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvFrom, tvDateStart, tvDateEnd;

        public PlanHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvDateStart = itemView.findViewById(R.id.tvDateStart);
            tvDateEnd = itemView.findViewById(R.id.tvDateEnd);

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
