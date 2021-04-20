package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

public class UnitedStatesData extends AppCompatActivity {
    TextView tv_confirmed, tv_confirmed_new, tv_active, tv_active_new,
            tv_recovered, tv_recovered_new, tv_death, tv_death_new, tv_tests,
            tv_critical,tv_cases_per_million,tv_deaths_per_million,tv_one_case_per_people,
            tv_one_death_per_people,tv_one_test_per_people,tv_population;

    SwipeRefreshLayout swipeRefreshLayout;

    String str_confirmed,str_tests_per_million, str_confirmed_new, str_active, str_recovered, str_recovered_new,str_state,
            str_death, str_death_new, str_tests,str_critical,str_deaths_per_million,str_cases_per_million,
            str_one_case_per_people,str_one_death_per_people,str_one_test_per_people,str_population;

    PieChart pieChart;

    MainActivity activity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_united_states_data);

        getSupportActionBar().setTitle(R.string.united_states_string);
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

    public void Init(){
        tv_confirmed = findViewById(R.id.activity_united_states_confirmed_textview);
        tv_confirmed_new = findViewById(R.id.activity_united_states_confirmed_new_textview);
        tv_active = findViewById(R.id.activity_united_states_active_textview);
        tv_active_new = findViewById(R.id.activity_united_states_active_new_textview);
        tv_recovered = findViewById(R.id.activity_united_states_recovered_textview);
        tv_recovered_new = findViewById(R.id.activity_united_states_recovered_new_textview);
        tv_death = findViewById(R.id.activity_united_states_death_textview);
        tv_death_new = findViewById(R.id.activity_united_states_death_new_textview);
        tv_tests = findViewById(R.id.activity_united_states_total_tests_textview);
        tv_cases_per_million = findViewById(R.id.activity_united_states_cases_per_million_textview);
        tv_critical = findViewById(R.id.activity_united_states_critical_textview);
        tv_deaths_per_million = findViewById(R.id.activity_united_states_deaths_per_one_million_textview);
        tv_one_case_per_people = findViewById(R.id.activity_united_states_one_case_per_people_textview);
        tv_one_death_per_people = findViewById(R.id.activity_united_states_one_death_per_people_textview);
        tv_one_test_per_people = findViewById(R.id.activity_united_states_one_test_per_people_textview);
        tv_population = findViewById(R.id.activity_united_states_population_textview);
        swipeRefreshLayout = findViewById(R.id.activity_united_states_swipe_refresh_layout);
        pieChart = findViewById(R.id.activity_united_states_pie_chart);

    }

    public void FetchData(){
        activity.ShowDialog(this);
        String apiUrl = "https://corona.lmao.ninja/v3/covid-19/countries/usa";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        pieChart.clearChart();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            str_confirmed = response.getString("cases");
                            str_confirmed_new = response.getString("todayCases");
                            str_active = response.getString("active");
                            str_recovered = response.getString("recovered");
                            str_recovered_new = response.getString("todayRecovered");
                            str_death = response.getString("deaths");
                            str_death_new = response.getString("todayDeaths");
                            str_tests = response.getString("tests");
                            str_critical = response.getString("critical");
                            str_cases_per_million = response.getString("casesPerOneMillion");
                            str_deaths_per_million = response.getString("deathsPerOneMillion");
                            str_one_case_per_people = response.getString("oneCasePerPeople");
                            str_one_death_per_people = response.getString("oneDeathPerPeople");
                            str_one_test_per_people = response.getString("oneTestPerPeople");
                            str_population = response.getString("population");
                            Handler delay = new Handler();

                            delay.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tv_confirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(str_confirmed)));
                                    tv_confirmed_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_confirmed_new)));

                                    tv_active.setText(NumberFormat.getInstance().format(Integer.parseInt(str_active)));

                                    tv_recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(str_recovered)));
                                    tv_recovered_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_recovered_new)));

                                    tv_death.setText(NumberFormat.getInstance().format(Integer.parseInt(str_death)));
                                    tv_death_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_death_new)));

                                    tv_tests.setText(NumberFormat.getInstance().format(Integer.parseInt(str_tests)));

                                    tv_critical.setText(NumberFormat.getInstance().format(Integer.parseInt(str_critical)));

                                    tv_cases_per_million.setText(NumberFormat.getInstance().format(Double.parseDouble(str_cases_per_million)));

                                    tv_deaths_per_million.setText(NumberFormat.getInstance().format(Double.parseDouble(str_deaths_per_million)));

                                    tv_one_case_per_people.setText(NumberFormat.getInstance().format(Integer.parseInt(str_one_case_per_people)));
                                    tv_one_death_per_people.setText(NumberFormat.getInstance().format(Integer.parseInt(str_one_death_per_people)));
                                    tv_one_test_per_people.setText(NumberFormat.getInstance().format(Integer.parseInt(str_one_test_per_people)));

                                    tv_population.setText(NumberFormat.getInstance().format(Integer.parseInt(str_population)));

                                    pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(str_active), Color.parseColor("#007afe")));
                                    pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(str_recovered), Color.parseColor("#08a045")));
                                    pieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(str_death), Color.parseColor("#F6404F")));

                                    pieChart.startAnimation();
                                    activity.DismissDialog();
                                }
                            },1000);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }
        );
        requestQueue.add(jsonObjectRequest);

    }

    public void toUsStateData(View view) {
        Intent intent = new Intent(UnitedStatesData.this,USStateData.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}