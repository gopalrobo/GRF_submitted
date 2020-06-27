package com.example.grievanceregistrationform;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.grievanceregistrationform.app.GlideApp;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class PhotoSelectAdapterCopy extends RecyclerView.Adapter<PhotoSelectAdapterCopy.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Projectbean> mailbeans;
    private OnImageSelect onItemClick;
    ArrayList<String> images;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView itemsImage;
        CheckBox selectCheckBox;
        LinearLayout linear;

        public MyViewHolder(View view) {
            super((view));
            itemsImage = (ImageView) view.findViewById(R.id.itemsImage);
            selectCheckBox = (CheckBox) view.findViewById(R.id.selectCheckBox);
            linear = (LinearLayout) view.findViewById(R.id.linear);
        }
    }

    public PhotoSelectAdapterCopy(Context mainActivityUser,
                                  ArrayList<Projectbean> mailbeans,
                                  OnImageSelect onItemClick, ArrayList<String> images) {
        this.mainActivityUser = mainActivityUser;
        this.mailbeans = mailbeans;
        this.onItemClick = onItemClick;
        this.images = images;
    }

    public void notifyData(ArrayList<Projectbean> myList, ArrayList<String> images) {
        Log.d("notifyData ", myList.size() + "");
        this.mailbeans = myList;
        this.images = images;
        notifyDataSetChanged();
    }

    public PhotoSelectAdapterCopy.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mail_photo_select, parent, false);

        return new PhotoSelectAdapterCopy.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoSelectAdapterCopy.MyViewHolder holder, final int position) {
        Projectbean bean = mailbeans.get(position);
        String name = bean.getName();
        if (images.contains(name)) {
            holder.selectCheckBox.setChecked(true);
        } else {
            holder.selectCheckBox.setChecked(false);
        }
        try {
            GlideApp.with(mainActivityUser).load(bean.getAttachment())
                    .into(holder.itemsImage);
        } catch (Exception e) {
            Toast.makeText(mainActivityUser, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        holder.selectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onItemClick.onChecked(isChecked, position, name);
            }
        });

    }

    public int getItemCount() {
        return mailbeans.size();
    }

}
