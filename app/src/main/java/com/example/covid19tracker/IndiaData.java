package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.sql.Time;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class IndiaData extends AppCompatActivity {
    private TextView tv_confirmed, tv_confirmed_new, tv_active, tv_active_new, tv_recovered, tv_recovered_new, tv_death,
            tv_death_new, tv_tests, tv_tests_new, tv_date, tv_time;
    private LinearLayout state_data_button;
    private PieChart pieChart;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String str_confirmed, str_confirmed_new, str_active, str_recovered, str_recovered_new,
            str_death, str_death_new, str_tests, str_tests_new, str_last_update_time;
    private int int_active_new;
    private MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_india_data);
        
        getSupportActionBar().setTitle("Covid-19 Tracker(India)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Initialize views
        Init();

        //Fetch data from API
        FetchData();

        //Data will Fetched again after user performs swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchData();
                swipeRefreshLayout.setRefreshing(false);
                //Toast.makeText(MainActivity.this, "Data refreshed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Init(){
        tv_confirmed = findViewById(R.id.activity_india_data_confirmed_textview);
        tv_confirmed_new = findViewById(R.id.activity_india_data_confirmed_new_textview);
        tv_active = findViewById(R.id.activity_india_data_active_textview);
        tv_active_new = findViewById(R.id.activity_india_data_active_new_textview);
        tv_recovered = findViewById(R.id.activity_india_data_recovered_textview);
        tv_recovered_new = findViewById(R.id.activity_india_data_recovered_new_textview);
        tv_death = findViewById(R.id.activity_india_data_death_textview);
        tv_death_new = findViewById(R.id.activity_india_data_death_new_textview);
        tv_tests = findViewById(R.id.activity_india_data_samples_tested_textview);
        tv_tests_new = findViewById(R.id.activity_india_data_samples_tested_new_textview);
        tv_date = findViewById(R.id.activity_india_last_update_date_textview);
        tv_time = findViewById(R.id.activity_india_last_update_time_textview);

        pieChart = findViewById(R.id.activity_india_data_pie_chart);
        swipeRefreshLayout = findViewById(R.id.activity_india_data_swipe_refresh_layout);
        state_data_button = findViewById(R.id.activity_india_data_state_data_button);

    }

    private void FetchData(){
        mainActivity.ShowDialog(this);
        String apiUrl = "https://api.covid19india.org/data.json";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        System.out.println("Fetch dataya girdi");
        pieChart.clearChart();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,apiUrl,null,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        System.out.println("onresponse a girdi");
                        JSONArray all_state_jsonArray = null;
                        JSONArray testData_jsonArray = null;

                        try {
                            all_state_jsonArray = response.getJSONArray("statewise");
                            testData_jsonArray = response.getJSONArray("tested");

                            JSONObject total_india_data = all_state_jsonArray.getJSONObject(0);
                            JSONObject test_india_data = testData_jsonArray.getJSONObject(testData_jsonArray.length()-1);
                            //Fetching data for India and storing it in String
                            str_confirmed = total_india_data.getString("confirmed"); //Confirmed cases in India
                            str_confirmed_new = total_india_data.getString("deltaconfirmed");//New confirmed cases in India

                            str_active = total_india_data.getString("active");    //Active cases in India

                            str_recovered = total_india_data.getString("recovered");  //Total recovered cased in India
                            str_recovered_new = total_india_data.getString("deltarecovered"); //New recovered cases from last update time

                            str_death = total_india_data.getString("deaths");     //Total deaths in India
                            str_death_new = total_india_data.getString("deltadeaths");    //New death cases from last update time

                            str_last_update_time = total_india_data.getString("lastupdatedtime"); //Last update date and time

                            str_tests = test_india_data.getString("totalsamplestested"); //Total samples tested in India
                            str_tests_new = test_india_data.getString("samplereportedtoday");   //New samples tested today
                            System.out.println("confirmed:"+str_confirmed);
                            Handler delay = new Handler();

                            delay.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tv_confirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(str_confirmed)));
                                    tv_confirmed_new.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(str_confirmed_new)));

                                    tv_active.setText(NumberFormat.getInstance().format(Integer.parseInt(str_active)));

                                    int_active_new = Integer.parseInt(str_confirmed_new)
                                            - (Integer.parseInt(str_recovered_new) + Integer.parseInt(str_death_new));
                                    tv_active_new.setText("+"+NumberFormat.getInstance().format(int_active_new));

                                    tv_recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(str_recovered)));
                                    tv_recovered_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_recovered_new)));

                                    tv_death.setText(NumberFormat.getInstance().format(Integer.parseInt(str_death)));
                                    tv_death_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_death_new)));

                                    tv_tests.setText(NumberFormat.getInstance().format(Integer.parseInt(str_tests)));
                                    tv_tests_new.setText("+"+ NumberFormat.getInstance().format(Integer.parseInt(str_tests_new)));
                                    System.out.println("confirmed:"+str_confirmed);
                                    tv_date.setText(FormatDate(str_last_update_time, 1));
                                    tv_time.setText(FormatDate(str_last_update_time, 2));

                                    pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(str_active), Color.parseColor("#007afe")));
                                    pieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(str_death), Color.parseColor("#F6404F")));
                                    pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(str_recovered), Color.parseColor("#08a045")));

                                    pieChart.startAnimation();

                                    mainActivity.DismissDialog();
                                }
                            },1000);
                        } catch (JSONException e) {
                            System.out.println("------------something catch------");
                            e.printStackTrace();
                            System.out.println("------------something catch------");

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                    }
                }
                );
        requestQueue.add(jsonObjectRequest);
    }

    //Formatting Date type String
    public String FormatDate(String date, int testCase) {
        Date mDate = null;
        String dateFormat;
        try {
            mDate = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault()).parse(date);

            if (testCase == 0) {
                dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(mDate);
                return dateFormat;
            } else if (testCase == 1) {
                dateFormat = new SimpleDateFormat("dd MMM yyyy").format(mDate);
                return dateFormat;
            } else if (testCase == 2) {
                dateFormat = new SimpleDateFormat("hh:mm a").format(mDate);
                return dateFormat;
            } else {
                Log.d("error", "Wrong input! Choose from 0 to 2");
                return "Error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //The function that allows the "MainActivity" activity to be redirected to the "IndiaStateData" activity
    public void toIndiaStateData(View view){

        Intent intent = new Intent(IndiaData.this,IndiaStateData.class);
        startActivity(intent);

    }
}

