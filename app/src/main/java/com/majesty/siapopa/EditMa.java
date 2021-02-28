package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

public class EditMa extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spOPT, spMa;
    MaterialEditText edtJumlah;
    Button btn_save;
    ArrayList<String> jenis_opt = new ArrayList<>();
    ArrayList<String> jenis_ma = new ArrayList<>();
    ArrayAdapter<String> optAdapter;
    ArrayAdapter<String> maAdapter;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    ImageView img_delete;
    String id;
    String opt, ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ma);

        edtJumlah = (MaterialEditText) findViewById(R.id.edtJumlah);
        spOPT = (Spinner) findViewById(R.id.spOPT);
        spMa = (Spinner) findViewById(R.id.spMA);
        btn_save = (Button) findViewById(R.id.save_button);
        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);

        id = getIntent().getStringExtra("id");

        String url = Constants.ROOT_URL+"Pop_MA?id="+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String jumlahMutlak = object.getString("jumlah");
                                opt = object.getString("opt");
                                ma = object.getString("ma");
                                String id_jenisMa = object.getString("id_jenisMa");
                                edtJumlah.setText(jumlahMutlak);

                                String url = Constants.ROOT_URL+"Pop_MA";

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray jsonArray = response.getJSONArray("jenis_ma");
                                            for (int i = 0; i < jsonArray.length(); i++){
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String Opt = jsonObject.optString("opt");
                                                jenis_opt.add(Opt);
                                                optAdapter = new ArrayAdapter<>(EditMa.this, android.R.layout.simple_dropdown_item_1line, jenis_opt);
                                                optAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                                spOPT.setAdapter(optAdapter);
                                                spOPT.setSelection(optAdapter.getPosition(opt));
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
                                spOPT.setOnItemSelectedListener(EditMa.this);
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
        Volley.newRequestQueue(EditMa.this).add(stringRequest);

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
        String url = Constants.ROOT_URL+"Opt";
        final String opt = spOPT.getSelectedItem().toString();
        final String ma = spMa.getSelectedItem().toString();
        final String jumlah = edtJumlah.getText().toString();
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(EditMa.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(EditOPT.this, InputDetailHarian.class);
//                            startActivity(intent);
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
                params.put("opt", opt);
                params.put("ma", ma);
                params.put("jumlah", jumlah);
                params.put("id_jenisMa", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditMa.this);
        requestQueue.add(request);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spOPT){
            jenis_ma.clear();
            String selectedOpt = adapterView.getSelectedItem().toString();
            String url = Constants.ROOT_URL+"Pop_MA?opt="+selectedOpt;
            requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("jenis_ma");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String mA = jsonObject.optString("ma");
                                    jenis_ma.add(mA);
                                    maAdapter = new ArrayAdapter<>(EditMa.this, android.R.layout.simple_dropdown_item_1line, jenis_ma);
                                    maAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                    spMa.setAdapter(maAdapter);
                                    spMa.setSelection(maAdapter.getPosition(ma));
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
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}