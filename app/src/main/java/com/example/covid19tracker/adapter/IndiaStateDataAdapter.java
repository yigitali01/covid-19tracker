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

import com.example.covid19tracker.Constants;
import com.example.covid19tracker.IndiaEachStateData;
import com.example.covid19tracker.IndiaStateData;
import com.example.covid19tracker.R;
import com.example.covid19tracker.model.IndiaStateModel;

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

public class IndiaStateDataAdapter extends RecyclerView.Adapter<IndiaStateDataAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<IndiaStateModel> stateWiseModelArrayList;
    private String searchText = "";
    private SpannableStringBuilder sb;


    public IndiaStateDataAdapter(Context mContext, ArrayList<IndiaStateModel> stateWiseModelArrayList) {
        this.mContext = mContext;
        this.stateWiseModelArrayList = stateWiseModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_india_state_data_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IndiaStateModel indiaStateModel = stateWiseModelArrayList.get(position);
        String stateName = indiaStateModel.getState();
        String stateCases = indiaStateModel.getConfirmed();
        holder.india_state_confirmed.setText(stateCases);


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
            holder.india_state_name.setText(sb);

        }else{
            holder.india_state_name.setText(stateName);
        }

        //After users performs click send data to other activity
        holder.india_state_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndiaStateModel selectedPosition = stateWiseModelArrayList.get(position);
                Intent toEachState = new Intent(mContext, IndiaEachStateData.class);
                toEachState.putExtra(STATE_NAME, selectedPosition.getState());
                toEachState.putExtra(STATE_CONFIRMED, selectedPosition.getConfirmed());
                toEachState.putExtra(STATE_CONFIRMED_NEW, selectedPosition.getConfirmed_new());
                toEachState.putExtra(STATE_ACTIVE, selectedPosition.getActive());
                toEachState.putExtra(STATE_DEATH, selectedPosition.getDeath());
                toEachState.putExtra(STATE_DEATH_NEW, selectedPosition.getDeath_new());
                toEachState.putExtra(STATE_RECOVERED, selectedPosition.getRecovered());
                toEachState.putExtra(STATE_RECOVERED_NEW, selectedPosition.getRecovered_new());
                toEachState.putExtra(STATE_LAST_UPDATE, selectedPosition.getLastupdate());

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

    public void FilterList(ArrayList<IndiaStateModel> models,String str){
        searchText = str;
        stateWiseModelArrayList = models;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView india_state_name,india_state_confirmed;
        LinearLayout india_state_lin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            india_state_name = (TextView) itemView.findViewById(R.id.activity_india_state_data_row_name_textview);
            india_state_confirmed = (TextView) itemView.findViewById(R.id.activity_india_state_data_row_confirmed_textview);
            india_state_lin = itemView.findViewById(R.id.activity_india_state_data_row_lin);
        }
    }
}
