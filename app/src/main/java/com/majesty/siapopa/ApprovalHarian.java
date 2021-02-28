package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.majesty.siapopa.adapter.ApprovalHarianAdapter;
import com.majesty.siapopa.model.HasilModel;
import com.majesty.siapopa.model.IntensitasModel;
import com.majesty.siapopa.model.ModelLapor;
import com.majesty.siapopa.model.StockModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ApprovalHarian extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    Spinner spOPT;
    FloatingActionButton fab;
    List<HasilModel> listHasil;
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
    String usergroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_harian);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Validasi Harian");
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.hide();
        progressDialog = new ProgressDialog(this);

        TextView txtPeringatan = findViewById(R.id.peringatan);
        txtPeringatan.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ApprovalHarian.this);
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
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        String urlLaporan = Constants.ROOT_URL+"ApprovalProv?approval_kab='Sudah','Valid'&approval_satpel='Sudah','Valid'&provinsi="+userAlamat+"&tanggal="+date;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String jenis_opt = object.getString("jenis_opt");
                                String intensitas_tambah = object.getString("intensitas_tambah");
                                String id_intensitas = object.getString("id_intensitas");
                                String id_hasil = object.getString("id_hasil_pengamatan");
                                String luas_diamati = object.getString("luas_diamati");
                                String blok = object.getString("blok");
                                String status_approval = object.getString("approval_provinsi");
                                String status = object.getString("status");

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id_intensitas);
                                intensitasModel.setBlok(blok);
                                intensitasModel.setDesa(status_approval);
                                intensitasModel.setKabupaten(status);
                                intensitasModel.setJenis_opt(jenis_opt);
                                intensitasModel.setLuas_tanaman(luas_diamati);
                                intensitasModel.setIntensitas_tambah(intensitas_tambah);
                                intensitasModel.setId_hasil_pengamatan(id_hasil);
                                listApproval.add(intensitasModel);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new ApprovalHarianAdapter(ApprovalHarian.this, listApproval);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ApprovalHarian.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ApprovalHarian.this).add(stringRequest);
    }

    private void getApprovalSat() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        String urlLaporan = Constants.ROOT_URL+"Approvalsat?status='Belum','Sudah','Valid','Ditolak'&kabupaten="+userAlamat+"&tanggal="+date;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String jenis_opt = object.getString("jenis_opt");
                                String intensitas_tambah = object.getString("intensitas_tambah");
                                String id_intensitas = object.getString("id_intensitas");
                                String id_hasil = object.getString("id_hasil_pengamatan");
                                String luas_diamati = object.getString("luas_diamati");
                                String blok = object.getString("blok");
                                String status_approval = object.getString("approval_satpel");
                                String status = object.getString("status");

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id_intensitas);
                                intensitasModel.setBlok(blok);
                                intensitasModel.setDesa(status_approval);
                                intensitasModel.setKabupaten(status);
                                intensitasModel.setJenis_opt(jenis_opt);
                                intensitasModel.setLuas_tanaman(luas_diamati);
                                intensitasModel.setIntensitas_tambah(intensitas_tambah);
                                intensitasModel.setId_hasil_pengamatan(id_hasil);
                                listApproval.add(intensitasModel);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new ApprovalHarianAdapter(ApprovalHarian.this, listApproval);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ApprovalHarian.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ApprovalHarian.this).add(stringRequest);
    }

    private void getApproval() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        String urlLaporan = Constants.ROOT_URL+"Approval?status='Belum','Sudah','Valid','Ditolak'&kabupaten="+userAlamat+"&tanggal="+date;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String jenis_opt = object.getString("jenis_opt");
                                String intensitas_tambah = object.getString("intensitas_tambah");
                                String id_intensitas = object.getString("id_intensitas");
                                String id_hasil = object.getString("id_hasil_pengamatan");
                                String luas_diamati = object.getString("luas_diamati");
                                String blok = object.getString("blok");
                                String status_approval = object.getString("approval_kab");
                                String status = object.getString("status");

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id_intensitas);
                                intensitasModel.setBlok(blok);
                                intensitasModel.setDesa(status_approval);
                                intensitasModel.setKabupaten(status);
                                intensitasModel.setJenis_opt(jenis_opt);
                                intensitasModel.setLuas_tanaman(luas_diamati);
                                intensitasModel.setIntensitas_tambah(intensitas_tambah);
                                intensitasModel.setId_hasil_pengamatan(id_hasil);
                                listApproval.add(intensitasModel);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new ApprovalHarianAdapter(ApprovalHarian.this, listApproval);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ApprovalHarian.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ApprovalHarian.this).add(stringRequest);
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