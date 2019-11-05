package com.tap.taskassigningandplanning;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tap.taskassigningandplanning.utils.team.Team;

public class RatingAdapter extends FirestoreRecyclerAdapter<Team, RatingAdapter.RatingHolder> {
    private static final String TAG = "RatingAdapter";

    RatingListener listener;

    public RatingAdapter(@NonNull FirestoreRecyclerOptions<Team> options, RatingListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull RatingHolder holder, int position, @NonNull Team team) {
        holder.tvName.setText(team.getName());

        int value = team.getRating();
        holder.ratingBar.setRating(value);
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
        Button btnSubmit;
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        int myRating;
        String user_id;

        public RatingHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            btnSubmit = itemView.findViewById(R.id.btnSubmit);


            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromTouch) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());

                    int newRating = (int) rating;
                    myRating = (int) ratingBar.getRating();
                    user_id = (String) snapshot.get("user_id");
//                    listener.handleRateChanged(newRating, snapshot);

                    /*Team team = getItem(getAdapterPosition());

                    if(team.getRating() != myRating){
                        listener.handleRateChanged(newRating, snapshot);
                    }*/
                }

            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DocumentReference userRating = db.collection("Rating").document(user_id);
                    userRating.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot snapshot) {
                                    Log.d(TAG, "onSuccess Users Data: " + snapshot.getData());
                                    int rate_counter = snapshot.getLong("counter").intValue();
                                    int new_counter = rate_counter + 1;

                                    userRating.update("counter", new_counter);

                                    if(myRating==1){
                                        int rating_1 = snapshot.getLong("rating_1").intValue();
                                        int rating = rating_1 + 1 ;
                                        userRating.update("rating_1", rating);
                                        btnSubmit.setEnabled(false);
                                    }
                                    if(myRating==2){
                                        int rating_2 = snapshot.getLong("rating_2").intValue();
                                        int rating = rating_2 + 1;
                                        userRating.update("rating_2", rating);
                                        btnSubmit.setEnabled(false);
                                    }
                                    if(myRating==3){
                                        int rating_3 = snapshot.getLong("rating_3").intValue();
                                        int rating = rating_3 + 1;
                                        userRating.update("rating_3", rating);
                                        btnSubmit.setEnabled(false);
                                    }
                                    if(myRating==4){
                                        int rating_4 = snapshot.getLong("rating_4").intValue();
                                        int rating = rating_4 + 1;
                                        userRating.update("rating_4", rating);
                                        btnSubmit.setEnabled(false);
                                    }
                                    if(myRating==5){
                                        int rating_5 = snapshot.getLong("rating_5").intValue();
                                        int rating = rating_5 + 1;
                                        btnSubmit.setEnabled(false);
                                        userRating.update("rating_5", rating);
                                    }
                                }
                            });
                }
            });
        }
    }

    public interface RatingListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void handleRateChanged(int ratingChanged, DocumentSnapshot snapshot);
    }
}
