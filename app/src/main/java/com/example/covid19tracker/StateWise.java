package com.example.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StateWise extends AppCompatActivity {
    private Button btnSelect ;
    private Spinner spnrState;
    private  String TAG = "Started";
    private TextView nameState, total,totalNumber ,active,activeNumber,recovered,recoveredNumber,deaths,deathsNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_wise);
        Log.d(TAG, "onCreate: started");
        initWidge();
        onclick();
    }
    public void onclick(){
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) spnrState.getSelectedItemId();
                jsonParse(i);
                /*Toast.makeText(StateWise.this,
                        "OnClickListener : "+
                                "\n Spinner :"+ String.valueOf(spnrState.getSelectedItemId()),
                        Toast.LENGTH_LONG).show();*/
            }
        });
    }
    private void jsonParse(final int city) {
        String url = "https://api.rootnet.in/covid19-in/stats/latest";
        RequestQueue mQueue = null;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        mQueue = new RequestQueue(cache, network);
        mQueue.start();
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            Log.d(TAG, "onResponse: jsonWorking");
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject result = jsonObject.getJSONObject("data");
                            JSONArray c = result.getJSONArray("regional");
                            JSONObject res = c.getJSONObject(city);
                            String loc = res.getString("loc");
                            Log.d(TAG, "onResponse: location : "+loc);
                            nameState.setText(loc);
                            String total1 = res.getString("confirmedCasesIndian");
                            int act = res.getInt("confirmedCasesIndian")-(res.getInt("discharged")+res.getInt("deaths"));
                            String active1 = Integer.toString(act);
                            String recovered1 = res.getString("discharged");
                            String death1 = res.getString("deaths");
                            totalNumber.setText(total1);
                            activeNumber.setText(active1);
                            recoveredNumber.setText(recovered1);
                            deathsNumber.setText(death1);
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
     mQueue.add(request);
    }
    public void initWidge(){
        btnSelect  = (Button) findViewById(R.id.btnSelect);
        spnrState  = (Spinner) findViewById(R.id.spnrState);
        nameState = (TextView) findViewById(R.id.nameState);
        total = (TextView) findViewById(R.id.total);
        totalNumber = (TextView) findViewById(R.id.totalNumber);
        active = (TextView) findViewById(R.id.active);
        activeNumber = (TextView) findViewById(R.id.activeNumber);
        recovered = (TextView) findViewById(R.id.recovered);
        deaths = (TextView) findViewById(R.id.deaths);
        recoveredNumber = (TextView) findViewById(R.id.recoveredNumber);
        deathsNumber = (TextView) findViewById(R.id.deathsNumber);
    }
}