package com.example.covid19tracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19tracker.IndiaDistrictData;
import com.example.covid19tracker.IndiaEachDistrictData;
import com.example.covid19tracker.R;
import com.example.covid19tracker.model.IndiaDistrictModel;
import com.example.covid19tracker.Constants;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

import static com.example.covid19tracker.Constants.DISTRICT_ACTIVE;
import static com.example.covid19tracker.Constants.DISTRICT_CONFIRMED;
import static com.example.covid19tracker.Constants.DISTRICT_CONFIRMED_NEW;
import static com.example.covid19tracker.Constants.DISTRICT_DEATH;
import static com.example.covid19tracker.Constants.DISTRICT_DEATH_NEW;
import static com.example.covid19tracker.Constants.DISTRICT_NAME;
import static com.example.covid19tracker.Constants.DISTRICT_RECOVERED;
import static com.example.covid19tracker.Constants.DISTRICT_RECOVERED_NEW;

public class IndiaDistrictDataAdapter extends RecyclerView.Adapter<IndiaDistrictDataAdapter.ViewHolder> {

    private ArrayList<IndiaDistrictModel> districtWiseModelList;
    private Context mContext;
    private String searchText="";
    private SpannableStringBuilder sb;


    public IndiaDistrictDataAdapter(ArrayList<IndiaDistrictModel> districtWiseModelList, Context mContext) {
        this.districtWiseModelList = districtWiseModelList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_india_district_data_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IndiaDistrictModel districtModel = districtWiseModelList.get(position);
        String districtName = districtModel.getDistrict();
        String totalCases = districtModel.getConfirmed();

        holder.tv_india_district_name.setText(districtName);
        holder.tv_india_district_confirmed.setText(totalCases);

        // Matching letters according to the search result.
        if(searchText.length()>0){
            int index = districtName.indexOf(searchText);
            sb = new SpannableStringBuilder(districtName);
            Pattern word = Pattern.compile(searchText.toLowerCase());
            Matcher match = word.matcher(districtName.toLowerCase());
            while(match.find()){
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(52, 195, 235)); //specify color here
                sb.setSpan(fcs, match.start(), match.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                //index = stateName.indexOf(searchText,index+1);

            }
            holder.tv_india_district_name.setText(sb);

        }else{
            holder.tv_india_district_name.setText(districtName);
        }

        //After users performs click send data to other activity
        holder.india_district_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndiaDistrictModel districtModel = districtWiseModelList.get(position);
                Intent intent = new Intent(mContext, IndiaEachDistrictData.class);
                intent.putExtra(DISTRICT_NAME,districtModel.getDistrict());
                intent.putExtra(DISTRICT_CONFIRMED,districtModel.getConfirmed());
                intent.putExtra(DISTRICT_CONFIRMED_NEW,districtModel.getConfirmed_new());
                intent.putExtra(DISTRICT_RECOVERED,districtModel.getRecovered());
                intent.putExtra(DISTRICT_RECOVERED_NEW,districtModel.getRecovered_new());
                intent.putExtra(DISTRICT_DEATH,districtModel.getDeath());
                intent.putExtra(DISTRICT_DEATH_NEW,districtModel.getDeath_new());
                intent.putExtra(DISTRICT_ACTIVE,districtModel.getActive());

                mContext.startActivity(intent);

            }
        });
    }

    //Filtering the arraylist according to searched text
    public void filterList(ArrayList<IndiaDistrictModel> filteredList, String search) {
        districtWiseModelList = filteredList;
        this.searchText = search;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (districtWiseModelList == null){
            return 0;
        }
        else {
            return districtWiseModelList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_india_district_name,tv_india_district_confirmed;
        LinearLayout india_district_lin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_india_district_name = itemView.findViewById(R.id.activity_india_district_data_row_name_textview);
            tv_india_district_confirmed = itemView.findViewById(R.id.activity_india_district_data_row_confirmed_textview);
            india_district_lin = itemView.findViewById(R.id.activity_india_district_data_row_lin);
        }
    }
}
