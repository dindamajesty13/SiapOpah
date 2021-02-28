package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.majesty.siapopa.adapter.OptAdapter;
import com.majesty.siapopa.adapter.PopulasiAdapter;
import com.majesty.siapopa.adapter.ShowOptAdapter;
import com.majesty.siapopa.model.ModelOpt;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowDetailPengamatan extends AppCompatActivity {
    TextView txtProvinsi, txtKabupaten, txtKecamatan, txtDesa, txtJumlahAnakan, txtPbp, txtTikus,
            txtGanjur, txtTungro, txtKhKr, txtBp, txtNBlas, txtBlb, txtBrs, txtWbc, txtBlas, txtImago_wbc,
            txtNimfa_wbc, txtJumlah_wbc, txtImago_wpp, txtNimfa_wpp, txtJumlah_wpp, txtImago_wdh,
            txtNimfa_wdh, txtJumlah_wdh, txtImago_pbp, txtNimfa_pbp, txtJumlah_pbp, txtImago_ug,
            txtNimfa_ug, txtJumlah_ug, txtopms, txtdomi, txtkisar, txtluas_spot, txtTanggal;

    String id_detail;
    List<ModelOpt> listOPTMutlak, listOPTTdkMutlak;
    private ProgressDialog progressDialog;

    List<ModelOpt>listPopulasi, listMA;
    RecyclerView.LayoutManager layoutManager1, layoutManager2, layoutManager3, layoutManager4;
    private RecyclerView.Adapter adapterMutlak, adapterTdkMutlak, adapterPopulasi, adapterMA;
    RecyclerView rvMutlak, rvTdkMutlak, rvPopulasi, rvMA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_pengamatan);

        txtProvinsi = (TextView) findViewById(R.id.txtProvinsi);
        txtKabupaten = (TextView) findViewById(R.id.txtKabupaten);
        txtKecamatan = (TextView) findViewById(R.id.txtKecamatan);
        txtDesa = (TextView) findViewById(R.id.txtDesa);
        txtJumlahAnakan = (TextView) findViewById(R.id.txtJumlahAnakan);
        txtluas_spot = (TextView) findViewById(R.id.txt_luas_spot);
        txtTanggal = (TextView) findViewById(R.id.txtTanggal);
        rvMutlak = (RecyclerView) findViewById(R.id.rvMutlak);
        rvTdkMutlak = (RecyclerView) findViewById(R.id.rvTdkmutlak);
        rvPopulasi = (RecyclerView) findViewById(R.id.rvPopulasi);
        rvMA = (RecyclerView) findViewById(R.id.rvMA);
        progressDialog = new ProgressDialog(this);

        id_detail = getIntent().getStringExtra("id_detail");

        getDataDetail();

        rvMutlak.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(ShowDetailPengamatan.this);
        rvMutlak.setLayoutManager(layoutManager1);
        listOPTMutlak = new ArrayList<>();

        getDataMutlak();

        rvTdkMutlak.setHasFixedSize(true);
        layoutManager2 = new LinearLayoutManager(ShowDetailPengamatan.this);
        rvTdkMutlak.setLayoutManager(layoutManager2);
        listOPTTdkMutlak = new ArrayList<>();

        getDataTdkMutlak();

        rvPopulasi.setHasFixedSize(true);
        layoutManager3 = new LinearLayoutManager(ShowDetailPengamatan.this);
        rvPopulasi.setLayoutManager(layoutManager3);
        listPopulasi = new ArrayList<>();

        getDataPopulasi();

        rvMA.setHasFixedSize(true);
        layoutManager4 = new LinearLayoutManager(ShowDetailPengamatan.this);
        rvMA.setLayoutManager(layoutManager4);
        listMA = new ArrayList<>();

        getDataPopulasiMA();
    }

    private void getDataPopulasi() {
        String username = Common.currentUser.getUsername();
        id_detail = getIntent().getStringExtra("id_detail");
        String url = Constants.ROOT_URL + "Opt?username=" + username + "&mutlak=populasi&id_detail=" + id_detail;
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
                        adapterPopulasi = new PopulasiAdapter(ShowDetailPengamatan.this, listPopulasi);
                        rvPopulasi.setAdapter(adapterPopulasi);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ShowDetailPengamatan.this).add(stringRequest);
    }

    private void getDataPopulasiMA() {
        String username = Common.currentUser.getUsername();
        id_detail = getIntent().getStringExtra("id_detail");
        String url = Constants.ROOT_URL + "Opt?username=" + username + "&mutlak=MA&id_detail=" + id_detail;
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
                        adapterMA = new OptAdapter(ShowDetailPengamatan.this, listMA);
                        rvMA.setAdapter(adapterMA);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(InputDetailHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ShowDetailPengamatan.this).add(stringRequest);
    }

    private void getDataTdkMutlak() {
        progressDialog.setTitle("Loading");
        progressDialog.show();
        String username = Common.currentUser.getUsername();
        id_detail = getIntent().getStringExtra("id_detail");
        String url = Constants.ROOT_URL+"Opt?mutlak=tidak mutlak&id_detail="+id_detail;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String opt = object.getString("opt");
                                String jumlah = object.getString("jumlah");
                                String id = object.getString("id_jenisOPT");

                                ModelOpt modelOpt = new ModelOpt();
                                modelOpt.setId_jenisOPT(id);
                                modelOpt.setOpt(opt);
                                modelOpt.setJumlah(jumlah);
                                listOPTTdkMutlak.add(modelOpt);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        adapterTdkMutlak = new ShowOptAdapter(ShowDetailPengamatan.this, listOPTTdkMutlak);
                        rvTdkMutlak.setAdapter(adapterTdkMutlak);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ShowDetailPengamatan.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ShowDetailPengamatan.this).add(stringRequest);
    }

    private void getDataMutlak() {
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        date = dateFormat.format(calendar.getTime());
        progressDialog.setTitle("Loading");
        progressDialog.show();
        id_detail = getIntent().getStringExtra("id_detail");
        String username = Common.currentUser.getUsername();
        String url = Constants.ROOT_URL+"Opt?mutlak=mutlak&id_detail="+id_detail;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String opt = object.getString("opt");
                                String jumlah = object.getString("jumlah");
                                String id = object.getString("id_jenisOPT");

                                ModelOpt modelOpt = new ModelOpt();
                                modelOpt.setId_jenisOPT(id);
                                modelOpt.setOpt(opt);
                                modelOpt.setJumlah(jumlah);
                                listOPTMutlak.add(modelOpt);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        adapterMutlak = new ShowOptAdapter(ShowDetailPengamatan.this, listOPTMutlak);
                        rvMutlak.setAdapter(adapterMutlak);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ShowDetailPengamatan.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ShowDetailPengamatan.this).add(stringRequest);
    }

    private void getDataDetail() {
        progressDialog.setTitle("Loading");
        progressDialog.show();
        String url = Constants.ROOT_URL+"Detail_Pengamatan?id="+id_detail;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String provinsi = object.getString("provinsi");
                                String kabupaten = object.getString("kabupaten");
                                String kecamatan = object.getString("kecamatan");
//                                String desa = object.getString("desa");
                                String jumlah_anakan = object.getString("jumlah_anakan");
//                                String pbp = object.getString("pbp");
//                                String tikus = object.getString("tikus");
//                                String ganjur = object.getString("ganjur");
//                                String tungro = object.getString("tungro");
//                                String kh_kr = object.getString("kh_kr");
//                                String bp = object.getString("bp");
//                                String n_blas = object.getString("n_blas");
//                                String blb = object.getString("blb");
//                                String brs = object.getString("brs");
//                                String wbc = object.getString("wbc");
//                                String blas = object.getString("blas");
//                                String imago_wbc = object.getString("imago_wbc");
//                                String nimfa_wbc = object.getString("nimfa_wbc");
//                                String jumlah_wbc = object.getString("jumlah_wbc");
//                                String imago_wpp = object.getString("imago_wpp");
//                                String nimfa_wpp = object.getString("nimfa_wpp");
//                                String jumlah_wpp = object.getString("jumlah_wpp");
//                                String imago_wdh = object.getString("imago_wdh");
//                                String nimfa_wdh = object.getString("nimfa_wdh");
//                                String jumlah_wdh = object.getString("jumlah_wdh");
//                                String kr_pbp = object.getString("kr_pbp");
//                                String kt_pbp = object.getString("kt_pbp");
//                                String jumlah_pbp = object.getString("jumlah_pbp");
//                                String imago_ug = object.getString("imago_ug");
//                                String nimfa_ug = object.getString("nimfa_ug");
//                                String jumlah_ug = object.getString("jumlah_ug");
//                                String opms = object.getString("opms");
//                                String domi = object.getString("domi");
//                                String kisar = object.getString("kisar");
                                String luas_spot_hopperburn = object.getString("luas_spot_hopperburn");
//                                String bukti_foto = object.getString("bukti_foto");
                                String tanggal_detail = object.getString("tanggal_detail");
//                                String id_hasil_pengamatan = object.getString("id_hasil_pengamatan");

                                txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Tanggal: </b><br></font>" + tanggal_detail));
                                txtProvinsi.setText(Html.fromHtml("<font color='#000000'><b>Provinsi: </b><br></font>" + provinsi));
                                txtKabupaten.setText(Html.fromHtml("<font color='#000000'><b>Kabupaten: </b><br></font>" + kabupaten));
                                txtKecamatan.setText(Html.fromHtml("<font color='#000000'><b>Kecamatan: </b><br></font>" + kecamatan));
//                                txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Desa: </b><br></font>" + desa));
                                txtJumlahAnakan.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Anakan: </b><br></font>" + jumlah_anakan));
//                                txtPbp.setText(Html.fromHtml("<font color='#000000'><b>PBP: </b><br></font>" + pbp));
//                                txtTikus.setText(Html.fromHtml("<font color='#000000'><b>Tikus: </b><br></font>" + tikus));
//                                txtGanjur.setText(Html.fromHtml("<font color='#000000'><b>Ganjur: </b><br></font>" + ganjur));
//                                txtTungro.setText(Html.fromHtml("<font color='#000000'><b>Tungro: </b><br></font>" + tungro));
//                                txtKhKr.setText(Html.fromHtml("<font color='#000000'><b>KH/KR: </b><br></font>" + kh_kr));
//                                txtBp.setText(Html.fromHtml("<font color='#000000'><b>BP: </b><br></font>" + bp));
//                                txtNBlas.setText(Html.fromHtml("<font color='#000000'><b>N.Blas: </b><br></font>" + n_blas));
//                                txtBlb.setText(Html.fromHtml("<font color='#000000'><b>BLB: </b><br></font>" + blb));
//                                txtBrs.setText(Html.fromHtml("<font color='#000000'><b>BRS: </b><br></font>" + brs));
//                                txtWbc.setText(Html.fromHtml("<font color='#000000'><b>WBC: </b><br></font>" + wbc));
//                                txtBlas.setText(Html.fromHtml("<font color='#000000'><b>Blas: </b><br></font>" + blas));
//                                txtImago_wbc.setText(Html.fromHtml("<font color='#000000'><b>Imago: </b><br></font>" + imago_wbc));
//                                txtNimfa_wbc.setText(Html.fromHtml("<font color='#000000'><b>Nimfa: </b><br></font>" + nimfa_wbc));
//                                txtJumlah_wbc.setText(Html.fromHtml("<font color='#000000'><b>Jumlah: </b><br></font>" + jumlah_wbc));
//                                txtImago_wpp.setText(Html.fromHtml("<font color='#000000'><b>Imago: </b><br></font>" + imago_wpp));
//                                txtNimfa_wpp.setText(Html.fromHtml("<font color='#000000'><b>Nimfa: </b><br></font>" + nimfa_wpp));
//                                txtJumlah_wpp.setText(Html.fromHtml("<font color='#000000'><b>Jumlah: </b><br></font>" + jumlah_wpp));
//                                txtImago_wdh.setText(Html.fromHtml("<font color='#000000'><b>Imago: </b><br></font>" + imago_wdh));
//                                txtNimfa_wdh.setText(Html.fromHtml("<font color='#000000'><b>Nimfa: </b><br></font>" + nimfa_wdh));
//                                txtJumlah_wdh.setText(Html.fromHtml("<font color='#000000'><b>Jumlah: </b><br></font>" + jumlah_wdh));
//                                txtImago_pbp.setText(Html.fromHtml("<font color='#000000'><b>Imago: </b><br></font>" + kr_pbp));
//                                txtNimfa_pbp.setText(Html.fromHtml("<font color='#000000'><b>Nimfa: </b><br></font>" + kt_pbp));
//                                txtJumlah_pbp.setText(Html.fromHtml("<font color='#000000'><b>Jumlah: </b><br></font>" + jumlah_pbp));
//                                txtImago_ug.setText(Html.fromHtml("<font color='#000000'><b>Imago: </b><br></font>" + imago_ug));
//                                txtNimfa_ug.setText(Html.fromHtml("<font color='#000000'><b>Nimfa: </b><br></font>" + nimfa_ug));
//                                txtJumlah_ug.setText(Html.fromHtml("<font color='#000000'><b>Jumlah: </b><br></font>" + jumlah_ug));
//                                txtopms.setText(Html.fromHtml("<font color='#000000'><b>OPMS: </b><br></font>" + opms));
//                                txtdomi.setText(Html.fromHtml("<font color='#000000'><b>Domi: </b><br></font>" + domi));
//                                txtkisar.setText(Html.fromHtml("<font color='#000000'><b>Kisar: </b><br></font>" + kisar));
                                txtluas_spot.setText(Html.fromHtml("<font color='#000000'><b>Luas Spot Hopperburn: </b><br></font>" + luas_spot_hopperburn));

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
                Toast.makeText(ShowDetailPengamatan.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ShowDetailPengamatan.this).add(stringRequest);
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
