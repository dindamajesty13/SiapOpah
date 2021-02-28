package com.majesty.siapopa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.majesty.siapopa.adapter.AdapterMa;
import com.majesty.siapopa.model.ModelMa;
import com.rengwuxian.materialedittext.MaterialEditText;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailLapor extends AppCompatActivity {
    String id_intensitas;
    EditText edtRekomendasi;
    Button btnLapor, btnSimpan, btn_cetak;
    ProgressDialog progressDialog;
    Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date, substring;
    List<ModelMa> listMa;
    String intensitas_serang, luas_keadaan_sisa, intensitas_keadaan_sisa, ma, periode, k_ringan, k_sedang, k_berat, k_puso;
    String jumlah_ma;
    String musuh_alami, kimia, nabati, cara_lain, eradikasi, frek_kimia, frek_nabati, jumlah_pengendalian;
    ;
    Toolbar toolbar;
    int keadaan_ringan, keadaan_sedang, keadaan_berat, keadaan_puso;
    RecyclerView recyclerView;
    private AdapterMa adapterMA;
    RecyclerView.LayoutManager layoutManager, layoutManager1;
    String opt, kabupaten, kecamatan, desa, umur_tanaman, luas_tambah, periode_pengamatan,id_hsl_stgh, luas_tanaman, komoditas, varietas, intensitas_tambah, luas_waspada, now, musim_tanam;
    String tambah_ringan, tambah_sedang, tambah_berat, tambah_puso, blok, luas_panen,  pola_tanam, petugas_pengamatan, luas_persemaian, luas_tambah_serang, luas_sembuh, rekomendasi, jenis_opt, luas_serang, id_hasil,  luas,  umur,  intensitas, tanggal_intensitas;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    TextView txtPelaporan, txtApprovalKab, txtApprovalSatpel, txtApprovalProv, msg_kab, msg_satpel, msg_prov, txtKR, txtKS, txtKB, txtKP, txtJumlahKeadaan;
    TextView txtTanggal, txtKecamatan, txtDesa, txtPeriode, txtOPT, txtKomoditas, txtVarietas, txtLuasHamparan, txtLuasWaspada, txtUmurTanaman, txtMusim, txtLuasTambah, txtTR, txtTS, txtTB, txtTP,
            txtJumlahTambah, txtLuasPengendalian, txtKimia, txtNabati, txtEradikasi, txtCaraLain, txtJumlahPengendalian, txtFrekuensi, txtFrekKimia, txtFrekNabati,
            txtLuasSisa, txtSR, txtSS, txtSB, txtSP, txtJumlahSisa, sebelumnya, txtLuasPanen, txtLuasSembuh, txtSRS, txtSSS, txtSBS, txtSPS, txtJumlahSisaS, txtTRS, txtTSS, txtTBS, txtTPS, txtJumlahTambahS, sekarang ;
    MaterialEditText edtRingan, edtSedang, edtBerat, edtPuso, edtLuasSembuh, edtLuasPanen, edtKimia, edtNabati, edtEradikasi, edtCaraLain, edtFrekKimia, edtFrekNabati;
    String status, luas_tambah_sebelumnya, t_ringan, t_sedang, t_berat, t_puso, luas_sisa_sebelumnya, s_ringan, s_sedang, s_berat, s_puso, panen_sebelumnya, sembuh_sebelumnya, jumlah_keadaan, kimia_seb, nabati_seb, eradikasi_seb, cara_lain_seb, frek_kimia_seb, frek_nabati_seb, luas_pengendalian_seb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lapor);

        txtKR = (TextView) findViewById(R.id.txtKR);
        txtKB = (TextView) findViewById(R.id.txtKB);
        txtKS = (TextView) findViewById(R.id.txtKS);
        txtKP = (TextView) findViewById(R.id.txtKP);
        txtJumlahKeadaan = (TextView) findViewById(R.id.txtJumlahKeadaan);
        txtKimia = (TextView) findViewById(R.id.txtKimia);
        txtNabati = (TextView) findViewById(R.id.txtNabati);
        txtEradikasi = (TextView) findViewById(R.id.txtEradikasi);
        txtCaraLain = (TextView) findViewById(R.id.txtCaraLain);
        txtJumlahPengendalian = (TextView) findViewById(R.id.txtJumlahPengendalian);
        txtFrekKimia = (TextView) findViewById(R.id.txtFrekKimia);
        txtFrekNabati = (TextView) findViewById(R.id.txtFrekNabati);
        sebelumnya = (TextView) findViewById(R.id.sebelumnya);
        txtLuasPanen = (TextView) findViewById(R.id.txtLuasPanen);
        txtLuasSembuh = (TextView) findViewById(R.id.txtLuasSembuh);
        txtSRS = (TextView) findViewById(R.id.txtSRS);
        txtSSS = (TextView) findViewById(R.id.txtSSS);
        txtSBS = (TextView) findViewById(R.id.txtSBS);
        txtSPS = (TextView) findViewById(R.id.txtSPS);
        txtJumlahSisaS = (TextView) findViewById(R.id.txtJumlahSisaS);
        txtTRS = (TextView) findViewById(R.id.txtTRS);
        txtTSS = (TextView) findViewById(R.id.txtTSS);
        txtTBS = (TextView) findViewById(R.id.txtTBS);
        txtTPS = (TextView) findViewById(R.id.txtTPS);
        txtJumlahTambahS = (TextView) findViewById(R.id.txtJumlahTambahS);
        sekarang = (TextView) findViewById(R.id.sekarang);
        msg_kab = (TextView) findViewById(R.id.msg_kab);
        msg_satpel = (TextView) findViewById(R.id.msg_satpel);
        msg_prov = (TextView) findViewById(R.id.msg_prov);
        txtPelaporan = (TextView) findViewById(R.id.txtPelaporan);
        txtApprovalKab = (TextView) findViewById(R.id.txtApprovalKab);
        txtApprovalSatpel = (TextView) findViewById(R.id.txtApprovalSatpel);
        txtApprovalProv = (TextView) findViewById(R.id.txtApprovalProv);
        edtKimia = (MaterialEditText) findViewById(R.id.edtKimia);
        edtNabati = (MaterialEditText) findViewById(R.id.edtNabati);
        edtEradikasi = (MaterialEditText) findViewById(R.id.edtEradikasi);
        edtCaraLain = (MaterialEditText) findViewById(R.id.edtCaraLain);
        edtFrekKimia = (MaterialEditText) findViewById(R.id.edtFrekKimia);
        edtFrekNabati = (MaterialEditText) findViewById(R.id.edtFrekNabati);
        txtOPT = (TextView) findViewById(R.id.txtOPT);
        txtJumlahSisa = (TextView) findViewById(R.id.txtJumlahSisa);
        txtSP = (TextView) findViewById(R.id.txtSP);
        txtSB = (TextView) findViewById(R.id.txtSB);
        txtSS = (TextView) findViewById(R.id.txtSS);
        txtSR = (TextView) findViewById(R.id.txtSR);
        txtLuasSisa = (TextView) findViewById(R.id.txtLuasSisa);
        txtJumlahTambah = (TextView) findViewById(R.id.txtJumlahTambah);
        txtTP = (TextView) findViewById(R.id.txtTP);
        txtTB = (TextView) findViewById(R.id.txtTB);
        txtDesa = (TextView) findViewById(R.id.txtDesa);
        txtKomoditas = (TextView) findViewById(R.id.txtKomoditas);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Laporan Setengah Bulan");
        setSupportActionBar(toolbar);
        txtVarietas = (TextView) findViewById(R.id.txtVarietas);
        txtLuasHamparan = (TextView) findViewById(R.id.txtLuasHamparan);
        txtKecamatan = (TextView) findViewById(R.id.txtKecamatan);
        txtPeriode = (TextView) findViewById(R.id.txtPeriode);
        txtMusim = (TextView) findViewById(R.id.txtMusim);
        txtUmurTanaman = (TextView) findViewById(R.id.txtUmurTanaman);
        txtTanggal = (TextView) findViewById(R.id.txtTanggal);
        txtLuasTambah = (TextView) findViewById(R.id.txtLuasTambah);
        txtTR = (TextView) findViewById(R.id.txtTR);
        txtTS = (TextView) findViewById(R.id.txtTS);
        edtLuasSembuh = (MaterialEditText) findViewById(R.id.edtLuasSembuh);
        edtRingan = (MaterialEditText) findViewById(R.id.edtRingan);
//        edtMusimTanam = (MaterialEditText) findViewById(R.id.edtMusimTanam);
        edtSedang = (MaterialEditText) findViewById(R.id.edtSedang);
        edtBerat = (MaterialEditText) findViewById(R.id.edtBerat);
        edtPuso = (MaterialEditText) findViewById(R.id.edtPuso);
        edtLuasPanen = (MaterialEditText) findViewById(R.id.edtLuasPanen);
        txtLuasWaspada = (TextView) findViewById(R.id.txtLuasWaspada);
//        btnLapor = (Button) findViewById(R.id.btnLapor);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btn_cetak = (Button) findViewById(R.id.btn_cetak);
        progressDialog = new ProgressDialog(this);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM");
        now = dateFormat.format(calendar.getTime());
        if (now.equals("04")||now.equals("05")||now.equals("06")||now.equals("07")||now.equals("08")||now.equals("09")){
            musim_tanam = "Kemarau";
        }else {
            musim_tanam = "Hujan";
        }
        opt = getIntent().getStringExtra("opt");
        kabupaten = getIntent().getStringExtra("kabupaten");
        kecamatan = getIntent().getStringExtra("kecamatan");
        desa = getIntent().getStringExtra("desa");
        umur_tanaman = getIntent().getStringExtra("umur_tanaman");
        luas_tambah = getIntent().getStringExtra("luas_tambah");
        periode_pengamatan = getIntent().getStringExtra("periode_pengamatan");
        luas_tanaman = getIntent().getStringExtra("luas_tanaman");
        komoditas = getIntent().getStringExtra("komoditas");
        varietas = getIntent().getStringExtra("varietas");
        intensitas_tambah = getIntent().getStringExtra("intensitas_tambah");
        luas_waspada = getIntent().getStringExtra("luas_waspada");
        tambah_ringan = getIntent().getStringExtra("tambah_ringan");
        tambah_berat = getIntent().getStringExtra("tambah_berat");
        tambah_sedang = getIntent().getStringExtra("tambah_sedang");
        tambah_puso = getIntent().getStringExtra("tambah_puso");
        status = getIntent().getStringExtra("status");
//        kimia = getIntent().getStringExtra("kimia");
//        nabati = getIntent().getStringExtra("nabati");
//        eradikasi = getIntent().getStringExtra("eradikasi");
//        cara_lain = getIntent().getStringExtra("cara_lain");
//        frek_kimia = getIntent().getStringExtra("frek_kimia");
//        frek_nabati = getIntent().getStringExtra("frek_nabati");
//        jumlah_pengendalian = getIntent().getStringExtra("jumlah_pengendalian");

