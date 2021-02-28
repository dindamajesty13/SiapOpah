package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class DetailPermohonan extends AppCompatActivity {
    TextView txtKabupaten, txtKecamatan, txtDesa, txtLuasTanaman, txtUmurTanaman, txtVarietas, txtOPT, txtLuasSerang,
            txtIntenSerangan, txtLuasTerancam, txtLuasPengendalian, txtTanggalPengendalian, txtLokasiPengendalian, txtKelompok,
            txtKetuaPoktan, txtNamaPOPT, txtNipPOPT, txtTanggalPermohonan, txtStatus;
    Button btn_kartu, btn_ktp, btn_surat, btn_lampiran, btn_surat_perintah;
    ProgressDialog progressDialog;
    String userAlamat;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_permohonan);

        txtKabupaten = findViewById(R.id.txtKabupaten);
        txtKecamatan = findViewById(R.id.txtKecamatan);
        txtDesa = findViewById(R.id.txtDesa);
        txtLuasTanaman = findViewById(R.id.txtLuasTanaman);
        txtUmurTanaman = findViewById(R.id.txtUmurTanaman);
        txtVarietas = findViewById(R.id.txtVarietas);
        txtOPT = findViewById(R.id.txtOPT);
        txtStatus = findViewById(R.id.txtStatus);
        txtLuasSerang = findViewById(R.id.txtLuasSerang);
        txtIntenSerangan = findViewById(R.id.txtIntenSerangan);
        txtLuasTerancam = findViewById(R.id.txtLuasTerancam);
        txtLuasPengendalian = findViewById(R.id.txtLuasPengendalian);
        txtTanggalPengendalian = findViewById(R.id.txtTanggalPengendalian);
        txtLokasiPengendalian = findViewById(R.id.txtLokasiPengendalian);
        txtKelompok = findViewById(R.id.txtKelompok);
        txtKetuaPoktan = findViewById(R.id.txtKetuaPoktan);
        txtNamaPOPT = findViewById(R.id.txtNamaPOPT);
        txtNipPOPT = findViewById(R.id.txtNipPOPT);
        txtTanggalPermohonan = findViewById(R.id.txtTanggalPermohonan);
        btn_kartu = findViewById(R.id.btn_kartu);
        btn_ktp = findViewById(R.id.btn_ktp);
        btn_surat = findViewById(R.id.btn_surat);
        btn_surat_perintah = findViewById(R.id.btn_surat_perintah);
        btn_lampiran = findViewById(R.id.btn_lampiran);
        progressDialog = new ProgressDialog(this);
        id = getIntent().getStringExtra("id_permohonan");

        getLaporan();
    }

    private void getLaporan(){
        progressDialog.setTitle("Loading");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"Approval_Permohonan?id="+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                progressDialog.dismiss();

                                String kecamatan = object.getString("kecamatan");
                                String varietas = object.getString("varietas");
                                String desa = object.getString("desa");
                                String id = object.getString("id_permohonan");
                                String jenis_opt = object.getString("jenis_opt");
                                String kabupaten = object.getString("kabupaten");
                                String luas_tanaman = object.getString("luas_tanaman");
                                String umur_tanaman = object.getString("umur_tanaman");
                                String luas_serangan = object.getString("luas_serangan");
                                String intensitas_serangan = object.getString("intensitas_serangan");
                                String luas_terancam = object.getString("luas_terancam");
                                String luas_pengendalian = object.getString("luas_pengendalian");
                                String tanggal_pengendalian = object.getString("tanggal_pengendalian");
                                String lokasi_pengendalian = object.getString("lokasi_pengendalian");
                                String kelompok_tani = object.getString("kelompok_tani");
                                String ketua_poktan = object.getString("ketua_poktan");
                                String nama_popt = object.getString("nama_popt");
                                String nip_popt = object.getString("nip_popt");
                                String tanggal_permohonan = object.getString("tanggal_permohonan");
                                final String kartu_disposisi = object.getString("kartu_disposisi");
                                final String ktp = object.getString("ktp");
                                final String surat_rekomendasi = object.getString("surat_rekomendasi");
                                final String lampiran = object.getString("lampiran");
                                String status_approval = object.getString("status_approval");
                                final String surat_perintah = object.getString("surat_perintah");

                                txtKabupaten.setText(Html.fromHtml("<font color='#000000'><b>Kabupaten: </b><br></font>" + kabupaten));
                                txtKecamatan.setText(Html.fromHtml("<font color='#000000'><b>Kecamatan: </b><br></font>" + kecamatan));
                                txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Desa: </b><br></font>" + desa));
                                txtLuasTanaman.setText(Html.fromHtml("<font color='#000000'><b>Luas Tanaman: </b><br></font>" + luas_tanaman));
                                txtUmurTanaman.setText(Html.fromHtml("<font color='#000000'><b>Umur Tanaman: </b><br></font>" + umur_tanaman));
                                txtVarietas.setText(Html.fromHtml("<font color='#000000'><b>Varietas: </b><br></font>" + varietas));
                                txtOPT.setText(Html.fromHtml("<font color='#000000'><b>Jenis OPT: </b><br></font>" + jenis_opt));
                                txtLuasSerang.setText(Html.fromHtml("<font color='#000000'><b>Luas Serangan: </b><br></font>" + luas_serangan));
                                txtIntenSerangan.setText(Html.fromHtml("<font color='#000000'><b>Intensitas Serangan: </b><br></font>" + intensitas_serangan));
                                txtLuasTerancam.setText(Html.fromHtml("<font color='#000000'><b>Luas Terancam: </b><br></font>" + luas_terancam));
                                txtLuasPengendalian.setText(Html.fromHtml("<font color='#000000'><b>Luas Pengendalian: </b><br></font>" + luas_pengendalian));
                                txtTanggalPengendalian.setText(Html.fromHtml("<font color='#000000'><b>Tanggal Pengendalian: </b><br></font>" + tanggal_pengendalian));
                                txtLokasiPengendalian.setText(Html.fromHtml("<font color='#000000'><b>Lokasi Pengendalian: </b><br></font>" + lokasi_pengendalian));
                                txtKelompok.setText(Html.fromHtml("<font color='#000000'><b>Kelompok Tani: </b><br></font>" + kelompok_tani));
                                txtKetuaPoktan.setText(Html.fromHtml("<font color='#000000'><b>Ketua Poktan: </b><br></font>" + ketua_poktan));
                                txtNamaPOPT.setText(Html.fromHtml("<font color='#000000'><b>Nama POPT: </b><br></font>" + nama_popt));
                                txtNipPOPT.setText(Html.fromHtml("<font color='#000000'><b>NIP POPT: </b><br></font>" + nip_popt));
                                txtTanggalPermohonan.setText(Html.fromHtml("<font color='#000000'><b>Tanggal Permohonan: </b><br></font>" + tanggal_permohonan));
                                txtStatus.setText(Html.fromHtml("<font color='#000000'><b>Status Approval: </b><br></font>" + status_approval));

                                btn_kartu.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(DetailPermohonan.this, PdfView.class);
                                        intent.putExtra("kartu", kartu_disposisi);
                                        startActivity(intent);
                                    }
                                });

                                btn_ktp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(DetailPermohonan.this, PdfView.class);
                                        intent.putExtra("kartu", ktp);
                                        startActivity(intent);
                                    }
                                });

                                btn_surat.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(DetailPermohonan.this, PdfView.class);
                                        intent.putExtra("kartu", surat_rekomendasi);
                                        startActivity(intent);
                                    }
                                });

                                btn_lampiran.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(DetailPermohonan.this, PdfView.class);
                                        intent.putExtra("kartu", lampiran);
                                        startActivity(intent);
                                    }
                                });

                                btn_surat_perintah.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(DetailPermohonan.this, PdfView.class);
                                        intent.putExtra("kartu", surat_perintah);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DetailPermohonan.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailPermohonan.this).add(stringRequest);
    }
}