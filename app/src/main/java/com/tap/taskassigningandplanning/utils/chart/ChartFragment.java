package com.tap.taskassigningandplanning.utils.chart;

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
import com.anychart.charts.Pie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tap.taskassigningandplanning.NavigationBottomActivity;
import com.tap.taskassigningandplanning.R;

public class ChartFragment extends Fragment {
    private static final String TAG = "ChartFragment";

    private FirebaseFirestore db;
    TextView tvTitle;
    AnyChartView anyChartView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        tvTitle = view.findViewById(R.id.tvTitle);
        anyChartView = view.findViewById(R.id.anyChart);

        db = FirebaseFirestore.getInstance();

        getPlan();

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
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String title = (String) document.get("title");
                                tvTitle.setText(title);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setupChart(){
        Pie pie = AnyChart.pie();
    }
}