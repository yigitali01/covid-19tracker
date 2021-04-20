package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;

import static com.example.covid19tracker.Constants.STATE_ACTIVE;
import static com.example.covid19tracker.Constants.STATE_CONFIRMED;
import static com.example.covid19tracker.Constants.STATE_CONFIRMED_NEW;
import static com.example.covid19tracker.Constants.STATE_DEATH;
import static com.example.covid19tracker.Constants.STATE_DEATH_NEW;
import static com.example.covid19tracker.Constants.STATE_LAST_UPDATE;
import static com.example.covid19tracker.Constants.STATE_NAME;
import static com.example.covid19tracker.Constants.STATE_RECOVERED;
import static com.example.covid19tracker.Constants.STATE_RECOVERED_NEW;

public class IndiaEachStateData extends AppCompatActivity {
    private TextView tv_confirmed, tv_confirmed_new, tv_active, tv_active_new, tv_death, tv_death_new,
            tv_recovered, tv_recovered_new, tv_lastupdatedate, tv_dist;

    private String str_stateName, str_confirmed, str_confirmed_new, str_active, str_active_new, str_death, str_death_new,
            str_recovered, str_recovered_new, str_lastupdatedate;

    private PieChart pieChart;

    private LinearLayout lin_district;

    private SwipeRefreshLayout swipeRefreshLayout;

    private IndiaData activity = new IndiaData();
    private MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_india_each_state_data);

        //Get various data from IndiaStateActivity
        GetIntent();

        //Initializing views
        Init();

        getSupportActionBar().setTitle(str_stateName);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get data
        FetchEachState();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchEachState();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }



    public void toDistrictStateData(View view){
        Intent intent = new Intent(this,IndiaDistrictData.class);
        intent.putExtra(STATE_NAME,str_stateName);
        startActivity(intent);
    }

    public void FetchEachState(){
        mainActivity.ShowDialog(IndiaEachStateData.this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                pieChart.clearChart();

                tv_confirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(str_confirmed)));
                tv_confirmed_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_confirmed_new)));
                tv_active.setText(NumberFormat.getInstance().format(Integer.parseInt(str_active)));
//                tv_active_new.setText("+"+str_active_new);
                tv_death.setText(NumberFormat.getInstance().format(Integer.parseInt(str_death)));
                tv_death_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_death_new)));
                tv_recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(str_recovered)));
                tv_recovered_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_recovered_new)));
                String dateFormat = activity.FormatDate(str_lastupdatedate,0);
                tv_lastupdatedate.setText(dateFormat);

                pieChart.addPieSlice(new PieModel("Active",Integer.valueOf(str_active), Color.parseColor("#007afe")));
                pieChart.addPieSlice(new PieModel("Deaths",Integer.valueOf(str_death),Color.parseColor("#F6404F")));
                pieChart.addPieSlice(new PieModel("Recovered",Integer.valueOf(str_recovered),Color.parseColor("#08a045")));

                pieChart.startAnimation();

                mainActivity.DismissDialog();
            }
        },2500);
    }

    public void Init(){
        tv_confirmed = findViewById(R.id.activity_india_each_state_data_confirmed_textview);
        tv_confirmed_new = findViewById(R.id.activity_india_each_state_data_confirmed_new_textview);
        tv_active = findViewById(R.id.activity_india_each_state_data_active_textview);
        tv_active_new = findViewById(R.id.activity_india_each_state_data_active_new_textview);
        tv_recovered = findViewById(R.id.activity_india_each_state_data_recovered_textview);
        tv_recovered_new = findViewById(R.id.activity_india_each_state_data_recovered_new_textview);
        tv_death = findViewById(R.id.activity_india_each_state_data_death_textview);
        tv_death_new = findViewById(R.id.activity_india_each_state_data_death_new_textview);
        tv_lastupdatedate = findViewById(R.id.activity_india_each_state_data_last_update_date_textview);
        pieChart = findViewById(R.id.activity_india_each_state_data_pie_chart);
        swipeRefreshLayout = findViewById(R.id.activity_india_each_state_data_swipe_refresh_layout);
    }
    public void GetIntent(){
        Intent intent = getIntent();
        str_stateName = intent.getStringExtra(STATE_NAME);
        str_confirmed = intent.getStringExtra(STATE_CONFIRMED);
        str_confirmed_new = intent.getStringExtra(STATE_CONFIRMED_NEW);
        str_active = intent.getStringExtra(STATE_ACTIVE);
        str_death = intent.getStringExtra(STATE_DEATH);
        str_death_new = intent.getStringExtra(STATE_DEATH_NEW);
        str_recovered = intent.getStringExtra(STATE_RECOVERED);
        str_recovered_new = intent.getStringExtra(STATE_RECOVERED_NEW);
        str_lastupdatedate = intent.getStringExtra(STATE_LAST_UPDATE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}