//        getData();
        String id = getIntent().getStringExtra("id");
        if (id == null){
            getIdStghBln();
        }else {
            id_hsl_stgh = id;
            updateStatus();
            getSisa();
        }


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanData();
            }
        });
        btn_cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void updateStatus() {
        String url = Constants.ROOT_URL + "Pemberitahuan";
        final String id_notif = getIntent().getStringExtra("id_notif");
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")) {
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
                params.put("id", id_notif);
                params.put("status", "Sudah");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailLapor.this);
        requestQueue.add(request);
    }

    private void getSisa() {
        String url = Constants.ROOT_URL + "Stgh_Bln?id="+id_hsl_stgh;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dateFormat = new SimpleDateFormat("dd");
                            String hari = dateFormat.format(calendar.getTime());
                            SimpleDateFormat month = new SimpleDateFormat("MMMM");
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                progressDialog.dismiss();

                                String periode_pengamatan = object.getString("periode");
                                String desa = object.getString("desa");
                                String opt = object.getString("jenis_opt");

                                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                date = dateFormat.format(calendar.getTime());
                                String bulan = date.substring(5,7);
                                String tahun = date.substring(0,4);
                                if (periode_pengamatan.equals("Periode 1-15")){
                                    periode = "Periode 16-31";
                                    int bulan1 = Integer.parseInt(bulan) - 1;
                                    int tahun1 = Integer.parseInt(tahun);
                                    String bul = "";
                                    if (bulan1 != 10 && bulan1 != 11 && bulan1 != 12){
                                        bul = "0" + String.valueOf(bulan1);

                                    }else {
                                        bul = String.valueOf(bulan1);
                                    }
                                    String substring = String.valueOf(tahun1) + "-" + bul;
                                    String urlLaporan = Constants.ROOT_URL+"Update_Inten?periode="+periode+"&date="+substring+"&desa="+desa+"&opt="+opt;
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONArray array = new JSONArray(response);
                                                        for (int i = 0; i<array.length(); i++){
                                                            JSONObject object = array.getJSONObject(i);

                                                            luas_keadaan_sisa = object.getString("jumlah_keadaan");
                                                            k_ringan = object.getString("keadaan_ringan");
                                                            k_sedang = object.getString("keadaan_sedang");
                                                            k_berat = object.getString("keadaan_berat");
                                                            k_puso = object.getString("keadaan_puso");
                                                            intensitas_keadaan_sisa = object.getString("intensitas_keadaan");
                                                            luas_tambah_sebelumnya = object.getString("jumlah_tambah");
                                                            t_ringan = object.getString("tambah_ringan");
                                                            t_sedang = object.getString("tambah_sedang");
                                                            t_berat = object.getString("tambah_berat");
                                                            t_puso = object.getString("tambah_puso");
                                                            luas_sisa_sebelumnya = object.getString("jumlah_sisa");
                                                            s_ringan = object.getString("sisa_ringan");
                                                            s_sedang = object.getString("sisa_sedang");
                                                            s_berat = object.getString("sisa_berat");
                                                            s_puso = object.getString("sisa_puso");
                                                            panen_sebelumnya = object.getString("luas_panen");
                                                            sembuh_sebelumnya = object.getString("luas_sembuh");
                                                            kimia_seb = object.getString("luas_kimia");
                                                            nabati_seb = object.getString("luas_nabati");
                                                            eradikasi_seb = object.getString("luas_eradikasi");
                                                            cara_lain_seb = object.getString("luas_cara_lain");
                                                            frek_kimia_seb = object.getString("frek_kimia");
                                                            frek_nabati_seb = object.getString("frek_nabati");
                                                            luas_pengendalian_seb = object.getString("jumlah_pengendalian");

                                                            showData();


                                                        }
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            luas_keadaan_sisa = "0";
                                            intensitas_keadaan_sisa = "0";
                                            k_ringan = "0";
                                            k_sedang = "0";
                                            k_berat = "0";
                                            k_puso = "0";
                                            luas_tambah_sebelumnya = "0";
                                            t_ringan = "0";
                                            t_sedang = "0";
                                            t_berat = "0";
                                            t_puso = "0";
                                            luas_sisa_sebelumnya = "0";
                                            s_ringan = "0";
                                            s_sedang = "0";
                                            s_berat = "0";
                                            s_puso = "0";
                                            panen_sebelumnya = "0";
                                            sembuh_sebelumnya = "0";
                                            kimia_seb = "0";
                                            nabati_seb = "0";
                                            eradikasi_seb = "0";
                                            cara_lain_seb = "0";
                                            frek_kimia_seb = "0";
                                            frek_nabati_seb = "0";
                                            luas_pengendalian_seb = "0";
                                            showData();
                                        }
                                    });
                                    Volley.newRequestQueue(DetailLapor.this).add(stringRequest);
                                }else {
                                    periode = "Periode 1-15";
                                    String substring = date.substring(0, 7);

                                    String urlLaporan = Constants.ROOT_URL + "Update_Inten?periode=" + periode + "&date=" + substring + "&desa=" + desa + "&opt=" + opt;
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONArray array = new JSONArray(response);
                                                        for (int i = 0; i < array.length(); i++) {
                                                            JSONObject object = array.getJSONObject(i);

                                                            luas_keadaan_sisa = object.getString("jumlah_keadaan");
                                                            k_ringan = object.getString("keadaan_ringan");
                                                            k_sedang = object.getString("keadaan_sedang");
                                                            k_berat = object.getString("keadaan_berat");
                                                            k_puso = object.getString("keadaan_puso");
                                                            intensitas_keadaan_sisa = object.getString("intensitas_keadaan");
                                                            luas_tambah_sebelumnya = object.getString("jumlah_tambah");
                                                            t_ringan = object.getString("tambah_ringan");
                                                            t_sedang = object.getString("tambah_sedang");
                                                            t_berat = object.getString("tambah_berat");
                                                            t_puso = object.getString("tambah_puso");
                                                            luas_sisa_sebelumnya = object.getString("jumlah_sisa");
                                                            s_ringan = object.getString("sisa_ringan");
                                                            s_sedang = object.getString("sisa_sedang");
                                                            s_berat = object.getString("sisa_berat");
                                                            s_puso = object.getString("sisa_puso");
                                                            panen_sebelumnya = object.getString("luas_panen");
                                                            sembuh_sebelumnya = object.getString("luas_sembuh");
                                                            kimia_seb = object.getString("luas_kimia");
                                                            nabati_seb = object.getString("luas_nabati");
                                                            eradikasi_seb = object.getString("luas_eradikasi");
                                                            cara_lain_seb = object.getString("luas_cara_lain");
                                                            frek_kimia_seb = object.getString("frek_kimia");
                                                            frek_nabati_seb = object.getString("frek_nabati");
                                                            luas_pengendalian_seb = object.getString("jumlah_pengendalian");

                                                            showData();

                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            luas_keadaan_sisa = "0";
                                            intensitas_keadaan_sisa = "0";
                                            k_ringan = "0";
                                            k_sedang = "0";
                                            k_berat = "0";
                                            k_puso = "0";
                                            luas_tambah_sebelumnya = "0";
                                            t_ringan = "0";
                                            t_sedang = "0";
                                            t_berat = "0";
                                            t_puso = "0";
                                            luas_sisa_sebelumnya = "0";
                                            s_ringan = "0";
                                            s_sedang = "0";
                                            s_berat = "0";
                                            s_puso = "0";
                                            panen_sebelumnya = "0";
                                            sembuh_sebelumnya = "0";
                                            kimia_seb = "0";
                                            nabati_seb = "0";
                                            eradikasi_seb = "0";
                                            cara_lain_seb = "0";
                                            frek_kimia_seb = "0";
                                            frek_nabati_seb = "0";
                                            luas_pengendalian_seb = "0";
                                            showData();
                                        }
                                    });
                                    Volley.newRequestQueue(DetailLapor.this).add(stringRequest);
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(DetailLapor.this).add(stringRequest);

    }

    private void showData() {
        String url = Constants.ROOT_URL + "Stgh_Bln?id="+id_hsl_stgh;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dateFormat = new SimpleDateFormat("dd");
                            String hari = dateFormat.format(calendar.getTime());
                            SimpleDateFormat month = new SimpleDateFormat("MMMM");
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                progressDialog.dismiss();

                                String luas_sembuh = object.getString("luas_terkendali");
                                String luas_panen = object.getString("luas_panen");
                                String kimia = object.getString("luas_kimia");
                                String nabati = object.getString("luas_nabati");
                                String eradikasi = object.getString("luas_eradikasi");
                                String cara_lain = object.getString("luas_cara_lain");
                                String frek_kimia = object.getString("frek_kimia");
                                String frek_nabati = object.getString("frek_nabati");
                                String sisa_ringan = object.getString("sisa_ringan");
                                String sisa_sedang = object.getString("sisa_sedang");
                                String sisa_berat = object.getString("sisa_berat");
                                String sisa_puso = object.getString("sisa_puso");
                                String status_pelaporan = object.getString("status_pelaporan");
                                String approval_kab = object.getString("approval_kab");
                                String approval_satpel = object.getString("approval_satpel");
                                String approval_provinsi = object.getString("approval_prov");
                                String kab_to_popt = object.getString("kab_to_popt");
                                String satpel_to_popt = object.getString("satpel_to_popt");
                                String prov_to_popt = object.getString("prov_to_popt");
                                periode_pengamatan = object.getString("periode");
                                kecamatan = object.getString("kecamatan");
                                desa = object.getString("desa");
                                opt = object.getString("jenis_opt");
                                luas_tanaman = object.getString("luas_tanaman");
                                umur_tanaman = object.getString("umur_tanaman");
                                varietas = object.getString("varietas");
                                komoditas = object.getString("komoditas");
                                luas_waspada = object.getString("luas_waspada");
                                tambah_ringan = object.getString("tambah_ringan");
                                tambah_sedang = object.getString("tambah_sedang");
                                tambah_berat = object.getString("tambah_berat");
                                tambah_puso = object.getString("tambah_puso");
                                String keadaan_ringan = object.getString("keadaan_ringan");
                                String keadaan_sedang = object.getString("keadaan_sedang");
                                String keadaan_berat = object.getString("keadaan_berat");
                                String keadaan_puso = object.getString("keadaan_puso");
                                String jumlah_sisa = object.getString("jumlah_sisa");
                                String jumlah_tambah = object.getString("jumlah_tambah");
                                jumlah_keadaan = object.getString("jumlah_keadaan");

                                String bulan = month.format(calendar.getTime());
                                if (periode_pengamatan.equals("Periode 1-15")) {
                                    txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Laporan Setengah Bulan (Tanggal 1- </b></font>" + hari + " " + bulan + ")"));
                                } else {
                                    txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Laporan Setengah Bulan (Tanggal 16- </b></font>" + hari + " " + bulan + ")"));
                                }
                                txtOPT.setText(Html.fromHtml("<font color='#000000'><b>OPT: </b></font>" + opt));
                                txtLuasWaspada.setText(Html.fromHtml("<font color='#000000'><b>Luas Waspada: </b></font>" + luas_waspada));
                                txtMusim.setText(Html.fromHtml("<font color='#000000'><b>Musim: </b></font>" + musim_tanam));
                                txtTR.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + tambah_ringan));
                                txtTS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + tambah_sedang));
                                txtTB.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + tambah_berat));
                                txtTP.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + tambah_puso));
                                txtJumlahTambah.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + jumlah_tambah));
                                txtSR.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + k_ringan));
                                txtSS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + k_sedang));
                                txtSB.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + k_berat));
                                txtSP.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + k_puso));
                                txtJumlahSisa.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_keadaan_sisa));
                                txtKomoditas.setText(Html.fromHtml("<font color='#000000'><b>Komoditas: </b></font>" + komoditas));
                                txtKecamatan.setText(Html.fromHtml("<font color='#000000'><b>Kec: </b></font>" + kecamatan));
                                txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Desa: </b></font>" + desa));
                                txtVarietas.setText(Html.fromHtml("<font color='#000000'><b>Varietas: </b></font>" + varietas));
                                txtPeriode.setText(Html.fromHtml(periode_pengamatan));
                                txtLuasHamparan.setText(Html.fromHtml("<font color='#000000'><b>Luas Pertanaman: </b></font>" + luas_tanaman + " Ha"));
                                txtUmurTanaman.setText(Html.fromHtml("<font color='#000000'><b>Umur Tanaman: </b></font>" + umur_tanaman + " HST"));
                                edtRingan.setText(sisa_ringan);
                                edtSedang.setText(sisa_sedang);
                                edtBerat.setText(sisa_berat);
                                edtPuso.setText(sisa_puso);
                                edtKimia.setText(kimia);
                                edtNabati.setText(nabati);
                                edtEradikasi.setText(eradikasi);
                                edtCaraLain.setText(cara_lain);
                                edtFrekKimia.setText(frek_kimia);
                                edtFrekNabati.setText(frek_nabati);
                                edtLuasSembuh.setText(luas_sembuh);
                                edtLuasPanen.setText(luas_panen);
                                txtPelaporan.setText(Html.fromHtml("<font color='#000000'><b>Pelaporan: </b></font>" + status_pelaporan));
                                txtApprovalKab.setText(Html.fromHtml("<font color='#000000'><b>Val Kortikab: </b></font>" + approval_kab));
                                txtApprovalSatpel.setText(Html.fromHtml("<font color='#000000'><b>Val SatPel: </b></font>" + approval_satpel));
                                txtApprovalProv.setText(Html.fromHtml("<font color='#000000'><b>Val BPTPH: </b></font>" + approval_provinsi));
                                msg_kab.setText(Html.fromHtml("<font color='#000000'><b>Kortikab: </b></font>" + kab_to_popt));
                                msg_prov.setText(Html.fromHtml("<font color='#000000'><b>Val BPTPH: </b></font>" + prov_to_popt));
                                msg_satpel.setText(Html.fromHtml("<font color='#000000'><b>Val BPTPH: </b></font>" + satpel_to_popt));
                                sebelumnya.setText(Html.fromHtml("<font color='#000000'><b>Laporan : </b></font>" + periode));
                                sekarang.setText(Html.fromHtml("<font color='#000000'><b>Laporan : </b></font>" + periode_pengamatan));
                                txtLuasPanen.setText(Html.fromHtml("<font color='#000000'><b>L Panen: </b></font>" + panen_sebelumnya));
                                txtLuasSembuh.setText(Html.fromHtml("<font color='#000000'><b>L Sembuh: </b></font>" + sembuh_sebelumnya));
                                txtSRS.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + s_ringan));
                                txtSSS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + s_sedang));
                                txtSBS.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + s_berat));
                                txtSPS.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + s_puso));
                                txtJumlahSisaS.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_sisa_sebelumnya));
                                txtTRS.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + t_ringan));
                                txtTSS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + t_sedang));
                                txtTBS.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + t_berat));
                                txtTPS.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + t_puso));
                                txtJumlahTambahS.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_tambah_sebelumnya));
                                txtKimia.setText(Html.fromHtml("<font color='#000000'><b>K: </b></font>" + kimia_seb));
                                txtNabati.setText(Html.fromHtml("<font color='#000000'><b>N: </b></font>" + nabati_seb));
                                txtEradikasi.setText(Html.fromHtml("<font color='#000000'><b>E: </b></font>" + eradikasi_seb));
                                txtCaraLain.setText(Html.fromHtml("<font color='#000000'><b>CL: </b></font>" + cara_lain_seb));
                                txtJumlahPengendalian.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_pengendalian_seb));
                                txtFrekKimia.setText(Html.fromHtml("<font color='#000000'><b>Frek Kimia: </b></font>" + frek_kimia_seb));
                                txtFrekNabati.setText(Html.fromHtml("<font color='#000000'><b>Frek Nabati: </b></font>" + frek_nabati_seb));
                                txtKR.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + keadaan_ringan));
                                txtKS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + keadaan_sedang));
                                txtKB.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + keadaan_berat));
                                txtKP.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + keadaan_puso));
                                txtJumlahKeadaan.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + jumlah_keadaan));

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(DetailLapor.this).add(stringRequest);

    }


    private void getIdStghBln() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String tanggal = simpleDateFormat.format(calendar.getTime());
        String username = Common.currentUser.getUsername();
        String url = Constants.ROOT_URL+"Stgh_Bln?username="+username+"&periode="+periode_pengamatan+"&date="+tanggal+"&desa="+desa+"&opt="+opt;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                id_hsl_stgh = object.getString("id_hsl_stgh");

                                getData();

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getData();
//                    buildRecyclerViewDataHarian();
//                    Toast.makeText(DetailLapor.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailLapor.this).add(stringRequest);
    }


    private void getDatatoPDF() {
        String url = Constants.ROOT_URL+"Stgh_Bln?id="+id_hsl_stgh;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/siap_opa/setengah_bulan");
                            if (!docsFolder.exists()) {
                                docsFolder.mkdir();
                            }
                            String pdfname = "Laporan Setengah Bulan "+tanggal_intensitas+".pdf";
                            pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
                            OutputStream output = null;
                            try {
                                output = new FileOutputStream(pdfFile);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            final Document document = new Document(PageSize.A4.rotate());
                            Font a = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.NORMAL, BaseColor.BLACK);
                            final PdfPTable data = new PdfPTable(new float[]{2, 2});
                            data.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                            data.getDefaultCell().setFixedHeight(50);
                            data.setTotalWidth(PageSize.A4.getWidth());
                            data.setWidthPercentage(100);
                            data.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                            PdfPCell note1 = new PdfPCell(new Paragraph("Provinsi: Jawa Barat", a));
                            PdfPCell note2 = new PdfPCell(new Paragraph("Kabupaten: "+kabupaten, a));
                            PdfPCell note3 = new PdfPCell(new Paragraph("Kecamatan: "+kecamatan, a));
                            PdfPCell note4 = new PdfPCell(new Paragraph("Desa: "+desa, a));
                            note1.setBorder(Rectangle.NO_BORDER);
                            note2.setBorder(Rectangle.NO_BORDER);
                            note1.setSpaceCharRatio(4f);
                            note2.setSpaceCharRatio(4f);
                            note3.setBorder(Rectangle.NO_BORDER);
                            note4.setBorder(Rectangle.NO_BORDER);
                            note3.setSpaceCharRatio(4f);
                            note4.setSpaceCharRatio(4f);
                            data.addCell(note1);
                            data.addCell(note2);
                            data.addCell(note3);
                            data.addCell(note4);
                            final PdfPTable table = new PdfPTable(30);
                            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.getDefaultCell().setFixedHeight(50);
                            table.setTotalWidth(PageSize.A4.getWidth());
                            table.setWidthPercentage(100);
                            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                            PdfPCell cell = new PdfPCell(new Phrase("No", a));
                            cell.setRowspan(2);
                            table.addCell(cell);
                            PdfPCell cell3 = new PdfPCell(new Phrase("Kom", a));
                            cell3.setRowspan(2);
                            table.addCell(cell3);
                            PdfPCell cell4 = new PdfPCell(new Phrase("Var", a));
                            cell4.setRowspan(2);
                            table.addCell(cell4);
                            PdfPCell cell5 = new PdfPCell(new Phrase("LPertanaman (Ha)", a));
                            cell5.setRowspan(2);
                            table.addCell(cell5);
                            PdfPCell cell6 = new PdfPCell(new Phrase("OPT", a));
                            cell6.setRowspan(2);
                            table.addCell(cell6);
                            PdfPCell cell7 = new PdfPCell(new Phrase("Luas Sisa Serangan (Ha)", a));
                            cell7.setColspan(5);
                            table.addCell(cell7);
                            PdfPCell cell71 = new PdfPCell(new Phrase("LSem (Ha)", a));
                            cell71.setRowspan(2);
                            table.addCell(cell71);
                            PdfPCell cell72 = new PdfPCell(new Phrase("LPn (Ha)", a));
                            cell72.setRowspan(2);
                            table.addCell(cell72);
                            PdfPCell cell8 = new PdfPCell(new Phrase("Luas Tambah Serangan (Ha)", a));
                            cell8.setColspan(5);
                            table.addCell(cell8);
                            PdfPCell cell9 = new PdfPCell(new Phrase("Luas Pengendalian (Ha)", a));
                            cell9.setColspan(5);
                            table.addCell(cell9);
                            PdfPCell cell10 = new PdfPCell(new Phrase("Luas Keadaan Serangan (Ha)", a));
                            cell10.setColspan(5);
                            table.addCell(cell10);
                            PdfPCell cell11 = new PdfPCell(new Phrase("Frek (kali)", a));
                            cell11.setColspan(2);
                            table.addCell(cell11);
                            PdfPCell cell12 = new PdfPCell(new Phrase("LWas (Ha)", a));
                            cell12.setRowspan(2);
                            table.addCell(cell12);
                            cell7 = new PdfPCell(new Phrase("R", a));
                            cell7.setColspan(1);
                            table.addCell(cell7);
                            cell7 = new PdfPCell(new Phrase("S", a));
                            cell7.setColspan(1);
                            table.addCell(cell7);
                            cell7 = new PdfPCell(new Phrase("B", a));
                            cell7.setColspan(1);
                            table.addCell(cell7);
                            cell7 = new PdfPCell(new Phrase("P", a));
                            cell7.setColspan(1);
                            table.addCell(cell7);
                            cell7 = new PdfPCell(new Phrase("J", a));
                            cell7.setColspan(1);
                            table.addCell(cell7);
                            cell8 = new PdfPCell(new Phrase("R", a));
                            cell8.setColspan(1);
                            table.addCell(cell8);
                            cell8 = new PdfPCell(new Phrase("S", a));
                            cell8.setColspan(1);
                            table.addCell(cell8);
                            cell8 = new PdfPCell(new Phrase("B", a));
                            cell8.setColspan(1);
                            table.addCell(cell8);
                            cell8 = new PdfPCell(new Phrase("P", a));
                            cell8.setColspan(1);
                            table.addCell(cell8);
                            cell8 = new PdfPCell(new Phrase("J", a));
                            cell8.setColspan(1);
                            table.addCell(cell8);
                            cell9 = new PdfPCell(new Phrase("K", a));
                            cell9.setColspan(1);
                            table.addCell(cell9);
                            cell9 = new PdfPCell(new Phrase("N", a));
                            cell9.setColspan(1);
                            table.addCell(cell9);
                            cell9 = new PdfPCell(new Phrase("E", a));
                            cell9.setColspan(1);
                            table.addCell(cell9);
                            cell9 = new PdfPCell(new Phrase("CL", a));
                            cell9.setColspan(1);
                            table.addCell(cell9);
                            cell9 = new PdfPCell(new Phrase("J", a));
                            cell9.setColspan(1);
                            table.addCell(cell9);
                            cell10 = new PdfPCell(new Phrase("R", a));
                            cell10.setColspan(1);
                            table.addCell(cell10);
                            cell10 = new PdfPCell(new Phrase("S", a));
                            cell10.setColspan(1);
                            table.addCell(cell10);
                            cell10 = new PdfPCell(new Phrase("B", a));
                            cell10.setColspan(1);
                            table.addCell(cell10);
                            cell10 = new PdfPCell(new Phrase("P", a));
                            cell10.setColspan(1);
                            table.addCell(cell10);
                            cell10 = new PdfPCell(new Phrase("J", a));
                            cell10.setColspan(1);
                            table.addCell(cell10);
                            cell11 = new PdfPCell(new Phrase("K", a));
                            cell11.setColspan(1);
                            table.addCell(cell11);
                            cell11 = new PdfPCell(new Phrase("N", a));
                            cell11.setColspan(1);
                            table.addCell(cell11);
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int nomor = i + 1;
                                String kecamatan = object.getString("kecamatan");
                                String desa = object.getString("desa");
                                periode_pengamatan = object.getString("periode");
                                luas_tanaman = object.getString("luas_tanaman");
                                umur_tanaman = object.getString("umur_tanaman");
                                jenis_opt = object.getString("jenis_opt");
                                String jumlah_sisa = object.getString("jumlah_sisa");
                                String jumlah_tambah = object.getString("jumlah_tambah");
                                String jumlah_keadaan = object.getString("jumlah_keadaan");
                                luas_sembuh = object.getString("luas_terkendali");
                                luas_tambah_serang = object.getString("luas_tambah");
                                String luas_waspada = object.getString("luas_waspada");
                                luas_panen = object.getString("luas_panen");
                                String kimia = object.getString("luas_kimia");
                                String nabati = object.getString("luas_nabati");
                                String eradikasi = object.getString("luas_eradikasi");
                                String cara_lain = object.getString("luas_cara_lain");
                                String jumlah_pengendalian = object.getString("jumlah_pengendalian");
                                tanggal_intensitas = object.getString("tanggal_laporan");
                                String frek_kimia = object.getString("frek_kimia");
                                String frek_nabati = object.getString("frek_nabati");
                                String sisa_ringan = object.getString("sisa_ringan");
                                String sisa_sedang = object.getString("sisa_sedang");
                                String sisa_berat = object.getString("sisa_berat");
                                String sisa_puso = object.getString("sisa_puso");
                                String intensitas_tambah = object.getString("intensitas_tambah");
                                String tambah_ringan = object.getString("tambah_ringan");
                                String tambah_sedang = object.getString("tambah_sedang");
                                String tambah_berat = object.getString("tambah_berat");
                                String tambah_puso = object.getString("tambah_puso");
                                String keadaan_ringan = object.getString("keadaan_ringan");
                                String keadaan_sedang = object.getString("keadaan_sedang");
                                String keadaan_berat = object.getString("keadaan_berat");
                                String keadaan_puso = object.getString("keadaan_puso");
                                petugas_pengamatan = object.getString("petugas");

                                cell = new PdfPCell(new Phrase(String.valueOf(nomor)));
                                table.addCell(cell);

                                cell3 = new PdfPCell(new Phrase(komoditas));
                                table.addCell(cell3);

                                cell4 = new PdfPCell(new Phrase(varietas));
                                table.addCell(cell4);

                                cell5 = new PdfPCell(new Phrase(luas_tanaman));
                                table.addCell(cell5);

                                cell6 = new PdfPCell(new Phrase(jenis_opt));
                                table.addCell(cell6);

                                cell7 = new PdfPCell(new Phrase(sisa_ringan));
                                table.addCell(cell7);

                                cell7 = new PdfPCell(new Phrase(sisa_sedang));
                                table.addCell(cell7);

                                cell7 = new PdfPCell(new Phrase(sisa_berat));
                                table.addCell(cell7);

                                cell7 = new PdfPCell(new Phrase(sisa_puso));
                                table.addCell(cell7);

                                cell7 = new PdfPCell(new Phrase(jumlah_sisa));
                                table.addCell(cell7);

                                cell71 = new PdfPCell(new Phrase(luas_sembuh));
                                table.addCell(cell71);

                                cell72 = new PdfPCell(new Phrase(luas_panen));
                                table.addCell(cell72);

                                cell8 = new PdfPCell(new Phrase(tambah_ringan));
                                table.addCell(cell8);

                                cell8 = new PdfPCell(new Phrase(tambah_sedang));
                                table.addCell(cell8);

                                cell8 = new PdfPCell(new Phrase(tambah_berat));
                                table.addCell(cell8);

                                cell8 = new PdfPCell(new Phrase(tambah_puso));
                                table.addCell(cell8);

                                cell8 = new PdfPCell(new Phrase(jumlah_tambah));
                                table.addCell(cell8);

                                cell9 = new PdfPCell(new Phrase(kimia));
                                table.addCell(cell9);

                                cell9 = new PdfPCell(new Phrase(nabati));
                                table.addCell(cell9);

                                cell9 = new PdfPCell(new Phrase(eradikasi));
                                table.addCell(cell9);

                                cell9 = new PdfPCell(new Phrase(cara_lain));
                                table.addCell(cell9);

                                cell9 = new PdfPCell(new Phrase(jumlah_pengendalian));
                                table.addCell(cell9);

                                cell10 = new PdfPCell(new Phrase(keadaan_ringan));
                                table.addCell(cell10);

                                cell10 = new PdfPCell(new Phrase(keadaan_sedang));
                                table.addCell(cell10);

                                cell10 = new PdfPCell(new Phrase(keadaan_berat));
                                table.addCell(cell10);

                                cell10 = new PdfPCell(new Phrase(keadaan_puso));
                                table.addCell(cell10);

                                cell10 = new PdfPCell(new Phrase(jumlah_keadaan));
                                table.addCell(cell10);

                                cell11 = new PdfPCell(new Phrase(frek_kimia));
                                table.addCell(cell11);

                                cell11 = new PdfPCell(new Phrase(frek_nabati));
                                table.addCell(cell11);

                                cell12 = new PdfPCell(new Phrase(luas_waspada));
                                table.addCell(cell12);

                            }
                            PdfWriter.getInstance(document, output);
                            document.open();
                            dateFormat = new SimpleDateFormat("dd");
                            String hari = dateFormat.format(calendar.getTime());
                            SimpleDateFormat month = new SimpleDateFormat("MMMM");
                            String bulan = month.format(calendar.getTime());

                            SimpleDateFormat years = new SimpleDateFormat("yyyy");
                            String tahun = years.format(calendar.getTime());

                            Font b = new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, Font.BOLD, BaseColor.BLACK);
                            Font c = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.BOLD, BaseColor.BLACK);
                            Paragraph title = new Paragraph("Laporan Setengah Bulanan", b);
                            title.setAlignment(Element.ALIGN_CENTER);
                            Paragraph subTitle;
                            if (periode_pengamatan.equals("Periode 1-15")) {
                                subTitle = new Paragraph("Periode 1-"+hari+" "+ bulan +" "+ tahun, c);
                                subTitle.setAlignment(Element.ALIGN_CENTER);
                            } else {
                                subTitle = new Paragraph("Periode 16-"+hari+" "+bulan +" "+ tahun, c);
                                subTitle.setAlignment(Element.ALIGN_CENTER);
                            }
                            document.add(title);
                            document.add(subTitle);
                            document.add(data);
                            document.add(table);
                            document.add(new Paragraph("POPT-PHP", a));
