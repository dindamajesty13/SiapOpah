package com.majesty.siapopa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
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
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InputDetailLaporan extends AppCompatActivity {
    MaterialEditText edtLuasTanaman, edtUmurAwal, edtUmurAkhir, edtLuasSerangan, edtIntensitas,
            edtLuasSembuh, edtLuasPanen, edtLuasTambah, edtIntensitasTambah, edtPM,
            edtJumlahKimia, edtJumlahNabati, edtLuasKeadaan, edtIntensitasKeadaan,
            edtJumlahAH, edtJumlahCL, edtJumNabati, edtJumKimia, edtLuasWaspada;
    Spinner spKategori, spKategoriKeadaan, spKategoriTambah;
    Button save_button, cancel_button;
    TextView txtDesa, txtJumlahSerangan, txtJumlahTambah, txtJumlahKeadaan, txtJumlahPengendalian, txtPestisida, txtNonPestisida;
    ImageView img_bukti;
    String encodedImage, desa, id_laporan;
    ArrayList<String> kategori = new ArrayList<>();
    ArrayAdapter<String> kategoriAdapter;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private int PICK_IMAGE_REQUEST = 71;
    Uri uri;
    String base_url_bukti;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String jumlahSerang, jumlahTambah, jumlahKeadaan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_detail_laporan);

        edtLuasTanaman = (MaterialEditText) findViewById(R.id.edtLuasTanaman);
        edtUmurAwal = (MaterialEditText) findViewById(R.id.edtUmurAwal);
        edtUmurAkhir = (MaterialEditText) findViewById(R.id.edtUmurAkhir);
        edtLuasSerangan = (MaterialEditText) findViewById(R.id.edtLuasSerangan);
        edtIntensitas = (MaterialEditText) findViewById(R.id.edtIntensitas);
        edtLuasSembuh = (MaterialEditText) findViewById(R.id.edtLuasSembuh);
        edtLuasPanen = (MaterialEditText) findViewById(R.id.edtLuasPanen);
        edtLuasTambah = (MaterialEditText) findViewById(R.id.edtLuasTambah);
        edtIntensitasTambah = (MaterialEditText) findViewById(R.id.edtIntensitasTambah);
        edtPM = (MaterialEditText) findViewById(R.id.edtPM);
        edtLuasKeadaan = (MaterialEditText) findViewById(R.id.edtLuasKeadaan);
        edtIntensitasKeadaan = (MaterialEditText) findViewById(R.id.edtIntensitasKeadaan);
        edtJumlahNabati = (MaterialEditText) findViewById(R.id.edtJumlahNabati);
        edtLuasWaspada = (MaterialEditText) findViewById(R.id.edtLuasWaspada);
        edtJumKimia = (MaterialEditText) findViewById(R.id.edtJumKimia);
        edtJumNabati = (MaterialEditText) findViewById(R.id.edtJumNabati);
        edtJumlahKimia = (MaterialEditText) findViewById(R.id.edtJumlahKimia);
        edtJumlahCL = (MaterialEditText) findViewById(R.id.edtJumlahCL);
        edtJumlahAH = (MaterialEditText) findViewById(R.id.edtJumlahAH);
        spKategori = (Spinner) findViewById(R.id.spKategori);
        spKategoriKeadaan = (Spinner) findViewById(R.id.spKategoriKeadaan);
        spKategoriTambah = (Spinner) findViewById(R.id.spKategoriTambah);
        save_button = (Button) findViewById(R.id.save_button);
        cancel_button = (Button) findViewById(R.id.cancel_button);
        txtDesa = (TextView) findViewById(R.id.desa);
        txtPestisida = (TextView) findViewById(R.id.txtPestisida);
        txtNonPestisida = (TextView) findViewById(R.id.txtNonPestisida);
        txtJumlahSerangan = (TextView) findViewById(R.id.txtJumlah);
        txtJumlahTambah = (TextView) findViewById(R.id.txtJumlahTambah);
        txtJumlahKeadaan = (TextView) findViewById(R.id.txtJumlahKeadaan);
        txtJumlahPengendalian = (TextView) findViewById(R.id.txtJumlahTambahPengendalian);
        img_bukti = (ImageView) findViewById(R.id.buktiFoto);
        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);

        jumlahSerang = edtLuasSerangan.getText().toString();
        txtJumlahSerangan.setText(jumlahSerang);

        jumlahTambah = edtLuasTambah.getText().toString();
        txtJumlahTambah.setText(jumlahTambah);

        jumlahKeadaan = edtLuasKeadaan.getText().toString();
        txtJumlahKeadaan.setText(jumlahKeadaan);

        img_bukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        id_laporan = getIntent().getStringExtra("id");
        if (id_laporan != null){
            getDataDesa();
        }

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_laporan != null){
                    updateData();
                }else {
                    insertData();
                }
            }
        });

        desa = getIntent().getStringExtra("desa");
        txtDesa.setText(desa);



        String url = Constants.ROOT_URL+"PopulateKategori.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("kategori_intensitas");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String jenis_kategori = jsonObject.optString("jenis_kategori");
                        kategori.add(jenis_kategori);
                        kategoriAdapter = new ArrayAdapter<>(InputDetailLaporan.this, android.R.layout.simple_dropdown_item_1line, kategori);
                        kategoriAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spKategori.setAdapter(kategoriAdapter);
                        spKategoriKeadaan.setAdapter(kategoriAdapter);
                        spKategoriTambah.setAdapter(kategoriAdapter);
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

    private void getDataDesa() {
        String url = Constants.ROOT_URL+"desa?id="+id_laporan;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String desa = object.getString("desa");
                                String luas_tanaman = object.getString("luas_tanaman");
                                String dari_umur = object.getString("dari_umur");
                                String hingga_umur = object.getString("hingga_umur");
                                String luas_serangan = object.getString("luas_serangan");
                                String kategori_serangan = object.getString("kategori_serangan");
                                String intensitas_serangan = object.getString("intensitas_serangan");
                                String luas_sembuh = object.getString("luas_sembuh");
                                String luas_panen = object.getString("luas_panen");
                                String luas_tambah = object.getString("luas_tambah");
                                String kategori_tambah = object.getString("kategori_tambah");
                                String intensitas_tambah = object.getString("intensitas_tambah");
                                String pm = object.getString("pm");
                                String jum_kimia = object.getString("jum_kimia");
                                String jum_nabati = object.getString("jum_nabati");
                                String jum_AH = object.getString("jum_AH");
                                String jum_CL = object.getString("jum_CL");
                                String jum_luas_pengendalian = object.getString("jum_luas_pengendalian");
                                String luas_keadaan = object.getString("luas_keadaan");
                                String kategori_keadaan = object.getString("kategori_keadaan");
                                String intensitas_keadaan = object.getString("intensitas_keadaan");
                                String frek_kimia = object.getString("frek_kimia");
                                String frek_nabati = object.getString("frek_nabati");
                                String luas_waspada = object.getString("luas_waspada");
                                String bukti_foto = object.getString("bukti_foto");
                                base_url_bukti = Constants.ROOT_URL+"ci_app/assets/Images/"+bukti_foto;

                                txtDesa.setText(desa);
                                edtLuasTanaman.setText(luas_tanaman);
                                edtUmurAwal.setText(dari_umur);
                                edtUmurAkhir.setText(hingga_umur);
                                edtLuasSembuh.setText(luas_sembuh);
                                edtLuasPanen.setText(luas_panen);
                                edtLuasSerangan.setText(luas_serangan);
                                spKategori.setSelection(kategoriAdapter.getPosition(kategori_serangan));
                                edtIntensitas.setText(intensitas_serangan);
                                edtLuasTambah.setText(luas_tambah);
                                spKategoriTambah.setSelection(kategoriAdapter.getPosition(kategori_tambah));
                                edtIntensitasTambah.setText(intensitas_tambah);
                                edtLuasKeadaan.setText(luas_keadaan);
                                spKategoriKeadaan.setSelection(kategoriAdapter.getPosition(kategori_keadaan));
                                edtIntensitasKeadaan.setText(intensitas_keadaan);
                                edtPM.setText(pm);
                                edtJumKimia.setText(jum_kimia);
                                edtJumNabati.setText(jum_nabati);
                                edtJumlahAH.setText(jum_AH);
                                edtJumlahCL.setText(jum_CL);
                                edtLuasSembuh.setText(jum_luas_pengendalian);
                                edtLuasWaspada.setText(luas_waspada);
                                edtJumlahKimia.setText(frek_kimia);
                                edtJumlahNabati.setText(frek_nabati);
                                Picasso.get().load(base_url_bukti).into(img_bukti);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InputDetailLaporan.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(InputDetailLaporan.this).add(stringRequest);
    }

    private void updateData() {
        progressDialog.setTitle("Update Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        final String id = getIntent().getStringExtra("id");
        final String kecamatan = Common.currentUser.getAlamat();
        final String desa = txtDesa.getText().toString().trim();
        final String luas_tanaman = edtLuasTanaman.getText().toString().trim();
        final String dari_umur = edtUmurAwal.getText().toString().trim();
        final String hingga_umur = edtUmurAkhir.getText().toString().trim();
        final String luas_serangan = edtLuasSerangan.getText().toString().trim();
        final String kategori_serangan = spKategori.getSelectedItem().toString().trim();
        final String intensitas_serangan = edtIntensitas.getText().toString().trim();
        final String jum_luas_serangan = txtJumlahSerangan.getText().toString().trim();
        final String luas_terkendali = edtLuasSembuh.getText().toString().trim();
        final String luas_panen = edtLuasPanen.getText().toString().trim();
        final String luas_tambah = edtLuasTambah.getText().toString().trim();
        final String kategori_tambah = spKategoriTambah.getSelectedItem().toString().trim();
        final String intensitas_tambah = edtIntensitasTambah.getText().toString().trim();
        final String jum_luas_tambah = txtJumlahTambah.getText().toString().trim();
        final String pm = edtPM.getText().toString().trim();
        final String pestisida = txtPestisida.getText().toString().trim();
        final String jum_kimia = edtJumKimia.getText().toString().trim();
        final String jum_nabati = edtJumNabati.getText().toString().trim();
        final String non_pestisida = txtNonPestisida.getText().toString().trim();
        final String jum_AH = edtJumlahAH.getText().toString().trim();
        final String jum_CL = edtJumlahCL.getText().toString().trim();
        final String jum_luas_pengendalian = txtJumlahPengendalian.getText().toString().trim();
        final String luas_keadaan = edtLuasKeadaan.getText().toString().trim();
        final String kategori_keadaan = spKategoriKeadaan.getSelectedItem().toString().trim();
        final String intensitas_keadaan = edtIntensitasKeadaan.getText().toString().trim();
        final String jum_luas_keadaan = txtJumlahKeadaan.getText().toString().trim();
        final String frek_kimia = edtJumlahKimia.getText().toString().trim();
        final String frek_nabati = edtJumlahNabati.getText().toString().trim();
        final String luas_waspada = edtLuasWaspada.getText().toString().trim();
        final String lap_lengkap_id = "0";
        String url = Constants.ROOT_URL+"updatelaporan";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(InputDetailLaporan.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                            Intent home = new Intent(InputDetailLaporan.this, InputLapor.class);
                            home.putExtra("HOME", "homepopt");
                            startActivity(home);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InputDetailLaporan.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_laporan", id);
                params.put("kecamatan", kecamatan);
                params.put("desa", desa);
                params.put("luas_tanaman", luas_tanaman);
                params.put("dari_umur", dari_umur);
                params.put("hingga_umur", hingga_umur);
                params.put("luas_serangan", luas_serangan);
                params.put("kategori_serangan", kategori_serangan);
                params.put("intensitas_serangan", intensitas_serangan);
                if (kategori_serangan.contains("Ringan")){
                    params.put("jum_serang_ringan", luas_serangan);
                    params.put("jum_serang_sedang", "0");
                    params.put("jum_serang_berat", "0");
                    params.put("jum_serang_puso", "0");
                } else if (kategori_serangan.contains("Sedang")){
                    params.put("jum_serang_ringan", "0");
                    params.put("jum_serang_sedang", luas_serangan);
                    params.put("jum_serang_berat", "0");
                    params.put("jum_serang_puso", "0");
                } else if (kategori_serangan.contains("Berat")){
                    params.put("jum_serang_ringan", "0");
                    params.put("jum_serang_sedang", "0");
                    params.put("jum_serang_berat", luas_serangan);
                    params.put("jum_serang_puso", "0");
                } else if (kategori_serangan.contains("Puso")){
                    params.put("jum_serang_ringan", "0");
                    params.put("jum_serang_sedang", "0");
                    params.put("jum_serang_berat", "0");
                    params.put("jum_serang_puso", luas_serangan);
                }
                params.put("jum_luas_serangan", jum_luas_serangan);
                params.put("luas_sembuh", luas_terkendali);
                params.put("luas_panen", luas_panen);
                params.put("luas_tambah", luas_tambah);
                params.put("kategori_tambah", kategori_tambah);
                params.put("intensitas_tambah", intensitas_tambah);
                if (kategori_tambah.contains("Ringan")){
                    params.put("jum_tambah_ringan", luas_tambah);
                    params.put("jum_tambah_sedang", "0");
                    params.put("jum_tambah_berat", "0");
                    params.put("jum_tambah_puso", "0");
                } else if (kategori_tambah.contains("Sedang")){
                    params.put("jum_tambah_ringan", "0");
                    params.put("jum_tambah_sedang", luas_tambah);
                    params.put("jum_tambah_berat", "0");
                    params.put("jum_tambah_puso", "0");
                } else if (kategori_tambah.contains("Berat")){
                    params.put("jum_tambah_ringan", "0");
                    params.put("jum_tambah_sedang", "0");
                    params.put("jum_tambah_berat", luas_tambah);
                    params.put("jum_tambah_puso", "0");
                } else if (kategori_tambah.contains("Puso")){
                    params.put("jum_tambah_ringan", "0");
                    params.put("jum_tambah_sedang", "0");
                    params.put("jum_tambah_berat", "0");
                    params.put("jum_tambah_puso", luas_tambah);
                }
                params.put("jum_luas_tambah", jum_luas_tambah);
                params.put("pm", pm);
                params.put("pestisida", pestisida);
                params.put("jum_kimia", jum_kimia);
                params.put("jum_nabati", jum_nabati);
                params.put("non_pestisida", non_pestisida);
                params.put("jum_AH", jum_AH);
                params.put("jum_CL", jum_CL);
                params.put("jum_luas_pengendalian", jum_luas_pengendalian);
                params.put("luas_keadaan", luas_keadaan);
                params.put("kategori_keadaan", kategori_keadaan);
                params.put("intensitas_keadaan", intensitas_keadaan);
                if (kategori_keadaan.contains("Ringan")){
                    params.put("jum_keadaan_ringan", luas_keadaan);
                    params.put("jum_keadaan_sedang", "0");
                    params.put("jum_keadaan_berat", "0");
                    params.put("jum_keadaan_puso", "0");
                } else if (kategori_keadaan.contains("Sedang")){
                    params.put("jum_keadaan_ringan", "0");
                    params.put("jum_keadaan_sedang", luas_keadaan);
                    params.put("jum_keadaan_berat", "0");
                    params.put("jum_keadaan_puso", "0");
                } else if (kategori_keadaan.contains("Berat")){
                    params.put("jum_keadaan_ringan", "0");
                    params.put("jum_keadaan_sedang", "0");
                    params.put("jum_keadaan_berat", luas_keadaan);
                    params.put("jum_keadaan_puso", "0");
                } else if (kategori_keadaan.contains("Puso")){
                    params.put("jum_keadaan_ringan", "0");
                    params.put("jum_keadaan_sedang", "0");
                    params.put("jum_keadaan_berat", "0");
                    params.put("jum_keadaan_puso", luas_keadaan);
                }
                params.put("jum_luas_keadaan", jum_luas_keadaan);
                params.put("frek_kimia", frek_kimia);
                params.put("frek_nabati", frek_nabati);
                params.put("luas_waspada", luas_waspada);
//                params.put("bukti_foto", encodedImage);
                params.put("lap_lengkap_id", lap_lengkap_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InputDetailLaporan.this);
        requestQueue.add(request);
    }


    private void insertData() {
        progressDialog.setTitle("Insert Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        String url = Constants.ROOT_URL+"desa";
        final String kecamatan = Common.currentUser.getAlamat();
        final String desa = txtDesa.getText().toString().trim();
        final String luas_tanaman = edtLuasTanaman.getText().toString().trim();
        final String dari_umur = edtUmurAwal.getText().toString().trim();
        final String hingga_umur = edtUmurAkhir.getText().toString().trim();
        final String luas_serangan = edtLuasSerangan.getText().toString().trim();
        final String kategori_serangan = spKategori.getSelectedItem().toString().trim();
        final String intensitas_serangan = edtIntensitas.getText().toString().trim();
        final String jum_luas_serangan = txtJumlahSerangan.getText().toString().trim();
        final String luas_terkendali = edtLuasSembuh.getText().toString().trim();
        final String luas_panen = edtLuasPanen.getText().toString().trim();
        final String luas_tambah = edtLuasTambah.getText().toString().trim();
        final String kategori_tambah = spKategoriTambah.getSelectedItem().toString().trim();
        final String intensitas_tambah = edtIntensitasTambah.getText().toString().trim();
        final String jum_luas_tambah = txtJumlahTambah.getText().toString().trim();
        final String pm = edtPM.getText().toString().trim();
        final String pestisida = txtPestisida.getText().toString().trim();
        final String jum_kimia = edtJumKimia.getText().toString().trim();
        final String jum_nabati = edtJumNabati.getText().toString().trim();
        final String non_pestisida = txtNonPestisida.getText().toString().trim();
        final String jum_AH = edtJumlahAH.getText().toString().trim();
        final String jum_CL = edtJumlahCL.getText().toString().trim();
        final String jum_luas_pengendalian = txtJumlahPengendalian.getText().toString().trim();
        final String luas_keadaan = edtLuasKeadaan.getText().toString().trim();
        final String kategori_keadaan = spKategoriKeadaan.getSelectedItem().toString().trim();
        final String intensitas_keadaan = edtIntensitasKeadaan.getText().toString().trim();
        final String jum_luas_keadaan = txtJumlahKeadaan.getText().toString().trim();
        final String frek_kimia = edtJumlahKimia.getText().toString().trim();
        final String frek_nabati = edtJumlahNabati.getText().toString().trim();
        final String luas_waspada = edtLuasWaspada.getText().toString().trim();
        final String lap_lengkap_id = "0";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(InputDetailLaporan.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            Intent home = new Intent(InputDetailLaporan.this, InputLapor.class);
                            home.putExtra("HOME", "homepopt");
                            startActivity(home);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InputDetailLaporan.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kecamatan", kecamatan);
                params.put("desa", desa);
                params.put("luas_tanaman", luas_tanaman);
                params.put("dari_umur", dari_umur);
                params.put("hingga_umur", hingga_umur);
                params.put("luas_serangan", luas_serangan);
                params.put("kategori_serangan", kategori_serangan);
                params.put("intensitas_serangan", intensitas_serangan);
                if (kategori_serangan.contains("Ringan")){
                    params.put("jum_serang_ringan", luas_serangan);
                    params.put("jum_serang_sedang", "0");
                    params.put("jum_serang_berat", "0");
                    params.put("jum_serang_puso", "0");
                } else if (kategori_serangan.contains("Sedang")){
                    params.put("jum_serang_ringan", "0");
                    params.put("jum_serang_sedang", luas_serangan);
                    params.put("jum_serang_berat", "0");
                    params.put("jum_serang_puso", "0");
                } else if (kategori_serangan.contains("Berat")){
                    params.put("jum_serang_ringan", "0");
                    params.put("jum_serang_sedang", "0");
                    params.put("jum_serang_berat", luas_serangan);
                    params.put("jum_serang_puso", "0");
                } else if (kategori_serangan.contains("Puso")){
                    params.put("jum_serang_ringan", "0");
                    params.put("jum_serang_sedang", "0");
                    params.put("jum_serang_berat", "0");
                    params.put("jum_serang_puso", luas_serangan);
                }
                params.put("jum_luas_serangan", jum_luas_serangan);
                params.put("luas_sembuh", luas_terkendali);
                params.put("luas_panen", luas_panen);
                params.put("luas_tambah", luas_tambah);
                params.put("kategori_tambah", kategori_tambah);
                params.put("intensitas_tambah", intensitas_tambah);
                if (kategori_tambah.contains("Ringan")){
                    params.put("jum_tambah_ringan", luas_tambah);
                    params.put("jum_tambah_sedang", "0");
                    params.put("jum_tambah_berat", "0");
                    params.put("jum_tambah_puso", "0");
                } else if (kategori_tambah.contains("Sedang")){
                    params.put("jum_tambah_ringan", "0");
                    params.put("jum_tambah_sedang", luas_tambah);
                    params.put("jum_tambah_berat", "0");
                    params.put("jum_tambah_puso", "0");
                } else if (kategori_tambah.contains("Berat")){
                    params.put("jum_tambah_ringan", "0");
                    params.put("jum_tambah_sedang", "0");
                    params.put("jum_tambah_berat", luas_tambah);
                    params.put("jum_tambah_puso", "0");
                } else if (kategori_tambah.contains("Puso")){
                    params.put("jum_tambah_ringan", "0");
                    params.put("jum_tambah_sedang", "0");
                    params.put("jum_tambah_berat", "0");
                    params.put("jum_tambah_puso", luas_tambah);
                }
                params.put("jum_luas_tambah", jum_luas_tambah);
                params.put("pm", pm);
                params.put("pestisida", pestisida);
                params.put("jum_kimia", jum_kimia);
                params.put("jum_nabati", jum_nabati);
                params.put("non_pestisida", non_pestisida);
                params.put("jum_AH", jum_AH);
                params.put("jum_CL", jum_CL);
                params.put("jum_luas_pengendalian", jum_luas_pengendalian);
                params.put("luas_keadaan", luas_keadaan);
                params.put("kategori_keadaan", kategori_keadaan);
                params.put("intensitas_keadaan", intensitas_keadaan);
                if (kategori_keadaan.contains("Ringan")){
                    params.put("jum_keadaan_ringan", luas_keadaan);
                    params.put("jum_keadaan_sedang", "0");
                    params.put("jum_keadaan_berat", "0");
                    params.put("jum_keadaan_puso", "0");
                } else if (kategori_keadaan.contains("Sedang")){
                    params.put("jum_keadaan_ringan", "0");
                    params.put("jum_keadaan_sedang", luas_keadaan);
                    params.put("jum_keadaan_berat", "0");
                    params.put("jum_keadaan_puso", "0");
                } else if (kategori_keadaan.contains("Berat")){
                    params.put("jum_keadaan_ringan", "0");
                    params.put("jum_keadaan_sedang", "0");
                    params.put("jum_keadaan_berat", luas_keadaan);
                    params.put("jum_keadaan_puso", "0");
                } else if (kategori_keadaan.contains("Puso")){
                    params.put("jum_keadaan_ringan", "0");
                    params.put("jum_keadaan_sedang", "0");
                    params.put("jum_keadaan_berat", "0");
                    params.put("jum_keadaan_puso", luas_keadaan);
                }
                params.put("jum_luas_keadaan", jum_luas_keadaan);
                params.put("frek_kimia", frek_kimia);
                params.put("frek_nabati", frek_nabati);
                params.put("luas_waspada", luas_waspada);
                params.put("bukti_foto", encodedImage);
                params.put("lap_lengkap_id", lap_lengkap_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InputDetailLaporan.this);
        requestQueue.add(request);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(InputDetailLaporan.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void saveBitmapToJPG(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] imageBytes = stream.toByteArray();

        encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img_bukti.setImageBitmap(bitmap);
                saveBitmapToJPG(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
