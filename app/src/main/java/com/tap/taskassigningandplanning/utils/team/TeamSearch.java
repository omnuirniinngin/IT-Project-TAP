package com.tap.taskassigningandplanning.utils.team;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tap.taskassigningandplanning.R;

public class TeamSearch extends AppCompatActivity implements TeamSearchAdapter.TeamListener{
    private static final String TAG = "TeamSearch";

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView recyclerView;

    private TeamSearchAdapter teamSearchAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        mSearchField = findViewById(R.id.search_field);
        mSearchBtn = findViewById(R.id.search_btn);
        recyclerView = findViewById(R.id.recycler_view);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = mSearchField.getText().toString();

                firestoreUser(searchText);

            }
        });
    }

    private void firestoreUser(String searchText) {

        CollectionReference docUsers = db.collection("User");

        Query query = docUsers.orderBy("name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirestoreRecyclerOptions<Team> options = new FirestoreRecyclerOptions.Builder<Team>()
                .setQuery(query, Team.class)
                .build();

        teamSearchAdapter = new TeamSearchAdapter(options);
        teamSearchAdapter.notifyDataSetChanged();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(teamSearchAdapter);
        teamSearchAdapter.startListening();

    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

    }
}
