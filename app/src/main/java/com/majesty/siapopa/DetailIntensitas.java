package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailIntensitas extends AppCompatActivity {
    TextView txtApprovalKab, txtApprovalSatpel, txtApprovalProv, txtProvinsi, txtKabupaten, txtKecamatan, txtDesa,
            txtTanggal, txtPeriode, txtLuasTanaman, txtUmurTanaman, txtOPT,
            txtLuasSembuh, txtLuasSerang, txtIntensitasSerang, txtPusoSisa,
            txtJumlahSisa, txtLuasTambah, txtIntenTambah, txtPusoTambah, txtJumlahTambah,
            txtLuasPanen, txtLuasPengendalian, txtLuasKeadaan, txtIntenKeadaan, txtPusoKeadaan,
            txtJumlahKeadaan, txtKimia, txtNabati, txtEradikasi, txtCaraLain, txtJumlahPengendalian,
            txtFrekKimia, txtFrekNabati, txtLuasWaspada, txtKabOpt, txtSatpelOpt, txtSatpelKab, txtProvOpt,
            txtProvKab, txtProvSatpel, txtBatas, txtRekomendasi, txtBatasSatPel;
    String id_intensitas, ma, musuh_alami, jumlah_ma, kecamatan, kabupaten, opt, blok, komoditas, rekomendasi, petugas, luas_serang, id_hasil, desa, luas, varietas, umur, luas_waspada, intensitas, puso_tambah_serang;
    Toolbar toolbar;
    Button btn_send, btn_pdf;
    ProgressDialog progressDialog;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date, surat_kab, surat_satpel, surat_provinsi, surat_popt;
    ImageView img_bukti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_intensitas);

        txtApprovalProv = (TextView) findViewById(R.id.txtApprovalProv);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Laporan Setengah Bulan");
        setSupportActionBar(toolbar);
        txtRekomendasi = (TextView) findViewById(R.id.txtRekomendasi);
        txtBatas = (TextView) findViewById(R.id.txtBatas);
        txtBatasSatPel = (TextView) findViewById(R.id.txtBatasSatPel);
        txtKabOpt = (TextView) findViewById(R.id.txtKabOpt);
        txtSatpelOpt = (TextView) findViewById(R.id.txtSatpelOpt);
        txtSatpelKab = (TextView) findViewById(R.id.txtSatpelKab);
        txtProvOpt = (TextView) findViewById(R.id.txtProvOpt);
        txtProvKab = (TextView) findViewById(R.id.txtProvKab);
        txtProvSatpel = (TextView) findViewById(R.id.txtProvSatpel);
        txtApprovalKab = (TextView) findViewById(R.id.txtApprovalKab);
        txtApprovalSatpel = (TextView) findViewById(R.id.txtApprovalSatpel);
