package com.example.itcompanies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class CalanderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calander);
        CalendarView  calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year; // Les mois sont indexés à partir de 0
                Toast.makeText(CalanderActivity.this, "Date sélectionnée: " + selectedDate, Toast.LENGTH_SHORT).show();
                createCalendarEvent(year, month, dayOfMonth);

            }


        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void createCalendarEvent(int year, int month, int dayOfMonth) {
        // Créer une instance de Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 10, 0); // Exemple : 10h du matin

        // Créer un Intent pour ajouter un événement au calendrier
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(Uri.parse("content://com.android.calendar/events"));
        intent.putExtra("title", "Mon Événement"); // Définir le titre de l'événement
        intent.putExtra("eventLocation", "Mon Lieu"); // Optionnel : définir un lieu
        intent.putExtra("description", "Description de l'événement"); // Optionnel : définir une description
        intent.putExtra("beginTime", calendar.getTimeInMillis());
        intent.putExtra("endTime", calendar.getTimeInMillis() + 60 * 60 * 1000); // Durée d'1 heure
        intent.putExtra("allDay", false); // Définir si l'événement est une journée entière

        // Vérifier s'il existe une application de calendrier qui peut gérer l'Intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Aucune application de calendrier trouvée.", Toast.LENGTH_SHORT).show();
        }
    }
}