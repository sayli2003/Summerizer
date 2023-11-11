package com.example.mini2;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        final ArrayList<Note> dataArrayList = new ArrayList<>();
        final ArrayList<String> NotesList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Log.v("Reached","No Data");
        databaseReference = FirebaseDatabase.getInstance().getReference(user.getEmail().toString().substring(0,user.getEmail().toString().indexOf("@")));
        Log.v("Reached","No Data2");
        ListView notelist=(ListView) findViewById(R.id.Historyview);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("Reached","No Data3");
                if (dataSnapshot.exists()) {
                    Log.v("Reached","No Data3");
                    for (DataSnapshot titleSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot noteSnapshot : titleSnapshot.getChildren()) {
                            String title = noteSnapshot.child("title").getValue().toString();
                            String Value = noteSnapshot.child("summery").getValue().toString();

                            Note note=new Note(title,title,Value);
                            dataArrayList.add(note);
                            NotesList.add(title);
                        }

                    }
                    notelist.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,NotesList));
                    notelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent=new Intent(MainActivity2.this,MainActivity3.class);
                            intent.putExtra("title",dataArrayList.get(position).Title);
                            intent.putExtra("summery",dataArrayList.get(position).Summery);
                            startActivity(intent);
                        }
                    });

                } else {
                    // Data doesn't exist for the specified email ID
                    Log.v("Not GOT DATA","No Data");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // An error occurred
            }
        });
//        Log.v("DATA",NotesList.get(0));

    }
}