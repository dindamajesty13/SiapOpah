package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
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

public class ShowDetailLaporan extends AppCompatActivity {
    TextView txtDate, txtDesa, txtUsiaTanam, txtLuasSerang, txtLuasSembuh, txtLuasWaspada,
            txtLuasPanen, txtTTD, txtNama, txtSerangRingan,txtSerangSedang, txtSerangBerat, txtSerangPuso,
            txtLuasTambah, txtTambahRingan, txtTambahSedang, txtTambahBerat, txtTambahPuso,
            txtLuasKeadaan, txtKeadaanRingan,txtKeadaanSedang, txtKeadaanBerat, txtKeadaanPuso,
            txtPM, txtKimia, txtNabati, txtAH, txtCL, txtLuasKendali, txtfrekKimia, txtfrekNabati, txtLuasTanaman,
            txtKategoriSerangan, txtIntensitasSerangan, txtKategoriTambah, txtIntensitasTambah,
            txtKategoriKeadaan, txtIntensitasKeadaan, txtKecamatan, txtJumLuasSerang, txtJumLK, txtJumLuasTambah;
    ImageView imgBukti;
    String id;
    String base_url_bukti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_laporan);

        txtDate = (TextView) findViewById(R.id.txtTanggal);
        txtJumLuasSerang = (TextView) findViewById(R.id.txtJumLS);
        txtJumLK = (TextView) findViewById(R.id.txtJumLK);
        txtJumLuasTambah = (TextView) findViewById(R.id.txtJumLuasTambah);
        txtKategoriSerangan = (TextView) findViewById(R.id.txtKategoriSerangan);
        txtIntensitasSerangan = (TextView) findViewById(R.id.txtIntenSerangan);
        txtKategoriTambah = (TextView) findViewById(R.id.txtKategoriTambah);
        txtIntensitasTambah = (TextView) findViewById(R.id.txtIntenTambah);
        txtKategoriKeadaan = (TextView) findViewById(R.id.txtKategoriKeadaan);
        txtIntensitasKeadaan = (TextView) findViewById(R.id.txtIntenKeadaan);
        txtDesa = (TextView) findViewById(R.id.txtDesa);
        txtKecamatan = (TextView) findViewById(R.id.txtKecamatan);
        txtLuasTanaman = (TextView) findViewById(R.id.txtLuasTanaman);
        txtUsiaTanam = (TextView) findViewById(R.id.txtUsiaTanam);
        txtLuasPanen = (TextView) findViewById(R.id.txtLuasPanen);
        txtLuasSembuh = (TextView) findViewById(R.id.txtLuasSembuh);
        txtLuasSerang = (TextView) findViewById(R.id.txtLuasSerang);
        txtSerangRingan = (TextView) findViewById(R.id.txtSR);
        txtSerangSedang = (TextView) findViewById(R.id.txtSS);
        txtSerangBerat = (TextView) findViewById(R.id.txtSB);
        txtSerangPuso = (TextView) findViewById(R.id.txtSP);
        txtLuasTambah = (TextView) findViewById(R.id.txtLuasTambah);
        txtTambahRingan = (TextView) findViewById(R.id.txtTR);
        txtTambahSedang = (TextView) findViewById(R.id.txtTS);
        txtTambahBerat = (TextView) findViewById(R.id.txtTB);
        txtTambahPuso = (TextView) findViewById(R.id.txtTP);
        txtLuasKeadaan = (TextView) findViewById(R.id.txtLuasKeadaan);
        txtKeadaanRingan = (TextView) findViewById(R.id.txtKR);
        txtKeadaanSedang = (TextView) findViewById(R.id.txtKS);
        txtKeadaanBerat = (TextView) findViewById(R.id.txtKB);
        txtKeadaanPuso = (TextView) findViewById(R.id.txtKP);
        txtPM = (TextView) findViewById(R.id.txtPM);
        txtKimia = (TextView) findViewById(R.id.txtKimia);
        txtNabati = (TextView) findViewById(R.id.txtNabati);
        txtAH = (TextView) findViewById(R.id.txtAH);
        txtCL = (TextView) findViewById(R.id.txtCL);
        txtLuasKendali = (TextView) findViewById(R.id.txtLuasPengendalian);
        txtfrekKimia = (TextView) findViewById(R.id.txtfrekKimia);
        txtfrekNabati = (TextView) findViewById(R.id.txtfrekNabati);
        txtLuasWaspada = (TextView) findViewById(R.id.txtLuasWaspada);
