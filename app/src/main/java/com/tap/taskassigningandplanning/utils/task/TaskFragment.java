package com.tap.taskassigningandplanning.utils.task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.RatingActivity;
import com.tap.taskassigningandplanning.utils.activities.Activities;
import com.tap.taskassigningandplanning.utils.activities.Task.ActivityTask;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskFragment extends Fragment implements TaskAdapter.TaskListener{
    private static final String TAG = "TaskFragment";

    //firebase firestore
    private FirebaseFirestore db;

    private TaskAdapter taskAdapter;
    private RecyclerView recyclerView;

    TextView tvTitle, tvDaysLeftPlan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        tvTitle = view.findViewById(R.id.tvTitle);
        tvDaysLeftPlan = view.findViewById(R.id.tvDaysLeftPlan);

        recyclerView = view.findViewById(R.id.recycler_view);

        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
        Bundle id_result = activity.getPlanId();
        String plan_name = id_result.getString("plan_name");
        tvTitle.setText(plan_name);

        db = FirebaseFirestore.getInstance();

        getPlan();
        setupRecyclerView();

        return view;
    }

    private void getPlan(){

        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
        Bundle id_result = activity.getPlanId();
        final String plan_id = id_result.getString("plan_id");

        db.collection("Plan")
                .whereEqualTo("plan_id", plan_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String dateStart = (String) document.get("dateStart");
                                String dateEnd = (String) document.get("dateEnd");

                                Date date = Calendar.getInstance().getTime();
                                String myFormat = "yyyy-MM-dd";
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat);
                                String currentDate = simpleDateFormat.format(date);

                                DateTime newCurrentDate = new DateTime(currentDate);
                                DateTime newStartDate = new DateTime(dateStart);
                                DateTime newEndDate = new DateTime(dateEnd);

                                if (newCurrentDate.isEqual(newStartDate)) {
                                    Period period = new Period(newStartDate, newEndDate, PeriodType.days());
                                    PeriodFormatter formatter = new PeriodFormatterBuilder()
                                            .appendDays().appendSuffix(" Day/s Left", " Day/s Left").toFormatter();

                                    tvDaysLeftPlan.setText(formatter.print(period));
                                }

                                if (newCurrentDate.isBefore(newStartDate)) {
                                    Period period = new Period(newStartDate, newEndDate, PeriodType.days());
                                    PeriodFormatter formatter = new PeriodFormatterBuilder()
                                            .appendDays().appendSuffix(" Day/s Left", " Day/s to start").toFormatter();

                                    tvDaysLeftPlan.setText(formatter.print(period));
                                }

                                if (newCurrentDate.isAfter(newStartDate)) {
                                    Period period = new Period(newCurrentDate, newEndDate, PeriodType.days());
                                    PeriodFormatter formatter = new PeriodFormatterBuilder()
                                            .appendDays().appendSuffix(" Day/s Left", " Day/s Left").toFormatter();
                                    tvDaysLeftPlan.setText(formatter.print(period));
                                }

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setupRecyclerView(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();

        Bundle id_result = activity.getPlanId();
        final String plan_id = id_result.getString("plan_id");

        Query query = FirebaseFirestore.getInstance()
                .collection("Activity")
                .whereEqualTo("plan_id", plan_id).whereArrayContains("user_id", userId)
                .orderBy("dateEnd", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Activities> options = new FirestoreRecyclerOptions.Builder<Activities>()
                .setQuery(query, Activities.class)
                .build();
        taskAdapter= new TaskAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(taskAdapter);
        taskAdapter.startListening();

    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

        Intent intent;
        intent = getActivity().getIntent();

        String plan_id = intent.getExtras().getString("plan_id");
        String activity_id = documentSnapshot.getId();

        intent = new Intent(getContext(), ActivityTask.class);
        intent.putExtra("plan_id", plan_id);
        intent.putExtra("activity_id", activity_id);
        startActivity(intent);
    }

    @Override
    public void handleProgressChanged(final int progressChanged, final DocumentSnapshot documentSnapshot) {
        int value = 100;
        Intent intent;
        intent = getActivity().getIntent();

        final String plan_id = intent.getExtras().getString("plan_id");
        final String id = documentSnapshot.getId();
        //Current user
        final String current_user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(value == progressChanged){
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.dialog_confirm_progress)
                    .setPositiveButton("Finish task", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            documentSnapshot.getReference().update("progress", progressChanged)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "onSuccess: " + progressChanged);

                                            db.collection("Plan").whereEqualTo("plan_id", plan_id)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                                    String user_id = (String) document.get("user_id");

                                                                    if(user_id.equals(current_user)){
                                                                        new AlertDialog.Builder(getContext())
                                                                                .setTitle("RatingActivity")
                                                                                .setMessage("Would you like to give a rate?")
                                                                                .setPositiveButton("Rate now", new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                                        Intent intent = new Intent(getContext(), RatingActivity.class);
                                                                                        intent.putExtra("plan_id", plan_id);
                                                                                        intent.putExtra("activity_id", id);
                                                                                        startActivity(intent);
                                                                                    }
                                                                                }).setNegativeButton("No", null)
                                                                                .show();
                                                                    }
                                                                }
                                                            } else {
                                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                                            }
                                                        }
                                                    });

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                        }
                                    });
                        }
                    }).setNegativeButton("Cancel", null)
                    .show();
        }else{
            documentSnapshot.getReference().update("progress", progressChanged)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: ");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                        }
                    });
        }

    }
}
