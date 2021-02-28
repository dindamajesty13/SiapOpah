package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class DetailStock extends AppCompatActivity {
    TextView txtPestisida, txtTanggal, txtStockAwal, txtPenggunaan, txtStockAkhir, txtProvinsi, txtKabupaten, txtNama, txtTambahan, txtKeterangan, txtSasaran;
    String id_stock;
    ImageView img_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_stock);

        txtPestisida = (TextView) findViewById(R.id.txtPestisida);
        txtTanggal = (TextView) findViewById(R.id.txtDate);
        txtStockAwal = (TextView) findViewById(R.id.txtStockAwal);
        txtPenggunaan = (TextView) findViewById(R.id.txtPenggunaan);
        txtStockAkhir = (TextView) findViewById(R.id.txtStockAkhir);
        txtProvinsi = (TextView) findViewById(R.id.txtProvinsi);
        txtKabupaten = (TextView) findViewById(R.id.txtKabupaten);
        txtNama = (TextView) findViewById(R.id.txtNama);
        txtTambahan = (TextView) findViewById(R.id.txtTambahan);
        txtKeterangan = (TextView) findViewById(R.id.txtKeterangan);
        txtSasaran = (TextView) findViewById(R.id.txtSasaran);
        img_edit = (ImageView) findViewById(R.id.img_edit);

        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_stock = getIntent().getStringExtra("id");
                Intent intent = new Intent(DetailStock.this, Stock.class);
                intent.putExtra("id_stock", id_stock);
                startActivity(intent);
            }
        });

        id_stock = getIntent().getStringExtra("id");

        String urlLaporan = Constants.ROOT_URL+"Stock?id="+id_stock;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String tanggal = object.getString("tanggal_stock");
                                String pestisida = object.getString("pestisida");
                                String provinsi = object.getString("provinsi");
                                String kabupaten = object.getString("kabupaten");
                                String sasaran = object.getString("sasaran");
                                String stock_awal = object.getString("stock_awal");
                                String stock_akhir = object.getString("stock_akhir");
                                String nama = object.getString("nama_produk");
                                String tambahan = object.getString("tambahan");
                                String penggunaan = object.getString("penggunaan");
                                String keterangan = object.getString("keterangan");

                                txtPestisida.setText(Html.fromHtml("<font color='#000000'><b>Pestisida: </b><br></font>" + pestisida));
                                txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Tanggal: </b><br></font>" + tanggal));
                                txtProvinsi.setText(Html.fromHtml("<font color='#000000'><b>Provinsi: </b><br>" + provinsi));
                                txtPenggunaan.setText(Html.fromHtml("<font color='#000000'><b>Penggunaan: </b><br>" + penggunaan+" Liter/Kg"));
                                txtKabupaten.setText(Html.fromHtml("<font color='#000000'><b>Kabupaten: </b><br>" + kabupaten));
                                txtStockAwal.setText(Html.fromHtml("<font color='#000000'><b>Stock Awal: </b><br>" + stock_awal+" Liter/Kg"));
                                txtStockAkhir.setText(Html.fromHtml("<font color='#000000'><b>Stock Akhir: </b><br>" + stock_akhir+" Liter/Kg"));
                                txtPenggunaan.setText(Html.fromHtml("<font color='#000000'><b>Penggunaan: </b><br>" + penggunaan+" Liter/Kg"));
                                txtTambahan.setText(Html.fromHtml("<font color='#000000'><b>Tambahan: </b><br>" + tambahan+" Liter/Kg"));
                                txtNama.setText(Html.fromHtml("<font color='#000000'><b>Nama Produk: </b><br>" + nama));
                                txtKeterangan.setText(Html.fromHtml("<font color='#000000'><b>Keterangan: </b><br>" + keterangan));
                                txtSasaran.setText(Html.fromHtml("<font color='#000000'><b>Sasaran: </b><br>" + sasaran));


                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(DetailStock.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailStock.this).add(stringRequest);
    }

//    private void getDataStockKab() {
//        id_stock = getIntent().getStringExtra("id");
//        String url = Constants.ROOT_URL + "Insert_Stock?id=" + id_stock;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray array = new JSONArray(response);
//                            for (int i = 0; i < array.length(); i++) {
//                                JSONObject object = array.getJSONObject(i);
//
//                                String kabupaten = object.getString("kabupaten");
//                                String jumlah = object.getString("jumlah_pestisida");
//                                String id = object.getString("id_wilayah");
//
//                                ModelStockKab modelStockKab = new ModelStockKab();
//                                modelStockKab.setId_wilayah(id);
//                                modelStockKab.setKabupaten(kabupaten);
//                                modelStockKab.setJumlah_pestisida(jumlah);
//                                listStockKab.add(modelStockKab);
//
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        adapter = new StockKabAdapter(DetailStock.this, listStockKab);
//                        recyclerView.setAdapter(adapter);
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
////                Toast.makeText(DetailStock.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        Volley.newRequestQueue(DetailStock.this).add(stringRequest);
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