//        txtNama = (TextView) findViewById(R.id.txtNama);
        imgBukti = (ImageView) findViewById(R.id.imgBukti);

        id = getIntent().getStringExtra("id");

        getDataDesa();
    }

    private void getDataDesa() {
        String url = Constants.ROOT_URL+"ci_app/desa?id="+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String desa = object.getString("desa");
                                String kecamatan = object.getString("kecamatan");
                                String luas_tanaman = object.getString("luas_tanaman");
                                String dari_umur = object.getString("dari_umur");
                                String hingga_umur = object.getString("hingga_umur");
                                String umur_tanaman = dari_umur + "-" + hingga_umur;
                                String luas_serangan = object.getString("luas_serangan");
                                String kategori_serangan = object.getString("kategori_serangan");
                                String intensitas_serangan = object.getString("intensitas_serangan");
                                String jum_serang_ringan = object.getString("jum_serang_ringan");
                                String jum_serang_sedang = object.getString("jum_serang_sedang");
                                String jum_serang_berat = object.getString("jum_serang_berat");
                                String jum_serang_puso = object.getString("jum_serang_puso");
                                String jum_luas_serangan = object.getString("jum_luas_serangan");
                                String luas_sembuh = object.getString("luas_sembuh");
                                String luas_panen = object.getString("luas_panen");
                                String luas_tambah = object.getString("luas_tambah");
                                String kategori_tambah = object.getString("kategori_tambah");
                                String intensitas_tambah = object.getString("intensitas_tambah");
                                String jum_tambah_ringan = object.getString("jum_tambah_ringan");
                                String jum_tambah_sedang = object.getString("jum_tambah_sedang");
                                String jum_tambah_berat = object.getString("jum_tambah_berat");
                                String jum_tambah_puso = object.getString("jum_tambah_puso");
                                String jum_luas_tambah = object.getString("jum_luas_tambah");
                                String pm = object.getString("pm");
                                String jum_kimia = object.getString("jum_kimia");
                                String jum_nabati = object.getString("jum_nabati");
                                String jum_AH = object.getString("jum_AH");
                                String jum_CL = object.getString("jum_CL");
                                String jum_luas_pengendalian = object.getString("jum_luas_pengendalian");
                                String luas_keadaan = object.getString("luas_keadaan");
                                String kategori_keadaan = object.getString("kategori_keadaan");
                                String intensitas_keadaan = object.getString("intensitas_keadaan");
                                String jum_keadaan_ringan = object.getString("jum_keadaan_ringan");
                                String jum_keadaan_sedang = object.getString("jum_keadaan_sedang");
                                String jum_keadaan_berat = object.getString("jum_keadaan_berat");
                                String jum_keadaan_puso = object.getString("jum_keadaan_puso");
                                String jum_luas_keadaan = object.getString("jum_luas_keadaan");
                                String frek_kimia = object.getString("frek_kimia");
                                String frek_nabati = object.getString("frek_nabati");
                                String luas_waspada = object.getString("luas_waspada");
                                String bukti_foto = object.getString("bukti_foto");
                                String date_time = object.getString("date_time");
                                base_url_bukti = Constants.ROOT_URL+"ci_app/assets/Images/"+bukti_foto;

                                txtDate.setText(Html.fromHtml("<font color='#000000'><b>Tanggal: </b><br></font>" + date_time));
                                txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Desa: </b><br></font>" + desa));
                                txtKecamatan.setText(Html.fromHtml("<font color='#000000'><b>Kecamatan: </b><br></font>" + kecamatan));
                                txtLuasTanaman.setText(Html.fromHtml("<font color='#000000'><b>Luas Tanaman: </b><br></font>" + luas_tanaman+ " Ha"));
                                txtUsiaTanam.setText(Html.fromHtml("<font color='#000000'><b>Usia Tanaman: </b><br></font>" + umur_tanaman+ " HST"));
                                txtLuasSembuh.setText(Html.fromHtml("<font color='#000000'><b>Luas Sembuh: </b><br></font>" + luas_sembuh+ " Ha"));
                                txtLuasPanen.setText(Html.fromHtml("<font color='#000000'><b>Luas Panen: </b><br></font>" + luas_panen+ " Ha"));
                                txtSerangRingan.setText(Html.fromHtml("<font color='#000000'><b>Ringan: </b><br></font>" + jum_serang_ringan+ " Ha"));
                                txtSerangSedang.setText(Html.fromHtml("<font color='#000000'><b>Sedang: </b><br></font>" + jum_serang_sedang+ " Ha"));
                                txtSerangBerat.setText(Html.fromHtml("<font color='#000000'><b>Berat: </b><br></font>" + jum_serang_berat+ " Ha"));
                                txtSerangPuso.setText(Html.fromHtml("<font color='#000000'><b>Puso: </b><br></font>" + jum_serang_puso+ " Ha"));
                                txtLuasSerang.setText(Html.fromHtml("<font color='#000000'><b>Luas Serangan: </b><br></font>" + luas_serangan+ " Ha"));
                                txtJumLuasSerang.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Luas Serangan: </b><br></font>" + jum_luas_serangan+ " Ha"));
                                txtKategoriSerangan.setText(Html.fromHtml("<font color='#000000'><b>Kategori Serangan: </b><br></font>" + kategori_serangan));
                                txtIntensitasSerangan.setText(Html.fromHtml("<font color='#000000'><b>Intensitas Serangan: </b><br></font>" + intensitas_serangan + "%"));
                                txtTambahRingan.setText(Html.fromHtml("<font color='#000000'><b>Ringan: </b><br></font>" + jum_tambah_ringan+ " Ha"));
                                txtTambahSedang.setText(Html.fromHtml("<font color='#000000'><b>Sedang: </b><br></font>" + jum_tambah_sedang+ " Ha"));
                                txtTambahBerat.setText(Html.fromHtml("<font color='#000000'><b>Berat: </b><br></font>" + jum_tambah_berat+ " Ha"));
                                txtTambahPuso.setText(Html.fromHtml("<font color='#000000'><b>Puso: </b><br></font>" + jum_tambah_puso+ " Ha"));
                                txtLuasTambah.setText(Html.fromHtml("<font color='#000000'><b>Luas Tambah: </b><br></font>" + luas_tambah+ " Ha"));
                                txtJumLuasTambah.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Luas Tambah: </b><br></font>" + jum_luas_tambah+ " Ha"));
                                txtKategoriTambah.setText(Html.fromHtml("<font color='#000000'><b>Kategori Tambah: </b><br></font>" + kategori_tambah));
                                txtIntensitasTambah.setText(Html.fromHtml("<font color='#000000'><b>Intensitas Tambah: </b><br></font>" + intensitas_tambah + "%"));
                                txtKeadaanRingan.setText(Html.fromHtml("<font color='#000000'><b>Ringan: </b><br></font>" + jum_keadaan_ringan+ " Ha"));
                                txtKeadaanSedang.setText(Html.fromHtml("<font color='#000000'><b>Sedang: </b><br></font>" + jum_keadaan_sedang+ " Ha"));
                                txtKeadaanBerat.setText(Html.fromHtml("<font color='#000000'><b>Berat: </b><br></font>" + jum_keadaan_berat+ " Ha"));
                                txtKeadaanPuso.setText(Html.fromHtml("<font color='#000000'><b>Puso: </b><br></font>" + jum_keadaan_puso+ " Ha"));
                                txtLuasKeadaan.setText(Html.fromHtml("<font color='#000000'><b>Luas Keadaan: </b><br></font>" + luas_keadaan+ " Ha"));
                                txtJumLK.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Luas Keadaan: </b><br></font>" + jum_luas_keadaan+ " Ha"));
                                txtKategoriKeadaan.setText(Html.fromHtml("<font color='#000000'><b>Kategori Keadaan: </b><br></font>" + kategori_keadaan));
                                txtIntensitasKeadaan.setText(Html.fromHtml("<font color='#000000'><b>Intensitas Keadaan: </b><br></font>" + intensitas_keadaan + "%"));
                                txtPM.setText(Html.fromHtml("<font color='#000000'><b>PM: </b><br></font>" + pm+ " Ha"));
                                txtKimia.setText(Html.fromHtml("<font color='#000000'><b>Kimia: </b><br></font>" + jum_kimia));
                                txtNabati.setText(Html.fromHtml("<font color='#000000'><b>Nabati: </b><br></font>" + jum_nabati));
                                txtAH.setText(Html.fromHtml("<font color='#000000'><b>AH: </b><br></font>" + jum_AH));
                                txtCL.setText(Html.fromHtml("<font color='#000000'><b>CL: </b><br></font>" + jum_CL));
                                txtLuasKendali.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Luas Pengendalian: </b><br></font>" + jum_luas_pengendalian+ " Ha"));
                                txtLuasWaspada.setText(Html.fromHtml("<font color='#000000'><b>Luas Waspada: </b><br></font>" + luas_waspada+ " Ha"));
                                txtfrekKimia.setText(Html.fromHtml("<font color='#000000'><b>Frekuensi Kimia: </b><br></font>" + frek_kimia+ " Kali"));
                                txtfrekNabati.setText(Html.fromHtml("<font color='#000000'><b>Frekuensi Nabati: </b><br></font>" + frek_nabati+ " Kali"));
                                Picasso.get().load(base_url_bukti).into(imgBukti);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowDetailLaporan.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ShowDetailLaporan.this).add(stringRequest);
    }
}
