package com.majesty.siapopa.ui;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.majesty.siapopa.Common;
import com.majesty.siapopa.Constants;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.BarModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner spOPT, spKab, spOpt, spKec, spinnerOPT;
    BarChart barChart, barChartOPT, barchartKab;
    ArrayList<BarEntry> barEntries;
    ArrayList<BarModel> barModels = new ArrayList<>();
    ArrayList<String> jenis_opt = new ArrayList<>();
    ArrayList<String> listKab = new ArrayList<>();
    ArrayList<String> opt = new ArrayList<>();
    ArrayList<String> listKec = new ArrayList<>();
    ArrayList<String> listOpt = new ArrayList<>();
    ArrayAdapter<String> optAdapter;
    ArrayAdapter<String> kabAdapter;
    ArrayAdapter<String> oPTAdapter;
    ArrayAdapter<String> kecAdapter;
    ArrayAdapter<String> listOptAdapter;
    RequestQueue requestQueue;
    String selectedOPT, selectedKab, selectedOpt, selectedKec, selectedOpT;
    ArrayList<String> labels;
    ProgressDialog progressDialog;
    Calendar calendar;
    String userAlamat;
    SimpleDateFormat dateFormat;


    public ChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartFragment newInstance(String param1, String param2) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);
        spKec = (Spinner) rootView.findViewById(R.id.spKec);
        spOpt = (Spinner) rootView.findViewById(R.id.spOpt);
        requestQueue = Volley.newRequestQueue(getActivity());
        barChart = rootView.findViewById(R.id.barchart);
        progressDialog = new ProgressDialog(getActivity());
        barChart = rootView.findViewById(R.id.barchart);
        barChartOPT = rootView.findViewById(R.id.barchartOPT);
        barchartKab = rootView.findViewById(R.id.barchartKab);

        calendar = Calendar.getInstance();

        spOPT = (Spinner) rootView.findViewById(R.id.spOPT);
        spinnerOPT = (Spinner) rootView.findViewById(R.id.spinnerOPT);
        spKab = (Spinner) rootView.findViewById(R.id.spKab);


        getData();

        getDataOPT();

        getDataKab();
        return rootView;
    }

    private void getDataKab() {
        String urlOpt = Constants.ROOT_URL + "Beranda";

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, urlOpt, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("jenis_opt");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String jenis_opt = jsonObject.optString("opt");
                        listOpt.add(jenis_opt);
                        listOptAdapter = new ArrayAdapter<>(getActivity(), R.layout.my_spinner, listOpt);
                        listOptAdapter.setDropDownViewResource(R.layout.my_spinner);
                        spinnerOPT.setAdapter(listOptAdapter);

                        spinnerOPT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View v,
                                                       int pos, long id) {
                                selectedOpT = parent.getSelectedItem().toString();

                                String urlLaporan = Constants.ROOT_URL+"Beranda?jenis_opt="+selectedOpT;
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

                                                        final String kabupaten = object.getString("kabupaten");
                                                        final String jumlah_keadaan_serang = object.getString("jumlah_keadaan_serang");
                                                        final String jenis_opt = object.getString("jenis_opt");

                                                        barEntries.add(new BarEntry(i, Integer.parseInt(jumlah_keadaan_serang)));
                                                        labels.add(kabupaten);
                                                    }
                                                    BarDataSet barDataSet = new BarDataSet(barEntries, "Kabupaten");
                                                    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

//                                                    Description description = new Description();
//                                                    description.setText("Kumulatif LKS");
//                                                    barchartKab.setDescription(description);

                                                    BarData barData = new BarData(barDataSet);
                                                    barchartKab.setData(barData);

                                                    XAxis xAxis = barchartKab.getXAxis();
                                                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

                                                    xAxis.setPosition(XAxis.XAxisPosition.TOP);
                                                    xAxis.setDrawGridLines(false);
                                                    xAxis.setDrawAxisLine(false);
                                                    xAxis.setGranularity(1f);
                                                    xAxis.setLabelCount(labels.size());
                                                    barchartKab.animateY(2000);
                                                    barchartKab.invalidate();
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
                                    }
                                });
                                Volley.newRequestQueue(getActivity()).add(stringRequest);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {

                            }
                        });

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
        requestQueue.add(jsonObjectRequest1);
    }

    private void getData() {
        String urlOpt = Constants.ROOT_URL + "Beranda";

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, urlOpt, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("jenis_opt");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String jenis_opt = jsonObject.optString("opt");
                        opt.add(jenis_opt);
                        oPTAdapter = new ArrayAdapter<>(getActivity(), R.layout.my_spinner, opt);
                        oPTAdapter.setDropDownViewResource(R.layout.my_spinner);
                        spOpt.setAdapter(oPTAdapter);

                        spOpt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View v,
                                                       int pos, long id) {
                                listKec.clear();
//                                barChart.setVisibility(View.VISIBLE);
                                selectedOpt = parent.getSelectedItem().toString();


//                                String kabupaten = Common.currentUser.getKabupaten();

                                String urlKab = Constants.ROOT_URL+"Beranda?opt="+selectedOpt;
                                JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, urlKab, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray jsonArray = response.getJSONArray("intensitas_stgh_bln");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String kecamatan = jsonObject.optString("kabupaten");
                                                listKec.add(kecamatan);
                                                kecAdapter = new ArrayAdapter<>(getActivity(), R.layout.my_spinner, listKec);
                                                kecAdapter.setDropDownViewResource(R.layout.my_spinner);
                                                spKec.setAdapter(kecAdapter);

                                                spKec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View v,
                                                                               int pos, long id) {
//                                                        barChart.setVisibility(View.VISIBLE);

                                                        selectedKec = spKec.getSelectedItem().toString();


                                                        String urlLaporan = Constants.ROOT_URL+"Beranda?jenis_opt="+selectedOpt+"&kab="+selectedKec;
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

                                                                                final String kecamatan = object.getString("kecamatan");
                                                                                final String jumlah_keadaan_serang = object.getString("jumlah_keadaan_serang");
                                                                                final String jenis_opt = object.getString("jenis_opt");

                                                                                barEntries.add(new BarEntry(i, Integer.parseInt(jumlah_keadaan_serang)));
                                                                                labels.add(kecamatan);
                                                                            }
                                                                            BarDataSet barDataSet = new BarDataSet(barEntries, "Kecamatan");
                                                                            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

