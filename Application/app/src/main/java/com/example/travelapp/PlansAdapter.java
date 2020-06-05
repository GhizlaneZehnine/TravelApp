package com.example.travelapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<ModelPlans> mlist;
    PlansAdapter (Context context, ArrayList<ModelPlans> list ){
        mContext = context;
        mlist = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View view = layoutInflater.inflate(R.layout.rv_plans_items,parent,false);
        ViewHolder  viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelPlans planItem = mlist.get(position);
        ImageView image = holder.item_image;
        TextView name, description;

        image = holder.item_image;
        name = holder.item_name;
        description = holder.item_description;

        image.setImageResource(mlist.get(position).getImage());

        name.setText(planItem.getName());
        name.setContentDescription(planItem.getName());
        description.setText(planItem.getDescription());
        description.setContentDescription(planItem.getDescription());

    }

    @Override
    public int getItemCount() {

        return mlist.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        ImageView item_image;
        TextView item_name, item_description;
        Context context;

        public ViewHolder(View itemView){

            super(itemView);
            context = itemView.getContext();
            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);
            item_description = itemView.findViewById(R.id.item_description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra("name",item_name.getText().toString());

                    context.startActivity(intent);
                }
            });
        }
    }
}
