package com.tap.taskassigningandplanning.utils.team;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.User;
import com.tap.taskassigningandplanning.utils.activities.ActivitiesAdapter;
import com.tap.taskassigningandplanning.utils.activities.ActivityCustomDialog;
import com.tap.taskassigningandplanning.utils.activities.ActivityFragment;

public class TeamFragment extends Fragment implements View.OnClickListener, TeamAdapter.TeamListener{
    //, TeamAdapter.TeamListener

    private static final String TAG = "TeamFragment";

    //Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    //Recycler && Adapter
    private TeamAdapter teamAdapter;
    private RecyclerView recyclerView;

    private FloatingActionButton fab;

    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private TextView tvName;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recycler_view);
        tvName = view.findViewById(R.id.tvName);

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
                                    setupRecyclerViewMember();
                                } else if (creator.equals(user_id)){
                                    setupRecyclerView();
                                    fab.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            TeamCustomDialog dialog = new TeamCustomDialog();
                                            dialog.setTargetFragment(TeamFragment.this, 1);
                                            dialog.show(getFragmentManager(), "TeamCustomDialog");
                                        }
                                    });
                                }
                            }
                        }
                    }
                });


        updateTeamTaskCompleted();
        updateTeamTotalActivity();
        return view;
    }

    private void updateTeamTaskCompleted(){
        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
        Bundle id_result = activity.getPlanId();
        final String plan_id = id_result.getString("plan_id");
        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("Task")
                .whereEqualTo("plan_id", plan_id)
                .whereArrayContains("user_id", user_id)
                .whereEqualTo("completed", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            final int finalCount = count;
                            db.collection("Team")
                                    .whereEqualTo("plan_id", plan_id)
                                    .whereEqualTo("user_id", user_id)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    String teamID = document.getId();
                                                    DocumentReference teamRef = db.collection("Team").document(teamID);
                                                    teamRef.update("task_completed", finalCount);
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void updateTeamTotalActivity(){NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
        Bundle id_result = activity.getPlanId();
        final String plan_id = id_result.getString("plan_id");
        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("Activity")
                .whereEqualTo("plan_id", plan_id)
                .whereArrayContains("user_id", user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            final int finalCount = count;
                            db.collection("Team")
                                    .whereEqualTo("plan_id", plan_id)
                                    .whereEqualTo("user_id", user_id)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    String teamID = document.getId();
                                                    Log.d(TAG, "onComplete: " + document.getId());
                                                    DocumentReference teamRef = db.collection("Team").document(teamID);
                                                    teamRef.update("total_activity", finalCount);
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void setupRecyclerView(){
        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
        Bundle id_result = activity.getPlanId();
        final String plan_id = id_result.getString("plan_id");

        CollectionReference id = db.collection("Team");

        Query query = id.whereEqualTo("plan_id", plan_id).orderBy("email", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Team> options = new FirestoreRecyclerOptions.Builder<Team>()
                .setQuery(query, Team.class)
                .build();
        teamAdapter = new TeamAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(teamAdapter);
        teamAdapter.startListening();
    }

    private void setupRecyclerViewMember(){
        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
        Bundle id_result = activity.getPlanId();
        final String plan_id = id_result.getString("plan_id");
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference id = db.collection("Team");

        Query query = id.whereEqualTo("plan_id", plan_id).whereEqualTo("user_id", user_id).orderBy("email", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Team> options = new FirestoreRecyclerOptions.Builder<Team>()
                .setQuery(query, Team.class)
                .build();
        teamAdapter = new TeamAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(teamAdapter);
        teamAdapter.startListening();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                Log.d(TAG, "onClick: Opening dialog activity");
                TeamCustomDialog dialog = new TeamCustomDialog();
                dialog.setTargetFragment(TeamFragment.this, 1);
                dialog.show(getFragmentManager(), "TeamCustomDialog");
                break;
        }
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

    }
}