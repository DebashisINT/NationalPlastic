package com.breezenationalplasticfsm.features.reimbursement.presentation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.breezenationalplasticfsm.R;
import com.breezenationalplasticfsm.features.reimbursement.model.reimbursementlist.ReimbursementListDetailsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReimbursementDateAdapter extends RecyclerView.Adapter<ReimbursementDateAdapter.ViewHolder> {

    private Context mContext;
    private List<Date> dateList;
    private onPetSelectedListener dateItemClickListener;
    private int mSelectedPosition = 0;
    private boolean isFromApplyClass;
    private ReimbursementListDetailsModel reimbursementDetails;

    public ReimbursementDateAdapter(Context context, boolean isFromApplyClass, onPetSelectedListener _dateItemClickListener, ReimbursementListDetailsModel reimbursementDetails) {
        mContext = context;
        dateList = new ArrayList<>();
        dateItemClickListener = _dateItemClickListener;
        this.isFromApplyClass = isFromApplyClass;
        this.reimbursementDetails = reimbursementDetails;
    }

    public void refreshAdapter(List<Date> _dateList) {
        dateList.clear();
        dateList.addAll(_dateList);
        notifyDataSetChanged();
    }

    public void refreshAdapter(List<Date> _dateList, int selectedPosition) {
        mSelectedPosition = selectedPosition;
        dateList.clear();
        dateList.addAll(_dateList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_date_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH);
            try {
                Date date_ = inputFormat.parse(String.valueOf(dateList.get(position)));
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                System.out.println(outputFormat.format(date_));

                if ( String.valueOf(outputFormat.format(date_)).equals(reimbursementDetails.getApplied_date())) {
                    holder.clDateParent.setBackgroundResource(R.drawable.layerlist_date_selected);
                    holder.tvDateText.setTextColor(mContext.getResources().getColor(R.color.date_text_selected_color));
                    holder.tvMonthText.setTextColor(mContext.getResources().getColor(R.color.date_month_selected_color));
                }else {
                    holder.clDateParent.setBackgroundResource(R.drawable.layerlist_date_normal);
                    holder.tvDateText.setTextColor(mContext.getResources().getColor(R.color.date_text_unselected_color));
                    holder.tvMonthText.setTextColor(mContext.getResources().getColor(R.color.date_month_unselected_color));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


         /*   if (position == mSelectedPosition) {
            holder.clDateParent.setBackgroundResource(R.drawable.layerlist_date_selected);
            holder.tvDateText.setTextColor(mContext.getResources().getColor(R.color.date_text_selected_color));
            holder.tvMonthText.setTextColor(mContext.getResources().getColor(R.color.date_month_selected_color));
        } else {
            holder.clDateParent.setBackgroundResource(R.drawable.layerlist_date_normal);
            holder.tvDateText.setTextColor(mContext.getResources().getColor(R.color.date_text_unselected_color));
            holder.tvMonthText.setTextColor(mContext.getResources().getColor(R.color.date_month_unselected_color));
        }*/


        holder.clDateParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFromApplyClass) {
                    mSelectedPosition = position;
                    if (dateItemClickListener != null) {
                        dateItemClickListener.onDateItemClick(position);
                    }
                    notifyDataSetChanged();
                }
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        String formattedDate = dateFormat.format(dateList.get(position));
        String formattedMonth = monthFormat.format(dateList.get(position));
        holder.tvDateText.setText("" + formattedDate);
        holder.tvMonthText.setText("" + formattedMonth);


    }

    @Override
    public int getItemCount() {
        return dateList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View clDateParent;
        TextView tvDateText, tvMonthText;

        public ViewHolder(View itemView) {
            super(itemView);
            clDateParent = itemView.findViewById(R.id.clDateParent);
            tvDateText = itemView.findViewById(R.id.tvDateText);
            tvMonthText = itemView.findViewById(R.id.tvMonthText);

        }
    }

    public interface onPetSelectedListener {
        void onDateItemClick(int pos);
    }
}
