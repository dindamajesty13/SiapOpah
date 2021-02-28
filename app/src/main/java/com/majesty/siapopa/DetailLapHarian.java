package com.majesty.siapopa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

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
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

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
import java.util.Map;

public class DetailLapHarian extends AppCompatActivity {
    TextView txtTanggal, txtOPT, txtLuasTambah, txtIntensitas, txtLuasHamparan, txtLuasWaspada,
            txtUmurTanaman, txtKecamatan, txtDesa, txtBlok, txtVarietas, txtKomoditas, txtPolaTanam;
    MaterialEditText edtRekomendasi, edtLuasWaspada;
    String id_intensitas, rekomendasi;
    Toolbar toolbar;
    Button btnLapor, btnSimpan, btn_cetak;
    ProgressDialog progressDialog;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date, surat_kab, surat_satpel, surat_provinsi, surat_popt;
    AppCompatImageView img_bukti;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    TextView txtPelaporan, txtApprovalKab, txtApprovalSatpel, txtApprovalProv, msg_kab, msg_satpel, msg_prov, txtRekomendasi, text, txtMusuhAlami, txtPopulasi, txtPopulasiM;
    String petugas_pengamatan, status, tanggal_intensitas, kabupaten, kecamatan, desa, blok, intensitas_tambah, luas_hamparan, luas_diamati, komoditas, luas_tanaman, umur_tanaman, jenis_opt, luas_sisa_serang, luas_waspada, varietas;
    StringBuilder populasi, populasiM2, MA, jumlahPopulasi, jumlahPopulasiM2, jumlahMA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lap_harian);

        txtPopulasiM = (TextView) findViewById(R.id.txtPopulasiM);
        txtPopulasi = (TextView) findViewById(R.id.txtPopulasi);
        txtMusuhAlami = (TextView) findViewById(R.id.txtMusuhAlami);
        txtRekomendasi = (TextView) findViewById(R.id.txtRekomendasi);
        text = (TextView) findViewById(R.id.text);
        msg_kab = (TextView) findViewById(R.id.msg_kab);
        msg_satpel = (TextView) findViewById(R.id.msg_satpel);
        msg_prov = (TextView) findViewById(R.id.msg_prov);
        txtPelaporan = (TextView) findViewById(R.id.txtPelaporan);
        txtApprovalKab = (TextView) findViewById(R.id.txtApprovalKab);
        txtApprovalSatpel = (TextView) findViewById(R.id.txtApprovalSatpel);
        txtApprovalProv = (TextView) findViewById(R.id.txtApprovalProv);
        txtKomoditas = (TextView) findViewById(R.id.txtKomoditas);
//        TextView txtPeringatan = findViewById(R.id.peringatan);
//        txtPeringatan.setVisibility(View.GONE);
        edtRekomendasi = findViewById(R.id.edtRekomendasi);
        edtLuasWaspada = findViewById(R.id.edtLuasWaspada);
        txtVarietas = (TextView) findViewById(R.id.txtVarietas);
        txtLuasTambah = (TextView) findViewById(R.id.txtLuasTambah);
        txtLuasHamparan = (TextView) findViewById(R.id.txtLuasHamparan);
        txtPolaTanam = (TextView) findViewById(R.id.txtPolaTanam);
        txtIntensitas = (TextView) findViewById(R.id.txtIntensitas);
        txtBlok = (TextView) findViewById(R.id.txtBlok);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Laporan Harian");
        setSupportActionBar(toolbar);
        txtLuasWaspada = (TextView) findViewById(R.id.txtLuasWaspada);
        txtKecamatan = (TextView) findViewById(R.id.txtKecamatan);
        txtDesa = (TextView) findViewById(R.id.txtDesa);
        txtTanggal = (TextView) findViewById(R.id.txtTanggal);
        txtUmurTanaman = (TextView) findViewById(R.id.txtUmurTanaman);
        txtOPT = (TextView) findViewById(R.id.txtOPT);
        btnLapor = (Button) findViewById(R.id.btnLapor);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        img_bukti = (AppCompatImageView) findViewById(R.id.img_bukti);
        progressDialog = new ProgressDialog(this);
        calendar = Calendar.getInstance();
