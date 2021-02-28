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
import com.majesty.siapopa.adapter.StockAdapter;
import com.majesty.siapopa.model.StockModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StockProv extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    FloatingActionButton fab;
    List<StockModel> listStock;
    private RecyclerView.Adapter mAdapter;
    String alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_prov);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Stock");
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.hide();

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(StockProv.this);
        recyclerView.setLayoutManager(layoutManager);
        listStock = new ArrayList<>();

        getStock();
    }

    private void getStock() {
        alamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"Stock?provinsi="+alamat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String id = object.getString("id_stock");
                                String tanggal = object.getString("tanggal_stock");
                                String pestisida = object.getString("pestisida");
                                String provinsi = object.getString("provinsi");
                                String kabupaten = object.getString("kabupaten");
                                String sasaran = object.getString("sasaran");
                                String stock_awal = object.getString("stock_awal");
                                String stock_akhir = object.getString("stock_akhir");
                                String nama = object.getString("nama_produk");
                                String tambahan = object.getString("tambahan");
                                String penggunaan = object.getString("penggunaan");
                                String keterangan = object.getString("keterangan");

                                StockModel stock = new StockModel();
                                stock.setPestisida(pestisida);
                                stock.setTanggal_stock(tanggal);
                                stock.setProvinsi(provinsi);
                                stock.setKabupaten(kabupaten);
                                stock.setStock_akhir(stock_akhir);
                                stock.setId_stock(id);
                                listStock.add(stock);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        mAdapter = new StockAdapter(StockProv.this, listStock);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(StockProv.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(StockProv.this).add(stringRequest);
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