//                                                document.add(image);
                            document.add(new Paragraph(petugas_pengamatan, a));
                            document.close();
                            previewPdf();
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DetailLapor.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailLapor.this).add(stringRequest);

    }

    private void previewPdf() {
        Intent intent = new Intent(DetailLapor.this, PdfView.class);
        intent.putExtra("setengah", "Laporan Setengah Bulan "+tanggal_intensitas+".pdf");
        intent.putExtra("id_hsl_stgh", id_hsl_stgh);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length <= 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(DetailLapor.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createPdfWrapper() throws FileNotFoundException, DocumentException {
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
//            getIdStghBln();
            getID();
        }
    }

    private void getID() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String tanggal = simpleDateFormat.format(calendar.getTime());
        String username = Common.currentUser.getUsername();
        String url = Constants.ROOT_URL+"Stgh_Bln?username="+username+"&periode="+periode_pengamatan+"&date="+tanggal+"&desa="+desa+"&opt="+opt;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                id_hsl_stgh = object.getString("id_hsl_stgh");

                                getDatatoPDF();

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                    buildRecyclerViewDataHarian();
                    Toast.makeText(DetailLapor.this, "Data Belum Disimpan", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailLapor.this).add(stringRequest);
    }


    private void getData() {
        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        opt = getIntent().getStringExtra("opt");
        kabupaten = getIntent().getStringExtra("kabupaten");
        kecamatan = getIntent().getStringExtra("kecamatan");
        desa = getIntent().getStringExtra("desa");
        umur_tanaman = getIntent().getStringExtra("umur_tanaman");
        luas_tambah = getIntent().getStringExtra("luas_tambah");
        periode_pengamatan = getIntent().getStringExtra("periode_pengamatan");
        luas_tanaman = getIntent().getStringExtra("luas_tanaman");
        komoditas = getIntent().getStringExtra("komoditas");
        varietas = getIntent().getStringExtra("varietas");
        intensitas_tambah = getIntent().getStringExtra("intensitas_tambah");
        luas_waspada = getIntent().getStringExtra("luas_waspada");

        getSisaSerangan();

//        id_intensitas = getIntent().getStringExtra("id_intensitas");
//        String urlLaporan = Constants.ROOT_URL+"intensitas?id="+id_intensitas;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray array = new JSONArray(response);
//                            for (int i = 0; i<array.length(); i++){
//                                JSONObject object = array.getJSONObject(i);
//
//                                String desa = object.getString("desa");
//                                String blok = object.getString("blok");
//                                String periode_pengamatan = object.getString("periode_pengamatan");
//                                String jenis_opt = object.getString("jenis_opt");
//                                String luas_tambah_serang = object.getString("luas_tambah_serang");
//                                String intensitas_tambah = object.getString("intensitas_tambah");
//                                String id = object.getString("id_hasil_pengamatan");
//
//                                IntensitasModel intensitasModel = new IntensitasModel();
//                                intensitasModel.setLuas_tambah_serang(luas_tambah_serang);
//                                intensitasModel.setIntensitas_tambah(intensitas_tambah);
//                                intensitasModel.setPeriode_pengamatan(periode_pengamatan);
//                                intensitasModel.setDesa(desa);
//                                intensitasModel.setBlok(blok);
//                                intensitasModel.setJenis_opt(jenis_opt);
//                                intensitasModel.setId_hasil_pengamatan(id);
//                                Common.intensitasModel = intensitasModel;
//
//
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(DetailLapor.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        Volley.newRequestQueue(DetailLapor.this).add(stringRequest);
    }

    private void getSisaSerangan() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        String bulan = date.substring(5,7);
        String tahun = date.substring(0,4);
        if (periode_pengamatan.equals("Periode 1-15")){
            periode = "Periode 16-31";
            int bulan1 = Integer.parseInt(bulan) - 1;
            int tahun1 = Integer.parseInt(tahun);
            String bul = "";
            if (bulan1 != 10 && bulan1 != 11 && bulan1 != 12){
                bul = "0" + String.valueOf(bulan1);

            }else {
                bul = String.valueOf(bulan1);
            }
            String substring = String.valueOf(tahun1) + "-" + bul;
            String urlLaporan = Constants.ROOT_URL+"Update_Inten?periode="+periode+"&date="+substring+"&desa="+desa+"&opt="+opt;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i<array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);

                                    luas_keadaan_sisa = object.getString("jumlah_keadaan");
                                    k_ringan = object.getString("keadaan_ringan");
                                    k_sedang = object.getString("keadaan_sedang");
                                    k_berat = object.getString("keadaan_berat");
                                    k_puso = object.getString("keadaan_puso");
                                    intensitas_keadaan_sisa = object.getString("intensitas_keadaan");
                                    luas_tambah_sebelumnya = object.getString("jumlah_tambah");
                                    t_ringan = object.getString("tambah_ringan");
                                    t_sedang = object.getString("tambah_sedang");
                                    t_berat = object.getString("tambah_berat");
                                    t_puso = object.getString("tambah_puso");
                                    luas_sisa_sebelumnya = object.getString("jumlah_sisa");
                                    s_ringan = object.getString("sisa_ringan");
                                    s_sedang = object.getString("sisa_sedang");
                                    s_berat = object.getString("sisa_berat");
                                    s_puso = object.getString("sisa_puso");
                                    panen_sebelumnya = object.getString("luas_panen");
                                    sembuh_sebelumnya = object.getString("luas_sembuh");
                                    kimia_seb = object.getString("luas_kimia");
                                    nabati_seb = object.getString("luas_nabati");
                                    eradikasi_seb = object.getString("luas_eradikasi");
                                    cara_lain_seb = object.getString("luas_cara_lain");
                                    frek_kimia_seb = object.getString("frek_kimia");
                                    frek_nabati_seb = object.getString("frek_nabati");
                                    luas_pengendalian_seb = object.getString("jumlah_pengendalian");
                                    getDataAll();


                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    luas_keadaan_sisa = "0";
                    intensitas_keadaan_sisa = "0";
                    k_ringan = "0";
                    k_sedang = "0";
                    k_berat = "0";
                    k_puso = "0";
                    luas_tambah_sebelumnya = "0";
                    t_ringan = "0";
                    t_sedang = "0";
                    t_berat = "0";
                    t_puso = "0";
                    luas_sisa_sebelumnya = "0";
                    s_ringan = "0";
                    s_sedang = "0";
                    s_berat = "0";
                    s_puso = "0";
                    panen_sebelumnya = "0";
                    sembuh_sebelumnya = "0";
                    kimia_seb = "0";
                    nabati_seb = "0";
                    eradikasi_seb = "0";
                    cara_lain_seb = "0";
                    frek_kimia_seb = "0";
                    frek_nabati_seb = "0";
                    luas_pengendalian_seb = "0";
                    getDataAll();
//                    buildRecyclerViewDataHarian();
//                    Toast.makeText(DetailLapor.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(DetailLapor.this).add(stringRequest);
        }else {
            periode = "Periode 1-15";
            String substring = date.substring(0,7);

            String urlLaporan = Constants.ROOT_URL+"Update_Inten?periode="+periode+"&date="+substring+"&desa="+desa+"&opt="+opt;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i<array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);

                                    luas_keadaan_sisa = object.getString("jumlah_keadaan");
                                    k_ringan = object.getString("keadaan_ringan");
                                    k_sedang = object.getString("keadaan_sedang");
                                    k_berat = object.getString("keadaan_berat");
                                    k_puso = object.getString("keadaan_puso");
                                    intensitas_keadaan_sisa = object.getString("intensitas_keadaan");
                                    luas_tambah_sebelumnya = object.getString("jumlah_tambah");
                                    t_ringan = object.getString("tambah_ringan");
                                    t_sedang = object.getString("tambah_sedang");
                                    t_berat = object.getString("tambah_berat");
                                    t_puso = object.getString("tambah_puso");
                                    luas_sisa_sebelumnya = object.getString("jumlah_sisa");
                                    s_ringan = object.getString("sisa_ringan");
                                    s_sedang = object.getString("sisa_sedang");
                                    s_berat = object.getString("sisa_berat");
                                    s_puso = object.getString("sisa_puso");
                                    panen_sebelumnya = object.getString("luas_panen");
                                    sembuh_sebelumnya = object.getString("luas_sembuh");
                                    kimia_seb = object.getString("luas_kimia");
                                    nabati_seb = object.getString("luas_nabati");
                                    eradikasi_seb = object.getString("luas_eradikasi");
                                    cara_lain_seb = object.getString("luas_cara_lain");
                                    frek_kimia_seb = object.getString("frek_kimia");
                                    frek_nabati_seb = object.getString("frek_nabati");
                                    luas_pengendalian_seb = object.getString("jumlah_pengendalian");

                                    getDataAll();

