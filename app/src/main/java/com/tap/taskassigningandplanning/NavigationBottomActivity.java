package com.tap.taskassigningandplanning;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class NavigationBottomActivity extends AppCompatActivity {

    private String plan_id;
    private Bundle id_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottom_navigation);

        Intent intent = getIntent();
        plan_id = intent.getExtras().getString("plan_id");


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_activities, R.id.navigation_progress, R.id.navigation_task,
                R.id.navigation_chart, R.id.navigation_team)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public Bundle getMyId(){
        Bundle bundle = new Bundle();
        bundle.putString("plan_id", plan_id);
        return bundle;
    }

//    public boolean onKeyDown(int keyCode, KeyEvent event){
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            startActivity(new Intent(this, MainActivity.class));
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}
