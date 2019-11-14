package com.tap.taskassigningandplanning.utils.activities.Task;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.utils.activities.Activities;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class ActivityTask extends AppCompatActivity implements View.OnClickListener, ActivityTaskAdapter.TaskListener {

    private static final String TAG = "ActivityTask";

    //Firebase Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;

    //Initialize
    ActivityTaskAdapter activityTaskAdapter;
    Intent intent;
    String plan_id, activity_id, title;

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

        DocumentReference documentReference = db.collection("Activity").document(activity_id);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Activities activities = documentSnapshot.toObject(Activities.class);
                        getSupportActionBar().setTitle(activities.getTitle());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                }else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        fab.setOnClickListener(this);

        setupRecyclerView();
        setResult(Activity.RESULT_OK);
    }
    private void setupRecyclerView(){
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = FirebaseFirestore.getInstance()
                .collection("Task")
                .whereEqualTo("activity_id", activity_id)
                .whereArrayContains("user_id", user_id)
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
    public void handleCheckChanged(final boolean isChecked, final DocumentSnapshot snapshot) {

        snapshot.getReference().update("completed", isChecked)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        final DocumentReference documentReference = db.collection("Activity").document(activity_id);

                        // Get tasks where equal to activity_id where completed is true
                        db.collection("Task")
                                .whereEqualTo("activity_id", activity_id)
                                .whereEqualTo("completed", true)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            int count = 0;
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                count++;
                                            }
                                            documentReference.update("completed_task", count);
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                        // Get tasks where equal to activity_id
                        db.collection("Task")
                                .whereEqualTo("activity_id", activity_id)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            int count = 0;
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                count++;
                                            }
                                            documentReference.update("total_task", count);
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });


                        // Get tasks where equal to activity_id where completed false
                        db.collection("Task")
                                .whereEqualTo("activity_id", activity_id)
                                .whereEqualTo("completed", false)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            QuerySnapshot document = task.getResult();
                                            if (document.isEmpty()) {
                                                Log.d(TAG, "onComplete: Document is empty");
                                                //Update activity_completed
                                                documentReference.update("completed", true);

                                            } else {
                                                Log.d(TAG, "Document exist");
                                                documentReference.update("completed", false);
                                            }
                                        } else {
                                            Log.d(TAG, "Error getting documents: ");
                                        }
                                    }
                                });

//                        // Get team where plan_id is equal to plan_id then update task_completed
//                        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                        db.collection("Team")
//                                .whereEqualTo("plan_id", plan_id)
//                                .whereEqualTo("user_id", user_id)
//                                .get()
//                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            if (task.isSuccessful()) {
//                                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                                    String team_id = document.getId();
//                                                    int completed = document.getLong("completed").intValue();
//                                                    int total = completed + 1;
//
//                                                    if(isChecked == true){
//                                                        DocumentReference teamRef = db.collection("Team").document(team_id);
//                                                        teamRef.update("completed", total);
//                                                    }
//                                                }
//
//                                            } else {
//                                                Log.d(TAG, "Error getting documents: ", task.getException());
//                                            }
//                                        }
//                                    }
//                                });
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
        String title = task.getTitle();
        DocumentReference documentReference = snapshot.getReference();
        String snapshot_id = documentReference.getId();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("snapshot_id", snapshot_id);

        ActivityTaskCustomEditDialog activityTaskCustomEditDialog = new ActivityTaskCustomEditDialog();
        activityTaskCustomEditDialog.setArguments(bundle);
        activityTaskCustomEditDialog.show(getSupportFragmentManager(), "Update Task");
    }

    @Override
    public void handleDelete(final DocumentSnapshot snapshot) {
        final DocumentReference documentReference = snapshot.getReference();
        final DocumentReference docActivity = db.collection("Activity").document(activity_id);
        final Task task = snapshot.toObject(Task.class);

        new AlertDialog.Builder(this)
                .setTitle("Alert!")
                .setMessage("Do you wish to delete this task?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        snapshot.getReference().delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        docActivity.get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot snapshot) {
                                                        int total_task = snapshot.getLong("total_task").intValue();
                                                        int new_total_task = total_task - 1;
                                                        docActivity.update("total_task", new_total_task);
                                                    }
                                                });
                                    }
                                });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    @Override
    public void handleDesignateUser(DocumentSnapshot snapshot) {
        DocumentReference documentReference = snapshot.getReference();
        String snapshot_id = documentReference.getId();
        Bundle bundle = new Bundle();
        bundle.putString("snapshot_id", snapshot_id);

        ActivitySearchUserDialog activitySearchUserDialog = new ActivitySearchUserDialog();
        activitySearchUserDialog.setArguments(bundle);
        activitySearchUserDialog.show(getSupportFragmentManager(), "Designate Task");
    }

}