//                                    buildRecyclerViewDataHarian();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    luas_keadaan_sisa = "0";
                    intensitas_keadaan_sisa = "0";
                    k_ringan = "0";
                    k_sedang = "0";
                    k_berat = "0";
                    k_puso = "0";
                    luas_tambah_sebelumnya = "0";
                    t_ringan = "0";
                    t_sedang = "0";
                    t_berat = "0";
                    t_puso = "0";
                    luas_sisa_sebelumnya = "0";
                    s_ringan = "0";
                    s_sedang = "0";
                    s_berat = "0";
                    s_puso = "0";
                    panen_sebelumnya = "0";
                    sembuh_sebelumnya = "0";
                    kimia_seb = "0";
                    nabati_seb = "0";
                    eradikasi_seb = "0";
                    cara_lain_seb = "0";
                    frek_kimia_seb = "0";
                    frek_nabati_seb = "0";
                    luas_pengendalian_seb = "0";
//                    buildRecyclerViewDataHarian();
                    getDataAll();
                }
            });
            Volley.newRequestQueue(DetailLapor.this).add(stringRequest);
        }

    }

    private void getDataByID() {

    }


//    private void buildRecyclerViewDataHarian() {
//        recyclerView = (RecyclerView) findViewById(R.id.rvData);
//        recyclerView.setHasFixedSize(true);
//        layoutManager1 = new LinearLayoutManager(DetailLapor.this);
//        recyclerView.setLayoutManager(layoutManager1);
//        listMa = new ArrayList<>();
//
//        getDataMa();
//    }

