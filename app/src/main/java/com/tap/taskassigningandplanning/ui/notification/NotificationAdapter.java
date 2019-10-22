package com.tap.taskassigningandplanning.ui.notification;

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

public class NotificationAdapter extends FirestoreRecyclerAdapter <Plan, NotificationAdapter.NotifHolder> {

    private static final String TAG = "NotificationAdapter";



    public NotificationAdapter(@NonNull FirestoreRecyclerOptions<Plan> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotifHolder holder, int position, @NonNull Plan plan) {
        holder.tvTitle.setText(plan.getTitle());
    }

    @NonNull
    @Override
    public NotifHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_row_request, parent, false);
        return new NotifHolder(view);
    }

    class NotifHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;

        public NotifHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);

        }
    }

    public interface NotifListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
}
