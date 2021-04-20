package com.example.covid19tracker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19tracker.R;
import com.example.covid19tracker.model.USDistrictModel;
import com.example.covid19tracker.model.USStateModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class USDistrictAdapter extends RecyclerView.Adapter<USDistrictAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<USDistrictModel> usDistrictModelArrayList;
    private String searchText = "";
    private SpannableStringBuilder sb;

    public USDistrictAdapter(Context mContext, ArrayList<USDistrictModel> usDistrictModelArrayList) {
        this.mContext = mContext;
        this.usDistrictModelArrayList = usDistrictModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_us_district_data_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            USDistrictModel usDistrictModel = usDistrictModelArrayList.get(position);
            holder.district_case.setText(usDistrictModel.getConfirmed());
            holder.district_death.setText(usDistrictModel.getDeath());

        // Matching letters according to the search result.
        if(searchText.length()>0){
            sb = new SpannableStringBuilder(usDistrictModel.getProvince());
            Pattern word = Pattern.compile(searchText.toLowerCase());
            Matcher match = word.matcher(usDistrictModel.getProvince());
            while(match.find()){
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(52, 195, 235)); //specify color here
                sb.setSpan(fcs, match.start(), match.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            holder.district_name.setText(sb);

        }else{
            holder.district_name.setText(usDistrictModel.getProvince());
        }
    }

    //Filtering the arraylist according to searched text
    public void FilterList(ArrayList<USDistrictModel> models,String str){
        searchText = str;
        usDistrictModelArrayList = models;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (usDistrictModelArrayList == null){
            return 0;
        }
        else {
            return usDistrictModelArrayList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView district_name,district_case,district_death;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            district_case = itemView.findViewById(R.id.activity_us_district_data_row_confirmed_textview);
            district_name = itemView.findViewById(R.id.activity_us_district_data_row_name_textview);
            district_death = itemView.findViewById(R.id.activity_us_district_data_row_deaths_textview);
        }
    }
}
