package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.majesty.siapopa.adapter.LaporAdapter;
import com.majesty.siapopa.model.IntensitasModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Laporan extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<IntensitasModel> listLapor, listApproval, listApprovalKab, listApprovalProv;
    private RecyclerView.Adapter mAdapter;
    String userAlamat;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Laporan");
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.hide();
        progressDialog = new ProgressDialog(this);

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(Laporan.this);
        recyclerView.setLayoutManager(layoutManager);

        switch (Common.currentUser.getId_usergroup()) {
            case "2":
                listApproval = new ArrayList<>();
                getApproval();
                break;
            case "3":
                listApprovalKab = new ArrayList<>();
                getApprovalSat();
                break;
            case "4":
                listApprovalProv = new ArrayList<>();
                getApprovalData();
                break;
            case "1":
                listLapor = new ArrayList<>();
                getLaporan();
                break;
        }


    }

    private void getLaporan(){
        progressDialog.setTitle("Loading");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"Intensitas?kecamatan="+userAlamat+"&status=Sudah";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String tanggal_intensitas = object.getString("tanggal_intensitas");
                                String kecamatan = object.getString("kecamatan");
                                String periode = object.getString("periode_pengamatan");
                                String opt = object.getString("jenis_opt");
                                String id = object.getString("id_intensitas");
                                String desa = object.getString("desa");

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id);
                                intensitasModel.setKecamatan(kecamatan);
                                intensitasModel.setTanggal_intensitas(tanggal_intensitas);
                                intensitasModel.setJenis_opt(opt);
                                intensitasModel.setPeriode_pengamatan(periode);
                                intensitasModel.setDesa(desa);
                                listLapor.add(intensitasModel);


                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new LaporAdapter(Laporan.this, listLapor);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Laporan.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Laporan.this).add(stringRequest);
    }

    private void getApprovalData() {
        progressDialog.setTitle("Loading");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"ApprovalProv?provinsi="+userAlamat+"&approval_provinsi=Sudah";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String tanggal_intensitas = object.getString("tanggal_intensitas");
                                String kecamatan = object.getString("kecamatan");
                                String periode = object.getString("periode_pengamatan");
                                String opt = object.getString("jenis_opt");
                                String id = object.getString("id_intensitas");
                                String desa = object.getString("desa");

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id);
                                intensitasModel.setKecamatan(kecamatan);
                                intensitasModel.setTanggal_intensitas(tanggal_intensitas);
                                intensitasModel.setJenis_opt(opt);
                                intensitasModel.setPeriode_pengamatan(periode);
                                intensitasModel.setDesa(desa);
                                listApprovalProv.add(intensitasModel);


                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new LaporAdapter(Laporan.this, listApprovalProv);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Laporan.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Laporan.this).add(stringRequest);
    }

    private void getApprovalSat() {
        progressDialog.setTitle("Loading");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"Approvalsat?status='Sudah','Terlambat','Ditolak'&kabupaten="+userAlamat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String tanggal_intensitas = object.getString("tanggal_intensitas");
                                String kecamatan = object.getString("kecamatan");
                                String periode = object.getString("periode_pengamatan");
                                String opt = object.getString("jenis_opt");
                                String id = object.getString("id_intensitas");
                                String desa = object.getString("desa");

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id);
                                intensitasModel.setKecamatan(kecamatan);
                                intensitasModel.setTanggal_intensitas(tanggal_intensitas);
                                intensitasModel.setJenis_opt(opt);
                                intensitasModel.setPeriode_pengamatan(periode);
                                intensitasModel.setDesa(desa);
                                listApprovalKab.add(intensitasModel);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new LaporAdapter(Laporan.this, listApprovalKab);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Laporan.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Laporan.this).add(stringRequest);
    }

    private void getApproval() {
        progressDialog.setTitle("Loading");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"Approval?status='Sudah','Terlambat','Ditolak'&kabupaten="+userAlamat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String tanggal_intensitas = object.getString("tanggal_intensitas");
                                String kecamatan = object.getString("kecamatan");
                                String periode = object.getString("periode_pengamatan");
                                String opt = object.getString("jenis_opt");
                                String id = object.getString("id_intensitas");
                                String desa = object.getString("desa");

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id);
                                intensitasModel.setKecamatan(kecamatan);
                                intensitasModel.setTanggal_intensitas(tanggal_intensitas);
                                intensitasModel.setJenis_opt(opt);
                                intensitasModel.setPeriode_pengamatan(periode);
                                intensitasModel.setDesa(desa);
                                listApproval.add(intensitasModel);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new LaporAdapter(Laporan.this, listApproval);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Laporan.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Laporan.this).add(stringRequest);
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
