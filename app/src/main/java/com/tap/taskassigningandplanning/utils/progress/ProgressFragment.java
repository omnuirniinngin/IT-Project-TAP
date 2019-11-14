package com.tap.taskassigningandplanning.utils.progress;

import android.app.Activity;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.ui.plan.Plan;
import com.tap.taskassigningandplanning.utils.activities.Activities;
import com.tap.taskassigningandplanning.utils.activities.ActivitiesAdapter;
import com.tap.taskassigningandplanning.utils.activities.Task.ActivityTask;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProgressFragment extends Fragment implements ProgressAdapter.ProgressList{

    private static final String TAG = "ProgressFragment";

//    firebase firestore
    private FirebaseFirestore db;

    private ProgressAdapter progressAdapter;
    private RecyclerView recyclerView;

    private TextView tvTitle, tvDays, tvPlanDuration, tvCompleted;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        tvTitle = view.findViewById(R.id.tvTitle);
        tvDays = view.findViewById(R.id.tvDays);
        tvPlanDuration = view.findViewById(R.id.tvPlanDuration);
        tvCompleted = view.findViewById(R.id.tvCompleted);

        recyclerView = view.findViewById(R.id.recycler_view);

        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
        Bundle id_result = activity.getPlanId();
        String plan_id = id_result.getString("plan_id");
        String plan_name = id_result.getString("plan_name");

        tvTitle.setText(plan_name);

        db = FirebaseFirestore.getInstance();

        getPlan();
        getActivityCompleted();
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
                                            .appendDays().appendSuffix(" Day/s", " Day/s").toFormatter();

                                    tvDays.setText(formatter.print(period));
                                }

                                if (newCurrentDate.isBefore(newStartDate)) {
                                    Period period = new Period(newCurrentDate, newStartDate, PeriodType.days());
                                    PeriodFormatter formatter = new PeriodFormatterBuilder()
                                            .appendDays().appendSuffix(" Day/s", " Day/s Remaining").toFormatter();

                                    tvDays.setText(formatter.print(period));
                                }

                                if (newCurrentDate.isAfter(newStartDate)) {
                                    Period period = new Period(newCurrentDate, newEndDate, PeriodType.days());
                                    PeriodFormatter formatter = new PeriodFormatterBuilder()
                                            .appendDays().appendSuffix(" Day/s", " Day/s").toFormatter();
                                    tvDays.setText(formatter.print(period));
                                }

                                Period period = new Period(newStartDate, newEndDate, PeriodType.days());
                                PeriodFormatter formatter = new PeriodFormatterBuilder()
                                        .appendDays().appendSuffix(" Day/s", " Day/s").toFormatter();

                                tvPlanDuration.setText(formatter.print(period));



                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getActivityCompleted(){
        NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
        Bundle id_result = activity.getPlanId();
        final String plan_id = id_result.getString("plan_id");

        db.collection("Activity")
                .whereEqualTo("plan_id", plan_id)
                .whereEqualTo("completed", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count_true = 0;
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                count_true++;

                            }

                            final int finalCount_true = count_true;
                            db.collection("Activity")
                                    .whereEqualTo("plan_id", plan_id)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                int activity_total = 0;
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    activity_total++;
//                                                    activity_total = document.getLong("total_activity").intValue();
                                                }
                                                tvCompleted.setText(finalCount_true + "/" + activity_total);
                                            }
                                        }
                                    });
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
        progressAdapter = new ProgressAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(progressAdapter);
        progressAdapter.startListening();

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
        startActivityForResult(intent, 5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 5) && (resultCode == Activity.RESULT_OK)){
            // recreate your fragment here
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }

    }
}