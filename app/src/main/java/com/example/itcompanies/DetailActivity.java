package com.example.itcompanies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.Clock;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ImageView www=findViewById(R.id.iconWebsite);
        ImageView tel=findViewById(R.id.iconContact);
        Compines compines = (Compines) getIntent().getSerializableExtra("compines");

        TextView nameView = findViewById(R.id.textViewServices);

        ImageView imageView = findViewById(R.id.imageView8);
        ListView servicesListView = findViewById(R.id.servicesListView);

        nameView.setText(compines.getName());
        imageView.setImageResource(compines.getImage());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, compines.getServices());
        servicesListView.setAdapter(adapter);
        www.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri link= Uri.parse(compines.getWebsite());
                Intent web=new Intent(Intent.ACTION_VIEW,link);
                startActivity(web);
            }
        });
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number=Uri.parse("tel:"+compines.getPhone());
                Intent callIntent=new Intent(Intent.ACTION_DIAL,number);
                startActivity(callIntent);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}