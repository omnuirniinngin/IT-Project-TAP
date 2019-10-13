package com.tap.taskassigningandplanning.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.tap.taskassigningandplanning.R;

public class ProfileFragment extends Fragment {

    private ListView listView1;
    private ListView listView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile,
                container, false);



        RatingBar ratingBar = rootView.findViewById(R.id.myratingbar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                Toast.makeText(ProfileFragment.this.getContext(),
                        "Rating changed, current rating "+ ratingBar.getRating(),
                        Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }




}