//    private void getDataMa() {
//        final String id= Common.intensitasModel.getId_hasil_pengamatan();
//        String opt = Common.intensitasModel.getJenis_opt();
//        String url = Constants.ROOT_URL+"Sum_Ma?opt="+opt+"&id_hasil="+id;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray array = new JSONArray(response);
//                            for (int i = 0; i<array.length(); i++){
//                                JSONObject object = array.getJSONObject(i);
//
//                                ma = object.getString("ma");
//
//                                String url = Constants.ROOT_URL+"Sum_Ma?ma="+ma+"&id_hasil="+id;
//
//                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                                        new Response.Listener<String>() {
//                                            @Override
//                                            public void onResponse(String response) {
//                                                try {
//                                                    JSONArray array = new JSONArray(response);
//                                                    for (int i = 0; i<array.length(); i++){
//                                                        JSONObject object = array.getJSONObject(i);
//
//                                                        jumlah_ma = object.getString("jumlah");
//                                                        musuh_alami = object.getString("ma");
//
//                                                        float total_ma = Float.parseFloat(jumlah_ma)/30;
//
//                                                        ModelMa modelMa = new ModelMa();
//                                                        modelMa.setMa(musuh_alami);
//                                                        modelMa.setJumlah(String.valueOf(total_ma));
//                                                        listMa.add(modelMa);
//
//                                                        getDataAll();
//
//                                                    }
//                                                }catch (Exception e){
//                                                    e.printStackTrace();
//                                                }
//                                                adapterMA = new AdapterMa(DetailLapor.this, listMa);
//                                                recyclerView.setAdapter(adapterMA);
//
//                                            }
//                                        }, new Response.ErrorListener() {
//                                    @Override
//                                    public void onErrorResponse(VolleyError error) {
////                                        Toast.makeText(InputHarian.this, error.toString(), Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                                Volley.newRequestQueue(DetailLapor.this).add(stringRequest);
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                jumlah_ma = "0";
//                musuh_alami = "tidal ditemukan";
////                Toast.makeText(InputHarian.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        Volley.newRequestQueue(DetailLapor.this).add(stringRequest);
//    }

    private void getDataAll() {
        String url = Constants.ROOT_URL + "Stgh_Bln?id="+id_hsl_stgh;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dateFormat = new SimpleDateFormat("dd");
                            String hari = dateFormat.format(calendar.getTime());
                            SimpleDateFormat month = new SimpleDateFormat("MMMM");
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                progressDialog.dismiss();

                                String luas_sembuh = object.getString("luas_terkendali");
                                String luas_panen = object.getString("luas_panen");
                                String kimia = object.getString("luas_kimia");
                                String nabati = object.getString("luas_nabati");
                                String eradikasi = object.getString("luas_eradikasi");
                                String cara_lain = object.getString("luas_cara_lain");
                                String frek_kimia = object.getString("frek_kimia");
                                String frek_nabati = object.getString("frek_nabati");
                                String sisa_ringan = object.getString("sisa_ringan");
                                String sisa_sedang = object.getString("sisa_sedang");
                                String sisa_berat = object.getString("sisa_berat");
                                String sisa_puso = object.getString("sisa_puso");
                                String status_pelaporan = object.getString("status_pelaporan");
                                String approval_kab = object.getString("approval_kab");
                                String approval_satpel = object.getString("approval_satpel");
                                String approval_provinsi = object.getString("approval_prov");
                                String kab_to_popt = object.getString("kab_to_popt");
                                String satpel_to_popt = object.getString("satpel_to_popt");
                                String prov_to_popt = object.getString("prov_to_popt");
                                String jumlah_sisa = object.getString("jumlah_sisa");
                                String keadaan_ringan = object.getString("keadaan_ringan");
                                String keadaan_sedang = object.getString("keadaan_sedang");
                                String keadaan_berat = object.getString("keadaan_berat");
                                String keadaan_puso = object.getString("keadaan_puso");
                                jumlah_keadaan = object.getString("jumlah_keadaan");

                                String bulan = month.format(calendar.getTime());
                                if (periode_pengamatan.equals("Periode 1-15")) {
                                    txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Laporan Setengah Bulan (Tanggal 1- </b></font>" + hari + " " + bulan + ")"));
                                } else {
                                    txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Laporan Setengah Bulan (Tanggal 16- </b></font>" + hari + " " + bulan + ")"));
                                }
                                txtOPT.setText(Html.fromHtml("<font color='#000000'><b>OPT: </b></font>" + opt));
                                txtLuasWaspada.setText(Html.fromHtml("<font color='#000000'><b>Luas Waspada: </b></font>" + luas_waspada));
                                txtMusim.setText(Html.fromHtml("<font color='#000000'><b>Musim: </b></font>" + musim_tanam));
                                txtTR.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + tambah_ringan));
                                txtTS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + tambah_sedang));
                                txtTB.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + tambah_berat));
                                txtTP.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + tambah_puso));
                                txtJumlahTambah.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_tambah));
                                txtSR.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + k_ringan));
                                txtSS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + k_sedang));
                                txtSB.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + k_berat));
                                txtSP.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + k_puso));
                                txtJumlahSisa.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_keadaan_sisa));
                                txtKomoditas.setText(Html.fromHtml("<font color='#000000'><b>Komoditas: </b></font>" + komoditas));
                                txtKecamatan.setText(Html.fromHtml("<font color='#000000'><b>Kec: </b></font>" + kecamatan));
                                txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Desa: </b></font>" + desa));
                                txtVarietas.setText(Html.fromHtml("<font color='#000000'><b>Varietas: </b></font>" + varietas));
                                txtPeriode.setText(Html.fromHtml(periode_pengamatan));
                                txtLuasHamparan.setText(Html.fromHtml("<font color='#000000'><b>Luas Pertanaman: </b></font>" + luas_tanaman + " Ha"));
                                txtUmurTanaman.setText(Html.fromHtml("<font color='#000000'><b>Umur Tanaman: </b></font>" + umur_tanaman + " HST"));
                                edtRingan.setText(sisa_ringan);
                                Toast.makeText(DetailLapor.this, sisa_ringan, Toast.LENGTH_SHORT).show();
                                edtSedang.setText(sisa_sedang);
                                edtBerat.setText(sisa_berat);
                                edtPuso.setText(sisa_puso);
                                edtKimia.setText(kimia);
                                edtNabati.setText(nabati);
                                edtEradikasi.setText(eradikasi);
                                edtCaraLain.setText(cara_lain);
                                edtFrekKimia.setText(frek_kimia);
                                edtFrekNabati.setText(frek_nabati);
                                edtLuasSembuh.setText(luas_sembuh);
                                edtLuasPanen.setText(luas_panen);
                                txtPelaporan.setText(Html.fromHtml("<font color='#000000'><b>Pelaporan: </b></font>" + status_pelaporan));
                                txtApprovalKab.setText(Html.fromHtml("<font color='#000000'><b>Val Kortikab: </b></font>" + approval_kab));
                                txtApprovalSatpel.setText(Html.fromHtml("<font color='#000000'><b>Val SatPel: </b></font>" + approval_satpel));
                                txtApprovalProv.setText(Html.fromHtml("<font color='#000000'><b>Val BPTPH: </b></font>" + approval_provinsi));
                                msg_kab.setText(Html.fromHtml("<font color='#000000'><b>Kortikab: </b></font>" + kab_to_popt));
                                msg_prov.setText(Html.fromHtml("<font color='#000000'><b>Val BPTPH: </b></font>" + prov_to_popt));
                                msg_satpel.setText(Html.fromHtml("<font color='#000000'><b>Val BPTPH: </b></font>" + satpel_to_popt));
                                sebelumnya.setText(Html.fromHtml("<font color='#000000'><b>Laporan : </b></font>" + periode));
                                sekarang.setText(Html.fromHtml("<font color='#000000'><b>Laporan : </b></font>" + periode_pengamatan));
                                txtLuasPanen.setText(Html.fromHtml("<font color='#000000'><b>L Panen: </b></font>" + panen_sebelumnya));
                                txtLuasSembuh.setText(Html.fromHtml("<font color='#000000'><b>L Sembuh: </b></font>" + sembuh_sebelumnya));
                                txtSRS.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + s_ringan));
                                txtSSS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + s_sedang));
                                txtSBS.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + s_berat));
                                txtSPS.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + s_puso));
                                txtJumlahSisaS.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_sisa_sebelumnya));
                                txtTRS.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + t_ringan));
                                txtTSS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + t_sedang));
                                txtTBS.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + t_berat));
                                txtTPS.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + t_puso));
                                txtJumlahTambahS.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_tambah_sebelumnya));
                                txtKimia.setText(Html.fromHtml("<font color='#000000'><b>K: </b></font>" + kimia_seb));
                                txtNabati.setText(Html.fromHtml("<font color='#000000'><b>N: </b></font>" + nabati_seb));
                                txtEradikasi.setText(Html.fromHtml("<font color='#000000'><b>E: </b></font>" + eradikasi_seb));
                                txtCaraLain.setText(Html.fromHtml("<font color='#000000'><b>CL: </b></font>" + cara_lain_seb));
                                txtJumlahPengendalian.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_pengendalian_seb));
                                txtFrekKimia.setText(Html.fromHtml("<font color='#000000'><b>Frek Kimia: </b></font>" + frek_kimia_seb));
                                txtFrekNabati.setText(Html.fromHtml("<font color='#000000'><b>Frek Nabati: </b></font>" + frek_nabati_seb));
                                txtKR.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + keadaan_ringan));
                                txtKS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + keadaan_sedang));
                                txtKB.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + keadaan_berat));
                                txtKP.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + keadaan_puso));
                                txtJumlahKeadaan.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + jumlah_keadaan));

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                dateFormat = new SimpleDateFormat("dd");
                String hari = dateFormat.format(calendar.getTime());
                SimpleDateFormat month = new SimpleDateFormat("MMMM");
                String bulan = month.format(calendar.getTime());
                if (periode_pengamatan.equals("Periode 1-15")){
                    txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Laporan Setengah Bulan (Tanggal 1- </b></font>" + hari+" "+bulan+")"));
                }else {
                    txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Laporan Setengah Bulan (Tanggal 16- </b></font>" + hari+" "+bulan+")"));
                }
                txtOPT.setText(Html.fromHtml("<font color='#000000'><b>OPT: </b></font>" +opt));
                txtLuasWaspada.setText(Html.fromHtml("<font color='#000000'><b>Luas Waspada: </b></font>" +luas_waspada));
                txtMusim.setText(Html.fromHtml("<font color='#000000'><b>Musim: </b></font>" +musim_tanam));
                txtTR.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" +tambah_ringan));
                txtTS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" +tambah_sedang));
                txtTB.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" +tambah_berat));
                txtTP.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" +tambah_puso));
                txtJumlahTambah.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" +luas_tambah));
                txtSR.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" +k_ringan));
                txtSS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" +k_sedang));
                txtSB.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" +k_berat));
                txtSP.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" +k_puso));
                txtJumlahSisa.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" +luas_keadaan_sisa));
                txtKomoditas.setText(Html.fromHtml("<font color='#000000'><b>Komoditas: </b></font>" + komoditas));
                txtKecamatan.setText(Html.fromHtml("<font color='#000000'><b>Kec: </b></font>" + kecamatan));
                txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Desa: </b></font>" + desa));
                txtVarietas.setText(Html.fromHtml("<font color='#000000'><b>Varietas: </b></font>" + varietas));
                txtPeriode.setText(Html.fromHtml(periode_pengamatan));
                txtLuasHamparan.setText(Html.fromHtml("<font color='#000000'><b>Luas Pertanaman: </b></font>" + luas_tanaman + " Ha"));
                txtUmurTanaman.setText(Html.fromHtml("<font color='#000000'><b>Umur Tanaman: </b></font>" + umur_tanaman + " HST"));
                txtPelaporan.setText(Html.fromHtml("<font color='#000000'><b>Pelaporan: Belum</b></font>"));
                txtApprovalKab.setText(Html.fromHtml("<font color='#000000'><b>Val Kortikab: Belum</b></font>"));
                txtApprovalSatpel.setText(Html.fromHtml("<font color='#000000'><b>Val SatPel: Belum</b></font>"));
                txtApprovalProv.setText(Html.fromHtml("<font color='#000000'><b>Val BPTPH: Belum</b></font>"));
                msg_kab.setText(Html.fromHtml("<font color='#000000'><b>Kortikab: </b></font>" ));
                msg_prov.setText(Html.fromHtml("<font color='#000000'><b>Val BPTPH: </b></font>"));
                msg_satpel.setText(Html.fromHtml("<font color='#000000'><b>Val BPTPH: </b></font>"));
                sebelumnya.setText(Html.fromHtml("<font color='#000000'><b>Laporan : </b></font>" + periode));
                sekarang.setText(Html.fromHtml("<font color='#000000'><b>Laporan : </b></font>" + periode_pengamatan));
                txtLuasPanen.setText(Html.fromHtml("<font color='#000000'><b>L Panen: </b></font>" + panen_sebelumnya));
                txtLuasSembuh.setText(Html.fromHtml("<font color='#000000'><b>L Sembuh: </b></font>" + sembuh_sebelumnya));
                txtSRS.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + s_ringan));
                txtSSS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + s_sedang));
                txtSBS.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + s_berat));
                txtSPS.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + s_puso));
                txtJumlahSisaS.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_sisa_sebelumnya));
                txtTRS.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + t_ringan));
                txtTSS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + t_sedang));
                txtTBS.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + t_berat));
                txtTPS.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + t_puso));
                txtJumlahTambahS.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_tambah_sebelumnya));
                txtKimia.setText(Html.fromHtml("<font color='#000000'><b>K: </b></font>" + kimia_seb));
                txtNabati.setText(Html.fromHtml("<font color='#000000'><b>N: </b></font>" + nabati_seb));
                txtEradikasi.setText(Html.fromHtml("<font color='#000000'><b>E: </b></font>" + eradikasi_seb));
                txtCaraLain.setText(Html.fromHtml("<font color='#000000'><b>CL: </b></font>" + cara_lain_seb));
                txtJumlahPengendalian.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_pengendalian_seb));
                txtFrekKimia.setText(Html.fromHtml("<font color='#000000'><b>Frek Kimia: </b></font>" + frek_kimia_seb));
                txtFrekNabati.setText(Html.fromHtml("<font color='#000000'><b>Frek Nabati: </b></font>" + frek_nabati_seb));
                txtKR.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>"));
                txtKS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>"));
                txtKB.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>"));
                txtKP.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>"));
                txtJumlahKeadaan.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>"));

            }
        });
        Volley.newRequestQueue(DetailLapor.this).add(stringRequest);

    }


