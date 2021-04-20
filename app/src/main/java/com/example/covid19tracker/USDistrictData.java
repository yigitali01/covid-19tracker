package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid19tracker.adapter.USDistrictAdapter;
import com.example.covid19tracker.adapter.USStateDataAdapter;
import com.example.covid19tracker.model.USDistrictModel;
import com.example.covid19tracker.model.USStateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class USDistrictData extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv_state_wise;
    private USDistrictAdapter districtWiseAdapter;
    private ArrayList<USDistrictModel> districtWiseModelArrayList;
    private EditText et_search;
    String str_confirmed,str_state_name,str_district_name,str_death;
    MainActivity activity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_us_district_data);

        GetIntent();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(str_state_name);
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

    public void FetchData(){
        activity.ShowDialog(USDistrictData.this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://corona.lmao.ninja/v3/covid-19/jhucsse/counties";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i<response.length(); i++){
                        JSONObject jsonObject = response.getJSONObject(i);

                        if (jsonObject.getString("province").toLowerCase().equals(str_state_name.toLowerCase())){
                            JSONObject jsonStats = jsonObject.getJSONObject("stats");
                            str_district_name = jsonObject.getString("county");
                            str_confirmed = jsonStats.getString("confirmed");
                            str_death = jsonStats.getString("deaths");
                            System.out.println("death"+str_death);
                            USDistrictModel usDistrictModel = new USDistrictModel(str_district_name,str_confirmed,str_death);
                            districtWiseModelArrayList.add(usDistrictModel);

                        }
                    }
                        Handler handler = new Handler();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                districtWiseAdapter.notifyDataSetChanged();
                                activity.DismissDialog();
                            }
                        },2000);

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
        ArrayList<USDistrictModel> modelList = new ArrayList<>();
        for (USDistrictModel model : districtWiseModelArrayList){
            if (model.getProvince().toLowerCase().contains(text.toLowerCase())){
                modelList.add(model);
            }
        }
        districtWiseAdapter.FilterList(modelList,text);
    }

    public void Init(){
        swipeRefreshLayout = findViewById(R.id.activity_us_district_wise_swipe_refresh_layout);
        rv_state_wise = findViewById(R.id.activity_us_district_wise_recyclerview);
        rv_state_wise.setHasFixedSize(true);
        rv_state_wise.setLayoutManager(new LinearLayoutManager(this));
        et_search = findViewById(R.id.activity_us_district_wise_search_editText);
        districtWiseModelArrayList = new ArrayList<>();
        districtWiseAdapter = new USDistrictAdapter(USDistrictData.this,districtWiseModelArrayList);
        rv_state_wise.setAdapter(districtWiseAdapter);
    }

    public void GetIntent(){
        Intent intent = getIntent();
        str_state_name = intent.getStringExtra("StateName");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}