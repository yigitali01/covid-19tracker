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
import com.example.covid19tracker.adapter.IndiaDistrictDataAdapter;
import com.example.covid19tracker.model.IndiaDistrictModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.covid19tracker.Constants.STATE_NAME;

public class IndiaDistrictData extends AppCompatActivity {
    private RecyclerView rv_district_wise;
    private IndiaDistrictDataAdapter districtWiseAdapter;
    private ArrayList<IndiaDistrictModel> districtWiseModelArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText et_search;


    private String str_state_name, str_district, str_confirmed, str_confirmed_new, str_active, str_recovered, str_recovered_new,
            str_death, str_death_new;

    private MainActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_india_district_data);


        //Get data from IndiaEachStateActivity
        GetIntent();

       //Initialize views
        Init();

        //Fetch data from API
        FetchDistrictData();

        getSupportActionBar().setTitle(str_state_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Filtering rows inside of RecyclerView
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

        //Data will Fetched again after user performs swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchDistrictData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void FetchDistrictData() {
        activity.ShowDialog(this);

        String url = "https://api.covid19india.org/v2/state_district_wise.json";

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int flag = 0;

                    districtWiseModelArrayList.clear();

                    for (int i = 1; i<response.length(); i++){
                        JSONObject jsonObjectStateName = response.getJSONObject(i);
                        System.out.println("for a girdi");
                        if(str_state_name.toLowerCase().equals(jsonObjectStateName.getString("state").toLowerCase())){
                            System.out.println("if successful");
                            JSONArray jsonDistrictArray = jsonObjectStateName.getJSONArray("districtData");
                            for (int j = 0; j<jsonDistrictArray.length();j++){
                               JSONObject districtJsonObject = jsonDistrictArray.getJSONObject(j);
                               str_district = districtJsonObject.getString("district");
                               str_active = districtJsonObject.getString("active");
                               str_confirmed = districtJsonObject.getString("confirmed");
                               str_death = districtJsonObject.getString("deceased");
                               str_recovered = districtJsonObject.getString("recovered");
                               JSONObject jsonDeltaDistrict = districtJsonObject.getJSONObject("delta");
                               str_confirmed_new = jsonDeltaDistrict.getString("confirmed");
                               str_death_new = jsonDeltaDistrict.getString("deceased");
                               str_recovered_new = jsonDeltaDistrict.getString("recovered");


                               IndiaDistrictModel indiaDistrictModel = new IndiaDistrictModel(str_district, str_confirmed,
                                       str_active, str_recovered, str_death, str_confirmed_new, str_recovered_new,
                                       str_death_new);

                               districtWiseModelArrayList.add(indiaDistrictModel);
                            }
                          flag=1;


                        }
                      if (flag==1){
                          break;
                      }

                    }
                    Handler makeDelay = new Handler();
                    makeDelay.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            districtWiseAdapter.notifyDataSetChanged();
                            activity.DismissDialog();
                        }
                    }, 2500);
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

    private void Filter(String s) {
        ArrayList<IndiaDistrictModel> filteredList = new ArrayList<>();
        for(IndiaDistrictModel item : districtWiseModelArrayList) {
            if (item.getDistrict().toLowerCase().contains(s.toLowerCase())) {
                filteredList.add(item);
            }
        }
        districtWiseAdapter.filterList(filteredList, s);
    }
    public void Init(){
        rv_district_wise = findViewById(R.id.activity_india_district_data_recyclerview);
        swipeRefreshLayout = findViewById(R.id.activity_india_district_data_swipe_refresh_layout);
        et_search = findViewById(R.id.activity_india_district_data_search_editText);
        activity = new MainActivity();
        rv_district_wise.setHasFixedSize(true);
        rv_district_wise.setLayoutManager(new LinearLayoutManager(this));

        districtWiseModelArrayList = new ArrayList<>();
        districtWiseAdapter = new IndiaDistrictDataAdapter(districtWiseModelArrayList, IndiaDistrictData.this);
        rv_district_wise.setAdapter(districtWiseAdapter);
    }

    public void GetIntent(){
        Intent intent = getIntent();
        str_state_name = intent.getStringExtra(STATE_NAME);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}