package com.example.itcompanies;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.example.itcompanies.R;  // Ensure this is the correct import

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    RecyclerView recyclerView;
    CompanyAdapter adapter;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        // Set up the Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.companiesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Populate the RecyclerView with companies
        updateCompanyList();

        // Add company button
        Button addCompanyButton = findViewById(R.id.buttonCreateCompany);
        addCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the activity to create a new company
                Intent intent = new Intent(MainActivity.this, CreateCompanyActivity.class);
                startActivity(intent);
            }
        });

        // Set up the ActionBarDrawerToggle for opening/closing the drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up the navigation item selection listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_logout:
                        // Handle Home action
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                }
                // Close the drawer
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the list of companies when returning to the main activity
        updateCompanyList();
    }

    private void updateCompanyList() {
        // Get all companies from the database and update the RecyclerView
        ArrayList<Compines> companies = dbHelper.getAllCompanies(this);
        adapter = new CompanyAdapter(this, companies);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        // Close the navigation drawer if it's open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
