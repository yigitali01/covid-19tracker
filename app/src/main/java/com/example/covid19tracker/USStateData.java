package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid19tracker.adapter.IndiaStateDataAdapter;
import com.example.covid19tracker.adapter.USStateDataAdapter;
import com.example.covid19tracker.model.CountryModel;
import com.example.covid19tracker.model.IndiaStateModel;
import com.example.covid19tracker.model.USStateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class USStateData extends AppCompatActivity {
    private RecyclerView rv_state_wise;
    private USStateDataAdapter stateWiseAdapter;
    private ArrayList<USStateModel> stateWiseModelArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText et_search;

    String str_confirmed,str_tests_per_million, str_confirmed_new, str_active, str_recovered, str_recovered_new,str_state,
            str_death, str_death_new, str_tests,str_critical,str_deaths_per_million,str_cases_per_million,
            str_one_case_per_people,str_one_death_per_people,str_one_test_per_people,str_population;

    private MainActivity activity = new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_us_state_data);

        getSupportActionBar().setTitle("Select State");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Init();

        FetchData();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void Init(){
        swipeRefreshLayout = findViewById(R.id.activity_us_state_wise_swipe_refresh_layout);
        rv_state_wise = findViewById(R.id.activity_us_state_wise_recyclerview);
        rv_state_wise.setHasFixedSize(true);
        rv_state_wise.setLayoutManager(new LinearLayoutManager(this));
        et_search = findViewById(R.id.activity_us_state_wise_search_editText);
        stateWiseModelArrayList = new ArrayList<>();
        stateWiseAdapter = new USStateDataAdapter(USStateData.this,stateWiseModelArrayList);
        rv_state_wise.setAdapter(stateWiseAdapter);
    }

    public void FetchData() {

        String url = "https://corona.lmao.ninja/v3/covid-19/states";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        activity.ShowDialog(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    stateWiseModelArrayList.clear();
                    for (int i = 0; i<response.length(); i++){

                        JSONObject stateJsonObject = response.getJSONObject(i);
                        str_state = stateJsonObject.getString("state");
                        str_confirmed = stateJsonObject.getString("cases");
                        str_confirmed_new = stateJsonObject.getString("todayCases");
                        str_active = stateJsonObject.getString("active");
                        str_recovered = stateJsonObject.getString("recovered");
                        str_death = stateJsonObject.getString("deaths");
                        str_death_new = stateJsonObject.getString("todayDeaths");
                        str_tests = stateJsonObject.getString("tests");
                        str_cases_per_million = stateJsonObject.getString("casesPerOneMillion");
                        str_deaths_per_million = stateJsonObject.getString("deathsPerOneMillion");
                        str_tests_per_million = stateJsonObject.getString("testsPerOneMillion");
                        str_population = stateJsonObject.getString("population");

                        USStateModel usStateModel = new USStateModel(str_state,str_confirmed,str_confirmed_new,
                                str_active,str_death,str_death_new,str_recovered,str_tests,
                                str_tests_per_million,str_deaths_per_million,str_cases_per_million,str_population);

                        stateWiseModelArrayList.add(usStateModel);
                    }

                    Handler makeDelay = new Handler();
                    makeDelay.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stateWiseAdapter.notifyDataSetChanged();
                            activity.DismissDialog();
                        }
                    }, 1000);
                }
                catch (JSONException e) {
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

    public void filter(String text){
        ArrayList<USStateModel> modelList = new ArrayList<>();
        for (USStateModel model : stateWiseModelArrayList){
            if (model.getState().toLowerCase().contains(text.toLowerCase())){
                modelList.add(model);
            }
        }
        stateWiseAdapter.FilterList(modelList,text);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}