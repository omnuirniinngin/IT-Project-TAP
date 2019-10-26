package com.tap.taskassigningandplanning.utils.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.Date;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.ui.plan.Plan;
import com.tap.taskassigningandplanning.ui.plan.PlanFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ActivityCustomDialog extends DialogFragment implements View.OnClickListener{
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

//    private void addActivity(){
    private void addActivity(){
        String title = etActivityTitle.getText().toString().trim();
        String dateStart = etStartDate.getText().toString().trim();
        String dateEnd = etEndDate.getText().toString().trim();
        String notes = null;

        // Get current user id
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get plan_id created from user
        NavigationBottomActivity navigationBottomActivity = (NavigationBottomActivity)getActivity();
        Bundle id_result = navigationBottomActivity.getPlanId();
        String plan_id = id_result.getString("plan_id");

        List<String> userId = Arrays.asList(user_id);

        if(!hasValidationErrors(title, dateStart, dateEnd)){
            Activities activities = new Activities(title, notes, dateStart, dateEnd, plan_id, userId, new Timestamp(new java.util.Date()));

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
                addActivity();
                break;
            case R.id.btnCancel:
                Log.d(TAG, "onClick: Closing Dialog");
                getDialog().dismiss();
                break;

        }
    }
}