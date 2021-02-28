package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StockPopt extends AppCompatActivity {
    ProgressDialog progressDialog;
    String userAlamat;
    ArrayList<BarEntry> barEntries;
    ArrayList<String> labels;
    BarChart barChart;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    ArrayList<String> jenis_opt = new ArrayList<>();
    ArrayAdapter<String> optAdapter;
    Toolbar toolbar;
    Spinner spOPT;
    ArrayList<BarModel> barModels = new ArrayList<>();
    RequestQueue requestQueue;
    String selectedOPT, intensitas_keadaan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_popt);
        progressDialog = new ProgressDialog(this);
        barChart = findViewById(R.id.barchart);
        spOPT = findViewById(R.id.spOPT);
        calendar = Calendar.getInstance();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chart Stock Pestisida");
        requestQueue = Volley.newRequestQueue(this);

        getLokasiStock();
    }

    private void getLokasiStock() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        userAlamat = Common.currentUser.getKabupaten();
        dateFormat = new SimpleDateFormat("yyyy-MM");
        date = dateFormat.format(calendar.getTime());
        String url = Constants.ROOT_URL+"Bar";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("lokasi_brigade");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String opt = jsonObject.optString("kabupaten");
                        jenis_opt.add(opt);
                        optAdapter = new ArrayAdapter<>(StockPopt.this, android.R.layout.simple_dropdown_item_1line, jenis_opt);
                        optAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spOPT.setAdapter(optAdapter);

                        spOPT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View v,
                                                       int pos, long id) {
                                barChart.setVisibility(View.VISIBLE);
                                selectedOPT = spOPT.getSelectedItem().toString();

                                String urlLaporan = Constants.ROOT_URL+"Stock_Popt?lokasi="+selectedOPT+"&date="+date;
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    barEntries = new ArrayList<>();
                                                    labels = new ArrayList<>();
                                                    final JSONArray array = new JSONArray(response);
                                                    for (int i = 0; i<array.length(); i++){
                                                        JSONObject object = array.getJSONObject(i);
                                                        progressDialog.dismiss();

                                                        final String jenis_pestisida = object.getString("jenis_pestisida");
                                                        final String total_pestisida = object.getString("total");

                                                        barEntries.add(new BarEntry(i, Float.parseFloat(total_pestisida)));
                                                        labels.add(jenis_pestisida);
                                                    }
                                                    BarDataSet barDataSet = new BarDataSet(barEntries, "Pestisida");
                                                    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                                                    Description description = new Description();
                                                    description.setText("Data Stock Pestisida");
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
                                        progressDialog.dismiss();
                                        Toast.makeText(StockPopt.this, "No Data Found", Toast.LENGTH_LONG).show();
                                    }
                                });
                                Volley.newRequestQueue(StockPopt.this).add(stringRequest);
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
}