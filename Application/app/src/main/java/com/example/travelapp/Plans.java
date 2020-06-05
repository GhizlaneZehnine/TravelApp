package com.example.travelapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        plansList.add(new ModelPlans(R.drawable.bus, "Stations de transports", "Les plus proches stations de transports"));
        plansList.add(new ModelPlans(R.drawable.hopital, "Hôpitaux", "Les hôpitaux à proximité"));
        plansList.add(new ModelPlans(R.drawable.bank, "Banques", "Les banques à proximité"));
        plansList.add(new ModelPlans(R.drawable.mosque, "Mosqués", "Les mosqués à proximité"));
        plansList.add(new ModelPlans(R.drawable.salon, "Salon de beauté", "Les plus proches salons de beauté"));
        plansList.add(new ModelPlans(R.drawable.cinema, "Salles de cinéma", "Les salles de cinéma à proximité"));
        plansList.add(new ModelPlans(R.drawable.cafe, "Café", "Les cafés à proximité"));
        plansList.add(new ModelPlans(R.drawable.pharmacy, "Pharmacies", "Les pharmacies les plus proches en cas d'urgence"));
        plansList.add(new ModelPlans(R.drawable.poste, "La poste", "Les bureaux de postes à proximité"));
        plansList.add(new ModelPlans(R.drawable.hotels, "Hôtels", "Les meilleurs hôtels de la ville"));
        plansList.add(new ModelPlans(R.drawable.supermarche, "Super-marchés", "Les super-marchés à proximité"));
        plansList.add(new ModelPlans(R.drawable.mall, "Shopping mall", "Les meilleurs centres de shopping de la ville"));



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLilayoutManager = layoutManager;

        recyclerView.setLayoutManager(rvLilayoutManager);

        PlansAdapter adapter = new PlansAdapter(this,plansList);
        recyclerView.setAdapter(adapter);
    }
}
