package com.example.mini2;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_history, container, false);
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        final ArrayList<Note> dataArrayList = new ArrayList<>();
        final ArrayList<String> NotesList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Log.v("Reached","No Data");
        databaseReference = FirebaseDatabase.getInstance().getReference(user.getEmail().toString().substring(0,user.getEmail().toString().indexOf("@")));
        Log.v("Reached","No Data2");
        ListView notelist=(ListView) view.findViewById(R.id.Historyview);

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
                    notelist.setAdapter(new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1,NotesList));
                    notelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent=new Intent(view.getContext(),MainActivity3.class);
                            Note note=dataArrayList.get(position);
                            intent.putExtra("title",note.Title);
                            intent.putExtra("summery",note.Summery);
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
        return view;
    }
}