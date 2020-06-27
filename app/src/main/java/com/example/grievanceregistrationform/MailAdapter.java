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

public class MailAdapter extends RecyclerView.Adapter<MailAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Mailbean> mailbeans;
    private OnItemClick onItemClick;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name, Province,mobile,numberset;
        CardView parentLinear;

        public MyViewHolder(View view) {
            super((view));
            name = (TextView) view.findViewById(R.id.name);
            Province = (TextView) view.findViewById(R.id.Province);
            mobile = (TextView) view.findViewById(R.id.mobile);
            numberset = (TextView) view.findViewById(R.id.numberset);

            parentLinear = (CardView) view.findViewById(R.id.parentLinear);
        }
    }

    public MailAdapter(Context mainActivityUser, ArrayList<Mailbean> mailbeans, Complaintletter onItemClick) {
        this.mainActivityUser = mainActivityUser;
        this.mailbeans = mailbeans;
        this.onItemClick = (OnItemClick) onItemClick;
    }

    public void notifyData(ArrayList<Mailbean> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.mailbeans = myList;
        notifyDataSetChanged();
    }

    public MailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mailaddress_row, parent, false);

        return new MailAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MailAdapter.MyViewHolder holder, final int position) {
        Mailbean bean = mailbeans.get(position);
        holder.numberset.setText(String.valueOf(position+1));
        holder.name.setText(bean.name);
        holder.Province.setText(bean.Province);
        holder.mobile.setText(bean.mobile);
        holder.parentLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemMailaddressClick(position);
            }
        });
    }

    public int getItemCount() {
        return mailbeans.size();
    }

}
