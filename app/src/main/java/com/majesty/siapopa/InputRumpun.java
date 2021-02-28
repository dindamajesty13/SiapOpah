package com.majesty.siapopa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.majesty.siapopa.adapter.DetailAdapter;
import com.majesty.siapopa.model.DetailModel;
import com.majesty.siapopa.model.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputRumpun extends AppCompatActivity {
    Toolbar toolbar;
    ImageView btn_add;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager1;
    Button btnLapor;
    private File pdfFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<DetailModel> listDetail;
    ProgressDialog progressDialog;
    private DetailAdapter adapter;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date, userAlamat, id_hasil, id_detail;
    RequestQueue requestQueue;
    String jumlah_anakan, opt_mutlak, jumlah_opt_mutlak, opt_tdk_mutlak, jumlah_opt_tdk_mutlak, opt_populasi, jumlah_opt_populasi, opt_populasi_m, jumlah_opt_populasi_m, opt_ma, jumlah_opt_ma, luas_spot_hopperburn,total_spot, intensitas_tambah, urlimage;
    String provinsi, kabupaten, kecamatan, desa, blok, luas_hamparan, luas_diamati, luas_panen, luas_persemaian, ph_tanah, komoditas, varietas, umur_tanaman, pola_tanam, petugas_pengamatan, tanggal_pengamatan, image;
    String jumlah_opt, session_id;
    String total_jumlah_anakan;
    int nomor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_rumpun);

        requestQueue = Volley.newRequestQueue(this);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Input 30 Rumpun");
        setSupportActionBar(toolbar);

        btn_add = (ImageView) findViewById(R.id.btn_add);
        progressDialog = new ProgressDialog(this);
        calendar = Calendar.getInstance();
        btnLapor = (Button) findViewById(R.id.btnLapor);

        SessionManager sessionManager = new SessionManager(InputRumpun.this, SessionManager.SESSION_ID);
        HashMap<String, String> rememberMeDetails = sessionManager.getIdHasilDetailsFromSession();
        id_hasil = rememberMeDetails.get(SessionManager.KEY_IDHASIL);
        session_id = rememberMeDetails.get(SessionManager.KEY_DATE);

        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String urlLaporan = Constants.ROOT_URL+"Hasil_Pengamatan?id_data="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                progressDialog.dismiss();

                                kabupaten = object.getString("kabupaten");
                                kecamatan = object.getString("kecamatan");
                                desa = object.getString("desa");
                                blok = object.getString("blok");
                                luas_hamparan = object.getString("luas_hamparan");
                                luas_diamati = object.getString("luas_diamati");
                                luas_panen = object.getString("luas_hasil_panen");
                                luas_persemaian = object.getString("luas_persemaian");
                                ph_tanah = object.getString("ph_tanah");
                                komoditas = object.getString("komoditas");
                                varietas = object.getString("varietas");
                                String dari_umur = object.getString("dari_umur");
                                String hingga_umur = object.getString("hingga_umur");
                                umur_tanaman = dari_umur + "-" +hingga_umur;
                                pola_tanam = object.getString("pola_tanam");
                                petugas_pengamatan = object.getString("petugas_pengamatan");
                                tanggal_pengamatan = object.getString("tanggal_pengamatan");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(InputRumpun.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(InputRumpun.this).add(stringRequest);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(InputRumpun.this, InputDetailHarian.class);
                int jumlah = listDetail.size() + 1;
                detailIntent.putExtra("jumlah", String.valueOf(jumlah));
                startActivity(detailIntent);
            }
        });

        buildRecyclerViewDataHarian();

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

    private void getData() {
        String url = Constants.ROOT_URL+"Detail_Pengamatan?id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/siap_opa/harian_keliling");
                            if (!docsFolder.exists()) {
                                docsFolder.mkdir();
                            }
                            String pdfname = "Harian Keliling "+tanggal_pengamatan+".pdf";
                            pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
                            OutputStream output = null;
                            try {
                                output = new FileOutputStream(pdfFile);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            final Document document = new Document(PageSize.A4);
                            final Font a = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.NORMAL, BaseColor.BLACK);
                            final PdfPTable data = new PdfPTable(new float[]{2, 2, 2});
                            data.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                            data.getDefaultCell().setFixedHeight(50);
                            data.setTotalWidth(PageSize.A4.getWidth());
                            data.setWidthPercentage(100);
                            data.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                            PdfPCell note1 = new PdfPCell(new Paragraph("Kabupaten: "+kabupaten, a));
                            PdfPCell note2 = new PdfPCell(new Paragraph("Desa: "+desa, a));
                            PdfPCell note3 = new PdfPCell(new Paragraph("Kecamatan: "+kecamatan, a));
                            PdfPCell note4 = new PdfPCell(new Paragraph("Blok: "+blok, a));
                            PdfPCell note5 = new PdfPCell(new Paragraph("Luas Pertanaman: "+luas_hamparan, a));
                            PdfPCell note6 = new PdfPCell(new Paragraph("Luas Diamati: "+luas_diamati, a));
                            PdfPCell note7 = new PdfPCell(new Paragraph("Luas Panen: "+luas_panen, a));
                            PdfPCell note8 = new PdfPCell(new Paragraph("Luas Persemaian: "+luas_persemaian, a));
                            PdfPCell note9 = new PdfPCell(new Paragraph("Komoditas: "+komoditas, a));
                            PdfPCell note10 = new PdfPCell(new Paragraph("Varietas: "+varietas, a));
                            PdfPCell note11 = new PdfPCell(new Paragraph("Umur Tanaman: "+umur_tanaman, a));
                            PdfPCell note12 = new PdfPCell(new Paragraph("Pola Tanam: "+pola_tanam, a));
                            note1.setBorder(Rectangle.NO_BORDER);
                            note2.setBorder(Rectangle.NO_BORDER);
                            note3.setBorder(Rectangle.NO_BORDER);
                            note4.setBorder(Rectangle.NO_BORDER);
                            note5.setBorder(Rectangle.NO_BORDER);
                            note6.setBorder(Rectangle.NO_BORDER);
                            note7.setBorder(Rectangle.NO_BORDER);
                            note8.setBorder(Rectangle.NO_BORDER);
                            note9.setBorder(Rectangle.NO_BORDER);
                            note10.setBorder(Rectangle.NO_BORDER);
                            note11.setBorder(Rectangle.NO_BORDER);
                            note12.setBorder(Rectangle.NO_BORDER);
                            data.addCell(note1);
                            data.addCell(note3);
                            data.addCell(note2);
                            data.addCell(note4);
                            data.addCell(note5);
                            data.addCell(note6);
                            data.addCell(note7);
                            data.addCell(note8);
                            data.addCell(note9);
                            data.addCell(note10);
                            data.addCell(note11);
                            data.addCell(note12);
                            final PdfPTable table = new PdfPTable(new float[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
                            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.getDefaultCell().setFixedHeight(50);
                            table.setTotalWidth(PageSize.A4.getWidth());
                            table.setWidthPercentage(100);
                            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                            PdfPCell No = new PdfPCell(new Paragraph("Rumpun", a));
                            PdfPCell Anakan = new PdfPCell(new Paragraph("Jumlah Anakan", a));
                            PdfPCell Mutlak = new PdfPCell(new Paragraph("OPT Mutlak", a));
                            PdfPCell Jumlah = new PdfPCell(new Paragraph("Skala/Rumpun Rusak", a));
                            PdfPCell Tidak = new PdfPCell(new Paragraph("OPT Tidak Mutlak", a));
                            PdfPCell Jumlah_tdk = new PdfPCell(new Paragraph("Skor/Skala", a));
                            PdfPCell Populasi = new PdfPCell(new Paragraph("OPT Populasi", a));
                            PdfPCell Jumlah_pop = new PdfPCell(new Paragraph("Jumlah OPT Populasi", a));
                            PdfPCell Populasi_m = new PdfPCell(new Paragraph("OPT Populasi (m2)", a));
                            PdfPCell Jumlah_pop_m = new PdfPCell(new Paragraph("Jumlah OPT Populasi (m2)", a));
                            PdfPCell MusuhAlami = new PdfPCell(new Paragraph("Musuh Alami", a));
                            PdfPCell jumMusuhAlami = new PdfPCell(new Paragraph("Jumlah Musuh Alami", a));
                            PdfPCell LuasSpot = new PdfPCell(new Paragraph("Luas Spot Hopperburn", a));
                            table.addCell(No);
                            table.addCell(Anakan);
                            table.addCell(Mutlak);
                            table.addCell(Jumlah);
                            table.addCell(Tidak);
                            table.addCell(Jumlah_tdk);
                            table.addCell(Populasi);
                            table.addCell(Jumlah_pop);
                            table.addCell(Populasi_m);
                            table.addCell(Jumlah_pop_m);
                            table.addCell(MusuhAlami);
                            table.addCell(jumMusuhAlami);
                            table.addCell(LuasSpot);
                            table.setHeaderRows(1);
                            PdfPCell[] cells = table.getRow(0).getCells();
                            for (int j = 0; j < cells.length; j++) {
                                cells[j].setBackgroundColor(BaseColor.GRAY);
                            }
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                nomor = i + 1;
                                jumlah_anakan = object.getString("jumlah_anakan");
                                opt_mutlak = object.getString("opt_mutlak");
                                jumlah_opt_mutlak = object.getString("jumlah_opt_mutlak");
                                opt_tdk_mutlak = object.getString("opt_tdk_mutlak");
                                jumlah_opt_tdk_mutlak = object.getString("jumlah_opt_tdk_mutlak");
                                opt_populasi = object.getString("opt_populasi");
                                jumlah_opt_populasi = object.getString("jumlah_opt_populasi");
                                opt_populasi_m = object.getString("opt_populasi_m");
                                jumlah_opt_populasi_m = object.getString("jumlah_populasi_m");
                                opt_ma = object.getString("opt_ma");
                                jumlah_opt_ma = object.getString("jumlah_opt_ma");
                                luas_spot_hopperburn = object.getString("luas_spot_hopperburn");

                                No = new PdfPCell(new Paragraph(String.valueOf(nomor), a));
                                Anakan = new PdfPCell(new Paragraph(jumlah_anakan, a));
                                Mutlak = new PdfPCell(new Paragraph(opt_mutlak, a));
                                Jumlah = new PdfPCell(new Paragraph(jumlah_opt_mutlak, a));
                                Tidak = new PdfPCell(new Paragraph(opt_tdk_mutlak, a));
                                Jumlah_tdk = new PdfPCell(new Paragraph(jumlah_opt_tdk_mutlak, a));
                                Populasi = new PdfPCell(new Paragraph(opt_populasi, a));
                                Jumlah_pop = new PdfPCell(new Paragraph(jumlah_opt_populasi, a));
                                Populasi_m = new PdfPCell(new Paragraph(opt_populasi_m, a));
                                Jumlah_pop_m = new PdfPCell(new Paragraph(jumlah_opt_populasi_m, a));
                                MusuhAlami = new PdfPCell(new Paragraph(opt_ma, a));
                                jumMusuhAlami = new PdfPCell(new Paragraph(jumlah_opt_ma, a));
                                LuasSpot = new PdfPCell(new Paragraph(luas_spot_hopperburn, a));

                                table.addCell(No);
                                table.addCell(Anakan);
                                table.addCell(Mutlak);
                                table.addCell(Jumlah);
                                table.addCell(Tidak);
                                table.addCell(Jumlah_tdk);
                                table.addCell(Populasi);
                                table.addCell(Jumlah_pop);
                                table.addCell(Populasi_m);
                                table.addCell(Jumlah_pop_m);
                                table.addCell(MusuhAlami);
                                table.addCell(jumMusuhAlami);
                                table.addCell(LuasSpot);

                            }

                            String url = Constants.ROOT_URL+"Hasil_Pengamatan?id_hsl="+id_hasil;
                            final OutputStream finalOutput = output;
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONArray array = new JSONArray(response);
                                                final PdfPTable table1 = new PdfPTable(new float[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
                                                table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                                                table1.getDefaultCell().setFixedHeight(50);
                                                table1.setTotalWidth(PageSize.A4.getWidth());
                                                table1.setWidthPercentage(100);
                                                table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                                                PdfPCell jumlah = new PdfPCell(new Paragraph("+", a));
//                                                jumlah.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(jumlah);

                                                PdfPCell jumlah_anakan = new PdfPCell(new Paragraph(total_jumlah_anakan, a));
//                                                jumlah_anakan.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(jumlah_anakan);

                                                StringBuilder total_opt_mut = new StringBuilder();
                                                StringBuilder total_jumlah_mut = new StringBuilder();
                                                StringBuilder total_rata_mut = new StringBuilder();
                                                StringBuilder inten_rata_mut = new StringBuilder();
                                                StringBuilder total_opt_tdk_mut = new StringBuilder();
                                                StringBuilder total_jumlah_tdk_mut = new StringBuilder();
                                                StringBuilder total_rata_tdk_mut = new StringBuilder();
                                                StringBuilder inten_rata_tdk_mut = new StringBuilder();
                                                StringBuilder total_opt_pop = new StringBuilder();
                                                StringBuilder total_jumlah_pop = new StringBuilder();
                                                StringBuilder total_rata_pop = new StringBuilder();
//                                                StringBuilder inten_rata_pop = new StringBuilder();
                                                StringBuilder total_opt_ma = new StringBuilder();
                                                StringBuilder total_jumlah_ma = new StringBuilder();
                                                StringBuilder total_rata_ma = new StringBuilder();
                                                StringBuilder total_opt_pop_m = new StringBuilder();
                                                StringBuilder total_jumlah_pop_m = new StringBuilder();
                                                StringBuilder total_rata_pop_m = new StringBuilder();

                                                for (int i = 0; i<array.length(); i++){
                                                    JSONObject object = array.getJSONObject(i);

                                                    jumlah_opt = object.getString("jumlah");
                                                    String jum_mut = "";
                                                    String rata_mut = "";
                                                    String opt_mut = "";
                                                    String jum_tdk_mut = "";
                                                    String rata_tdk_mut = "";
                                                    String opt_tdk_mut = "";
                                                    String jum_pop = "";
                                                    String rata_pop = "";
                                                    String opt_pop = "";
                                                    String jum_MA = "";
                                                    String rata_MA = "";
                                                    String opt_MA = "";
                                                    String intensitas_tambah_mut = "";
                                                    String intensitas_tambah_tdk = "";
//                                                    String intensitas_tambah_pop = "";
                                                    String jum_pop_m2 = "";
                                                    String rata_pop_m2 = "";
                                                    String opt_pop_m2 = "";
                                                    String status = object.getString("mutlak");
                                                    if (status.equals("mutlak")){
                                                        opt_mut = object.getString("opt")+ " | ";
                                                        jum_mut = object.getString("jumlah")+ " | ";
                                                        int mutlak = Integer.parseInt(object.getString("jumlah"))/30;
                                                        rata_mut = String.valueOf(mutlak)+ " | ";
                                                        float inten_mutlak = Float.parseFloat(object.getString("jumlah"))/Float.parseFloat(total_jumlah_anakan)*100;
                                                        intensitas_tambah_mut = String.format("%.2f", inten_mutlak)+ " | ";
                                                    }else if (status.equals("tidak mutlak")){
                                                        opt_tdk_mut = object.getString("opt")+ " | ";
                                                        jum_tdk_mut = object.getString("jumlah")+ " | ";
                                                        int mutlak = Integer.parseInt(object.getString("jumlah"))/30;
                                                        rata_tdk_mut = String.valueOf(mutlak)+ " | ";
                                                        float inten_tdk_mutlak = Float.parseFloat(object.getString("jumlah"))/270*100;
                                                        intensitas_tambah_tdk = String.format("%.2f", inten_tdk_mutlak)+ " | ";
                                                    }else if (status.equals("populasi")){
                                                        opt_pop = object.getString("opt")+ " | ";
                                                        jum_pop = object.getString("jumlah")+ " | ";
                                                        float mutlak = Float.parseFloat(object.getString("jumlah"))/30;
                                                        rata_pop = String.format("%.2f",mutlak)+ " | ";
                                                    }else if (status.equals("MA")){
                                                        opt_MA = object.getString("opt")+ " | ";
                                                        jum_MA = object.getString("jumlah")+ " | ";
                                                        float mutlak = Float.parseFloat(object.getString("jumlah"))/30;
                                                        rata_MA = String.format("%.2f",mutlak)+ " | ";
                                                    }else if (status.equals("populasi(m2)")){
                                                        opt_pop_m2 = object.getString("opt")+ " | ";
                                                        jum_pop_m2 = object.getString("jumlah")+ " | ";
                                                        float mutlak = Float.parseFloat(object.getString("jumlah"))/2;
                                                        rata_pop_m2 = String.format("%.2f",mutlak)+ " | ";
                                                    }

                                                    total_opt_mut.append(opt_mut);
                                                    total_jumlah_mut.append(jum_mut);
                                                    total_rata_mut.append(rata_mut);
                                                    total_opt_tdk_mut.append(opt_tdk_mut);
                                                    total_jumlah_tdk_mut.append(jum_tdk_mut);
                                                    total_rata_tdk_mut.append(rata_tdk_mut);
                                                    total_opt_pop.append(opt_pop);
                                                    total_jumlah_pop.append(jum_pop);
                                                    total_rata_pop.append(rata_pop);
                                                    total_opt_pop_m.append(opt_pop_m2);
                                                    total_jumlah_pop_m.append(jum_pop_m2);
                                                    total_rata_pop_m.append(rata_pop_m2);
                                                    total_opt_ma.append(opt_MA);
                                                    total_jumlah_ma.append(jum_MA);
                                                    total_rata_ma.append(rata_MA);
//                                                    inten_rata_pop.append(intensitas_tambah_pop);
                                                    inten_rata_mut.append(intensitas_tambah_mut);
                                                    inten_rata_tdk_mut.append(intensitas_tambah_tdk);
                                                }

                                                PdfPCell note_opt = new PdfPCell(new Paragraph(total_opt_mut.toString(), a));
//                                                note_opt.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_opt);

                                                PdfPCell note = new PdfPCell(new Paragraph(total_jumlah_mut.toString(), a));
//                                                note.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note);

                                                PdfPCell note_opt_tdk = new PdfPCell(new Paragraph(total_opt_tdk_mut.toString(), a));
//                                                note_opt_tdk.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_opt_tdk);

                                                PdfPCell note_jum_tdk = new PdfPCell(new Paragraph(total_jumlah_tdk_mut.toString(), a));
//                                                note_jum_tdk.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_jum_tdk);

                                                PdfPCell note_opt_pop = new PdfPCell(new Paragraph(total_opt_pop.toString(), a));
//                                                note_opt_pop.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_opt_pop);

                                                PdfPCell note_jum_pop = new PdfPCell(new Paragraph(total_jumlah_pop.toString(), a));
//                                                note_jum_pop.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_jum_pop);

                                                PdfPCell note_opt_pop_m = new PdfPCell(new Paragraph(total_opt_pop_m.toString(), a));
//                                                note_opt_pop.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_opt_pop_m);

                                                PdfPCell note_jum_pop_m = new PdfPCell(new Paragraph(total_jumlah_pop_m.toString(), a));
//                                                note_jum_pop.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_jum_pop_m);

                                                PdfPCell ma = new PdfPCell(new Paragraph(total_opt_ma.toString(), a));
//                                                ma.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(ma);

                                                PdfPCell jum_ma = new PdfPCell(new Paragraph(total_jumlah_ma.toString(), a));
//                                                jum_ma.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(jum_ma);

                                                PdfPCell luas = new PdfPCell(new Paragraph(total_spot, a));
//                                                luas.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(luas);

                                                jumlah = new PdfPCell(new Paragraph("Rata-Rata", a));
//                                                jumlah.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(jumlah);

                                                float hasil = Float.parseFloat(total_jumlah_anakan)/30;
                                                jumlah_anakan = new PdfPCell(new Paragraph(String.format("%.2f", hasil), a));
//                                                jumlah_anakan.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(jumlah_anakan);

                                                note_opt = new PdfPCell(new Paragraph(total_opt_mut.toString(), a));
//                                                note_opt.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_opt);

                                                note = new PdfPCell(new Paragraph("", a));
//                                                note.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note);

                                                note_opt_tdk = new PdfPCell(new Paragraph(total_opt_tdk_mut.toString(), a));
//                                                note_opt_tdk.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_opt_tdk);

                                                note_jum_tdk = new PdfPCell(new Paragraph("", a));
//                                                note_jum_tdk.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_jum_tdk);

                                                note_opt_pop = new PdfPCell(new Paragraph(total_opt_pop.toString(), a));
//                                                note_opt_pop.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_opt_pop);

                                                note_jum_pop = new PdfPCell(new Paragraph(String.valueOf(total_rata_pop), a));
//                                                note_jum_pop.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_jum_pop);

                                                note_opt_pop_m = new PdfPCell(new Paragraph(total_opt_pop_m.toString(), a));
//                                                note_opt_pop.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_opt_pop_m);

                                                note_jum_pop_m = new PdfPCell(new Paragraph(String.valueOf(total_rata_pop_m), a));
//                                                note_jum_pop.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_jum_pop_m);

                                                ma = new PdfPCell(new Paragraph(total_opt_ma.toString(), a));
//                                                ma.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(ma);

                                                jum_ma = new PdfPCell(new Paragraph(String.valueOf(total_rata_ma), a));
//                                                jum_ma.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(jum_ma);

                                                float rata_spot = Float.parseFloat(total_spot)/30;
                                                luas = new PdfPCell(new Paragraph(String.format("%.2f", rata_spot), a));
//                                                luas.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(luas);

                                               jumlah = new PdfPCell(new Paragraph("%", a));
//                                                jumlah.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(jumlah);

                                                jumlah_anakan = new PdfPCell(new Paragraph("", a));
//                                                jumlah_anakan.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(jumlah_anakan);

                                                note_opt = new PdfPCell(new Paragraph(total_opt_mut.toString(), a));
//                                                note_opt.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_opt);

                                                note = new PdfPCell(new Paragraph(String.valueOf(inten_rata_mut), a));
//                                                note.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note);

                                                note_opt_tdk = new PdfPCell(new Paragraph(total_opt_tdk_mut.toString(), a));
//                                                note_opt_tdk.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_opt_tdk);

                                                note_jum_tdk = new PdfPCell(new Paragraph(String.valueOf(inten_rata_tdk_mut), a));
//                                                note_jum_tdk.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_jum_tdk);

                                                note_opt_pop = new PdfPCell(new Paragraph(total_opt_pop.toString(), a));
//                                                note_opt_pop.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_opt_pop);

                                                note_jum_pop = new PdfPCell(new Paragraph("", a));
//                                                note_jum_pop.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_jum_pop);

                                                note_opt_pop_m = new PdfPCell(new Paragraph(total_opt_pop_m.toString(), a));
//                                                note_opt_pop.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_opt_pop_m);

                                                note_jum_pop_m = new PdfPCell(new Paragraph("", a));
//                                                note_jum_pop.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(note_jum_pop_m);

                                                ma = new PdfPCell(new Paragraph(total_opt_ma.toString(), a));
//                                                ma.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(ma);

                                                jum_ma = new PdfPCell(new Paragraph("", a));
//                                                jum_ma.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(jum_ma);

                                                luas = new PdfPCell(new Paragraph("", a));
//                                                luas.setBorder(Rectangle.NO_BORDER);
                                                table1.addCell(luas);


                                                String url = Constants.ROOT_URL+"Opt?id_hasil="+id_hasil;
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
                                                                        String opt = object.getString("opt");

                                                                        jumlah = new PdfPCell(new Paragraph(opt+": "+nama_opt, a));
//                                                                        jumlah.setHorizontalAlignment(Element.ALIGN_LEFT);
                                                                        jumlah.setBorder(Rectangle.NO_BORDER);
                                                                        table2.addCell(jumlah);
                                                                    }
                                                                    PdfWriter.getInstance(document, finalOutput);
                                                                    document.open();
                                                                    Font a = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.NORMAL, BaseColor.BLACK);
                                                                    Font c = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.BOLD, BaseColor.BLACK);
                                                                    Font b = new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, Font.BOLD, BaseColor.BLACK);
                                                                    Paragraph preface = new Paragraph("Hasil Pengamatan Harian Keliling", b);
                                                                    preface.setAlignment(Element.ALIGN_CENTER);
                                                                    SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                    Date date = dateFormat.parse(tanggal_pengamatan);

                                                                    SimpleDateFormat newDateFormat= new SimpleDateFormat("dd MMMM yyyy");
                                                                    String tanggal = newDateFormat.format(date);
                                                                    Paragraph subTitle = new Paragraph("Tanggal "+tanggal, c);
                                                                    subTitle.setAlignment(Element.ALIGN_CENTER);
                                                                    document.add(preface);
                                                                    document.add(subTitle);
                                                                    document.add(data);
                                                                    document.add(table);
                                                                    document.add(table1);
                                                                    Paragraph popt = new Paragraph("POPT-PHP", a);
                                                                    popt.setAlignment(Element.ALIGN_RIGHT);
                                                                    document.add(popt);
