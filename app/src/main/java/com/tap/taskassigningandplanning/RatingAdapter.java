package com.tap.taskassigningandplanning;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tap.taskassigningandplanning.utils.team.Team;

public class RatingAdapter extends FirestoreRecyclerAdapter<Team, RatingAdapter.RatingHolder> {
    private static final String TAG = "RatingAdapter";

    RatingListener listener;

    public RatingAdapter(@NonNull FirestoreRecyclerOptions<Team> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RatingHolder holder, int position, @NonNull Team team) {
        holder.tvName.setText(team.getName());
    }

    @NonNull
    @Override
    public RatingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_list_rate, parent, false);
        return new RatingHolder(view);
    }

    class RatingHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        RatingBar ratingBar;

        public RatingHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    int rating = (int) v;
                }
            });
        }
    }

    public interface RatingListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void handleProgressChanged(int ratingChanged, DocumentSnapshot snapshot);
    }
}
