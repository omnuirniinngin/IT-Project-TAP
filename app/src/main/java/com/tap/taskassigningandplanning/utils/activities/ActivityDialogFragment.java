package com.tap.taskassigningandplanning.utils.activities;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Adapter;
//import android.widget.EditText;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.Query;
//import com.tap.taskassigningandplanning.R;
//
//public class ActivityDialogFragment extends Fragment implements ActivityCustomDialog.fabSelected{
//    private static final String TAG = "ActivityDialogFragment";
//
//    //firebase firestore
//    private FirebaseFirestore db;
//    private CollectionReference activityRef;
//    private FirebaseAuth mAuth;
//
//    private ActivitiesAdapter adapter;
//
//    private EditText etActivityName;
//
//    private FloatingActionButton fab;
//
//    View mParentLayout;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_activities, container, false);
//
//        etActivityName = view.findViewById(R.id.etActivityName);
//
////        //Firebase get instance
////        activityRef = db.collection("Activity");
////        mAuth = FirebaseAuth.getInstance();
////        db = FirebaseFirestore.getInstance();
//
//        fab = view.findViewById(R.id.fab);
//
////        setupRecyclerView();
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: Opening dialog activity");
//
//                ActivityCustomDialog dialog = new ActivityCustomDialog();
//                dialog.setTargetFragment(ActivityDialogFragment.this, 1);
//                dialog.show(getFragmentManager(), "ActivityCustomDialog");
//            }
//        });
//
//        return view;
//    }
//
////    private void setupRecyclerView(){
////        Query query = activityRef.orderBy("priority", Query.Direction.DESCENDING);
////
////        FirestoreRecyclerOptions<Activities> options = new FirestoreRecyclerOptions.Builder<Activities>()
////                .setQuery(query, Activities.class)
////                .build();
////
////        adapter = new ActivitiesAdapter(options);
////
////        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view_activities);
////        recyclerView.setHasFixedSize(true);
////        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
////        recyclerView.setAdapter(adapter);
////    }
//
////    @Override
////    public void onStart() {
////        super.onStart();
////        adapter.startListening();
////    }
////
////    @Override
////    public void onStop() {
////        super.onStop();
////        adapter.stopListening();
////    }
//
//    @Override
//    public void sendInput(String input) {
//        Log.d(TAG, "sendInput: Found incoming input" + input);
//
//        etActivityName.setText(input);
//    }
//
//    private void makeSnackBarMessage(String message){
//        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
//    }
//}


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.tap.taskassigningandplanning.Login;
import com.tap.taskassigningandplanning.MainActivity;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;

public class ActivityDialogFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "ActivityDialogFragment";

    //firebase firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private CollectionReference activityRef = db.collection(("Activity"));
//    private FirebaseAuth mAuth;

    private ActivitiesAdapter activitiesAdapter;
    private RecyclerView recyclerView;

    private FloatingActionButton fab;

    View mParentLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_activities);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        setupRecyclerView();
        return view;
    }

    private void setupRecyclerView(){
        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();

        Bundle id_result = activity.getMyId();
        final String myValue = id_result.getString("plan_id");

        Query query = db.collection("Activity").whereEqualTo("plan_id", myValue).orderBy("dateStart", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Activities> options = new FirestoreRecyclerOptions.Builder<Activities>()
                .setQuery(query, Activities.class)
                .build();

        activitiesAdapter = new ActivitiesAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(activitiesAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        activitiesAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        activitiesAdapter.stopListening();
    }

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

}