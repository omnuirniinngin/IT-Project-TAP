package com.tap.taskassigningandplanning.utils.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.utils.team.Team;

public class ActivityClickedSearch extends AppCompatActivity implements ActivityClickedAdapter.TeamListener{

    private static final String TAG = "ActivityClickedSearch";

    //firebase firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ActivityClickedAdapter activityClickedAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        recyclerView = findViewById(R.id.recycler_view);

        setupRecyclerAdapter();

    }

    private void setupRecyclerAdapter(){

        Intent intent = getIntent();
        String plan_id = intent.getExtras().getString("plan_id");
        String activity_id = intent.getExtras().getString("activity_id");

        Log.d(TAG, "Plan id is: " + plan_id);
        Log.d(TAG, "Activity id is: " + activity_id);

        CollectionReference id = db.collection("Team");

        Query query = id.whereEqualTo("plan_id", plan_id).orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Team> options = new FirestoreRecyclerOptions.Builder<Team>()
                .setQuery(query, Team.class)
                .build();
        activityClickedAdapter = new ActivityClickedAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(activityClickedAdapter);
        activityClickedAdapter.startListening();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
        String id = documentSnapshot.getId();
        Log.d(TAG, "onItemClick: " + id);

        Intent intent = getIntent();
        final String activity_id = intent.getExtras().getString("activity_id");

        final DocumentReference docRef = db.collection("Team").document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "Document data: " + document.getData());
                    String user_id = (String) document.get("user_id");
                    String name = (String) document.get("name");

                    docRef.update("activity_id", FieldValue.arrayUnion(activity_id));

                    DocumentReference userRef = db.collection("Activity").document(activity_id);
                    userRef.update("user_id", FieldValue.arrayUnion(user_id));
                    Snackbar.make(recyclerView, "Task successfully designated.", Snackbar.LENGTH_LONG).show();


                } else {
                    Log.d(TAG, "Failed to retrieve: ", task.getException());
                }
            }
        });

    }

    @Override
    public void handleDeleteItem(DocumentSnapshot snapshot) {

    }

}
