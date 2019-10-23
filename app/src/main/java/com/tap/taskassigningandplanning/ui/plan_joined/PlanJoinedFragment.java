package com.tap.taskassigningandplanning.ui.plan_joined;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.User;
import com.tap.taskassigningandplanning.ui.plan.Plan;

import java.util.HashMap;
import java.util.List;

public class PlanJoinedFragment extends Fragment implements PlanJoinedAdapter.PlanListener{
    private static final String TAG = "PlanJoinedFragment";

    //Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    //Recycler && Adapter
    private PlanJoinedAdapter planJoinedAdapter;
    private RecyclerView recyclerView;

    private TextView tvName, tvActivities;
    private List list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_joined, container, false);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recycler_view);
        tvName = view.findViewById(R.id.tvName);


        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView(){
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // User must accept first the invitation before he can view these plans on plan fragment screen but how?

        CollectionReference planRef = db.collection("Plan");

        planRef.whereArrayContains("team", userId);
        db.collection("Plan").whereArrayContains("team", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    }
                });



        Query query = FirebaseFirestore.getInstance()
                .collection("Plan")
                .whereArrayContains("team", userId)
                .orderBy("plan_id", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Plan> options = new FirestoreRecyclerOptions.Builder<Plan>()
                .setQuery(query, Plan.class)
                .build();
        planJoinedAdapter = new PlanJoinedAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(planJoinedAdapter);
        planJoinedAdapter.startListening();

        /*db.collection("Team").whereEqualTo("user_id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String plan_id = (String) document.get("plan_id");
                                Log.d(TAG, "Your plan id is: " + plan_id);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });*/

        /*docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        final List<Object> plan_id = (List<Object>) document.get("plan_id");
                        for (Object o : plan_id) {
                            Log.d(TAG, "List element " + o.toString());
                            DocumentReference docRef = db.collection("Plan").document(o.toString());

                            Query query = FirebaseFirestore.getInstance()
                                    .collection("Plan")
                                    .whereEqualTo("plan_id", o.toString())
                                    .orderBy("title", Query.Direction.ASCENDING);

                            FirestoreRecyclerOptions<PlanJoined> options = new FirestoreRecyclerOptions.Builder<PlanJoined>()
                                    .setQuery(query, PlanJoined.class)
                                    .build();
                            planJoinedAdapter = new PlanJoinedAdapter(options);

                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(planJoinedAdapter);
                            planJoinedAdapter.startListening();

//                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    if (task.isSuccessful()) {
//                                        DocumentSnapshot document = task.getResult();
//                                        if (document.exists()) {
//                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//
//                                            Query query = FirebaseFirestore.getInstance()
//                                                    .collection("Plan")
//                                                    .whereEqualTo("plan_id", o.toString())
//                                                    .orderBy("title", Query.Direction.ASCENDING);
//
//                                            FirestoreRecyclerOptions<PlanJoined> options = new FirestoreRecyclerOptions.Builder<PlanJoined>()
//                                                    .setQuery(query, PlanJoined.class)
//                                                    .build();
//                                            planJoinedAdapter = new PlanJoinedAdapter(options);
//
//                                            recyclerView.setHasFixedSize(true);
//                                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                                            recyclerView.setAdapter(planJoinedAdapter);
//                                            planJoinedAdapter.startListening();
//
//
//                                        } else {
//                                            Log.d(TAG, "No such document");
//                                        }
//                                    } else {
//                                        Log.d(TAG, "get failed with ", task.getException());
//                                    }
//                                }
//                            });


//                            Query query = FirebaseFirestore.getInstance()
//                                    .collection("Users")
//                                    .whereEqualTo("plan_id", o.toString())
//                                    .orderBy("name", Query.Direction.ASCENDING);
//
//                            Log.d(TAG, "Query: " + query);
//
//
//                            FirestoreRecyclerOptions<PlanJoined> options = new FirestoreRecyclerOptions.Builder<PlanJoined>()
//                                    .setQuery(query, PlanJoined.class)
//                                    .build();
//                            planJoinedAdapter = new PlanJoinedAdapter(options);
//
//                            recyclerView.setHasFixedSize(true);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                            recyclerView.setAdapter(planJoinedAdapter);
//                            planJoinedAdapter.startListening();
                        }
//                        String plan_id =  document.get("plan_id").toString();
//                        Log.d(TAG, "***Found field: " + plan_id);
                    } else {
                        Log.d(TAG, "***No such document");
                    }
                } else {
                    Log.d(TAG, "***Get failed with ", task.getException());
                }
            }
        });*/

    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

    }
}
