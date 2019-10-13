package com.tap.taskassigningandplanning.utils.activities;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.tap.taskassigningandplanning.R;

import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ActivityClicked extends AppCompatActivity {

    private EditText etActivityTitle, etNotes, etStartDate;
    private String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.layout_activities);
        super.onCreate(savedInstanceState);

        etStartDate = findViewById(R.id.etStartDate);
        etStartDate.setText(date_n);
    }
}
