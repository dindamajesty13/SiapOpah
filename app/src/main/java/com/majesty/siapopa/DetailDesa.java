package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.majesty.siapopa.adapter.BulananAdapter;
import com.majesty.siapopa.adapter.HarianAdapter;
import com.majesty.siapopa.adapter.IntensitasAdapter;
import com.majesty.siapopa.model.HasilModel;
import com.majesty.siapopa.model.ModelLapor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailDesa extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    FloatingActionButton fab;
    List<HasilModel> listHasil;
    List<ModelLapor> listLapor;
    private RecyclerView.Adapter mAdapter;
    String username;
    String userAlamat;
    private ProgressDialog progressDialog;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date, hari, periode;
    TextView txtPeringatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_desa);

        String id = getIntent().getStringExtra("id");
        if (id.equals("bulanan")){
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Laporan Bulanan");
            setSupportActionBar(toolbar);
            fab = findViewById(R.id.fab);
            fab.hide();

            txtPeringatan = findViewById(R.id.peringatan);
            txtPeringatan.setVisibility(View.GONE);

            recyclerView = (RecyclerView) findViewById(R.id.rvList);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(DetailDesa.this);
            recyclerView.setLayoutManager(layoutManager);
            listHasil = new ArrayList<>();
            progressDialog = new ProgressDialog(this);
            calendar = Calendar.getInstance();

//            getBulanan();
        }else if (id.equals("setengah")){
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Laporan Setengah Bulanan");
            setSupportActionBar(toolbar);
            fab = findViewById(R.id.fab);
            fab.hide();

            TextView txtVarietas = findViewById(R.id.txtVarietas);
            txtVarietas.setText("Desa");

            TextView txtDate = findViewById(R.id.txtDate);
            txtDate.setText("Komoditas");

            TextView txtStatus = findViewById(R.id.txtStatus);
            txtStatus.setText("L Tambah");

            recyclerView = (RecyclerView) findViewById(R.id.rvList);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(DetailDesa.this);
            recyclerView.setLayoutManager(layoutManager);
            listHasil = new ArrayList<>();
            progressDialog = new ProgressDialog(this);
            calendar = Calendar.getInstance();

            dateFormat = new SimpleDateFormat("dd");
            hari = dateFormat.format(calendar.getTime());

            if (Integer.parseInt(hari) <= 15){
                periode = "Periode 1-15";
                getLaporan();
            }else {
                periode = "Periode 16-31";
                getLaporan();
            }
        }else if (id.equals("harian")){
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Laporan Harian");
            setSupportActionBar(toolbar);
            fab = findViewById(R.id.fab);
            fab.hide();

            recyclerView = (RecyclerView) findViewById(R.id.rvList);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(DetailDesa.this);
            recyclerView.setLayoutManager(layoutManager);
            listHasil = new ArrayList<>();
            progressDialog = new ProgressDialog(this);
            calendar = Calendar.getInstance();

            getHarian();
        }


    }

    private void getHarian() {
        progressDialog.setTitle("Loading");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String tanggal = dateFormat.format(calendar.getTime());
        String desa = getIntent().getStringExtra("desa");
        String urlLaporan = Constants.ROOT_URL+"Inten_Mutlak?kecamatan="+userAlamat+"&desa="+desa+"&tanggal="+tanggal;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                String jenis_opt = object.getString("jenis_opt");
                                String intensitas_tambah = object.getString("intensitas_tambah");
                                String tanggal_intensitas = object.getString("tanggal_intensitas");
                                String id_intensitas = object.getString("id_intensitas");
                                String id_hasil = object.getString("id_hasil");
                                String luas_diamati = object.getString("luas_diamati");
                                String blok = object.getString("blok");
                                String status_pelaporan = object.getString("status_pelaporan");
                                String status = object.getString("status");

                                HasilModel hasilModel = new HasilModel();
                                hasilModel.setOpt(jenis_opt);
                                hasilModel.setLuas_diamati(luas_diamati);
                                hasilModel.setBlok(blok);
                                hasilModel.setPetugas_pengamatan(status_pelaporan);
                                hasilModel.setId_hasil(id_intensitas);
                                hasilModel.setTanggal_pengamatan(tanggal_intensitas);
                                hasilModel.setTotal_intensitas(intensitas_tambah);
                                hasilModel.setKecamatan(id_hasil);
                                hasilModel.setKabupaten(status);
                                listHasil.add(hasilModel);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new HarianAdapter(DetailDesa.this, listHasil);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DetailDesa.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailDesa.this).add(stringRequest);
    }

    private void getBulanan() {
        dateFormat = new SimpleDateFormat("dd");
        String day = dateFormat.format(calendar.getTime());
        if (day.equals("28") || day.equals("29") || day.equals("30") || day.equals("31")){
            progressDialog.setTitle("Loading");
            progressDialog.show();
            userAlamat = Common.currentUser.getAlamat();
            dateFormat = new SimpleDateFormat("yyyy-MM");
            final String tanggal = dateFormat.format(calendar.getTime());
            String desa = getIntent().getStringExtra("desa");
            String urlLaporan = Constants.ROOT_URL+"Detail_Pengamatan?kecamatan="+userAlamat+"&desa="+desa+"&tanggal="+tanggal;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i<array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);

                                    String jumlah = object.getString("jumlah");
                                    String kabupaten = object.getString("kabupaten");
                                    String kecamatan = object.getString("kecamatan");
                                    String desa = object.getString("desa");
                                    String umur_tanaman = object.getString("umur_tanaman");
                                    String luas_tambah_serang = object.getString("luas_tambah");
                                    String luas_tanaman = object.getString("luas_tanaman");
                                    String jenis_opt = object.getString("jenis_opt");
                                    String intensitas_tambah = object.getString("intensitas_tambah");
                                    String komoditas = object.getString("komoditas");
                                    String varietas = object.getString("varietas");
                                    String luas_waspada = object.getString("luas_waspada");
                                    float total_intensitas = Float.parseFloat(intensitas_tambah) / Float.parseFloat(jumlah);

                                    HasilModel hasilModel = new HasilModel();
                                    hasilModel.setOpt(jenis_opt);
                                    hasilModel.setKabupaten(kabupaten);
                                    hasilModel.setKecamatan(kecamatan);
                                    hasilModel.setDesa(desa);
                                    hasilModel.setDari_umur(umur_tanaman);
                                    hasilModel.setLuas_diamati(luas_tambah_serang);
                                    hasilModel.setLuas_hamparan(luas_tanaman);
                                    hasilModel.setKomoditas(komoditas);
                                    hasilModel.setVarietas(varietas);
                                    hasilModel.setLuas_persemaian(luas_waspada);
                                    hasilModel.setTotal_intensitas(String.valueOf(total_intensitas));
                                    listHasil.add(hasilModel);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
                            mAdapter = new BulananAdapter(DetailDesa.this, listHasil);
                            recyclerView.setAdapter(mAdapter);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(DetailDesa.this, "No Data Found", Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(DetailDesa.this).add(stringRequest);
        }
    }

    private void getLaporan(){
        dateFormat = new SimpleDateFormat("dd");
        String day = dateFormat.format(calendar.getTime());
        progressDialog.setTitle("Loading");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        dateFormat = new SimpleDateFormat("yyyy-MM");
        final String tanggal = dateFormat.format(calendar.getTime());
        String desa = getIntent().getStringExtra("desa");
        String kabupaten = Common.currentUser.getKabupaten();
        String urlLaporan = Constants.ROOT_URL+"Hasil_Pengamatan?kabupaten="+kabupaten+"&kecamatan="+userAlamat+"&desa="+desa+"&tanggal="+tanggal+"&periode="+periode;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String jumlah = object.getString("jumlah");
                                String kabupaten = object.getString("kabupaten");
                                String kecamatan = object.getString("kecamatan");
                                String desa = object.getString("desa");
                                String umur_tanaman = object.getString("umur_tanaman");
                                String luas_tambah_serang = object.getString("luas_tambah_serang");
                                String periode_pengamatan = object.getString("periode_pengamatan");
                                String luas_tanaman = object.getString("luas_tanaman");
                                String jenis_opt = object.getString("jenis_opt");
                                String intensitas_tambah = object.getString("intensitas_tambah");
                                String komoditas = object.getString("komoditas");
                                String varietas = object.getString("varietas");
                                String luas_waspada = object.getString("luas_waspada");
                                String tambah_ringan = object.getString("tambah_ringan");
                                String tambah_sedang = object.getString("tambah_sedang");
                                String tambah_berat = object.getString("tambah_berat");
                                String tambah_puso = object.getString("tambah_puso");
                                String status = object.getString("status");
//                                String luas_tambah_serang = object.getString("kimia");
//                                String nabati = object.getString("nabati");
//                                String cara_lain = object.getString("cara_lain");
//                                String eradikasi = object.getString("eradikasi");
//                                String jumlah_pengendalian = object.getString("jumlah_pengendalian");
//                                String frek_kimia = object.getString("frek_kimia");
//                                String frek_nabati = object.getString("frek_nabati");
                                float total_intensitas = Float.parseFloat(intensitas_tambah) / Float.parseFloat(jumlah);

                                HasilModel hasilModel = new HasilModel();
//                                hasilModel.setKimia(kimia);
//                                hasilModel.setNabati(nabati);
//                                hasilModel.setEradikasi(eradikasi);
//                                hasilModel.setCara_lain(cara_lain);
//                                hasilModel.setFrek_kimia(frek_kimia);
//                                hasilModel.setFrek_nabati(frek_nabati);
//                                hasilModel.setJumlah_pengendalian(jumlah_pengendalian);
                                hasilModel.setOpt(jenis_opt);
                                hasilModel.setKabupaten(kabupaten);
                                hasilModel.setFrek_kimia(status);
                                hasilModel.setKecamatan(kecamatan);
                                hasilModel.setDesa(desa);
                                hasilModel.setBlok(tambah_ringan);
                                hasilModel.setProvinsi(tambah_berat);
                                hasilModel.setHingga_umur(tambah_sedang);
                                hasilModel.setPola_tanam(tambah_puso);
                                hasilModel.setDari_umur(umur_tanaman);
                                hasilModel.setLuas_diamati(luas_tambah_serang);
                                hasilModel.setTanggal_pengamatan(periode_pengamatan);
                                hasilModel.setLuas_hamparan(luas_tanaman);
                                hasilModel.setKomoditas(komoditas);
                                hasilModel.setVarietas(varietas);
                                hasilModel.setLuas_persemaian(luas_waspada);
                                hasilModel.setTotal_intensitas(String.valueOf(total_intensitas));
                                listHasil.add(hasilModel);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new IntensitasAdapter(DetailDesa.this, listHasil);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DetailDesa.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailDesa.this).add(stringRequest);
