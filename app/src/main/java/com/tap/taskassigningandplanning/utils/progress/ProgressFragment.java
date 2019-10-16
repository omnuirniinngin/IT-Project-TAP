package com.tap.taskassigningandplanning.utils.progress;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.ui.plan.Plan;
import com.tap.taskassigningandplanning.utils.activities.Activities;
import com.tap.taskassigningandplanning.utils.activities.ActivitiesAdapter;

public class ProgressFragment extends Fragment implements ActivitiesAdapter.ActivitiesListener{

    private static final String TAG = "ProgressFragment";

//    firebase firestore
    private FirebaseFirestore db;

    private ActivitiesAdapter activitiesAdapter;
//    private ProgressAdapter progressAdapter;
    private RecyclerView recyclerView;

    TextView tvTitle, tvDaysLeftPlan;

    private ProgressViewModel progressViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        progressViewModel = ViewModelProviders.of(this).get(ProgressViewModel.class);

        tvTitle = view.findViewById(R.id.tvTitle);
        tvDaysLeftPlan = view.findViewById(R.id.tvDaysLeftPlan);

        recyclerView = view.findViewById(R.id.recycler_view);

        db = FirebaseFirestore.getInstance();

        getPlan();
        setupRecyclerView();
        return view;
    }

    private void getPlan(){

        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
        Bundle id_result = activity.getPlanId();
        final String plan_id = id_result.getString("plan_id");

        final DocumentReference documentReference = db.collection("Plan").document(plan_id);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        Log.d(TAG, "onComplete: Success getting plan" + documentSnapshot.getData());
                        Plan plan = documentSnapshot.toObject(Plan.class);
                        tvTitle.setText(plan.getTitle());

                    }else {
                        Log.d(TAG, "No such document");
                    }
                }else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void setupRecyclerView(){
        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();

        Bundle id_result = activity.getPlanId();
        final String plan_id = id_result.getString("plan_id");

        Query query = FirebaseFirestore.getInstance()
                .collection("Activity")
                .whereEqualTo("plan_id", plan_id)
                .orderBy("dateStart", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Activities> options = new FirestoreRecyclerOptions.Builder<Activities>()
                .setQuery(query, Activities.class)
                .build();
        activitiesAdapter = new ActivitiesAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(activitiesAdapter);
        activitiesAdapter.startListening();

//        FirestoreRecyclerOptions<Activities> options = new FirestoreRecyclerOptions.Builder<Activities>()
//                .setQuery(query, Activities.class)
//                .build();
//        progressAdapter = new ProgressAdapter(options,this);
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(progressAdapter);
//        progressAdapter.startListening();

    }

    @Override
    public void handleDeleteItem(DocumentSnapshot snapshot) {

    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

    }
}