//        txtApprovalProv = (TextView) findViewById(R.id.txtApprovalProv);
        txtProvinsi = (TextView) findViewById(R.id.txtProvinsi);
        txtKabupaten = (TextView) findViewById(R.id.txtKabupaten);
        txtKecamatan = (TextView) findViewById(R.id.txtKecamatan);
        txtDesa = (TextView) findViewById(R.id.txtDesa);
        txtTanggal = (TextView) findViewById(R.id.txtTanggal);
        txtPeriode = (TextView) findViewById(R.id.txtPeriode);
        txtLuasTanaman = (TextView) findViewById(R.id.txtLuasTanaman);
        txtUmurTanaman = (TextView) findViewById(R.id.txtUmurTanaman);
        txtOPT = (TextView) findViewById(R.id.txtOPT);
        txtLuasSembuh = (TextView) findViewById(R.id.txtLuasSembuh);
        txtLuasSerang = (TextView) findViewById(R.id.txtLuasSerang);
        txtIntensitasSerang = (TextView) findViewById(R.id.txtIntensitasSerang);
        txtPusoSisa = (TextView) findViewById(R.id.txtPusoSisa);
        txtJumlahSisa = (TextView) findViewById(R.id.txtJumlahSisa);
        txtLuasTambah = (TextView) findViewById(R.id.txtLuasTambah);
        txtIntenTambah = (TextView) findViewById(R.id.txtIntenTambah);
        txtPusoTambah = (TextView) findViewById(R.id.txtPusoTambah);
        txtJumlahTambah = (TextView) findViewById(R.id.txtJumlahTambah);
        txtLuasPanen = (TextView) findViewById(R.id.txtLuasPanen);
        txtLuasPengendalian = (TextView) findViewById(R.id.txtLuasPengendalian);
        txtLuasKeadaan = (TextView) findViewById(R.id.txtLuasKeadaan);
        txtIntenKeadaan = (TextView) findViewById(R.id.txtIntenKeadaan);
        txtPusoKeadaan = (TextView) findViewById(R.id.txtPusoKeadaan);
        txtJumlahKeadaan = (TextView) findViewById(R.id.txtJumlahKeadaan);
        txtKimia = (TextView) findViewById(R.id.txtKimia);
        txtNabati = (TextView) findViewById(R.id.txtNabati);
        txtEradikasi = (TextView) findViewById(R.id.txtEradikasi);
        txtCaraLain = (TextView) findViewById(R.id.txtCaraLain);
        txtJumlahPengendalian = (TextView) findViewById(R.id.txtJumlahPengendalian);
        txtFrekKimia = (TextView) findViewById(R.id.txtFrekKimia);
        txtFrekNabati = (TextView) findViewById(R.id.txtFrekNabati);
        txtLuasWaspada = (TextView) findViewById(R.id.txtLuasWaspada);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_pdf = (Button) findViewById(R.id.btn_pdf);
        img_bukti = (ImageView) findViewById(R.id.img_bukti);
        progressDialog = new ProgressDialog(this);
        calendar = Calendar.getInstance();

        id_intensitas = getIntent().getStringExtra("id_intensitas");

        getLaporan();

