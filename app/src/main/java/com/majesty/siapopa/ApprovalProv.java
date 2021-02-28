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
import com.majesty.siapopa.adapter.ApprovalAdapter;
import com.majesty.siapopa.model.IntensitasModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApprovalProv extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    FloatingActionButton fab;
    List<IntensitasModel> listApprovalProv;
    private RecyclerView.Adapter mAdapter;
    String userAlamat;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_prov);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Approval");
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.hide();
        progressDialog = new ProgressDialog(this);

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ApprovalProv.this);
        recyclerView.setLayoutManager(layoutManager);
        listApprovalProv = new ArrayList<>();

        getApprovalData();
    }

    private void getApprovalData() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"ApprovalProv?approval_kab='Sudah','Sudah (Approve by Provinsi)'&approval_satpel='Sudah','Sudah (Approve by Provinsi)'&provinsi="+userAlamat;
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

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setId_intensitas(id);
                                intensitasModel.setKecamatan(kecamatan);
                                intensitasModel.setDesa(desa);
                                intensitasModel.setJenis_opt(jenis_opt);
                                intensitasModel.setPeriode_pengamatan(periode_pengamatan);
                                intensitasModel.setTanggal_intensitas(date);
                                listApprovalProv.add(intensitasModel);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new ApprovalAdapter(ApprovalProv.this, listApprovalProv);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ApprovalProv.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ApprovalProv.this).add(stringRequest);
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
