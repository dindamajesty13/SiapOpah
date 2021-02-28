package com.majesty.siapopa;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.majesty.siapopa.model.SessionManager;
import com.majesty.siapopa.model.SumModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfView extends AppCompatActivity {
    PDFView pdfView;
    ProgressDialog mProgressDialog, progressDialog;
    public Context context;
    public String samplePDF, id_hasil, date, userAlamat, id_detail, hari, id_intensitas;
    Button btn_edit, btn_submit;
    private Calendar calendar;
    RequestQueue requestQueue;
    private SimpleDateFormat dateFormat;
    public static final int PERMISSIONS_MULTIPLE_REQUEST_CERTIFICATE = 123;
    List<String> listOPT, listOPT2, listPopulasi, listPopulasiMP, listPopulasiMA;
    String optMutlak;
    String jumlahMutlak;
    String optTdkMutlak;
    String jumlahTdkMutlak;
    String opt1;
    String opt2;
    String jumlahPopulasi;
    String populasi;
    String optMA;
    String jumlahMA;
    String id_jenisMA;
    String populasiMP;
    String jumlahPopulasiMP;
    String id_hsl_stgh;
    StringBuilder optMa, jumlahMa;
    LinearLayout linearLayout;
    float total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        btn_edit = findViewById(R.id.btn_edit);
        btn_submit = findViewById(R.id.btn_submit);
        linearLayout = findViewById(R.id.linearLayout);

        progressDialog = new ProgressDialog(PdfView.this);
        calendar = Calendar.getInstance();

        pdfView = findViewById(R.id.pdfView);
        if (getIntent().getStringExtra("surat") != null){
            samplePDF = Constants.ROOT_URL+"assets/Documents/"+getIntent().getStringExtra("surat");
            checkAndroidVersionCertificate();
        }else if (getIntent().getStringExtra("kartu") != null){
            samplePDF = Constants.ROOT_URL+"uploads/"+getIntent().getStringExtra("kartu");
            checkAndroidVersionCertificate();
        }else if (getIntent().getStringExtra("bulanan") != null){
            showBulanan();
        }else if (getIntent().getStringExtra("musiman") != null){
            showMusiman();
        }else if (getIntent().getStringExtra("path") != null){
            showFile();

            requestQueue = Volley.newRequestQueue(this);
            id_hasil = getIntent().getStringExtra("id_hasil");

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            btn_submit.setText("Simpan");

            linearLayout.setVisibility(View.VISIBLE);

            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int total = getIntent().getIntExtra("total", 0);
                    if (total == 30){
                        AlertDialog.Builder builder = new AlertDialog.Builder(PdfView.this);
                        builder.setTitle("Insert Data");
                        builder.setMessage("Apakah Anda Yakin?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: Do something, when user click on the positive button

                                selectIDSum();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                // TODO: Do something, when user click on the positive button
                                dialog.cancel();
                            }
                        });
                        builder.create().show();

                    }else if (total < 30){
//                    Toast.makeText(InputHarian.this, "Data Tunas Kurang Dari 30", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(PdfView.this);
                        builder.setTitle("Data Kurang Dari 30");
                        builder.setMessage("Mohon Inputkan Tambahan Data!");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: Do something, when user click on the positive button
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }

                }
            });
        }
        else if (getIntent().getStringExtra("setengah") != null){
            viewSetengah();

            id_hsl_stgh = getIntent().getStringExtra("id_hsl_stgh");

            requestQueue = Volley.newRequestQueue(this);
            btn_edit.setVisibility(View.GONE);
            btn_submit.setText("Laporkan");
//            btn_submit.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);


            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateDataSetBulan();
                }
