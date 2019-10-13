package com.tap.taskassigningandplanning.utils.activities;

//import android.app.DatePickerDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FieldValue;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.ServerTimestamp;
//import com.google.type.Date;
//import com.tap.taskassigningandplanning.NavigationBottomActivity;
//import com.tap.taskassigningandplanning.R;
//import com.tap.taskassigningandplanning.ui.plan.PlanFragment;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//public class ActivityCustomDialog extends DialogFragment {
//    private static final String TAG = "ActivityCustomDialog";
//
//    private EditText etActivityName, etStartDate, etEndDate;
//    private Button btnAdd, btnCancel;
//    final Calendar myCalendar = Calendar.getInstance();
//    DatePickerDialog.OnDateSetListener dateSetListener;
//
//    //firebase
//    private FirebaseAuth mAuth;
//    private FirebaseFirestore db;
//    @ServerTimestamp
//    Date time;
//
//    View mParentLayout;
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
//        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
//
//        Bundle id_result = activity.getMyId();
//        final String myValue = id_result.getString("plan_id");
//
////        Bundle mBundle = getArguments();
////        final String myValue = mBundle.getString("PLAN_ID");
//
////        final String myValue = getArguments().getString("planId");
//
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
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
//                String title = etActivityName.getText().toString().trim();
//                String startDate = etStartDate.getText().toString().trim();
//                String endDate = etEndDate.getText().toString().trim();
//
////                String myValue = getArguments().getString("planId");
//                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                // Create a new collection
//                Map<String, Object> activity = new HashMap<>();
//                activity.put("title",title);
//                activity.put("dateStart",startDate);
//                activity.put("dateEnd",endDate);
//                activity.put("user_id", userId);
//                activity.put("plan_id", myValue);
//                activity.put("timestamp", FieldValue.serverTimestamp());
//                //activity.put("ActivityId", db.collection("Activity").get());
//
//                // Add a new document with a generated ID
//                db.collection("Activity")
//                        .add(activity)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Log.d(TAG, "onSuccess: DocumentSnapshot added with ID: " + documentReference.getId());
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Error adding document", e);
//                            }
//                        });
//
////                mfabSelected.sendInput(title);
//
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
//    private boolean hasValidationErrors(String title, String dateStart, String dateEnd){
//
//        if (title.isEmpty()) {
//            etActivityName.setError(getString(R.string.input_error_plan), null);
//            etActivityName.requestFocus();
//            return true;
//        }
//
//        if (dateStart.isEmpty()) {
//            etStartDate.setError(getString(R.string.input_error_start_date), null);
//            etStartDate.requestFocus();
//            return true;
//        }
//
//        if (dateEnd.isEmpty()) {
//            etEndDate.setError(getString(R.string.input_error_end_date), null);
//            etEndDate.requestFocus();
//            return true;
//        }
//        return false;
//    }
//
//}

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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.Date;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.ui.plan.PlanFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ActivityCustomDialog extends DialogFragment implements View.OnClickListener{
    private static final String TAG = "ActivityCustomDialog";

    private EditText etActivityName;
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

        etActivityName = view.findViewById(R.id.etActivityName);
        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        etAssignUser = view.findViewById(R.id.etAssignUser);
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
            etActivityName.setError(getString(R.string.input_error_activity), null);
            etActivityName.requestFocus();
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
        String title = etActivityName.getText().toString().trim();
        String dateStart = etStartDate.getText().toString().trim();
        String dateEnd = etEndDate.getText().toString().trim();
        String assignUser = etAssignUser.getText().toString().trim();

        NavigationBottomActivity navigationBottomActivity = (NavigationBottomActivity)getActivity();

        Bundle id_result = navigationBottomActivity.getMyId();
        String myValue = id_result.getString("plan_id");

        if(!hasValidationErrors(title, dateStart, dateEnd)){
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Activities activities = new Activities(title, dateStart, dateEnd, assignUser, userId, myValue, new Timestamp(new java.util.Date()));

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

//            // Create a new collection
//            Map<String, Object> activity = new HashMap<>();
//            activity.put("title",title);
//            activity.put("dateStart",dateStart);
//            activity.put("dateEnd",dateEnd);
//            activity.put("user_id", userId);
//            activity.put("plan_id", myValue);
//            activity.put("timestamp", FieldValue.serverTimestamp());
//
//            // Add a new document with a generated ID
//            db.collection("Activity")
//                    .add(activity)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Log.d(TAG, "onSuccess: DocumentSnapshot added with ID: " + documentReference.getId());
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w(TAG, "Error adding document", e);
//                        }
//                    });

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}