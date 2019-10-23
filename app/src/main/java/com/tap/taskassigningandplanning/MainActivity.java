package com.tap.taskassigningandplanning;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private AppBarConfiguration mAppBarConfiguration;
    private String userID;
    private TextView tvName, tvEmail, tvSubmenu;
    private ImageView imageView;
    private TextDrawable textDrawable;
    private ColorGenerator generator;

    int count = 0;

    //FIREBASE
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, Login.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener!=null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        userID = firebaseUser.getUid();

        final DrawerLayout drawer = findViewById(R.id.drawer_navigation);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_plan, R.id.nav_notification,
                R.id.nav_profile, R.id.nav_setting, R.id.nav_plan)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment); //content_main
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                    //tvEmail.setText(user.getEmail()); //get email displayed using this method
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

        // get plan_id to get all plans created
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // get menu from navigationView
//        Menu menu = navigationView.getMenu();
//        final SubMenu subMenu = menu.addSubMenu("Recent");
//
//        db.collection("Plan")
//                .whereEqualTo("user_id", userId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            QuerySnapshot querySnapshot = task.getResult();
//                            count = querySnapshot.size();
//                            System.out.println(count);
//                            for (DocumentSnapshot document: querySnapshot.getDocuments()) {
//                                Log.d(TAG, "onComplete: Found incoming id " + count);
//
//                                subMenu.add(0, count++, Menu.NONE, document.get("title").toString());
//                            }
//                        }
//                    }
//                });

        generator = ColorGenerator.MATERIAL;
        imageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tvName);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvEmail);

        DocumentReference documentReference = db.collection("Users").document(userId);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());

                    User user = documentSnapshot.toObject(User.class);
                    tvName.setText(user.getName());
                    tvEmail.setText(user.getEmail());
                    textDrawable = TextDrawable.builder().buildRoundRect(String.valueOf(user.getName().charAt(0)), generator.getRandomColor(), 8);
                    imageView.setImageDrawable(textDrawable);

                } else {
                    Log.d(TAG, "Error getting info");
                }
            }
        });

