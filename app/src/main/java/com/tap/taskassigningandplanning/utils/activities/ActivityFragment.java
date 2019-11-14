package com.tap.taskassigningandplanning.utils.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;

import java.util.Calendar;

public class ActivityFragment extends Fragment implements View.OnClickListener, ActivitiesAdapter.ActivitiesListener {
    private static final String TAG = "ActivityDialogFragment";

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSetListener;

    //firebase firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ActivitiesAdapter activitiesAdapter;
    private RecyclerView recyclerView;

    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        fab = view.findViewById(R.id.fab);

        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
        Bundle id_result = activity.getPlanId();
        final String plan_id = id_result.getString("plan_id");

        db.collection("Plan")
                .whereEqualTo("plan_id", plan_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()){
                                String creator = (String) document.get("user_id");

                                if (!creator.equals(user_id)) {
                                    fab.setEnabled(false);
                                } else if (creator.equals(user_id)){
                                    fab.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ActivityCustomDialog dialog = new ActivityCustomDialog();
                                            dialog.setTargetFragment(ActivityFragment.this, 1);
                                            dialog.show(getFragmentManager(), "ActivityCustomDialog");
                                        }
                                    });
                                }
                            }
                        }
                    }
                });

//        fab.setOnClickListener(this);

        setupRecyclerView();
//        setCountActivity();
        return view;
    }

//    private void setCountActivity(){
//        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
//        Bundle id_result = activity.getPlanId();
//        final String plan_id = id_result.getString("plan_id");
//
//        db.collection("Activity")
//                .whereEqualTo("plan_id", plan_id)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            int count = 0;
//                            for (final QueryDocumentSnapshot document : task.getResult()) {
//                                count++;
//                                boolean completed;
//                                completed = (boolean) document.get("completed");
//                            }
//                            final int new_count = count;
//                            db.collection("Plan")
//                                    .whereEqualTo("plan_id", plan_id)
//                                    .get()
//                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                            if (task.isSuccessful()) {
//                                                String plan_document_id = "";
//                                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                                    plan_document_id = document.getId();
//                                                }
//                                                DocumentReference documentReference = db.collection("Plan").document(plan_document_id);
//                                                documentReference.update("total_activity", new_count);
//                                            }
//                                        }
//                                    });
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }

    private void setupRecyclerView(){
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
        Bundle id_result = activity.getPlanId();
        final String plan_id = id_result.getString("plan_id");

        Query query = FirebaseFirestore.getInstance()
                .collection("Activity")
                .whereEqualTo("plan_id", plan_id)
                .whereArrayContains("user_id", user_id)
                .orderBy("dateEnd", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Activities> options = new FirestoreRecyclerOptions.Builder<Activities>()
                .setQuery(query, Activities.class)
                .build();
        activitiesAdapter = new ActivitiesAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(activitiesAdapter);
        activitiesAdapter.startListening();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                Log.d(TAG, "onClick: Opening dialog activity");
                ActivityCustomDialog dialog = new ActivityCustomDialog();
                dialog.setTargetFragment(ActivityFragment.this, 1);
                dialog.show(getFragmentManager(), "ActivityCustomDialog");
                break;
        }
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
        Intent intent;
        intent = getActivity().getIntent();

        String plan_id = intent.getExtras().getString("plan_id");
        String id = documentSnapshot.getId();

        intent = new Intent(getContext(), ActivityEdit.class);
        intent.putExtra("plan_id", plan_id);
        intent.putExtra("activity_id", id);
        startActivity(intent);
    }

    @Override
    public void handleDelete(final DocumentSnapshot snapshot) {

        new AlertDialog.Builder(getContext())
                .setTitle("Alert!")
                .setMessage("Do you wish to delete this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
                        Bundle id_result = activity.getPlanId();
                        final String plan_id = id_result.getString("plan_id");

                        final DocumentReference documentReference = snapshot.getReference();
                        final Activities activities = snapshot.toObject(Activities.class);
                        final String activity_id = documentReference.getId();

                        documentReference.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: Item deleted: " + activity_id);

                                        db.collection("Team")
                                                .whereArrayContains("activity_id", activity_id)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                String Team = document.getId();
                                                                DocumentReference docTeam = db.collection("Team").document(Team);
                                                                docTeam.update("activity_id", FieldValue.arrayRemove(activity_id));
                                                            }
                                                        } else {
                                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                                        }
                                                    }
                                                });

                                        db.collection("Task")
                                                .whereEqualTo("activity_id", activity_id)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                String Task = document.getId();
                                                                db.collection("Task").document(Task)
                                                                        .delete()
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                                            }
                                                                        });
                                                            }
                                                        }
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
                }).show();
    }
}