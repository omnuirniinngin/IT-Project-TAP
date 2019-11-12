package com.tap.taskassigningandplanning.utils.activities.Task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tap.taskassigningandplanning.R;

public class ActivityTaskCustomEditDialog extends DialogFragment {
    private static final String TAG = "ActivityTaskCustomEditD";

    //FirebaseFirestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String title, snapshot_id;
    int position;
    private TextView tvTitle;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add_task, null);

        Bundle bundle = getArguments();
        title = bundle.getString("title", "");
        snapshot_id = bundle.getString("snapshot_id", "");

        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        final Editable text = (Editable) tvTitle.getText();
        position = tvTitle.length();
        Selection.setSelection(text, position);

        builder.setView(view)
                .setTitle("Edit task")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateTask();
                    }
                });

        return builder.create();
    }

    public void updateTask(){
        String new_title = tvTitle.getText().toString().trim();

        if(!new_title.isEmpty()){
            db.collection("Task").document(snapshot_id)
                    .update("title", new_title)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: ");
                        }
                    });
        }
    }
}