//                                                                            Description description = new Description();
//                                                                            description.setText("Data Kumulatif Luas Serangan");
//                                                                            barChart.setDescription(description);

                                                                            BarData barData = new BarData(barDataSet);
                                                                            barChart.setData(barData);

                                                                            XAxis xAxis = barChart.getXAxis();
                                                                            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

                                                                            xAxis.setPosition(XAxis.XAxisPosition.TOP);
                                                                            xAxis.setDrawGridLines(false);
                                                                            xAxis.setDrawAxisLine(false);
                                                                            xAxis.setGranularity(1f);
                                                                            xAxis.setLabelCount(labels.size());
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
                                                                Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                        Volley.newRequestQueue(getActivity()).add(stringRequest);
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
                                requestQueue.add(jsonObjectRequest1);
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
        requestQueue.add(jsonObjectRequest1);
    }

    private void getDataOPT() {
        String urlOpt = Constants.ROOT_URL + "Beranda";

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, urlOpt, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("jenis_opt");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String opt = jsonObject.optString("opt");
                        jenis_opt.add(opt);
                        optAdapter = new ArrayAdapter<>(getActivity(), R.layout.my_spinner, jenis_opt);
                        optAdapter.setDropDownViewResource(R.layout.my_spinner);
                        spOPT.setAdapter(optAdapter);

                        spOPT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View v,
                                                       int pos, long id) {
                                listKab.clear();
                                barChart.setVisibility(View.VISIBLE);
                                selectedOPT = parent.getSelectedItem().toString();


//                                String kabupaten = Common.currentUser.getKabupaten();

                                String urlKab = Constants.ROOT_URL+"Beranda?opt="+selectedOPT;
                                JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, urlKab, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray jsonArray = response.getJSONArray("intensitas_stgh_bln");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String opt = jsonObject.optString("kabupaten");
                                                listKab.add(opt);
                                                kabAdapter = new ArrayAdapter<>(getActivity(), R.layout.my_spinner, listKab);
                                                kabAdapter.setDropDownViewResource(R.layout.my_spinner);
                                                spKab.setAdapter(kabAdapter);

                                                spKab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View v,
                                                                               int pos, long id) {
                                                        barChart.setVisibility(View.VISIBLE);

                                                        selectedKab = spKab.getSelectedItem().toString();


                                                        String urlLaporan = Constants.ROOT_URL+"Beranda?opt="+selectedOPT+"&kabupaten="+selectedKab;
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

                                                                                final String ringan = object.getString("ringan");
                                                                                final String sedang = object.getString("sedang");
                                                                                final String berat = object.getString("berat");
                                                                                final String puso = object.getString("puso");
                                                                                final String jenis_opt = object.getString("jenis_opt");

                                                                                barEntries.add(new BarEntry(0, Integer.parseInt(ringan)));
                                                                                barEntries.add(new BarEntry(1, Integer.parseInt(sedang)));
                                                                                barEntries.add(new BarEntry(2, Integer.parseInt(berat)));
                                                                                barEntries.add(new BarEntry(3, Integer.parseInt(puso)));
                                                                                labels.add("R");
                                                                                labels.add("S");
                                                                                labels.add("B");
                                                                                labels.add("P");
                                                                            }
                                                                            BarDataSet barDataSet = new BarDataSet(barEntries, "RSBP");
                                                                            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

//                                                                            Description description = new Description();
//                                                                            description.setText("Data Luas Serangan");
//                                                                            barChartOPT.setDescription(description);

                                                                            BarData barData = new BarData(barDataSet);
                                                                            barChartOPT.setData(barData);

                                                                            XAxis xAxis = barChartOPT.getXAxis();
                                                                            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

                                                                            xAxis.setPosition(XAxis.XAxisPosition.TOP);
                                                                            xAxis.setDrawGridLines(false);
                                                                            xAxis.setDrawAxisLine(false);
                                                                            xAxis.setGranularity(1f);
                                                                            xAxis.setLabelCount(labels.size());
                                                                            barChartOPT.animateY(2000);
                                                                            barChartOPT.invalidate();
                                                                        }catch (Exception e){
                                                                            e.printStackTrace();
                                                                        }

                                                                    }
                                                                }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                        Volley.newRequestQueue(getActivity()).add(stringRequest);
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
                                requestQueue.add(jsonObjectRequest1);
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
        requestQueue.add(jsonObjectRequest1);

    }

    private void getLokasiStock() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        userAlamat = Common.currentUser.getKabupaten();
        dateFormat = new SimpleDateFormat("yyyy-MM");
        final String tanggal = dateFormat.format(calendar.getTime());
        String urlLaporan = Constants.ROOT_URL+"Stock_Popt?kabupaten="+userAlamat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                progressDialog.dismiss();

                                String lokasi_stock = object.getString("lokasi_stock");

                                String urlLaporan = Constants.ROOT_URL+"Stock_Popt?lokasi="+lokasi_stock+"&date="+tanggal;
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
                                        Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
                                    }
                                });
                                Volley.newRequestQueue(getActivity()).add(stringRequest);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}