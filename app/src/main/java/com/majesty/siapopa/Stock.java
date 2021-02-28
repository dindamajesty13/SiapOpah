package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Stock extends AppCompatActivity {

    MaterialEditText edtSasaranOPT, edtNama, edtStockAwal, edtTambah, edtPenggunaan, edtStockAkhir, edtKeterangan;
    Button inputStock;
    Spinner spPestida;
    ProgressDialog progressDialog;
    ArrayList<String> jenis_pestisida = new ArrayList<>();
    ArrayAdapter<String> pestisidaAdapter;
    ArrayList<String> lokasi = new ArrayList<>();
    ArrayAdapter<String> lokasiAdapter;
    RequestQueue requestQueue;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date, tanggal;
    String id_stock;
    String total, id_ttl_stck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        edtSasaranOPT = (MaterialEditText) findViewById(R.id.edtSasaranOPT);
        edtNama = (MaterialEditText) findViewById(R.id.edtNama);
        edtStockAwal = (MaterialEditText) findViewById(R.id.edtStockAwal);
        edtTambah = (MaterialEditText) findViewById(R.id.edtTambah);
        edtPenggunaan = (MaterialEditText) findViewById(R.id.edtPenggunaan);
//        edtStockAkhir = (MaterialEditText) findViewById(R.id.edtStockAkhir);
        edtKeterangan = (MaterialEditText) findViewById(R.id.edtKeterangan);
        inputStock = (Button) findViewById(R.id.btnInput);
        spPestida = (Spinner) findViewById(R.id.spPestisida);
        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);
        calendar = Calendar.getInstance();

        String url = Constants.ROOT_URL + "Populate_Pestisida";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("jenis_pestisida");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String tanaman = jsonObject.optString("pestisida");
                        jenis_pestisida.add(tanaman);
                        pestisidaAdapter = new ArrayAdapter<>(Stock.this, android.R.layout.simple_dropdown_item_1line, jenis_pestisida);
                        pestisidaAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spPestida.setAdapter(pestisidaAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);

        id_stock = getIntent().getStringExtra("id_stock");
        if (id_stock != null){
            getDataStock();
        }

        inputStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_stock = getIntent().getStringExtra("id_stock");
                if (id_stock != null){
                    updateData();
                }else {
                    if (TextUtils.isEmpty(edtKeterangan.getText().toString()) || TextUtils.isEmpty(edtNama.getText().toString()) || TextUtils.isEmpty(edtSasaranOPT.getText().toString())){
                        edtKeterangan.setError("Tidak Boleh Kosong");
                        edtNama.setError("Tidak Boleh Kosong");
                        edtSasaranOPT.setError("Tidak Boleh Kosong");
                    }else {
                        insertData();
                    }
                }

            }
        });
    }

    private void getDataStock() {
        String id_stock = getIntent().getStringExtra("id_stock");
        String url = Constants.ROOT_URL + "Stock?id="+id_stock;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

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

                                spPestida.setSelection(pestisidaAdapter.getPosition(pestisida));
                                edtPenggunaan.setText(penggunaan);
                                edtStockAwal.setText(stock_awal);
                                edtTambah.setText(tambahan);
                                edtNama.setText(nama);
                                edtKeterangan.setText(keterangan);
                                edtSasaranOPT.setText(sasaran);

//                                updateData();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Stock.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Stock.this).add(stringRequest);
    }

    private void updateData() {
        progressDialog.setTitle("Update Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        String url = Constants.ROOT_URL + "Stock";
        final String sasaran = edtSasaranOPT.getText().toString();
        final String nama = edtNama.getText().toString();
        final String stock_awal = edtStockAwal.getText().toString();
        final String tambahan = edtTambah.getText().toString();
        final String penggunaan = edtPenggunaan.getText().toString();
        final String keterangan = edtKeterangan.getText().toString();
        final String pestisida = spPestida.getSelectedItem().toString();
        final String kabupaten = Common.currentUser.getKabupaten();
        final String id_stock = getIntent().getStringExtra("id_stock");
        final int stock_akhir = Integer.parseInt(stock_awal)+Integer.parseInt(tambahan)-Integer.parseInt(penggunaan);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")) {
                            getDataStockSum();
//                            progressDialog.dismiss();
//                            Toast.makeText(Stock.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
//                            Intent home = new Intent(Stock.this, Home.class);
//                            home.putExtra("HOME", "brigade");
//                            startActivity(home);
//                            finish();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(Stock.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_stock", id_stock);
                params.put("pestisida", pestisida);
                params.put("kabupaten", kabupaten);
                params.put("nama_produk", nama);
                params.put("sasaran", sasaran);
                params.put("stock_awal", stock_awal);
                params.put("stock_akhir", String.valueOf(stock_akhir));
                params.put("tambahan", tambahan);
                params.put("penggunaan", penggunaan);
                params.put("keterangan", keterangan);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Stock.this);
        requestQueue.add(request);
    }

    private void insertData() {
        progressDialog.setTitle("Insert Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        String url = Constants.ROOT_URL + "Stock";
        final String username = Common.currentUser.getUsername();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        final String sasaran = edtSasaranOPT.getText().toString();
        final String nama = edtNama.getText().toString();
        final String stock_awal = edtStockAwal.getText().toString();
        final String tambahan = edtTambah.getText().toString();
        final String penggunaan = edtPenggunaan.getText().toString();
        final String keterangan = edtKeterangan.getText().toString();
        final String pestisida = spPestida.getSelectedItem().toString();
        final String kabupaten = Common.currentUser.getKabupaten();
        final String provinsi = Common.currentUser.getAlamat();
        tanggal = date;
        final int stock_akhir = Integer.parseInt(stock_awal)+Integer.parseInt(tambahan)-Integer.parseInt(penggunaan);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")) {
                            getDataStockSum();
//                            insertDataTotal();
//                            progressDialog.dismiss();
//                            Toast.makeText(Stock.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
//                            Intent home = new Intent(Stock.this, Home.class);
//                            home.putExtra("HOME", "brigade");
//                            startActivity(home);
//                            finish();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(Stock.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("pestisida", pestisida);
                params.put("provinsi", provinsi);
                params.put("kabupaten", kabupaten);
                params.put("nama_produk", nama);
                params.put("sasaran", sasaran);
                params.put("tanggal_stock", tanggal);
                params.put("stock_awal", stock_awal);
                params.put("stock_akhir", String.valueOf(stock_akhir));
                params.put("tambahan", tambahan);
                params.put("penggunaan", penggunaan);
                params.put("keterangan", keterangan);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Stock.this);
        requestQueue.add(request);
    }

    private void getDataStockSum() {
        String pestisida = spPestida.getSelectedItem().toString().trim();
        String kabupaten = Common.currentUser.getKabupaten();
        dateFormat = new SimpleDateFormat("yyyy-MM");
        String date = dateFormat.format(calendar.getTime());
        String url = Constants.ROOT_URL + "Stock?pestisida="+pestisida+"&kabupaten="+kabupaten+"&date="+date;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                total = object.getString("total_stock");

                                checkDataIfExist();

//                                updateData();
//                                updateDataTotal();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Stock.this, error.toString(), Toast.LENGTH_LONG).show();
//                insertDataTotal();
            }
        });
        Volley.newRequestQueue(Stock.this).add(stringRequest);
    }

    private void checkDataIfExist() {
        String pestisida = spPestida.getSelectedItem().toString().trim();
        String kabupaten = Common.currentUser.getKabupaten();
        dateFormat = new SimpleDateFormat("yyyy-MM");
        String date = dateFormat.format(calendar.getTime());
        String url = Constants.ROOT_URL + "Total_Stock?pestisida="+pestisida+"&date="+date+"&kabupaten="+kabupaten;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                id_ttl_stck = object.getString("id_ttl_stck");

//                                checkDataIfExist();

//                                updateData();
                                updateDataTotal();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Stock.this, error.toString(), Toast.LENGTH_LONG).show();
                insertDataTotal();
            }
        });
        Volley.newRequestQueue(Stock.this).add(stringRequest);
    }

    private void updateDataTotal() {
        String url = Constants.ROOT_URL + "Total_Stock";
        final String username = Common.currentUser.getUsername();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        final String pestisida = spPestida.getSelectedItem().toString();
        final String kabupaten = Common.currentUser.getKabupaten();
        tanggal = date;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")) {
//                            insertDataTotal();
                            progressDialog.dismiss();
                            Toast.makeText(Stock.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            Intent home = new Intent(Stock.this, Home.class);
                            home.putExtra("HOME", "brigade");
                            startActivity(home);
                            finish();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(Stock.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jenis_pestisida", pestisida);
                params.put("lokasi_stock", kabupaten);
                params.put("total", total);
                params.put("tgl_stock", tanggal);
                params.put("id_ttl_stck", id_ttl_stck);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Stock.this);
        requestQueue.add(request);
    }

    private void insertDataTotal() {
        String url = Constants.ROOT_URL + "Total_Stock";
        final String username = Common.currentUser.getUsername();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        final String sasaran = edtSasaranOPT.getText().toString();
        final String nama = edtNama.getText().toString();
        final String stock_awal = edtStockAwal.getText().toString();
        final String tambahan = edtTambah.getText().toString();
        final String penggunaan = edtPenggunaan.getText().toString();
        final String keterangan = edtKeterangan.getText().toString();
        final String pestisida = spPestida.getSelectedItem().toString();
        final String kabupaten = Common.currentUser.getKabupaten();
        final String provinsi = Common.currentUser.getAlamat();
        tanggal = date;
        final int stock_akhir = Integer.parseInt(stock_awal)+Integer.parseInt(tambahan)-Integer.parseInt(penggunaan);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")) {
//                            insertDataTotal();
                            progressDialog.dismiss();
                            Toast.makeText(Stock.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            Intent home = new Intent(Stock.this, Home.class);
                            home.putExtra("HOME", "brigade");
                            startActivity(home);
                            finish();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(Stock.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jenis_pestisida", pestisida);
                params.put("lokasi_stock", kabupaten);
                params.put("total", total);
                params.put("tgl_stock", tanggal);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Stock.this);
        requestQueue.add(request);
    }

}
