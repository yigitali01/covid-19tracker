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
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid19tracker.adapter.IndiaStateDataAdapter;
import com.example.covid19tracker.model.IndiaStateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class IndiaStateData extends AppCompatActivity {
    private RecyclerView rv_state_wise;
    private IndiaStateDataAdapter stateWiseAdapter;
    private ArrayList<IndiaStateModel> stateWiseModelArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText et_search;

    private String str_state, str_confirmed, str_confirmed_new, str_active, str_recovered, str_recovered_new,
            str_death, str_death_new, str_lastupdatedate;

    private MainActivity activity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_india_state_data);

        getSupportActionBar().setTitle("Select State");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Init();

        FetchStates();

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
                FetchStates();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    public void Init(){
        swipeRefreshLayout = findViewById(R.id.activity_state_wise_swipe_refresh_layout);
        rv_state_wise = findViewById(R.id.activity_state_wise_recyclerview);
        rv_state_wise.setHasFixedSize(true);
        rv_state_wise.setLayoutManager(new LinearLayoutManager(this));
        et_search = findViewById(R.id.activity_state_wise_search_editText);
        stateWiseModelArrayList = new ArrayList<>();
        stateWiseAdapter = new IndiaStateDataAdapter(IndiaStateData.this,stateWiseModelArrayList);
        rv_state_wise.setAdapter(stateWiseAdapter);
    }

    public void FetchStates(){
        activity.ShowDialog(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.covid19india.org/data.json";
        stateWiseModelArrayList.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray stateArray = response.getJSONArray("statewise");
                            stateWiseModelArrayList.clear();

                            for (int i = 1; i<stateArray.length();i++){
                                JSONObject states = stateArray.getJSONObject(i);

                                str_state = states.getString("state");
                                str_confirmed = states.getString("confirmed");
                                str_confirmed_new = states.getString("deltaconfirmed");

                                str_active = states.getString("active");

                                str_death = states.getString("deaths");
                                str_death_new = states.getString("deltadeaths");

                                str_recovered = states.getString("recovered");
                                str_recovered_new = states.getString("deltarecovered");
                                str_lastupdatedate = states.getString("lastupdatedtime");

                                IndiaStateModel indiaStateModel = new IndiaStateModel(str_state,
                                        str_confirmed,str_confirmed_new,str_active,str_death,
                                        str_death_new,str_recovered,str_recovered_new,str_lastupdatedate);

                                stateWiseModelArrayList.add(indiaStateModel);
                                System.out.println(stateWiseModelArrayList);
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    stateWiseAdapter.notifyDataSetChanged();
                                    activity.DismissDialog();
                                }
                            },2000);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public void filter(String text){
        ArrayList<IndiaStateModel> modelList = new ArrayList<>();
        for (IndiaStateModel model : stateWiseModelArrayList){
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