package com.example.itcompanies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    RecyclerView recyclerView;
    CompanyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);
        dbHelper.addSampleData(this);
        recyclerView = findViewById(R.id.companiesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lire toutes les entreprises et les afficher dans le RecyclerView
        updateCompanyList();

        Button addCompanyButton = findViewById(R.id.buttonCreateCompany);
        addCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lancer l'activité de création d'entreprise
                Intent intent = new Intent(MainActivity.this, CreateCompanyActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Rafraîchir la liste des entreprises après ajout
        updateCompanyList();
    }

    private void updateCompanyList() {
        ArrayList<Compines> companies = dbHelper.getAllCompanies(this);
        adapter = new CompanyAdapter(this, companies);
        recyclerView.setAdapter(adapter);
    }
}
