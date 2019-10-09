package com.tap.taskassigningandplanning.utils.activities;

//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.tap.taskassigningandplanning.R;
//
//public class ActivityDialogFragment extends Fragment implements ActivityCustomDialog.fabSelected{
//    private static final String TAG = "ActivityDialogFragment";
//
//    private EditText etActivityName;
//
//    private FloatingActionButton fab;
//
//    View mParentLayout;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_activities, container, false);
//
//        etActivityName = view.findViewById(R.id.etActivityName);
//
//        fab = view.findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: Opening dialog activity");
//                ActivityCustomDialog dialog = new ActivityCustomDialog();
//                dialog.setTargetFragment(ActivityDialogFragment.this, 1);
//                dialog.show(getFragmentManager(), "ActivityCustomDialog");
//            }
//        });
//
//        return view;
//    }
//
//    @Override
//    public void sendInput(String input) {
//        Log.d(TAG, "sendInput: Found incoming input" + input);
//
//        etActivityName.setText(input);
//    }
//
//    private void makeSnackBarMessage(String message){
//        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
//    }
//}
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tap.taskassigningandplanning.R;

public class ActivityDialogFragment extends DialogFragment implements View.OnClickListener{
    private static final String TAG = "ActivityDialogFragment";

    private EditText etActivityName, etStartDate, etEndDate;

    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities, container, false);

        etActivityName = view.findViewById(R.id.etActivityName);
        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                //insert new activity
                ActivityCustomDialog dialog = new ActivityCustomDialog();
                dialog.setTargetFragment(ActivityDialogFragment.this, 1);
                dialog.show(getFragmentManager(), "ActivityCustomDialog");
                break;
                default:
                    getActivity().onBackPressed();
        }
    }
}
