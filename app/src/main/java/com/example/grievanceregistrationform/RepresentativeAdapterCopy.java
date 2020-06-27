package com.example.grievanceregistrationform;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RepresentativeAdapterCopy extends RecyclerView.Adapter<RepresentativeAdapterCopy.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Mailbean> mailbeans;
    private OnItemClick onItemClick;
    Map<String, String> nameProjectMap;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final CardView itemsCard, itemsAdd;
        private final TextView geodetails, description,village;

        public MyViewHolder(View view) {
            super((view));
            itemsCard = (CardView) view.findViewById(R.id.itemsCard);
            itemsAdd = (CardView) view.findViewById(R.id.itemsAdd);
            geodetails = (TextView) view.findViewById(R.id.geodetails);
            description = (TextView) view.findViewById(R.id.description);
            village = (TextView) view.findViewById(R.id.village);

        }
    }

    public RepresentativeAdapterCopy(Context mainActivityUser, ArrayList<Mailbean> mailbeans,
                                     CompliantLetterCopy onItemClick, Map<String, String> nameProjectMap) {
        this.mainActivityUser = mainActivityUser;
        this.mailbeans = mailbeans;
        this.onItemClick = onItemClick;
        this.nameProjectMap = nameProjectMap;
    }

    public void notifyData(ArrayList<Mailbean> myList, Map<String, String> nameProjectMap) {
        Log.d("notifyData ", myList.size() + "");
        this.mailbeans = myList;
        this.nameProjectMap = nameProjectMap;
        notifyDataSetChanged();
    }

    public RepresentativeAdapterCopy.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.representative_row_copy, parent, false);

        return new RepresentativeAdapterCopy.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RepresentativeAdapterCopy.MyViewHolder holder, final int position) {
        Mailbean bean = mailbeans.get(position);

            holder.itemsCard.setVisibility(View.VISIBLE);
            holder.itemsAdd.setVisibility(View.GONE);
            holder.geodetails.setText(bean.name);
            holder.description.setText(bean.Province+"\n"+bean.district);
            holder.village.setText(bean.village);


        holder.itemsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemRepresentativeClick(position);
            }
        });
        holder.itemsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemRepresentativeClick(position);
            }
        });
    }

    public int getItemCount() {
        return mailbeans.size();
    }

}
