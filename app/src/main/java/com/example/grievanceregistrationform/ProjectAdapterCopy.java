package com.example.grievanceregistrationform;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grievanceregistrationform.app.AppConfig;
import com.example.grievanceregistrationform.app.GlideApp;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectAdapterCopy extends RecyclerView.Adapter<ProjectAdapterCopy.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Projectbean> projectbeans;
    private OnItemClick onItemClick;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final CardView itemsCard, itemsAdd;
        private final ImageView itemsImage;
        private final TextView geodetails, description;

        public MyViewHolder(View view) {
            super((view));
            itemsCard = (CardView) view.findViewById(R.id.itemsCard);
            itemsAdd = (CardView) view.findViewById(R.id.itemsAdd);
            itemsImage = (ImageView) view.findViewById(R.id.itemsImage);
            geodetails = (TextView) view.findViewById(R.id.geodetails);
            description = (TextView) view.findViewById(R.id.description);

        }
    }

    public ProjectAdapterCopy(Context mainActivityUser, ArrayList<Projectbean> projectbeans,
                              CompliantLetterCopy onItemClick) {
        this.mainActivityUser = mainActivityUser;
        this.projectbeans = projectbeans;
        this.onItemClick = (OnItemClick) onItemClick;
    }

    public void notifyData(ArrayList<Projectbean> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.projectbeans = myList;
        notifyDataSetChanged();
    }

    public ProjectAdapterCopy.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projectdetail_row_copy, parent, false);

        return new ProjectAdapterCopy.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Projectbean projectbean = projectbeans.get(position);

        holder.itemsCard.setVisibility(View.VISIBLE);
        holder.itemsAdd.setVisibility(View.GONE);
        holder.geodetails.setText(projectbean.geotag);
        try {
            holder.geodetails.setText(AppConfig.df.format(Double.parseDouble(
                    projectbean.geotag.split(",")[0])) + "," + AppConfig.df.format(Double.parseDouble(
                    projectbean.geotag.split(",")[1])));
        } catch (Exception e) {

        }
        if (projectbean.detail.length() > 15) {
            holder.description.setText(projectbean.detail.substring(0, 14) + "...");
        } else {
            holder.description.setText(projectbean.detail);
        }
        try {
            GlideApp.with(mainActivityUser).load(projectbean.getAttachment())
                    .into(holder.itemsImage);
        } catch (Exception e) {
            Toast.makeText(mainActivityUser, "Something went wrong", Toast.LENGTH_SHORT).show();
        }


        holder.itemsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemProjectDetailClick(position);
            }
        });
        holder.itemsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemProjectDetailClick(position);
            }
        });
    }

    public int getItemCount() {
        return projectbeans.size();
    }

}


