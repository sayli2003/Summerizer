package com.example.mini2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentTransaction fragmentTransaction;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.Audio) {
            selectedFragment = new AudioFragment();
        } else if (itemId == R.id.text) {
            selectedFragment = new TextFragment();
        } else if (itemId == R.id.Video) {
            selectedFragment = new VideoFragment();
        }
        // It will help to replace the
        // one fragment to other.
        if (selectedFragment != null) {
            fragmentTransaction.replace(R.id.frame_layout, selectedFragment).commit();
        }
        return true;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.innerframelay, new TextFragment()).commit();
        BottomNavigationView bottomNav = view.findViewById(R.id.bottomNavigationView);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Fragment selectedFragment = null;
                if (itemId == R.id.Audio) {
                    selectedFragment=new AudioFragment();
                    String name=itemId+"  "+R.id.Audio;
                    Log.e("MyTag", name);
                } else if (itemId == R.id.text) {
                    selectedFragment = new TextFragment();
                    String name=itemId+"  "+R.id.text;
                    Log.e("MyTag", name);
                } else if (itemId == R.id.Video) {
                    selectedFragment = new VideoFragment();
                    String name=itemId+"  "+R.id.Video;
                    Log.e("MyTag", name);
                }

                if (selectedFragment != null) {
                    Log.e("Another Tag","selected Fragment is not null");
//                    fragmentTransaction.replace(R.id.innerframelay, selectedFragment).commit();
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.innerframelay, selectedFragment)
                            .commit();
                }
                else {
                    Log.e("Another Tag","selected Fragment is null");
                }

                return true;
            }
        });
        return view;
    }
}