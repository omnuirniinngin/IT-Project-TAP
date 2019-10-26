package com.tap.taskassigningandplanning.utils.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tap.taskassigningandplanning.R;

public class TaskCustomDialog extends DialogFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_progress, container, false);

        Button btnFinish = view.findViewById(R.id.btnFinish);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnFinish.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return view;
    }

    private void finishClicked(){

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnFinish:

                break;
            case R.id.btnCancel:
                break;
        }
    }
}
