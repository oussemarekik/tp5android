package com.example.itcompanies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setupCardClick((ImageView) findViewById(R.id.imageView),
                new Compines(R.drawable.actia, "Actia",
                        new ArrayList<>(Arrays.asList("Deep domain expertise", "Agile Scrum framework", "Java, JavaEE, .NET, HTML5 development", "Onshore–nearshore support")),
                        "https://www.actia.com", "+123456789"));

        setupCardClick((ImageView) findViewById(R.id.imageView2),
                new Compines(R.drawable.fod, "FOD",
                        new ArrayList<>(Arrays.asList("Expertise", "Consulting")),
                        "https://www.fod.com", "+987654321"));

        setupCardClick((ImageView) findViewById(R.id.imageView3),
                new Compines(R.drawable.sofrecom, "Sofrecom",
                        new ArrayList<>(Arrays.asList("Telecommunications", "Cloud Solutions")),
                        "https://www.sofrecom.com", "+5647382910"));

        setupCardClick((ImageView) findViewById(R.id.imageView4),
                new Compines(R.drawable.spark, "Spark",
                        new ArrayList<>(Arrays.asList("AI Development", "Data Science")),
                        "https://www.spark.com", "+1029384756"));

        setupCardClick((ImageView) findViewById(R.id.imageView5),
                new Compines(R.drawable.udini, "Udini",
                        new ArrayList<>(Arrays.asList("Digital Marketing", "SEO")),
                        "https://www.udini.com", "+1928374650"));

        setupCardClick((ImageView) findViewById(R.id.imageView6),
                new Compines(R.drawable.primatec, "Primatec",
                        new ArrayList<>(Arrays.asList("Embedded Systems", "Automotive")),
                        "https://www.primatec.com", "+2039485761"));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void setupCardClick(ImageView imageView, final Compines compines) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("compines", compines);
                startActivity(intent);
            }
        });
    }
}