package com.tap.taskassigningandplanning;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tap.taskassigningandplanning.utils.team.Team;

public class Rating extends AppCompatActivity implements RatingAdapter.RatingListener{

    private RecyclerView recyclerView;
    private RatingAdapter ratingAdapter;

    //FirebaseFirestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;

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
        ratingAdapter = new RatingAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ratingAdapter);
        ratingAdapter.startListening();

    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

    }

    @Override
    public void handleProgressChanged(int ratingChanged, DocumentSnapshot documentSnapshot) {
        documentSnapshot.getReference().update("rating", ratingChanged);
    }
}