//                    int total = getIntent().getIntExtra("total", 0);
//                    if (total == 30){
//                        AlertDialog.Builder builder = new AlertDialog.Builder(PdfView.this);
//                        builder.setTitle("Insert Data");
//                        builder.setMessage("Apakah Anda Yakin?");
//                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // TODO: Do something, when user click on the positive button
//
//
//                            }
//                        });
//                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override public void onClick(DialogInterface dialog, int which) {
//                                // TODO: Do something, when user click on the positive button
//                                dialog.cancel();
//                            }
//                        });
//                        builder.create().show();
//
//                    }else if (total < 30){
////                    Toast.makeText(InputHarian.this, "Data Tunas Kurang Dari 30", Toast.LENGTH_SHORT).show();
//                        AlertDialog.Builder builder = new AlertDialog.Builder(PdfView.this);
//                        builder.setTitle("Data Kurang Dari 30");
//                        builder.setMessage("Mohon Inputkan Tambahan Data!");
//                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // TODO: Do something, when user click on the positive button
//                                dialog.dismiss();
//                            }
//                        });
//                        builder.create().show();
//                    }
//                }
            });
        }
        else if (getIntent().getStringExtra("view") != null){
            viewFile();
            requestQueue = Volley.newRequestQueue(this);
            id_hasil = getIntent().getStringExtra("id_hasil");
            btn_edit.setVisibility(View.GONE);

            linearLayout.setVisibility(View.VISIBLE);

            id_intensitas = getIntent().getStringExtra("id_intensitas");

            String status = getIntent().getStringExtra("status");
            if (status.equals("Sudah")){
                btn_submit.setVisibility(View.GONE);
            }else {
                btn_submit.setText("Laporkan");

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateDataIntensitas();
                    }
                });
            }

        }
        else if (getIntent().getStringExtra("validasi") != null){
            validasiFile();
            requestQueue = Volley.newRequestQueue(this);
            btn_edit.setVisibility(View.GONE);

            linearLayout.setVisibility(View.VISIBLE);

            id_intensitas = getIntent().getStringExtra("id_intensitas");

            String status = getIntent().getStringExtra("status");
            if (status.equals("Sudah") || status.equals("Sudah (Approve by Provinsi)")){
                btn_submit.setVisibility(View.GONE);
            }else {
                btn_submit.setText("Validasi");

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Common.currentUser.getId_usergroup().equals("2")){
                            updateStatusApproval();
                        }else if (Common.currentUser.getId_usergroup().equals("3")) {
                            updateStatusAppSatpel();
                        }else if (Common.currentUser.getId_usergroup().equals("4")) {
                            updateStatusApprovalProv();
                        }
                    }
                });
            }

        }
        else if (getIntent().getStringExtra("validasi_stgh") != null){
            validasiStghFile();
            requestQueue = Volley.newRequestQueue(this);
            btn_edit.setVisibility(View.GONE);

            linearLayout.setVisibility(View.VISIBLE);

            id_intensitas = getIntent().getStringExtra("id_hsl_stgh");

            String status = getIntent().getStringExtra("status");
            if (status.equals("Sudah") || status.equals("Validasi")){
                btn_submit.setVisibility(View.GONE);
            }else {
                btn_submit.setText("Validasi");

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Common.currentUser.getId_usergroup().equals("2")){
                            updateStatusApprovalStgh();
                        }else if (Common.currentUser.getId_usergroup().equals("3")) {
                            updateStatusAppSatpelStgh();
                        }else if (Common.currentUser.getId_usergroup().equals("4")) {
                            updateStatusApprovalProvStgh();
                        }
                    }
                });
            }

        }
        else if (getIntent().getStringExtra("hasil") != null){
            showHasil();
            requestQueue = Volley.newRequestQueue(this);
            linearLayout.setVisibility(View.GONE);
        }

        context = PdfView.this;
        mProgressDialog = new ProgressDialog(context);
    }

    private void validasiStghFile() {
        String path = getIntent().getStringExtra("validasi_stgh");
        File file = new File(Environment.getExternalStorageDirectory() .getAbsolutePath()  + "/siap_opa/setengah_bulan/"+path);
        if (file.exists()) {
            pdfView.fromFile(file)  //.pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) .swipeHorizontal(true) .enableDoubletap(true) .defaultPage(0) .enableAnnotationRendering(true) .password(null) .scrollHandle(null) .onLoad(new OnLoadCompleteListener() {
                @Override  public void loadComplete(int nbPages) {
                    pdfView.setMinZoom(1f);
                    pdfView.setMidZoom(5f);
                    pdfView.setMaxZoom(10f);
                    pdfView.zoomTo(2f);
                    pdfView.scrollTo(0, 0);
                    pdfView.moveTo(0f, 0f);
                }
            }) .load();
        }
    }

    private void updateStatusAppSatpelStgh() {
        progressDialog.setTitle("Approve Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        final String satpel_to_popt = getIntent().getStringExtra("msg_to_popt");
        final String satpel_to_kab = getIntent().getStringExtra("msg_to_kab");
        String url = Constants.ROOT_URL+"Approvalsat";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            insertNotificationStghProv();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(DetailLaporan.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_hsl_stgh", id_intensitas);
                params.put("approval_satpel", "Sudah");
                params.put("satpel_to_popt", satpel_to_popt);
                params.put("satpel_to_kab", satpel_to_kab);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PdfView.this);
        requestQueue.add(request);
    }

    private void insertNotificationStghProv() {
        String url = Constants.ROOT_URL+"Notification";
        final String username = Common.currentUser.getProvinsi();
        StringRequest jobReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            insertNotificationPOPTStgh();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", "Belum");
                params.put("id_laporan", id_intensitas);
                params.put("title", "Laporan Butuh Validasi");
                params.put("msg", "Mohon validasi laporan ini sebelum tanggal yang ditentukan");
                params.put("laporan", "Setengah");
                params.put("username", username);
                params.put("towho", "Provinsi");
                return params;
            }
        };

        requestQueue.add(jobReq);
    }

    private void updateStatusApprovalStgh() {
        progressDialog.setTitle("Approve Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        final String kab_to_popt = getIntent().getStringExtra("msg_to_popt");
        String url = Constants.ROOT_URL+"Approval";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            insertNotificationStgh();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(DetailLaporan.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_hsl_stgh", id_intensitas);
                params.put("approval_kab", "Sudah");
                params.put("kab_to_popt", kab_to_popt);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PdfView.this);
        requestQueue.add(request);
    }

    private void insertNotificationStgh() {
        String url = Constants.ROOT_URL+"Notification";
        final String username = Common.currentUser.getKabupaten();
        StringRequest jobReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            insertNotificationPOPTStgh();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", "Belum");
                params.put("id_laporan", id_intensitas);
                params.put("title", "Laporan Butuh Validasi");
                params.put("msg", "Mohon validasi laporan ini sebelum tanggal yang ditentukan");
                params.put("laporan", "Setengah");
                params.put("username", username);
                params.put("towho", "SatPel");
                return params;
            }
        };

        requestQueue.add(jobReq);
    }

    private void insertNotificationPOPTStgh() {
        String url = Constants.ROOT_URL+"Notification";
        final String username = Common.currentUser.getKabupaten();
        StringRequest jobReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(PdfView.this, "Data Berhasil diValidasi", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", "Belum");
                params.put("id_laporan", id_intensitas);
                params.put("title", "Laporan Telah diValidasi");
                params.put("msg", "Laporan ini Telah diValidasi");
                params.put("laporan", "Setengah");
                params.put("username", username);
                params.put("towho", "POPT");
                return params;
            }
        };

        requestQueue.add(jobReq);
    }

    private void updateStatusApprovalProvStgh() {
        progressDialog.setTitle("Approve Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        final String prov_to_popt = getIntent().getStringExtra("msg_to_popt");
        final String prov_to_kab = getIntent().getStringExtra("msg_to_kab");
        final String prov_to_satpel = getIntent().getStringExtra("msg_to_satpel");
        String url = Constants.ROOT_URL+"ApprovalProv";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            insertNotificationPOPTStgh();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PdfView.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_hsl_stgh", id_intensitas);
                params.put("approval_provinsi", "Sudah");
                params.put("prov_to_popt", prov_to_popt);
                params.put("prov_to_kab", prov_to_kab);
                params.put("prov_to_satpel", prov_to_satpel);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PdfView.this);
        requestQueue.add(request);
    }

    private void validasiFile() {
        String path = getIntent().getStringExtra("validasi");
        File file = new File(Environment.getExternalStorageDirectory() .getAbsolutePath()  + "/siap_opa/harian/"+path);
        if (file.exists()) {
            pdfView.fromFile(file)  //.pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) .swipeHorizontal(true) .enableDoubletap(true) .defaultPage(0) .enableAnnotationRendering(true) .password(null) .scrollHandle(null) .onLoad(new OnLoadCompleteListener() {
                @Override  public void loadComplete(int nbPages) {
                    pdfView.setMinZoom(1f);
                    pdfView.setMidZoom(5f);
                    pdfView.setMaxZoom(10f);
                    pdfView.zoomTo(2f);
                    pdfView.scrollTo(0, 0);
                    pdfView.moveTo(0f, 0f);
                }
            }) .load();
        }
    }

    private void showBulanan() {
        String path = getIntent().getStringExtra("bulanan");
        File file = new File(Environment.getExternalStorageDirectory() .getAbsolutePath()  + "/siap_opa/"+path);
        if (file.exists()) {
            pdfView.fromFile(file)  //.pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) .swipeHorizontal(true) .enableDoubletap(true) .defaultPage(0) .enableAnnotationRendering(true) .password(null) .scrollHandle(null) .onLoad(new OnLoadCompleteListener() {
                @Override  public void loadComplete(int nbPages) {
                    pdfView.setMinZoom(1f);
                    pdfView.setMidZoom(5f);
                    pdfView.setMaxZoom(10f);
                    pdfView.zoomTo(2f);
                    pdfView.scrollTo(0, 0);
                    pdfView.moveTo(0f, 0f);
                }
            }) .load();
        }
    }

    private void showMusiman() {
        String path = getIntent().getStringExtra("musiman");
        File file = new File(Environment.getExternalStorageDirectory() .getAbsolutePath()  + "/siap_opa/"+path);
        if (file.exists()) {
            pdfView.fromFile(file)  //.pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) .swipeHorizontal(true) .enableDoubletap(true) .defaultPage(0) .enableAnnotationRendering(true) .password(null) .scrollHandle(null) .onLoad(new OnLoadCompleteListener() {
                @Override  public void loadComplete(int nbPages) {
                    pdfView.setMinZoom(1f);
                    pdfView.setMidZoom(5f);
                    pdfView.setMaxZoom(10f);
                    pdfView.zoomTo(2f);
                    pdfView.scrollTo(0, 0);
                    pdfView.moveTo(0f, 0f);
                }
            }) .load();
        }
    }

    private void updateStatusApprovalProv() {
        progressDialog.setTitle("Approve Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        final String prov_to_popt = getIntent().getStringExtra("msg_to_popt");
        final String prov_to_kab = getIntent().getStringExtra("msg_to_kab");
        final String prov_to_satpel = getIntent().getStringExtra("msg_to_satpel");
        String url = Constants.ROOT_URL+"ApprovalProv";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            insertNotificationPOPT();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PdfView.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_intensitas", id_intensitas);
                params.put("approval_provinsi", "Sudah");
                params.put("prov_to_popt", prov_to_popt);
                params.put("prov_to_kab", prov_to_kab);
                params.put("prov_to_satpel", prov_to_satpel);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PdfView.this);
        requestQueue.add(request);
    }

    private void updateStatusAppSatpel() {
        progressDialog.setTitle("Approve Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        final String satpel_to_popt = getIntent().getStringExtra("msg_to_popt");
        final String satpel_to_kab = getIntent().getStringExtra("msg_to_kab");
        String url = Constants.ROOT_URL+"Approvalsat";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            insertNotificationProvinsi();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(DetailLaporan.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_intensitas", id_intensitas);
                params.put("approval_satpel", "Sudah");
                params.put("satpel_to_popt", satpel_to_popt);
                params.put("satpel_to_kab", satpel_to_kab);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PdfView.this);
        requestQueue.add(request);
    }

    private void insertNotificationProvinsi() {
        String url = Constants.ROOT_URL+"Notification";
        final String username = Common.currentUser.getProvinsi();
        StringRequest jobReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            insertNotificationPOPT();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", "Belum");
                params.put("id_laporan", id_intensitas);
                params.put("title", "Laporan Butuh Validasi");
                params.put("msg", "Mohon validasi laporan ini sebelum tanggal yang ditentukan");
                params.put("laporan", "Harian");
                params.put("username", username);
                params.put("towho", "Provinsi");
                return params;
            }
        };

        requestQueue.add(jobReq);
    }

    private void updateStatusApproval() {
        progressDialog.setTitle("Approve Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        final String kab_to_popt = getIntent().getStringExtra("msg_to_popt");
        String url = Constants.ROOT_URL+"Approval";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            insertNotificationSatPel();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(DetailLaporan.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_intensitas", id_intensitas);
                params.put("approval_kab", "Sudah");
                params.put("kab_to_popt", kab_to_popt);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PdfView.this);
        requestQueue.add(request);
    }

    private void insertNotificationSatPel() {
        String url = Constants.ROOT_URL+"Notification";
        final String username = Common.currentUser.getKabupaten();
        StringRequest jobReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            insertNotificationPOPT();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", "Belum");
                params.put("id_laporan", id_intensitas);
                params.put("title", "Laporan Butuh Validasi");
                params.put("msg", "Mohon validasi laporan ini sebelum tanggal yang ditentukan");
                params.put("laporan", "Harian");
                params.put("username", username);
                params.put("towho", "SatPel");
                return params;
            }
        };

        requestQueue.add(jobReq);
    }

    private void insertNotificationPOPT() {
        String url = Constants.ROOT_URL+"Notification";
        final String username = getIntent().getStringExtra("username");
        StringRequest jobReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(PdfView.this, "Data berhasil diapprove", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", "Belum");
                params.put("id_laporan", id_intensitas);
                params.put("title", "Validasi Laporan");
                params.put("msg", "Laporan ini telah divalidasi");
                params.put("laporan", "Harian");
                params.put("username", username);
                params.put("towho", "POPT");
                return params;
            }
        };

        requestQueue.add(jobReq);
    }

    private void updateDataSetBulan() {
        progressDialog.setTitle("Insert Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        String url = Constants.ROOT_URL+"Simpan_Inten";

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String now = dateFormat.format(calendar.getTime());

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


        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            insertNotificationSetBulan();
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
                params.put("id_hsl_stgh", id_hsl_stgh);
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
        RequestQueue requestQueue = Volley.newRequestQueue(PdfView.this);
        requestQueue.add(request);
    }

    private void insertNotificationSetBulan() {
        String url = Constants.ROOT_URL+"Notification";
        final String username = Common.currentUser.getKabupaten();
        StringRequest jobReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(PdfView.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", "Belum");
                params.put("id_laporan", id_hsl_stgh);
                params.put("title", "Laporan Butuh Validasi");
                params.put("msg", "Mohon validasi laporan ini sebelum tanggal yang ditentukan");
                params.put("laporan", "Setengah");
                params.put("username", username);
                params.put("towho", "Kortikab");
                return params;
            }
        };

        requestQueue.add(jobReq);
    }

    private void viewSetengah() {
        String path = getIntent().getStringExtra("setengah");
        File file = new File(Environment.getExternalStorageDirectory() .getAbsolutePath()  + "/siap_opa/setengah_bulan/"+path);
        if (file.exists()) {
            pdfView.fromFile(file)  //.pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) .swipeHorizontal(true) .enableDoubletap(true) .defaultPage(0) .enableAnnotationRendering(true) .password(null) .scrollHandle(null) .onLoad(new OnLoadCompleteListener() {
                @Override  public void loadComplete(int nbPages) {
                    pdfView.setMinZoom(1f);
                    pdfView.setMidZoom(5f);
                    pdfView.setMaxZoom(10f);
                    pdfView.zoomTo(2f);
                    pdfView.scrollTo(0, 0);
                    pdfView.moveTo(0f, 0f);
                }
            }) .load();
        }
    }

    private void showHasil() {
        String path = getIntent().getStringExtra("hasil");
        File file = new File(Environment.getExternalStorageDirectory() .getAbsolutePath()  + "/siap_opa/"+path);
        if (file.exists()) {
            pdfView.fromFile(file)  //.pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) .swipeHorizontal(true) .enableDoubletap(true) .defaultPage(0) .enableAnnotationRendering(true) .password(null) .scrollHandle(null) .onLoad(new OnLoadCompleteListener() {
                @Override  public void loadComplete(int nbPages) {
                    pdfView.setMinZoom(1f);
                    pdfView.setMidZoom(5f);
                    pdfView.setMaxZoom(10f);
                    pdfView.zoomTo(2f);
                    pdfView.scrollTo(0, 0);
                    pdfView.moveTo(0f, 0f);
                }
            }) .load();
        }
    }

    private void updateDataIntensitas() {
        progressDialog.setTitle("Insert Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        String url = Constants.ROOT_URL+"Update_Inten";

//        rekomendasi = edtRekomendasi.getText().toString();
        id_intensitas = getIntent().getStringExtra("id_intensitas");

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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


        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                           insertNotification();
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
                params.put("id_intensitas", id_intensitas);
//                params.put("rekomendasi", rekomendasi);
                params.put("status_pelaporan", "Sudah");
                params.put("batas_waktu_kab", datePlusHour);
                params.put("batas_waktu_satpel", datePlusOne);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PdfView.this);
        requestQueue.add(request);

    }

    private void viewFile() {
        String path = getIntent().getStringExtra("view");
        File file = new File(Environment.getExternalStorageDirectory() .getAbsolutePath()  + "/siap_opa/harian/"+path);
        if (file.exists()) {
            pdfView.fromFile(file)  //.pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) .swipeHorizontal(true) .enableDoubletap(true) .defaultPage(0) .enableAnnotationRendering(true) .password(null) .scrollHandle(null) .onLoad(new OnLoadCompleteListener() {
                @Override  public void loadComplete(int nbPages) {
                    pdfView.setMinZoom(1f);
                    pdfView.setMidZoom(5f);
                    pdfView.setMaxZoom(10f);
                    pdfView.zoomTo(2f);
                    pdfView.scrollTo(0, 0);
                    pdfView.moveTo(0f, 0f);
                }
            }) .load();
        }
    }

    private void selectIDSum() {
        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        userAlamat = Common.currentUser.getAlamat();
        String url = Constants.ROOT_URL+"Sum?kecamatan="+userAlamat+"&date="+date+"&id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                id_detail = object.getString("id_detail");

//                                updateLapDesa();

                                selectSum();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PdfView.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(PdfView.this).add(stringRequest);
    }

    private void showFile() {
        String path = getIntent().getStringExtra("path");
        File file = new File(Environment.getExternalStorageDirectory() .getAbsolutePath()  + "/siap_opa/harian_keliling/"+path);
        if (file.exists()) {
            pdfView.fromFile(file)  //.pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) .swipeHorizontal(true) .enableDoubletap(true) .defaultPage(0) .enableAnnotationRendering(true) .password(null) .scrollHandle(null) .onLoad(new OnLoadCompleteListener() {
                @Override  public void loadComplete(int nbPages) {
                    pdfView.setMinZoom(1f);
                    pdfView.setMidZoom(5f);
                    pdfView.setMaxZoom(10f);
                    pdfView.zoomTo(2f);
                    pdfView.scrollTo(0, 0);
                    pdfView.moveTo(0f, 0f);
                }
            }) .load();
        }
    }

    private void downloadFile() {
        mProgressDialog.show();
        mProgressDialog.setMessage("downloading");
        mProgressDialog.setMax(100);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        DownloadFileTask task = new DownloadFileTask( PdfView.this,  samplePDF,  "/download/surat.pdf");
        task.startTask();
    }

    public class DownloadFileTask {
        public static final String TAG = "DownloadFileTask";
        private PdfView context;
        private GetTask contentTask;
        private String url;
        private String fileName;
        public DownloadFileTask(PdfView context, String url, String fileName) {
            this.context = context;
            this.url = url;
            this.fileName = fileName;
        }
        public void startTask() {
            doRequest();
        }
        private void doRequest() {
            contentTask = new GetTask();
            contentTask.execute();
        }
        private class GetTask extends AsyncTask< String, Integer, String > {

            @Override
            protected String doInBackground(String... strings) {
                int count;
                try {
                    URL _url = new URL(url);
                    URLConnection conection = _url.openConnection();
                    conection.connect();
                    String extension = url.substring(url.lastIndexOf('.') + 1).trim();
                    InputStream input = new BufferedInputStream(_url.openStream(),  8192);
                    OutputStream output = new FileOutputStream( Environment.getExternalStorageDirectory() + fileName);
                    byte data[] = new byte[1024];
                    while ((count = input.read(data))  != -1) {
                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    input.close();
                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
                return null;
            }

            protected void onPostExecute(String data) {
                context.onFileDownloaded();
            }
        }
    }
    public void onFileDownloaded() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        File file = new File(Environment.getExternalStorageDirectory() .getAbsolutePath()  + "/download/surat.pdf");
        if (file.exists()) {
            pdfView.fromFile(file)  //.pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) .swipeHorizontal(true) .enableDoubletap(true) .defaultPage(0) .enableAnnotationRendering(true) .password(null) .scrollHandle(null) .onLoad(new OnLoadCompleteListener() {
                @Override  public void loadComplete(int nbPages) {
                    pdfView.setMinZoom(1f);
                    pdfView.setMidZoom(5f);
                    pdfView.setMaxZoom(10f);
                    pdfView.zoomTo(2f);
                    pdfView.scrollTo(0, 0);
                    pdfView.moveTo(0f, 0f);
                }
            }) .load();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)  private void checkPermission_Certificate() {
        if (ContextCompat.checkSelfPermission(context,  Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat .checkSelfPermission(context,  Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale ((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE) ||  ActivityCompat.shouldShowRequestPermissionRationale ((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissions( new String[] {
                        Manifest.permission .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                },  PERMISSIONS_MULTIPLE_REQUEST_CERTIFICATE);
            } else {
                requestPermissions( new String[] {
                        Manifest.permission .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                },  PERMISSIONS_MULTIPLE_REQUEST_CERTIFICATE);
            }
        } else {  // write your logic code if permission already granted
            if (!samplePDF.equalsIgnoreCase("")) {
                downloadFile();
            }
        }
    }
    private void checkAndroidVersionCertificate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission_Certificate();
        } else {
            if (!samplePDF.equalsIgnoreCase("")) {
                downloadFile();
            }
        }
    }
    @Override  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST_CERTIFICATE:
                if (grantResults.length > 0) {
                    boolean writePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writePermission && readExternalFile) {
                        if (!samplePDF.equalsIgnoreCase("")) {
                            downloadFile();
                        }
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Build.VERSION.SDK_INT >= 23 &&  !shouldShowRequestPermissionRationale(permissions[0])) {
                            Intent intent = new Intent();
                            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        } else {
                            requestPermissions( new String[] {
                                    Manifest.permission .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },  PERMISSIONS_MULTIPLE_REQUEST_CERTIFICATE);
                        }
                    }
                }
                break;
        }
    }

    private void selectSum() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        userAlamat = Common.currentUser.getAlamat();
        String url = Constants.ROOT_URL+"Sum?date="+date+"&id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String total_data = object.getString("total_data");
                                String jumlah_anakan = object.getString("jumlah_anakan");
                                String luas_spot_hopperburn = object.getString("luas_spot_hopperburn");
                                String luas_hamparan = object.getString("luas_hamparan");
                                String luas_diamati = object.getString("luas_diamati");
                                String varietas = object.getString("varietas");
                                String dari_umur = object.getString("dari_umur");
                                String hingga_umur = object.getString("hingga_umur");
                                String petugas_pengamatan = object.getString("petugas_pengamatan");
                                String desa = object.getString("desa");

                                SumModel sumModel = new SumModel();
                                sumModel.setTotal_data(total_data);
                                sumModel.setJumlah_anakan(jumlah_anakan);
                                sumModel.setLuas_spot_hopperburn(luas_spot_hopperburn);
                                sumModel.setLuas_hamparan(luas_hamparan);
                                sumModel.setLuas_diamati(luas_diamati);
                                sumModel.setVarietas(varietas);
                                sumModel.setDari_umur(dari_umur);
                                sumModel.setHingga_umur(hingga_umur);
                                sumModel.setPetugas_pengamatan(petugas_pengamatan);
                                sumModel.setDesa(desa);
                                Common.sum = sumModel;

                                getOPTMutlak();

