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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.covid19tracker.EachCountryData;
import com.example.covid19tracker.R;
import com.example.covid19tracker.model.CountryModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.covid19tracker.Constants.COUNTRY_ACTIVE;
import static com.example.covid19tracker.Constants.COUNTRY_CONFIRMED;
import static com.example.covid19tracker.Constants.COUNTRY_CRITICAL;
import static com.example.covid19tracker.Constants.COUNTRY_DECEASED;
import static com.example.covid19tracker.Constants.COUNTRY_NAME;
import static com.example.covid19tracker.Constants.COUNTRY_NEW_CONFIRMED;
import static com.example.covid19tracker.Constants.COUNTRY_NEW_DECEASED;
import static com.example.covid19tracker.Constants.COUNTRY_ONE_CASE_PER_PEOPLE;
import static com.example.covid19tracker.Constants.COUNTRY_ONE_DEATH_PER_PEOPLE;
import static com.example.covid19tracker.Constants.COUNTRY_ONE_TEST_PER_PEOPLE;
import static com.example.covid19tracker.Constants.COUNTRY_POPULATION;
import static com.example.covid19tracker.Constants.COUNTRY_RECOVERED;
import static com.example.covid19tracker.Constants.COUNTRY_TESTS;

public class CountryDataAdapter extends RecyclerView.Adapter<CountryDataAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CountryModel> countryModelArrayList;
    private String searchText = "";
    private SpannableStringBuilder sb;

    public CountryDataAdapter(Context mContext, ArrayList<CountryModel> countryModelArrayList) {
        this.mContext = mContext;
        this.countryModelArrayList = countryModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_country_data_row,parent,false);
        return new ViewHolder(view);
    }

    //Filtering the arraylist according to searched text
    public void filterList(ArrayList<CountryModel> filteredList, String text) {
        countryModelArrayList = filteredList;
        this.searchText = text;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CountryModel countryModel = countryModelArrayList.get(position);
        String countryName = countryModel.getCountry();
        String countryCases = countryModel.getConfirmed();
        String countryFlag = countryModel.getFlag();
        String countryRank = String.valueOf(position+1);

        holder.tv_countryTotalCases.setText(countryCases);
        holder.tv_rankTextView.setText(countryRank+".");


        // Matching letters according to the search result.
        if(searchText.length()>0){
            //color your text here
            int index = countryName.indexOf(searchText);
            sb = new SpannableStringBuilder(countryName);
            Pattern word = Pattern.compile(searchText.toLowerCase());
            Matcher match = word.matcher(countryName.toLowerCase());
            while(match.find()){
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(52, 195, 235)); //specify color here
                sb.setSpan(fcs, match.start(), match.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                //index = stateName.indexOf(searchText,index+1);

            }
            holder.tv_countryName.setText(sb);

        }else{
            holder.tv_countryName.setText(countryName);
        }

        //Get flag photo from url
        Glide.with(mContext).load(countryFlag).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.iv_flagImage);

        //After users performs click send data to other activity
        holder.lin_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CountryModel countryModel = countryModelArrayList.get(position);
                Intent intent = new Intent(mContext, EachCountryData.class);

                intent.putExtra(COUNTRY_NAME,countryModel.getCountry());
                intent.putExtra(COUNTRY_ACTIVE,countryModel.getActive());
                intent.putExtra(COUNTRY_CONFIRMED,countryModel.getConfirmed());
                intent.putExtra(COUNTRY_NEW_CONFIRMED,countryModel.getNewConfirmed());
                intent.putExtra(COUNTRY_DECEASED,countryModel.getDeaths());
                intent.putExtra(COUNTRY_NEW_DECEASED,countryModel.getNewDeaths());
                intent.putExtra(COUNTRY_RECOVERED,countryModel.getRecovered());
                intent.putExtra(COUNTRY_CRITICAL,countryModel.getCritical());
                intent.putExtra(COUNTRY_TESTS,countryModel.getTests());
                intent.putExtra(COUNTRY_ONE_CASE_PER_PEOPLE,countryModel.getOneCasePerPeople());
                intent.putExtra(COUNTRY_ONE_DEATH_PER_PEOPLE,countryModel.getOneDeathPerPeople());
                intent.putExtra(COUNTRY_ONE_TEST_PER_PEOPLE,countryModel.getOneTestPerPeople());
                intent.putExtra(COUNTRY_POPULATION,countryModel.getPopulation());

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (countryModelArrayList == null){
            return 0;

        }
        else {
            return countryModelArrayList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_countryName, tv_countryTotalCases, tv_rankTextView;
        ImageView iv_flagImage;
        LinearLayout lin_country;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_countryName = itemView.findViewById(R.id.layout_country_wise_country_name_textview);
            tv_countryTotalCases = itemView.findViewById(R.id.layout_country_wise_confirmed_textview);
            iv_flagImage = itemView.findViewById(R.id.layout_country_wise_flag_imageview);
            tv_rankTextView = itemView.findViewById(R.id.layout_country_wise_country_rank);
            lin_country = itemView.findViewById(R.id.layout_country_wise_lin);

        }
    }
}
