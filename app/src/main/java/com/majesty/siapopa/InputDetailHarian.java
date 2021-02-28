package com.majesty.siapopa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.majesty.siapopa.adapter.OptAdapter;
import com.majesty.siapopa.model.DetailModel;
import com.majesty.siapopa.model.ModelOpt;
import com.majesty.siapopa.model.SessionManager;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class  InputDetailHarian extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    MaterialEditText edtJumlahAnakan, edtImago, edtNimfa, edtOPMS, edtDomi, edtKisar, edtLuasSpot, edtJumlah, edtJumlahMutlak;
    Button btn_save;
    Spinner spOPT, spOPTTdkMutlak, spPopulasi, spMA, spPopulasiM2;
    ImageView btn_add, btn_tambah, btn_Populasi, btn_ma, btn_populasi2;
    Dialog dialog;
    TextView txtDesa;
    RecyclerView.LayoutManager layoutManager1, layoutManager2, layoutManager3, layoutManager4, layoutManager5;
//    private RecyclerView.Adapter adapterMA;
    private OptAdapter adapterMutlak, adapterTdkMutlak, adapterPopulasi, adapterPopulasiM2, adapterMA;
    RecyclerView rvMutlak, rvTdkMutlak, rvPopulasi, rvMA, rvPopulasi2;
    ProgressDialog progressDialog;
    String id_detail;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_LOCATION_PERMISSION_CODE = 99;
    ImageView img_bukti;
    String encodedImage, base_url_bukti;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    FusedLocationProviderClient fusedLocationProviderClient;
    String kecamatan, latitude, longitude;
    Button mSaveButton;
    Button mCancelButton;
    List<ModelOpt> listOPTMutlak, listOPTTdkMutlak, listPopulasi, listMA, listPopulasiM2;
    String optMutlak, optTdkMutlak, jumlahMutlak, jumlahTdkMutlak, id_jenisOPT, id_jenisOpt, id_populasi, jumlahPopulasi, optPopulasi, jumlahMA, optMA, id_ma, jumlahPopM2, populasiM2, id_populasiM2;
    ArrayList<String> jenis_opt = new ArrayList<>();
    ArrayAdapter<String> optAdapter;
    ArrayList<String> jenis_opt_mutlak = new ArrayList<>();
    ArrayAdapter<String> optAdapterMutlak;
    ArrayList<String> jenis_populasi = new ArrayList<>();
    ArrayAdapter<String> populasiAdapter;
    ArrayList<String> jenis_populasi_m2 = new ArrayList<>();
    ArrayAdapter<String> populasiAdapterM2;
    ArrayList<String> jenis_MA = new ArrayList<>();
    ArrayAdapter<String> maAdapter;
    RequestQueue requestQueue;
    String tanggal, id_hasil;
    String data1, data2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_detail_harian);

        edtJumlahAnakan = (MaterialEditText) findViewById(R.id.edtJumlahAnakan);
        edtLuasSpot = (MaterialEditText) findViewById(R.id.edtLuasSpot);
        btn_save = (Button) findViewById(R.id.save_button);
        btn_add = (ImageView) findViewById(R.id.btn_add);
        btn_tambah = (ImageView) findViewById(R.id.btn_tambah);
        btn_Populasi = (ImageView) findViewById(R.id.btn_populasi);
        btn_ma = (ImageView) findViewById(R.id.btn_ma);
        btn_populasi2 = (ImageView) findViewById(R.id.btn_populasi2);
        img_bukti = (ImageView) findViewById(R.id.buktiFoto);
        rvMutlak = (RecyclerView) findViewById(R.id.rvMutlak);
        rvTdkMutlak = (RecyclerView) findViewById(R.id.rvTdkmutlak);
        rvPopulasi = (RecyclerView) findViewById(R.id.rvPopulasi);
        rvMA = (RecyclerView) findViewById(R.id.rvMA);
        rvPopulasi2 = (RecyclerView) findViewById(R.id.rvPopulasi2);
        txtDesa = findViewById(R.id.txtDesa);
        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);
        calendar = Calendar.getInstance();

        txtDesa.setText("Rumpun: " + getIntent().getStringExtra("jumlah"));


        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        tanggal = date;

        SessionManager sessionManager = new SessionManager(InputDetailHarian.this, SessionManager.SESSION_ID);
        HashMap<String, String> rememberMeDetails = sessionManager.getIdHasilDetailsFromSession();
        id_hasil = rememberMeDetails.get(SessionManager.KEY_IDHASIL);

        String id = getIntent().getStringExtra("id_detail");
        if (id != null){
            String url = Constants.ROOT_URL+"Hasil_Pengamatan?id_dtl="+id_detail+"&id_hsl="+id_hasil;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i<array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);

                                    data1 = object.getString("jumlah");
                                    if (data1.equals("null")){
                                        data1 = "0";
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    data1 = "0";
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
        }else {
            String url = Constants.ROOT_URL + "Hasil_Pengamatan?id_dtl=0&id_hsl=" + id_hasil;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    data1 = object.getString("jumlah");
                                    if (data1.equals("null")) {
                                        data1 = "0";
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    data1 = "0";
//                    getDataOPTTdkMutlak();
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
        }

        rvMutlak.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(InputDetailHarian.this);
        rvMutlak.setLayoutManager(layoutManager1);
        listOPTMutlak = new ArrayList<>();
//        DetailAdapter detailAdapter = new DetailAdapter(this, listOPTMutlak)

        getDataMutlak();

        rvTdkMutlak.setHasFixedSize(true);
        layoutManager2 = new LinearLayoutManager(InputDetailHarian.this);
        rvTdkMutlak.setLayoutManager(layoutManager2);
        listOPTTdkMutlak = new ArrayList<>();

        getDataTdkMutlak();

        rvPopulasi.setHasFixedSize(true);
        layoutManager3 = new LinearLayoutManager(InputDetailHarian.this);
        rvPopulasi.setLayoutManager(layoutManager3);
        listPopulasi = new ArrayList<>();

        getDataPopulasi();

        rvMA.setHasFixedSize(true);
        layoutManager4 = new LinearLayoutManager(InputDetailHarian.this);
        rvMA.setLayoutManager(layoutManager4);
        listMA = new ArrayList<>();

        getDataPopulasiMA();

        rvPopulasi2.setHasFixedSize(true);
        layoutManager5 = new LinearLayoutManager(InputDetailHarian.this);
        rvPopulasi2.setLayoutManager(layoutManager5);
        listPopulasiM2 = new ArrayList<>();

        getDataPopulasiM2();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogMutlak();
            }
        });

        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogTdkMutlak();
            }
        });

        btn_Populasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogPopulasi();
            }
        });

        btn_ma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogPopulasiMA();
            }
        });

        btn_populasi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogPopulasiM2();
            }
        });

        id_detail = getIntent().getStringExtra("id_detail");
        if (id_detail != null) {
            getDataDetail();
        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_detail != null) {
                    getDataOPTtoInsert();
                } else {
                    if (edtJumlahAnakan.getText().toString().equals("0") || edtJumlahAnakan.getText().toString().isEmpty() || edtJumlahAnakan.getText().toString().equals("") || TextUtils.isEmpty(edtJumlahAnakan.getText().toString())) {
                        edtJumlahAnakan.setError("Tidak Boleh Kosong");
                        Toast.makeText(InputDetailHarian.this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                    } else {
                        getDataOPTtoInsert();
                    }
                }
            }
        });
    }

    private void getDataPopulasiM2() {
        String username = Common.currentUser.getUsername();
        id_detail = getIntent().getStringExtra("id_detail");
        String url = Constants.ROOT_URL + "Opt?username=" + username + "&status=populasi(m2)&id_detail=" + id_detail+"&id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                String opt = object.getString("opt");
                                String id = object.getString("id_jenisOPT");

                                ModelOpt modelOpt = new ModelOpt();
                                modelOpt.setId_jenisOPT(id);
                                modelOpt.setOpt(opt);
                                listPopulasiM2.add(modelOpt);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        adapterPopulasiM2 = new OptAdapter(InputDetailHarian.this, listPopulasiM2);
                        rvPopulasi2.setAdapter(adapterPopulasiM2);
                        adapterPopulasiM2.setOnItemClickCallback(new OptAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(final ModelOpt modelOpt) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InputDetailHarian.this);
                                builder.setTitle("Delete Data");
                                builder.setMessage("Apakah Anda Yakin?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.setTitle("Delete Data");
                                        progressDialog.setMessage("Mohon Tunggu Sebentar");
                                        progressDialog.show();
                                        final String id_jenisOPT = modelOpt.getId_jenisOPT();
                                        String url = Constants.ROOT_URL+"Opt";
                                        StringRequest request = new StringRequest(Request.Method.POST, url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (response.contains("berhasil")){
                                                            progressDialog.dismiss();
                                                            finish();
                                                            overridePendingTransition(0, 0);
                                                            startActivity(getIntent());
                                                            overridePendingTransition(0, 0);
                                                            Toast.makeText(InputDetailHarian.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                    }
                                                })
                                        {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("id_jenisOPT", id_jenisOPT);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                                        requestQueue.add(request);

                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override public void onClick(DialogInterface dialog, int which) {
                                        // TODO: Do something, when user click on the positive button
                                        dialog.cancel();
                                    }
                                });
                                builder.create().show();

                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
    }

    private void openDialogPopulasiM2() {
        dialog = new Dialog(InputDetailHarian.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_populasi_meter);
        dialog.setCancelable(false);

        edtJumlah = (MaterialEditText) dialog.findViewById(R.id.edtJumlah);
        spPopulasiM2 = (Spinner) dialog.findViewById(R.id.spPopulasiM2);
        mSaveButton = (Button) dialog.findViewById(R.id.save_button);
        mCancelButton = (Button) dialog.findViewById(R.id.cancel_button);

        String url = Constants.ROOT_URL + "Populate_OPT?status=populasi(m2)";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("jenis_opt");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String opt = jsonObject.optString("opt");
                        jenis_populasi_m2.add(opt);
                        populasiAdapterM2 = new ArrayAdapter<>(InputDetailHarian.this, android.R.layout.simple_dropdown_item_1line, jenis_populasi_m2);
                        populasiAdapterM2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spPopulasiM2.setAdapter(populasiAdapterM2);
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

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Insert Data");
                progressDialog.setMessage("Mohon Tunggu Sebentar");
                progressDialog.show();
                String url = Constants.ROOT_URL + "Opt";
                final String populasi = spPopulasiM2.getSelectedItem().toString();
                final String jumlah = edtJumlah.getText().toString();
                final String username = Common.currentUser.getUsername();
                final String id_detail = getIntent().getStringExtra("id_detail");
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = dateFormat.format(calendar.getTime());
                final String tanggal = date;

                if (id_detail != null) {
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("berhasil")) {
                                        dialog.dismiss();
                                        progressDialog.dismiss();
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                        Toast.makeText(InputDetailHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(InputDetailHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("opt", populasi);
                            params.put("jumlah", jumlah);
                            params.put("username", username);
                            params.put("id_detail", id_detail);
                            params.put("id_hasil", id_hasil);
                            params.put("tanggal_insert", tanggal);
                            params.put("mutlak", "populasi(m2)");
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                    requestQueue.add(request);
                } else {
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("berhasil")) {
                                        dialog.dismiss();
                                        progressDialog.dismiss();
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                        Toast.makeText(InputDetailHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(InputDetailHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("opt", populasi);
                            params.put("jumlah", jumlah);
                            params.put("username", username);
                            params.put("id_detail", "0");
                            params.put("id_hasil", id_hasil);
                            params.put("tanggal_insert", tanggal);
                            params.put("mutlak", "populasi(m2)");
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                    requestQueue.add(request);
                }


            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                dialog.dismiss();
                dialog.cancel();
                populasiAdapterM2.clear();
            }
        });
        dialog.show();
    }

    private void getDataPopulasiMA() {
        String username = Common.currentUser.getUsername();
        id_detail = getIntent().getStringExtra("id_detail");
        String url = Constants.ROOT_URL + "Opt?username=" + username + "&status=MA&id_detail=" + id_detail+"&id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                String opt = object.getString("opt");
                                String id = object.getString("id_jenisOPT");

                                ModelOpt modelOpt = new ModelOpt();
                                modelOpt.setId_jenisOPT(id);
                                modelOpt.setOpt(opt);
                                listMA.add(modelOpt);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        adapterMA = new OptAdapter(InputDetailHarian.this, listMA);
                        rvMA.setAdapter(adapterMA);
                        adapterMA.setOnItemClickCallback(new OptAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(final ModelOpt modelOpt) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InputDetailHarian.this);
                                builder.setTitle("Delete Data");
                                builder.setMessage("Apakah Anda Yakin?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.setTitle("Delete Data");
                                        progressDialog.setMessage("Mohon Tunggu Sebentar");
                                        progressDialog.show();
                                        final String id_jenisOPT = modelOpt.getId_jenisOPT();
                                        String url = Constants.ROOT_URL+"Opt";
                                        StringRequest request = new StringRequest(Request.Method.POST, url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (response.contains("berhasil")){
                                                            progressDialog.dismiss();
                                                            finish();
                                                            overridePendingTransition(0, 0);
                                                            startActivity(getIntent());
                                                            overridePendingTransition(0, 0);
                                                            Toast.makeText(InputDetailHarian.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                    }
                                                })
                                        {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("id_jenisOPT", id_jenisOPT);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                                        requestQueue.add(request);

                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override public void onClick(DialogInterface dialog, int which) {
                                        // TODO: Do something, when user click on the positive button
                                        dialog.cancel();
                                    }
                                });
                                builder.create().show();

                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
    }

//    private void takePhoto() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//            {
//                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
//            }
//            else
//            {
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
//            }
//        }
//    }

    private void getDataPopulasi() {
        String username = Common.currentUser.getUsername();
        id_detail = getIntent().getStringExtra("id_detail");
        String url = Constants.ROOT_URL + "Opt?username=" + username + "&status=populasi&id_detail=" + id_detail+"&id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                String opt = object.getString("opt");
                                String id = object.getString("id_jenisOPT");

                                ModelOpt modelOpt = new ModelOpt();
                                modelOpt.setId_jenisOPT(id);
                                modelOpt.setOpt(opt);
                                listPopulasi.add(modelOpt);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        adapterPopulasi = new OptAdapter(InputDetailHarian.this, listPopulasi);
                        rvPopulasi.setAdapter(adapterPopulasi);
                        adapterPopulasi.setOnItemClickCallback(new OptAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(final ModelOpt modelOpt) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InputDetailHarian.this);
                                builder.setTitle("Delete Data");
                                builder.setMessage("Apakah Anda Yakin?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.setTitle("Delete Data");
                                        progressDialog.setMessage("Mohon Tunggu Sebentar");
                                        progressDialog.show();
                                        final String id_jenisOPT = modelOpt.getId_jenisOPT();
                                        String url = Constants.ROOT_URL+"Opt";
                                        StringRequest request = new StringRequest(Request.Method.POST, url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (response.contains("berhasil")){
                                                            progressDialog.dismiss();
                                                            finish();
                                                            overridePendingTransition(0, 0);
                                                            startActivity(getIntent());
                                                            overridePendingTransition(0, 0);
                                                            Toast.makeText(InputDetailHarian.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                    }
                                                })
                                        {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("id_jenisOPT", id_jenisOPT);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                                        requestQueue.add(request);

                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override public void onClick(DialogInterface dialog, int which) {
                                        // TODO: Do something, when user click on the positive button
                                        dialog.cancel();
                                    }
                                });
                                builder.create().show();

                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
    }

    private void openDialogPopulasi() {
        dialog = new Dialog(InputDetailHarian.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_populasi);
        dialog.setCancelable(false);

        edtJumlah = (MaterialEditText) dialog.findViewById(R.id.edtJumlah);
        spPopulasi = (Spinner) dialog.findViewById(R.id.spPopulasi);
        mSaveButton = (Button) dialog.findViewById(R.id.save_button);
        mCancelButton = (Button) dialog.findViewById(R.id.cancel_button);

        String url = Constants.ROOT_URL + "Populate_OPT?status=populasi";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("jenis_opt");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String opt = jsonObject.optString("opt");
                        jenis_populasi.add(opt);
                        populasiAdapter = new ArrayAdapter<>(InputDetailHarian.this, android.R.layout.simple_dropdown_item_1line, jenis_populasi);
                        populasiAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spPopulasi.setAdapter(populasiAdapter);
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

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Insert Data");
                progressDialog.setMessage("Mohon Tunggu Sebentar");
                progressDialog.show();
                String url = Constants.ROOT_URL + "Opt";
                final String populasi = spPopulasi.getSelectedItem().toString();
                final String jumlah = edtJumlah.getText().toString();
                final String username = Common.currentUser.getUsername();
                final String id_detail = getIntent().getStringExtra("id_detail");
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = dateFormat.format(calendar.getTime());
                final String tanggal = date;

                if (id_detail != null) {
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("berhasil")) {
                                        dialog.dismiss();
                                        progressDialog.dismiss();
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                        Toast.makeText(InputDetailHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(InputDetailHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("opt", populasi);
                            params.put("jumlah", jumlah);
                            params.put("username", username);
                            params.put("id_detail", id_detail);
                            params.put("id_hasil", id_hasil);
                            params.put("tanggal_insert", tanggal);
                            params.put("mutlak", "populasi");
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                    requestQueue.add(request);
                } else {
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("berhasil")) {
                                        dialog.dismiss();
                                        progressDialog.dismiss();
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                        Toast.makeText(InputDetailHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(InputDetailHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("opt", populasi);
                            params.put("jumlah", jumlah);
                            params.put("username", username);
                            params.put("id_detail", "0");
                            params.put("id_hasil", id_hasil);
                            params.put("tanggal_insert", tanggal);
                            params.put("mutlak", "populasi");
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                    requestQueue.add(request);
                }


            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                dialog.dismiss();
                dialog.cancel();
                populasiAdapter.clear();
            }
        });
        dialog.show();
    }

    private void openDialogPopulasiMA() {
        dialog = new Dialog(InputDetailHarian.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.input_ma);
        dialog.setCancelable(false);

        spMA = (Spinner) dialog.findViewById(R.id.spMA);
        edtJumlah = (MaterialEditText) dialog.findViewById(R.id.edtJumlah);
        mSaveButton = (Button) dialog.findViewById(R.id.save_button);
        mCancelButton = (Button) dialog.findViewById(R.id.cancel_button);

        String url = Constants.ROOT_URL + "Populate_OPT?status=MA";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("jenis_opt");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String opt = jsonObject.optString("opt");
                        jenis_MA.add(opt);
                        maAdapter = new ArrayAdapter<>(InputDetailHarian.this, android.R.layout.simple_dropdown_item_1line, jenis_MA);
                        maAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spMA.setAdapter(maAdapter);
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

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Insert Data");
                progressDialog.setMessage("Mohon Tunggu Sebentar");
                progressDialog.show();
                String url = Constants.ROOT_URL + "Opt";
                final String ma = spMA.getSelectedItem().toString();
//                final String opt = spOPT.getSelectedItem().toString();
                final String jumlah = edtJumlah.getText().toString();
                final String username = Common.currentUser.getUsername();
                final String id_detail = getIntent().getStringExtra("id_detail");
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = dateFormat.format(calendar.getTime());
                final String tanggal = date;
                if (id_detail != null) {
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("berhasil")) {
                                        dialog.dismiss();
                                        progressDialog.dismiss();
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                        Toast.makeText(InputDetailHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(InputDetailHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("opt", ma);
                            params.put("jumlah", jumlah);
                            params.put("username", username);
                            params.put("id_detail", id_detail);
                            params.put("id_hasil", id_hasil);
                            params.put("tanggal_insert", tanggal);
                            params.put("mutlak", "MA");
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                    requestQueue.add(request);
                } else {
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("berhasil")) {
                                        dialog.dismiss();
                                        progressDialog.dismiss();
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                        Toast.makeText(InputDetailHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(InputDetailHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("opt", ma);
                            params.put("jumlah", jumlah);
                            params.put("username", username);
                            params.put("id_detail", "0");
                            params.put("id_hasil", id_hasil);
                            params.put("tanggal_insert", tanggal);
                            params.put("mutlak", "MA");
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                    requestQueue.add(request);
                }


            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                dialog.dismiss();
                dialog.cancel();
                maAdapter.clear();
            }
        });
        dialog.show();
    }

    private void checkGPS() {
        LocationManager lm = (LocationManager) InputDetailHarian.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(InputDetailHarian.this)
                    .setMessage("GPS tidak Aktif")
                    .setPositiveButton("Aktifkan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(InputDetailHarian.this);

            if (ActivityCompat.checkSelfPermission(InputDetailHarian.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            try {
                                Geocoder geocoder = new Geocoder(InputDetailHarian.this, Locale.getDefault());

                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                kecamatan = String.valueOf(addresses.get(0).getAddressLine(0));
                                if (kecamatan.contains(Common.currentUser.getAlamat())) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                                        } else {
                                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                        }
                                    }
                                } else {
                                    Toast.makeText(InputDetailHarian.this, kecamatan, Toast.LENGTH_SHORT).show();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(InputDetailHarian.this);
                                    builder.setTitle("Lokasi Diluar Wilayah Kerja");
                                    builder.setMessage("System mendeteksi lokasi perangkat anda diluar wilayah kerja");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO: Do something, when user click on the positive button
                                            dialog.cancel();
                                        }
                                    });
                                    builder.create().show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            } else {
                ActivityCompat.requestPermissions(InputDetailHarian.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }
    }

    private void openDialogTdkMutlak() {
        dialog = new Dialog(InputDetailHarian.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.input_opt);
        dialog.setCancelable(false);

        edtJumlah = (MaterialEditText) dialog.findViewById(R.id.edtJumlahTdkMutlak);
        spOPTTdkMutlak = (Spinner) dialog.findViewById(R.id.spOPTTdkMutlak);
        mSaveButton = (Button) dialog.findViewById(R.id.save_button);
        mCancelButton = (Button) dialog.findViewById(R.id.cancel_button);

        String url = Constants.ROOT_URL + "Populate_OPT?status=tidak mutlak";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("jenis_opt");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String opt = jsonObject.optString("opt");
                        jenis_opt.add(opt);
                        optAdapter = new ArrayAdapter<>(InputDetailHarian.this, android.R.layout.simple_dropdown_item_1line, jenis_opt);
                        optAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spOPTTdkMutlak.setAdapter(optAdapter);
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

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtJumlah.getText().toString().equals("1") || edtJumlah.getText().toString().equals("3") || edtJumlah.getText().toString().equals("5") || edtJumlah.getText().toString().equals("7") || edtJumlah.getText().toString().equals("9")){
                    progressDialog.setTitle("Insert Data");
                    progressDialog.setMessage("Mohon Tunggu Sebentar");
                    progressDialog.show();
                    String url = Constants.ROOT_URL + "Opt";
                    final String opt = spOPTTdkMutlak.getSelectedItem().toString();
                    final String jumlah = edtJumlah.getText().toString();
                    final String username = Common.currentUser.getUsername();
                    final String id_detail = getIntent().getStringExtra("id_detail");
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date = dateFormat.format(calendar.getTime());
                    final String tanggal = date;
                    if (id_detail != null) {
                        StringRequest request = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.contains("berhasil")) {
                                            dialog.dismiss();
                                            progressDialog.dismiss();
                                            finish();
                                            overridePendingTransition(0, 0);
                                            startActivity(getIntent());
                                            overridePendingTransition(0, 0);
                                            Toast.makeText(InputDetailHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(InputDetailHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("opt", opt);
                                params.put("jumlah", jumlah);
                                params.put("username", username);
                                params.put("id_detail", id_detail);
                                params.put("id_hasil", id_hasil);
                                params.put("tanggal_insert", tanggal);
                                params.put("mutlak", "tidak mutlak");
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                        requestQueue.add(request);
                    } else {
                        StringRequest request = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.contains("berhasil")) {
                                            dialog.dismiss();
                                            progressDialog.dismiss();
                                            finish();
                                            overridePendingTransition(0, 0);
                                            startActivity(getIntent());
                                            overridePendingTransition(0, 0);
                                            Toast.makeText(InputDetailHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(InputDetailHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("opt", opt);
                                params.put("jumlah", jumlah);
                                params.put("username", username);
                                params.put("id_detail", "0");
                                params.put("id_hasil", id_hasil);
                                params.put("tanggal_insert", tanggal);
                                params.put("mutlak", "tidak mutlak");
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                        requestQueue.add(request);
                    }
                }else {
                    edtJumlah.setError("Mohon Inputkan Skala");
                }



            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                dialog.dismiss();
                dialog.cancel();
                optAdapter.clear();
            }
        });
        dialog.show();
    }

    private void getDataTdkMutlak() {
        String username = Common.currentUser.getUsername();
        id_detail = getIntent().getStringExtra("id_detail");
        String url = Constants.ROOT_URL + "Opt?username=" + username + "&status=tidak mutlak&id_detail=" + id_detail+"&id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                String opt = object.getString("opt");
                                String id = object.getString("id_jenisOPT");

                                ModelOpt modelOpt = new ModelOpt();
                                modelOpt.setId_jenisOPT(id);
                                modelOpt.setOpt(opt);
                                listOPTTdkMutlak.add(modelOpt);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        adapterTdkMutlak = new OptAdapter(InputDetailHarian.this, listOPTTdkMutlak);
                        rvTdkMutlak.setAdapter(adapterTdkMutlak);
                        adapterTdkMutlak.setOnItemClickCallback(new OptAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(final ModelOpt modelOpt) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InputDetailHarian.this);
                                builder.setTitle("Delete Data");
                                builder.setMessage("Apakah Anda Yakin?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.setTitle("Delete Data");
                                        progressDialog.setMessage("Mohon Tunggu Sebentar");
                                        progressDialog.show();
                                        final String id_jenisOPT = modelOpt.getId_jenisOPT();
                                        String url = Constants.ROOT_URL+"Opt";
                                        StringRequest request = new StringRequest(Request.Method.POST, url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (response.contains("berhasil")){
                                                            progressDialog.dismiss();
                                                            finish();
                                                            overridePendingTransition(0, 0);
                                                            startActivity(getIntent());
                                                            overridePendingTransition(0, 0);
                                                            Toast.makeText(InputDetailHarian.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                    }
                                                })
                                        {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("id_jenisOPT", id_jenisOPT);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                                        requestQueue.add(request);

                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override public void onClick(DialogInterface dialog, int which) {
                                        // TODO: Do something, when user click on the positive button
                                        dialog.cancel();
                                    }
                                });
                                builder.create().show();

                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
    }

    private void getDataMutlak() {
        id_detail = getIntent().getStringExtra("id_detail");
        String username = Common.currentUser.getUsername();
        String url = Constants.ROOT_URL + "Opt?username=" + username + "&status=mutlak&id_detail=" + id_detail+"&id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                String opt = object.getString("opt");
                                String id = object.getString("id_jenisOPT");

                                ModelOpt modelOpt = new ModelOpt();
                                modelOpt.setId_jenisOPT(id);
                                modelOpt.setOpt(opt);
                                listOPTMutlak.add(modelOpt);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        adapterMutlak = new OptAdapter(InputDetailHarian.this, listOPTMutlak);
                        rvMutlak.setAdapter(adapterMutlak);
                        adapterMutlak.setOnItemClickCallback(new OptAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(final ModelOpt modelOpt) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InputDetailHarian.this);
                                builder.setTitle("Delete Data");
                                builder.setMessage("Apakah Anda Yakin?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.setTitle("Delete Data");
                                        progressDialog.setMessage("Mohon Tunggu Sebentar");
                                        progressDialog.show();
                                        final String id_jenisOPT = modelOpt.getId_jenisOPT();
                                        String url = Constants.ROOT_URL+"Opt";
                                        StringRequest request = new StringRequest(Request.Method.POST, url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (response.contains("berhasil")){
                                                            progressDialog.dismiss();
                                                            finish();
                                                            overridePendingTransition(0, 0);
                                                            startActivity(getIntent());
                                                            overridePendingTransition(0, 0);
                                                            Toast.makeText(InputDetailHarian.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                    }
                                                })
                                        {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("id_jenisOPT", id_jenisOPT);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                                        requestQueue.add(request);

                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override public void onClick(DialogInterface dialog, int which) {
                                        // TODO: Do something, when user click on the positive button
                                        dialog.cancel();
                                    }
                                });
                                builder.create().show();

                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
    }

    private void openDialogMutlak() {
        dialog = new Dialog(InputDetailHarian.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.input_opt_mutlak);
        dialog.setCancelable(false);

        edtJumlahMutlak = (MaterialEditText) dialog.findViewById(R.id.edtJumlah);
        spOPT = (Spinner) dialog.findViewById(R.id.spOPT);
        mSaveButton = (Button) dialog.findViewById(R.id.save_button);
        mCancelButton = (Button) dialog.findViewById(R.id.cancel_button);

        String url = Constants.ROOT_URL + "Populate_OPT?status=mutlak";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("jenis_opt");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String opt = jsonObject.optString("opt");
                        jenis_opt_mutlak.add(opt);
                        optAdapterMutlak = new ArrayAdapter<>(InputDetailHarian.this, android.R.layout.simple_dropdown_item_1line, jenis_opt_mutlak);
                        optAdapterMutlak.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spOPT.setAdapter(optAdapterMutlak);
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

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Insert Data");
                progressDialog.setMessage("Mohon Tunggu Sebentar");
                progressDialog.show();
                String url = Constants.ROOT_URL + "Opt";
                final String opt = spOPT.getSelectedItem().toString();
                final String jumlah = edtJumlahMutlak.getText().toString();
                final String username = Common.currentUser.getUsername();
                final String id_detail = getIntent().getStringExtra("id_detail");
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = dateFormat.format(calendar.getTime());
                final String tanggal = date;
                if (id_detail != null) {
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("berhasil")) {
                                        progressDialog.dismiss();
                                        dialog.dismiss();
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                        Toast.makeText(InputDetailHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(InputDetailHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("opt", opt);
                            params.put("jumlah", jumlah);
                            params.put("username", username);
                            params.put("id_detail", id_detail);
                            params.put("id_hasil", id_hasil);
                            params.put("tanggal_insert", tanggal);
                            params.put("mutlak", "mutlak");
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                    requestQueue.add(request);
                } else {
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("berhasil")) {
                                        progressDialog.dismiss();
                                        dialog.dismiss();
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                        Toast.makeText(InputDetailHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(InputDetailHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("opt", opt);
                            params.put("jumlah", jumlah);
                            params.put("username", username);
                            params.put("id_detail", "0");
                            params.put("id_hasil", id_hasil);
                            params.put("tanggal_insert", tanggal);
                            params.put("mutlak", "mutlak");
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
                    requestQueue.add(request);
                }

            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                dialog.dismiss();
                dialog.cancel();
                optAdapterMutlak.clear();
            }
        });
        dialog.show();
    }

//    private void insertOPT() {
//
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == MY_LOCATION_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "location permission granted", Toast.LENGTH_LONG).show();
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "location permission denied", Toast.LENGTH_LONG).show();
            }
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img_bukti.setImageBitmap(photo);
            saveBitmapToJPG(photo);
        }
    }

    private void saveBitmapToJPG(Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] imageBytes = stream.toByteArray();

        encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void getDataDetail() {
        String url = Constants.ROOT_URL+"Detail_Pengamatan?id="+id_detail;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String jumlah_anakan = object.getString("jumlah_anakan");
//                                String opms = object.getString("opms");
//                                String domi = object.getString("domi");
//                                String kisar = object.getString("kisar");
                                String luas_spot_hopperburn = object.getString("luas_spot_hopperburn");
//                                String bukti_foto = object.getString("bukti_foto");
//                                base_url_bukti = Constants.ROOT_URL+"assets/Images/"+bukti_foto;
//                                DetailModel detailModel = new DetailModel();
//                                detailModel.setBukti_foto(bukti_foto);
//                                Common.detailModel = detailModel;

                                edtJumlahAnakan.setText(jumlah_anakan);
//                                edtOPMS.setText(opms);
//                                edtDomi.setText(domi);
//                                edtKisar.setText(kisar);
                                edtLuasSpot.setText(luas_spot_hopperburn);
                                txtDesa.setText("Rumpun ke-" + getIntent().getStringExtra("no_rumpun"));
//                                Picasso.get().load(base_url_bukti).into(img_bukti);

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
        Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
    }

    private void updateData() {
//        progressDialog.setTitle("Update Data");
//        progressDialog.setMessage("Mohon Tunggu Sebentar");
//        progressDialog.show();
//        progressDialog.setCancelable(false);
        final String id = getIntent().getStringExtra("id_detail");
        final String jumlah_anakan = edtJumlahAnakan.getText().toString();
//        final String bukti_foto = Common.detailModel.getBukti_foto();
        final String luas_spot_hopperburn = edtLuasSpot.getText().toString().trim();
        String url = Constants.ROOT_URL+"UpdateLaporan";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(InputDetailHarian.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(InputDetailHarian.this, "Data Gagal di Update", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_detail", id);
                params.put("jumlah_anakan", jumlah_anakan);
                assert optMutlak != null;
                if (!optMutlak.equals("null")){
                    params.put("opt_mutlak", optMutlak);
                    params.put("jumlah_opt_mutlak", jumlahMutlak);
                }else {
                    params.put("opt_mutlak", "0");
                    params.put("jumlah_opt_mutlak", "0");
                }
                if (!optTdkMutlak.equals("null")){
                    params.put("opt_tdk_mutlak", optTdkMutlak);
                    params.put("jumlah_opt_tdk_mutlak", jumlahTdkMutlak);
                }else {
                    params.put("opt_tdk_mutlak", "0");
                    params.put("jumlah_opt_tdk_mutlak", "0");
                }
                if (!optPopulasi.equals("null")){
                    params.put("opt_populasi", optPopulasi);
                    params.put("jumlah_opt_populasi", jumlahPopulasi);
                }else {
                    params.put("opt_populasi", "0");
                    params.put("jumlah_opt_populasi", "0");
                }
                if (!populasiM2.equals("null")){
                    params.put("opt_populasi_m", populasiM2);
                    params.put("jumlah_populasi_m", jumlahPopM2);
                }else {
                    params.put("opt_populasi_m", "0");
                    params.put("jumlah_populasi_m", "0");
                }
                if (!optMA.equals("null")){
                    params.put("opt_ma", optMA);
                    params.put("jumlah_opt_ma", jumlahMA);
                }else {
                    params.put("opt_ma", "0");
                    params.put("jumlah_opt_ma", "0");
                }
                params.put("luas_spot_hopperburn", luas_spot_hopperburn);
//                if (encodedImage != null){
//                    params.put("bukti_foto", encodedImage);
//                }else {
//                    params.put("bukti_foto_lama", bukti_foto);
//                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
        requestQueue.add(request);
    }

    public void getDataOPTtoInsert(){
        progressDialog.setTitle("Insert Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        progressDialog.setCancelable(false);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        String username = Common.currentUser.getUsername();
        String id = getIntent().getStringExtra("id_detail");
        if (id != null){
            String url = Constants.ROOT_URL+"Pop_OPT?username="+username+"&status=mutlak&id_detail="+id_detail+"&date="+date+"&id_hasil="+id_hasil;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i<array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);

                                    jumlahMutlak = object.getString("jumlah");
//                                    data1 = object.getString("jumlah");
                                    optMutlak = object.getString("opt");
                                    id_jenisOPT = object.getString("id_jenisOPT");

                                    getDataOPTTdkMutlak();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    jumlahMutlak = "0";
                    optMutlak = "0";
                    getDataOPTTdkMutlak();
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
        }else {
            String url = Constants.ROOT_URL + "Pop_OPT?username=" + username + "&status=mutlak&id_detail=0&date=" + date+"&id_hasil="+id_hasil;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    jumlahMutlak = object.getString("jumlah");
                                    optMutlak = object.getString("opt");
//                                    data1 = object.getString("jumlah");
                                    id_jenisOPT = object.getString("id_jenisOPT");

                                    getDataOPTTdkMutlak();

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    jumlahMutlak = "0";
                    optMutlak = "0";
                    getDataOPTTdkMutlak();
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
        }
    }

//    private void getData1() {
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        date = dateFormat.format(calendar.getTime());
//        String username = Common.currentUser.getUsername();
//
//        }
//    }

    public void getDataOPTTdkMutlak(){
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        String username = Common.currentUser.getUsername();
        String id = getIntent().getStringExtra("id_detail");
        if (id != null){
            String url = Constants.ROOT_URL+"Pop_OPT?username="+username+"&status=tidak mutlak&id_detail="+id+"&date="+date+"&id_hasil="+id_hasil;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i<array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);

                                    jumlahTdkMutlak = object.getString("jumlah");
                                    data2 = object.getString("jumlah");
                                    if (data2.equals("null")){
                                        data2 = "0";
                                    }
                                    optTdkMutlak = object.getString("opt");
                                    id_jenisOpt = object.getString("id_jenisOPT");

                                    getDataPopulasiInsert();


                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            adapterMutlak = new OptAdapter(InputDetailHarian.this, listOPTMutlak);
                            rvMutlak.setAdapter(adapterMutlak);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    jumlahTdkMutlak = "0";
                    optTdkMutlak = "0";
                    data2 = "0";
                    getDataPopulasiInsert();
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
        }else {
            String url = Constants.ROOT_URL + "Pop_OPT?username=" + username + "&status=tidak mutlak&id_detail=0&date=" + date+"&id_hasil="+id_hasil;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    jumlahTdkMutlak = object.getString("jumlah");
                                    optTdkMutlak = object.getString("opt");
                                    data2 = object.getString("jumlah");
                                    id_jenisOpt = object.getString("id_jenisOPT");
                                    if (data2.equals("null")){
                                        data2 = "0";
                                    }


                                    getDataPopulasiInsert();


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            adapterMutlak = new OptAdapter(InputDetailHarian.this, listOPTMutlak);
                            rvMutlak.setAdapter(adapterMutlak);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    jumlahTdkMutlak = "0";
                    optTdkMutlak = "0";
                    data2 = "0";
                    getDataPopulasiInsert();
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
        }
    }

    private void getDataPopulasiInsert() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        String username = Common.currentUser.getUsername();
        String id = getIntent().getStringExtra("id_detail");
        if (id != null){
            String url = Constants.ROOT_URL + "Pop_OPT?username=" + username + "&status=populasi&id_detail="+id+"&date=" + date+"&id_hasil="+id_hasil;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i<array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);

                                    jumlahPopulasi = object.getString("jumlah");
                                    optPopulasi = object.getString("opt");
                                    id_populasi = object.getString("id_jenisOPT");

//                                    getDataPopulasiMAtoInsert();
                                    getDataPopulasiM2toInsert();
//                                    if (id_detail != null){
//                                        updateData();
//                                    }else {
//                                        insertData();
//                                    }


                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
//                            adapterPopulasi = new PopulasiAdapter(InputDetailHarian.this, listPopulasi);
//                            rvPopulasi.setAdapter(adapterPopulasi);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    jumlahPopulasi = "0";
                    optPopulasi = "0";
                    getDataPopulasiM2toInsert();
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
        }else {
            String url = Constants.ROOT_URL + "Pop_OPT?username=" + username + "&status=populasi&id_detail=0&date=" + date+"&id_hasil="+id_hasil;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    jumlahPopulasi = object.getString("jumlah");
                                    optPopulasi = object.getString("opt");
                                    id_populasi = object.getString("id_jenisOPT");

//                                    getDataPopulasiMAtoInsert();

                                    getDataPopulasiM2toInsert();


//                                    if (id_detail != null) {
//                                        updateData();
//                                    } else {
//                                        insertData();
//                                    }


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            adapterPopulasi = new PopulasiAdapter(InputDetailHarian.this, listPopulasi);
//                            rvPopulasi.setAdapter(adapterPopulasi);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    jumlahTdkMutlak = "0";
                    optTdkMutlak = "0";
                    getDataPopulasiM2toInsert();
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
        }
    }

    private void getDataPopulasiM2toInsert() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        String username = Common.currentUser.getUsername();
        String id = getIntent().getStringExtra("id_detail");
        if (id != null){
            String url = Constants.ROOT_URL + "Pop_OPT?username=" + username + "&status=populasi(ma)&id_detail="+id+"&date=" + date+"&id_hasil="+id_hasil;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i<array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);

                                    jumlahPopM2 = object.getString("jumlah");
                                    populasiM2 = object.getString("opt");
                                    id_populasiM2 = object.getString("id_jenisOPT");

                                    getDataPopulasiMAtoInsert();
