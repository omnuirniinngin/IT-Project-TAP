package com.tap.taskassigningandplanning.utils.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.utils.team.Team;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ActivityClicked extends AppCompatActivity implements ActivitiesAdapter.ActivitiesListener, View.OnClickListener, ActivityClickedAdapter.TeamListener {

    private static final String TAG = "ActivityClicked";

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSetListener;

//    firebase firestore
    private FirebaseFirestore db;

    private Intent intent;

    private EditText etActivityTitle, etNotes, etStartDate, etEndDate, etAssignUser;
    private ActivityClickedAdapter activityClickedAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.layout_edit_activities);
        super.onCreate(savedInstanceState);

        recyclerView = findViewById(R.id.recycler_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etActivityTitle = findViewById(R.id.etActivityTitle);
        etNotes = findViewById(R.id.etNotes);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etAssignUser = findViewById(R.id.etAssignUser);

        db = FirebaseFirestore.getInstance();

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                };

                new DatePickerDialog(ActivityClicked.this, dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
            private void updateLabel() {
                String myFormat = "yyyy/MM/dd";
                java.text.SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                etStartDate.setText(simpleDateFormat.format(myCalendar.getTime()));
            }

        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                };

                new DatePickerDialog(ActivityClicked.this, dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
            private void updateLabel() {
                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                etEndDate.setText(simpleDateFormat.format(myCalendar.getTime()));
            }
        });

        etAssignUser.setOnClickListener(this);

        EditActivity();
        setupRecyclerView();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if(direction == ItemTouchHelper.LEFT){
                Toast.makeText(ActivityClicked.this, "Deleting...", Toast.LENGTH_SHORT).show();

                ActivityClickedAdapter.TeamHolder teamHolder = (ActivityClickedAdapter.TeamHolder) viewHolder;
                teamHolder.deleteItem();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(ActivityClicked.this, R.color.colorAccent))
                    .addActionIcon(R.drawable.ic_delete_sweep_black_24dp)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_button:
                updateActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void EditActivity(){
        intent = getIntent();
        String activity_id = intent.getExtras().getString("activity_id");

        final DocumentReference documentReference = db.collection("Activity").document(activity_id);

        // Get data and display to field
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());

                        Activities activities = documentSnapshot.toObject(Activities.class);
                        etActivityTitle.setText(activities.getTitle());
                        etActivityTitle.setSelection(activities.getTitle().length());
                        etStartDate.setText(activities.getDateStart());
                        etEndDate.setText(activities.getDateEnd());
                        etNotes.setText(activities.getNotes());

                        getSupportActionBar().setTitle(activities.getTitle());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                }else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void updateActivity(){

        String title = etActivityTitle.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        String dateStart = etStartDate.getText().toString().trim();
        String dateEnd = etEndDate.getText().toString().trim();

        // Get activity_id from created activity || Get plan_id created from user
        Intent intent = getIntent();
        String activity_id = intent.getExtras().getString("activity_id");
        String plan_id = intent.getExtras().getString("plan_id");

        // Get current user id
        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("Team")
                .whereEqualTo("plan_id", plan_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        }
                    }
                });

        DocumentReference docRef = db.collection("Activity").document(activity_id);

        Map<String, Object> updates = new HashMap<>();
        updates.put("title", title);
        updates.put("dateStart", dateStart);
        updates.put("dateEnd", dateEnd);
        updates.put("notes", notes);

        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: Activity document updated!");
            }
        });


//        // Update fields
//        Activities activities = new Activities(title, notes, dateStart, dateEnd, plan_id, user_id, new Timestamp(new java.util.Date()));
//        db.collection("Activity").document(activity_id).set(activities);

        onBackPressed();
    }

    private void setupRecyclerView(){
        Intent intent = getIntent();
        String activity_id = intent.getExtras().getString("activity_id");

        CollectionReference id = db.collection("Team");

        Query query = id.whereArrayContains("activity_id", activity_id).orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Team> options = new FirestoreRecyclerOptions.Builder<Team>()
                .setQuery(query, Team.class)
                .build();
        activityClickedAdapter = new ActivityClickedAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(activityClickedAdapter);
        activityClickedAdapter.startListening();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void handleDeleteItem(DocumentSnapshot snapshot) {
        Intent intent = getIntent();
        final String activity_id = intent.getExtras().getString("activity_id");
        final DocumentReference documentReference = snapshot.getReference();

        // Delete user from Activity Collection
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String user_id = (String) documentSnapshot.get("user_id");
                DocumentReference userRef = db.collection("Activity").document(activity_id);
                userRef.update("user_id", FieldValue.arrayRemove(user_id));
            }
        });

        //Delete activity document id to Team collection
        documentReference.update("activity_id", FieldValue.arrayRemove(activity_id))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Item deleted");

                    }
                });

        //Restore deleted
        Snackbar.make(recyclerView, "Item deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        documentReference.update("activity_id", FieldValue.arrayUnion(activity_id));

                        // Delete user from Activity Collection
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String user_id = (String) documentSnapshot.get("user_id");
                                DocumentReference userRef = db.collection("Activity").document(activity_id);
                                userRef.update("user_id", FieldValue.arrayUnion(user_id));
                            }
                        });
                    }
                })
                .show();
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.etAssignUser:
                intent = getIntent();

                String plan_id = intent.getExtras().getString("plan_id");
                String activity_id = intent.getExtras().getString("activity_id");
                intent = new Intent(this, ActivityClickedSearch.class);
                intent.putExtra("plan_id", plan_id);
                intent.putExtra("activity_id", activity_id);
                startActivity(intent);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == 1){
                String name = data.getStringExtra("name");
                String user_id = data.getStringExtra("user_id");
                Log.d(TAG, "Result name: " + name);
                Log.d(TAG, "Result user_id: " + user_id);
            }
        }
    }
}