//        btn_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean installed = appInstalledOrNot("com.whatsapp");
//                if (installed){
//                    getData();
//                }else {
//                    Toast.makeText(DetailIntensitas.this, "WhatsApp Not Installed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        btn_pdf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Common.currentUser.getId_usergroup().equals("1")){
//                    Intent intent = new Intent(DetailIntensitas.this, PdfView.class);
//                    intent.putExtra("surat", surat_popt);
//                    startActivity(intent);
//                }else if (Common.currentUser.getId_usergroup().equals("2")){
//                    Intent intent = new Intent(DetailIntensitas.this, PdfView.class);
//                    intent.putExtra("surat", surat_popt);
//                    startActivity(intent);
//                }else if (Common.currentUser.getId_usergroup().equals("3")){
//                    Intent intent = new Intent(DetailIntensitas.this, PdfView.class);
//                    intent.putExtra("surat", surat_kab);
//                    startActivity(intent);
//                }else if (Common.currentUser.getId_usergroup().equals("4")){
//                    Intent intent = new Intent(DetailIntensitas.this, PdfView.class);
//                    intent.putExtra("surat", surat_satpel);
//                    startActivity(intent);
//                }
//                else if (Common.currentUser.getId_usergroup().equals("6")){
//                    Intent intent = new Intent(DetailIntensitas.this, PdfView.class);
//                    intent.putExtra("surat", surat_provinsi);
//                    startActivity(intent);
//                }
//            }
//        });
    }

    private void getDataMa() {
        String url = Constants.ROOT_URL+"Sum_Ma?opt="+opt+"&id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                ma = object.getString("ma");

                                String url = Constants.ROOT_URL+"Sum_Ma?ma="+ma+"&id_hasil="+id_hasil;

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray array = new JSONArray(response);
                                                    for (int i = 0; i<array.length(); i++){
                                                        JSONObject object = array.getJSONObject(i);

                                                        jumlah_ma = object.getString("jumlah");
                                                        musuh_alami = object.getString("ma");

                                                        float total_ma = Float.parseFloat(jumlah_ma)/30;

                                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=628112000279&text=SATPEL BPTPH WILAYAH IV BANDUNG \t\nLaporan Harian POPT \t\nKegiatan: Pengamatan Keliling \t\nNama POPT: "+petugas+" \t\nHari/Tanggal: "+date+" \t\n \t\nKabupaten: "+kabupaten+"\t\nKecamatan: "+kecamatan+"\t\nDesa: "+desa+"\t\nBlok: "+blok+"\t\n \t\nLuas Hamparan: "+luas+" Ha\t\nKomoditas: "+komoditas+"\t\nVarietas: "+varietas+"\t\nUmur Tanaman: "+umur+" HST\t\n \t\nOPT yang ditemukan: "+opt+"\t\nLuas Serangan: "+luas_serang+" Ha\t\nLuas Waspada: "+luas_waspada+" Ha\t\nIntensitas : "+intensitas+" persen\t\n \t\nMusuh alami: \t\n- "+musuh_alami+" : "+total_ma+" e/r\t\n \t\nRekomendasi: \t\n "+rekomendasi));
                                                        startActivity(intent);

                                                    }
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                        Toast.makeText(InputHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                                Volley.newRequestQueue(DetailIntensitas.this).add(stringRequest);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                jumlah_ma = "0";
                musuh_alami = "tidal ditemukan";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=628112000279&text=SATPEL BPTPH WILAYAH IV BANDUNG \t\nLaporan Harian POPT \t\nKegiatan: Pengamatan Keliling \t\nNama POPT: "+petugas+" \t\nHari/Tanggal: "+date+" \t\n \t\nKabupaten: "+kabupaten+"\t\nKecamatan: "+kecamatan+"\t\nDesa: "+desa+"\t\nBlok: "+blok+"\t\n \t\nLuas Hamparan: "+luas+" Ha\t\nKomoditas: "+komoditas+"\t\nVarietas: "+varietas+"\t\nUmur Tanaman: "+umur+" HST\t\n \t\nOPT yang ditemukan: "+opt+"\t\nLuas Serangan: "+luas_serang+" Ha\t\nLuas Waspada: "+luas_waspada+" Ha\t\nIntensitas : "+intensitas+" persen\t\n \t\nMusuh alami: \t\n-"+musuh_alami+"\t\n \t\nRekomendasi: \t\n "+rekomendasi));
                startActivity(intent);
//                Toast.makeText(InputHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailIntensitas.this).add(stringRequest);
    }

    private void getData() {
        String urlLaporan = Constants.ROOT_URL+"Intensitas?id="+id_intensitas;
        dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        date = dateFormat.format(calendar.getTime());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                kabupaten = object.getString("kabupaten");
                                kecamatan = object.getString("kecamatan");
                                desa = object.getString("desa");
                                blok = object.getString("blok");
                                luas = object.getString("luas_tanaman");
                                komoditas = object.getString("komoditas");
                                varietas = object.getString("varietas");
                                umur = object.getString("umur_tanaman");
                                opt = object.getString("jenis_opt");
                                id_hasil = object.getString("id_hasil_pengamatan");
                                luas_waspada = object.getString("luas_waspada");
                                luas_serang = object.getString("luas_sisa_serang");
                                intensitas = object.getString("intensitas_keadaan");
                                petugas = object.getString("petugas_pengamatan");
                                rekomendasi = object.getString("rekomendasi");

                                getDataMa();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DetailIntensitas.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailIntensitas.this).add(stringRequest);
    }

    private boolean appInstalledOrNot(String url) {
        PackageManager packageManager = getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return app_installed;
    }



    private void getLaporan(){
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String urlLaporan = Constants.ROOT_URL+"Stgh_Bln?id="+id_intensitas;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String wilayah_pengamatan = object.getString("provinsi");
                                String batas_waktu_kab = object.getString("batas_waktu_kab");
                                String batas_waktu_satpel = object.getString("batas_waktu_satpel");
                                String kabupaten = object.getString("kabupaten");
                                String kecamatan = object.getString("kecamatan");
                                String desa = object.getString("desa");
                                String periode_pengamatan = object.getString("periode");
                                String luas_tanaman = object.getString("luas_tanaman");
                                String umur_tanaman = object.getString("umur_tanaman");
                                String jenis_opt = object.getString("jenis_opt");
                                String luas_sembuh = object.getString("luas_sembuh");
                                String luas_sisa_serang = object.getString("luas_sisa");
                                String intensitas_serang = object.getString("intensitas_sisa");
                                String puso_sisa_serang = object.getString("sisa_puso");
                                String jumlah_sisa_serang = object.getString("jumlah_sisa");
                                String intensitas_tambah = object.getString("intensitas_tambah");
                                if (Float.parseFloat(intensitas_serang) >= 85){
                                    puso_tambah_serang = object.getString("jumlah_tambah");
                                }else {
                                    puso_tambah_serang = "0";
                                }
                                String jumlah_tambah_serang = object.getString("jumlah_tambah");
                                String luas_tambah_serang = object.getString("luas_tambah");
                                String luas_pengendalian = object.getString("luas_terkendali");
                                String luas_keadaan_serang = object.getString("luas_keadaan");
                                String intensitas_keadaan = object.getString("intensitas_keadaan");
                                String puso_keadaan_serang = object.getString("keadaan_puso");
                                String jumlah_keadaan_serang = object.getString("jumlah_keadaan");
                                String luas_waspada = object.getString("luas_waspada");
//                                String id_hasil_pengamatan = object.getString("id_hasil_pengamatan");
                                String luas_panen = object.getString("luas_panen");
                                String kimia = object.getString("luas_kimia");
                                String nabati = object.getString("luas_nabati");
                                String eradikasi = object.getString("luas_eradikasi");
                                String cara_lain = object.getString("luas_cara_lain");
                                String jumlah_pengendalian = object.getString("jumlah_pengendalian");
                                String tanggal_intensitas = object.getString("tanggal_laporan");
                                String frek_kimia = object.getString("frek_kimia");
                                String frek_nabati = object.getString("frek_nabati");
                                String approval_kab = object.getString("approval_kab");
                                String approval_satpel = object.getString("approval_satpel");
                                String approval_provinsi = object.getString("approval_prov");
                                String kab_to_popt = object.getString("kab_to_popt");
                                String satpel_to_popt = object.getString("satpel_to_popt");
                                String satpel_to_kab = object.getString("satpel_to_kab");
                                String prov_to_kab = object.getString("prov_to_kab");
                                String prov_to_satpel = object.getString("prov_to_satpel");
                                String prov_to_popt = object.getString("prov_to_popt");
                                String rekomendasi = object.getString("rekomendasi");
//                                String bukti_foto = object.getString("bukti_foto");
//                                String url = Constants.ROOT_URL+"assets/Images/"+bukti_foto;
//                                surat_kab = object.getString("surat_kab");
//                                surat_satpel = object.getString("surat_satpel");
//                                surat_provinsi = object.getString("surat_provinsi");
//                                surat_popt = object.getString("surat");
//                                String approval_prov = object.getString("approval_provinsi");

                                if (Common.currentUser.getId_usergroup().equals("1")){
                                    txtKabOpt.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari Kabupaten: </b><br></font>" + kab_to_popt));
                                    txtSatpelOpt.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari SatPel: </b><br></font>" + satpel_to_popt));
                                    txtProvOpt.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari Provinsi: </b><br></font>" + prov_to_popt));
                                    txtSatpelKab.setVisibility(View.GONE);
                                    txtProvKab.setVisibility(View.GONE);
                                    txtProvSatpel.setVisibility(View.GONE);
                                    btn_send.setVisibility(View.GONE);
                                }else if (Common.currentUser.getId_usergroup().equals("2")){
                                    txtKabOpt.setVisibility(View.GONE);
                                    txtSatpelOpt.setVisibility(View.GONE);
                                    txtProvOpt.setVisibility(View.GONE);
                                    txtSatpelKab.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari SatPel: </b><br></font>" + satpel_to_kab));
                                    txtProvKab.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari Provinsi: </b><br></font>" + prov_to_kab));
                                    txtProvSatpel.setVisibility(View.GONE);
//                                    btn_send.setVisibility(View.GONE);
                                }else if (Common.currentUser.getId_usergroup().equals("3")){
                                    txtKabOpt.setVisibility(View.GONE);
                                    txtSatpelOpt.setVisibility(View.GONE);
                                    txtProvOpt.setVisibility(View.GONE);
                                    txtSatpelKab.setVisibility(View.GONE);
                                    txtProvKab.setVisibility(View.GONE);
                                    txtProvSatpel.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari Provinsi: </b><br></font>" + prov_to_satpel));
//                                    btn_send.setVisibility(View.GONE);
                                }else if (Common.currentUser.getId_usergroup().equals("4")){
                                    txtKabOpt.setVisibility(View.GONE);
                                    txtSatpelOpt.setVisibility(View.GONE);
                                    txtProvOpt.setVisibility(View.GONE);
                                    txtSatpelKab.setVisibility(View.GONE);
                                    txtProvKab.setVisibility(View.GONE);
                                    txtProvSatpel.setVisibility(View.GONE);
                                }
                                else if (Common.currentUser.getId_usergroup().equals("6")){
                                    txtKabOpt.setVisibility(View.GONE);
                                    txtSatpelOpt.setVisibility(View.GONE);
                                    txtProvOpt.setVisibility(View.GONE);
                                    txtSatpelKab.setVisibility(View.GONE);
                                    txtProvKab.setVisibility(View.GONE);
                                    txtProvSatpel.setVisibility(View.GONE);
//                                    btn_send.setVisibility(View.GONE);
                                }

                                txtRekomendasi.setText(Html.fromHtml("<font color='#ff0000'><b>Rekomendasi Pengendalian: </b></font>" + rekomendasi));
                                txtBatas.setText(Html.fromHtml("<font color='#ff0000'><b>Batas Waktu Approval Kabupaten: </b><br></font>" + batas_waktu_kab));
                                txtBatasSatPel.setText(Html.fromHtml("<font color='#ff0000'><b>Batas Waktu Approval SatPel: </b><br></font>" + batas_waktu_satpel));
                                txtApprovalKab.setText(Html.fromHtml("<font color='#000000'><b>Approval Kabupaten: </b></font>" + approval_kab));
                                txtApprovalSatpel.setText(Html.fromHtml("<font color='#000000'><b>Approval Satpel: </b></font>" + approval_satpel));
                                txtApprovalProv.setText(Html.fromHtml("<font color='#000000'><b>Approval Provinsi: </b></font>" + approval_provinsi));
                                txtProvinsi.setText(Html.fromHtml("<font color='#000000'><b>Provinsi: </b></font>" + wilayah_pengamatan));
                                txtKabupaten.setText(Html.fromHtml("<font color='#000000'><b>Kabupaten: </b></font>" + kabupaten));
                                txtKecamatan.setText(Html.fromHtml("<font color='#000000'><b>Kecamatan: </b></font>" + kecamatan));
                                txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Desa: </b></font>" + desa));
                                txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Tanggal: </b></font>" + tanggal_intensitas));
                                txtPeriode.setText(Html.fromHtml("<font color='#000000'><b>Periode Pengamatan: </b></font>" + periode_pengamatan));
                                txtLuasTanaman.setText(Html.fromHtml("<font color='#000000'><b>Luas Tanaman: </b></font>" + luas_tanaman+ " Ha"));
                                txtUmurTanaman.setText(Html.fromHtml("<font color='#000000'><b>Umur Tanaman: </b></font>" + umur_tanaman+ " HST"));
                                txtOPT.setText(Html.fromHtml("<font color='#000000'><b>Jenis OPT: </b></font>" + jenis_opt));
                                txtLuasSembuh.setText(Html.fromHtml("<font color='#000000'><b>Luas Sembuh: </b></font>" + luas_sembuh+ " Ha"));
                                txtLuasSerang.setText(Html.fromHtml("<font color='#000000'><b>Luas Sisa Serangan: </b></font>" + luas_sisa_serang+ " Ha"));
                                txtIntensitasSerang.setText(Html.fromHtml("<font color='#000000'><b>Intensitas Serangan: </b></font>" + intensitas_serang+ " %"));
                                txtPusoSisa.setText(Html.fromHtml("<font color='#000000'><b>Puso Sisa Serangan: </b></font>" + puso_sisa_serang+ " Ha"));
                                txtJumlahSisa.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Sisa Serangan: </b></font>" + jumlah_sisa_serang+ " Ha"));
                                txtLuasTambah.setText(Html.fromHtml("<font color='#000000'><b>Luas Tambah Serangan: </b></font>" + luas_tambah_serang+ " Ha"));
                                txtIntenTambah.setText(Html.fromHtml("<font color='#000000'><b>Intensitas Tambah Serangan: </b></font>" + intensitas_tambah+ " %"));
                                txtPusoTambah.setText(Html.fromHtml("<font color='#000000'><b>Puso Tambah Serangan: </b></font>" + puso_tambah_serang+ " Ha"));
                                txtJumlahTambah.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Tambah Serangan: </b></font>" + jumlah_tambah_serang+ " Ha"));
                                txtLuasPengendalian.setText(Html.fromHtml("<font color='#000000'><b>Luas Pengendalian: </b></font>" + luas_pengendalian+ " Ha"));
                                txtLuasKeadaan.setText(Html.fromHtml("<font color='#000000'><b>Luas Keadaan Serangan: </b></font>" + luas_keadaan_serang+ " Ha"));
                                txtIntenKeadaan.setText(Html.fromHtml("<font color='#000000'><b>Intensitas Keadaan Serangan: </b></font>" + intensitas_keadaan+ " %"));
                                txtPusoKeadaan.setText(Html.fromHtml("<font color='#000000'><b>Puso Keadaan Serangan: </b></font>" + puso_keadaan_serang+ " Ha"));
                                txtJumlahKeadaan.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Keadaan Serangan: </b></font>" + jumlah_keadaan_serang+ " Ha"));
                                txtLuasWaspada.setText(Html.fromHtml("<font color='#000000'><b>Luas Waspada: </b></font>" + luas_waspada+ " Ha"));
                                txtLuasPanen.setText(Html.fromHtml("<font color='#000000'><b>Luas Panen: </b></font>" + luas_panen+ " Ha"));
                                txtKimia.setText(Html.fromHtml("<font color='#000000'><b>Kimia: </b></font>" + kimia+ " Ha"));
                                txtNabati.setText(Html.fromHtml("<font color='#000000'><b>Nabati: </b></font>" + nabati+ " Ha"));
                                txtEradikasi.setText(Html.fromHtml("<font color='#000000'><b>Eradikasi: </b></font>" + eradikasi+ " Ha"));
                                txtCaraLain.setText(Html.fromHtml("<font color='#000000'><b>Cara Lain: </b></font>" + cara_lain+ " Ha"));
                                txtJumlahPengendalian.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Pengendalian: </b></font>" + jumlah_pengendalian+ " Ha"));
                                txtFrekKimia.setText(Html.fromHtml("<font color='#000000'><b>Frekuensi Kimia: </b></font>" + frek_kimia+ " Kali"));
                                txtFrekNabati.setText(Html.fromHtml("<font color='#000000'><b>Frekuensi Nabati: </b></font>" + frek_nabati+ " Kali"));
//                                Picasso.get().load(url).into(img_bukti);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DetailIntensitas.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailIntensitas.this).add(stringRequest);
    }

}
