package com.example.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class History extends AppCompatActivity {
    private Button btnSelect ;
    private Spinner spnrState;
    private String TAG = "Started";
    DatePickerDialog picker;
    String date = "";
    private TextView nameState, total,datePicker1,totalNumber ,active,activeNumber,recovered,recoveredNumber,deaths,deathsNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Log.d(TAG, "onCreate: started");
        initWidge();
        onclick();
    }
    public void onclick(){
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) spnrState.getSelectedItemId();
                jsonParse(i,date);

                /*Toast.makeText(StateWise.this,
                        "OnClickListener : "+
                                "\n Spinner :"+ String.valueOf(spnrState.getSelectedItemId()),
                        Toast.LENGTH_LONG).show();*/
            }
        });
        datePicker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(History.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                if(dayOfMonth<10){
                                    if(monthOfYear<9){
                                        date = year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                    }
                                    else {
                                        date = year + "-" + (monthOfYear + 1) +  "-0" + dayOfMonth ;
                                    }
                                }
                                if(monthOfYear<9){
                                    if(dayOfMonth<10){
                                        date = year + "-0" + (monthOfYear + 1)  + "-0" + dayOfMonth;
                                    }
                                    else{
                                        date = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth ;
                                    }
                                }
                                datePicker1.setText(date);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }

    private void jsonParse(final int city, final String  date) {
        String url = "https://api.rootnet.in/covid19-in/stats/history";
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
                            JSONArray result = jsonObject.getJSONArray("data");
                            String [] ar1 = getResources().getStringArray(R.array.states);
                            for(int i=0;i<result.length();i++) {
                                JSONObject array = result.getJSONObject(i);
                                String dateCheck = array.getString("day");
                                Log.d(TAG, "onResponse: inside for");
                                if (dateCheck.equals(date)) {
                                    JSONArray region = array.getJSONArray("regional");
                                    int x=0;
                                    for(x=0;x<region.length();x++) {
                                        JSONObject res = region.getJSONObject(x);
                                        String cityx = res.getString("loc");
                                        if (ar1[city].equals(cityx)) {
                                            res = region.getJSONObject(x);
                                            cityx = res.getString("loc");
                                            Log.d(TAG, "onResponse: inside if " + dateCheck + " state : " + cityx);
                                            nameState.setText(cityx);
                                            String total1 = res.getString("confirmedCasesIndian");
                                            int act = res.getInt("confirmedCasesIndian") - (res.getInt("discharged") + res.getInt("deaths"));
                                            String active1 = Integer.toString(act);
                                            String recovered1 = res.getString("discharged");
                                            String death1 = res.getString("deaths");
                                            totalNumber.setText(total1);
                                            activeNumber.setText(active1);
                                            recoveredNumber.setText(recovered1);
                                            deathsNumber.setText(death1);
                                            break;
                                        }
                                        else if (x == region.length() - 1) {
                                            Toast.makeText(History.this,"No Data Found",Toast.LENGTH_LONG).show();
                                            nameState.setText(ar1[city]);
                                            totalNumber.setText("0");
                                            activeNumber.setText("0");
                                            recoveredNumber.setText("0");
                                            deathsNumber.setText("0");
                                        }
                                    }
                                }
                            }
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
        datePicker1 = (TextView) findViewById(R.id.datePicker1);
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