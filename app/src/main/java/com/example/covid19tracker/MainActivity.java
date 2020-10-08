package com.example.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button stateWise,history ;
    private Toolbar toolbar;
    private TextView  nameState, total,totalNumber ,active,activeNumber,recovered,recoveredNumber,deaths,deathsNumber;
    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);
        initWidge();
        jsonParse();
        onclick();
    }

    private void jsonParse() {
        String url = "https://api.rootnet.in/covid19-in/stats/latest";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject result = jsonObject.getJSONObject("data");
                            JSONObject res = result.getJSONObject("summary");
                            String total1 = res.getString("total");
                            int act = res.getInt("total")-(res.getInt("discharged")+res.getInt("deaths"));
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

    public void onclick(){
        stateWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,StateWise.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,History.class);
                startActivity(intent);

//                Toast.makeText(MainActivity.this,
//                        " IN PROGRESS ",
//                        Toast.LENGTH_LONG).show();
            }
        });

    }
    public void initWidge(){
        stateWise  = (Button) findViewById(R.id.stateWise);
        history  = (Button)findViewById(R.id.history);
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