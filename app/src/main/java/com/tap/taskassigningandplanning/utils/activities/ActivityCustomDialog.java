package com.tap.taskassigningandplanning.utils.activities;

//import android.app.DatePickerDialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//
//import com.tap.taskassigningandplanning.R;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Locale;
//
//public class ActivityCustomDialog extends DialogFragment {
//    private static final String TAG = "ActivityCustomDialog";
//
//    private EditText etActivityName, etStartDate, etEndDate;
//    private Button btnAdd, btnCancel;
//    final Calendar myCalendar = Calendar.getInstance();
//    DatePickerDialog.OnDateSetListener dateSetListener;
//
//    public interface fabSelected{
//        void sendInput(String input);
//    }
//    public fabSelected mfabSelected;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.dialog_add_activities, container, false);
//        getDialog().setTitle("Add activity");
//
//        etActivityName = view.findViewById(R.id.etActivityName);
//        etStartDate = view.findViewById(R.id.etStartDate);
//        etEndDate = view.findViewById(R.id.etEndDate);
//        btnAdd = view.findViewById(R.id.btnAdd);
//        btnCancel = view.findViewById(R.id.btnCancel);
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: Closing Dialog");
//                getDialog().dismiss();
//            }
//        });
//
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: Capturing input");
//
//                String input = etActivityName.getText().toString();
//                if(!input.equals("")){
//
//                    mfabSelected.sendInput(input);
//
//                }
//                getDialog().dismiss();
//            }
//        });
//
//        etStartDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dateSetListener = new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
//                        myCalendar.set(Calendar.YEAR, year);
//                        myCalendar.set(Calendar.MONTH, monthOfYear);
//                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                        updateLabel();
//                    }
//
//                };
//
//                new DatePickerDialog(getContext(), dateSetListener, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//            private void updateLabel() {
//                String myFormat = "MM/dd/yy";
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
//                etStartDate.setText(simpleDateFormat.format(myCalendar.getTime()));
//            }
//
//        });
//
//        etEndDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dateSetListener = new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
//                        myCalendar.set(Calendar.YEAR, year);
//                        myCalendar.set(Calendar.MONTH, monthOfYear);
//                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                        updateLabel();
//                    }
//
//                };
//
//                new DatePickerDialog(getContext(), dateSetListener, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//            private void updateLabel() {
//                String myFormat = "MM/dd/yy";
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
//                etEndDate.setText(simpleDateFormat.format(myCalendar.getTime()));
//            }
//        });
//
//        return view;
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        try {
//            mfabSelected = (fabSelected) getTargetFragment();
//        }catch (ClassCastException e){
//            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
//        }
//    }
//
//
//}
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tap.taskassigningandplanning.MainActivity;
import com.tap.taskassigningandplanning.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ActivityCustomDialog extends DialogFragment implements View.OnClickListener{
    private static final String TAG = "ActivityCustomDialog";

    private EditText etActivityName, etStartDate, etEndDate, etAssignMember;
    private Button btnAdd, btnCancel;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSetListener;

    //firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    View mParentLayout;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAdd:
                progressDialog = ProgressDialog.show(getContext(), "", "Please wait a moment...", true);
                createActivity();
                break;
            case R.id.btnCancel:
                getDialog().dismiss();
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_activities, container, false);
        getDialog().setTitle("Add activity");

        etActivityName = view.findViewById(R.id.etActivityName);
        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        etAssignMember = view.findViewById(R.id.etAssignMember);

        btnAdd = view.findViewById(R.id.btnAdd);
        btnCancel = view.findViewById(R.id.btnCancel);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnCancel.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

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
            etActivityName.setError(getString(R.string.input_error_plan), null);
            etActivityName.requestFocus();
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

    private void createActivity(){
        String title = etActivityName.getText().toString().trim();
        String dateStart = etStartDate.getText().toString().trim();
        String dateEnd = etEndDate.getText().toString().trim();
        String AssignMember = etAssignMember.getText().toString().trim();

        if(!hasValidationErrors(title, dateStart, dateEnd)) {

//            CollectionReference dbPlan = db.collection("Plan");
//            DocumentReference newPlanRef = db.collection("Plan").document();
            DocumentReference newActivityRef = db.collection("Activities").document();

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Activities activities = new Activities();
            activities.setTitle(title);
            activities.setDateStart(dateStart);
            activities.setDateEnd(dateEnd);
            activities.setActivity_id(newActivityRef.getId());
            activities.setUser_id(userId);

            newActivityRef.set(activities).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        makeSnackBarMessage("Successfully Added.");
                    } else {
                        makeSnackBarMessage("Failed. Check log.");
                        getDialog().dismiss();
                    }
                }
            });
        }
    }


    private void makeSnackBarMessage(String message){
        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
    }

}
