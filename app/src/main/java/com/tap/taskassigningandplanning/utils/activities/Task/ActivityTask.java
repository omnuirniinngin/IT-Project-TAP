package com.tap.taskassigningandplanning.utils.activities.Task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tap.taskassigningandplanning.R;


public class ActivityTask extends AppCompatActivity implements View.OnClickListener, ActivityTaskAdapter.TaskListener {

    private static final String TAG = "ActivityTask";

    //Firebase Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;

    //Initialize
    ActivityTaskAdapter activityTaskAdapter;
    Intent intent;
    String plan_id, activity_id;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycler_view);

        intent = getIntent();
        plan_id = intent.getExtras().getString("plan_id");
        activity_id = intent.getExtras().getString("activity_id");

        fab.setOnClickListener(this);

        setupRecyclerView();
    }
    private void setupRecyclerView(){

        Query query = FirebaseFirestore.getInstance()
                .collection("Task")
                .whereEqualTo("activity_id", activity_id)
                .orderBy("created", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();
        activityTaskAdapter = new ActivityTaskAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(activityTaskAdapter);
        activityTaskAdapter.startListening();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                openDialog();
                break;
        }
    }

    private void openDialog(){
        ActivityTaskCustomDialog activityTaskCustomDialog = new ActivityTaskCustomDialog();
        activityTaskCustomDialog.show(getSupportFragmentManager(), "Add Task");
    }

    @Override
    public void handleCheckChanged(boolean isChecked, DocumentSnapshot snapshot) {
        Log.d(TAG, "handleCheckChanged: " + isChecked);
        snapshot.getReference().update("completed", isChecked)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void handleEditTask(DocumentSnapshot snapshot) {
        Task task = snapshot.toObject(Task.class);
        String title = task.getTitle().toString();
        DocumentReference documentReference = snapshot.getReference();
        String snapshot_id = documentReference.getId();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("snapshot_id", snapshot_id);

        ActivityTaskCustomEditDialog activityTaskCustomEditDialog = new ActivityTaskCustomEditDialog();
        activityTaskCustomEditDialog.setArguments(bundle);
        activityTaskCustomEditDialog.show(getSupportFragmentManager(), "Update Task");
    }

}
