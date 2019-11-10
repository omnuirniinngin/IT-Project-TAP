package com.tap.taskassigningandplanning.utils.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.Date;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.ui.plan.Plan;
import com.tap.taskassigningandplanning.ui.plan.PlanFragment;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ActivityCustomDialog extends AppCompatDialogFragment implements View.OnClickListener{
    private static final String TAG = "ActivityCustomDialog";

    private EditText etActivityTitle;
    private EditText etStartDate;
    private EditText etEndDate;
    private EditText etAssignUser;
    private Button btnAdd, btnCancel;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSetListener;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @ServerTimestamp
    Date time;

    View mParentLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_activities, container, false);
        getDialog().setTitle("Add activity");

        etActivityTitle = view.findViewById(R.id.etActivityTitle);
        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnCancel = view.findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
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

                new DatePickerDialog(getContext(), dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
            private void updateLabel() {
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
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

                new DatePickerDialog(getContext(), dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
            private void updateLabel() {
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                etEndDate.setText(simpleDateFormat.format(myCalendar.getTime()));
            }
        });

        return view;
    }

    private boolean hasValidationErrors(String title, String dateStart, String dateEnd){

        if (title.isEmpty()) {
            etActivityTitle.setError(getString(R.string.input_error_activity), null);
            etActivityTitle.requestFocus();
            return true;
        }

        if (dateStart.isEmpty()) {
            etStartDate.setError(getString(R.string.input_error_start_date_activity), null);
            etStartDate.requestFocus();
            return true;
        }

        if (dateEnd.isEmpty()) {
            etEndDate.setError(getString(R.string.input_error_end_date_activity), null);
            etEndDate.requestFocus();
            return true;
        }
        return false;
    }

    private void addActivity(){
        String title = etActivityTitle.getText().toString().trim();
        String dateStart = etStartDate.getText().toString().trim();
        String dateEnd = etEndDate.getText().toString().trim();

        // Get current user id
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get plan_id created from user
        NavigationBottomActivity navigationBottomActivity = (NavigationBottomActivity)getActivity();
        Bundle id_result = navigationBottomActivity.getPlanId();
        String plan_id = id_result.getString("plan_id");

        List<String> userId = Arrays.asList(user_id);

        if(!hasValidationErrors(title, dateStart, dateEnd)){
            Activities activities = new Activities(title, dateStart, dateEnd, plan_id, userId, new Timestamp(new java.util.Date()));

            FirebaseFirestore.getInstance()
                    .collection("Activity")
                    .add(activities)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "onSuccess: added data on Firestore");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            getDialog().dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAdd:
                // Get plan_id created from user
                NavigationBottomActivity navigationBottomActivity = (NavigationBottomActivity)getActivity();
                Bundle id_result = navigationBottomActivity.getPlanId();
                String plan_id = id_result.getString("plan_id");

                final String activity_date_end = etEndDate.getText().toString();
                final String activity_date_start = etStartDate.getText().toString();
                db.collection("Plan").whereEqualTo("plan_id", plan_id)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String plan_date_end = (String) document.get("dateEnd");
                                        String plan_date_start = (String) document.get("dateStart");

                                        DateTime activity_dateEnd = new DateTime(activity_date_end);
                                        DateTime activity_dateStart = new DateTime(activity_date_start);
                                        DateTime plan_dateEnd = new DateTime(plan_date_end);
                                        DateTime plan_dateStart = new DateTime(plan_date_start);

                                        if (activity_dateStart.isBefore(plan_dateStart) || activity_dateStart.isAfter(plan_dateEnd)) {
                                            Toast.makeText(getContext(), "Invalid range of starting date.", Toast.LENGTH_LONG).show();
                                        }

                                        if (activity_dateEnd.isAfter(plan_dateEnd) || activity_dateEnd.isBefore(plan_dateStart)) {
                                            Toast.makeText(getContext(), "Invalid range of ending date.", Toast.LENGTH_LONG).show();
                                        }

                                        if( activity_dateEnd.isBefore(plan_dateEnd) && activity_dateStart.isBefore(plan_dateEnd) ) {
                                            addActivity();
                                        }

                                        if( activity_dateEnd.isBefore(plan_dateEnd) && activity_dateStart.isEqual(plan_dateEnd) ) {
                                            addActivity();
                                        }

                                        if( activity_dateEnd.isEqual(plan_dateEnd) && activity_dateStart.isBefore(plan_dateEnd) ) {
                                            addActivity();
                                        }

                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                break;
            case R.id.btnCancel:
                Log.d(TAG, "onClick: Closing Dialog");
                getDialog().dismiss();
                break;

        }
    }
}