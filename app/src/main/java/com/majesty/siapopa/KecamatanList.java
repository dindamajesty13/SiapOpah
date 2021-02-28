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
import com.majesty.siapopa.adapter.KecamatanAdapter;
import com.majesty.siapopa.model.KecamatanModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KecamatanList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    Spinner spOPT;
    FloatingActionButton fab;
    private RecyclerView.Adapter mAdapter;
    List<KecamatanModel> listHasil;
    String userAlamat;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kecamatan_list);

//        toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("Harian Keliling");
//        setSupportActionBar(toolbar);

//        fab = findViewById(R.id.fab);
//        fab.hide();

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(KecamatanList.this);
        recyclerView.setLayoutManager(layoutManager);
        listHasil = new ArrayList<>();
        progressDialog = new ProgressDialog(this);

        getKecamatan();
    }

    private void getKecamatan() {
        progressDialog.setTitle("Loading");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String kabupaten = getIntent().getStringExtra("kabupaten");
        String urlLaporan = Constants.ROOT_URL+"Kecamatan?provinsi="+userAlamat+"&kabupaten="+kabupaten;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String kecamatan = object.getString("kecamatan");
                                String id = getIntent().getStringExtra("id");

                                KecamatanModel kecamatanModel = new KecamatanModel();
                                kecamatanModel.setKecamatan(kecamatan);
                                kecamatanModel.setId(id);
                                listHasil.add(kecamatanModel);


                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new KecamatanAdapter(KecamatanList.this, listHasil);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(KecamatanList.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(KecamatanList.this).add(stringRequest);
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
