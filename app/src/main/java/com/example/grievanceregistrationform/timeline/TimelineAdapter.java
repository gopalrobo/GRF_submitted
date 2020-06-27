package com.example.grievanceregistrationform.timeline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.grievanceregistrationform.R;
import com.example.grievanceregistrationform.app.AppConfig;
import com.repsly.library.timelineview.LineType;
import com.repsly.library.timelineview.TimelineView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;


/**
 * Adapter for RecyclerView with TimelineView
 */

class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private int orientation;
    private ArrayList<Timeline> items;
    private Context context;
    TimelineListener timelineListener;
    private static DecimalFormat df1 = new DecimalFormat("0.00");

    TimelineAdapter(int orientation, ArrayList<Timeline> items, Context context, TimelineListener timelineListener) {
        this.orientation = orientation;
        this.items = items;
        this.context = context;
        this.timelineListener = timelineListener;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.booking_time_vertical;

    }

    public void notifiData(ArrayList<Timeline> listItems) {

        items = listItems;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvStatus.setText(AppConfig.convertKhmer(items.get(position).getStatus(), context.getApplicationContext()));
        //  holder.tv_status_new.setText(items.get(position).getInstatus());

        if (items.get(position).getStatus().equals("Second Level")
                || items.get(position).getStatus().equals("Third Level")) {
            holder.textviewLayout.setVisibility(View.VISIBLE);
            holder.contentLayout.setVisibility(View.GONE);
            holder.textview.setText(items.get(position).getStatus());
        } else {
            holder.textviewLayout.setVisibility(View.GONE);
            holder.textview.setText("");
            holder.contentLayout.setVisibility(View.VISIBLE);
        }


        if (!items.get(position).getInstatus().equals("pending")
                && !items.get(position).getInstatus().equals("progress")) {
            holder.tvName.setVisibility(View.VISIBLE);
            holder.tvAddress.setVisibility(View.VISIBLE);
            holder.tvName.setText(items.get(position).getApprovedBy());
            holder.tvAddress.setText(items.get(position).getTime());
            holder.tvName.setVisibility(View.VISIBLE);
            holder.tvAddress.setVisibility(View.VISIBLE);

            holder.timelineView.setDrawable(AppCompatResources
                    .getDrawable(holder.timelineView.getContext(),
                            R.drawable.ic_checked));
        } else {
            holder.tvName.setVisibility(View.GONE);
            holder.tvAddress.setVisibility(View.GONE);
            if (position > 8) {
                if (position == 17) {
                    holder.timelineView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    holder.timelineView.setNumber("T");
                } else if (position > 17) {
                    holder.timelineView.setNumber(String.valueOf(position-1));
                    holder.timelineView.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    holder.timelineView.setTextColor(context.getResources().getColor(R.color.white));
                    holder.timelineView.setNumber(String.valueOf(position));
                }
            } else if (position == 8) {
                holder.timelineView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.timelineView.setNumber("S");
            } else {
                holder.timelineView.setTextColor(context.getResources().getColor(R.color.white));
                holder.timelineView.setNumber(String.valueOf(position + 1));
            }
            if(items.get(position).getInstatus().equals("progress")){
                holder.timelineView.setActive(true);
                holder.timelineView.setStartLineColor(context.getResources().getColor(R.color.blueGray));
                holder.timelineView.setEndLineColor(context.getResources().getColor(R.color.gray));
                holder.timelineView.setMarkerColor(context.getResources().getColor(R.color.blueGray));
                holder.timelineView.setTextColor(context.getResources().getColor(R.color.black));
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colortint));
            }else{
                holder.timelineView.setActive(false);
                holder.timelineView.setStartLineColor(context.getResources().getColor(R.color.gray));
                holder.timelineView.setEndLineColor(context.getResources().getColor(R.color.gray));
                holder.timelineView.setMarkerColor(context.getResources().getColor(R.color.gray));
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.gray));
            }

        }
        if (items.get(position).getInstatus().equals("progress")) {
//            holder.timelineView.setActive(true);
//            holder.timelineView.setFillMarker(true);
//            holder.timelineView.setMarkerColor(context.getResources().getColor(R.color.blueGray));
//            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.blueGray));
            holder.callbtn.setVisibility(View.VISIBLE);
            holder.reportbtn.setVisibility(View.VISIBLE);
            holder.donebtn.setVisibility(View.VISIBLE);
            holder.cancelbtn.setVisibility(View.VISIBLE);
        } else {
            holder.callbtn.setVisibility(View.GONE);
            holder.cancelbtn.setVisibility(View.GONE);
            holder.reportbtn.setVisibility(View.GONE);
            holder.donebtn.setVisibility(View.GONE);
        }

        if (items.get(position).getInstatus().equals("progress") && (items.get(position).getStatus().equals("Waiting for Machine")
                || items.get(position).getStatus().equals("Booked"))) {
            holder.cancelbtn.setVisibility(View.VISIBLE);
        } else {
            holder.cancelbtn.setVisibility(View.GONE);
        }

        holder.callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timelineListener.onCallClick(items.get(position).getManagerC());
            }
        });

        if (items.get(position).getStatus().equals("Waiting for Operator/Driver") &&
                items.get(position).getDriverName() != null && items.get(position).getDriverName().toString().length() > 0
                && !items.get(position).getDriverName().toString().equals("null")) {
            holder.drLinear.setVisibility(View.VISIBLE);
            holder.dr_name.setText(items.get(position).getDriverName());
        } else {
            holder.drLinear.setVisibility(View.GONE);
        }

        if (items.get(position).getInstatus().equals("progress") && (!items.get(position).getStatus().equals("Waiting for Machine")
                && !items.get(position).getStatus().equals("Waiting for Operator/Driver"))) {
            holder.donebtn.setVisibility(View.VISIBLE);
        } else {
            holder.donebtn.setVisibility(View.GONE);
        }

        if ((items.get(position).getStatus().equals("Payment"))) {
            String startDate = items.get(position - 2).getTime();
            String endDate = items.get(position - 1).getTime();
            String totalTime = "10";
            if (Integer.parseInt(totalTime) < 30) {
                totalTime = "30";
            }
            double totalCostCash = Integer.parseInt(totalTime) *
                    Double.parseDouble(items.get(position).getPriceC());
            double totalCostCashLess = (((totalCostCash * 1.0) / 100)) * 80;
            String hint = totalTime + " mins" + "\ncash: ₹"
                    + df1.format(totalCostCash) + "\ncashless: ₹" + df1.format(
                    totalCostCashLess);
            if (Integer.parseInt(totalTime) < 30) {
                hint = "Min Charge applied\n" + hint;
            }
            holder.tvName.setText(hint);
            if (items.get(position).getInstatus().equals("progress")) {
                holder.cashbtn.setVisibility(View.VISIBLE);
            } else {
                holder.cashbtn.setVisibility(View.GONE);
            }
        }

        if ((items.get(position).getStatus().equals("Rating"))) {
            if (items.get(position).getDescription() != null &&
                    !items.get(position).getDescription().equals("null")) {
                holder.tvName.setText(items.get(position).getDescription() + " Rating");
            } else {
                holder.tvName.setText("Rating");
            }
            if (items.get(position).getInstatus().equals("progress")) {

            } else {

            }
        }

        holder.timelineView.setLineType(getLineType(position));

        holder.donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((items.get(position).getStatus().equals("Payment"))) {
                    String startDate = items.get(position - 2).getTime();
                    String endDate = items.get(position - 1).getTime();
                    String totalTime = "10";
                    if (Integer.parseInt(totalTime) < 30) {
                        totalTime = "30";
                    }
                    double totalCostCash = Integer.parseInt(totalTime) *
                            Double.parseDouble(items.get(position).getPriceC());
                    double totalCostCashLess = (((totalCostCash * 1.0) / 100)) * 80;
                    timelineListener.onPaymentClick(String.valueOf(totalCostCashLess));
                } else {
                    if (position == items.size() - 1) {
                        timelineListener.onDoneClick(items.get(position).getStatus(), items.get(position).getStatus(), String.valueOf(1));
                    } else {
                        timelineListener.onDoneClick(items.get(position).getStatus(), items.get(position + 1).getStatus(), String.valueOf(1));
                    }
                }

            }
        });
        holder.cashbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((items.get(position).getStatus().equals("Payment"))) {
                    String startDate = items.get(position - 2).getTime();
                    String endDate = items.get(position - 1).getTime();
                    String totalTime = "10";
                    if (Integer.parseInt(totalTime) < 30) {
                        totalTime = "30";
                    }
                    double totalCostCash = Integer.parseInt(totalTime) *
                            Double.parseDouble(items.get(position).getPriceC());
                    double totalCostCashLess = (((totalCostCash * 1.0) / 100)) * 80;
                    timelineListener.onCashClick(String.valueOf(totalCostCash));
                } else {
                    if (position == items.size() - 1) {
                    } else {
                    }
                }

            }
        });
        holder.cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timelineListener.onCancelClick();

            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private LineType getLineType(int position) {
        if (getItemCount() == 1) {
            return LineType.ONLYONE;

        } else {
            if (position == 0) {
                return LineType.BEGIN;

            } else if (position == getItemCount() - 1) {
                return LineType.END;

            } else {
                return LineType.NORMAL;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TimelineView timelineView;
        TextView tvName, dr_name;
        TextView tvAddress, tv_status_new;
        TextView tvStatus;
        LinearLayout drLinear;
        FloatingTextButton callbtn, reportbtn, donebtn, cancelbtn, cashbtn;
        LinearLayout textviewLayout, contentLayout;
        TextView textview;


        ViewHolder(View view) {
            super(view);
            timelineView = (TimelineView) view.findViewById(R.id.timeline);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            dr_name = (TextView) view.findViewById(R.id.dr_name);
            drLinear = (LinearLayout) view.findViewById(R.id.drLinear);
            tvAddress = (TextView) view.findViewById(R.id.tv_address);
            tvStatus = (TextView) view.findViewById(R.id.tv_status);

            callbtn = (FloatingTextButton) view.findViewById(R.id.callbtn);
            cashbtn = (FloatingTextButton) view.findViewById(R.id.cashbtn);
            donebtn = (FloatingTextButton) view.findViewById(R.id.donebtn);
            cancelbtn = (FloatingTextButton) view.findViewById(R.id.cancelbtn);
            reportbtn = (FloatingTextButton) view.findViewById(R.id.reportbtn);
            tv_status_new = (TextView) view.findViewById(R.id.tv_status_new);
            textviewLayout = (LinearLayout) view.findViewById(R.id.textviewLayout);
            contentLayout = (LinearLayout) view.findViewById(R.id.contentLayout);
            textview = (TextView) view.findViewById(R.id.textview);

        }
    }

}
