package com.example.sqlconnected;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity2_Register extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button registerButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_register);

        dbHelper = new DatabaseHelper(this);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Insert user data into the database
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("username", username);
                values.put("password", password);
                long newRowId = db.insert("users", null, values);

                // Check if the insertion was successful
                if (newRowId != -1) {
                    Toast.makeText(MainActivity2_Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    // Handle successful registration, navigate to another activity, etc.
                } else {
                    Toast.makeText(MainActivity2_Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }

                // Close the database connection
                db.close();
            }
        });
    }
}
