package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.majesty.siapopa.adapter.LapHarianAdapter;
import com.majesty.siapopa.model.HasilModel;
import com.majesty.siapopa.model.IntensitasModel;
import com.majesty.siapopa.model.ModelLapor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListLapHarian extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    FloatingActionButton fab;
    List<HasilModel> listHasil;
    List<ModelLapor> listLapor;
    private RecyclerView.Adapter mAdapter;
    String username;
    String userAlamat;
    private ProgressDialog progressDialog;
    String usergroup;
    List<IntensitasModel> listApproval, listApprovalKab;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    TextView txtPeringatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lap_harian);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Laporan Harian");
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.hide();

        txtPeringatan = findViewById(R.id.peringatan);
        txtPeringatan.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ListLapHarian.this);
        recyclerView.setLayoutManager(layoutManager);
        listHasil = new ArrayList<>();
        listApproval = new ArrayList<>();
        listApprovalKab = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        usergroup = Common.currentUser.getId_usergroup();
        calendar = Calendar.getInstance();

        if (usergroup.equals("2")){
            getLaporanKab();
        }else if (usergroup.equals("3")){
            getLaporanSat();
        }else if (usergroup.equals("1")){
            getLaporan();
        }else if (usergroup.equals("4")){
            getLaporanProv();
        }
    }

    private void getLaporanProv() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"ApprovalProv?approval='Sudah','Sudah (Approve by Provinsi)'&provinsi="+userAlamat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String date = object.getString("tanggal_intensitas");
                                String kecamatan = object.getString("kecamatan");
                                String jenis_opt = object.getString("jenis_opt");
                                String desa = object.getString("desa");
                                String id = object.getString("id_intensitas");
                                String intensitas_tambah = object.getString("intensitas_tambah");

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id);
                                intensitasModel.setKecamatan(kecamatan);
                                intensitasModel.setDesa(desa);
                                intensitasModel.setJenis_opt(jenis_opt);
                                intensitasModel.setIntensitas_tambah(intensitas_tambah);
                                intensitasModel.setTanggal_intensitas(date);
                                listApprovalKab.add(intensitasModel);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new LapHarianAdapter(ListLapHarian.this, listApprovalKab);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ListLapHarian.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ListLapHarian.this).add(stringRequest);
    }

    private void getLaporanSat() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"Approvalsat?approval='Sudah','Sudah (Approve by Provinsi)'&kabupaten="+userAlamat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String date = object.getString("tanggal_intensitas");
                                String kecamatan = object.getString("kecamatan");
                                String jenis_opt = object.getString("jenis_opt");
                                String desa = object.getString("desa");
                                String id = object.getString("id_intensitas");
                                String intensitas_tambah = object.getString("intensitas_tambah");

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id);
                                intensitasModel.setKecamatan(kecamatan);
                                intensitasModel.setDesa(desa);
                                intensitasModel.setJenis_opt(jenis_opt);
                                intensitasModel.setIntensitas_tambah(intensitas_tambah);
                                intensitasModel.setTanggal_intensitas(date);
                                listApprovalKab.add(intensitasModel);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new LapHarianAdapter(ListLapHarian.this, listApprovalKab);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ListLapHarian.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ListLapHarian.this).add(stringRequest);
    }

    private void getLaporanKab() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"Approval?status='Sudah','Sudah (Approve by Provinsi)'&kabupaten="+userAlamat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String date = object.getString("tanggal_intensitas");
                                String kecamatan = object.getString("kecamatan");
                                String jenis_opt = object.getString("jenis_opt");
                                String desa = object.getString("desa");
                                String id = object.getString("id_intensitas");
                                String intensitas_tambah = object.getString("intensitas_tambah");

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id);
                                intensitasModel.setKecamatan(kecamatan);
                                intensitasModel.setDesa(desa);
                                intensitasModel.setJenis_opt(jenis_opt);
                                intensitasModel.setIntensitas_tambah(intensitas_tambah);
                                intensitasModel.setTanggal_intensitas(date);
                                listApproval.add(intensitasModel);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new LapHarianAdapter(ListLapHarian.this, listApproval);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ListLapHarian.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ListLapHarian.this).add(stringRequest);
    }

    private void getLaporan(){
        progressDialog.setTitle("Loading");
        progressDialog.show();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        userAlamat = Common.currentUser.getAlamat();
        String kabupaten = Common.currentUser.getKabupaten();
        String urlLaporan = Constants.ROOT_URL+"Intensitas?kabupaten="+kabupaten+"&kecamatan="+userAlamat+"&tanggal="+date;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String date = object.getString("tanggal_intensitas");
                                String status = object.getString("approval_provinsi");
                                String jenis_opt = object.getString("jenis_opt");
                                String desa = object.getString("desa");
                                String id = object.getString("id_intensitas");
                                String intensitas_tambah = object.getString("intensitas_tambah");

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id);
                                intensitasModel.setPetugas_pengamatan(status);
                                intensitasModel.setDesa(desa);
                                intensitasModel.setJenis_opt(jenis_opt);
                                intensitasModel.setIntensitas_tambah(intensitas_tambah);
                                intensitasModel.setTanggal_intensitas(date);
                                listApproval.add(intensitasModel);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new LapHarianAdapter(ListLapHarian.this, listApproval);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ListLapHarian.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ListLapHarian.this).add(stringRequest);
    }
}