//                                                document.add(image);
                                                                    Paragraph petugas = new Paragraph(petugas_pengamatan, a);
                                                                    petugas.setAlignment(Element.ALIGN_RIGHT);
                                                                    document.add(petugas);
                                                                    document.add(table2);
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
                                                        Toast.makeText(InputRumpun.this, "No Data Found", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                                Volley.newRequestQueue(InputRumpun.this).add(stringRequest);


                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(InputRumpun.this, "No Data Found", Toast.LENGTH_LONG).show();
                                }
                            });
                            Volley.newRequestQueue(InputRumpun.this).add(stringRequest);


                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(InputRumpun.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(InputRumpun.this).add(stringRequest);

    }

    private void previewPdf() {
        Intent intent = new Intent(InputRumpun.this, PdfView.class);
        intent.putExtra("path", "Harian Keliling "+tanggal_pengamatan+".pdf");
        intent.putExtra("total", listDetail.size());
        intent.putExtra("id_hasil", id_hasil);
        startActivity(intent);
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
            getJumlahAnakan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length <= 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(InputRumpun.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getJumlahAnakan() {
        String url = Constants.ROOT_URL+"Detail_Pengamatan?id_pengamatan="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                total_jumlah_anakan = object.getString("jumlah_anakan");
                                total_spot = object.getString("luas_spot_hopperburn");

//                                getKeterangan();
                                getData();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(InputRumpun.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(InputRumpun.this).add(stringRequest);
    }


    private void buildRecyclerViewDataHarian() {
        recyclerView = (RecyclerView) findViewById(R.id.rvData);
        recyclerView.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(InputRumpun.this);
        recyclerView.setLayoutManager(layoutManager1);
        listDetail = new ArrayList<>();

        getDataHarian();
    }

    private void getDataHarian() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        userAlamat = Common.currentUser.getAlamat();
        String url = Constants.ROOT_URL+"Detail_Pengamatan?kecamatan="+userAlamat+"&date="+date+"&id_hasil="+id_hasil;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String jumlah_anakan = object.getString("jumlah_anakan");
                                String id = object.getString("id_detail");
                                int nomor_rumpun = i + 1;

                                DetailModel detailModel = new DetailModel();
                                detailModel.setId_detail(id);
                                detailModel.setJumlah_anakan(jumlah_anakan);
                                detailModel.setNomor_rumpun(String.valueOf(nomor_rumpun));
                                listDetail.add(detailModel);

                                if (listDetail.size() == 30){
                                    btn_add.setVisibility(View.GONE);
                                    Toast.makeText(InputRumpun.this, "Data Telah Mencapai Batas Maximum", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        adapter = new DetailAdapter(InputRumpun.this, listDetail);
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickCallback(new DetailAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(final DetailModel detailModel) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InputRumpun.this);
                                builder.setTitle("Delete Data");
                                builder.setMessage("Apakah Anda Yakin?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.setTitle("Delete Data");
                                        progressDialog.setMessage("Mohon Tunggu Sebentar");
                                        progressDialog.show();
                                        final String id_detail = detailModel.getId_detail();
                                        String url = Constants.ROOT_URL+"Populate_OPT";
                                        StringRequest request = new StringRequest(Request.Method.POST, url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (response.contains("berhasil")){
                                                            String url = Constants.ROOT_URL+"Detail_Pengamatan";
                                                            StringRequest request = new StringRequest(Request.Method.POST, url,
                                                                    new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            if (response.contains("berhasil")){
                                                                                progressDialog.dismiss();
                                                                                finish();
                                                                                overridePendingTransition(0, 0);
                                                                                startActivity(getIntent());
                                                                                overridePendingTransition(0, 0);
                                                                                Toast.makeText(InputRumpun.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    },
                                                                    new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                        }
                                                                    })
                                                            {
                                                                @Override
                                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                                    Map<String, String> params = new HashMap<>();
                                                                    params.put("id_detail", id_detail);
                                                                    return params;
                                                                }
                                                            };
                                                            RequestQueue requestQueue = Volley.newRequestQueue(InputRumpun.this);
                                                            requestQueue.add(request);
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                    }
                                                })
                                        {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("id_detail", id_detail);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(InputRumpun.this);
                                        requestQueue.add(request);

                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override public void onClick(DialogInterface dialog, int which) {
                                        // TODO: Do something, when user click on the positive button
                                        dialog.cancel();
                                    }
                                });
                                builder.create().show();

                            }
                        });


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(InputHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(InputRumpun.this).add(stringRequest);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SessionManager sessionManager = new SessionManager(InputRumpun.this, SessionManager.SESSION_ID);
        HashMap<String, String> rememberMeDetails = sessionManager.getIdHasilDetailsFromSession();
        id_hasil = rememberMeDetails.get(SessionManager.KEY_IDHASIL);
        if (id_hasil == null || id_hasil.equals("null")){
            finish();
        }else {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
    }
}