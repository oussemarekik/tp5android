package com.example.itcompanies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {

    private Context context;
    private List<Compines> companyList;

    public CompanyAdapter(Context context, List<Compines> companyList) {
        this.context = context;
        this.companyList = companyList;
    }

    @Override
    public CompanyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.company_item, parent, false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompanyViewHolder holder, int position) {
        final Compines company = companyList.get(position);

        holder.nameTextView.setText(company.getName());
        // Remplacez par l'image correspondante à chaque entreprise
        holder.companyImageView.setImageBitmap(company.getImage()); // Image par défaut, à remplacer

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("compines", company);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public static class CompanyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView companyImageView;

        public CompanyViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.companyName);
            companyImageView = itemView.findViewById(R.id.companyImage);
        }
    }
}
