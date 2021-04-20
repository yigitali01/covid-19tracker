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
import com.example.covid19tracker.adapter.CountryDataAdapter;
import com.example.covid19tracker.model.CountryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CountryData extends AppCompatActivity {

    private RecyclerView rv_country_wise;
    private CountryDataAdapter countryDataAdapter;
    private ArrayList<CountryModel> countryModelArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText et_search;

    private String str_country, str_confirmed, str_confirmed_new, str_active, str_recovered, str_recovered_new,str_population,str_critical,
            str_death, str_death_new, str_tests , str_one_case_per_people,str_one_death_per_people,str_one_test_per_people;

    private MainActivity activity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_data);

        //Initializing Views
        Init();

        //Fetch Data From API
        FetchData();

        //Filter the list after the text changed
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Filter(s.toString());
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Data will Fetched again after user performs swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //Filtering rows inside of RecyclerView
    private void Filter(String text) {
        ArrayList<CountryModel> filteredList = new ArrayList<>();
        for (CountryModel item : countryModelArrayList) {
            if (item.getCountry().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        countryDataAdapter.filterList(filteredList, text);
    }

    public void FetchData() {

        String url = "https://corona.lmao.ninja/v3/covid-19/countries/";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        activity.ShowDialog(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    countryModelArrayList.clear();
                for (int i = 0; i<response.length(); i++){

                    JSONObject countryJsonObject = response.getJSONObject(i);
                    str_country = countryJsonObject.getString("country");
                    str_confirmed = countryJsonObject.getString("cases");
                    str_confirmed_new = countryJsonObject.getString("todayCases");
                    str_active = countryJsonObject.getString("active");
                    str_recovered = countryJsonObject.getString("recovered");
                    str_death = countryJsonObject.getString("deaths");
                    str_death_new = countryJsonObject.getString("todayDeaths");
                    str_tests = countryJsonObject.getString("tests");
                    str_critical = countryJsonObject.getString("critical");
                    str_one_case_per_people = countryJsonObject.getString("oneCasePerPeople");
                    str_one_death_per_people = countryJsonObject.getString("oneDeathPerPeople");
                    str_one_test_per_people = countryJsonObject.getString("oneTestPerPeople");
                    str_population = countryJsonObject.getString("population");
                    JSONObject countryJsonFlagObject = countryJsonObject.getJSONObject("countryInfo");
                    String flagUrl = countryJsonFlagObject.getString("flag");

                    CountryModel countryModel = new CountryModel(str_country,str_confirmed,str_confirmed_new,
                                                                str_active,str_death,str_death_new,str_recovered,
                                                                str_recovered_new,str_critical,str_tests,flagUrl,
                                                                str_one_case_per_people,str_one_death_per_people,str_one_test_per_people,str_population);

                    countryModelArrayList.add(countryModel);
                }
                Collections.sort(countryModelArrayList, new Comparator<CountryModel>() {
                        @Override
                        public int compare(CountryModel o1, CountryModel o2) {
                            if (Integer.parseInt(o1.getConfirmed())>Integer.parseInt(o2.getConfirmed())){
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    });
                    Handler makeDelay = new Handler();
                    makeDelay.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            countryDataAdapter.notifyDataSetChanged();
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

    public void Init() {

        rv_country_wise = findViewById(R.id.activity_country_wise_recyclerview);
        et_search = findViewById(R.id.activity_country_wise_search_editText);
        swipeRefreshLayout = findViewById(R.id.activity_country_wise_swipe_refresh_layout);

        countryModelArrayList = new ArrayList<>();
        countryDataAdapter = new CountryDataAdapter(this,countryModelArrayList);

        rv_country_wise.setHasFixedSize(true);
        rv_country_wise.setLayoutManager(new LinearLayoutManager(this));

        rv_country_wise.setAdapter(countryDataAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}