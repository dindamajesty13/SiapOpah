package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditStock extends AppCompatActivity {
    Spinner spKabupaten;
    MaterialEditText edtJumlah;
    Button btn_save;
    ArrayList<String> lokasi = new ArrayList<>();
    ArrayAdapter<String> lokasiAdapter;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stock);

        edtJumlah = (MaterialEditText) findViewById(R.id.edtJumlah);
        spKabupaten = (Spinner) findViewById(R.id.spKabupaten);
        btn_save = (Button) findViewById(R.id.save_button);
        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);

        id = getIntent().getStringExtra("id_wilayah");

        String url = Constants.ROOT_URL+"Insert_Stock?id_wilayah="+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String jumlahMutlak = object.getString("jumlah_pestisida");
                                String kabupaten = object.getString("kabupaten");
                                edtJumlah.setText(jumlahMutlak);

                                String url = Constants.ROOT_URL + "Populate_Kabupaten";

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray jsonArray = response.getJSONArray("lokasi");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String opt = jsonObject.optString("kabupaten");
                                                lokasi.add(opt);
                                                lokasiAdapter = new ArrayAdapter<>(EditStock.this, android.R.layout.simple_dropdown_item_1line, lokasi);
                                                lokasiAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                                spKabupaten.setAdapter(lokasiAdapter);
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
                                spKabupaten.setSelection(lokasiAdapter.getPosition(kabupaten));

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(EditStock.this).add(stringRequest);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStock();
            }
        });


    }

    private void updateStock() {
        progressDialog.setTitle("Insert Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        String url = Constants.ROOT_URL+"Insert_Stock";
        final String kabupaten = spKabupaten.getSelectedItem().toString();
        final String jumlah = edtJumlah.getText().toString();
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(EditStock.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditStock.this, Stock.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(InputDetailHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kabupaten", kabupaten);
                params.put("jumlah_pestisida", jumlah);
                params.put("id_wilayah", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditStock.this);
        requestQueue.add(request);
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
