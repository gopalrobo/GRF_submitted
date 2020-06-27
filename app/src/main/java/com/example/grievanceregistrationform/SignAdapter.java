package com.example.grievanceregistrationform;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grievanceregistrationform.app.GlideApp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SignAdapter extends RecyclerView.Adapter<SignAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Signbean> signbeans;
    private OnItemClick onItemClick;



    public class MyViewHolder extends RecyclerView.ViewHolder{

        private FloatingActionButton addImage;
        private TextView nameofCompliant;
        private ImageView signImg;
        CardView parentLinear;

        public MyViewHolder(View view){
            super((view));
             nameofCompliant=(TextView)view.findViewById(R.id.nameofCompliant);
            parentLinear = (CardView) view.findViewById(R.id.parentLinear);
            signImg=view.findViewById(R.id.signImg);
            addImage=(FloatingActionButton)view.findViewById(R.id.addImage);
        }
    }

    public SignAdapter(Context mainActivityUser, ArrayList<Signbean> signbeans, Complaintletter onItemClick) {
        this.mainActivityUser = mainActivityUser;
        this.signbeans = signbeans;
        this.onItemClick = (OnItemClick) onItemClick;
    }

    public void notifyData(ArrayList<Signbean> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.signbeans = myList;
        notifyDataSetChanged();
    }
    public SignAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.signdetail, parent, false);

        return new SignAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SignAdapter.MyViewHolder holder, final int position) {
        Signbean bean = signbeans.get(position);
        holder.nameofCompliant.setText(bean.name);

        if (bean.getFilePath().length()>0){
            holder.signImg.setVisibility(View.VISIBLE);
            GlideApp.with(mainActivityUser).load(bean.filePath)
                    .dontAnimate()
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.file)
                    .into(holder.signImg);
        }else{
            holder.signImg.setVisibility(View.GONE);
        }


        holder.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemSignClick(position);
            }
        });
    }
    public  int getItemCount(){
        return signbeans.size();
    }

}
