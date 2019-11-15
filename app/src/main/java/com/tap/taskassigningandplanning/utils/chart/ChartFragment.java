package com.tap.taskassigningandplanning.utils.chart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.utils.activities.Activities;

import java.util.ArrayList;
import java.util.List;

public class ChartFragment extends Fragment {
    private static final String TAG = "ChartFragment";

    private FirebaseFirestore db;
    TextView tvTitle;
    AnyChartView anyChartView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_chart, container, false);

        tvTitle = view.findViewById(R.id.tvTitle);
        anyChartView = view.findViewById(R.id.anyChart);

        db = FirebaseFirestore.getInstance();

        setupProgressChart();

        return view;
    }

    private void setupProgressChart(){
        final NavigationBottomActivity activity = (NavigationBottomActivity)getActivity();
        Bundle id_result = activity.getPlanId();
        final String plan_id = id_result.getString("plan_id");
        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final Cartesian cartesian = AnyChart.column();
        final List<DataEntry> dataEntries = new ArrayList<>();

//        db.collection("Task")
//                .whereEqualTo("plan_id", plan_id)
//                .whereArrayContains("user_id", user_id)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            int count = 0;
//                            for (final QueryDocumentSnapshot document : task.getResult()) {
//                                count++;
//                                Log.d(TAG, "Task activity_id: " + document.get("activity_id"));
//                                String activity_id = (String) document.get("activity_id");
//
//                                DocumentReference activity_ref = db.collection("Activity").document(activity_id);
//
//                                activity_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            DocumentSnapshot document = task.getResult();
//                                            if (document.exists()) {
//                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//
//                                                List<Integer> progressList = new ArrayList<>();
//                                                List<String> titleList = new ArrayList<>();
//
//                                                int completed_task = document.getLong("completed_task").intValue();
//                                                int total_task = document.getLong("total_task").intValue();
//                                                if ( total_task == 0 ){
//                                                    progressList.add(0);
//                                                    titleList.add(document.get("title").toString());
//                                                }
//                                                if (total_task > 0 ){
//                                                    float progress = completed_task*100 / total_task;
//                                                    progressList.add((int) progress);
//                                                    titleList.add(document.get("title").toString());
//                                                }
//
//                                                Integer[] prog = new Integer[progressList.size()];
//                                                prog = progressList.toArray(prog);
//
//                                                String[] title = new String[titleList.size()];
//                                                title = titleList.toArray(title);
//
//                                                for (int i = 0; i < title.length; i++){
//                                                    dataEntries.add(new ValueDataEntry(title[i], prog[i]));
//                                                }
//
//                                                Column column = cartesian.column(dataEntries);
//
//                                                column.tooltip()
//                                                        .titleFormat("{%X}")
//                                                        .position(Position.CENTER_BOTTOM)
//                                                        .anchor(Anchor.CENTER_BOTTOM)
//                                                        .offsetX(0d)
//                                                        .offsetY(5d)
//                                                        .format("{%Value}{groupsSeparator: }%");
//
//                                                cartesian.animation(true);
//
//                                                db.collection("Plan")
//                                                        .whereEqualTo("plan_id", plan_id)
//                                                        .get()
//                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                                if (task.isSuccessful()) {
//                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                                                        Log.d(TAG, document.getId() + " => " + document.getData());
//                                                                        String title = (String) document.get("title");
//                                                                        cartesian.title(title);
//
//                                                                        cartesian.yScale().minimum(0d);
//                                                                        cartesian.yScale().maximum(100);
//
//                                                                        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }%");
//                                                                        cartesian.xAxis(0).labels().rotation(-70);
//
//                                                                        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
//                                                                        cartesian.interactivity().hoverMode(HoverMode.BY_X);
//
//                                                                        cartesian.xAxis(0).title("Activities");
//                                                                        cartesian.yAxis(0).title("Percentage");
//
//                                                                        anyChartView.setChart(cartesian);
//                                                                    }
//                                                                } else {
//                                                                    Log.d(TAG, "Error getting documents: ", task.getException());
//                                                                }
//                                                            }
//                                                        });
//
//                                            } else {
//                                                Log.d(TAG, "No such document");
//                                            }
//                                        } else {
//                                            Log.d(TAG, "get failed with ", task.getException());
//                                        }
//                                    }
//                                });
//
//                            }
//                        }
//                    }
//                });
        db.collection("Activity")
                .whereEqualTo("plan_id", plan_id)
                .whereArrayContains("user_id", user_id)
                .orderBy("dateEnd")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Integer> progressList = new ArrayList<>();
                            List<String> titleList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int completed_task = document.getLong("completed_task").intValue();
                                int total_task = document.getLong("total_task").intValue();
                                if ( total_task == 0 ){
                                    progressList.add(0);
                                    titleList.add(document.get("title").toString());
                                }
                                if (total_task > 0 ){
                                    float progress = completed_task*100 / total_task;
                                    progressList.add((int) progress);
                                    titleList.add(document.get("title").toString());
                                }
                            }

                            Integer[] prog = new Integer[progressList.size()];
                            prog = progressList.toArray(prog);

                            String[] title = new String[titleList.size()];
                            title = titleList.toArray(title);

                            for (int i = 0; i < title.length; i++){
                                dataEntries.add(new ValueDataEntry(title[i], prog[i]));
                            }

                            Column column = cartesian.column(dataEntries);

                            column.tooltip()
                                    .titleFormat("{%X}")
                                    .position(Position.CENTER_BOTTOM)
                                    .anchor(Anchor.CENTER_BOTTOM)
                                    .offsetX(0d)
                                    .offsetY(5d)
                                    .format("{%Value}{groupsSeparator: }%");

                            cartesian.animation(true);

                            db.collection("Plan")
                                    .whereEqualTo("plan_id", plan_id)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                    String title = (String) document.get("title");
                                                    cartesian.title(title);

                                                    cartesian.yScale().minimum(0d);
                                                    cartesian.yScale().maximum(100);

                                                    cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }%");
                                                    cartesian.xAxis(0).labels().rotation(-70);

                                                    cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                                                    cartesian.interactivity().hoverMode(HoverMode.BY_X);

                                                    cartesian.xAxis(0).title("Activities");
                                                    cartesian.yAxis(0).title("Percentage");

                                                    anyChartView.setChart(cartesian);
                                                }
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });

                        }
                    }
                });

    }
}