//                                    if (id_detail != null){
//                                        updateData();
//                                    }else {
//                                        insertData();
//                                    }


                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
//                            adapterPopulasi = new PopulasiAdapter(InputDetailHarian.this, listPopulasi);
//                            rvPopulasi.setAdapter(adapterPopulasi);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    jumlahPopM2 = "0";
                    populasiM2 = "0";
                    getDataPopulasiMAtoInsert();
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
        }else {
            String url = Constants.ROOT_URL + "Pop_OPT?username=" + username + "&status=populasi(m2)&id_detail=0&date="+date+"&id_hasil="+id_hasil;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    jumlahPopM2 = object.getString("jumlah");
                                    populasiM2 = object.getString("opt");
                                    id_populasiM2 = object.getString("id_jenisOPT");

                                    getDataPopulasiMAtoInsert();


//                                    if (id_detail != null) {
//                                        updateData();
//                                    } else {
//                                        insertData();
//                                    }


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            adapterPopulasi = new PopulasiAdapter(InputDetailHarian.this, listPopulasi);
//                            rvPopulasi.setAdapter(adapterPopulasi);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    jumlahPopM2 = "0";
                    populasiM2 = "0";
                    getDataPopulasiMAtoInsert();
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
        }
    }

    private void getDataPopulasiMAtoInsert() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        String username = Common.currentUser.getUsername();
        String id = getIntent().getStringExtra("id_detail");
        if (id != null){
            String url = Constants.ROOT_URL + "Pop_OPT?username=" + username + "&status=MA&id_detail="+id+"&date="+date+"&id_hasil="+id_hasil;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    jumlahMA = object.getString("jumlah");
                                    optMA = object.getString("opt");
                                    id_ma = object.getString("id_jenisOPT");

//                                    getDataPopulasiMAtoInsert();

                                    if (id_detail != null) {
                                        int total = Integer.parseInt(data1);
                                        if (Integer.parseInt(edtJumlahAnakan.getText().toString()) < total){
                                            progressDialog.dismiss();
                                            edtJumlahAnakan.setError("Jumlah Lebih Kecil dari Anakan Rusak");
                                        }else {
                                            updateData();
                                        }
                                    } else {
                                        int total = Integer.parseInt(data1);
                                        if (Integer.parseInt(edtJumlahAnakan.getText().toString()) < total){
                                            progressDialog.dismiss();
                                            edtJumlahAnakan.setError("Jumlah Lebih Kecil dari Anakan Rusak");
                                        }else {
                                            insertData();
                                        }
                                    }

//                                    if (id_detail != null){
//                                        updateData();
//                                    }else {
//                                        insertData();
//                                    }


                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
//                            adapterPopulasi = new PopulasiAdapter(InputDetailHarian.this, listPopulasi);
//                            rvPopulasi.setAdapter(adapterPopulasi);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    jumlahMA = "0";
                    optMA = "0";
//                    if (id_detail != null){
//                        updateData();
//                    }else {
//                        insertData();
//                    }
                    if (id_detail != null) {
                        int total = Integer.parseInt(data1);
                        if (Integer.parseInt(edtJumlahAnakan.getText().toString()) < total){
                            progressDialog.dismiss();
                            edtJumlahAnakan.setError("Jumlah Lebih Kecil dari Anakan Rusak");
                        }else {
                            updateData();
                        }
                    } else {
                        int total = Integer.parseInt(data1);
                        if (Integer.parseInt(edtJumlahAnakan.getText().toString()) < total){
                            progressDialog.dismiss();
                            edtJumlahAnakan.setError("Jumlah Lebih Kecil dari Anakan Rusak");
                        }else {
                            insertData();
                        }
                    }
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
        }else {
            String url = Constants.ROOT_URL + "Pop_OPT?username=" + username + "&status=MA&id_detail=0&date="+date+"&id_hasil="+id_hasil;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    jumlahMA = object.getString("jumlah");
                                    optMA = object.getString("opt");
                                    id_ma = object.getString("id_jenisOPT");


                                    if (id_detail != null) {
                                        int total = Integer.parseInt(data1);
                                        if (Integer.parseInt(edtJumlahAnakan.getText().toString()) < total){
                                            progressDialog.dismiss();
                                            edtJumlahAnakan.setError("Jumlah Lebih Kecil dari Anakan Rusak");
                                        }else {
                                            updateData();
                                        }
                                    } else {
                                        int total = Integer.parseInt(data1);
                                        if (Integer.parseInt(edtJumlahAnakan.getText().toString()) < total){
                                            progressDialog.dismiss();
                                            edtJumlahAnakan.setError("Jumlah Lebih Kecil dari Anakan Rusak");
                                        }else {
                                            insertData();
                                        }
                                    }
//                                    if (id_detail != null){
//                                        updateData();
//                                    }else {
//                                        insertData();
//                                    }


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            adapterPopulasi = new PopulasiAdapter(InputDetailHarian.this, listPopulasi);
//                            rvPopulasi.setAdapter(adapterPopulasi);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    jumlahMA = "0";
                    optMA = "0";
                    if (id_detail != null) {
                        int total = Integer.parseInt(data1);
                        if (Integer.parseInt(edtJumlahAnakan.getText().toString()) < total){
                            progressDialog.dismiss();
                            edtJumlahAnakan.setError("Jumlah Lebih Kecil dari Anakan Rusak");
                        }else {
                            updateData();
                        }
                    } else {
                        int total = Integer.parseInt(data1);
                        if (Integer.parseInt(edtJumlahAnakan.getText().toString()) < total){
                            progressDialog.dismiss();
                            edtJumlahAnakan.setError("Jumlah Lebih Kecil dari Anakan Rusak");
                        }else {
                            insertData();
                        }
                    }
//                    if (id_detail != null){
//                        updateData();
//                    }else {
//                        insertData();
//                    }
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
        }
    }

    private void insertData() {
        String url = Constants.ROOT_URL+"Detail_Pengamatan";
        final String kecamatan = Common.currentUser.getAlamat();
        final String provinsi = Common.currentUser.getProvinsi();
        final String kabupaten = Common.currentUser.getKabupaten();
        final String jumlah_anakan = edtJumlahAnakan.getText().toString().trim();
        final String luas_spot_hopperburn = edtLuasSpot.getText().toString().trim();
//        final String id_hasil_pengamatan = "0";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            getIDDetail();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(InputDetailHarian.this, "Data Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("provinsi", provinsi);
                params.put("kabupaten", kabupaten);
                params.put("kecamatan", kecamatan);
                params.put("jumlah_anakan", jumlah_anakan);
                assert optMutlak != null;
                if (!optMutlak.equals("null")){
                    params.put("opt_mutlak", optMutlak);
                    params.put("jumlah_opt_mutlak", jumlahMutlak);
                }else {
                    params.put("opt_mutlak", "0");
                    params.put("jumlah_opt_mutlak", "0");
                }
                if (!optTdkMutlak.equals("null")){
                    params.put("opt_tdk_mutlak", optTdkMutlak);
                    params.put("jumlah_opt_tdk_mutlak", jumlahTdkMutlak);
                }else {
                    params.put("opt_tdk_mutlak", "0");
                    params.put("jumlah_opt_tdk_mutlak", "0");
                }
                if (!optPopulasi.equals("null")){
                    params.put("opt_populasi", optPopulasi);
                    params.put("jumlah_opt_populasi", jumlahPopulasi);
                }else {
                    params.put("opt_populasi", "0");
                    params.put("jumlah_opt_populasi", "0");
                }
                if (!populasiM2.equals("null")){
                    params.put("opt_populasi_m", populasiM2);
                    params.put("jumlah_populasi_m", jumlahPopM2);
                }else {
                    params.put("opt_populasi_m", "0");
                    params.put("jumlah_populasi_m", "0");
                }
                if (!optMA.equals("null")){
                    params.put("opt_ma", optMA);
                    params.put("jumlah_opt_ma", jumlahMA);
                }else {
                    params.put("opt_ma", "0");
                    params.put("jumlah_opt_ma", "0");
                }

                params.put("luas_spot_hopperburn", luas_spot_hopperburn);
                params.put("id_hasil_pengamatan", id_hasil);
                params.put("tanggal_detail", tanggal);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
        requestQueue.add(request);
    }

    private void updateIdOpt() {
        String url = Constants.ROOT_URL+"Pop_OPT";
        final String id_detail = Common.detailModel.getId_detail();
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(InputDetailHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(InputDetailHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_detail", id_detail);
                params.put("id_jenisOPT", id_jenisOPT+","+id_jenisOpt+","+id_populasi+","+id_populasiM2+","+id_ma);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
        requestQueue.add(request);
    }

    private void updateIdPop() {
        String url = Constants.ROOT_URL+"Update_Ma";
        final String id_detail = Common.detailModel.getId_detail();
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(InputDetailHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(InputDetailHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Toast.makeText(InputDetailHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        Intent home = new Intent(InputDetailHarian.this, InputHarian.class);
                        finish();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_detail", id_detail);
                params.put("id_jenisMa", id_ma);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InputDetailHarian.this);
        requestQueue.add(request);
    }

    private void getIDDetail() {
        String userAlamat = Common.currentUser.getAlamat();
        String url = Constants.ROOT_URL+"Detail_Pengamatan?kecamatan="+userAlamat+"&date="+tanggal;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String id = object.getString("id_detail");

                                DetailModel detailModel = new DetailModel();
                                detailModel.setId_detail(id);
                                Common.detailModel = detailModel;
                                updateIdOpt();
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
        Volley.newRequestQueue(InputDetailHarian.this).add(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spOPT){
            jenis_MA.clear();
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
                                    String ma = jsonObject.optString("ma");
                                    jenis_MA.add(ma);
                                    maAdapter = new ArrayAdapter<>(InputDetailHarian.this, android.R.layout.simple_dropdown_item_1line, jenis_MA);
                                    maAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                    spMA.setAdapter(maAdapter);
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

//    @Override
//    public void onBackPressed() {
//        Log.d("CDA", "onBackPressed Called");
//        Intent setIntent = new Intent(Intent.ACTION_MAIN);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(setIntent);
//    }

}


