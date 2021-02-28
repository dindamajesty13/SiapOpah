package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.majesty.siapopa.adapter.ApprovalKabAdapter;
import com.majesty.siapopa.model.IntensitasModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Approval extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    FloatingActionButton fab;
    List<IntensitasModel> listApproval, listApprovalKab;
    private RecyclerView.Adapter mAdapter;
    String userAlamat;

    ProgressDialog progressDialog;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    String usergroup;
    TextView txtVarietas, txtDate, txtOPT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Validasi Setengah Bulan");
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.hide();
        progressDialog = new ProgressDialog(this);
        txtVarietas = findViewById(R.id.txtVarietas);
        txtVarietas.setText("Desa");

        txtDate = findViewById(R.id.txtDate);
        txtDate.setText("L Tambah");

        txtOPT = findViewById(R.id.txtOPT);
        txtOPT.setText("L Keadaan");

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(Approval.this);
        recyclerView.setLayoutManager(layoutManager);
        listApproval = new ArrayList<>();
        calendar = Calendar.getInstance();
        usergroup = Common.currentUser.getId_usergroup();

        if (usergroup.equals("2")){
            getApproval();
        }else if (usergroup.equals("3")){
            getApprovalSat();
        }else if (usergroup.equals("4")){
            getApprovalProv();
        }

    }

    private void getApprovalProv() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"ApprovalProv?approval='Belum','Sudah','Valid','Ditolak'&provinsi="+userAlamat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String jenis_opt = object.getString("jenis_opt");
                                String jumlah_tambah = object.getString("jumlah_tambah");
                                String jumlah_keadaan = object.getString("jumlah_keadaan");
                                String desa = object.getString("desa");
                                String id = object.getString("id_hsl_stgh");
                                String status = object.getString("approval_prov");

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id);
                                intensitasModel.setLuas_tambah_serang(jumlah_tambah);
                                intensitasModel.setLuas_keadaan_serang(jumlah_keadaan);
                                intensitasModel.setDesa(desa);
                                intensitasModel.setJenis_opt(jenis_opt);
                                intensitasModel.setPeriode_pengamatan(status);
                                intensitasModel.setTanggal_intensitas(date);
                                listApproval.add(intensitasModel);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new ApprovalKabAdapter(Approval.this, listApproval);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Approval.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Approval.this).add(stringRequest);
    }

    private void getApprovalSat() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"Approvalsat?approval='Belum','Sudah','Valid','Ditolak'&kabupaten="+userAlamat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String jenis_opt = object.getString("jenis_opt");
                                String jumlah_tambah = object.getString("jumlah_tambah");
                                String jumlah_keadaan = object.getString("jumlah_keadaan");
                                String desa = object.getString("desa");
                                String id = object.getString("id_hsl_stgh");
                                String status = object.getString("approval_satpel");

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id);
                                intensitasModel.setLuas_tambah_serang(jumlah_tambah);
                                intensitasModel.setLuas_keadaan_serang(jumlah_keadaan);
                                intensitasModel.setDesa(desa);
                                intensitasModel.setJenis_opt(jenis_opt);
                                intensitasModel.setPeriode_pengamatan(status);
                                intensitasModel.setTanggal_intensitas(date);
                                listApproval.add(intensitasModel);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new ApprovalKabAdapter(Approval.this, listApproval);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Approval.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Approval.this).add(stringRequest);
    }

    private void getApproval() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"Approval?approval='Belum','Sudah','Valid','Ditolak'&kabupaten="+userAlamat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String jenis_opt = object.getString("jenis_opt");
                                String jumlah_tambah = object.getString("jumlah_tambah");
                                String jumlah_keadaan = object.getString("jumlah_keadaan");
                                String desa = object.getString("desa");
                                String id = object.getString("id_hsl_stgh");
                                String status = object.getString("approval_kab");

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id);
                                intensitasModel.setLuas_tambah_serang(jumlah_tambah);
                                intensitasModel.setLuas_keadaan_serang(jumlah_keadaan);
                                intensitasModel.setDesa(desa);
                                intensitasModel.setJenis_opt(jenis_opt);
                                intensitasModel.setPeriode_pengamatan(status);
                                intensitasModel.setTanggal_intensitas(date);
                                listApproval.add(intensitasModel);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new ApprovalKabAdapter(Approval.this, listApproval);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Approval.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Approval.this).add(stringRequest);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}