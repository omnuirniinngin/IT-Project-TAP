package com.tap.taskassigningandplanning.utils.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tap.taskassigningandplanning.R;

public class ActivityDialogFragment extends Fragment implements ActivityCustomDialog.fabSelected{
    private static final String TAG = "ActivityDialogFragment";

    private EditText etActivityName;

    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities, container, false);

        etActivityName = view.findViewById(R.id.etActivityName);

        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Opening dialog activity");
                ActivityCustomDialog dialog = new ActivityCustomDialog();
                dialog.setTargetFragment(ActivityDialogFragment.this, 1);
                dialog.show(getFragmentManager(), "ActivityCustomDialog");
            }
        });

        return view;
    }

    @Override
    public void sendInput(String input) {
        Log.d(TAG, "sendInput: Found incoming input" + input);

        etActivityName.setText(input);
    }
}
