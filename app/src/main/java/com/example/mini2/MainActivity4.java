package com.example.mini2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity4 extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        mAuth=FirebaseAuth.getInstance();

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(MainActivity4.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void Register(View view){
        Intent intent = new Intent(MainActivity4.this,Register.class);
        startActivity(intent);
        finish();
    }
    public void Login(View view){
        Intent intent = new Intent(MainActivity4.this,Login.class);
        startActivity(intent);
        finish();
    }
}