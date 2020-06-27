package com.example.grievanceregistrationform;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RepresentativeAdapter extends RecyclerView.Adapter<RepresentativeAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Representativebean> representativebeans;
    private OnItemClick onItemClick;



    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView name,Province,telephone,numberset;
        CardView parentLinear;

        public MyViewHolder(View view){
            super((view));
            name =(TextView) view.findViewById(R.id.name);
            Province =(TextView) view.findViewById(R.id.Province);
            numberset =(TextView) view.findViewById(R.id.numberset);
            telephone =(TextView) view.findViewById(R.id.telephone);
            parentLinear = (CardView) view.findViewById(R.id.parentLinear);
        }
    }

    public RepresentativeAdapter(Context mainActivityUser, ArrayList<Representativebean> representativebeans, Complaintletter onItemClick) {
        this.mainActivityUser = mainActivityUser;
        this.representativebeans = representativebeans;
        this.onItemClick = (OnItemClick) onItemClick;
    }

    public void notifyData(ArrayList<Representativebean> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.representativebeans = myList;
        notifyDataSetChanged();
    }
    public RepresentativeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.representative_row, parent, false);

        return new RepresentativeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RepresentativeAdapter.MyViewHolder holder, final int position) {
        Representativebean bean = representativebeans.get(position);
        holder.numberset.setText(String.valueOf(position+1));
        holder.name.setText(bean.name);
        holder.Province.setText(bean.Province);
        holder.telephone.setText(bean.mobile);




        holder.parentLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemRepresentativeClick(position);
            }
        });
    }
    public  int getItemCount(){
        return representativebeans.size();
    }

}
