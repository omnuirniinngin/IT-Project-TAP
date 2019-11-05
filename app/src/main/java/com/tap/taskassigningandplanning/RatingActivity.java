package com.tap.taskassigningandplanning;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tap.taskassigningandplanning.utils.team.Team;

public class RatingActivity extends AppCompatActivity implements RatingAdapter.RatingListener{
    private static final String TAG = "RatingActivity";

    private RecyclerView recyclerView;
    private RatingAdapter ratingAdapter;

    //FirebaseFirestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        getSupportActionBar().setTitle("Rate");

        recyclerView = findViewById(R.id.recycler_view);

        setupRecyclerView();
    }

    private void setupRecyclerView(){
        Intent intent = getIntent();
        String activity_id = intent.getExtras().getString("activity_id");

        CollectionReference id = db.collection("Team");

        Query query = id.whereArrayContains("activity_id", activity_id).orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Team> options = new FirestoreRecyclerOptions.Builder<Team>()
                .setQuery(query, Team.class)
                .build();
        ratingAdapter = new RatingAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ratingAdapter);
        ratingAdapter.startListening();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rate_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_done:
                Toast.makeText(RatingActivity.this, "Thank you for rating!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

    }

    @Override
    public void handleRateChanged(final int ratingChanged, DocumentSnapshot documentSnapshot) {
//        documentSnapshot.getReference().update("rating", ratingChanged);
        DocumentReference documentReference = documentSnapshot.getReference();

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        Log.d(TAG, "onSuccess, user_id is: " + snapshot.get("user_id"));
                        String user_id = (String) snapshot.get("user_id");

                        final DocumentReference userRating = db.collection("Rating").document(user_id);

                        userRating.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        Log.d(TAG, "onSuccess Users Data: " + snapshot.getData());
                                        int rate_counter = snapshot.getLong("counter").intValue();
                                        int new_counter = rate_counter + 1;

                                        userRating.update("counter", new_counter);

                                        if(ratingChanged==1){
                                            int rating_1 = snapshot.getLong("rating_1").intValue();
                                            int rating = rating_1 + 1 ;
                                            userRating.update("rating_1", rating);
                                        }
                                        if(ratingChanged==2){
                                            int rating_2 = snapshot.getLong("rating_2").intValue();
                                            int rating = rating_2 + 1;
                                            userRating.update("rating_2", rating);
                                        }
                                        if(ratingChanged==3){
                                            int rating_3 = snapshot.getLong("rating_3").intValue();
                                            int rating = rating_3 + 1;
                                            userRating.update("rating_3", rating);
                                        }
                                        if(ratingChanged==4){
                                            int rating_4 = snapshot.getLong("rating_4").intValue();
                                            int rating = rating_4 + 1;
                                            userRating.update("rating_4", rating);
                                        }
                                        if(ratingChanged==5){
                                            int rating_5 = snapshot.getLong("rating_5").intValue();
                                            int rating = rating_5 + 1;
                                            userRating.update("rating_5", rating);
                                        }
                                    }
                                });

                    }
                });

    }
}
