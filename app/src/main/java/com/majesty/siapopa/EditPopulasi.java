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

public class EditPopulasi extends AppCompatActivity {
    Spinner spOPT;
    MaterialEditText edtImago, edtNimfa;
    Button btn_save;
    ArrayList<String> jenis_opt = new ArrayList<>();
    ArrayAdapter<String> optAdapter;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String id;
    String opt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_populasi);

        edtImago = (MaterialEditText) findViewById(R.id.edtImago);
        edtNimfa = (MaterialEditText) findViewById(R.id.edtNimfa);
        spOPT = (Spinner) findViewById(R.id.spPopulasi);
        btn_save = (Button) findViewById(R.id.save_button);
        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);

        id = getIntent().getStringExtra("id");

        String url = Constants.ROOT_URL+"Populasi?id_populasi="+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String nimfa = object.getString("nimfa");
                                opt = object.getString("populasi");
                                String imago = object.getString("imago");
                                String id_populasi = object.getString("id_populasi");
                                edtImago.setText(imago);
                                edtNimfa.setText(nimfa);

                                String url = Constants.ROOT_URL+"Populate_OPT?status=populasi";

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray jsonArray = response.getJSONArray("jenis_opt");
                                            for (int i = 0; i < jsonArray.length(); i++){
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String opt = jsonObject.optString("opt");
                                                jenis_opt.add(opt);
                                                optAdapter = new ArrayAdapter<>(EditPopulasi.this, android.R.layout.simple_dropdown_item_1line, jenis_opt);
                                                optAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                                spOPT.setAdapter(optAdapter);
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
                                spOPT.setSelection(optAdapter.getPosition(opt));

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
        Volley.newRequestQueue(EditPopulasi.this).add(stringRequest);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOPT();
            }
        });
    }

    private void updateOPT() {
        progressDialog.setTitle("Insert Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        String url = Constants.ROOT_URL+"Populasi";
        final String opt = spOPT.getSelectedItem().toString();
        final String imago = edtImago.getText().toString();
        final String nimfa = edtNimfa.getText().toString();
        final int jumlah = Integer.parseInt(imago) + Integer.parseInt(nimfa);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(EditPopulasi.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditPopulasi.this, InputDetailHarian.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(EditPopulasi.this, "Data Gagal Diupdate", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("populasi", opt);
                params.put("imago", imago);
                params.put("nimfa", nimfa);
                params.put("jumlah", String.valueOf(jumlah));
                params.put("id_populasi", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditPopulasi.this);
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
