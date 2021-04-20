package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid19tracker.model.IndiaStateModel;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView tv_confirmed, tv_confirmed_new, tv_active, tv_active_new,
            tv_recovered, tv_recovered_new, tv_death, tv_death_new, tv_tests,
            tv_critical,tv_cases_per_million,tv_deaths_per_million;

    SwipeRefreshLayout swipeRefreshLayout;


    boolean lang_selected;

    String str_confirmed, str_confirmed_new, str_active, str_recovered, str_recovered_new,
            str_death, str_death_new, str_tests,str_critical,str_deaths_per_million,str_cases_per_million;

    LinearLayout lin_countrywise;

    ProgressDialog progressDialog;

    PieChart pieChart;
    private int int_active_new = 0;

    LinearLayout toindiadataPage;
    private boolean doubleTimeClickBack = false;
    private Toast backPressToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Add language selection to itemMenu
        LoadLocale();

        getSupportActionBar().setTitle("Covid-19 Tracker(World)");

        //Initializing Views
        Init();

        //Fetch Data From API
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

    private void Init() {
        tv_confirmed = findViewById(R.id.activity_main_confirmed_textview);
        tv_confirmed_new = findViewById(R.id.activity_main_confirmed_new_textview);
        tv_active = findViewById(R.id.activity_main_active_textview);
        tv_active_new = findViewById(R.id.activity_main_active_new_textview);
        tv_recovered = findViewById(R.id.activity_main_recovered_textview);
        tv_recovered_new = findViewById(R.id.activity_main_recovered_new_textview);
        tv_death = findViewById(R.id.activity_main_death_textview);
        tv_death_new = findViewById(R.id.activity_main_death_new_textview);
        tv_tests = findViewById(R.id.activity_main_total_tests_textview);
        tv_cases_per_million = findViewById(R.id.activity_main_cases_per_million_textview);
        tv_critical = findViewById(R.id.activity_main_critical_textview);
        tv_deaths_per_million = findViewById(R.id.activity_main_deaths_per_one_million_textview);
        swipeRefreshLayout = findViewById(R.id.activity_main_swipe_refresh_layout);
        pieChart = findViewById(R.id.activity_main_pie_chart);
        lin_countrywise = findViewById(R.id.activity_main_country_data_button);
        toindiadataPage = findViewById(R.id.activity_main_india_data_button);
    }

    //Get menu design
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflateMenu = getMenuInflater();
        inflateMenu.inflate(R.menu.menu,menu);
        return true;
    }

    //function that adds onClickListener to MenuItems
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_about:
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_change_language:
                ChangeLanguage();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void FetchData(){
        ShowDialog(this);
        String apiUrl = "https://corona.lmao.ninja/v2/all";
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

                            Handler delay = new Handler();

                            delay.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tv_confirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(str_confirmed)));
                                    tv_confirmed_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_confirmed_new)));

                                    tv_active.setText(NumberFormat.getInstance().format(Integer.parseInt(str_active)));

                                    int_active_new = Integer.parseInt(str_confirmed_new)
                                            - (Integer.parseInt(str_recovered_new) + Integer.parseInt(str_death_new));
                                    tv_active_new.setText("+"+NumberFormat.getInstance().format(int_active_new));

                                    tv_recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(str_recovered)));
                                    tv_recovered_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_recovered_new)));

                                    tv_death.setText(NumberFormat.getInstance().format(Integer.parseInt(str_death)));
                                    tv_death_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(str_death_new)));

                                    tv_tests.setText(NumberFormat.getInstance().format(Integer.parseInt(str_tests)));

                                    tv_critical.setText(NumberFormat.getInstance().format(Integer.parseInt(str_critical)));

                                    tv_cases_per_million.setText(NumberFormat.getInstance().format(Double.parseDouble(str_cases_per_million)));

                                    tv_deaths_per_million.setText(NumberFormat.getInstance().format(Double.parseDouble(str_deaths_per_million)));

                                    pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(str_active), Color.parseColor("#007afe")));
                                    pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(str_recovered), Color.parseColor("#08a045")));
                                    pieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(str_death), Color.parseColor("#F6404F")));

                                    pieChart.startAnimation();
                                    DismissDialog();
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

    //Show loading dialog
    public void ShowDialog(Context context) {
        //setting up progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    //Close the loading dialog
    public void DismissDialog() {
        progressDialog.dismiss();
    }

    //The function that allows the "MainActivity" activity to be redirected to the "IndiaData" activity
    public void toIndiaData(View view){
        Intent intent = new Intent(MainActivity.this,IndiaData.class);
        startActivity(intent);
    }

    //The function that allows the "MainActivity" activity to be redirected to the "USData" activity
    public void toUsData(View view){
        Intent intent = new Intent(MainActivity.this,UnitedStatesData.class);
        startActivity(intent);
    }

    //The function that allows the "MainActivity" activity to be redirected to the "CountryData" activity
    public void toCountryData(View view){
        Intent intent = new Intent(MainActivity.this,CountryData.class);
        startActivity(intent);
    }

    //Adding functionality to double time back prees
    @Override
    public void onBackPressed() {
        if(doubleTimeClickBack){
            backPressToast.cancel();
            super.onBackPressed();
            return;
        }
        doubleTimeClickBack = true;
        backPressToast = Toast.makeText(this,"Click back again to exit the app",Toast.LENGTH_SHORT);
        backPressToast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleTimeClickBack = false;
            }
        },2000);
    }


    //Add language selection to itemMenu
    public void ChangeLanguage(){

        final String[] Language = {"English", "Türkçe"};

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle(R.string.select_language_string).setSingleChoiceItems(Language, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lang_selected = Language[which].equals("English");
                if (which == 0){
                    SetLocale("en");
                    recreate();
                }
                else if(which == 1)
                {
                    SetLocale("tr");
                    recreate();
                }

                dialog.dismiss();

            }
        });
        AlertDialog showDialog = alertDialog.create();
        showDialog.show();
    }

    //Set language
    public void SetLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();

        editor.putString("Lang",lang);
        editor.apply();
    }

    //Get language
    public void LoadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings",MODE_PRIVATE);
        String language = preferences.getString("Lang","");
        SetLocale(language);
    }
}
