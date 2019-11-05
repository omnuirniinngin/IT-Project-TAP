package com.tap.taskassigningandplanning.utils.task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.utils.activities.Activities;
import com.tap.taskassigningandplanning.utils.progress.Progress;

public class TaskAdapter extends FirestoreRecyclerAdapter <Activities, TaskAdapter.TaskHolder> {
    private static final String TAG = "TaskAdapter";

    TaskListener listener;

    public TaskAdapter(@NonNull FirestoreRecyclerOptions<Activities> options, TaskListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final TaskHolder holder, int position, @NonNull Activities activities) {
        holder.tvTitle.setText(activities.getTitle());
        holder.tvPercent.setText(String.valueOf(activities.getProgress())+"%");

        int value = activities.getProgress();
        holder.seekBar.setProgress(value);

        if(value == 100){
            holder.seekBar.setEnabled(false);
        }
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_list_tasks, parent, false);
        return new TaskAdapter.TaskHolder(view);
    }

    class TaskHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvPercent;
        SeekBar seekBar;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvActivityTitle);
            seekBar = itemView.findViewById(R.id.seekBar);
            tvPercent = itemView.findViewById(R.id.tvPercent);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    tvPercent.setText("" + progress + "%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    int progress = seekBar.getProgress();
                    Activities activities = getItem(getAdapterPosition());
                    if(activities.getProgress() != progress){
                        listener.handleProgressChanged(progress, snapshot);
                    }
                }
            });
        }

    }
    public interface TaskListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void handleProgressChanged(int progressChanged, DocumentSnapshot snapshot);
    }

}
