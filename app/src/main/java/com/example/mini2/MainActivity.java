package com.example.mini2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button logout;
    Button add;
    TextView text;
    FirebaseUser user;
    private String courseID;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(MainActivity.this,Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(MainActivity.this,Login.class);
            startActivity(intent);
            finish();
        }else{
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference(user.getEmail().toString().substring(0,user.getEmail().toString().indexOf("@")));


//        logout=findViewById(R.id.LogoutButton);
//        add=findViewById(R.id.addToDB);
//        text=findViewById(R.id.HelloText);

        if(user== null){
            Intent intent=new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }
        else{
            Log.v("GOOD","Login successful"+user.getEmail().toString());
        }
            drawerLayout = findViewById(R.id.my_drawer_layout);

            //Connection the Drawer with the toolbar fo the application
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
//            TextView displayemail=findViewById(R.id.useremail);
//            displayemail.setText(user.getEmail().toString());
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            else{
                // Create and display a short-duration toast message
                Toast.makeText(this, "action bar is null", Toast.LENGTH_SHORT).show();

            }

            //Using this to connect the Fragments to the UI of the app
            NavigationView navview=findViewById(R.id.navView);
            navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                                                          @Override
                                                          public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                                                              Log.v("Here","ProcessNav");
                                                              Fragment selectedFragment = null;
                                                              int itemId = menuItem.getItemId();
                                                              if (itemId == R.id.nav_home) {
                                                                  selectedFragment = new HomeFragment();
                                                              } else if (itemId == R.id.nav_history) {
                                                                  selectedFragment = new HistoryFragment();
                                                              }else if(itemId == R.id.nav_logout){
                                                                  selectedFragment=new LogoutFragment();
                                                                  Log.v("User",user.getEmail().toString());
                                                                  FirebaseAuth.getInstance().signOut();
                                                                  Intent intent=new Intent(getApplicationContext(),Login.class);
                                                                  startActivity(intent);
                                                                  finish();
                                                              }
                                                              if (selectedFragment != null) {
                                                                  getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
                                                              }
                                                              return true;
                                                          }
                                                      }

            );



//        logout.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        FirebaseAuth.getInstance().signOut();
//                        Intent intent=new Intent(getApplicationContext(),Login.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//        );

//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // getting data from our edit text.
//                String Title = "Animal Kingdom";
//                String Sums = "Lorem jubdjsnso scunwoec wjow cwcnoecwewic cwincoiwc weconwc wec wec wec we cwecw ef   e  v  v vss  se s cs e sec  se fvververv ev er v e ve r e v er ve rv ree   gere r  e rf e rf ";
//                courseID = Title;
//                // on below line we are passing all data to our modal class.
//                Note putnote = new Note(courseID, Title, Sums);
//                // on below line we are calling a add value event
//                // to pass data to firebase database.
//                Log.e("GOOD","not Added yet");
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        // on below line we are setting data in our firebase database.
//                        databaseReference.child("Notes").child(courseID).setValue(putnote);
//                        // displaying a toast message.
//                        Log.e("GOOD","Added");
//                        Toast.makeText(MainActivity.this, "Course Added..", Toast.LENGTH_SHORT).show();
//                        // starting a main activity
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        // displaying a failure message on below line.
//                        Log.e("BAD","Got Cancelled");
//                        Toast.makeText(MainActivity.this, "Fail to add Course..", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
    }
    }
//    public void DisplayNotes(View view){
//        startActivity(new Intent(MainActivity.this, MainActivity2.class));
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        Log.v(itemId+"",(R.id.nav_logout)+"");
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_history) {
                selectedFragment = new HistoryFragment();
            }
            else if(itemId == R.id.nav_logout){
                selectedFragment=new LogoutFragment();
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}