package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.majesty.siapopa.model.BarModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Chart extends AppCompatActivity {
    Toolbar toolbar;
    Spinner spOPT;
    BarChart barChart;
    ArrayList<BarEntry> barEntries;
    ArrayList<BarModel> barModels = new ArrayList<>();
    ArrayList<String> jenis_opt = new ArrayList<>();
    ArrayAdapter<String> optAdapter;
    RequestQueue requestQueue;
    String selectedOPT, intensitas_keadaan;
    ArrayList<String> labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        spOPT = (Spinner) findViewById(R.id.spOPT);
        requestQueue = Volley.newRequestQueue(this);
        barChart = findViewById(R.id.barchart);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Peringkat OPT");
        setSupportActionBar(toolbar);

        getDataOPT();
    }

    private void getDataOPT() {
        String url = Constants.ROOT_URL+"Populate_OPT";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("jenis_opt");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String opt = jsonObject.optString("opt");
                        jenis_opt.add(opt);
                        optAdapter = new ArrayAdapter<>(Chart.this, android.R.layout.simple_dropdown_item_1line, jenis_opt);
                        optAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spOPT.setAdapter(optAdapter);

                        spOPT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View v,
                                                       int pos, long id) {
                                barChart.setVisibility(View.VISIBLE);
                                selectedOPT = spOPT.getSelectedItem().toString();

                                String urlLaporan = Constants.ROOT_URL+"Bar?opt="+selectedOPT;
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    barEntries = new ArrayList<>();
                                                    labels = new ArrayList<>();
                                                    JSONArray array = new JSONArray(response);
                                                    for (int i = 0; i<array.length(); i++){
                                                        JSONObject object = array.getJSONObject(i);

                                                        String tanggal = object.getString("kabupaten");
                                                        intensitas_keadaan = object.getString("luas_keadaan_serang");

                                                        barEntries.add(new BarEntry(i, Integer.parseInt(intensitas_keadaan)));
                                                        labels.add(tanggal);
                                                    }
                                                    BarDataSet barDataSet = new BarDataSet(barEntries, "Report OPT");
                                                    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                                    Description description = new Description();
                                                    description.setText("Date");
                                                    barChart.setDescription(description);
                                                    BarData barData = new BarData(barDataSet);
                                                    barChart.setData(barData);

                                                    XAxis xAxis = barChart.getXAxis();
                                                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

                                                    xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
                                                    xAxis.setDrawGridLines(false);
                                                    xAxis.setDrawAxisLine(false);
                                                    xAxis.setGranularity(1f);
                                                    xAxis.setLabelCount(labels.size());
                                                    xAxis.setLabelRotationAngle(35);
                                                    barChart.animateY(2000);
                                                    barChart.invalidate();
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        barChart.setVisibility(View.GONE);
                                        Toast.makeText(Chart.this, "No Data Found", Toast.LENGTH_LONG).show();
                                    }
                                });
                                Volley.newRequestQueue(Chart.this).add(stringRequest);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {

                            }});

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

//    @Override
//    public void onBackPressed() {
//        Log.d("CDA", "onBackPressed Called");
//        Intent setIntent = new Intent(Intent.ACTION_MAIN);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(setIntent);
//    }
}
