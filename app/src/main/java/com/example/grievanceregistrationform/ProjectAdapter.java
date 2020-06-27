package com.example.grievanceregistrationform;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grievanceregistrationform.app.GlideApp;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Projectbean> projectbeans;
    private OnItemClick onItemClick;



    public class MyViewHolder extends RecyclerView.ViewHolder{

        private final CircleImageView plotimg;
        private final TextView plotname;
        TextView plotarea;

        CardView parentLinear;

        public MyViewHolder(View view){
            super((view));
            plotimg = (CircleImageView) view.findViewById(R.id.profile_image);
            plotname = (TextView) view.findViewById(R.id.plotname);
            plotarea = (TextView) view.findViewById(R.id.areaofplot);
            parentLinear = (CardView) view.findViewById(R.id.parentLinear);
        }
    }

    public ProjectAdapter(Context mainActivityUser, ArrayList<Projectbean> projectbeans,
                          Complaintletter onItemClick) {
        this.mainActivityUser = mainActivityUser;
        this.projectbeans = projectbeans;
        this.onItemClick = (OnItemClick) onItemClick;
    }

    public void notifyData(ArrayList<Projectbean> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.projectbeans = myList;
        notifyDataSetChanged();
    }
    public ProjectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projectdetail_row, parent, false);

        return new ProjectAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Projectbean projectbean = projectbeans.get(position);
        try {
            holder.plotname.setText(projectbean.getName());
            holder.plotarea.setText(projectbean.getDetail());
            GlideApp.with(mainActivityUser).load(projectbean.getAttachment())
                    .dontAnimate()
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.profile)
                    .into(holder.plotimg);
        } catch (Exception e) {
            Toast.makeText(mainActivityUser, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        holder.parentLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemProjectDetailClick(position);
            }
        });
    }
    public  int getItemCount(){
        return projectbeans.size();
    }

}