//        if (day.equals("14") || day.equals("15")){
//            progressDialog.setTitle("Loading");
//            progressDialog.show();
//            userAlamat = Common.currentUser.getAlamat();
//            dateFormat = new SimpleDateFormat("yyyy-MM");
//            final String tanggal = dateFormat.format(calendar.getTime());
//            String desa = getIntent().getStringExtra("desa");
//            String urlLaporan = Constants.ROOT_URL+"Hasil_Pengamatan?kecamatan="+userAlamat+"&desa="+desa+"&tanggal="+tanggal+"&periode="+periode;
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONArray array = new JSONArray(response);
//                                for (int i = 0; i<array.length(); i++){
//                                    JSONObject object = array.getJSONObject(i);
//
//                                    String jumlah = object.getString("jumlah");
//                                    String kabupaten = object.getString("kabupaten");
//                                    String kecamatan = object.getString("kecamatan");
//                                    String desa = object.getString("desa");
//                                    String umur_tanaman = object.getString("umur_tanaman");
//                                    String luas_tambah_serang = object.getString("luas_tambah_serang");
//                                    String periode_pengamatan = object.getString("periode_pengamatan");
//                                    String luas_tanaman = object.getString("luas_tanaman");
//                                    String jenis_opt = object.getString("jenis_opt");
//                                    String intensitas_tambah = object.getString("intensitas_tambah");
//                                    String komoditas = object.getString("komoditas");
//                                    String varietas = object.getString("varietas");
//                                    String luas_waspada = object.getString("luas_waspada");
//                                    float total_intensitas = Float.parseFloat(intensitas_tambah) / Float.parseFloat(jumlah);
//
//                                    HasilModel hasilModel = new HasilModel();
//                                    hasilModel.setOpt(jenis_opt);
//                                    hasilModel.setKabupaten(kabupaten);
//                                    hasilModel.setKecamatan(kecamatan);
//                                    hasilModel.setDesa(desa);
//                                    hasilModel.setDari_umur(umur_tanaman);
//                                    hasilModel.setLuas_diamati(luas_tambah_serang);
//                                    hasilModel.setTanggal_pengamatan(periode_pengamatan);
//                                    hasilModel.setLuas_hamparan(luas_tanaman);
//                                    hasilModel.setKomoditas(komoditas);
//                                    hasilModel.setVarietas(varietas);
//                                    hasilModel.setLuas_persemaian(luas_waspada);
//                                    hasilModel.setTotal_intensitas(String.valueOf(total_intensitas));
//                                    listHasil.add(hasilModel);
//                                }
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//                            progressDialog.dismiss();
//                            mAdapter = new IntensitasAdapter(DetailDesa.this, listHasil);
//                            recyclerView.setAdapter(mAdapter);
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    progressDialog.dismiss();
//                    Toast.makeText(DetailDesa.this, "No Data Found", Toast.LENGTH_LONG).show();
//                }
//            });
//            Volley.newRequestQueue(DetailDesa.this).add(stringRequest);
//        }else if (day.equals("28") || day.equals("29") || day.equals("30") || day.equals("31")){
//
//        }

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