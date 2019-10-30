/*
package com.tap.taskassigningandplanning.utils.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;

import java.util.Calendar;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ActivityDialogFragment extends Fragment implements View.OnClickListener, ActivitiesAdapter.ActivitiesListener {
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
        fab.setOnClickListener(this);

        setupRecyclerView();
        return view;
    }

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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if(direction == ItemTouchHelper.LEFT){
                Toast.makeText(getContext(), "Deleting...", Toast.LENGTH_SHORT).show();

                ActivitiesAdapter.ActivityHolder activityHolder = (ActivitiesAdapter.ActivityHolder) viewHolder;
                activityHolder.deleteItem();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                    .addActionIcon(R.drawable.ic_delete_sweep_black_24dp)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                Log.d(TAG, "onClick: Opening dialog activity");
                ActivityCustomDialog dialog = new ActivityCustomDialog();
                dialog.setTargetFragment(ActivityDialogFragment.this, 1);
                dialog.show(getFragmentManager(), "ActivityCustomDialog");
                break;
        }
    }

    @Override
    public void handleDeleteItem(DocumentSnapshot snapshot) {

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
                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                               String Team = document.getId();
                                               DocumentReference docTeam = db.collection("Team").document(Team);

                                               docTeam.update("activity_id", FieldValue.arrayRemove(activity_id));
                                            }
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    }
                });
        Snackbar.make(recyclerView, "Task successfully deleted.", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        documentReference.set(activities);

                        db.collection("Team")
                                .whereEqualTo("plan_id", plan_id)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                                String Team = document.getId();
                                                DocumentReference docTeam = db.collection("Team").document(Team);

                                                docTeam.update("activity_id", FieldValue.arrayUnion(activity_id));
                                            }
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                    }
                })
                .show();

    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
        Intent intent;
        intent = getActivity().getIntent();

        String plan_id = intent.getExtras().getString("plan_id");
        String id = documentSnapshot.getId();

        intent = new Intent(getContext(), ActivityClicked.class);
        intent.putExtra("plan_id", plan_id);
        intent.putExtra("activity_id", id);
        startActivity(intent);
    }
}*/
