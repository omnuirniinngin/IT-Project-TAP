package com.tap.taskassigningandplanning.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.tap.taskassigningandplanning.MainActivity;
import com.tap.taskassigningandplanning.R;
//Inserted

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;

import android.widget.Toast;


public class ProfileFragment extends Fragment {
    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        ListView list = (ListView) findViewById(R.id.listView1);
        customadapter ca = new customadapter();
        list.setAdapter(ca);

        RatingBar ratingBar = (RatingBar)findViewById(R.id.myratingbar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                //Toast.makeText(ProfileViewModel.this,
                       // "Rating changed, current rating "+ ratingBar.getRating(),
                        //Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    private Object findViewById(int listView1) {
        return null;
    }



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
            convertview = inflater.inflate(R.layout.custom, null);
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