/*        //Go to your specific database directory or Child
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        //Connect the views of navigation bar
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tvName);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvEmail);

        generator = ColorGenerator.MATERIAL;

        imageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);

        //Use you DB reference object and add this method to access realtime data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Fetch values from you database child and set it to the specific view object.
                tvName.setText(dataSnapshot.child("name").getValue().toString());
                tvEmail.setText(dataSnapshot.child("email").getValue().toString());
                textDrawable = TextDrawable.builder().buildRoundRect(String.valueOf(dataSnapshot.child("name").getValue().toString().charAt(0)), generator.getRandomColor(), 8);
                imageView.setImageDrawable(textDrawable);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}

/*New Layout*/
//import android.app.Notification;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.SubMenu;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.navigation.NavController;
//import androidx.navigation.NavOptions;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
//
//import com.amulyakhare.textdrawable.TextDrawable;
//import com.amulyakhare.textdrawable.util.ColorGenerator;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.navigation.NavigationView;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.tap.taskassigningandplanning.ui.notification.NotificationFragment;
//import com.tap.taskassigningandplanning.ui.plan.Plan;
//import com.tap.taskassigningandplanning.ui.plan.PlanFragment;
//import com.tap.taskassigningandplanning.ui.profile.ProfileFragment;
//import com.tap.taskassigningandplanning.ui.setting.SettingFragment;
//import com.tap.taskassigningandplanning.utils.activities.ActivityClicked;
//import com.tap.taskassigningandplanning.utils.activities.ActivityDialogFragment;
//import com.tap.taskassigningandplanning.utils.chart.ChartFragment;
//import com.tap.taskassigningandplanning.utils.progress.ProgressFragment;
//import com.tap.taskassigningandplanning.utils.task.TaskFragment;
//import com.tap.taskassigningandplanning.utils.team.TeamFragment;
//
//import org.w3c.dom.Text;
//
//public class MainActivity extends AppCompatActivity{
//    private static final String TAG = "MainActivity";
//
//    private AppBarConfiguration mAppBarConfiguration;
//    private String userID;
//    private TextView tvName, tvEmail, tvSubmenu;
//    private ImageView imageView;
//    private TextDrawable textDrawable;
//    private ColorGenerator generator;
//
//    int count = 0;
//
//    //FIREBASE
//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private FirebaseDatabase firebaseDatabase;
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthStateListener;
//    private DatabaseReference databaseReference;
//
//    private ActionBar toolbar;
//    private NavigationView navigationView;
//    private DrawerLayout drawer;
////    private ActionBarDrawerToggle toggle;
//
//    private Intent intent;
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(mAuth.getCurrentUser() == null){
//            finish();
//            startActivity(new Intent(this, Login.class));
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(mAuthStateListener!=null){
//            mAuth.removeAuthStateListener(mAuthStateListener);
//        }
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_drawer);
//
//        Toolbar toolbar1 = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar1);
//
//        drawer = findViewById(R.id.drawer_layout);
//        loadFragment(new ActivityDialogFragment());
//
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(nNavigationItemSelectedListener);
//
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnNavigationItemSelectedListener(bNavigationItemSelectedListener);
//
//
//
//        mAuth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//        userID = firebaseUser.getUid();
//
//        generator = ColorGenerator.MATERIAL;
//        imageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
//        tvName = navigationView.getHeaderView(0).findViewById(R.id.tvName);
//        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
//
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        DocumentReference documentReference = db.collection("Users").document(userId);
//
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                DocumentSnapshot documentSnapshot = task.getResult();
//                if (documentSnapshot.exists()) {
//                    Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());
//
//                    User user = documentSnapshot.toObject(User.class);
//                    tvName.setText(user.getName());
//                    tvEmail.setText(user.getEmail());
//                    textDrawable = TextDrawable.builder().buildRoundRect(String.valueOf(user.getName().charAt(0)), generator.getRandomColor(), 8);
//                    imageView.setImageDrawable(textDrawable);
//
//                } else {
//                    Log.d(TAG, "Error getting info");
//                }
//            }
//        });
//
//        // get menu from navigationView
//        Menu menu = navigationView.getMenu();
//        final SubMenu subMenu = menu.addSubMenu("Recent");
//
//        db.collection("Plan")
//                .whereEqualTo("user_id", userId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            QuerySnapshot querySnapshot = task.getResult();
//                            count = querySnapshot.size();
//                            System.out.println(count);
//                            for (DocumentSnapshot document: querySnapshot.getDocuments()) {
//                                Log.d(TAG, "onComplete: Found incoming id " + count);
//
//                                subMenu.add(0, count++, Menu.NONE, document.get("title").toString()); //HAHAHAHAHHAA I AM THE GREATEST CHAROT!
//                            }
//                        }
//                    }
//                });
//
//    }
//
//    private void loadFragment(Fragment fragment) {
//        // load fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//
//
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////
////        switch (item.getItemId()){
////
////            case android.R.id.home:
////                if(drawer.isDrawerOpen(GravityCompat.START)){
////                    drawer.closeDrawer(GravityCompat.START);
////                    return true;
////                }
////                else{
////                    return false;
////                }
////        }
////
////        return super.onOptionsItemSelected(item);
////    }
//
//    @Override
//    public void onBackPressed() {
//        if(drawer.isDrawerOpen(GravityCompat.START)){
//            drawer.closeDrawer(GravityCompat.START);
//        }
//        else{
//            super.onBackPressed();
//        }
//    }
//
//    // Bottom Navigation //
//    private BottomNavigationView.OnNavigationItemSelectedListener bNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment fragment;
//            switch (item.getItemId()) {
//                case R.id.navigation_activities:
//                    getSupportActionBar().setTitle("Activity");
//                    fragment = new ActivityDialogFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_progress:
//                    getSupportActionBar().setTitle("Progress");
//                    fragment = new ProgressFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_task:
//                    getSupportActionBar().setTitle("Task");
//                    fragment = new TaskFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_chart:
//                    getSupportActionBar().setTitle("Chart");
//                    fragment = new ChartFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_team:
//                    getSupportActionBar().setTitle("Team");
//                    fragment = new TeamFragment();
//                    loadFragment(fragment);
//                    return true;
//            }
//            return false;
//        }
//    };
//
//    // Navigation Drawer //
//    private NavigationView.OnNavigationItemSelectedListener nNavigationItemSelectedListener
//            = new NavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.nav_plan:
//                    intent = new Intent(MainActivity.this, PlanFragment.class);
//                    drawer.closeDrawers();
//                    startActivity(intent);
//                    break;
//                case R.id.nav_notification:
//                    intent = new Intent(MainActivity.this, NotificationFragment.class);
//                    drawer.closeDrawers();
//                    startActivity(intent);
//                    return true;
//                case R.id.nav_profile:
//                    getSupportActionBar().setTitle("Profile");
//                    return true;
//                case R.id.nav_setting:
//                    intent = new Intent(MainActivity.this, SettingFragment.class);
//                    drawer.closeDrawers();
//                    startActivity(intent);
//                    return true;
//            }
//            return false;
//        }
//    };
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//}