//                                getDataMaPopulasi();

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PdfView.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(PdfView.this).add(stringRequest);
    }

    public void getOPTMutlak(){
        String url = Constants.ROOT_URL+"Sum_Opt?status=mutlak&id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                optMutlak = object.getString("opt");

                                String url = Constants.ROOT_URL+"Sum_Opt?opt="+optMutlak+"&id_hasil="+id_hasil+"&stat=mutlak";
//
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray array = new JSONArray(response);
                                                    for (int i = 0; i<array.length(); i++){
                                                        JSONObject object = array.getJSONObject(i);

                                                        jumlahMutlak = object.getString("jumlah");
                                                        opt1 = object.getString("opt");

                                                        listOPT = new ArrayList<>();
                                                        listOPT.add(opt1);

                                                        insertDataIntensitasMutlak();

                                                    }
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(InputHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                                Volley.newRequestQueue(PdfView.this).add(stringRequest);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getOPTTdkMutlak();
//                Toast.makeText(InputHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(PdfView.this).add(stringRequest);
    }

    private void getOPTTdkMutlak() {
        String url = Constants.ROOT_URL+"Sum_Opt?status=tidak mutlak&id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                optTdkMutlak = object.getString("opt");

                                String url = Constants.ROOT_URL+"Sum_Opt?opt="+optTdkMutlak+"&id_hasil="+id_hasil+"&stat=tidak mutlak";

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray array = new JSONArray(response);
                                                    for (int i = 0; i<array.length(); i++){
                                                        JSONObject object = array.getJSONObject(i);

                                                        jumlahTdkMutlak = object.getString("jumlah");
                                                        opt2 = object.getString("opt");

                                                        listOPT2 = new ArrayList<>();
                                                        listOPT2.add(jumlahTdkMutlak);

                                                        insertDataIntensitasTdkMutlak();
//                                                        getDataIntensitasPopMA();

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
                                Volley.newRequestQueue(PdfView.this).add(stringRequest);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getDataIntensitasPopMA();
//                deleteDuplicateValue();
//                Toast.makeText(InputHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(PdfView.this).add(stringRequest);
    }

    private void insertDataIntensitasMutlak() {
        String url = Constants.ROOT_URL+"Inten_Mutlak";
        final String kecamatan = Common.currentUser.getAlamat();
        final String wilayah_pengamatan = Common.currentUser.getProvinsi();
        final String kabupaten = Common.currentUser.getKabupaten();
        final String desa = Common.sum.getDesa();
        final String luas_tanaman = Common.sum.getLuas_hamparan();
        final String umur_tanaman = Common.sum.getDari_umur() +"-"+Common.sum.getHingga_umur();
        final String luas_sembuh = "0";
        final String luas_sisa_serang = "0";
        final String intensitas_serang = "0";
        final String puso_sisa_serang = "0";
        final String jumlah_sisa_serang = "0";
        final String luas_tambah_serang = Common.sum.getLuas_diamati();
        final String intensitas_tambah = "0";
        final String puso_tambah_serang = "0";
        final String jumlah_tambah_serang = "0";
        final String luas_pengendalian = "0";
        final String luas_keadaan_serang = "0";
        final String intensitas_keadaan = "0";
        final String puso_keadaan_serang = "0";
        final String luas_waspada = "0";
        final String luas_panen = "0";
        final String kimia = "0";
        final String nabati = "0";
        final String eradikasi = "0";
        final String cara_lain = "0";
        final String jumlah_pengendalian = "0";
        final String frek_kimia = "0";
        final String frek_nabati = "0";

        final String jumlah_anakan = Common.sum.getJumlah_anakan();
//
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        final String tanggal = date;
//
        final float intensitas_tambah_mutlak = Float.parseFloat(jumlahMutlak)/Float.parseFloat(jumlah_anakan)*100;
//        float intensitas_ma = Float.parseFloat(jumlahMA)/30;
//        float intensitas_populasi = Float.parseFloat(jumlahTdkMutlak)/270*100;
//
//
        requestQueue = Volley.newRequestQueue(this);
        final JSONArray array=new JSONArray();
        dateFormat = new SimpleDateFormat("dd");
        hari = dateFormat.format(calendar.getTime());
////
        for(int i=0;i<listOPT.size();i++){
            JSONObject obj=new JSONObject();
            try {
                obj.put("wilayah_pengamatan", wilayah_pengamatan);
                obj.put("kabupaten",kabupaten);
                obj.put("kecamatan",kecamatan);
                obj.put("desa",desa);
                if (Integer.parseInt(hari) <= 15){
                    obj.put("periode_pengamatan","Periode 1-15");
                }else {
                    obj.put("periode_pengamatan","Periode 16-31");
                }
                obj.put("luas_tanaman",luas_tanaman);
                obj.put("umur_tanaman",umur_tanaman);
                obj.put("jenis_opt", opt1);
                obj.put("intensitas_serang",intensitas_serang);
                obj.put("luas_sembuh",luas_sembuh);
                obj.put("luas_sisa_serang",luas_sisa_serang);
                obj.put("serang_ringan",luas_sisa_serang);
                obj.put("serang_sedang",luas_sisa_serang);
                obj.put("serang_berat",luas_sisa_serang);
                obj.put("serang_puso",luas_sisa_serang);
                obj.put("jumlah_sisa_serang",luas_sisa_serang);
                obj.put("intensitas_tambah",String.format("%.2f", intensitas_tambah_mutlak));
                if (Float.parseFloat(String.valueOf(intensitas_tambah_mutlak)) >= 85.0){
                    obj.put("puso_tambah_serang",luas_tambah_serang);
                }
                obj.put("luas_pengendalian",luas_pengendalian);
                obj.put("keadaan_ringan","0");
                obj.put("keadaan_sedang","0");
                obj.put("keadaan_berat","0");
                obj.put("keadaan_puso","0");
                obj.put("luas_keadaan_serang",luas_keadaan_serang);
                obj.put("intensitas_keadaan",intensitas_keadaan);
                obj.put("puso_keadaan_serang",puso_keadaan_serang);
                obj.put("jumlah_keadaan_serang",luas_keadaan_serang);
                if (opt1.equals("01")){
                    if (intensitas_tambah_mutlak <= 10){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 10 && intensitas_tambah_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 25 && intensitas_tambah_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 50 && intensitas_tambah_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 85 && intensitas_tambah_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }else if (opt1.equals("02")){
                    if (intensitas_tambah_mutlak <= 20){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 20 && intensitas_tambah_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 25 && intensitas_tambah_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 50 && intensitas_tambah_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 85 && intensitas_tambah_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt1.equals("04")){
                    if (intensitas_tambah_mutlak <= 15){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 15 && intensitas_tambah_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 25 && intensitas_tambah_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 50 && intensitas_tambah_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 85 && intensitas_tambah_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt1.equals("06A")){
                    if (intensitas_tambah_mutlak < 25){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 25 && intensitas_tambah_mutlak <= 45){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 45 && intensitas_tambah_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 50 && intensitas_tambah_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 85 && intensitas_tambah_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt1.equals("06B")){
                    if (intensitas_tambah_mutlak < 25){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 25 && intensitas_tambah_mutlak >= 45){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 45 && intensitas_tambah_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 50 && intensitas_tambah_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 85 && intensitas_tambah_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt1.equals("05")){
                    if (intensitas_tambah_mutlak <= 15){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 15 && intensitas_tambah_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 25 && intensitas_tambah_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 50 && intensitas_tambah_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 85 && intensitas_tambah_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt1.equals("07")){
                    if (intensitas_tambah_mutlak <= 0.6){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 0.6 && intensitas_tambah_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 25 && intensitas_tambah_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 50 && intensitas_tambah_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 85 && intensitas_tambah_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt1.equals("03")){
                    if (intensitas_tambah_mutlak <= 5){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 5 && intensitas_tambah_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 25 && intensitas_tambah_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 50 && intensitas_tambah_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 85 && intensitas_tambah_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt1.equals("08")){
                    if (intensitas_tambah_mutlak <= 5){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 5 && intensitas_tambah_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 25 && intensitas_tambah_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 50 && intensitas_tambah_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 85 && intensitas_tambah_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt1.equals("14")){
                    if (intensitas_tambah_mutlak <= 11){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 11 && intensitas_tambah_mutlak <= 15){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 15 && intensitas_tambah_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 25 && intensitas_tambah_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 85 && intensitas_tambah_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
//                else if (opt1.equals("BHD")){
//                    if (intensitas_tambah_mutlak <= 11){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_tambah_mutlak > 11 && intensitas_tambah_mutlak <= 15){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_tambah_mutlak > 15 && intensitas_tambah_mutlak <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_tambah_mutlak > 25 && intensitas_tambah_mutlak <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_tambah_mutlak > 85 && intensitas_tambah_mutlak <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
            else {
                     if (intensitas_tambah_mutlak > 0 && intensitas_tambah_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 25 && intensitas_tambah_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 50 && intensitas_tambah_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_mutlak > 85 && intensitas_tambah_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }

                }
                obj.put("id_hasil_pengamatan",id_hasil);
                obj.put("luas_panen",luas_panen);
                obj.put("kimia",kimia);
                obj.put("nabati",nabati);
                obj.put("eradikasi",eradikasi);
                obj.put("cara_lain",cara_lain);
                obj.put("jumlah_pengendalian",jumlah_pengendalian);
                obj.put("tanggal_intensitas",tanggal);
                obj.put("status_pelaporan","Belum");
                obj.put("frek_kimia",frek_kimia);
                obj.put("frek_nabati",frek_nabati);
                obj.put("approval_kab","Belum");
                obj.put("approval_satpel","Belum");
                obj.put("approval_provinsi","Belum");
                obj.put("kab_to_popt","Tidak Ada Catatan");
                obj.put("satpel_to_popt","Tidak Ada Catatan");
                obj.put("satpel_to_kab","Tidak Ada Catatan");
                obj.put("prov_to_kab","Tidak Ada Catatan");
                obj.put("prov_to_satpel","Tidak Ada Catatan");
                obj.put("prov_to_popt","Tidak Ada Catatan");
                obj.put("surat_kab","Belum");
                obj.put("surat_satpel","Belum");
                obj.put("batas_waktu_kab","tidak ada batas waktu");
                obj.put("batas_waktu_satpel","tidak ada batas waktu");
                obj.put("surat_provinsi","Belum");
//                obj.put("musuh_alami", jumlahMa);
//                obj.put("intensitas_ma","");
                obj.put("rekomendasi","");
                obj.put("status","mutlak");
//                obj.put("populasi",jumlahPopulasi);
//                obj.put("populasi_m2",jumlahPopulasiMP);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);
//            Toast.makeText(InputHarian.this, String.valueOf(obj), Toast.LENGTH_SHORT).show();
        }
//
        StringRequest jobReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            getOPTTdkMutlak();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("data", String.valueOf(array));
                return params;
            }
        };

        requestQueue.add(jobReq);
    }

    private void insertDataIntensitasTdkMutlak() {
        String url = Constants.ROOT_URL+"Inten_Mutlak";
        final String kecamatan = Common.currentUser.getAlamat();
        final String wilayah_pengamatan = Common.currentUser.getProvinsi();
        final String kabupaten = Common.currentUser.getKabupaten();
        final String desa = Common.sum.getDesa();
        final String luas_tanaman = Common.sum.getLuas_hamparan();
        final String umur_tanaman = Common.sum.getDari_umur() +"-"+Common.sum.getHingga_umur();
        final String luas_sembuh = "0";
        final String luas_sisa_serang = "0";
        final String intensitas_serang = "0";
        final String puso_sisa_serang = "0";
        final String jumlah_sisa_serang = "0";
        final String luas_tambah_serang = Common.sum.getLuas_diamati();
        final String intensitas_tambah = "0";
        final String puso_tambah_serang = "0";
        final String jumlah_tambah_serang = "0";
        final String luas_pengendalian = "0";
        final String luas_keadaan_serang = "0";
        final String intensitas_keadaan = "0";
        final String puso_keadaan_serang = "0";
        final String luas_waspada = "0";
        final String luas_panen = "0";
        final String kimia = "0";
        final String nabati = "0";
        final String eradikasi = "0";
        final String cara_lain = "0";
        final String jumlah_pengendalian = "0";
        final String frek_kimia = "0";
        final String frek_nabati = "0";
//
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        final String tanggal = date;
//
//        final float intensitas_populasi = Float.parseFloat(jumlahMutlak)/Float.parseFloat(jumlah_anakan)*100;
        float intensitas_tambah_tdk_mutlak = Float.parseFloat(jumlahTdkMutlak)/270*100;
//        float intensitas_ma = Float.parseFloat(jumlahMA)/30;
//
//
        requestQueue = Volley.newRequestQueue(this);
        final JSONArray array=new JSONArray();
        dateFormat = new SimpleDateFormat("dd");
        hari = dateFormat.format(calendar.getTime());

        for(int i=0;i<listOPT2.size();i++){
            JSONObject obj=new JSONObject();
            try {
                obj.put("wilayah_pengamatan", wilayah_pengamatan);
                obj.put("kabupaten",kabupaten);
                obj.put("kecamatan",kecamatan);
                obj.put("desa",desa);
                if (Integer.parseInt(hari) <= 15){
                    obj.put("periode_pengamatan","Periode 1-15");
                }else {
                    obj.put("periode_pengamatan","Periode 16-31");
                }
                obj.put("luas_tanaman",luas_tanaman);
                obj.put("umur_tanaman",umur_tanaman);
                obj.put("jenis_opt", opt2);
                obj.put("intensitas_serang",intensitas_serang);
                obj.put("luas_sembuh",luas_sembuh);
                obj.put("luas_sisa_serang",luas_sisa_serang);
                obj.put("serang_ringan",luas_sisa_serang);
                obj.put("serang_sedang",luas_sisa_serang);
                obj.put("serang_berat",luas_sisa_serang);
                obj.put("serang_puso",luas_sisa_serang);
                obj.put("jumlah_sisa_serang",luas_sisa_serang);
                obj.put("intensitas_tambah",String.format("%.2f", intensitas_tambah_tdk_mutlak));
                if (Float.parseFloat(String.valueOf(intensitas_tambah_tdk_mutlak)) >= 85.0){
                    obj.put("puso_tambah_serang",luas_tambah_serang);
                }
                obj.put("luas_pengendalian",luas_pengendalian);
                obj.put("keadaan_ringan","0");
                obj.put("keadaan_sedang","0");
                obj.put("keadaan_berat","0");
                obj.put("keadaan_puso","0");
                obj.put("luas_keadaan_serang",luas_keadaan_serang);
                obj.put("intensitas_keadaan",intensitas_keadaan);
                obj.put("puso_keadaan_serang",puso_keadaan_serang);
                obj.put("jumlah_keadaan_serang",luas_keadaan_serang);
                if (opt2.equals("01")){
                    if (intensitas_tambah_tdk_mutlak <= 10){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 10 && intensitas_tambah_tdk_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 25 && intensitas_tambah_tdk_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 50 && intensitas_tambah_tdk_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 85 && intensitas_tambah_tdk_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }else if (opt2.equals("02")){
                    if (intensitas_tambah_tdk_mutlak <= 20){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 20 && intensitas_tambah_tdk_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 25 && intensitas_tambah_tdk_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 50 && intensitas_tambah_tdk_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 85 && intensitas_tambah_tdk_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt2.equals("04")){
                    if (intensitas_tambah_tdk_mutlak <= 15){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 15 && intensitas_tambah_tdk_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 25 && intensitas_tambah_tdk_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 50 && intensitas_tambah_tdk_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 85 && intensitas_tambah_tdk_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt2.equals("06A")){
                    if (intensitas_tambah_tdk_mutlak <= 25){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 25 && intensitas_tambah_tdk_mutlak <= 45){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 45 && intensitas_tambah_tdk_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 50 && intensitas_tambah_tdk_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 85 && intensitas_tambah_tdk_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt2.equals("06B")){
                    if (intensitas_tambah_tdk_mutlak <= 25){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 25 && intensitas_tambah_tdk_mutlak >= 45){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 45 && intensitas_tambah_tdk_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 50 && intensitas_tambah_tdk_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 85 && intensitas_tambah_tdk_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt2.equals("05")){
                    if (intensitas_tambah_tdk_mutlak <= 15){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 15 && intensitas_tambah_tdk_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 25 && intensitas_tambah_tdk_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 50 && intensitas_tambah_tdk_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 85 && intensitas_tambah_tdk_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt2.equals("07")){
                    if (intensitas_tambah_tdk_mutlak <= 0.6){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 0.6 && intensitas_tambah_tdk_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 25 && intensitas_tambah_tdk_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 50 && intensitas_tambah_tdk_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 85 && intensitas_tambah_tdk_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt2.equals("03")){
                    if (intensitas_tambah_tdk_mutlak <= 5){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 5 && intensitas_tambah_tdk_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 25 && intensitas_tambah_tdk_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 50 && intensitas_tambah_tdk_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 85 && intensitas_tambah_tdk_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt2.equals("08")){
                    if (intensitas_tambah_tdk_mutlak <= 5){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 5 && intensitas_tambah_tdk_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 25 && intensitas_tambah_tdk_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 50 && intensitas_tambah_tdk_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 85 && intensitas_tambah_tdk_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
                else if (opt2.equals("14")){
                    if (intensitas_tambah_tdk_mutlak <= 11){
                        obj.put("luas_waspada",luas_tambah_serang);
                        obj.put("jumlah_tambah_serang","0");
                        obj.put("luas_tambah_serang","0");
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 11 && intensitas_tambah_tdk_mutlak <= 15){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 15 && intensitas_tambah_tdk_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 25 && intensitas_tambah_tdk_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 85 && intensitas_tambah_tdk_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }
                }
//                else if (opt2.equals("BHD")){
//                    if (intensitas_tambah_tdk_mutlak <= 11){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_tambah_tdk_mutlak > 11 && intensitas_tambah_tdk_mutlak <= 15){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_tambah_tdk_mutlak > 15 && intensitas_tambah_tdk_mutlak <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_tambah_tdk_mutlak > 25 && intensitas_tambah_tdk_mutlak <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_tambah_tdk_mutlak > 85 && intensitas_tambah_tdk_mutlak <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
            else {
                    if (intensitas_tambah_tdk_mutlak > 0 && intensitas_tambah_tdk_mutlak <= 25){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan",luas_tambah_serang);
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 25 && intensitas_tambah_tdk_mutlak <= 50){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang",luas_tambah_serang);
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 50 && intensitas_tambah_tdk_mutlak <= 85){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat",luas_tambah_serang);
                        obj.put("tambah_puso","0");
                    }else if (intensitas_tambah_tdk_mutlak > 85 && intensitas_tambah_tdk_mutlak <= 100){
                        obj.put("luas_waspada","0");
                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
                        obj.put("luas_tambah_serang",luas_tambah_serang);
                        obj.put("tambah_ringan","0");
                        obj.put("tambah_sedang","0");
                        obj.put("tambah_berat","0");
                        obj.put("tambah_puso",luas_tambah_serang);
                    }

                }
                obj.put("id_hasil_pengamatan",id_hasil);
                obj.put("luas_panen",luas_panen);
                obj.put("kimia",kimia);
                obj.put("nabati",nabati);
                obj.put("eradikasi",eradikasi);
                obj.put("cara_lain",cara_lain);
                obj.put("jumlah_pengendalian",jumlah_pengendalian);
                obj.put("tanggal_intensitas",tanggal);
                obj.put("status_pelaporan","Belum");
                obj.put("frek_kimia",frek_kimia);
                obj.put("frek_nabati",frek_nabati);
                obj.put("approval_kab","Belum");
                obj.put("approval_satpel","Belum");
                obj.put("approval_provinsi","Belum");
                obj.put("kab_to_popt","Tidak Ada Catatan");
                obj.put("satpel_to_popt","Tidak Ada Catatan");
                obj.put("satpel_to_kab","Tidak Ada Catatan");
                obj.put("prov_to_kab","Tidak Ada Catatan");
                obj.put("prov_to_satpel","Tidak Ada Catatan");
                obj.put("prov_to_popt","Tidak Ada Catatan");
                obj.put("surat_kab","Belum");
                obj.put("surat_satpel","Belum");
                obj.put("batas_waktu_kab","tidak ada batas waktu");
                obj.put("batas_waktu_satpel","tidak ada batas waktu");
                obj.put("surat_provinsi","Belum");
//                obj.put("musuh_alami",jumlahMa);
//                obj.put("intensitas_ma","");
                obj.put("rekomendasi","");
                obj.put("status","tidak mutlak");
//                obj.put("populasi",jumlahPopulasi);
//                obj.put("populasi_m2",jumlahPopulasiMP);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);
        }
//
        StringRequest jobReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
//                            deleteDuplicateValue();
                            getDataIntensitasPopMA();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("data", String.valueOf(array));
                return params;
            }
        };

        requestQueue.add(jobReq);
    }

    private void getDataIntensitasPopMA() {
        String url = Constants.ROOT_URL+"Sum_Opt?status=populasi&id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                populasi = object.getString("opt");

                                String url = Constants.ROOT_URL+"Sum_Opt?opt="+populasi+"&id_hasil="+id_hasil+"&stat=populasi";
//
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray array = new JSONArray(response);
                                                    for (int i = 0; i<array.length(); i++){
                                                        JSONObject object = array.getJSONObject(i);

                                                        populasi = object.getString("opt");
                                                        jumlahPopulasi = object.getString("jumlah");

                                                        listPopulasi = new ArrayList<>();
                                                        listPopulasi.add(populasi);

                                                        insertDataPopulasi();

                                                    }
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(InputHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                                Volley.newRequestQueue(PdfView.this).add(stringRequest);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                try {
//                    insertDataPop();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                getDataPopulasiMP();
            }
        });
        Volley.newRequestQueue(PdfView.this).add(stringRequest);
    }

    private void getDataMaPopulasi() {
        String url = Constants.ROOT_URL+"Sum_Opt?status=MA&id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                optMA = object.getString("opt");

                                String url = Constants.ROOT_URL+"Sum_Opt?opt="+optMA+"&id_hasil="+id_hasil+"&stat=MA";
//
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
//                                                    optMa = new StringBuilder();
                                                    JSONArray array = new JSONArray(response);
                                                    for (int i = 0; i<array.length(); i++){
                                                        JSONObject object = array.getJSONObject(i);

                                                        jumlahMA = object.getString("jumlah");
                                                        optMA = object.getString("opt");

                                                        listPopulasiMA = new ArrayList<>();
                                                        listPopulasiMA.add(optMA);

                                                        insertDataPopulasiMA();

                                                    }
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(InputHarian.this, error.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                                Volley.newRequestQueue(PdfView.this).add(stringRequest);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                deleteDuplicateValue();
//                Toast.makeText(InputHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(PdfView.this).add(stringRequest);
    }

    private void insertDataPopulasiMA() {
        String url = Constants.ROOT_URL+"Inten_Mutlak";
        final String kecamatan = Common.currentUser.getAlamat();
        final String wilayah_pengamatan = Common.currentUser.getProvinsi();
        final String kabupaten = Common.currentUser.getKabupaten();
        final String desa = Common.sum.getDesa();
        final String luas_tanaman = Common.sum.getLuas_hamparan();
        final String umur_tanaman = Common.sum.getDari_umur() +"-"+Common.sum.getHingga_umur();
        final String luas_sembuh = "0";
        final String luas_sisa_serang = "0";
        final String intensitas_serang = "0";
        final String puso_sisa_serang = "0";
        final String jumlah_sisa_serang = "0";
        final String luas_tambah_serang = Common.sum.getLuas_diamati();
        final String intensitas_tambah = "0";
        final String puso_tambah_serang = "0";
        final String jumlah_tambah_serang = "0";
        final String luas_pengendalian = "0";
        final String luas_keadaan_serang = "0";
        final String intensitas_keadaan = "0";
        final String puso_keadaan_serang = "0";
        final String luas_waspada = "0";
        final String luas_panen = "0";
        final String kimia = "0";
        final String nabati = "0";
        final String eradikasi = "0";
        final String cara_lain = "0";
        final String jumlah_pengendalian = "0";
        final String frek_kimia = "0";
        final String frek_nabati = "0";

        final String jumlah_anakan = Common.sum.getJumlah_anakan();

//
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        final String tanggal = date;

        requestQueue = Volley.newRequestQueue(this);
        final JSONArray array=new JSONArray();
        dateFormat = new SimpleDateFormat("dd");
        hari = dateFormat.format(calendar.getTime());

        float intensitas_populasi = Float.parseFloat(jumlahMA)/30;
//        float intensitas_ma = Float.parseFloat(jumlahMA)/30;

        for(int i=0;i<listPopulasiMA.size();i++){
            JSONObject obj=new JSONObject();
            try {
                obj.put("wilayah_pengamatan", wilayah_pengamatan);
                obj.put("kabupaten",kabupaten);
                obj.put("kecamatan",kecamatan);
                obj.put("desa",desa);
                if (Integer.parseInt(hari) <= 15){
                    obj.put("periode_pengamatan","Periode 1-15");
                }else {
                    obj.put("periode_pengamatan","Periode 16-31");
                }
                obj.put("luas_tanaman",luas_tanaman);
                obj.put("umur_tanaman",umur_tanaman);
                obj.put("jenis_opt", optMA);
                obj.put("intensitas_serang",intensitas_serang);
                obj.put("luas_sembuh",luas_sembuh);
                obj.put("luas_sisa_serang",luas_sisa_serang);
                obj.put("serang_ringan",luas_sisa_serang);
                obj.put("serang_sedang",luas_sisa_serang);
                obj.put("serang_berat",luas_sisa_serang);
                obj.put("serang_puso",luas_sisa_serang);
                obj.put("jumlah_sisa_serang",luas_sisa_serang);
                obj.put("intensitas_tambah",String.format("%.2f", intensitas_populasi));
                obj.put("puso_tambah_serang","0");
                obj.put("luas_pengendalian",luas_pengendalian);
                obj.put("keadaan_ringan","0");
                obj.put("keadaan_sedang","0");
                obj.put("keadaan_berat","0");
                obj.put("keadaan_puso","0");
                obj.put("luas_keadaan_serang",luas_keadaan_serang);
                obj.put("intensitas_keadaan",intensitas_keadaan);
                obj.put("puso_keadaan_serang",puso_keadaan_serang);
                obj.put("jumlah_keadaan_serang",luas_keadaan_serang);
                if (intensitas_populasi <= 25){
                    obj.put("luas_waspada",luas_tambah_serang);
                    obj.put("jumlah_tambah_serang","0");
                    obj.put("luas_tambah_serang","0");
                    obj.put("tambah_ringan","0");
                    obj.put("tambah_sedang","0");
                    obj.put("tambah_berat","0");
                    obj.put("tambah_puso","0");
                }else{
                    obj.put("luas_waspada","0");
                    obj.put("jumlah_tambah_serang",luas_tambah_serang);
                    obj.put("luas_tambah_serang",luas_tambah_serang);
                    obj.put("tambah_ringan",luas_tambah_serang);
                    obj.put("tambah_sedang","0");
                    obj.put("tambah_berat","0");
                    obj.put("tambah_puso","0");
                }
//                if (intensitas_populasi > 0 && intensitas_populasi <= 25){
//                    obj.put("luas_waspada","0");
//                    obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                    obj.put("luas_tambah_serang",luas_tambah_serang);
//                    obj.put("tambah_ringan",luas_tambah_serang);
//                    obj.put("tambah_sedang","0");
//                    obj.put("tambah_berat","0");
//                    obj.put("tambah_puso","0");
//                }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                    obj.put("luas_waspada","0");
//                    obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                    obj.put("luas_tambah_serang",luas_tambah_serang);
//                    obj.put("tambah_ringan","0");
//                    obj.put("tambah_sedang",luas_tambah_serang);
//                    obj.put("tambah_berat","0");
//                    obj.put("tambah_puso","0");
//                }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                    obj.put("luas_waspada","0");
//                    obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                    obj.put("luas_tambah_serang",luas_tambah_serang);
//                    obj.put("tambah_ringan","0");
//                    obj.put("tambah_sedang","0");
//                    obj.put("tambah_berat",luas_tambah_serang);
//                    obj.put("tambah_puso","0");
//                }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                    obj.put("luas_waspada","0");
//                    obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                    obj.put("luas_tambah_serang",luas_tambah_serang);
//                    obj.put("tambah_ringan","0");
//                    obj.put("tambah_sedang","0");
//                    obj.put("tambah_berat","0");
//                    obj.put("tambah_puso",luas_tambah_serang);
//                }
                obj.put("id_hasil_pengamatan",id_hasil);
                obj.put("luas_panen",luas_panen);
                obj.put("kimia",kimia);
                obj.put("nabati",nabati);
                obj.put("eradikasi",eradikasi);
                obj.put("cara_lain",cara_lain);
                obj.put("jumlah_pengendalian",jumlah_pengendalian);
                obj.put("tanggal_intensitas",tanggal);
                obj.put("status_pelaporan","Belum");
                obj.put("frek_kimia",frek_kimia);
                obj.put("frek_nabati",frek_nabati);
                obj.put("approval_kab","Belum");
                obj.put("approval_satpel","Belum");
                obj.put("approval_provinsi","Belum");
                obj.put("kab_to_popt","Tidak Ada Catatan");
                obj.put("satpel_to_popt","Tidak Ada Catatan");
                obj.put("satpel_to_kab","Tidak Ada Catatan");
                obj.put("prov_to_kab","Tidak Ada Catatan");
                obj.put("prov_to_satpel","Tidak Ada Catatan");
                obj.put("prov_to_popt","Tidak Ada Catatan");
                obj.put("surat_kab","Belum");
                obj.put("surat_satpel","Belum");
                obj.put("batas_waktu_kab","tidak ada batas waktu");
                obj.put("batas_waktu_satpel","tidak ada batas waktu");
                obj.put("surat_provinsi","Belum");
//                obj.put("musuh_alami",String.valueOf(optMa));
//                obj.put("intensitas_ma",String.valueOf(jumlahMa));
                obj.put("rekomendasi","");
                obj.put("status","MA");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);
        }
//
        StringRequest jobReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            deleteDuplicateValue();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("data", String.valueOf(array));
                return params;
            }
        };

        requestQueue.add(jobReq);
    }

    private void getDataPopulasiMP() {
        String url = Constants.ROOT_URL+"Sum_Opt?status=populasi(m2)&id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                populasiMP = object.getString("opt");

                                String url = Constants.ROOT_URL+"Sum_Opt?opt="+populasiMP+"&id_hasil="+id_hasil+"&stat=populasi(m2)";
//
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray array = new JSONArray(response);
                                                    for (int i = 0; i<array.length(); i++){
                                                        JSONObject object = array.getJSONObject(i);

                                                        jumlahPopulasiMP= object.getString("jumlah");
                                                        populasiMP = object.getString("opt");

                                                        listPopulasiMP = new ArrayList<>();
                                                        listPopulasiMP.add(populasiMP);

                                                        insertDataPopulasiMP();

                                                    }

                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                    }
                                });
                                Volley.newRequestQueue(PdfView.this).add(stringRequest);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               getDataMaPopulasi();
//                Toast.makeText(InputHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(PdfView.this).add(stringRequest);
    }

    private void insertDataPopulasiMP() {
        String url = Constants.ROOT_URL+"Inten_Mutlak";
        final String kecamatan = Common.currentUser.getAlamat();
        final String wilayah_pengamatan = Common.currentUser.getProvinsi();
        final String kabupaten = Common.currentUser.getKabupaten();
        final String desa = Common.sum.getDesa();
        final String luas_tanaman = Common.sum.getLuas_hamparan();
        final String umur_tanaman = Common.sum.getDari_umur() +"-"+Common.sum.getHingga_umur();
        final String luas_sembuh = "0";
        final String luas_sisa_serang = "0";
        final String intensitas_serang = "0";
        final String puso_sisa_serang = "0";
        final String jumlah_sisa_serang = "0";
        final String luas_tambah_serang = Common.sum.getLuas_diamati();
        final String intensitas_tambah = "0";
        final String puso_tambah_serang = "0";
        final String jumlah_tambah_serang = "0";
        final String luas_pengendalian = "0";
        final String luas_keadaan_serang = "0";
        final String intensitas_keadaan = "0";
        final String puso_keadaan_serang = "0";
        final String luas_waspada = "0";
        final String luas_panen = "0";
        final String kimia = "0";
        final String nabati = "0";
        final String eradikasi = "0";
        final String cara_lain = "0";
        final String jumlah_pengendalian = "0";
        final String frek_kimia = "0";
        final String frek_nabati = "0";

        final String jumlah_anakan = Common.sum.getJumlah_anakan();

//
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        final String tanggal = date;

        requestQueue = Volley.newRequestQueue(this);
        final JSONArray array=new JSONArray();
        dateFormat = new SimpleDateFormat("dd");
        hari = dateFormat.format(calendar.getTime());

        float intensitas_populasi = Float.parseFloat(jumlahPopulasiMP)/2;
//        float intensitas_ma = Float.parseFloat(jumlahMA)/30;

        for(int i=0;i<listPopulasiMP.size();i++){
            JSONObject obj=new JSONObject();
            try {
                obj.put("wilayah_pengamatan", wilayah_pengamatan);
                obj.put("kabupaten",kabupaten);
                obj.put("kecamatan",kecamatan);
                obj.put("desa",desa);
                if (Integer.parseInt(hari) <= 15){
                    obj.put("periode_pengamatan","Periode 1-15");
                }else {
                    obj.put("periode_pengamatan","Periode 16-31");
                }
                obj.put("luas_tanaman",luas_tanaman);
                obj.put("umur_tanaman",umur_tanaman);
                obj.put("jenis_opt", populasiMP);
                obj.put("intensitas_serang",intensitas_serang);
                obj.put("luas_sembuh",luas_sembuh);
                obj.put("luas_sisa_serang",luas_sisa_serang);
                obj.put("serang_ringan",luas_sisa_serang);
                obj.put("serang_sedang",luas_sisa_serang);
                obj.put("serang_berat",luas_sisa_serang);
                obj.put("serang_puso",luas_sisa_serang);
                obj.put("jumlah_sisa_serang",luas_sisa_serang);
                obj.put("intensitas_tambah",String.format("%.2f", intensitas_populasi));
                obj.put("puso_tambah_serang","0");
                obj.put("luas_pengendalian",luas_pengendalian);
                obj.put("keadaan_ringan","0");
                obj.put("keadaan_sedang","0");
                obj.put("keadaan_berat","0");
                obj.put("keadaan_puso","0");
                obj.put("luas_keadaan_serang",luas_keadaan_serang);
                obj.put("intensitas_keadaan",intensitas_keadaan);
                obj.put("puso_keadaan_serang",puso_keadaan_serang);
                obj.put("jumlah_keadaan_serang",luas_keadaan_serang);
                if (intensitas_populasi <= 25){
                    obj.put("luas_waspada",luas_tambah_serang);
                    obj.put("jumlah_tambah_serang","0");
                    obj.put("luas_tambah_serang","0");
                    obj.put("tambah_ringan","0");
                    obj.put("tambah_sedang","0");
                    obj.put("tambah_berat","0");
                    obj.put("tambah_puso","0");
                }else{
                    obj.put("luas_waspada","0");
                    obj.put("jumlah_tambah_serang",luas_tambah_serang);
                    obj.put("luas_tambah_serang",luas_tambah_serang);
                    obj.put("tambah_ringan",luas_tambah_serang);
                    obj.put("tambah_sedang","0");
                    obj.put("tambah_berat","0");
                    obj.put("tambah_puso","0");
                }
//                if (populasiMP.equals("01")){
//                    if (intensitas_populasi <= 10){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 10 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }else if (populasiMP.equals("WBC")){
//                    if (intensitas_populasi <= 20){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 20 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasiMP.equals("Tikus")){
//                    if (intensitas_populasi <= 15){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 15 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasiMP.equals("HP")){
//                    if (intensitas_populasi < 25){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi == 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasiMP.equals("HPP")){
//                    if (intensitas_populasi < 25){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi == 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasiMP.equals("UG")){
//                    if (intensitas_populasi <= 15){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 15 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasiMP.equals("07")){
//                    if (intensitas_populasi <= 0.6){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 0.6 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasiMP.equals("Ganjur")){
//                    if (intensitas_populasi <= 5){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 5 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasiMP.equals("08")){
//                    if (intensitas_populasi <= 5){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 5 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasiMP.equals("BLB")){
//                    if (intensitas_populasi <= 11){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 11 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasiMP.equals("BHD")){
//                    if (intensitas_populasi <= 11){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 11 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }else {
//                    if (intensitas_populasi > 0 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//
//                }
                obj.put("id_hasil_pengamatan",id_hasil);
                obj.put("luas_panen",luas_panen);
                obj.put("kimia",kimia);
                obj.put("nabati",nabati);
                obj.put("eradikasi",eradikasi);
                obj.put("cara_lain",cara_lain);
                obj.put("jumlah_pengendalian",jumlah_pengendalian);
                obj.put("tanggal_intensitas",tanggal);
                obj.put("status_pelaporan","Belum");
                obj.put("frek_kimia",frek_kimia);
                obj.put("frek_nabati",frek_nabati);
                obj.put("approval_kab","Belum");
                obj.put("approval_satpel","Belum");
                obj.put("approval_provinsi","Belum");
                obj.put("kab_to_popt","Tidak Ada Catatan");
                obj.put("satpel_to_popt","Tidak Ada Catatan");
                obj.put("satpel_to_kab","Tidak Ada Catatan");
                obj.put("prov_to_kab","Tidak Ada Catatan");
                obj.put("prov_to_satpel","Tidak Ada Catatan");
                obj.put("prov_to_popt","Tidak Ada Catatan");
                obj.put("surat_kab","Belum");
                obj.put("surat_satpel","Belum");
                obj.put("batas_waktu_kab","tidak ada batas waktu");
                obj.put("batas_waktu_satpel","tidak ada batas waktu");
                obj.put("surat_provinsi","Belum");
//                obj.put("musuh_alami",String.valueOf(optMa));
//                obj.put("intensitas_ma",String.valueOf(jumlahMa));
                obj.put("rekomendasi","");
                obj.put("status","populasi(m2)");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);
        }
//
        StringRequest jobReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            getDataMaPopulasi();
//                            getDataMaPopulasi();
//                            try {
////                                insertDataPop();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("data", String.valueOf(array));
                return params;
            }
        };

        requestQueue.add(jobReq);
    }

    private void insertDataPopulasi() {
        String url = Constants.ROOT_URL+"Inten_Mutlak";
        final String kecamatan = Common.currentUser.getAlamat();
        final String wilayah_pengamatan = Common.currentUser.getProvinsi();
        final String kabupaten = Common.currentUser.getKabupaten();
        final String desa = Common.sum.getDesa();
        final String luas_tanaman = Common.sum.getLuas_hamparan();
        final String umur_tanaman = Common.sum.getDari_umur() +"-"+Common.sum.getHingga_umur();
        final String luas_sembuh = "0";
        final String luas_sisa_serang = "0";
        final String intensitas_serang = "0";
        final String puso_sisa_serang = "0";
        final String jumlah_sisa_serang = "0";
        final String luas_tambah_serang = Common.sum.getLuas_diamati();
        final String intensitas_tambah = "0";
        final String puso_tambah_serang = "0";
        final String jumlah_tambah_serang = "0";
        final String luas_pengendalian = "0";
        final String luas_keadaan_serang = "0";
        final String intensitas_keadaan = "0";
        final String puso_keadaan_serang = "0";
        final String luas_waspada = "0";
        final String luas_panen = "0";
        final String kimia = "0";
        final String nabati = "0";
        final String eradikasi = "0";
        final String cara_lain = "0";
        final String jumlah_pengendalian = "0";
        final String frek_kimia = "0";
        final String frek_nabati = "0";

        final String jumlah_anakan = Common.sum.getJumlah_anakan();

//
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        final String tanggal = date;

        requestQueue = Volley.newRequestQueue(this);
        final JSONArray array=new JSONArray();
        dateFormat = new SimpleDateFormat("dd");
        hari = dateFormat.format(calendar.getTime());

        float intensitas_populasi = Float.parseFloat(jumlahPopulasi)/30;
//        float intensitas_ma = Float.parseFloat(jumlahMA)/30;

        for(int i=0;i<listPopulasi.size();i++){
            JSONObject obj=new JSONObject();
            try {
                obj.put("wilayah_pengamatan", wilayah_pengamatan);
                obj.put("kabupaten",kabupaten);
                obj.put("kecamatan",kecamatan);
                obj.put("desa",desa);
                if (Integer.parseInt(hari) <= 15){
                    obj.put("periode_pengamatan","Periode 1-15");
                }else {
                    obj.put("periode_pengamatan","Periode 16-31");
                }
                obj.put("luas_tanaman",luas_tanaman);
                obj.put("umur_tanaman",umur_tanaman);
                obj.put("jenis_opt", populasi);
                obj.put("intensitas_serang",intensitas_serang);
                obj.put("luas_sembuh",luas_sembuh);
                obj.put("luas_sisa_serang",luas_sisa_serang);
                obj.put("serang_ringan",luas_sisa_serang);
                obj.put("serang_sedang",luas_sisa_serang);
                obj.put("serang_berat",luas_sisa_serang);
                obj.put("serang_puso",luas_sisa_serang);
                obj.put("jumlah_sisa_serang",luas_sisa_serang);
                obj.put("intensitas_tambah",String.format("%.2f", intensitas_populasi));
                obj.put("puso_tambah_serang","0");
                obj.put("luas_pengendalian",luas_pengendalian);
                obj.put("keadaan_ringan","0");
                obj.put("keadaan_sedang","0");
                obj.put("keadaan_berat","0");
                obj.put("keadaan_puso","0");
                obj.put("luas_keadaan_serang",luas_keadaan_serang);
                obj.put("intensitas_keadaan",intensitas_keadaan);
                obj.put("puso_keadaan_serang",puso_keadaan_serang);
                obj.put("jumlah_keadaan_serang",luas_keadaan_serang);
                if (intensitas_populasi <= 25){
                    obj.put("luas_waspada",luas_tambah_serang);
                    obj.put("jumlah_tambah_serang","0");
                    obj.put("luas_tambah_serang","0");
                    obj.put("tambah_ringan","0");
                    obj.put("tambah_sedang","0");
                    obj.put("tambah_berat","0");
                    obj.put("tambah_puso","0");
                }else{
                    obj.put("luas_waspada","0");
                    obj.put("jumlah_tambah_serang",luas_tambah_serang);
                    obj.put("luas_tambah_serang",luas_tambah_serang);
                    obj.put("tambah_ringan",luas_tambah_serang);
                    obj.put("tambah_sedang","0");
                    obj.put("tambah_berat","0");
                    obj.put("tambah_puso","0");
                }

//                if (populasi.equals("01")){
//                    if (intensitas_populasi <= 10){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 10 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }else if (populasi.equals("02")){
//                    if (intensitas_populasi <= 20){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 20 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasi.equals("Tikus")){
//                    if (intensitas_populasi <= 15){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 15 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasi.equals("HP")){
//                    if (intensitas_populasi < 25){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi == 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasi.equals("HPP")){
//                    if (intensitas_populasi < 25){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi == 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasi.equals("05")){
//                    if (intensitas_populasi <= 15){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 15 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasi.equals("WS")){
//                    if (intensitas_populasi <= 0.6){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 0.6 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasi.equals("Ganjur")){
//                    if (intensitas_populasi <= 5){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 5 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasi.equals("KT")){
//                    if (intensitas_populasi <= 5){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 5 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasi.equals("BLB")){
//                    if (intensitas_populasi <= 11){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 11 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }
//                else if (populasi.equals("BHD")){
//                    if (intensitas_populasi <= 11){
//                        obj.put("luas_waspada",luas_tambah_serang);
//                        obj.put("jumlah_tambah_serang","0");
//                        obj.put("luas_tambah_serang","0");
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 11 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//                }else {
//                    if (intensitas_populasi > 0 && intensitas_populasi <= 25){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan",luas_tambah_serang);
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 25 && intensitas_populasi <= 50){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang",luas_tambah_serang);
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 50 && intensitas_populasi <= 85){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat",luas_tambah_serang);
//                        obj.put("tambah_puso","0");
//                    }else if (intensitas_populasi > 85 && intensitas_populasi <= 100){
//                        obj.put("luas_waspada","0");
//                        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//                        obj.put("luas_tambah_serang",luas_tambah_serang);
//                        obj.put("tambah_ringan","0");
//                        obj.put("tambah_sedang","0");
//                        obj.put("tambah_berat","0");
//                        obj.put("tambah_puso",luas_tambah_serang);
//                    }
//
//                }
                obj.put("id_hasil_pengamatan",id_hasil);
                obj.put("luas_panen",luas_panen);
                obj.put("kimia",kimia);
                obj.put("nabati",nabati);
                obj.put("eradikasi",eradikasi);
                obj.put("cara_lain",cara_lain);
                obj.put("jumlah_pengendalian",jumlah_pengendalian);
                obj.put("tanggal_intensitas",tanggal);
                obj.put("status_pelaporan","Belum");
                obj.put("frek_kimia",frek_kimia);
                obj.put("frek_nabati",frek_nabati);
                obj.put("approval_kab","Belum");
                obj.put("approval_satpel","Belum");
                obj.put("approval_provinsi","Belum");
                obj.put("kab_to_popt","Tidak Ada Catatan");
                obj.put("satpel_to_popt","Tidak Ada Catatan");
                obj.put("satpel_to_kab","Tidak Ada Catatan");
                obj.put("prov_to_kab","Tidak Ada Catatan");
                obj.put("prov_to_satpel","Tidak Ada Catatan");
                obj.put("prov_to_popt","Tidak Ada Catatan");
                obj.put("surat_kab","Belum");
                obj.put("surat_satpel","Belum");
                obj.put("batas_waktu_kab","tidak ada batas waktu");
                obj.put("batas_waktu_satpel","tidak ada batas waktu");
                obj.put("surat_provinsi","Belum");
//                obj.put("musuh_alami",String.valueOf(optMa));
//                obj.put("intensitas_ma",String.valueOf(jumlahMa));
                obj.put("rekomendasi","");
                obj.put("status","populasi");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);
        }
//
        StringRequest jobReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
//                            deleteDuplicateValue();
//                            getDataMaPopulasi();
                            getDataPopulasiMP();
//                            try {
////                                insertDataPop();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("data", String.valueOf(array));
                return params;
            }
        };

        requestQueue.add(jobReq);
    }

//    private void insertDataPop() throws JSONException {
//        progressDialog.setTitle("Insert Data");
//        progressDialog.show();
//        progressDialog.setCancelable(false);
//        String url = Constants.ROOT_URL+"Insert";
//        final String kecamatan = Common.currentUser.getAlamat();
//        final String wilayah_pengamatan = Common.currentUser.getProvinsi();
//        final String kabupaten = Common.currentUser.getKabupaten();
//        final String desa = Common.sum.getDesa();
//        final String luas_tanaman = Common.sum.getLuas_hamparan();
//        final String umur_tanaman = Common.sum.getDari_umur() +"-"+Common.sum.getHingga_umur();
//        final String luas_sembuh = "0";
//        final String luas_sisa_serang = "0";
//        final String intensitas_serang = "0";
//        final String puso_sisa_serang = "0";
//        final String jumlah_sisa_serang = "0";
//        final String luas_tambah_serang = Common.sum.getLuas_diamati();
//        final String intensitas_tambah = "0";
//        final String puso_tambah_serang = "0";
//        final String jumlah_tambah_serang = "0";
//        final String luas_pengendalian = "0";
//        final String luas_keadaan_serang = "0";
//        final String intensitas_keadaan = "0";
//        final String puso_keadaan_serang = "0";
//        final String luas_waspada = "0";
//        final String luas_panen = "0";
//        final String kimia = "0";
//        final String nabati = "0";
//        final String eradikasi = "0";
//        final String cara_lain = "0";
//        final String jumlah_pengendalian = "0";
//        final String frek_kimia = "0";
//        final String frek_nabati = "0";
//
////        String opms = Common.sum.getOpms();
////        String domi = Common.sum.getDomi();
////        String kisar = Common.sum.getKisar();
//        String luas_spot = Common.sum.getLuas_spot_hopperburn();
//
////        float inten_opms = Integer.parseInt(opms)/30;
////        float inten_domi = Integer.parseInt(domi)/30;
////        float inten_kisar = Integer.parseInt(kisar)/30;
//        final float inten_luas_spot = Integer.parseInt(luas_spot)/30;
//
//        final String jumlah_anakan = Common.sum.getJumlah_anakan();
//
////        List<String> list = new ArrayList<>();
//////        list.add("OPMS");
//////        list.add("Domi");
//////        list.add("Kisar");
////        list.add("Luas Spot Hopperburn");
//
////        List<String> listIntensitas = new ArrayList<>();
//////        listIntensitas.add(String.valueOf(inten_opms));
//////        listIntensitas.add(String.valueOf(inten_domi));
//////        listIntensitas.add(String.valueOf(inten_kisar));
////        listIntensitas.add(String.valueOf(inten_luas_spot));
//
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        date = dateFormat.format(calendar.getTime());
//        final String tanggal = date;
//
//        requestQueue = Volley.newRequestQueue(this);
//        final JSONArray array=new JSONArray();
//        dateFormat = new SimpleDateFormat("dd");
//        hari = dateFormat.format(calendar.getTime());
//
////        float intensitas_ma = Float.parseFloat(jumlahPopulasiMA)/30;
//
//        JSONObject obj=new JSONObject();
//        obj.put("wilayah_pengamatan", wilayah_pengamatan);
//        obj.put("kabupaten",kabupaten);
//        obj.put("kecamatan",kecamatan);
//        obj.put("desa",desa);
//        if (Integer.parseInt(hari) <= 15){
//            obj.put("periode_pengamatan","Periode 1-15");
//        }else {
//            obj.put("periode_pengamatan","Periode 16-31");
//        }
//        obj.put("luas_tanaman",luas_tanaman);
//        obj.put("umur_tanaman",umur_tanaman);
//        obj.put("jenis_opt", "Luas Spot Hopperburn");
//        obj.put("intensitas_serang",intensitas_serang);
//        obj.put("luas_sembuh",luas_sembuh);
//        obj.put("luas_sisa_serang",luas_sisa_serang);
//        obj.put("serang_ringan",luas_sisa_serang);
//        obj.put("serang_sedang",luas_sisa_serang);
//        obj.put("serang_berat",luas_sisa_serang);
//        obj.put("serang_puso",luas_sisa_serang);
//        obj.put("jumlah_sisa_serang",luas_sisa_serang);
//        obj.put("intensitas_tambah",String.valueOf(inten_luas_spot));
//        if (Float.parseFloat(String.valueOf(inten_luas_spot)) >= 85.0){
//            obj.put("puso_tambah_serang",luas_tambah_serang);
//        }
//        obj.put("jumlah_tambah_serang",luas_tambah_serang);
//        obj.put("luas_tambah_serang",luas_tambah_serang);
//        obj.put("luas_pengendalian",luas_pengendalian);
//        obj.put("luas_keadaan_serang",luas_keadaan_serang);
//        obj.put("intensitas_keadaan",intensitas_keadaan);
//        obj.put("puso_keadaan_serang",puso_keadaan_serang);
//        obj.put("jumlah_keadaan_serang",luas_keadaan_serang);
//        obj.put("luas_waspada",luas_waspada);
//        obj.put("id_hasil_pengamatan",id_hasil);
//        obj.put("luas_panen",luas_panen);
//        obj.put("kimia",kimia);
//        obj.put("nabati",nabati);
//        obj.put("eradikasi",eradikasi);
//        obj.put("cara_lain",cara_lain);
//        obj.put("jumlah_pengendalian",jumlah_pengendalian);
//        obj.put("tanggal_intensitas",tanggal);
//        obj.put("status_pelaporan","Belum");
//        obj.put("frek_kimia",frek_kimia);
//        obj.put("frek_nabati",frek_nabati);
//        obj.put("approval_kab","Belum");
//        obj.put("approval_satpel","Belum");
//        obj.put("approval_provinsi","Belum");
//        obj.put("kab_to_popt","Tidak Ada Catatan");
//        obj.put("satpel_to_popt","Tidak Ada Catatan");
//        obj.put("satpel_to_kab","Tidak Ada Catatan");
//        obj.put("prov_to_kab","Tidak Ada Catatan");
//        obj.put("prov_to_satpel","Tidak Ada Catatan");
//        obj.put("prov_to_popt","Tidak Ada Catatan");
//        obj.put("surat_kab","Belum");
//        obj.put("surat_satpel","Belum");
//        obj.put("batas_waktu_kab","tidak ada batas waktu");
//        obj.put("batas_waktu_satpel","tidak ada batas waktu");
//        obj.put("surat_provinsi","Belum");
//        obj.put("musuh_alami","tidak ada musuh alami");
//        obj.put("intensitas_ma","0");
//        obj.put("rekomendasi","");
//
//        array.put(obj);
////
//        StringRequest jobReq = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (response.contains("berhasil")){
//                            deleteDuplicateValue();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        progressDialog.dismiss();
//                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("data", String.valueOf(array));
////                params.put("kabupaten",kabupaten);
////                params.put("kecamatan",kecamatan);
////                params.put("desa",desa);
////                if (Integer.parseInt(hari) <= 15){
////                    params.put("periode_pengamatan","Periode 1-15");
////                }else {
////                    params.put("periode_pengamatan","Periode 16-31");
////                }
////                params.put("luas_tanaman",luas_tanaman);
////                params.put("umur_tanaman",umur_tanaman);
////                params.put("jenis_opt", "Luas Spot Hopperburn");
////                params.put("intensitas_serang",intensitas_serang);
////                params.put("luas_sembuh",luas_sembuh);
////                params.put("luas_sisa_serang",luas_sisa_serang);
////                params.put("serang_ringan",luas_sisa_serang);
////                params.put("serang_sedang",luas_sisa_serang);
////                params.put("serang_berat",luas_sisa_serang);
////                params.put("serang_puso",luas_sisa_serang);
////                params.put("jumlah_sisa_serang",luas_sisa_serang);
////                params.put("intensitas_tambah",String.valueOf(inten_luas_spot));
////                if (Float.parseFloat(String.valueOf(inten_luas_spot)) >= 85.0){
////                    params.put("puso_tambah_serang",luas_tambah_serang);
////                }
////                params.put("jumlah_tambah_serang",luas_tambah_serang);
////                params.put("luas_tambah_serang",luas_tambah_serang);
////                params.put("luas_pengendalian",luas_pengendalian);
////                params.put("luas_keadaan_serang",luas_keadaan_serang);
////                params.put("intensitas_keadaan",intensitas_keadaan);
////                params.put("puso_keadaan_serang",puso_keadaan_serang);
////                params.put("jumlah_keadaan_serang",luas_keadaan_serang);
////                params.put("luas_waspada",luas_waspada);
////                params.put("id_hasil_pengamatan",id_hasil_pengamatan);
////                params.put("luas_panen",luas_panen);
////                params.put("kimia",kimia);
////                params.put("nabati",nabati);
////                params.put("eradikasi",eradikasi);
////                params.put("cara_lain",cara_lain);
////                params.put("jumlah_pengendalian",jumlah_pengendalian);
////                params.put("tanggal_intensitas",tanggal);
////                params.put("status_pelaporan","Belum");
////                params.put("frek_kimia",frek_kimia);
////                params.put("frek_nabati",frek_nabati);
////                params.put("approval_kab","Belum");
////                params.put("approval_satpel","Belum");
////                params.put("approval_provinsi","Belum");
////                params.put("kab_to_popt","Tidak Ada Catatan");
////                params.put("satpel_to_popt","Tidak Ada Catatan");
////                params.put("satpel_to_kab","Tidak Ada Catatan");
////                params.put("prov_to_kab","Tidak Ada Catatan");
////                params.put("prov_to_satpel","Tidak Ada Catatan");
////                params.put("prov_to_popt","Tidak Ada Catatan");
////                params.put("surat_kab","Belum");
////                params.put("surat_satpel","Belum");
////                params.put("batas_waktu_kab","tidak ada batas waktu");
////                params.put("batas_waktu_satpel","tidak ada batas waktu");
////                params.put("surat_provinsi","Belum");
////                params.put("musuh_alami","tidak ada musuh alami");
////                params.put("intensitas_ma","0");
//                return params;
//            }
//        };
//
//        requestQueue.add(jobReq);
//    }

    private void deleteDuplicateValue() {
        String url = Constants.ROOT_URL+"Intensitas";
        StringRequest jobReq = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(PdfView.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            SessionManager sessionManager = new SessionManager(PdfView.this, SessionManager.SESSION_ID);
                            sessionManager.clearIdHasil();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jobReq);
    }

    private void insertNotification() {
        String url = Constants.ROOT_URL+"Notification";
        final String username = Common.currentUser.getKabupaten();
        StringRequest jobReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(PdfView.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfView.this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", "Belum");
                params.put("id_laporan", id_intensitas);
                params.put("title", "Laporan Butuh Validasi");
                params.put("msg", "Mohon validasi laporan ini sebelum tanggal yang ditentukan");
                params.put("laporan", "Harian");
                params.put("username", username);
                params.put("towho", "Kortikab");
                return params;
            }
        };

        requestQueue.add(jobReq);
    }
}