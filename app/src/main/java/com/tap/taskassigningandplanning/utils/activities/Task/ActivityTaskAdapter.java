package com.tap.taskassigningandplanning.utils.activities.Task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tap.taskassigningandplanning.R;

public class ActivityTaskAdapter extends FirestoreRecyclerAdapter <Task, ActivityTaskAdapter.ActivityTaskHolder> {

    TaskListener listener;

    public ActivityTaskAdapter(@NonNull FirestoreRecyclerOptions<Task> options, TaskListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ActivityTaskHolder holder, int position, @NonNull Task task) {
        holder.tvTitle.setText(task.getTitle());
        holder.cbCompleted.setChecked(task.isCompleted());

        boolean completed = task.isCompleted();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String creator = task.getCreator();

        if(completed==true && !userId.equals(creator)){
            holder.cbCompleted.setEnabled(false);
        }

        if(!userId.equals(creator)){
            holder.btnDelete.setVisibility(View.INVISIBLE);
        }
}

    @NonNull
    @Override
    public ActivityTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_row_task, parent, false);
        return new ActivityTaskAdapter.ActivityTaskHolder(view);
    }

    class ActivityTaskHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        CheckBox cbCompleted;
        ImageButton btnDesignate, btnDelete;

        public ActivityTaskHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            cbCompleted = itemView.findViewById(R.id.cbCompleted);
            btnDesignate = itemView.findViewById(R.id.btnDesignate);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            cbCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    Task task = getItem(getAdapterPosition());
                    if (task.isCompleted() != isChecked) {
                        listener.handleCheckChanged(isChecked, snapshot);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    listener.handleEditTask(snapshot);
                }
            });

            btnDesignate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    listener.handleDesignateUser(snapshot);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    listener.handleDelete(snapshot);
                }
            });
        }
    }

    interface TaskListener{
        void handleCheckChanged(boolean isChecked, DocumentSnapshot snapshot);
        void handleEditTask(DocumentSnapshot snapshot);
        void handleDelete(DocumentSnapshot snapshot);
        void handleDesignateUser(DocumentSnapshot snapshot);
    }

}
