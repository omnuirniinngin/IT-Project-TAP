package com.tap.taskassigningandplanning.utils.progress;

import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.utils.activities.Activities;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProgressAdapter extends FirestoreRecyclerAdapter <Activities, ProgressAdapter.ProgressHolder> {
    private static final String TAG = "ProgressAdapter";

    private ProgressList listener;

    public ProgressAdapter(@NonNull FirestoreRecyclerOptions<Activities> options, ProgressList listener) {
        super(options);
        this.listener = listener;
    }
    @Override
    public ProgressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_list_progress, parent, false);
        return new ProgressHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProgressHolder holder, int position, @NonNull Activities activities) {
        holder.tvActivityTitle.setText(activities.getTitle());
        holder.tvCompleted.setText(String.valueOf(activities.getCompleted_task())+"/"+String.valueOf(activities.getTotal_task()));
        int completed_task = activities.getCompleted_task();
        int total_task = activities.getTotal_task();

        if ( total_task == 0 ){
            holder.progressBar.setProgress(0);
            holder.tvPercent.setText("0%");

        }
        if (total_task > 0 ){
            float progress = completed_task*100 / total_task;
            holder.progressBar.setProgress((int) progress);
            holder.tvPercent.setText((int) progress + "%");
        }


        String dateStart = activities.getDateStart();
        String dateEnd = activities.getDateEnd();


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

            holder.tvDays.setText(formatter.print(period));
        }

        if (newCurrentDate.isBefore(newStartDate)) {
            Period period = new Period(newCurrentDate, newStartDate, PeriodType.days());
            PeriodFormatter formatter = new PeriodFormatterBuilder()
                    .appendDays().appendSuffix(" Day/s Remaining", " Day/s Remaining").toFormatter();

            holder.tvDays.setText(formatter.print(period));
        }

        if (newCurrentDate.isAfter(newStartDate)) {
            Period period = new Period(newCurrentDate, newEndDate, PeriodType.days());
            PeriodFormatter formatter = new PeriodFormatterBuilder()
                    .appendDays().appendSuffix(" Day/s Left", " Day/s Left").toFormatter();
            holder.tvDays.setText(formatter.print(period));
        }


    }


    class ProgressHolder extends RecyclerView.ViewHolder{

        TextView tvActivityTitle, tvCompleted, tvDays, tvPercent;
        ProgressBar progressBar;

        public ProgressHolder(@NonNull View itemView) {
            super(itemView);
            tvActivityTitle = itemView.findViewById(R.id.tvActivityTitle);
            tvDays = itemView.findViewById(R.id.tvDays);
            tvCompleted = itemView.findViewById(R.id.tvCompleted);
            tvPercent = itemView.findViewById(R.id.tvPercent);
            progressBar = itemView.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface ProgressList{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
}
