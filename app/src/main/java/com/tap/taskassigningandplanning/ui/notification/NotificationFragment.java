package com.tap.taskassigningandplanning.ui.notification;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.ui.plan.Plan;
import com.tap.taskassigningandplanning.ui.plan_joined.PlanJoinedAdapter;
import com.tap.taskassigningandplanning.utils.team.Team;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class NotificationFragment extends Fragment implements NotificationAdapter.NotifListener{
    private static final String TAG = "NotificationFragment";

    //Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    private NotificationAdapter notifAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        setupRecyclerView();
        return view;
    }

    private void setupRecyclerView(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = FirebaseFirestore.getInstance()
                .collection("Team")
                .whereEqualTo("status", false).whereEqualTo("user_id", userId)
                .orderBy("created", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Team> options = new FirestoreRecyclerOptions.Builder<Team>()
                .setQuery(query, Team.class)
                .build();
        notifAdapter = new NotificationAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(notifAdapter);
        notifAdapter.startListening();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            NotificationAdapter.NotifHolder notifHolder = (NotificationAdapter.NotifHolder) viewHolder;
            switch (direction){
                case ItemTouchHelper.LEFT:
                    notifHolder.IgnoreItem();
                    break;

                case ItemTouchHelper.RIGHT:
                    notifHolder.AcceptItem();
                    break;
            }
        }

        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    //LEFT
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
                    .addSwipeLeftActionIcon(R.drawable.ic_reject)
                    //RIGHT
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                    .addSwipeRightActionIcon(R.drawable.ic_accept)

                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
        Snackbar.make(recyclerView, "Swipe left to reject or swipe right to accept", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void handleAcceptItem(DocumentSnapshot snapshot) {
        final DocumentReference documentReference = snapshot.getReference();
        final Team team = snapshot.toObject(Team.class);

        documentReference.update("status", true);
        documentReference.update("request", "");

        Snackbar.make(recyclerView, "Request successfully accepted!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void handleIgnoreItem(DocumentSnapshot snapshot) {
        final DocumentReference documentReference = snapshot.getReference();
        final Team team = snapshot.toObject(Team.class);

        documentReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Request rejected");
                    }
                });
        Snackbar.make(recyclerView, "Request successfully rejected!", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        documentReference.set(team);
                    }
                })
                .show();
    }
}
