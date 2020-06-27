package com.example.grievanceregistrationform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.grievanceregistrationform.ComplaintLetter.ComplaintLetterbean;
import com.example.grievanceregistrationform.ComplaintLetter.MasterAllSurveyMediaAdapter;
import com.example.grievanceregistrationform.timeline.BookingTimelineActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MasterAllSurveyAdapter extends RecyclerView.Adapter<MasterAllSurveyAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<ComplaintLetterbean> moviesList;
    SharedPreferences sharedpreferences;
    OnSummaryClick onSummaryClick;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView date, adboffice, projectname, status;
        MaterialButton editImage;
        Button printImage, viewImage;
        RecyclerView recycler_view_media;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            adboffice = (TextView) view.findViewById(R.id.adboffice);
            projectname = (TextView) view.findViewById(R.id.projectname);
            editImage = view.findViewById(R.id.editImage);
            status = (TextView) view.findViewById(R.id.status);
            viewImage = view.findViewById(R.id.viewImage);
            printImage = view.findViewById(R.id.printBtn);
            recycler_view_media = (RecyclerView) view.findViewById(R.id.recycler_view_media);


        }
    }


    public MasterAllSurveyAdapter(Context mainActivityUser, ArrayList<ComplaintLetterbean> moviesList, OnSummaryClick onSummaryClick) {
        this.moviesList = moviesList;
        this.mainActivityUser = mainActivityUser;
        this.onSummaryClick = onSummaryClick;

    }

    public void notifyData(ArrayList<ComplaintLetterbean> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.moviesList = myList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ComplaintLetterbean complaintLetterbean = moviesList.get(position);

        holder.date.setText(complaintLetterbean.getDate());
        holder.adboffice.setText(complaintLetterbean.getAdboffice());
        holder.projectname.setText(complaintLetterbean.getProjectname());
        holder.status.setText(complaintLetterbean.getStatus());

        ArrayList<Projectbean> projectbeans = new ArrayList<>();
        for (int k = 0; k < complaintLetterbean.projectbeans.size(); k++) {
            if (complaintLetterbean.projectbeans.get(k).attachment
                    != null && complaintLetterbean.projectbeans.get(k).attachment.length() > 0) {
                projectbeans.add(complaintLetterbean.projectbeans.get(k));
            }
        }
        MasterAllSurveyMediaAdapter surveyMediaAdapter = new
                MasterAllSurveyMediaAdapter(mainActivityUser, projectbeans);
        final LinearLayoutManager addManager = new LinearLayoutManager(mainActivityUser, LinearLayoutManager.HORIZONTAL, false);
        holder.recycler_view_media.setLayoutManager(addManager);
        holder.recycler_view_media.setAdapter(surveyMediaAdapter);

        if (complaintLetterbean.getStatus().equals("Feed back date")
                || complaintLetterbean.getStatus().equals("Issues resolved or escalated")
                || complaintLetterbean.getStatus().equals("Resolution Proposed")
                || complaintLetterbean.getStatus().equals("Feedback done")) {
            holder.editImage.setIcon(mainActivityUser.getResources().getDrawable(R.drawable.ic_feedback_black_24dp));
            holder.editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mainActivityUser, ResolutionFeedBack.class);
                    intent.putExtra("object", moviesList.get(position));
                    mainActivityUser.startActivity(intent);
                }
            });
        } else if (complaintLetterbean.getStatus().equals("Submitted")) {
            holder.editImage.setIcon(mainActivityUser.getResources().getDrawable(R.drawable.ic_mode_edit_black_24dp));
            holder.editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mainActivityUser, CompliantLetterCopy.class);
                    intent.putExtra("object", moviesList.get(position));
                    mainActivityUser.startActivity(intent);
                }
            });
        } else{
            holder.editImage.setIcon(mainActivityUser.getResources().getDrawable(R.drawable.ic_remove_red_eye_black_24dp));

        }


        holder.viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivityUser, BookingTimelineActivity.class);
                intent.putExtra("id", moviesList.get(position).getUniId());
                mainActivityUser.startActivity(intent);
            }
        });
        holder.printImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSummaryClick.onPrintClick(position);
                //               CompliantPdfConfig.printFunction(mainActivityUser, complaintLetterbean);
//                CompliantPdfConfig.printFunction(mainActivityUser,complaintLetterbean);
            }
        });


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public static String round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return String.valueOf((double) tmp / factor);
    }

}
