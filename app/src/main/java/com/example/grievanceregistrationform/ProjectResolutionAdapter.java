package com.example.grievanceregistrationform;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grievanceregistrationform.app.AppConfig;
import com.example.grievanceregistrationform.app.GlideApp;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectResolutionAdapter extends RecyclerView.Adapter<ProjectResolutionAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Projectbean> projectbeans;
    private OnResolution onResolution;
    private OnItemClick onItemClick;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final CardView itemsCard, itemsAdd, itemsReportCard;
        private final ImageView itemsImage, itemsReportImage;
        private final TextView geodetails, description, geodetailsReport, descriptionReport,
                resolution, fullText, partialText, notText, closeText, escalateText;
        private LinearLayout partialLayout;

        public MyViewHolder(View view) {
            super((view));
            itemsAdd = (CardView) view.findViewById(R.id.itemsAdd);
            itemsCard = (CardView) view.findViewById(R.id.itemsCard);
            itemsImage = (ImageView) view.findViewById(R.id.itemsImage);
            geodetails = (TextView) view.findViewById(R.id.geodetails);
            description = (TextView) view.findViewById(R.id.description);
            itemsReportCard = (CardView) view.findViewById(R.id.itemsReportCard);
            itemsReportImage = (ImageView) view.findViewById(R.id.itemsReportImage);
            resolution = view.findViewById(R.id.resolution);
            geodetailsReport = (TextView) view.findViewById(R.id.geodetailsReport);
            descriptionReport = (TextView) view.findViewById(R.id.descriptionReport);
            fullText = view.findViewById(R.id.fullText);
            partialText = view.findViewById(R.id.partialText);
            notText = view.findViewById(R.id.notText);
            partialLayout = view.findViewById(R.id.partialLayout);
            closeText = view.findViewById(R.id.closeText);
            escalateText = view.findViewById(R.id.escalateText);

        }
    }

    public ProjectResolutionAdapter(Context mainActivityUser, ArrayList<Projectbean> projectbeans,
                                    OnResolution onResolution, OnItemClick onItemClick) {
        this.mainActivityUser = mainActivityUser;
        this.projectbeans = projectbeans;
        this.onResolution = onResolution;
        this.onItemClick = onItemClick;
    }

    public void notifyData(ArrayList<Projectbean> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.projectbeans = myList;
        notifyDataSetChanged();
    }

    public ProjectResolutionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projectdetail_row_resolution, parent, false);

        return new ProjectResolutionAdapter.MyViewHolder(itemView);
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
        try {
            holder.geodetailsReport.setText(AppConfig.df.format(Double.parseDouble(
                    projectbean.geotagReport.split(",")[0])) + "," + AppConfig.df.format(Double.parseDouble(
                    projectbean.geotagReport.split(",")[1])));
        } catch (Exception e) {

        }
        if (projectbean.detail.length() > 15) {
            holder.description.setText(projectbean.detail.substring(0, 14) + "...");
        } else {
            holder.description.setText(projectbean.detail);
        }
        if (projectbean.resolution != null) {
            holder.resolution.setText(projectbean.resolution);
        } else {
            holder.resolution.setText("");
        }

        if (projectbean.detailReport != null) {
            if (projectbean.detailReport.length() > 15) {
                holder.descriptionReport.setText(projectbean.detailReport.substring(0, 14) + "...");
            } else {
                holder.descriptionReport.setText(projectbean.detailReport);
            }
        } else {
            holder.descriptionReport.setText("");
        }

        try {
            GlideApp.with(mainActivityUser).load(projectbean.getAttachment())
                    .into(holder.itemsImage);
        } catch (Exception e) {
            Toast.makeText(mainActivityUser, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        if (projectbean.attachmentReport != null && projectbean.attachmentReport.length() > 0) {
            holder.itemsAdd.setVisibility(View.GONE);
            holder.itemsReportCard.setVisibility(View.VISIBLE);
            try {
                GlideApp.with(mainActivityUser).load(projectbean.getAttachmentReport())
                        .into(holder.itemsReportImage);
            } catch (Exception e) {
                Toast.makeText(mainActivityUser, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            holder.itemsAdd.setVisibility(View.VISIBLE);
            holder.itemsReportCard.setVisibility(View.GONE);
        }

        holder.itemsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemProjectReportDetailClick(position);
            }
        });

        holder.itemsReportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemProjectReportDetailClick(position);
            }
        });


        holder.itemsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.itemProjectDetailClick(position);
            }
        });


        if (projectbean.feedback1 != null) {
            if (projectbean.feedback1.equals("full")) {
                holder.fullText.setTextColor(Color.parseColor("#ffffff"));
                holder.fullText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_filed));
                holder.partialText.setTextColor(Color.parseColor("#000000"));
                holder.partialText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
                holder.notText.setTextColor(Color.parseColor("#000000"));
                holder.notText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
                holder.partialLayout.setVisibility(View.GONE);
            } else if (projectbean.feedback1.equals("partial")) {
                holder.partialText.setTextColor(Color.parseColor("#ffffff"));
                holder.partialText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_filed));
                holder.fullText.setTextColor(Color.parseColor("#000000"));
                holder.fullText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
                holder.notText.setTextColor(Color.parseColor("#000000"));
                holder.notText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
                holder.partialLayout.setVisibility(View.VISIBLE);
            } else {
                holder.notText.setTextColor(Color.parseColor("#ffffff"));
                holder.notText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_filed));
                holder.fullText.setTextColor(Color.parseColor("#000000"));
                holder.fullText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
                holder.partialText.setTextColor(Color.parseColor("#000000"));
                holder.partialText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
                holder.partialLayout.setVisibility(View.VISIBLE);
            }
        } else {
            holder.fullText.setTextColor(Color.parseColor("#ffffff"));
            holder.fullText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_filed));
            holder.partialText.setTextColor(Color.parseColor("#000000"));
            holder.partialText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
            holder.notText.setTextColor(Color.parseColor("#000000"));
            holder.notText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
            holder.partialLayout.setVisibility(View.GONE);
        }

        if (projectbean.feedback2 != null) {
            if (projectbean.feedback2.equals("close")) {
                holder.closeText.setTextColor(Color.parseColor("#ffffff"));
                holder.closeText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_filed));
                holder.escalateText.setTextColor(Color.parseColor("#000000"));
                holder.escalateText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
            } else {
                holder.escalateText.setTextColor(Color.parseColor("#ffffff"));
                holder.escalateText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_filed));
                holder.closeText.setTextColor(Color.parseColor("#000000"));
                holder.closeText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));

            }
        } else {
            holder.closeText.setTextColor(Color.parseColor("#ffffff"));
            holder.closeText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_filed));
            holder.escalateText.setTextColor(Color.parseColor("#000000"));
            holder.escalateText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
        }
        holder.fullText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.fullText.setTextColor(Color.parseColor("#ffffff"));
                holder.fullText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_filed));
                holder.partialText.setTextColor(Color.parseColor("#000000"));
                holder.partialText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
                holder.notText.setTextColor(Color.parseColor("#000000"));
                holder.notText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
                holder.partialLayout.setVisibility(View.GONE);
                onResolution.onFeedOneClick(position,"full");
                onResolution.onFeedTwoClick(position,"close");
            }
        });
        holder.partialText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.partialText.setTextColor(Color.parseColor("#ffffff"));
                holder.partialText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_filed));
                holder.fullText.setTextColor(Color.parseColor("#000000"));
                holder.fullText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
                holder.notText.setTextColor(Color.parseColor("#000000"));
                holder.notText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
                holder.partialLayout.setVisibility(View.VISIBLE);
                onResolution.onFeedOneClick(position,"partial");
                onResolution.onFeedTwoClick(position,"close");
            }
        });
        holder.notText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.notText.setTextColor(Color.parseColor("#ffffff"));
                holder.notText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_filed));
                holder.fullText.setTextColor(Color.parseColor("#000000"));
                holder.fullText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
                holder.partialText.setTextColor(Color.parseColor("#000000"));
                holder.partialText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
                holder.partialLayout.setVisibility(View.VISIBLE);
                onResolution.onFeedOneClick(position,"not");
                onResolution.onFeedTwoClick(position,"close");
            }
        });

        holder.closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.closeText.setTextColor(Color.parseColor("#ffffff"));
                holder.closeText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_filed));
                holder.escalateText.setTextColor(Color.parseColor("#000000"));
                holder.escalateText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
                onResolution.onFeedTwoClick(position,"close");   }
        });
        holder.escalateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.escalateText.setTextColor(Color.parseColor("#ffffff"));
                holder.escalateText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_filed));
                holder.closeText.setTextColor(Color.parseColor("#000000"));
                holder.closeText.setBackground(mainActivityUser.getResources().getDrawable(R.drawable.rectangle_trans));
                onResolution.onFeedTwoClick(position,"escalate"); }
        });
    }

    public int getItemCount() {
        return projectbeans.size();
    }

}


