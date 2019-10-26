package com.tap.taskassigningandplanning.ui.plan_joined;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.utils.team.Team;

public class PlanJoinedFragment extends Fragment implements PlanJoinedAdapter.PlanListener{
    private static final String TAG = "PlanJoinedFragment";

    //Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    //Recycler && Adapter
    private PlanJoinedAdapter planJoinedAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_joined, container, false);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recycler_view);


        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView(){
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = FirebaseFirestore.getInstance()
                .collection("Team")
                .whereEqualTo("status", true).whereEqualTo("user_id", userId)
                .orderBy("created", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Team> options = new FirestoreRecyclerOptions.Builder<Team>()
                .setQuery(query, Team.class)
                .build();
        planJoinedAdapter = new PlanJoinedAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(planJoinedAdapter);
        planJoinedAdapter.startListening();

    }

    @Override
    public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {
        final
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String id = documentSnapshot.getId();
//        intent = new Intent(getContext(), NavigationBottomActivity.class);
//        startActivity(intent);

        DocumentReference docRef = db.collection("Team").document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String plan_id = (String) document.get("plan_id");
                        String plan_name = (String) document.get("plan_name");

                        Intent intent = new Intent(getContext(), NavigationBottomActivity.class);
                        intent.putExtra("plan_id", plan_id);
                        intent.putExtra("plan_name", plan_name);
                        startActivity(intent);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                }
            }
        });

    }
}
