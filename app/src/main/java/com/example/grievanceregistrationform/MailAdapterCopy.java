package com.example.grievanceregistrationform;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grievanceregistrationform.app.GlideApp;

import java.util.ArrayList;
import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MailAdapterCopy extends RecyclerView.Adapter<MailAdapterCopy.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Mailbean> mailbeans;
    private OnItemClick onItemClick;
    Map<String, String> nameProjectMap;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final CardView itemsCard, itemsAdd;
        private final ImageView itemsImage, itemsImage1;
        private final TextView geodetails, description, village, commune;

        public MyViewHolder(View view) {
            super((view));
            itemsCard = (CardView) view.findViewById(R.id.itemsCard);
            itemsAdd = (CardView) view.findViewById(R.id.itemsAdd);
            itemsImage = (ImageView) view.findViewById(R.id.itemsImage);
            itemsImage1 = (ImageView) view.findViewById(R.id.itemsImage1);
            //   itemsImage2 = (ImageView) view.findViewById(R.id.itemsImage2);
            geodetails = (TextView) view.findViewById(R.id.geodetails);
            description = (TextView) view.findViewById(R.id.description);
            village = (TextView) view.findViewById(R.id.village);
            commune = (TextView) view.findViewById(R.id.commune);

        }
    }

    public MailAdapterCopy(Context mainActivityUser, ArrayList<Mailbean> mailbeans,
                           OnItemClick onItemClick, Map<String, String> nameProjectMap) {
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

    public MailAdapterCopy.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mailaddress_row_copy, parent, false);

        return new MailAdapterCopy.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MailAdapterCopy.MyViewHolder holder, final int position) {
        Mailbean bean = mailbeans.get(position);

        holder.itemsCard.setVisibility(View.VISIBLE);
        holder.itemsAdd.setVisibility(View.GONE);
        holder.geodetails.setText(bean.name);
        holder.description.setText(bean.parentname);
        holder.village.setText(bean.village);
        holder.commune.setText(bean.commune);
        int k = 0;
        if (bean.images != null) {
            for (int i = 0; i < bean.images.size(); i++) {
                if (nameProjectMap.containsKey(bean.images.get(i))) {

                    try {
                        if (k == 0) {
                            GlideApp.with(mainActivityUser).load(nameProjectMap.get(
                                    bean.images.get(i)))
                                    .into(holder.itemsImage1);
                        } else if (k == 1) {
                            GlideApp.with(mainActivityUser).load(nameProjectMap.get(
                                    bean.images.get(i)))
                                    .into(holder.itemsImage);
                        } else if (k == 2) {
//                                GlideApp.with(mainActivityUser).load(nameProjectMap.get(
//                                        bean.images.get(i)))
//                                        .into(holder.itemsImage);
                        }
                        k++;
                    } catch (Exception e) {
                        Toast.makeText(mainActivityUser, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }


        holder.itemsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemMailaddressClick(position);
            }
        });
        holder.itemsCard.setOnClickListener(new View.OnClickListener() {
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
