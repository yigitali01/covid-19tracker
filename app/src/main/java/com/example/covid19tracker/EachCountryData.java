package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

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

public class EachCountryData extends AppCompatActivity {
    private TextView tv_confirmed, tv_confirmed_new, tv_active, tv_active_new, tv_death, tv_death_new,
            tv_recovered, tv_recovered_new, tv_critical, tv_tests, tv_one_case_per_people,
            tv_one_death_per_people, tv_one_test_per_people,tv_population,tv_vaccined;

    private String str_countryName, str_confirmed, str_confirmed_new, str_active, str_death, str_death_new,
            str_recovered, str_one_test_per_people, str_vaccined,
            str_critical, str_one_case_per_people, str_population, str_one_death_per_people, str_recovered_new, str_tests;
    private PieChart pieChart,vaccine_piechart;
    private MainActivity activity = new MainActivity();

    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_country_data);

        //Get intent from last activity
        GetIntent();

        //Initializing Views
        Init();

        //Fetch Data From API
        FetchData();

        getSupportActionBar().setTitle(str_countryName);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Data will Fetched again after user performs swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void GetIntent() {
        Intent intent = getIntent();
        str_countryName = intent.getStringExtra(COUNTRY_NAME);
        str_one_test_per_people = intent.getStringExtra(COUNTRY_ONE_TEST_PER_PEOPLE);
        str_one_death_per_people = intent.getStringExtra(COUNTRY_ONE_DEATH_PER_PEOPLE);
        str_one_case_per_people = intent.getStringExtra(COUNTRY_ONE_CASE_PER_PEOPLE);
        str_critical = intent.getStringExtra(COUNTRY_CRITICAL);
        str_recovered = intent.getStringExtra(COUNTRY_RECOVERED);
        str_death_new = intent.getStringExtra(COUNTRY_NEW_DECEASED);
        str_death = intent.getStringExtra(COUNTRY_DECEASED);
        str_confirmed_new = intent.getStringExtra(COUNTRY_NEW_CONFIRMED);
        str_confirmed = intent.getStringExtra(COUNTRY_CONFIRMED);
        str_active = intent.getStringExtra(COUNTRY_ACTIVE);
        str_tests = intent.getStringExtra(COUNTRY_TESTS);
        str_population = intent.getStringExtra(COUNTRY_POPULATION);
    }

    public void Init() {
        tv_confirmed = findViewById(R.id.activity_each_country_data_confirmed_textView);
        tv_confirmed_new = findViewById(R.id.activity_each_country_data_confirmed_new_textView);
        tv_active = findViewById(R.id.activity_each_country_data_active_textView);
        tv_active_new = findViewById(R.id.activity_each_country_data_active_new_textView);
        tv_recovered = findViewById(R.id.activity_each_country_data_recovered_textView);
        tv_recovered_new = findViewById(R.id.activity_each_country_data_recovered_new_textView);
        tv_death = findViewById(R.id.activity_each_country_data_death_textView);
        tv_death_new = findViewById(R.id.activity_each_country_data_death_new_textView);
        tv_tests = findViewById(R.id.activity_each_country_data_samples_tested_textView);
        tv_one_case_per_people = findViewById(R.id.activity_each_country_data_one_case_per_people_textView);
        tv_one_death_per_people = findViewById(R.id.activity_each_country_data_one_death_per_people_textView);
        tv_one_test_per_people = findViewById(R.id.activity_each_country_data_one_tests_per_people_textView);
        tv_critical = findViewById(R.id.activity_each_country_data_critical_textView);
        tv_population = findViewById(R.id.activity_each_country_data_population_textview);
        tv_vaccined = findViewById(R.id.activity_each_country_data_vaccined_textView);
        pieChart = findViewById(R.id.activity_each_country_data_piechart);
        swipeRefreshLayout = findViewById(R.id.activity_each_country_data_swipe_refresh_layout);

    }

    public void FetchData() {
        activity.ShowDialog(this);
        pieChart.clearChart();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/yy");
        String currentDate = simpleDateFormat.format(new Date());

        Date date = null;
        try {
            date = simpleDateFormat.parse(currentDate);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        Date yesterdayDate = calendar.getTime();
        String yesterdayStr = simpleDateFormat.format(yesterdayDate);
        String url = "https://corona.lmao.ninja/v3/covid-19/vaccine/coverage/countries";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i<response.length();i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                if (jsonObject.getString("country").equals(str_countryName)){
                                   JSONObject jsonTimelineObject = jsonObject.getJSONObject("timeline");

                                    if (str_vaccined==null) {
                                        str_vaccined = jsonTimelineObject.getString(yesterdayStr);
                                    }
                                    else {
                                        str_vaccined = jsonTimelineObject.getString(currentDate);
                                    }

                                   System.out.println(str_vaccined);

                                }

                            }
                            Handler postDelayToshowProgress = new Handler();
                            postDelayToshowProgress.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tv_confirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(str_confirmed)));
                                    tv_confirmed_new.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(str_confirmed_new)));

                                    tv_active.setText(NumberFormat.getInstance().format(Integer.parseInt(str_active)));
                /*int int_active_new = Integer.parseInt(str_confirmed_new)
                        - (Integer.parseInt(str_recovered_new) + Integer.parseInt(str_death_new));
                tv_active_new.setText("+"+NumberFormat.getInstance().format(int_active_new<0 ? 0 : int_active_new));*/
                                    tv_active_new.setText("N/A");

                                    tv_death.setText(NumberFormat.getInstance().format(Integer.parseInt(str_death)));
                                    tv_death_new.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(str_death_new)));

                                    tv_recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(str_recovered)));
                                    //tv_recovered_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_recovered_new)));
                                    tv_recovered_new.setText("N/A");

                                    tv_tests.setText(NumberFormat.getInstance().format(Integer.parseInt(str_tests)));

                                    tv_critical.setText(NumberFormat.getInstance().format(Integer.parseInt(str_critical)));

                                    tv_one_case_per_people.setText(NumberFormat.getInstance().format(Integer.parseInt(str_one_case_per_people)));
                                    tv_one_death_per_people.setText(NumberFormat.getInstance().format(Integer.parseInt(str_one_death_per_people)));
                                    tv_one_test_per_people.setText(NumberFormat.getInstance().format(Integer.parseInt(str_one_test_per_people)));
                                    tv_population.setText(NumberFormat.getInstance().format(Integer.parseInt(str_population)));
                                    tv_vaccined.setText(NumberFormat.getInstance().format(Integer.parseInt(str_vaccined)));
                                    //setting piechart
                                    pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(str_active), Color.parseColor("#007afe")));
                                    pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(str_recovered), Color.parseColor("#08a045")));
                                    pieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(str_death), Color.parseColor("#F6404F")));
//                                    pieChart.addPieSlice(new PieModel("Vaccined", Integer.parseInt(str_vaccined),Color.parseColor("#FF03DAC5")));
                                    pieChart.startAnimation();

                                    activity.DismissDialog();
                                }
                            }, 1000);
                        } catch (JSONException e) {
                            e.printStackTrace();


                        }


                    }

                    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}