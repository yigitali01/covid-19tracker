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
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;

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

public class USEachStateData extends AppCompatActivity {

    TextView tv_confirmed, tv_confirmed_new, tv_active, tv_active_new,
            tv_recovered, tv_recovered_new, tv_death, tv_death_new, tv_tests,
            tv_critical,tv_cases_per_million,tv_deaths_per_million,tv_one_case_per_people,
            tv_tests_per_million,
            tv_one_death_per_people,tv_one_test_per_people,tv_population;

    SwipeRefreshLayout swipeRefreshLayout;

    String str_confirmed, str_confirmed_new, str_active, str_recovered, str_recovered_new,str_tests_per_million,
            str_death, str_death_new,str_state_name, str_tests,str_critical,str_deaths_per_million,str_cases_per_million,
            str_one_case_per_people,str_one_death_per_people,str_one_test_per_people,str_population;

    PieChart pieChart;

    MainActivity activity = new MainActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_us_each_state_data);

        GetIntent();

        getSupportActionBar().setTitle(str_state_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Init();

        FetchData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void FetchData() {
        activity.ShowDialog(USEachStateData.this);
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
                tv_active.setText(NumberFormat.getInstance().format(Integer.parseInt(str_active)));
                tv_population.setText(NumberFormat.getInstance().format(Integer.parseInt(str_population)));
                tv_tests.setText(NumberFormat.getInstance().format(Integer.parseInt(str_tests)));
                tv_tests_per_million.setText(NumberFormat.getInstance().format(Integer.parseInt(str_tests_per_million)));
                tv_deaths_per_million.setText(NumberFormat.getInstance().format(Integer.parseInt(str_deaths_per_million)));
                tv_cases_per_million.setText(NumberFormat.getInstance().format(Integer.parseInt(str_cases_per_million)));


                pieChart.addPieSlice(new PieModel("Active",Integer.valueOf(str_active), Color.parseColor("#007afe")));
                pieChart.addPieSlice(new PieModel("Deaths",Integer.valueOf(str_death),Color.parseColor("#F6404F")));
                pieChart.addPieSlice(new PieModel("Recovered",Integer.valueOf(str_recovered),Color.parseColor("#08a045")));

                pieChart.startAnimation();

                activity.DismissDialog();
            }
        },2500);

    }

    public void Init(){
        tv_confirmed = findViewById(R.id.activity_us_each_state_confirmed_textview);
        tv_confirmed_new = findViewById(R.id.activity_us_each_state_confirmed_new_textview);
        tv_active = findViewById(R.id.activity_us_each_state_active_textview);
        tv_recovered = findViewById(R.id.activity_us_each_state_recovered_textview);
        tv_death = findViewById(R.id.activity_us_each_state_death_textview);
        tv_death_new = findViewById(R.id.activity_us_each_state_death_new_textview);
        tv_tests = findViewById(R.id.activity_us_each_state_total_tests_textview);
        tv_cases_per_million = findViewById(R.id.activity_us_each_state_cases_per_million_textview);
        tv_deaths_per_million = findViewById(R.id.activity_us_each_state_deaths_per_one_million_textview);
        tv_tests_per_million = findViewById(R.id.activity_us_each_state_tests_per_one_million_textview);
        tv_population = findViewById(R.id.activity_us_each_state_population_textview);

        swipeRefreshLayout = findViewById(R.id.activity_us_each_state_swipe_refresh_layout);
        pieChart = findViewById(R.id.activity_us_each_state_pie_chart);

    }

     public void GetIntent() {
         Intent intent = getIntent();

         str_active = intent.getStringExtra(US_STATE_ACTIVE);
         str_cases_per_million = intent.getStringExtra(US_STATE_CASES_PER_ONE_MILLION);
         str_death = intent.getStringExtra(US_STATE_DECEASED);
         str_death_new = intent.getStringExtra(US_STATE_NEW_DECEASED);
         str_confirmed = intent.getStringExtra(US_STATE_CONFIRMED);
         str_confirmed_new = intent.getStringExtra(US_STATE_NEW_CONFIRMED);
         str_recovered = intent.getStringExtra(US_STATE_RECOVERED);
         str_population = intent.getStringExtra(US_STATE_POPULATION);
         str_tests = intent.getStringExtra(US_STATE_TESTS);
         str_state_name = intent.getStringExtra(US_STATE_NAME);
         str_tests_per_million = intent.getStringExtra(US_STATE_TESTS_PER_ONE_MILLION);
         str_deaths_per_million = intent.getStringExtra(US_STATE_DEATHS_PER_ONE_MILLION);

    }

    public void toUSDistrictData(View view) {
        Intent intent = new Intent(USEachStateData.this,USDistrictData.class);
        intent.putExtra("StateName",str_state_name);
        System.out.println(str_state_name);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
