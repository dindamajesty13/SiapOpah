package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.majesty.siapopa.adapter.HasilAdapter;
import com.majesty.siapopa.model.HasilModel;
import com.majesty.siapopa.model.IntensitasModel;
import com.majesty.siapopa.model.ModelLapor;
import com.majesty.siapopa.model.SessionManager;
import com.majesty.siapopa.model.StockModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class Harian extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    Spinner spOPT;
    FloatingActionButton fab;
    List<HasilModel> listHasil;
    List<ModelLapor> listLapor;
    List<IntensitasModel> listApproval, listApprovalKab, listApprovalProv;
    List<StockModel> listStock;
    private RecyclerView.Adapter mAdapter;
    String username;
    String userAlamat, id_hasil, session_id;

    ProgressDialog progressDialog;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    GifImageView imageView2;
    TextView textView, textView1, txtPeringatan, txtDesa, txtVarietas, txtDate, txtOPT, txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harian);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Harian Keliling");
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        imageView2 = findViewById(R.id.imageView2);
        textView = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textView1);
        txtPeringatan = findViewById(R.id.peringatan);
        txtPeringatan.setVisibility(View.GONE);
        txtDesa = findViewById(R.id.txtDesa);
        txtDesa.setText("Desa");
        txtVarietas = findViewById(R.id.txtVarietas);
        txtVarietas.setText("Blok");
        txtDate = findViewById(R.id.txtDate);
        txtDate.setText("Varietas");
        txtOPT = findViewById(R.id.txtOPT);
        txtOPT.setVisibility(View.GONE);
        txtStatus = findViewById(R.id.txtStatus);
        txtStatus.setVisibility(View.GONE);

        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());

        fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_camera_alt_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager sessionManager = new SessionManager(Harian.this, SessionManager.SESSION_ID);
                HashMap<String, String> rememberMeDetails = sessionManager.getIdHasilDetailsFromSession();
                id_hasil = rememberMeDetails.get(SessionManager.KEY_IDHASIL);
                session_id = rememberMeDetails.get(SessionManager.KEY_DATE);
                if (session_id != null){
                    if (session_id.equals(date)){
                        if (id_hasil != null){
                            Intent intent = new Intent(Harian.this, InputRumpun.class);
                            startActivity(intent);
                        }
                    }else {
                        sessionManager.clearIdHasil();
                        Intent cartIntent = new Intent(Harian.this, InputHarian.class);
                        startActivity(cartIntent);
                    }
                }else {
                    Intent cartIntent = new Intent(Harian.this, InputHarian.class);
                    startActivity(cartIntent);
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new  LinearLayoutManager(Harian.this);
        recyclerView.setLayoutManager(layoutManager);
        listHasil = new ArrayList<>();
//
        getLaporan();
    }

    private void getLaporan(){
        progressDialog.setTitle("Loading");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String kabupaten = Common.currentUser.getKabupaten();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String tanggal = dateFormat.format(calendar.getTime());
        String urlLaporan = Constants.ROOT_URL+"Hasil_Pengamatan?kabupaten="+kabupaten+"&kecamatan="+userAlamat+"&date="+tanggal;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            textView.setVisibility(View.GONE);
                            textView1.setVisibility(View.GONE);
                            imageView2.setVisibility(View.GONE);
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                progressDialog.dismiss();
                                JSONObject object = array.getJSONObject(i);

                                String date = object.getString("tanggal_pengamatan");
                                String kecamatan = object.getString("kecamatan");
                                String varietas = object.getString("varietas");
                                String blok = object.getString("blok");
                                String desa = object.getString("desa");
                                String id = object.getString("id_hasil");
                                String petugas_pengamatan = object.getString("petugas_pengamatan");

                                HasilModel hasilModel = new HasilModel();
                                hasilModel.setId_hasil(id);
                                hasilModel.setKecamatan(kecamatan);
                                hasilModel.setDesa(desa);
                                hasilModel.setBlok(blok);
                                hasilModel.setVarietas(varietas);
                                hasilModel.setPetugas_pengamatan(petugas_pengamatan);
                                hasilModel.setTanggal_pengamatan(date);
                                listHasil.add(hasilModel);


                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        mAdapter = new HasilAdapter(Harian.this, listHasil);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Harian.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Harian.this).add(stringRequest);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


}