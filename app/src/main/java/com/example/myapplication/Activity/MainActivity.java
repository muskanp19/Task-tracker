package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.myapplication.AddTask;
import com.example.myapplication.R;
import com.example.myapplication.fragment.AFragment;
import com.example.myapplication.fragment.BFragment;
import com.example.myapplication.fragment.CFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button btnFragA, btnFragB, btnFragC;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth  auth;
    FirebaseUser user;
    private int selectedButtonId;
    Button button;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerlayout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationview);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//--------------------On Navigation Item click---------------------------------------------------------------------------------------------------------------------
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int id = item.getItemId();
                if(id == R.id.logout)
                {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else if (id == R.id.home_menu)
                {   Toast.makeText(MainActivity.this, "Home opened ",Toast.LENGTH_SHORT).show();
                }
                else if (id == R.id.add_task)
                {   Intent intent = new Intent(MainActivity.this, AddTask.class);
                    startActivity(intent);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        //----------------tags to fragments-------------------------------------------------------------------
        // Create instances of your fragments
        AFragment aFragment = new AFragment();
        BFragment bFragment = new BFragment();
        CFragment cFragment = new CFragment();

        // Add or replace fragments in the FrameLayout with tags
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, aFragment, "a_fragment_tag")
                .add(R.id.container, bFragment, "b_fragment_tag")
                .add(R.id.container, cFragment, "c_fragment_tag")
                .commit();


    //----------------------ADDING FRAGMENTS-----------------------------------------------------------------------------------
        btnFragA = findViewById(R.id.btnFragA);
        btnFragB = findViewById(R.id.btnFragB);
        btnFragC = findViewById(R.id.btnFragC);
        //default first fragment
        loadFrag(new BFragment(), 0,R.id.btnFragB);
        //FRAGMENT A
        btnFragA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFrag(new AFragment(),1,R.id.btnFragA);}
        });
        //FRAGMENT B
        btnFragB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFrag(new BFragment(),1,R.id.btnFragB);
            }
        });
        //FRAGMENT C
        btnFragC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                loadFrag(new CFragment(),1,R.id.btnFragC);
            }
        });


    }

        //Method for loading fragments

        public void loadFrag(Fragment fragment,int flag, int buttonId)
        {
            selectedButtonId = buttonId;
            updateButtonColors();


            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            //adding fragments
            if (flag == 0) {
                ft.add(R.id.container, fragment);
            } else {
                ft.replace(R.id.container, fragment);
            }
            ft.commit();
        }

    private void updateButtonColors() {
        btnFragA.setBackgroundColor(btnFragA.getId() == selectedButtonId ? Color.DKGRAY : getResources().getColor(R.color.colorPrimary));
        btnFragB.setBackgroundColor(btnFragB.getId() == selectedButtonId ? Color.DKGRAY : getResources().getColor(R.color.colorPrimary));
        btnFragC.setBackgroundColor(btnFragC.getId() == selectedButtonId ? Color.DKGRAY : getResources().getColor(R.color.colorPrimary));
    }


    @Override
    public void onBackPressed()
    {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }



}

