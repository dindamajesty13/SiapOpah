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

public class PengeluaranStock extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LinearLayoutManager manager;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    FloatingActionButton fab;
    List<IntensitasModel> listPermintaan;
    String userAlamat;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran_stock);

        progressDialog = new ProgressDialog(this);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Stock");
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.hide();

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(PengeluaranStock.this);
        recyclerView.setLayoutManager(layoutManager);
        listPermintaan = new ArrayList<>();

        getDataPermintaanStock();
    }

    private void getDataPermintaanStock() {
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
                                listPermintaan.add(intensitasModel);


                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new LaporAdapter(PengeluaranStock.this, listPermintaan);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(PengeluaranStock.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(PengeluaranStock.this).add(stringRequest);
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
