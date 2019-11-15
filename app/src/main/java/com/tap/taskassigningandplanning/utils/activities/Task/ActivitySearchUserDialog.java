package com.tap.taskassigningandplanning.utils.activities.Task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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
import com.tap.taskassigningandplanning.utils.activities.ActivityClickedAdapter;
import com.tap.taskassigningandplanning.utils.team.Team;

public class ActivitySearchUserDialog extends DialogFragment implements ActivityClickedAdapter.TeamListener{
    private static final String TAG = "ActivitySearchUserDialo";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ActivityClickedAdapter activityClickedAdapter;
    RecyclerView recyclerView;

    String snapshot_id;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_search_user, null);

        recyclerView = view.findViewById(R.id.recycler_view);

        Bundle bundle = getArguments();
        snapshot_id = bundle.getString("snapshot_id", "");

        setupRecyclerAdapter();

        builder.setView(view)
                .setTitle("Designate task")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }

    private void setupRecyclerAdapter(){

        Intent intent = getActivity().getIntent();
        String plan_id = intent.getExtras().getString("plan_id");
        String activity_id = intent.getExtras().getString("activity_id");

        CollectionReference id = db.collection("Team");

        Query query = id.whereEqualTo("plan_id", plan_id).orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Team> options = new FirestoreRecyclerOptions.Builder<Team>()
                .setQuery(query, Team.class)
                .build();
        activityClickedAdapter = new ActivityClickedAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(activityClickedAdapter);
        activityClickedAdapter.startListening();

    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
        String id = documentSnapshot.getId();
        Log.d(TAG, "onItemClick: " + id);

        Intent intent = getActivity().getIntent();
        final String activity_id = intent.getExtras().getString("activity_id");

        final DocumentReference docRef = db.collection("Team").document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    String user_id = (String) document.get("user_id");
                    String name = (String) document.get("name");

                    // Add user to activity
                    docRef.update("activity_id", FieldValue.arrayUnion(activity_id));

                    DocumentReference activityRef = db.collection("Activity").document(activity_id);
                    activityRef.update("user_id", FieldValue.arrayUnion(user_id));

                    // Add user to task
                    DocumentReference taskRef = db.collection("Task").document(snapshot_id);
                    Log.d(TAG, "onComplete: " + snapshot_id);
                    taskRef.update("user_id", FieldValue.arrayUnion(user_id));

                    getDialog().dismiss();


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
