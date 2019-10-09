package com.tap.taskassigningandplanning.utils.activities;

//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//import com.tap.taskassigningandplanning.R;
//
//
//public class ActivityFragment extends Fragment {
//    private static final String TAG = "ActivityFragment";
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.dialog_frame_layout, container, false);
//
//        ActivityDialogFragment fragment = new ActivityDialogFragment();
//
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.container, fragment, "ActivityDialogFragment");
//        transaction.commit();
//
//        return view;
//    }
//}

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.tap.taskassigningandplanning.R;


public class ActivityFragment extends Fragment {
    private static final String TAG = "ActivityFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_frame_layout, container, false);

        ActivityDialogFragment fragment = new ActivityDialogFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, "ActivityDialogFragment");
        transaction.commit();

        return view;
    }
}