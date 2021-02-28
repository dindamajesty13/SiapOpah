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
import com.majesty.siapopa.adapter.ApprovalSatAdapter;
import com.majesty.siapopa.adapter.HasilAdapter;
import com.majesty.siapopa.model.HasilModel;
import com.majesty.siapopa.model.IntensitasModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApprovalKab extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    String userAlamat;
    List<IntensitasModel> listApproval;
    Toolbar toolbar;
    FloatingActionButton fab;
    List<HasilModel> listHasil;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_kab);

        String id = getIntent().getStringExtra("id");

        assert id != null;
        if (id.equals("1")){
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Approval Kabupaten");
            setSupportActionBar(toolbar);
            fab = findViewById(R.id.fab);
            fab.hide();
            progressDialog = new ProgressDialog(this);

            recyclerView = (RecyclerView) findViewById(R.id.rvList);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(ApprovalKab.this);
            recyclerView.setLayoutManager(layoutManager);
            listApproval = new ArrayList<>();

            getApproval();
        } else if (id.equals("2")){
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Approval SatPel");
            setSupportActionBar(toolbar);
            fab = findViewById(R.id.fab);
            fab.hide();
            progressDialog = new ProgressDialog(this);

            recyclerView = (RecyclerView) findViewById(R.id.rvList);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(ApprovalKab.this);
            recyclerView.setLayoutManager(layoutManager);
            listApproval = new ArrayList<>();

            getApprovalSatpel();
        }else if (id.equals("3")){
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Harian Keliling");
            setSupportActionBar(toolbar);

            fab = findViewById(R.id.fab);
            fab.hide();
            progressDialog = new ProgressDialog(this);

            recyclerView = (RecyclerView) findViewById(R.id.rvList);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(ApprovalKab.this);
            recyclerView.setLayoutManager(layoutManager);
            listHasil = new ArrayList<>();

            getLaporan();
        }

    }

    private void getLaporan(){
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String kecamatan = getIntent().getStringExtra("kecamatan");
        String urlLaporan = Constants.ROOT_URL+"Hasil_Pengamatan?kecamatan="+kecamatan;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String date = object.getString("tanggal_pengamatan");
                                String kecamatan = object.getString("kecamatan");
                                String varietas = object.getString("varietas");
                                String desa = object.getString("desa");
                                String id = object.getString("id_hasil");
                                String petugas_pengamatan = object.getString("petugas_pengamatan");

                                HasilModel hasilModel = new HasilModel();
                                hasilModel.setId_hasil(id);
                                hasilModel.setKecamatan(kecamatan);
                                hasilModel.setDesa(desa);
                                hasilModel.setVarietas(varietas);
                                hasilModel.setPetugas_pengamatan(petugas_pengamatan);
                                hasilModel.setTanggal_pengamatan(date);
                                listHasil.add(hasilModel);


                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new HasilAdapter(ApprovalKab.this, listHasil);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ApprovalKab.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ApprovalKab.this).add(stringRequest);
    }

    private void getApprovalSatpel() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String kecamatan = getIntent().getStringExtra("kecamatan");
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"ApprovalProv?provinsi="+userAlamat+"&approval_satpel='Belum','Terlambat','Ditolak'&kecamatan="+kecamatan;
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
                                String periode_pengamatan = object.getString("periode_pengamatan");
                                String satpel = "2";

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id);
                                intensitasModel.setKecamatan(kecamatan);
                                intensitasModel.setDesa(desa);
                                intensitasModel.setJenis_opt(jenis_opt);
                                intensitasModel.setPeriode_pengamatan(periode_pengamatan);
                                intensitasModel.setTanggal_intensitas(date);
                                intensitasModel.setKodeapproval(satpel);
                                listApproval.add(intensitasModel);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new ApprovalSatAdapter(ApprovalKab.this, listApproval);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ApprovalKab.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ApprovalKab.this).add(stringRequest);
    }

    private void getApproval() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String kecamatan = getIntent().getStringExtra("kecamatan");
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"ApprovalProv?provinsi="+userAlamat+"&approval_kab='Belum','Terlambat','Ditolak'&kecamatan="+kecamatan;
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
                                String periode_pengamatan = object.getString("periode_pengamatan");
                                String kab = "1";

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id);
                                intensitasModel.setKecamatan(kecamatan);
                                intensitasModel.setDesa(desa);
                                intensitasModel.setJenis_opt(jenis_opt);
                                intensitasModel.setPeriode_pengamatan(periode_pengamatan);
                                intensitasModel.setTanggal_intensitas(date);
                                intensitasModel.setKodeapproval(kab);
                                listApproval.add(intensitasModel);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new ApprovalSatAdapter(ApprovalKab.this, listApproval);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ApprovalKab.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ApprovalKab.this).add(stringRequest);
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
