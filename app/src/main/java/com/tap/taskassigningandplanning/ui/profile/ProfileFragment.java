package com.tap.taskassigningandplanning.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.User;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    //FIREBASE
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private TextView tvName, tvEmail, tvBio, tvRatingPercent;
    private RatingBar ratingBar;
    private int myRating;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvBio = view.findViewById(R.id.tvBio);
        tvRatingPercent = view.findViewById(R.id.tvRatingPercent);
        ratingBar = view.findViewById(R.id.ratingBar);
        ratingBar.setEnabled(false);

        final String current_user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DocumentReference ratingRef = db.collection("Rating").document(current_user);

        ratingRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                Log.d(TAG, "onComplete: " + documentSnapshot.getData());
                int counter = documentSnapshot.getLong("counter").intValue();
                int total_rate;

                int rating_1 = documentSnapshot.getLong("rating_1").intValue();

                int rating_2 = documentSnapshot.getLong("rating_2").intValue();

                int rating_3 = documentSnapshot.getLong("rating_3").intValue();

                int rating_4 = documentSnapshot.getLong("rating_4").intValue();

                int rating_5 = documentSnapshot.getLong("rating_5").intValue();

                float weigh_average = (rating_1*1 + rating_2*2 + rating_3*3 + rating_4*4 + rating_5*5) / counter;

                tvRatingPercent.setText(String.valueOf(weigh_average));
                ratingBar.setMax(5);
                ratingBar.setRating(weigh_average);


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                final int rating = (int) v;
                myRating = (int) ratingBar.getRating();

               /* ratingRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Log.d(TAG, "onComplete: " + documentSnapshot.getData());
                        int counter = documentSnapshot.getLong("counter").intValue();
                        int total_rate;

                        int rating_1 = documentSnapshot.getLong("rating_1").intValue();

                        int rating_2 = documentSnapshot.getLong("rating_2").intValue();

                        int rating_3 = documentSnapshot.getLong("rating_3").intValue();

                        int rating_4 = documentSnapshot.getLong("rating_4").intValue();

                        int rating_5 = documentSnapshot.getLong("rating_5").intValue();

                        int weigh_average = (rating_1*1 + rating_2+2 + rating_3*3 + rating_4*4 + rating_5*5) / counter;

                        tvRatingPercent.setText(String.valueOf(weigh_average)+"%");*/



                        /*switch (rating){
                            case 1:
                                int rating_1 = documentSnapshot.getLong("rating_1").intValue();
                                total_rate = rating_1 * counter;
                                tvRatingPercent.setText(String.valueOf(total_rate)+"%");
                                break;
                            case 2:
                                int rating_2 = documentSnapshot.getLong("rating_1").intValue();
                                total_rate = rating_1 * counter;
                                tvRatingPercent.setText(String.valueOf(total_rate)+"%");
                                break;
                            case 3:
                                int rating_3 = documentSnapshot.getLong("rating_3").intValue();
                                total_rate = rating_3 * counter;
                                tvRatingPercent.setText(String.valueOf(total_rate)+"%");
                                break;
                            case 4:
                                int rating_4 = documentSnapshot.getLong("rating_3").intValue();
                                total_rate = rating_3 * counter;
                                tvRatingPercent.setText(String.valueOf(total_rate)+"%");
                                break;
                            case 5:
                                int rating_5 = documentSnapshot.getLong("rating_5").intValue();
                                total_rate = rating_5 * counter;
                                tvRatingPercent.setText(String.valueOf(total_rate)+"%");
                                break;
                        }*/

                    }
                });
            }
        });

        DocumentReference documentReference = db.collection("Users").document(current_user);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());
                    String name = (String) documentSnapshot.get("name");
                    String email = (String) documentSnapshot.get("email");

                    tvName.setText(name);
                    tvEmail.setText(email);

                } else {
                    Log.d(TAG, "Error getting info");
                }
            }
        });

        /*ratingRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                int counter = documentSnapshot.getLong("counter").intValue();
                int total_rate;
                if (documentSnapshot.exists()) {
                    if(myRating==1){
                        int rating_1 = documentSnapshot.getLong("rating_1").intValue();
                        total_rate = rating_1 * counter;
                        tvRatingPercent.setText(total_rate);
                    }
                    if(myRating==2){
                        int rating_2 = documentSnapshot.getLong("rating_2").intValue();
                        total_rate = rating_2 * counter;
                        tvRatingPercent.setText(total_rate);
                    }
                    if(myRating==3){
                        int rating_3 = documentSnapshot.getLong("rating_3").intValue();
                        total_rate = rating_3 * counter;
                        tvRatingPercent.setText(total_rate);
                    }
                    if(myRating==4){
                        int rating_4 = documentSnapshot.getLong("rating_4").intValue();
                        total_rate = rating_4 * counter;
                        tvRatingPercent.setText(total_rate);
                    }
                    if(myRating==5){
                        int rating_5 = documentSnapshot.getLong("rating_5").intValue();
                        total_rate = rating_5 * counter;
                        tvRatingPercent.setText(total_rate);
                    }
                } else {
                    Log.d(TAG, "Error getting info");
                }
            }
        });*/

        return view;
    }

}


