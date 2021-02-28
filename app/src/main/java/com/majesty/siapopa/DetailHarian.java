package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailHarian extends AppCompatActivity {
    TextView txtApprovalKab, txtApprovalSatpel, txtApprovalProv, txtProvinsi, txtKabupaten, txtKecamatan, txtDesa,
            txtTanggal, txtPeriode, txtLuasTanaman, txtUmurTanaman, txtOPT,
            txtLuasSembuh, txtLuasSerang, txtIntensitasSerang, txtPusoSisa,
            txtJumlahSisa, txtLuasTambah, txtIntenTambah, txtPusoTambah, txtJumlahTambah,
            txtLuasPanen, txtLuasPengendalian, txtLuasKeadaan, txtIntenKeadaan, txtPusoKeadaan,
            txtJumlahKeadaan, txtKimia, txtNabati, txtEradikasi, txtCaraLain, txtJumlahPengendalian,
            txtFrekKimia, txtFrekNabati, txtLuasWaspada, txtKabOpt, txtSatpelOpt, txtSatpelKab, txtProvOpt,
            txtProvKab, txtProvSatpel, txtBatas, txtRekomendasi, txtBatasSatPel;
    String id_intensitas, ma, musuh_alami, jumlah_ma, kecamatan, kabupaten, opt, blok, komoditas, rekomendasi, petugas, luas_serang, id_hasil, desa, luas, varietas, umur, luas_waspada, intensitas;
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
        setContentView(R.layout.activity_detail_harian);

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

        id_intensitas = getIntent().getStringExtra("id");

        getLaporan();
    }

    private void getLaporan(){
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String urlLaporan = Constants.ROOT_URL+"Intensitas?id="+id_intensitas;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String wilayah_pengamatan = object.getString("wilayah_pengamatan");
                                String batas_waktu_kab = object.getString("batas_waktu_kab");
                                String batas_waktu_satpel = object.getString("batas_waktu_satpel");
                                String kabupaten = object.getString("kabupaten");
                                String kecamatan = object.getString("kecamatan");
                                String desa = object.getString("desa");
                                String periode_pengamatan = object.getString("periode_pengamatan");
                                String luas_tanaman = object.getString("luas_tanaman");
                                String umur_tanaman = object.getString("umur_tanaman");
                                String jenis_opt = object.getString("jenis_opt");
                                String luas_sembuh = object.getString("luas_sembuh");
                                String luas_sisa_serang = object.getString("luas_sisa_serang");
                                String intensitas_serang = object.getString("intensitas_serang");
                                String puso_sisa_serang = object.getString("serang_puso");
                                String jumlah_sisa_serang = object.getString("jumlah_sisa_serang");
                                String intensitas_tambah = object.getString("intensitas_tambah");
                                String puso_tambah_serang = object.getString("puso_tambah_serang");
                                String jumlah_tambah_serang = object.getString("jumlah_tambah_serang");
                                String luas_tambah_serang = object.getString("luas_tambah_serang");
                                String luas_pengendalian = object.getString("luas_pengendalian");
                                String luas_keadaan_serang = object.getString("luas_keadaan_serang");
                                String intensitas_keadaan = object.getString("intensitas_keadaan");
                                String puso_keadaan_serang = object.getString("puso_keadaan_serang");
                                String jumlah_keadaan_serang = object.getString("jumlah_keadaan_serang");
                                String luas_waspada = object.getString("luas_waspada");
                                String id_hasil_pengamatan = object.getString("id_hasil_pengamatan");
                                String luas_panen = object.getString("luas_panen");
                                String kimia = object.getString("kimia");
                                String nabati = object.getString("nabati");
                                String eradikasi = object.getString("eradikasi");
                                String cara_lain = object.getString("cara_lain");
                                String jumlah_pengendalian = object.getString("jumlah_pengendalian");
                                String tanggal_intensitas = object.getString("tanggal_intensitas");
                                String frek_kimia = object.getString("frek_kimia");
                                String frek_nabati = object.getString("frek_nabati");
                                String approval_kab = object.getString("approval_kab");
                                String approval_satpel = object.getString("approval_satpel");
                                String approval_provinsi = object.getString("approval_provinsi");
                                String kab_to_popt = object.getString("kab_to_popt");
                                String satpel_to_popt = object.getString("satpel_to_popt");
                                String satpel_to_kab = object.getString("satpel_to_kab");
                                String prov_to_kab = object.getString("prov_to_kab");
                                String prov_to_satpel = object.getString("prov_to_satpel");
                                String prov_to_popt = object.getString("prov_to_popt");
                                String rekomendasi = object.getString("rekomendasi");
                                String bukti_foto = object.getString("bukti_foto");
                                String url = Constants.ROOT_URL+"assets/Images/"+bukti_foto;
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
                                    btn_send.setVisibility(View.GONE);
                                }else if (Common.currentUser.getId_usergroup().equals("3")){
                                    txtKabOpt.setVisibility(View.GONE);
                                    txtSatpelOpt.setVisibility(View.GONE);
                                    txtProvOpt.setVisibility(View.GONE);
                                    txtSatpelKab.setVisibility(View.GONE);
                                    txtProvKab.setVisibility(View.GONE);
                                    txtProvSatpel.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari Provinsi: </b><br></font>" + prov_to_satpel));
                                    btn_send.setVisibility(View.GONE);
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
                                    btn_send.setVisibility(View.GONE);
                                }

                                txtRekomendasi.setText(Html.fromHtml("<font color='#ff0000'><b>Rekomendasi Pengendalian: </b><br></font>" + rekomendasi));
                                txtBatas.setText(Html.fromHtml("<font color='#ff0000'><b>Batas Waktu Approval Kabupaten: </b><br></font>" + batas_waktu_kab));
                                txtBatasSatPel.setText(Html.fromHtml("<font color='#ff0000'><b>Batas Waktu Approval SatPel: </b><br></font>" + batas_waktu_satpel));
                                txtApprovalKab.setText(Html.fromHtml("<font color='#000000'><b>Approval Kabupaten: </b><br></font>" + approval_kab));
                                txtApprovalSatpel.setText(Html.fromHtml("<font color='#000000'><b>Approval Satpel: </b><br></font>" + approval_satpel));
                                txtApprovalProv.setText(Html.fromHtml("<font color='#000000'><b>Approval Provinsi: </b><br></font>" + approval_provinsi));
                                txtProvinsi.setText(Html.fromHtml("<font color='#000000'><b>Provinsi: </b><br></font>" + wilayah_pengamatan));
                                txtKabupaten.setText(Html.fromHtml("<font color='#000000'><b>Kabupaten: </b><br></font>" + kabupaten));
                                txtKecamatan.setText(Html.fromHtml("<font color='#000000'><b>Kecamatan: </b><br></font>" + kecamatan));
                                txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Desa: </b><br></font>" + desa));
                                txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Tanggal: </b><br></font>" + tanggal_intensitas));
                                txtPeriode.setText(Html.fromHtml("<font color='#000000'><b>Periode Pengamatan: </b><br></font>" + periode_pengamatan));
                                txtLuasTanaman.setText(Html.fromHtml("<font color='#000000'><b>Luas Tanaman: </b><br></font>" + luas_tanaman+ " Ha"));
                                txtUmurTanaman.setText(Html.fromHtml("<font color='#000000'><b>Umur Tanaman: </b><br></font>" + umur_tanaman+ " HST"));
                                txtOPT.setText(Html.fromHtml("<font color='#000000'><b>Jenis OPT: </b><br></font>" + jenis_opt));
                                txtLuasSembuh.setText(Html.fromHtml("<font color='#000000'><b>Luas Sembuh: </b><br></font>" + luas_sembuh+ " Ha"));
                                txtLuasSerang.setText(Html.fromHtml("<font color='#000000'><b>Luas Sisa Serangan: </b><br></font>" + luas_sisa_serang+ " Ha"));
                                txtIntensitasSerang.setText(Html.fromHtml("<font color='#000000'><b>Intensitas Serangan: </b><br></font>" + intensitas_serang+ " %"));
                                txtPusoSisa.setText(Html.fromHtml("<font color='#000000'><b>Puso Sisa Serangan: </b><br></font>" + puso_sisa_serang+ " Ha"));
                                txtJumlahSisa.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Sisa Serangan: </b><br></font>" + jumlah_sisa_serang+ " Ha"));
                                txtLuasTambah.setText(Html.fromHtml("<font color='#000000'><b>Luas Tambah Serangan: </b><br></font>" + luas_tambah_serang+ " Ha"));
                                txtIntenTambah.setText(Html.fromHtml("<font color='#000000'><b>Intensitas Tambah Serangan: </b><br></font>" + intensitas_tambah+ " %"));
                                txtPusoTambah.setText(Html.fromHtml("<font color='#000000'><b>Puso Tambah Serangan: </b><br></font>" + puso_tambah_serang+ " Ha"));
                                txtJumlahTambah.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Tambah Serangan: </b><br></font>" + jumlah_tambah_serang+ " Ha"));
                                txtLuasPengendalian.setText(Html.fromHtml("<font color='#000000'><b>Luas Pengendalian: </b><br></font>" + luas_pengendalian+ " Ha"));
                                txtLuasKeadaan.setText(Html.fromHtml("<font color='#000000'><b>Luas Keadaan Serangan: </b><br></font>" + luas_keadaan_serang+ " Ha"));
                                txtIntenKeadaan.setText(Html.fromHtml("<font color='#000000'><b>Intensitas Keadaan Serangan: </b><br></font>" + intensitas_keadaan+ " %"));
                                txtPusoKeadaan.setText(Html.fromHtml("<font color='#000000'><b>Puso Keadaan Serangan: </b><br></font>" + puso_keadaan_serang+ " Ha"));
                                txtJumlahKeadaan.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Keadaan Serangan: </b><br></font>" + jumlah_keadaan_serang+ " Ha"));
                                txtLuasWaspada.setText(Html.fromHtml("<font color='#000000'><b>Luas Waspada: </b><br></font>" + luas_waspada+ " Ha"));
                                txtLuasPanen.setText(Html.fromHtml("<font color='#000000'><b>Luas Panen: </b><br></font>" + luas_panen+ " Ha"));
                                txtKimia.setText(Html.fromHtml("<font color='#000000'><b>Kimia: </b><br></font>" + kimia+ " Ha"));
                                txtNabati.setText(Html.fromHtml("<font color='#000000'><b>Nabati: </b><br></font>" + nabati+ " Ha"));
                                txtEradikasi.setText(Html.fromHtml("<font color='#000000'><b>Eradikasi: </b><br></font>" + eradikasi+ " Ha"));
                                txtCaraLain.setText(Html.fromHtml("<font color='#000000'><b>Cara Lain: </b><br></font>" + cara_lain+ " Ha"));
                                txtJumlahPengendalian.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Pengendalian: </b><br></font>" + jumlah_pengendalian+ " Ha"));
                                txtFrekKimia.setText(Html.fromHtml("<font color='#000000'><b>Frekuensi Kimia: </b><br></font>" + frek_kimia+ " Kali"));
                                txtFrekNabati.setText(Html.fromHtml("<font color='#000000'><b>Frekuensi Nabati: </b><br></font>" + frek_nabati+ " Kali"));
                                Picasso.get().load(url).into(img_bukti);
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
                Toast.makeText(DetailHarian.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailHarian.this).add(stringRequest);
    }
}