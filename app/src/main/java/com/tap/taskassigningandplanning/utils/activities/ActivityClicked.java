package com.tap.taskassigningandplanning.utils.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tap.taskassigningandplanning.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ActivityClicked extends AppCompatActivity implements ActivitiesAdapter.ActivitiesListener{

    private static final String TAG = "ActivityClicked";

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSetListener;

//    firebase firestore
    private FirebaseFirestore db;

    private EditText etActivityTitle, etNotes, etStartDate, etEndDate, etAssignUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.layout_activities);
        super.onCreate(savedInstanceState);


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
                String myFormat = "MM/dd/yy";
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
                String myFormat = "MM/dd/yy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                etEndDate.setText(simpleDateFormat.format(myCalendar.getTime()));
            }
        });


        EditActivity();
    }

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

        Intent intent = getIntent();
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
                        etAssignUser.setText(activities.getAssignUser());

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
        String assignUser = etAssignUser.getText().toString().trim();

        // Get current user id
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get activity_id from created activity
        Intent intent = getIntent();
        String activity_id = intent.getExtras().getString("activity_id");
        // Get plan_id created from user;
        String plan_id = intent.getExtras().getString("plan_id");
        // Update fields
        Activities activities = new Activities(title, notes, dateStart, dateEnd, assignUser, userId, plan_id, new Timestamp(new java.util.Date()));
        db.collection("Activity").document(activity_id).set(activities);

        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void handleDeleteItem(DocumentSnapshot snapshot) {

    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

    }
}
