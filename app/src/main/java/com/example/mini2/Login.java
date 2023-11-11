package com.example.mini2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextView texthyperlink;
    Button submit;
    EditText Email;
    EditText Passward;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        texthyperlink=findViewById(R.id.RegisterNow);
        submit=findViewById(R.id.SubmitLog);
        Email=findViewById(R.id.EmailIDLog);
        Passward=findViewById(R.id.PasswdLog);
        progressBar=findViewById(R.id.progressBarLog);
        mAuth=FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        texthyperlink.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(Login.this,Register.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email=String.valueOf(Email.getText()),password=String.valueOf(Passward.getText());
                        progressBar.setVisibility(View.VISIBLE);
                        if(TextUtils.isEmpty(email)){
                            Toast.makeText(Login.this,"Enter Email",Toast.LENGTH_LONG).show();
                            Log.v("My TAG","Enter Email ID");
                            return;
                        }
                        if(TextUtils.isEmpty(password)){
                            Toast.makeText(Login.this,"Enter password",Toast.LENGTH_LONG).show();
                            Log.v("My TAG","Enter Password");
                            return;
                        }

                        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    progressBar.setVisibility(View.GONE);
                                    Log.d("Success", "signInWithEmail:success");
                                    Toast.makeText(Login.this,"Successful",Toast.LENGTH_LONG);
                                    Intent intent = new Intent(Login.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    progressBar.setVisibility(View.GONE);
                                    Log.w("Fail", "", task.getException());
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(Login.this,errorMessage,Toast.LENGTH_LONG);

                                }
                            }
                        });
                    }
                }
        );
    }
}