//        btn_cetak = (Button) findViewById(R.id.btn_cetak);

        id_intensitas = getIntent().getStringExtra("id");
        if (getIntent().getStringExtra("status").equals("Sudah")){
            btnSimpan.setVisibility(View.GONE);
        }else {
            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    simpanData();
                }
            });
        }

        updateStatus();
        getDataDetail();
        getLaporan();

//        btnLapor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateDataIntensitas();
//
//            }
//        });
        btnLapor.setOnClickListener(new View.OnClickListener() {
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

    private void getDataDetail() {
        String id_hasil = getIntent().getStringExtra("id_hasil");
        populasi = new StringBuilder();
        populasiM2 = new StringBuilder();
        MA = new StringBuilder();

        String urlLaporan = Constants.ROOT_URL + "Inten_Mutlak?id_hasil=" + id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            String nama_pop = "";
                            String nama_pop_m = "";
                            String nama_pop_ma = "";

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);


                                String status = object.getString("status");
                                if (status.equals("populasi")){
                                    String opt = object.getString("jenis_opt");
                                    String intensitas = object.getString("intensitas_tambah");

                                    nama_pop = opt+": "+intensitas+" || ";
                                    populasi.append(nama_pop);
                                }else if (status.equals("populasi(m2)")){
                                    String opt = object.getString("jenis_opt");
                                    String intensitas = object.getString("intensitas_tambah");

                                    nama_pop_m = opt+": "+intensitas+" || ";
                                    populasiM2.append(nama_pop_m);
                                }else if (status.equals("MA")){
                                    String opt = object.getString("jenis_opt");
                                    String intensitas = object.getString("intensitas_tambah");

                                    nama_pop_ma = opt+": "+intensitas+" || ";
                                    MA.append(nama_pop_ma);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailLapHarian.this, "No Data Found", Toast.LENGTH_LONG).show();
                populasi.append("");
                populasiM2.append("");
                MA.append("");
            }
        });
        Volley.newRequestQueue(DetailLapHarian.this).add(stringRequest);
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
        RequestQueue requestQueue = Volley.newRequestQueue(DetailLapHarian.this);
        requestQueue.add(request);
    }

    private void getData() {
        String url = Constants.ROOT_URL + "Intensitas?id=" + id_intensitas;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/siap_opa/harian");
                            if (!docsFolder.exists()) {
                                docsFolder.mkdir();
                            }
                            String pdfname = "Laporan Harian "+tanggal_intensitas+".pdf";
                            pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
                            OutputStream output = null;
                            try {
                                output = new FileOutputStream(pdfFile);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            final Document document = new Document(PageSize.A4.rotate());
                            final Font a = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.NORMAL, BaseColor.BLACK);
                            final PdfPTable data = new PdfPTable(new float[]{2, 2});
                            data.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                            data.getDefaultCell().setFixedHeight(50);
                            data.setTotalWidth(PageSize.A4.getWidth());
                            data.setWidthPercentage(100);
                            data.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                            PdfPCell note1 = new PdfPCell(new Paragraph("Provinsi: Jawa Barat", a));
                            PdfPCell note2 = new PdfPCell(new Paragraph("Kabupaten: "+kabupaten, a));
                            PdfPCell note3 = new PdfPCell(new Paragraph("Kecamatan: "+kecamatan, a));
                            PdfPCell note4 = new PdfPCell(new Paragraph("Tanggal Pengamatan: "+tanggal_intensitas, a));
                            note1.setBorder(Rectangle.NO_BORDER);
                            note2.setBorder(Rectangle.NO_BORDER);
                            note3.setBorder(Rectangle.NO_BORDER);
                            note4.setBorder(Rectangle.NO_BORDER);
                            data.addCell(note1);
                            data.addCell(note2);
                            data.addCell(note3);
                            data.addCell(note4);
                            final PdfPTable table = new PdfPTable(15);
                            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.getDefaultCell().setFixedHeight(50);
                            table.setTotalWidth(PageSize.A4.getWidth());
                            table.setWidthPercentage(100);
                            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                            PdfPCell nomorCell = new PdfPCell(new Paragraph("No", a));
                            table.addCell(nomorCell);

                            PdfPCell desaCell = new PdfPCell(new Paragraph("Desa", a));
                            table.addCell(desaCell);

                            PdfPCell blokCell = new PdfPCell(new Paragraph("Blok", a));
                            table.addCell(blokCell);

                            PdfPCell kom = new PdfPCell(new Paragraph("Kom", a));
                            table.addCell(kom);

                            PdfPCell var = new PdfPCell(new Paragraph("Var", a));
                            table.addCell(var);

                            PdfPCell umur = new PdfPCell(new Paragraph("Umur Tanaman (HST)", a));
                            table.addCell(umur);

                            PdfPCell luas_tanamanCell = new PdfPCell(new Paragraph("L Pertanaman (Ha)", a));
                            table.addCell(luas_tanamanCell);

                            PdfPCell luas_serangan = new PdfPCell(new Paragraph("L Terserang (Ha)", a));
                            table.addCell(luas_serangan);

                            PdfPCell opt = new PdfPCell(new Paragraph("Jenis OPT", a));
                            table.addCell(opt);

                            PdfPCell intensitas = new PdfPCell(new Paragraph("Inten (%)/Popul (e/r)/(e/m2)", a));
                            table.addCell(intensitas);

                            PdfPCell populasiCell = new PdfPCell(new Paragraph("Populasi (e/r)", a));
                            table.addCell(populasiCell);

                            PdfPCell populasiMP = new PdfPCell(new Paragraph("Populasi (m2)", a));
                            table.addCell(populasiMP);

                            PdfPCell mA = new PdfPCell(new Paragraph("Musuh Alami (e/r)", a));
                            table.addCell(mA);

                            PdfPCell luas_waspadaCell = new PdfPCell(new Paragraph("L Was (Ha)", a));
                            table.addCell(luas_waspadaCell);

                            PdfPCell rekomendasiCell = new PdfPCell(new Paragraph("Rekomendasi", a));
                            table.addCell(rekomendasiCell);

                            table.setHeaderRows(1);
                            PdfPCell[] cells = table.getRow(0).getCells();
                            for (int j = 0; j < cells.length; j++) {
                                cells[j].setBackgroundColor(BaseColor.GRAY);
                            }
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int nomor = i + 1;
                                desa = object.getString("desa");
                                blok = object.getString("blok");
                                komoditas = object.getString("komoditas");
                                luas_tanaman = object.getString("luas_tanaman");
                                umur_tanaman = object.getString("umur_tanaman");
                                jenis_opt = object.getString("jenis_opt");
                                varietas = object.getString("varietas");
                                intensitas_tambah = object.getString("intensitas_tambah");
                                luas_sisa_serang = object.getString("luas_tambah_serang");
                                luas_waspada = object.getString("luas_waspada");
                                tanggal_intensitas = object.getString("tanggal_intensitas");
                                rekomendasi = object.getString("rekomendasi");
                                status = object.getString("status_pelaporan");
                                petugas_pengamatan = object.getString("petugas_pengamatan");

                                nomorCell = new PdfPCell(new Paragraph(String.valueOf(nomor), a));
                                table.addCell(nomorCell);

                               desaCell = new PdfPCell(new Paragraph(desa, a));
                                table.addCell(desaCell);

                                blokCell = new PdfPCell(new Paragraph(blok, a));
                                table.addCell(blokCell);

                                kom = new PdfPCell(new Paragraph(komoditas, a));
                                table.addCell(kom);

                                var = new PdfPCell(new Paragraph(varietas, a));
                                table.addCell(var);

                                umur = new PdfPCell(new Paragraph(umur_tanaman, a));
                                table.addCell(umur);

                                luas_tanamanCell = new PdfPCell(new Paragraph(luas_tanaman, a));
                                table.addCell(luas_tanamanCell);

                                luas_serangan = new PdfPCell(new Paragraph(luas_sisa_serang, a));
                                table.addCell(luas_serangan);

                                opt = new PdfPCell(new Paragraph(jenis_opt, a));
                                table.addCell(opt);

                                intensitas = new PdfPCell(new Paragraph(intensitas_tambah, a));
                                table.addCell(intensitas);

                                populasiCell = new PdfPCell(new Paragraph(String.valueOf(populasi), a));
                                table.addCell(populasiCell);

                                populasiMP = new PdfPCell(new Paragraph(String.valueOf(populasiM2), a));
                                table.addCell(populasiMP);

                                mA = new PdfPCell(new Paragraph(String.valueOf(MA), a));
                                table.addCell(mA);

                                luas_waspadaCell = new PdfPCell(new Paragraph(luas_waspada, a));
                                table.addCell(luas_waspadaCell);

                                rekomendasiCell = new PdfPCell(new Paragraph(rekomendasi, a));
                                table.addCell(rekomendasiCell);


                            }
                            String id_hasil = getIntent().getStringExtra("id_hasil");

                            String url = Constants.ROOT_URL+"Opt?id_hsl="+id_hasil;
                            final OutputStream finalOutput = output;
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                final PdfPTable table2 = new PdfPTable(new float[]{2});
                                                table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                                                table2.getDefaultCell().setFixedHeight(50);
                                                table2.setTotalWidth(PageSize.A4.getWidth());
                                                table2.setWidthPercentage(100);
                                                table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                                                PdfPCell jumlah = new PdfPCell(new Paragraph("Keterangan: ", a));
                                                jumlah.setBorder(Rectangle.NO_BORDER);
