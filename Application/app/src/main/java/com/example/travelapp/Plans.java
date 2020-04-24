package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Plans extends AppCompatActivity {
    RecyclerView recyclerView;

    ArrayList<ModelPlans> plansList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);
        recyclerView = findViewById(R.id.rv);

        plansList = new ArrayList<>();

        plansList.add(new ModelPlans(R.drawable.restaurant, "Restaurants", "Les meilleurs restaurants de la ville"));
        plansList.add(new ModelPlans(R.drawable.monument, "Monuments", "Les monuments à voir!"));
        plansList.add(new ModelPlans(R.drawable.bus, "Stations de bus/tram", "Les plus proches stations de transports"));
        plansList.add(new ModelPlans(R.drawable.hopital, "Hôpitaux", "Les hôpitaux à proximité"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLilayoutManager = layoutManager;

        recyclerView.setLayoutManager(rvLilayoutManager);

        PlansAdapter adapter = new PlansAdapter(this,plansList);
        recyclerView.setAdapter(adapter);
    }
}
