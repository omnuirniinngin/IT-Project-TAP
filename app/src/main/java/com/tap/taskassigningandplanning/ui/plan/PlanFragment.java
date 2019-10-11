package com.tap.taskassigningandplanning.ui.plan;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PlanFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "PlanFragment";

    //initialization of functions
    private EditText etPlanTitle, etStartDate, etEndDate;
    private Button btnCreate, btnCancel;

    private ProgressDialog progressDialog;

    //val
    View mParentLayout;
    //firebase
    private FirebaseFirestore db;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSetListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        etPlanTitle = view.findViewById(R.id.etPlanTitle);
        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        btnCreate = view.findViewById(R.id.btnCreate);
        btnCancel = view.findViewById(R.id.btnCancel);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnCreate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);


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
                String myFormat = "MM/dd/yy";
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
                String myFormat = "MM/dd/yy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                etEndDate.setText(simpleDateFormat.format(myCalendar.getTime()));
            }
        });


        return view;
    }

    private boolean hasValidationErrors(String title, String dateStart, String dateEnd){

        if (title.isEmpty()) {
            etPlanTitle.setError(getString(R.string.input_error_plan), null);
            etPlanTitle.requestFocus();
            return true;
        }

        if (dateStart.isEmpty()) {
            etStartDate.setError(getString(R.string.input_error_start_date), null);
            etStartDate.requestFocus();
            return true;
        }

        if (dateEnd.isEmpty()) {
            etEndDate.setError(getString(R.string.input_error_end_date), null);
            etEndDate.requestFocus();
            return true;
        }
        return false;
    }

    private void createPlan() {
        String title = etPlanTitle.getText().toString().trim();
        String dateStart = etStartDate.getText().toString().trim();
        String dateEnd = etEndDate.getText().toString().trim();

        if(!hasValidationErrors(title, dateStart, dateEnd)){

            //CollectionReference dbPlan = db.collection("Plan");
            final DocumentReference newPlanRef = db.collection("Plan").document();

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            final Plan plan = new Plan();
            plan.setTitle(title);
            plan.setDateStart(dateStart);
            plan.setDateEnd(dateEnd);
            plan.setPlan_id(newPlanRef.getId());
            plan.setUser_id(userId);

//            Plan plan = new Plan(
//                    title,
//                    dateStart,
//                    dateEnd,
//                    plan_id,
//                    user_id
//            );

            newPlanRef.set(plan).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        String myId = newPlanRef.getId();

                        //Sending plan id to another fragment
//                        Bundle bundle = new Bundle();
//                        bundle.putString("PLAN_ID", myId);
//
//                        ActivityCustomDialog activityCustomDialog = new ActivityCustomDialog();
//                        activityCustomDialog.setArguments(bundle);
//                        activityCustomDialog.show(getFragmentManager(), "TAG");

                        Intent intent = new Intent(getContext(), NavigationBottomActivity.class);
                        intent.putExtra("plan_id", myId);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        makeSnackBarMessage("Failed. Check log.");
                    }
                }
            });
//            dbPlan.add(plan)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Intent intent = new Intent(getContext(), NavigationBottomActivity.class);
//                            startActivity(intent);
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    });

        }
    }

    private void makeSnackBarMessage(String message){
        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCreate:
                progressDialog = ProgressDialog.show(getContext(), "", "Please wait a moment...", true);
                createPlan();
                break;
            case R.id.btnCancel:
                getActivity().finish();
                break;
        }
    }
}