package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.majesty.siapopa.adapter.WilayahAdapter;
import com.majesty.siapopa.model.WilayahModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Wilayah extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    FloatingActionButton fab;
    private RecyclerView.Adapter mAdapter;
    List<WilayahModel> listWilayah;
    String userAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wilayah);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Wilayah");
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.hide();

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(Wilayah.this);
        recyclerView.setLayoutManager(layoutManager);
        listWilayah = new ArrayList<>();

        getWilayah();
    }

    private void getWilayah() {
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"Wilayah?lokasi="+userAlamat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String id = object.getString("id_lokasi");
                                String desa = object.getString("desa");
                                String alamatLengkap = object.getString("alamat");

                                WilayahModel wilayahModel = new WilayahModel();
                                wilayahModel.setId_lokasi(id);
                                wilayahModel.setDesa(desa);
                                wilayahModel.setAlamat(alamatLengkap);
                                listWilayah.add(wilayahModel);

                        }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        mAdapter = new WilayahAdapter(Wilayah.this, listWilayah);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Wilayah.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Wilayah.this).add(stringRequest);
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
