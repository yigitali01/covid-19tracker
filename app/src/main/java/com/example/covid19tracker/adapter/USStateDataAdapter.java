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

import com.example.covid19tracker.IndiaEachStateData;
import com.example.covid19tracker.R;
import com.example.covid19tracker.USEachStateData;
import com.example.covid19tracker.model.IndiaStateModel;
import com.example.covid19tracker.model.USStateModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.covid19tracker.Constants.STATE_ACTIVE;
import static com.example.covid19tracker.Constants.STATE_CONFIRMED;
import static com.example.covid19tracker.Constants.STATE_CONFIRMED_NEW;
import static com.example.covid19tracker.Constants.STATE_DEATH;
import static com.example.covid19tracker.Constants.STATE_DEATH_NEW;
import static com.example.covid19tracker.Constants.STATE_LAST_UPDATE;
import static com.example.covid19tracker.Constants.STATE_NAME;
import static com.example.covid19tracker.Constants.STATE_RECOVERED;
import static com.example.covid19tracker.Constants.STATE_RECOVERED_NEW;
import static com.example.covid19tracker.Constants.US_STATE_ACTIVE;
import static com.example.covid19tracker.Constants.US_STATE_CASES_PER_ONE_MILLION;
import static com.example.covid19tracker.Constants.US_STATE_CONFIRMED;
import static com.example.covid19tracker.Constants.US_STATE_DEATHS_PER_ONE_MILLION;
import static com.example.covid19tracker.Constants.US_STATE_DECEASED;
import static com.example.covid19tracker.Constants.US_STATE_NAME;
import static com.example.covid19tracker.Constants.US_STATE_NEW_CONFIRMED;
import static com.example.covid19tracker.Constants.US_STATE_NEW_DECEASED;
import static com.example.covid19tracker.Constants.US_STATE_POPULATION;
import static com.example.covid19tracker.Constants.US_STATE_RECOVERED;
import static com.example.covid19tracker.Constants.US_STATE_TESTS;
import static com.example.covid19tracker.Constants.US_STATE_TESTS_PER_ONE_MILLION;

public class USStateDataAdapter extends RecyclerView.Adapter<USStateDataAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<USStateModel> stateWiseModelArrayList;
    private String searchText = "";
    private SpannableStringBuilder sb;

    public USStateDataAdapter(Context mContext, ArrayList<USStateModel> stateWiseModelArrayList) {
        this.mContext = mContext;
        this.stateWiseModelArrayList = stateWiseModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_us_state_data_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        USStateModel usStateModel = stateWiseModelArrayList.get(position);
        String stateName = usStateModel.getState();
        String stateCases = usStateModel.getConfirmed();
        holder.us_state_confirmed.setText(stateCases);

       // Matching letters according to the search result.
        if(searchText.length()>0){
            //color your text here
            sb = new SpannableStringBuilder(stateName);
            Pattern word = Pattern.compile(searchText.toLowerCase());
            Matcher match = word.matcher(stateName.toLowerCase());
            while(match.find()){
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(52, 195, 235)); //specify color here
                sb.setSpan(fcs, match.start(), match.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            holder.us_state_name.setText(sb);

        }else{
            holder.us_state_name.setText(stateName);
        }

        //After users performs click send data to other activity
        holder.us_state_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                USStateModel selectedPosition = stateWiseModelArrayList.get(position);
                Intent toEachState = new Intent(mContext, USEachStateData.class);
                toEachState.putExtra(US_STATE_NAME, selectedPosition.getState());
                toEachState.putExtra(US_STATE_CONFIRMED, selectedPosition.getConfirmed());
                toEachState.putExtra(US_STATE_NEW_CONFIRMED, selectedPosition.getNewConfirmed());
                toEachState.putExtra(US_STATE_ACTIVE, selectedPosition.getActive());
                toEachState.putExtra(US_STATE_DECEASED, selectedPosition.getDeaths());
                toEachState.putExtra(US_STATE_NEW_DECEASED, selectedPosition.getNewDeaths());
                toEachState.putExtra(US_STATE_RECOVERED, selectedPosition.getRecovered());
                toEachState.putExtra(US_STATE_TESTS, selectedPosition.getTests());
                toEachState.putExtra(US_STATE_POPULATION, selectedPosition.getPopulation());
                toEachState.putExtra(US_STATE_CASES_PER_ONE_MILLION, selectedPosition.getCasesPerOneMillion());
                toEachState.putExtra(US_STATE_DEATHS_PER_ONE_MILLION,selectedPosition.getDeathsPerOneMillion());
                toEachState.putExtra(US_STATE_TESTS_PER_ONE_MILLION,selectedPosition.getTestsPerOneMillion());


                mContext.startActivity(toEachState);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (stateWiseModelArrayList == null){
            return 0;
        }
        else {
            return stateWiseModelArrayList.size();
        }
    }

    //Filtering the arraylist according to searched text
    public void FilterList(ArrayList<USStateModel> models,String str){
        searchText = str;
        stateWiseModelArrayList = models;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView us_state_name,us_state_confirmed;
        LinearLayout us_state_lin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            us_state_name = (TextView) itemView.findViewById(R.id.activity_us_state_data_row_name_textview);
            us_state_confirmed = (TextView) itemView.findViewById(R.id.activity_us_state_data_row_confirmed_textview);
            us_state_lin = itemView.findViewById(R.id.activity_us_state_data_row_lin);
        }
    }
}
