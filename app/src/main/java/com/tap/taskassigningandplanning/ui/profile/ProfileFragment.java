package com.tap.taskassigningandplanning.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.tap.taskassigningandplanning.R;
import com.tap.taskassigningandplanning.utils.team.TeamFragment;

public class ProfileFragment extends Fragment{

    private ListView ListView;
    private ImageView image;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile,
                container, false);
        //For List View ===========================================================
        ListView list = rootView.findViewById(R.id.listView1);

        ProfileFragment.customadapter ca = new ProfileFragment.customadapter();
        list.setAdapter(ca);

        //For Ratingbar ===========================================================
        RatingBar ratingBar = rootView.findViewById(R.id.myratingbar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                Toast.makeText(ProfileFragment.this.getContext(),
                        "Rating changed, current rating "+ ratingBar.getRating(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Start of Clicking Image to go to TeamFragment =================================

        //End of Clicking Image =========================================================

        return rootView;
    }

    //For List View ===========================================================
    class customadapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return images.length;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub

            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertview, ViewGroup arg2) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = getLayoutInflater();
            convertview = inflater.inflate(R.layout.customteamfragment, null);
            TextView tv = convertview.findViewById(R.id.textView1);
            TextView tv1 = convertview.findViewById(R.id.textView2);
            ImageView image = convertview
                    .findViewById(R.id.imageView1);
            tv.setText(names[position]);
            tv1.setText(positions[position]);
            image.setImageResource(images[position]);

            return convertview;
        }

    }

    String[] names = { "Niño Muring", "Rexsa Salvador", "Sarfel Mulawan", "Christian Donzal"};
    String[] positions = { "Leader", "Member", "Member", "Member"};
    int[] images = { R.drawable.muring_pf, R.drawable.salvador_pf, R.drawable.mulawan_pf,
            R.drawable.donzal_pf };


}




