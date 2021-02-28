package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.majesty.siapopa.adapter.PermohonanAdapter;
import com.majesty.siapopa.model.IntensitasModel;
import com.majesty.siapopa.model.ModelLapor;
import com.majesty.siapopa.model.ModelPermohonan;
import com.majesty.siapopa.model.StockModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Permohonan extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    Spinner spOPT;
    FloatingActionButton fab;
    List<ModelPermohonan> listHasil;
    List<ModelLapor> listLapor;
    List<IntensitasModel> listApproval, listApprovalKab, listApprovalProv;
    List<StockModel> listStock;
    private RecyclerView.Adapter mAdapter;
    String username;
    String userAlamat;

    ProgressDialog progressDialog;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permohonan);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Permohonan");
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);

        fab = findViewById(R.id.fab);
        fab.hide();

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(Permohonan.this);
        recyclerView.setLayoutManager(layoutManager);
        listHasil = new ArrayList<ModelPermohonan>();
        calendar = Calendar.getInstance();
//
        getLaporan();
    }

    private void getLaporan(){
        progressDialog.setTitle("Loading");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"Approval_Permohonan?status=Sudah";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String date = object.getString("tanggal_permohonan");
                                String kecamatan = object.getString("kecamatan");
                                String varietas = object.getString("varietas");
                                String desa = object.getString("desa");
                                String id = object.getString("id_permohonan");
                                String jenis_opt = object.getString("jenis_opt");

                                ModelPermohonan modelPermohonan = new ModelPermohonan();
                                modelPermohonan.setId_permohonan(id);
                                modelPermohonan.setKecamatan(kecamatan);
                                modelPermohonan.setDesa(desa);
                                modelPermohonan.setVarietas(varietas);
                                modelPermohonan.setJenis_opt(jenis_opt);
                                modelPermohonan.setTanggal_permohonan(date);
                                listHasil.add(modelPermohonan);


                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new PermohonanAdapter(Permohonan.this, listHasil);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Permohonan.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Permohonan.this).add(stringRequest);
    }
}