//                                                                    jumlah.setHorizontalAlignment(Element.ALIGN_LEFT);
                                                table2.addCell(jumlah);

                                                JSONArray array = new JSONArray(response);
                                                for (int i = 0; i<array.length(); i++){
                                                    JSONObject object = array.getJSONObject(i);

                                                    String nama_opt = object.getString("nama_opt");
                                                    String opt = object.getString("jenis_opt");

                                                    jumlah = new PdfPCell(new Paragraph(opt+": "+nama_opt, a));
//                                                                        jumlah.setHorizontalAlignment(Element.ALIGN_LEFT);
                                                    jumlah.setBorder(Rectangle.NO_BORDER);
                                                    table2.addCell(jumlah);
                                                }

                                                PdfWriter.getInstance(document, finalOutput);
                                                document.open();
                                                Font a = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.NORMAL, BaseColor.BLACK);
                                                Font b = new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, Font.BOLD, BaseColor.BLACK);
                                                Font c = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.BOLD, BaseColor.BLACK);
                                                Paragraph title = new Paragraph("Laporan Harian", b);
                                                title.setAlignment(Element.ALIGN_CENTER);
                                                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                Date date = dateFormat.parse(tanggal_intensitas);

                                                SimpleDateFormat newDateFormat= new SimpleDateFormat("dd MMMM yyyy");
                                                String tanggal = newDateFormat.format(date);
                                                Paragraph subTitle = new Paragraph("Tanggal "+tanggal, c);
                                                subTitle.setAlignment(Element.ALIGN_CENTER);
                                                document.add(title);
                                                document.add(subTitle);
                                                document.add(data);
                                                document.add(table);
                                                document.add(table2);
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
                                    Toast.makeText(DetailLapHarian.this, "No Data Found", Toast.LENGTH_LONG).show();
                                }
                            });
                            Volley.newRequestQueue(DetailLapHarian.this).add(stringRequest);

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DetailLapHarian.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailLapHarian.this).add(stringRequest);

    }

    private void previewPdf() {
        Intent intent = new Intent(DetailLapHarian.this, PdfView.class);
        intent.putExtra("view", "Laporan Harian "+tanggal_intensitas+".pdf");
        intent.putExtra("id_intensitas", id_intensitas);
        intent.putExtra("status", status);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length <= 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(DetailLapHarian.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
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
            getData();
        }
    }

    private void updateDataIntensitas() {
        progressDialog.setTitle("Insert Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        String url = Constants.ROOT_URL+"Update_Inten";

        rekomendasi = edtRekomendasi.getText().toString();
        String luas_waspada = edtLuasWaspada.getText().toString();

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
                            progressDialog.dismiss();
                            Toast.makeText(DetailLapHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
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
                params.put("id_intensitas", id_intensitas);
                params.put("rekomendasi", rekomendasi);
                params.put("status_pelaporan", "Sudah");
                params.put("batas_waktu_kab", datePlusHour);
                params.put("batas_waktu_satpel", datePlusOne);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailLapHarian.this);
        requestQueue.add(request);

    }

    private void simpanData() {
        if (!edtRekomendasi.getText().toString().equals(rekomendasi)){
            progressDialog.setTitle("Insert Data");
            progressDialog.setMessage("Mohon Tunggu Sebentar");
            progressDialog.show();
            String url = Constants.ROOT_URL+"Update_Inten";

            rekomendasi = edtRekomendasi.getText().toString();
            final String luas_waspada = edtLuasWaspada.getText().toString();

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("berhasil")){
                                progressDialog.dismiss();
                                Toast.makeText(DetailLapHarian.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
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
                    params.put("rekomendasi", rekomendasi);
                    params.put("luas_waspada", luas_waspada);
                    params.put("status_pelaporan", "Belum");
                    params.put("batas_waktu_kab", "tidak ada batas waktu");
                    params.put("batas_waktu_satpel", "tidak ada batas waktu");

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(DetailLapHarian.this);
            requestQueue.add(request);
        }else {
            progressDialog.dismiss();
            edtRekomendasi.setError("Tidak ada perubahan data");
        }

    }

    private void getLaporan() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String urlLaporan = Constants.ROOT_URL + "Intensitas?id=" + id_intensitas;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                kabupaten = object.getString("kabupaten");
                                rekomendasi = object.getString("rekomendasi");
                                kecamatan = object.getString("kecamatan");
                                String desa = object.getString("desa");
                                String blok = object.getString("blok");
                                String komoditas = object.getString("komoditas");
                                String luas_tanaman = object.getString("luas_tanaman");
                                String umur_tanaman = object.getString("umur_tanaman");
                                String jenis_opt = object.getString("jenis_opt");
                                String varietas = object.getString("varietas");
                                String intensitas_tambah = object.getString("intensitas_tambah");
                                String luas_sisa_serang = object.getString("luas_tambah_serang");
                                String luas_waspada = object.getString("luas_waspada");
                                tanggal_intensitas = object.getString("tanggal_intensitas");
                                String pola_tanam = object.getString("pola_tanam");
                                String status_pelaporan = object.getString("status_pelaporan");
                                String approval_kab = object.getString("approval_kab");
                                String approval_satpel = object.getString("approval_satpel");
                                String approval_provinsi = object.getString("approval_provinsi");
                                String kab_to_popt = object.getString("kab_to_popt");
                                String satpel_to_popt = object.getString("satpel_to_popt");
                                String prov_to_popt = object.getString("prov_to_popt");
                                String bukti_foto = object.getString("bukti_foto");
                                String status = object.getString("status");
//                                String musuh_alami = object.getString("musuh_alami");
//                                String populasi = object.getString("populasi");
//                                String populasi_m2 = object.getString("populasi_m2");
                                String url = Constants.ROOT_URL + "assets/Images/" + bukti_foto;
                                txtKecamatan.setText(Html.fromHtml("<font color='#000000'><b>Kec: </b></font>" + kecamatan));
                                txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Desa: </b></font>" + desa));

                                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date = dateFormat.parse(tanggal_intensitas);

                                SimpleDateFormat newDateFormat= new SimpleDateFormat("dd MMMM yyyy");
                                String tanggal = newDateFormat.format(date);

                                txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Tanggal: </b></font>" + tanggal));
                                txtLuasHamparan.setText(Html.fromHtml("<font color='#000000'><b>L Pertanaman: </b></font>" + luas_tanaman + " Ha"));
                                txtUmurTanaman.setText(Html.fromHtml("<font color='#000000'><b>Umur Tanam: </b></font>" + umur_tanaman + " HST"));
                                txtOPT.setText(Html.fromHtml("<font color='#000000'><b>OPT: </b></font>" + jenis_opt));
                                txtLuasTambah.setText(Html.fromHtml("<font color='#000000'><b>L Tambah: </b></font>" + luas_sisa_serang + " Ha"));
                                txtLuasWaspada.setText(Html.fromHtml("<font color='#000000'><b>L Waspada: </b></font>" + luas_waspada + " Ha"));
                                if (status.equals("mutlak")){
                                    txtIntensitas.setText(Html.fromHtml("<font color='#000000'><b>Intensitas: </b></font>" + intensitas_tambah + " %"));
                                }else if (status.equals("tidak mutlak")){
                                    txtIntensitas.setText(Html.fromHtml("<font color='#000000'><b>Intensitas: </b></font>" + intensitas_tambah + " %"));
                                }else if (status.equals("populasi")){
                                    txtIntensitas.setText(Html.fromHtml("<font color='#000000'><b>Populasi: </b></font>" + intensitas_tambah + " e/r"));
                                }else if (status.equals("populasi(m2)")){
                                    txtIntensitas.setText(Html.fromHtml("<font color='#000000'><b>Populasi: </b></font>" + intensitas_tambah + " e/m2"));
                                }
                                txtKomoditas.setText(Html.fromHtml("<font color='#000000'><b>Kom: </b></font>" + komoditas));
                                txtVarietas.setText(Html.fromHtml("<font color='#000000'><b>Var: </b></font>" + varietas));
                                txtBlok.setText(Html.fromHtml("<font color='#000000'><b>Blok: </b></font>" + blok));
                                txtPolaTanam.setText(Html.fromHtml("<font color='#000000'><b>Pola Tanam: </b></font>" + pola_tanam));
                                txtPelaporan.setText(Html.fromHtml("<font color='#000000'><b>Pelaporan: </b></font>" + status_pelaporan));
                                txtApprovalKab.setText(Html.fromHtml("<font color='#000000'><b>Val Kortikab: </b></font>" + approval_kab));
                                txtApprovalSatpel.setText(Html.fromHtml("<font color='#000000'><b>Val SatPel: </b></font>" + approval_satpel));
                                txtApprovalProv.setText(Html.fromHtml("<font color='#000000'><b>Val BPTPH: </b></font>" + approval_provinsi));
                                msg_kab.setText(Html.fromHtml("<font color='#000000'><b>Kortikab: </b></font>" + kab_to_popt));
                                msg_prov.setText(Html.fromHtml("<font color='#000000'><b>BPTPH: </b></font>" + prov_to_popt));
                                msg_satpel.setText(Html.fromHtml("<font color='#000000'><b>Satpel: </b></font>" + satpel_to_popt));
                                txtMusuhAlami.setText(Html.fromHtml("<font color='#000000'><b>MA (e/r): </b></font>" + MA));
                                txtPopulasi.setText(Html.fromHtml("<font color='#000000'><b>Populasi (e/r): </b></font>" + populasi));
                                txtPopulasiM.setText(Html.fromHtml("<font color='#000000'><b>Populasi (m2): </b></font>" + populasiM2));
                                if (getIntent().getStringExtra("status").equals("Sudah")){
                                    btnSimpan.setVisibility(View.GONE);
                                    edtRekomendasi.setVisibility(View.GONE);
                                    txtRekomendasi.setText(rekomendasi);
                                    edtLuasWaspada.setVisibility(View.GONE);
                                    text.setVisibility(View.GONE);
                                }else {
                                    btnSimpan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            simpanData();
                                        }
                                    });
                                    edtRekomendasi.setText(rekomendasi);
                                    txtRekomendasi.setVisibility(View.GONE);
                                    edtLuasWaspada.setText(luas_waspada);
                                }

                                Picasso.get().load(url).into(img_bukti);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DetailLapHarian.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailLapHarian.this).add(stringRequest);
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