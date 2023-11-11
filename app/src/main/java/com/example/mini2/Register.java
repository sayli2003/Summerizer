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

public class Register extends AppCompatActivity {
    TextView texthyperlink;
    Button submit;
    EditText Email;
    EditText Passward;
    FirebaseAuth mAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();

        texthyperlink=findViewById(R.id.LoginNow);

        submit=findViewById(R.id.Submit_Register);
        Email=findViewById(R.id.EmailIDReg);
        Passward=findViewById(R.id.PasswrdReg);
        progressBar=findViewById(R.id.progressBar);

        texthyperlink.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if(currentUser != null){
                            Intent intent = new Intent(Register.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
        );

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email,password;
                        progressBar.setVisibility(View.VISIBLE);
                        email=String.valueOf(Email.getText());
                        password=String.valueOf(Passward.getText());

                        if(TextUtils.isEmpty(email)){
                            Toast.makeText(Register.this,"Enter Email",Toast.LENGTH_LONG).show();
                            return;
                        }
                        else if(TextUtils.isEmpty(password)){
                            Toast.makeText(Register.this,"Enter Email",Toast.LENGTH_LONG).show();
                            return;
                        }

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            progressBar.setVisibility(View.GONE);
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("Firebase Auth", "createUserWithEmail:success");
                                            Toast.makeText(Register.this, "Account Created.",
                                                    Toast.LENGTH_SHORT).show();
//                                            Intent intent =new Intent(Register.this,MainActivity.class);
//                                            startActivity(intent);
//                                            finish();

                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            progressBar.setVisibility(View.GONE);
                                            String errorMessage = task.getException().getMessage();
                                            Log.w("Firebase Auth", errorMessage, task.getException());
                                            Toast.makeText(Register.this, errorMessage,
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
        );



    }
}