//        id_intensitas = getIntent().getStringExtra("id_intensitas");
//        String urlLaporan = Constants.ROOT_URL+"intensitas?id="+id_intensitas;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray array = new JSONArray(response);
//                            for (int i = 0; i<array.length(); i++){
//                                JSONObject object = array.getJSONObject(i);
//
//                                String kecamatan = object.getString("kecamatan");
//                                String wilayah_pengamatan = object.getString("wilayah_pengamatan");
//                                String kabupaten = object.getString("kabupaten");
//                                String desa = object.getString("desa");
//                                String blok = object.getString("blok");
//                                String periode_pengamatan = object.getString("periode_pengamatan");
//                                String luas_tanaman = object.getString("luas_tanaman");
//                                String umur_tanaman = object.getString("umur_tanaman");
//                                String jenis_opt = object.getString("jenis_opt");
//                                String total_sembuh = object.getString("luas_sembuh");
//                                String luas_sisa_serang = object.getString("luas_sisa_serang");
//                                String serang_ringan = object.getString("serang_ringan");
//                                String serang_sedang = object.getString("serang_sedang");
//                                String serang_berat = object.getString("serang_berat");
//                                String serang_puso = object.getString("serang_puso");
//                                String intensitas_serang = object.getString("intensitas_serang");
//                                String luas_tambah_serang = object.getString("luas_tambah_serang");
//                                String intensitas_tambah = object.getString("intensitas_tambah");
//                                String puso_tambah_serang = object.getString("puso_tambah_serang");
//                                String jumlah_tambah_serang = object.getString("jumlah_tambah_serang");
//                                String luas_pengendalian = object.getString("luas_pengendalian");
//                                String luas_panen = object.getString("luas_panen");
//                                String kimia = object.getString("kimia");
//                                String nabati = object.getString("nabati");
//                                String eradikasi = object.getString("eradikasi");
//                                String cara_lain = object.getString("cara_lain");
//                                String luas_waspada = object.getString("luas_waspada");
//                                String tanggal_intensitas = object.getString("tanggal_intensitas");
//                                String frek_kimia = object.getString("frek_kimia");
//                                String frek_nabati = object.getString("frek_nabati");
//                                String rekomendasi = object.getString("rekomendasi");
//
//                                IntensitasModel intensitasModel = new IntensitasModel();
//                                intensitasModel.setLuas_tambah_serang(luas_tambah_serang);
//                                intensitasModel.setIntensitas_tambah(intensitas_tambah);
//                                intensitasModel.setPeriode_pengamatan(periode_pengamatan);
//                                intensitasModel.setDesa(desa);
//                                intensitasModel.setJenis_opt(jenis_opt);
//                                Common.intensitasModel = intensitasModel;
//
//
//                                txtlapor.setText(Html.fromHtml("<font color='#000000'><b>OPT: </b></font>" + jenis_opt));
//                                edtLuasSembuh.setText(total_sembuh);
//                                edtRingan.setText(serang_ringan);
//                                edtSedang.setText(serang_sedang);
//                                edtBerat.setText(serang_berat);
//                                edtPuso.setText(serang_puso);
//
//                                edtLuasPengendalian.setText(luas_pengendalian);
//                                edtLuasPanen.setText(luas_panen);
//                                edtKimia.setText(kimia);
//                                edtNabati.setText(nabati);
//                                edtEradikasi.setText(eradikasi);
//                                edtCaraLain.setText(cara_lain);
//
//                                edtIntensitasSerang.setText(intensitas_serang);
//                                edtFrekKimia.setText(frek_kimia);
//                                edtFrekNabati.setText(frek_nabati);
//                                edtRekomendasi.setText(rekomendasi);
//                                progressDialog.dismiss();
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(DetailLapor.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        Volley.newRequestQueue(DetailLapor.this).add(stringRequest);
//    }

    private void updateDataIntensitas() {
        progressDialog.setTitle("Insert Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        String url = Constants.ROOT_URL+"Simpan_Inten";
        final String luas_sembuh = edtLuasSembuh.getText().toString();
        final String serang_ringan = edtRingan.getText().toString();
        final String serang_sedang = edtSedang.getText().toString();
        final String serang_berat = edtBerat.getText().toString();
        final String serang_puso = edtPuso.getText().toString();
        final int luas_sisa_serang = Integer.parseInt(serang_ringan)+Integer.parseInt(serang_sedang)+Integer.parseInt(serang_berat)+Integer.parseInt(serang_puso);
        intensitas_serang = intensitas_keadaan_sisa;
        String luas_tambah_serangan = luas_tambah;
        final int luas_keadaan_serang = Integer.parseInt(luas_tambah_serangan) + luas_sisa_serang;
        String luas_tambah_serang = luas_tambah;
        String intensitas_tambah_serang = intensitas_tambah;
        float a = luas_sisa_serang*Float.parseFloat(intensitas_serang);
        float b = Float.parseFloat(luas_tambah_serang)*Float.parseFloat(intensitas_tambah_serang);
        float c = a+b;
        final float intensitas_keadaan = c/luas_keadaan_serang;
        final String luas_panen = edtLuasPanen.getText().toString();
        final String kimia = edtKimia.getText().toString();
        final String nabati = edtNabati.getText().toString();
        final String eradikasi = edtEradikasi.getText().toString();
        final String cara_lain = edtCaraLain.getText().toString();
        final String frek_kimia = edtFrekKimia.getText().toString();
        final String frek_nabati = edtFrekNabati.getText().toString();
        final int jumlah_pengendalian = Integer.parseInt(kimia)+Integer.parseInt(nabati)+Integer.parseInt(eradikasi)+Integer.parseInt(cara_lain);
        final String id_intensitas = getIntent().getStringExtra("id_intensitas");
//        final String rekomendasi = edtRekomendasi.getText().toString();
//        final String musim_tanam = edtMusimTanam.getText().toString();

        keadaan_ringan = Integer.parseInt(serang_ringan) + Integer.parseInt(tambah_ringan);
        keadaan_sedang = Integer.parseInt(serang_sedang)+ Integer.parseInt(tambah_sedang);
        keadaan_berat = Integer.parseInt(serang_berat)+ Integer.parseInt(tambah_berat);
        keadaan_puso = Integer.parseInt(serang_puso)+ Integer.parseInt(tambah_puso);

        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        String month = monthFormat.format(calendar.getTime());

        if (month.equals("04")||month.equals("05")||month.equals("06")||month.equals("07")||month.equals("08")||month.equals("09")){
            musim_tanam = "Kemarau";
        }else {
            musim_tanam = "Hujan";
        }

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        now = dateFormat.format(calendar.getTime());

        Date dt = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.DATE, 1);
        dt = calendar.getTime();
        final String datePlusOne = dateFormat.format(dt);

        Date hour = new Date();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(hour);
        calendar1.add(Calendar.HOUR, 12);
        hour = calendar1.getTime();
        final String datePlusHour = dateFormat.format(hour);


        int total = Integer.parseInt(luas_sembuh) + Integer.parseInt(luas_panen) + luas_sisa_serang;
        if (total == Integer.parseInt(luas_keadaan_sisa)){

            final String finalMusim_tanam = musim_tanam;
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("berhasil")){
                                progressDialog.dismiss();
                                Toast.makeText(DetailLapor.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
//                                Intent home = new Intent(DetailLapor.this, Lapor.class);
//                                startActivity(home);
                                finish();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(DetailLapor.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("provinsi", "Jawa Barat");
                    params.put("kabupaten", kabupaten);
                    params.put("kecamatan", kecamatan);
                    params.put("desa", desa);
                    params.put("periode", periode_pengamatan);
                    params.put("luas_tanaman", luas_tanaman);
                    params.put("umur_tanaman", umur_tanaman);
                    params.put("jenis_opt", opt);
                    params.put("komoditas", komoditas);
                    params.put("varietas", varietas);
                    params.put("luas_sisa", String.valueOf(luas_sisa_serang));
                    params.put("sisa_ringan", serang_ringan);
                    params.put("sisa_sedang", serang_sedang);
                    params.put("sisa_berat", serang_berat);
                    params.put("sisa_puso", serang_puso);
                    params.put("intensitas_sisa", intensitas_serang);
                    params.put("jumlah_sisa", String.valueOf(luas_sisa_serang));
                    params.put("luas_tambah", luas_tambah);
                    params.put("intensitas_tambah", intensitas_tambah);
                    params.put("jumlah_tambah", luas_tambah);
                    params.put("luas_keadaan", String.valueOf(luas_keadaan_serang));
                    params.put("keadaan_ringan",String.valueOf(keadaan_ringan));
                    params.put("keadaan_sedang",String.valueOf(keadaan_sedang));
                    params.put("keadaan_berat",String.valueOf(keadaan_berat));
                    params.put("keadaan_puso",String.valueOf(keadaan_puso));
                    params.put("jumlah_keadaan", String.valueOf(luas_keadaan_serang));
                    if (String.valueOf(intensitas_keadaan).contains("NaN")){
                        params.put("intensitas_keadaan", intensitas_serang);
                    }else {
                        params.put("intensitas_keadaan", String.valueOf(intensitas_keadaan));
                    }
                    params.put("luas_panen", luas_panen);
                    params.put("luas_terkendali", luas_sembuh);
                    params.put("luas_kimia", kimia);
                    params.put("luas_nabati", nabati);
                    params.put("luas_eradikasi", eradikasi);
                    params.put("luas_cara_lain", cara_lain);
                    params.put("jumlah_pengendalian", String.valueOf(jumlah_pengendalian));
                    params.put("musim_tanam", finalMusim_tanam);
                    params.put("luas_waspada", luas_waspada);
                    params.put("frek_kimia", frek_kimia);
                    params.put("frek_nabati", frek_nabati);
                    params.put("status_pelaporan", "Sudah");
                    params.put("batas_waktu_kab", datePlusHour);
                    params.put("batas_waktu_satpel", datePlusOne);
                    params.put("tanggal_laporan", now);
                    params.put("approval_kab", "Belum");
                    params.put("approval_satpel", "Belum");
                    params.put("approval_prov", "Belum");

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(DetailLapor.this);
            requestQueue.add(request);
        }else {
            progressDialog.dismiss();
            Toast.makeText(this, "Luas Sisa Serangan Terlalu Besar", Toast.LENGTH_SHORT).show();
        }

    }

    private void simpanData() {
        if (id_hsl_stgh ==  null){
            progressDialog.setTitle("Insert Data");
            progressDialog.setMessage("Mohon Tunggu Sebentar");
            progressDialog.show();
            String url = Constants.ROOT_URL+"Simpan_Inten";
            if (edtRingan.getText().toString().equals("") || edtSedang.getText().toString().equals("") || edtBerat.getText().toString().equals("")
                    || edtPuso.getText().toString().equals("") || edtLuasSembuh.getText().toString().equals("") || edtLuasPanen.getText().toString().equals("")
                    || edtKimia.getText().toString().equals("") || edtNabati.getText().toString().equals("") || edtEradikasi.getText().toString().equals("")
                    || edtCaraLain.getText().toString().equals("") || edtFrekKimia.getText().toString().equals("") || edtFrekNabati.getText().toString().equals("")){
                edtRingan.setError("Tidak boleh kosong");
                edtSedang.setError("Tidak boleh kosong");
                edtBerat.setError("Tidak boleh kosong");
                edtPuso.setError("Tidak boleh kosong");
                edtLuasSembuh.setError("Tidak boleh kosong");
                edtLuasPanen.setError("Tidak boleh kosong");
                edtKimia.setError("Tidak boleh kosong");
                edtNabati.setError("Tidak boleh kosong");
                edtEradikasi.setError("Tidak boleh kosong");
                edtCaraLain.setError("Tidak boleh kosong");
                edtFrekKimia.setError("Tidak boleh kosong");
                edtFrekNabati.setError("Tidak boleh kosong");
                progressDialog.dismiss();
            }else {
                final String luas_sembuh = edtLuasSembuh.getText().toString();
                final String serang_ringan = edtRingan.getText().toString();
                final String serang_sedang = edtSedang.getText().toString();
                final String serang_berat = edtBerat.getText().toString();
                final String serang_puso = edtPuso.getText().toString();
                final int luas_sisa_serang = Integer.parseInt(serang_ringan)+Integer.parseInt(serang_sedang)+Integer.parseInt(serang_berat)+Integer.parseInt(serang_puso);
                intensitas_serang = intensitas_keadaan_sisa;
                String luas_tambah_serangan = luas_tambah;
                final int luas_keadaan_serang = Integer.parseInt(luas_tambah_serangan) + luas_sisa_serang;
                String luas_tambah_serang = luas_tambah;
                String intensitas_tambah_serang = intensitas_tambah;
                float a = luas_sisa_serang*Float.parseFloat(intensitas_serang);
                float b = Float.parseFloat(luas_tambah_serang)*Float.parseFloat(intensitas_tambah_serang);
                float c = a+b;
                final float intensitas_keadaan = c/luas_keadaan_serang;
                final String luas_panen = edtLuasPanen.getText().toString();
                final String kimia = edtKimia.getText().toString();
                final String nabati = edtNabati.getText().toString();
                final String eradikasi = edtEradikasi.getText().toString();
                final String cara_lain = edtCaraLain.getText().toString();
                final String frek_kimia = edtFrekKimia.getText().toString();
                final String frek_nabati = edtFrekNabati.getText().toString();
                final int jumlah_pengendalian = Integer.parseInt(kimia)+Integer.parseInt(nabati)+Integer.parseInt(eradikasi)+Integer.parseInt(cara_lain);
                final String id_intensitas = getIntent().getStringExtra("id_intensitas");
//        final String rekomendasi = edtRekomendasi.getText().toString();
//        String musim_tanam = "";

                final String username = Common.currentUser.getUsername();
                final String petugas = Common.currentUser.getNama();
                status = getIntent().getStringExtra("status");

                keadaan_ringan = Integer.parseInt(serang_ringan) + Integer.parseInt(tambah_ringan);
                keadaan_sedang = Integer.parseInt(serang_sedang)+ Integer.parseInt(tambah_sedang);
                keadaan_berat = Integer.parseInt(serang_berat)+ Integer.parseInt(tambah_berat);
                keadaan_puso = Integer.parseInt(serang_puso)+ Integer.parseInt(tambah_puso);

                dateFormat = new SimpleDateFormat("MM");
                now = dateFormat.format(calendar.getTime());
                if (now.equals("04")||now.equals("05")||now.equals("06")||now.equals("07")||now.equals("08")||now.equals("09")){
                    musim_tanam = "Kemarau";
                }else {
                    musim_tanam = "Hujan";
                }

                SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String tanggal = datetime.format(calendar.getTime());

//        Date dt = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(dt);
//        calendar.add(Calendar.DATE, 1);
//        dt = calendar.getTime();
//        final String datePlusOne = dateFormat.format(dt);
//
//        Date hour = new Date();
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.setTime(hour);
//        calendar1.add(Calendar.HOUR, 12);
//        hour = calendar1.getTime();
//        final String datePlusHour = dateFormat.format(hour);

//        Date dt = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(dt);
//        calendar.add(Calendar.MONTH, 1);
//        dt = calendar.getTime();
//        if (dateFormat.format(dt).equals("03"))
//        final String datePlusOne = dateFormat.format(dt);

                int total = Integer.parseInt(luas_sembuh) + Integer.parseInt(luas_panen) + luas_sisa_serang;
                if (total == Integer.parseInt(luas_keadaan_sisa)){

                    final String finalMusim_tanam = musim_tanam;
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("berhasil")){
                                        progressDialog.dismiss();
                                        Toast.makeText(DetailLapor.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                        getIdStgh();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
//                            Toast.makeText(DetailLapor.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("provinsi", "Jawa Barat");
                            params.put("kabupaten", kabupaten);
                            params.put("kecamatan", kecamatan);
                            params.put("desa", desa);
                            params.put("periode", periode_pengamatan);
                            params.put("luas_tanaman", luas_tanaman);
                            params.put("umur_tanaman", umur_tanaman);
                            params.put("jenis_opt", opt);
                            params.put("luas_sembuh", luas_sembuh);
                            params.put("komoditas", komoditas);
                            params.put("varietas", varietas);
                            params.put("luas_sisa", String.valueOf(luas_sisa_serang));
                            params.put("sisa_ringan", serang_ringan);
                            params.put("sisa_sedang", serang_sedang);
                            params.put("sisa_berat", serang_berat);
                            params.put("sisa_puso", serang_puso);
                            params.put("intensitas_sisa", intensitas_serang);
                            params.put("jumlah_sisa", String.valueOf(luas_sisa_serang));
                            params.put("luas_tambah", luas_tambah);
                            params.put("tambah_ringan", tambah_ringan);
                            params.put("tambah_sedang", tambah_sedang);
                            params.put("tambah_berat", tambah_berat);
                            params.put("tambah_puso", tambah_puso);
                            params.put("intensitas_tambah", intensitas_tambah);
                            params.put("jumlah_tambah", luas_tambah);
                            params.put("luas_keadaan", String.valueOf(luas_keadaan_serang));
                            params.put("keadaan_ringan",String.valueOf(keadaan_ringan));
                            params.put("keadaan_sedang",String.valueOf(keadaan_sedang));
                            params.put("keadaan_berat",String.valueOf(keadaan_berat));
                            params.put("keadaan_puso",String.valueOf(keadaan_puso));
                            params.put("jumlah_keadaan", String.valueOf(luas_keadaan_serang));
                            if (String.valueOf(intensitas_keadaan).contains("NaN")){
                                params.put("intensitas_keadaan", intensitas_serang);
                            }else {
                                params.put("intensitas_keadaan", String.valueOf(intensitas_keadaan));
                            }
                            params.put("luas_panen", luas_panen);
                            params.put("luas_terkendali", luas_sembuh);
                            params.put("luas_kimia", kimia);
                            params.put("luas_nabati", nabati);
                            params.put("luas_eradikasi", eradikasi);
                            params.put("luas_cara_lain", cara_lain);
                            params.put("jumlah_pengendalian", String.valueOf(jumlah_pengendalian));
                            params.put("musim_tanam", finalMusim_tanam);
                            params.put("luas_waspada", luas_waspada);
                            params.put("frek_kimia", frek_kimia);
                            params.put("frek_nabati", frek_nabati);
                            params.put("status_pelaporan", "Belum");
                            params.put("batas_waktu_kab", "tidak ada batas waktu");
                            params.put("batas_waktu_satpel", "tidak ada batas waktu");
                            params.put("tanggal_laporan", tanggal);
                            params.put("approval_kab", "Belum");
                            params.put("username", username);
                            params.put("petugas", petugas);
                            params.put("approval_satpel", "Belum");
                            params.put("approval_prov", "Belum");
                            params.put("status", status);

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(DetailLapor.this);
                    requestQueue.add(request);
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Luas Sisa Serangan Terlalu Besar", Toast.LENGTH_SHORT).show();
                }
            }

        }else {
            progressDialog.setTitle("Update Data");
            progressDialog.setMessage("Mohon Tunggu Sebentar");
            progressDialog.show();
            String url = Constants.ROOT_URL+"Simpan_Inten";
            final String luas_sembuh = edtLuasSembuh.getText().toString();
            final String serang_ringan = edtRingan.getText().toString();
            final String serang_sedang = edtSedang.getText().toString();
            final String serang_berat = edtBerat.getText().toString();
            final String serang_puso = edtPuso.getText().toString();
            final int luas_sisa_serang = Integer.parseInt(serang_ringan)+Integer.parseInt(serang_sedang)+Integer.parseInt(serang_berat)+Integer.parseInt(serang_puso);
            intensitas_serang = intensitas_keadaan_sisa;
            String luas_tambah_serangan = luas_tambah;
            final int luas_keadaan_serang = Integer.parseInt(luas_tambah_serangan) + luas_sisa_serang;
            String luas_tambah_serang = luas_tambah;
            String intensitas_tambah_serang = intensitas_tambah;
            float a = luas_sisa_serang*Float.parseFloat(intensitas_serang);
            float b = Float.parseFloat(luas_tambah_serang)*Float.parseFloat(intensitas_tambah_serang);
            float c = a+b;
            final float intensitas_keadaan = c/luas_keadaan_serang;
            final String luas_panen = edtLuasPanen.getText().toString();
            final String kimia = edtKimia.getText().toString();
            final String nabati = edtNabati.getText().toString();
            final String eradikasi = edtEradikasi.getText().toString();
            final String cara_lain = edtCaraLain.getText().toString();
            final String frek_kimia = edtFrekKimia.getText().toString();
            final String frek_nabati = edtFrekNabati.getText().toString();
            final int jumlah_pengendalian = Integer.parseInt(kimia)+Integer.parseInt(nabati)+Integer.parseInt(eradikasi)+Integer.parseInt(cara_lain);
            final String id_intensitas = getIntent().getStringExtra("id_intensitas");

            final String username = Common.currentUser.getUsername();
            final String petugas = Common.currentUser.getNama();

            keadaan_ringan = Integer.parseInt(serang_ringan) + Integer.parseInt(tambah_ringan);
            keadaan_sedang = Integer.parseInt(serang_sedang)+ Integer.parseInt(tambah_sedang);
            keadaan_berat = Integer.parseInt(serang_berat)+ Integer.parseInt(tambah_berat);
            keadaan_puso = Integer.parseInt(serang_puso)+ Integer.parseInt(tambah_puso);

            dateFormat = new SimpleDateFormat("MM");
            now = dateFormat.format(calendar.getTime());
            if (now.equals("04")||now.equals("05")||now.equals("06")||now.equals("07")||now.equals("08")||now.equals("09")){
                musim_tanam = "Kemarau";
            }else {
                musim_tanam = "Hujan";
            }

            SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final String tanggal = datetime.format(calendar.getTime());

//        Date dt = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(dt);
//        calendar.add(Calendar.DATE, 1);
//        dt = calendar.getTime();
//        final String datePlusOne = dateFormat.format(dt);
//
//        Date hour = new Date();
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.setTime(hour);
//        calendar1.add(Calendar.HOUR, 12);
//        hour = calendar1.getTime();
//        final String datePlusHour = dateFormat.format(hour);

//        Date dt = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(dt);
//        calendar.add(Calendar.MONTH, 1);
//        dt = calendar.getTime();
//        if (dateFormat.format(dt).equals("03"))
//        final String datePlusOne = dateFormat.format(dt);

            int total = Integer.parseInt(luas_sembuh) + Integer.parseInt(luas_panen) + luas_sisa_serang;
            if (total == Integer.parseInt(luas_keadaan_sisa)){

                final String finalMusim_tanam = musim_tanam;
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("berhasil")){
                                    progressDialog.dismiss();
                                    Toast.makeText(DetailLapor.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
//                                Intent home = new Intent(DetailLapor.this, Lapor.class);
//                                startActivity(home);
//                                finish();
                                    getIdStgh();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
//                            Toast.makeText(DetailLapor.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_hsl_stgh", id_hsl_stgh);
                        params.put("provinsi", "Jawa Barat");
                        params.put("kabupaten", kabupaten);
                        params.put("kecamatan", kecamatan);
                        params.put("desa", desa);
                        params.put("periode", periode_pengamatan);
                        params.put("luas_tanaman", luas_tanaman);
                        params.put("umur_tanaman", umur_tanaman);
                        params.put("jenis_opt", opt);
                        params.put("luas_sembuh", luas_sembuh);
                        params.put("komoditas", komoditas);
                        params.put("varietas", varietas);
                        params.put("luas_sisa", String.valueOf(luas_sisa_serang));
                        params.put("sisa_ringan", serang_ringan);
                        params.put("sisa_sedang", serang_sedang);
                        params.put("sisa_berat", serang_berat);
                        params.put("sisa_puso", serang_puso);
                        params.put("tambah_ringan", tambah_ringan);
                        params.put("tambah_sedang", tambah_sedang);
                        params.put("tambah_berat", tambah_berat);
                        params.put("tambah_puso", tambah_puso);
                        params.put("intensitas_sisa", intensitas_serang);
                        params.put("jumlah_sisa", String.valueOf(luas_sisa_serang));
                        params.put("luas_tambah", luas_tambah);
                        params.put("intensitas_tambah", intensitas_tambah);
                        params.put("jumlah_tambah", luas_tambah);
                        params.put("luas_keadaan", String.valueOf(luas_keadaan_serang));
                        params.put("keadaan_ringan",String.valueOf(keadaan_ringan));
                        params.put("keadaan_sedang",String.valueOf(keadaan_sedang));
                        params.put("keadaan_berat",String.valueOf(keadaan_berat));
                        params.put("keadaan_puso",String.valueOf(keadaan_puso));
                        params.put("jumlah_keadaan", String.valueOf(luas_keadaan_serang));
                        if (String.valueOf(intensitas_keadaan).contains("NaN")){
                            params.put("intensitas_keadaan", intensitas_serang);
                        }else {
                            params.put("intensitas_keadaan", String.valueOf(intensitas_keadaan));
                        }
                        params.put("luas_panen", luas_panen);
                        params.put("luas_terkendali", luas_sembuh);
                        params.put("luas_kimia", kimia);
                        params.put("luas_nabati", nabati);
                        params.put("luas_eradikasi", eradikasi);
                        params.put("luas_cara_lain", cara_lain);
                        params.put("jumlah_pengendalian", String.valueOf(jumlah_pengendalian));
                        params.put("musim_tanam", finalMusim_tanam);
                        params.put("luas_waspada", luas_waspada);
                        params.put("frek_kimia", frek_kimia);
                        params.put("frek_nabati", frek_nabati);
                        params.put("status_pelaporan", "Belum");
                        params.put("batas_waktu_kab", "tidak ada batas waktu");
                        params.put("batas_waktu_satpel", "tidak ada batas waktu");
                        params.put("tanggal_laporan", tanggal);
                        params.put("approval_kab", "Belum");
                        params.put("username", username);
                        params.put("petugas", petugas);
                        params.put("approval_satpel", "Belum");
                        params.put("approval_prov", "Belum");

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(DetailLapor.this);
                requestQueue.add(request);
            }else {
                progressDialog.dismiss();
                Toast.makeText(this, "Luas Sisa Serangan Terlalu Besar", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void getIdStgh() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String tanggal = simpleDateFormat.format(calendar.getTime());
        String username = Common.currentUser.getUsername();
        String url = Constants.ROOT_URL+"Stgh_Bln?username="+username+"&periode="+periode_pengamatan+"&date="+tanggal+"&desa="+desa;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                id_hsl_stgh = object.getString("id_hsl_stgh");

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getData();
//                    buildRecyclerViewDataHarian();
//                    Toast.makeText(DetailLapor.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailLapor.this).add(stringRequest);
    }

//    private void buildRecyclerViewDataDesa() {
//        recyclerView1 = (RecyclerView) findViewById(R.id.rvList);
//        recyclerView1.setHasFixedSize(true);
//        layoutManager1 = new LinearLayoutManager(DetailLapor.this);
//        recyclerView1.setLayoutManager(layoutManager1);
//        listDataDesa = new ArrayList<>();
//
//        getDataDesa();
//    }
//
//    private void getDataDesa() {
//        String url = Constants.ROOT_URL+"ci_app/desa?id_lap="+id_lapor;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray array = new JSONArray(response);
//                            for (int i = 0; i<array.length(); i++){
//                                JSONObject object = array.getJSONObject(i);
//
//                                String desa = object.getString("desa");
//                                String id = object.getString("id_laporan");
//
//                                DesaModel desaModel = new DesaModel();
//                                desaModel.setId_laporan(id);
//                                desaModel.setDesa(desa);
//                                listDataDesa.add(desaModel);
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                        madapter = new DataDesaAdapter(DetailLapor.this, listDataDesa);
//                        recyclerView1.setAdapter(madapter);
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(DetailLapor.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        Volley.newRequestQueue(DetailLapor.this).add(stringRequest);
//    }

//    @Override
//    public void onBackPressed() {
//        Log.d("CDA", "onBackPressed Called");
//        Intent setIntent = new Intent(Intent.ACTION_MAIN);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(setIntent);
//    }
}
