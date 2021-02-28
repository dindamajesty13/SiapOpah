package com.majesty.siapopa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MusimKemarau extends AppCompatActivity {
    EditText awal, akhir;
    Calendar myCalendar;
    ArrayList<String> jenis_desa = new ArrayList<>();
    ArrayAdapter<String> desaAdapter;
    Spinner spDesa, spOPT;
    ArrayList<String> jenis_opt = new ArrayList<>();
    ArrayAdapter<String> optAdapter;
    RequestQueue requestQueue;
    String userAlamat, tambah_ringan, tambah_sedang, tambah_berat, tambah_puso, tanggal_intensitas, musim_tanam;
    Button btn_cetak;
    ProgressDialog progressDialog;
    File pdfFile;
    Toolbar toolbar;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date, substring;
    String opt, kabupaten, kecamatan, desa, umur_tanaman, dateNow, luas_tambah, periode_pengamatan,id_hsl_stgh, luas_tanaman, komoditas, varietas, intensitas_tambah, luas_waspada, now;
    String blok, luas_panen,  pola_tanam, petugas_pengamatan, luas_persemaian, luas_tambah_serang, luas_sembuh, rekomendasi, luas_serang, id_hasil,  luas,  umur,  intensitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musim_kemarau);

//        TextView txtPeringatan = findViewById(R.id.peringatan);
//        txtPeringatan.setVisibility(View.GONE);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Laporan Musiman");
        setSupportActionBar(toolbar);

        myCalendar = Calendar.getInstance();
        calendar = Calendar.getInstance();
        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);
        spDesa = (Spinner) findViewById(R.id.spDesa);
        spOPT = (Spinner) findViewById(R.id.spOPT);
        btn_cetak = findViewById(R.id.btn_cetak);

        if (Common.currentUser.getId_usergroup().equals("2")){
            spDesa.setVisibility(View.GONE);
            awal= (EditText) findViewById(R.id.awal);
            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

            };

            awal.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(MusimKemarau.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            akhir= (EditText) findViewById(R.id.akhir);
            final DatePickerDialog.OnDateSetListener dateAkhir = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabelAkhir();
                }

            };
            akhir.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(MusimKemarau.this, dateAkhir, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });


            String url = Constants.ROOT_URL + "Populate_OPT";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("jenis_opt");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String opt = jsonObject.optString("opt");
                            jenis_opt.add(opt);
                            optAdapter = new ArrayAdapter<>(MusimKemarau.this, R.layout.my_spinner, jenis_opt);
                            optAdapter.setDropDownViewResource(R.layout.my_spinner);
                            spOPT.setAdapter(optAdapter);
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
        }else if (Common.currentUser.getId_usergroup().equals("4")){
            spDesa.setVisibility(View.GONE);
            spOPT.setVisibility(View.GONE);
            awal= (EditText) findViewById(R.id.awal);
            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

            };

            awal.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(MusimKemarau.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            akhir= (EditText) findViewById(R.id.akhir);
            final DatePickerDialog.OnDateSetListener dateAkhir = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabelAkhir();
                }

            };
            akhir.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(MusimKemarau.this, dateAkhir, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }else if (Common.currentUser.getId_usergroup().equals("3")){
            spDesa.setVisibility(View.GONE);
            awal= (EditText) findViewById(R.id.awal);
            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

            };

            awal.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(MusimKemarau.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            akhir= (EditText) findViewById(R.id.akhir);
            final DatePickerDialog.OnDateSetListener dateAkhir = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabelAkhir();
                }

            };
            akhir.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(MusimKemarau.this, dateAkhir, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });


            String url = Constants.ROOT_URL + "Populate_OPT";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("jenis_opt");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String opt = jsonObject.optString("opt");
                            jenis_opt.add(opt);
                            optAdapter = new ArrayAdapter<>(MusimKemarau.this, R.layout.my_spinner, jenis_opt);
                            optAdapter.setDropDownViewResource(R.layout.my_spinner);
                            spOPT.setAdapter(optAdapter);
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
        }else if (Common.currentUser.getId_usergroup().equals("1")){
            awal= (EditText) findViewById(R.id.awal);
            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

            };

            awal.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(MusimKemarau.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            akhir= (EditText) findViewById(R.id.akhir);
            final DatePickerDialog.OnDateSetListener dateAkhir = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabelAkhir();
                }

            };
            akhir.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(MusimKemarau.this, dateAkhir, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });


            String url = Constants.ROOT_URL + "Populate_OPT";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("jenis_opt");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String opt = jsonObject.optString("opt");
                            jenis_opt.add(opt);
                            optAdapter = new ArrayAdapter<>(MusimKemarau.this, R.layout.my_spinner, jenis_opt);
                            optAdapter.setDropDownViewResource(R.layout.my_spinner);
                            spOPT.setAdapter(optAdapter);
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

            userAlamat = Common.currentUser.getAlamat();
            String userKab = Common.currentUser.getKabupaten();
            String url1 = Constants.ROOT_URL + "Populate_Desa?kecamatan=" + userAlamat + "&kabupaten=" + userKab;

            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("lokasi");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String desa = jsonObject.optString("desa");
                            jenis_desa.add(desa);
                            desaAdapter = new ArrayAdapter<>(MusimKemarau.this, R.layout.my_spinner, jenis_desa);
                            desaAdapter.setDropDownViewResource(R.layout.my_spinner);
                            spDesa.setAdapter(desaAdapter);
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
            requestQueue.add(jsonObjectRequest1);
        }

        btn_cetak.setOnClickListener(new View.OnClickListener() {
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

    private void getDataProv() {
        String tanggal1 = awal.getText().toString();
        String tanggal2 = akhir.getText().toString();
        final String opt = spOPT.getSelectedItem().toString();
        String url = Constants.ROOT_URL+"Stgh_Bln?awal="+tanggal1+"&akhir="+tanggal2;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int nomor = i + 1;
                                if (object.getString("provinsi").equals("null")){
                                    progressDialog.dismiss();
                                    Toast.makeText(MusimKemarau.this, "No Data Found", Toast.LENGTH_LONG).show();
                                }else {
                                    String kabupaten = object.getString("kabupaten");
                                    String kecamatan = object.getString("kecamatan");
                                    String desa = object.getString("desa");
                                    String jumlah = object.getString("jumlah");
                                    String komoditas = object.getString("komoditas");
                                    String varietas = object.getString("varietas");
                                    String luas_tanaman = object.getString("luas_tanaman");
                                    String opt = object.getString("jenis_opt");
                                    String luas_sembuh = object.getString("luas_sembuh");
                                    String luas_tambah_serang = object.getString("luas_tambah");
                                    String luas_waspada = object.getString("luas_waspada");
                                    String luas_panen = object.getString("luas_panen");
                                    String kimia = object.getString("luas_kimia");
                                    String nabati = object.getString("luas_nabati");
                                    String eradikasi = object.getString("luas_eradikasi");
                                    String cara_lain = object.getString("luas_cara_lain");
                                    String jumlah_pengendalian = object.getString("jumlah_pengendalian");
                                    tanggal_intensitas = object.getString("tanggal_laporan");
                                    String frek_kimia = object.getString("frek_kimia");
                                    String frek_nabati = object.getString("frek_nabati");
                                    String sisa_ringan = object.getString("sisa_ringan");
                                    String sisa_sedang = object.getString("sisa_sedang");
                                    String sisa_berat = object.getString("sisa_berat");
                                    String sisa_puso = object.getString("sisa_puso");
                                    float intensitas_tambah = Float.parseFloat(object.getString("intensitas_tambah"))/Float.parseFloat(jumlah);
                                    if (intensitas_tambah <= 25  && luas_waspada.equals("0")){
                                        tambah_ringan = String.valueOf(luas_tambah_serang);
                                        tambah_sedang = "0";
                                        tambah_berat = "0";
                                        tambah_puso = "0";
                                    }else if (intensitas_tambah > 25 && intensitas_tambah <= 50){
                                        tambah_ringan = "0";
                                        tambah_sedang = String.valueOf(luas_tambah_serang);
                                        tambah_berat = "0";
                                        tambah_puso = "0";
                                    }else if (intensitas_tambah > 50 && intensitas_tambah <= 85){
                                        tambah_ringan = "0";
                                        tambah_sedang = "0";
                                        tambah_berat = String.valueOf(luas_tambah_serang);
                                        tambah_puso = "0";
                                    }else if (intensitas_tambah > 85){
                                        tambah_ringan = "0";
                                        tambah_sedang = "0";
                                        tambah_berat = "0";
                                        tambah_puso = String.valueOf(luas_tambah_serang);
                                    }
                                    String keadaan_ringan = object.getString("keadaan_ringan");
                                    String keadaan_sedang = object.getString("keadaan_sedang");
                                    String keadaan_berat = object.getString("keadaan_berat");
                                    String keadaan_puso = object.getString("keadaan_puso");

                                    File docsFolder = new File(Environment.getExternalStorageDirectory() + "/siap_opa");
                                    if (!docsFolder.exists()) {
                                        docsFolder.mkdir();
                                    }
                                    String pdfname = "Laporan Bulanan "+tanggal_intensitas+".pdf";
                                    pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
                                    OutputStream output = null;
                                    try {
                                        output = new FileOutputStream(pdfFile);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    final Document document = new Document(PageSize.A4.rotate());
                                    Font a = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.NORMAL, BaseColor.BLACK);
                                    final PdfPTable data = new PdfPTable(new float[]{2, 2});
                                    data.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                                    data.getDefaultCell().setFixedHeight(50);
                                    data.setTotalWidth(PageSize.A4.getWidth());
                                    data.setWidthPercentage(100);
                                    data.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    PdfPCell note1 = new PdfPCell(new Paragraph("Provinsi: Jawa Barat", a));
                                    PdfPCell note2 = new PdfPCell(new Paragraph("Kabupaten: "+kabupaten, a));
                                    note1.setBorder(Rectangle.NO_BORDER);
                                    note2.setBorder(Rectangle.NO_BORDER);
                                    data.addCell(note1);
                                    data.addCell(note2);
                                    final PdfPTable table = new PdfPTable(29);
                                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table.getDefaultCell().setFixedHeight(50);
                                    table.setTotalWidth(PageSize.A4.getWidth());
                                    table.setWidthPercentage(100);
                                    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    PdfPCell cell = new PdfPCell(new Phrase("No"));
                                    cell.setRowspan(2);
                                    table.addCell(cell);
                                    PdfPCell cell1 = new PdfPCell(new Phrase("Kecamatan"));
                                    cell1.setRowspan(2);
                                    table.addCell(cell1);
                                    PdfPCell cell2 = new PdfPCell(new Phrase("Desa"));
                                    cell2.setRowspan(2);
                                    table.addCell(cell2);
                                    PdfPCell cell3 = new PdfPCell(new Phrase("Komoditas"));
                                    cell3.setRowspan(2);
                                    table.addCell(cell3);
                                    PdfPCell cell4 = new PdfPCell(new Phrase("Varietas"));
                                    cell4.setRowspan(2);
                                    table.addCell(cell4);
                                    PdfPCell cell5 = new PdfPCell(new Phrase("Luas Hamparan"));
                                    cell5.setRowspan(2);
                                    table.addCell(cell5);
                                    PdfPCell cell6 = new PdfPCell(new Phrase("Jenis OPT"));
                                    cell6.setRowspan(2);
                                    table.addCell(cell6);
                                    PdfPCell cell7 = new PdfPCell(new Phrase("Luas Sisa Serangan"));
                                    cell7.setColspan(4);
                                    table.addCell(cell7);
                                    PdfPCell cell71 = new PdfPCell(new Phrase("Luas Terkendali"));
                                    cell71.setRowspan(2);
                                    table.addCell(cell71);
                                    PdfPCell cell72 = new PdfPCell(new Phrase("Luas Panen"));
                                    cell72.setRowspan(2);
                                    table.addCell(cell72);
                                    PdfPCell cell8 = new PdfPCell(new Phrase("Luas Tambah Serangan"));
                                    cell8.setColspan(4);
                                    table.addCell(cell8);
                                    PdfPCell cell9 = new PdfPCell(new Phrase("Luas Pengendalian"));
                                    cell9.setColspan(5);
                                    table.addCell(cell9);
                                    PdfPCell cell10 = new PdfPCell(new Phrase("Luas Keadaan Serangan"));
                                    cell10.setColspan(4);
                                    table.addCell(cell10);
                                    PdfPCell cell11 = new PdfPCell(new Phrase("Frekuensi Pengendalian"));
                                    cell11.setColspan(2);
                                    table.addCell(cell11);
                                    PdfPCell cell12 = new PdfPCell(new Phrase("Luas Waspada"));
                                    cell12.setRowspan(2);
                                    table.addCell(cell12);
                                    cell7 = new PdfPCell(new Phrase("R"));
                                    cell7.setColspan(1);
                                    table.addCell(cell7);
                                    cell7 = new PdfPCell(new Phrase("S"));
                                    cell7.setColspan(1);
                                    table.addCell(cell7);
                                    cell7 = new PdfPCell(new Phrase("B"));
                                    cell7.setColspan(1);
                                    table.addCell(cell7);
                                    cell7 = new PdfPCell(new Phrase("P"));
                                    cell7.setColspan(1);
                                    table.addCell(cell7);
                                    cell8 = new PdfPCell(new Phrase("R"));
                                    cell8.setColspan(1);
                                    table.addCell(cell8);
                                    cell8 = new PdfPCell(new Phrase("S"));
                                    cell8.setColspan(1);
                                    table.addCell(cell8);
                                    cell8 = new PdfPCell(new Phrase("B"));
                                    cell8.setColspan(1);
                                    table.addCell(cell8);
                                    cell8 = new PdfPCell(new Phrase("P"));
                                    cell8.setColspan(1);
                                    table.addCell(cell8);
                                    cell9 = new PdfPCell(new Phrase("Kimia"));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell9 = new PdfPCell(new Phrase("Nabati"));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell9 = new PdfPCell(new Phrase("Eradikasi"));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell9 = new PdfPCell(new Phrase("Cara Lain"));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell9 = new PdfPCell(new Phrase("Jumlah"));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell10 = new PdfPCell(new Phrase("R"));
                                    cell10.setColspan(1);
                                    table.addCell(cell10);
                                    cell10 = new PdfPCell(new Phrase("S"));
                                    cell10.setColspan(1);
                                    table.addCell(cell10);
                                    cell10 = new PdfPCell(new Phrase("B"));
                                    cell10.setColspan(1);
                                    table.addCell(cell10);
                                    cell10 = new PdfPCell(new Phrase("P"));
                                    cell10.setColspan(1);
                                    table.addCell(cell10);
                                    cell11 = new PdfPCell(new Phrase("Kimia"));
                                    cell11.setColspan(1);
                                    table.addCell(cell11);
                                    cell11 = new PdfPCell(new Phrase("Nabati"));
                                    cell11.setColspan(1);
                                    table.addCell(cell11);

                                    table.addCell(String.valueOf(nomor));
                                    table.addCell(kecamatan);
                                    table.addCell(desa);
                                    table.addCell(komoditas);
                                    table.addCell(varietas);
                                    table.addCell(String.valueOf(luas_tanaman));
                                    table.addCell(opt);
                                    table.addCell(sisa_ringan);
                                    table.addCell(sisa_sedang);
                                    table.addCell(sisa_berat);
                                    table.addCell(sisa_puso);
                                    table.addCell(String.valueOf(luas_sembuh));
                                    table.addCell(String.valueOf(luas_panen));
                                    table.addCell(tambah_ringan);
                                    table.addCell(tambah_sedang);
                                    table.addCell(tambah_berat);
                                    table.addCell(tambah_puso);
                                    table.addCell(String.valueOf(kimia));
                                    table.addCell(String.valueOf(nabati));
                                    table.addCell(String.valueOf(eradikasi));
                                    table.addCell(String.valueOf(cara_lain));
                                    table.addCell(String.valueOf(jumlah_pengendalian));
                                    table.addCell(keadaan_ringan);
                                    table.addCell(keadaan_sedang);
                                    table.addCell(keadaan_berat);
                                    table.addCell(keadaan_puso);
                                    table.addCell(String.valueOf(frek_kimia));
                                    table.addCell(String.valueOf(frek_nabati));
                                    table.addCell(String.valueOf(luas_waspada));

                                    PdfWriter.getInstance(document, output);
                                    document.open();
                                    document.add(data);
                                    document.add(new Paragraph("Laporan Bulanan", a));
                                    document.add(table);
                                    document.close();
                                    previewPdf();

                                }

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MusimKemarau.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(MusimKemarau.this).add(stringRequest);
    }

    private void getData() {
        String tanggal1 = awal.getText().toString();
        String tanggal2 = akhir.getText().toString();
        final String opt = spOPT.getSelectedItem().toString();
        String kab = Common.currentUser.getKabupaten();
        String kecamatan = Common.currentUser.getAlamat();
        String desa = spDesa.getSelectedItem().toString();
        String url = Constants.ROOT_URL+"Stgh_Bln?awal="+tanggal1+"&akhir="+tanggal2+"&opt="+opt+"&kabupaten="+kab+"&kecamatan="+kecamatan+"&desa="+desa;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int nomor = i + 1;
                                if (object.getString("provinsi").equals("null")){
                                    progressDialog.dismiss();
                                    Toast.makeText(MusimKemarau.this, "No Data Found", Toast.LENGTH_LONG).show();
                                }else {
                                    String kecamatan = object.getString("kecamatan");
                                    String kabupaten = object.getString("kabupaten");
                                    String desa = object.getString("desa");
                                    periode_pengamatan = object.getString("periode");
                                    luas_tanaman = object.getString("luas_tanaman");
                                    umur_tanaman = object.getString("umur_tanaman");
                                    String jenis_opt = object.getString("jenis_opt");
                                    String jumlah_sisa = object.getString("jumlah_sisa");
                                    String jumlah_tambah = object.getString("jumlah_tambah");
                                    String jumlah_keadaan = object.getString("jumlah_keadaan");
                                    luas_sembuh = object.getString("luas_terkendali");
                                    luas_tambah_serang = object.getString("luas_tambah");
                                    String luas_waspada = object.getString("luas_waspada");
                                    luas_panen = object.getString("luas_panen");
                                    String kimia = object.getString("luas_kimia");
                                    String nabati = object.getString("luas_nabati");
                                    String eradikasi = object.getString("luas_eradikasi");
                                    String cara_lain = object.getString("luas_cara_lain");
                                    String jumlah_pengendalian = object.getString("jumlah_pengendalian");
                                    tanggal_intensitas = object.getString("tanggal_laporan");
                                    String frek_kimia = object.getString("frek_kimia");
                                    String frek_nabati = object.getString("frek_nabati");
                                    String sisa_ringan = object.getString("sisa_ringan");
                                    String sisa_sedang = object.getString("sisa_sedang");
                                    String sisa_berat = object.getString("sisa_berat");
                                    String sisa_puso = object.getString("sisa_puso");
//                                    String intensitas_tambah = object.getString("intensitas_tambah");
                                    String tambah_ringan = object.getString("tambah_ringan");
                                    String tambah_sedang = object.getString("tambah_sedang");
                                    String tambah_berat = object.getString("tambah_berat");
                                    String tambah_puso = object.getString("tambah_puso");
                                    String keadaan_ringan = object.getString("keadaan_ringan");
                                    String keadaan_sedang = object.getString("keadaan_sedang");
                                    String keadaan_berat = object.getString("keadaan_berat");
                                    String keadaan_puso = object.getString("keadaan_puso");
                                    petugas_pengamatan = object.getString("petugas");

                                    File docsFolder = new File(Environment.getExternalStorageDirectory() + "/siap_opa");
                                    if (!docsFolder.exists()) {
                                        docsFolder.mkdir();
                                    }
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    dateNow = dateFormat.format(calendar.getTime());

                                    String pdfname = "Laporan Musiman "+dateNow+".pdf";
                                    pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
                                    OutputStream output = null;
                                    try {
                                        output = new FileOutputStream(pdfFile);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    final Document document = new Document(PageSize.A4.rotate());
                                    Font a = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.NORMAL, BaseColor.BLACK);
                                    final PdfPTable data = new PdfPTable(new float[]{2, 2});
                                    data.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                                    data.getDefaultCell().setFixedHeight(50);
                                    data.setTotalWidth(PageSize.A4.getWidth());
                                    data.setWidthPercentage(100);
                                    data.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    PdfPCell note1 = new PdfPCell(new Paragraph("Provinsi: Jawa Barat", a));
                                    PdfPCell note2 = new PdfPCell(new Paragraph("Kabupaten: "+kabupaten, a));
                                    PdfPCell note3 = new PdfPCell(new Paragraph("Kecamatan: "+kecamatan, a));
                                    PdfPCell note4 = new PdfPCell(new Paragraph("Desa: "+desa, a));
                                    note1.setBorder(Rectangle.NO_BORDER);
                                    note2.setBorder(Rectangle.NO_BORDER);
                                    note1.setSpaceCharRatio(4f);
                                    note2.setSpaceCharRatio(4f);
                                    note3.setBorder(Rectangle.NO_BORDER);
                                    note4.setBorder(Rectangle.NO_BORDER);
                                    note3.setSpaceCharRatio(4f);
                                    note4.setSpaceCharRatio(4f);
                                    data.addCell(note1);
                                    data.addCell(note2);
                                    data.addCell(note3);
                                    data.addCell(note4);
                                    final PdfPTable table = new PdfPTable(30);
                                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table.getDefaultCell().setFixedHeight(50);
                                    table.setTotalWidth(PageSize.A4.getWidth());
                                    table.setWidthPercentage(100);
                                    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    PdfPCell cell = new PdfPCell(new Phrase("No", a));
                                    cell.setRowspan(2);
                                    table.addCell(cell);
                                    PdfPCell cell3 = new PdfPCell(new Phrase("Kom", a));
                                    cell3.setRowspan(2);
                                    table.addCell(cell3);
                                    PdfPCell cell4 = new PdfPCell(new Phrase("Var", a));
                                    cell4.setRowspan(2);
                                    table.addCell(cell4);
                                    PdfPCell cell5 = new PdfPCell(new Phrase("LH (Ha)", a));
                                    cell5.setRowspan(2);
                                    table.addCell(cell5);
                                    PdfPCell cell6 = new PdfPCell(new Phrase("OPT", a));
                                    cell6.setRowspan(2);
                                    table.addCell(cell6);
                                    PdfPCell cell7 = new PdfPCell(new Phrase("Luas Sisa Serangan (Ha)", a));
                                    cell7.setColspan(5);
                                    table.addCell(cell7);
                                    PdfPCell cell71 = new PdfPCell(new Phrase("LS (Ha)", a));
                                    cell71.setRowspan(2);
                                    table.addCell(cell71);
                                    PdfPCell cell72 = new PdfPCell(new Phrase("LP (Ha)", a));
                                    cell72.setRowspan(2);
                                    table.addCell(cell72);
                                    PdfPCell cell8 = new PdfPCell(new Phrase("Luas Tambah Serangan (Ha)", a));
                                    cell8.setColspan(5);
                                    table.addCell(cell8);
                                    PdfPCell cell9 = new PdfPCell(new Phrase("Luas Pengendalian (Ha)", a));
                                    cell9.setColspan(5);
                                    table.addCell(cell9);
                                    PdfPCell cell10 = new PdfPCell(new Phrase("Luas Keadaan Serangan (Ha)", a));
                                    cell10.setColspan(5);
                                    table.addCell(cell10);
                                    PdfPCell cell11 = new PdfPCell(new Phrase("Frek (kali)", a));
                                    cell11.setColspan(2);
                                    table.addCell(cell11);
                                    PdfPCell cell12 = new PdfPCell(new Phrase("LW (Ha)", a));
                                    cell12.setRowspan(2);
                                    table.addCell(cell12);
                                    cell7 = new PdfPCell(new Phrase("R", a));
                                    cell7.setColspan(1);
                                    table.addCell(cell7);
                                    cell7 = new PdfPCell(new Phrase("S", a));
                                    cell7.setColspan(1);
                                    table.addCell(cell7);
                                    cell7 = new PdfPCell(new Phrase("B", a));
                                    cell7.setColspan(1);
                                    table.addCell(cell7);
                                    cell7 = new PdfPCell(new Phrase("P", a));
                                    cell7.setColspan(1);
                                    table.addCell(cell7);
                                    cell7 = new PdfPCell(new Phrase("J", a));
                                    cell7.setColspan(1);
                                    table.addCell(cell7);
                                    cell8 = new PdfPCell(new Phrase("R", a));
                                    cell8.setColspan(1);
                                    table.addCell(cell8);
                                    cell8 = new PdfPCell(new Phrase("S", a));
                                    cell8.setColspan(1);
                                    table.addCell(cell8);
                                    cell8 = new PdfPCell(new Phrase("B", a));
                                    cell8.setColspan(1);
                                    table.addCell(cell8);
                                    cell8 = new PdfPCell(new Phrase("P", a));
                                    cell8.setColspan(1);
                                    table.addCell(cell8);
                                    cell8 = new PdfPCell(new Phrase("J", a));
                                    cell8.setColspan(1);
                                    table.addCell(cell8);
                                    cell9 = new PdfPCell(new Phrase("K", a));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell9 = new PdfPCell(new Phrase("N", a));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell9 = new PdfPCell(new Phrase("E", a));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell9 = new PdfPCell(new Phrase("CL", a));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell9 = new PdfPCell(new Phrase("J", a));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell10 = new PdfPCell(new Phrase("R", a));
                                    cell10.setColspan(1);
                                    table.addCell(cell10);
                                    cell10 = new PdfPCell(new Phrase("S", a));
                                    cell10.setColspan(1);
                                    table.addCell(cell10);
                                    cell10 = new PdfPCell(new Phrase("B", a));
                                    cell10.setColspan(1);
                                    table.addCell(cell10);
                                    cell10 = new PdfPCell(new Phrase("P", a));
                                    cell10.setColspan(1);
                                    table.addCell(cell10);
                                    cell10 = new PdfPCell(new Phrase("J", a));
                                    cell10.setColspan(1);
                                    table.addCell(cell10);
                                    cell11 = new PdfPCell(new Phrase("K", a));
                                    cell11.setColspan(1);
                                    table.addCell(cell11);
                                    cell11 = new PdfPCell(new Phrase("N", a));
                                    cell11.setColspan(1);
                                    table.addCell(cell11);

                                    cell = new PdfPCell(new Phrase(String.valueOf(nomor)));
                                    table.addCell(cell);

                                    cell3 = new PdfPCell(new Phrase(komoditas));
                                    table.addCell(cell3);

                                    cell4 = new PdfPCell(new Phrase(varietas));
                                    table.addCell(cell4);

                                    cell5 = new PdfPCell(new Phrase(luas_tanaman));
                                    table.addCell(cell5);

                                    cell6 = new PdfPCell(new Phrase(jenis_opt));
                                    table.addCell(cell6);

                                    cell7 = new PdfPCell(new Phrase(sisa_ringan));
                                    table.addCell(cell7);

                                    cell7 = new PdfPCell(new Phrase(sisa_sedang));
                                    table.addCell(cell7);

                                    cell7 = new PdfPCell(new Phrase(sisa_berat));
                                    table.addCell(cell7);

                                    cell7 = new PdfPCell(new Phrase(sisa_puso));
                                    table.addCell(cell7);

                                    cell7 = new PdfPCell(new Phrase(jumlah_sisa));
                                    table.addCell(cell7);

                                    cell71 = new PdfPCell(new Phrase(luas_sembuh));
                                    table.addCell(cell71);

                                    cell72 = new PdfPCell(new Phrase(luas_panen));
                                    table.addCell(cell72);

                                    cell8 = new PdfPCell(new Phrase(tambah_ringan));
                                    table.addCell(cell8);

                                    cell8 = new PdfPCell(new Phrase(tambah_sedang));
                                    table.addCell(cell8);

                                    cell8 = new PdfPCell(new Phrase(tambah_berat));
                                    table.addCell(cell8);

                                    cell8 = new PdfPCell(new Phrase(tambah_puso));
                                    table.addCell(cell8);

                                    cell8 = new PdfPCell(new Phrase(jumlah_tambah));
                                    table.addCell(cell8);

                                    cell9 = new PdfPCell(new Phrase(kimia));
                                    table.addCell(cell9);

                                    cell9 = new PdfPCell(new Phrase(nabati));
                                    table.addCell(cell9);

                                    cell9 = new PdfPCell(new Phrase(eradikasi));
                                    table.addCell(cell9);

                                    cell9 = new PdfPCell(new Phrase(cara_lain));
                                    table.addCell(cell9);

                                    cell9 = new PdfPCell(new Phrase(jumlah_pengendalian));
                                    table.addCell(cell9);

                                    cell10 = new PdfPCell(new Phrase(keadaan_ringan));
                                    table.addCell(cell10);

                                    cell10 = new PdfPCell(new Phrase(keadaan_sedang));
                                    table.addCell(cell10);

                                    cell10 = new PdfPCell(new Phrase(keadaan_berat));
                                    table.addCell(cell10);

                                    cell10 = new PdfPCell(new Phrase(keadaan_puso));
                                    table.addCell(cell10);

                                    cell10 = new PdfPCell(new Phrase(jumlah_keadaan));
                                    table.addCell(cell10);

                                    cell11 = new PdfPCell(new Phrase(frek_kimia));
                                    table.addCell(cell11);

                                    cell11 = new PdfPCell(new Phrase(frek_nabati));
                                    table.addCell(cell11);

                                    cell12 = new PdfPCell(new Phrase(luas_waspada));
                                    table.addCell(cell12);

                                    PdfWriter.getInstance(document, output);
                                    document.open();
                                    dateFormat = new SimpleDateFormat("dd");
                                    String hari = dateFormat.format(calendar.getTime());
                                    SimpleDateFormat month = new SimpleDateFormat("MMMM");
                                    String bulan = month.format(calendar.getTime());

                                    SimpleDateFormat years = new SimpleDateFormat("yyyy");
                                    String tahun = years.format(calendar.getTime());

                                    Font b = new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, Font.BOLD, BaseColor.BLACK);
                                    Font c = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.BOLD, BaseColor.BLACK);
                                    Paragraph title = new Paragraph("Laporan Musiman", b);
                                    title.setAlignment(Element.ALIGN_CENTER);
//                                    Paragraph subTitle;
//                                    if (periode_pengamatan.equals("Periode 1-15")) {
//                                        subTitle = new Paragraph("Periode 1-"+hari+" "+ bulan +" "+ tahun, c);
//                                        subTitle.setAlignment(Element.ALIGN_CENTER);
//                                    } else {
//                                        subTitle = new Paragraph("Periode 16-"+hari+" "+bulan +" "+ tahun, c);
//                                        subTitle.setAlignment(Element.ALIGN_CENTER);
//                                    }
                                    document.add(title);
//                                    document.add(subTitle);
                                    document.add(data);
                                    document.add(table);
                                    document.add(new Paragraph("POPT-PHP", a));
//                                                document.add(image);
                                    document.add(new Paragraph(petugas_pengamatan, a));
                                    document.close();
                                    previewPdf();

                                }

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MusimKemarau.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(MusimKemarau.this).add(stringRequest);
    }

    private void updateLabelAkhir() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        akhir.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        awal.setText(sdf.format(myCalendar.getTime()));
    }

    private void previewPdf() {
        progressDialog.dismiss();
        Intent intent = new Intent(MusimKemarau.this, PdfView.class);
        intent.putExtra("musiman", "Laporan Musiman "+dateNow+".pdf");
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length <= 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MusimKemarau.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
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
        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.dismiss();
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
            if (Common.currentUser.getId_usergroup().equals("1")){
                getData();
            }else if (Common.currentUser.getId_usergroup().equals("2")){
                getDataKab();
            }else if (Common.currentUser.getId_usergroup().equals("3")){
                getDataKab();
            }else if (Common.currentUser.getId_usergroup().equals("4")){
                getDataProv();
            }
        }
    }

    private void getDataKab() {
        String tanggal1 = awal.getText().toString();
        String tanggal2 = akhir.getText().toString();
        final String opt = spOPT.getSelectedItem().toString();
        String kab = Common.currentUser.getKabupaten();
        String url = Constants.ROOT_URL+"Stgh_Bln?awal="+tanggal1+"&akhir="+tanggal2+"&opt="+opt+"&kabupaten="+kab;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int nomor = i + 1;
                                if (object.getString("provinsi").equals("null")){
                                    progressDialog.dismiss();
                                    Toast.makeText(MusimKemarau.this, "No Data Found", Toast.LENGTH_LONG).show();
                                }else {
                                    String kabupaten = object.getString("kabupaten");
                                    String kecamatan = object.getString("kecamatan");
                                    String desa = object.getString("desa");
                                    String jumlah = object.getString("jumlah");
                                    String komoditas = object.getString("komoditas");
                                    String varietas = object.getString("varietas");
                                    String luas_tanaman = object.getString("luas_tanaman");
                                    String opt = object.getString("jenis_opt");
                                    String luas_sembuh = object.getString("luas_sembuh");
                                    String luas_tambah_serang = object.getString("luas_tambah");
                                    String luas_waspada = object.getString("luas_waspada");
                                    String luas_panen = object.getString("luas_panen");
                                    String kimia = object.getString("luas_kimia");
                                    String nabati = object.getString("luas_nabati");
                                    String eradikasi = object.getString("luas_eradikasi");
                                    String cara_lain = object.getString("luas_cara_lain");
                                    String jumlah_pengendalian = object.getString("jumlah_pengendalian");
                                    tanggal_intensitas = object.getString("tanggal_laporan");
                                    String frek_kimia = object.getString("frek_kimia");
                                    String frek_nabati = object.getString("frek_nabati");
                                    String sisa_ringan = object.getString("sisa_ringan");
                                    String sisa_sedang = object.getString("sisa_sedang");
                                    String sisa_berat = object.getString("sisa_berat");
                                    String sisa_puso = object.getString("sisa_puso");
                                    String intensitas_tambah = object.getString("intensitas_tambah");
                                    if (Float.parseFloat(intensitas_tambah) <= 25  && luas_waspada.equals("0")){
                                        tambah_ringan = String.valueOf(luas_tambah_serang);
                                        tambah_sedang = "0";
                                        tambah_berat = "0";
                                        tambah_puso = "0";
                                    }else if (Float.parseFloat(intensitas_tambah) > 25 && Float.parseFloat(intensitas_tambah) <= 50){
                                        tambah_ringan = "0";
                                        tambah_sedang = String.valueOf(luas_tambah_serang);
                                        tambah_berat = "0";
                                        tambah_puso = "0";
                                    }else if (Float.parseFloat(intensitas_tambah) > 50 && Float.parseFloat(intensitas_tambah) <= 85){
                                        tambah_ringan = "0";
                                        tambah_sedang = "0";
                                        tambah_berat = String.valueOf(luas_tambah_serang);
                                        tambah_puso = "0";
                                    }else if (Float.parseFloat(intensitas_tambah) > 85){
                                        tambah_ringan = "0";
                                        tambah_sedang = "0";
                                        tambah_berat = "0";
                                        tambah_puso = String.valueOf(luas_tambah_serang);
                                    }
                                    String keadaan_ringan = object.getString("keadaan_ringan");
                                    String keadaan_sedang = object.getString("keadaan_sedang");
                                    String keadaan_berat = object.getString("keadaan_berat");
                                    String keadaan_puso = object.getString("keadaan_puso");

                                    File docsFolder = new File(Environment.getExternalStorageDirectory() + "/siap_opa");
                                    if (!docsFolder.exists()) {
                                        docsFolder.mkdir();
                                    }
                                    String pdfname = "Laporan Bulanan "+tanggal_intensitas+".pdf";
                                    pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
                                    OutputStream output = null;
                                    try {
                                        output = new FileOutputStream(pdfFile);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    final Document document = new Document(PageSize.A4.rotate());
                                    Font a = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.NORMAL, BaseColor.BLACK);
                                    final PdfPTable data = new PdfPTable(new float[]{2, 2});
                                    data.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                                    data.getDefaultCell().setFixedHeight(50);
                                    data.setTotalWidth(PageSize.A4.getWidth());
                                    data.setWidthPercentage(100);
                                    data.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    PdfPCell note1 = new PdfPCell(new Paragraph("Provinsi: Jawa Barat", a));
                                    PdfPCell note2 = new PdfPCell(new Paragraph("Kabupaten: "+kabupaten, a));
                                    note1.setBorder(Rectangle.NO_BORDER);
                                    note2.setBorder(Rectangle.NO_BORDER);
                                    data.addCell(note1);
                                    data.addCell(note2);
                                    final PdfPTable table = new PdfPTable(29);
                                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table.getDefaultCell().setFixedHeight(50);
                                    table.setTotalWidth(PageSize.A4.getWidth());
                                    table.setWidthPercentage(100);
                                    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    PdfPCell cell = new PdfPCell(new Phrase("No"));
                                    cell.setRowspan(2);
                                    table.addCell(cell);
                                    PdfPCell cell1 = new PdfPCell(new Phrase("Kecamatan"));
                                    cell1.setRowspan(2);
                                    table.addCell(cell1);
                                    PdfPCell cell2 = new PdfPCell(new Phrase("Desa"));
                                    cell2.setRowspan(2);
                                    table.addCell(cell2);
                                    PdfPCell cell3 = new PdfPCell(new Phrase("Komoditas"));
                                    cell3.setRowspan(2);
                                    table.addCell(cell3);
                                    PdfPCell cell4 = new PdfPCell(new Phrase("Varietas"));
                                    cell4.setRowspan(2);
                                    table.addCell(cell4);
                                    PdfPCell cell5 = new PdfPCell(new Phrase("Luas Hamparan"));
                                    cell5.setRowspan(2);
                                    table.addCell(cell5);
                                    PdfPCell cell6 = new PdfPCell(new Phrase("Jenis OPT"));
                                    cell6.setRowspan(2);
                                    table.addCell(cell6);
                                    PdfPCell cell7 = new PdfPCell(new Phrase("Luas Sisa Serangan"));
                                    cell7.setColspan(4);
                                    table.addCell(cell7);
                                    PdfPCell cell71 = new PdfPCell(new Phrase("Luas Terkendali"));
                                    cell71.setRowspan(2);
                                    table.addCell(cell71);
                                    PdfPCell cell72 = new PdfPCell(new Phrase("Luas Panen"));
                                    cell72.setRowspan(2);
                                    table.addCell(cell72);
                                    PdfPCell cell8 = new PdfPCell(new Phrase("Luas Tambah Serangan"));
                                    cell8.setColspan(4);
                                    table.addCell(cell8);
                                    PdfPCell cell9 = new PdfPCell(new Phrase("Luas Pengendalian"));
                                    cell9.setColspan(5);
                                    table.addCell(cell9);
                                    PdfPCell cell10 = new PdfPCell(new Phrase("Luas Keadaan Serangan"));
                                    cell10.setColspan(4);
                                    table.addCell(cell10);
                                    PdfPCell cell11 = new PdfPCell(new Phrase("Frekuensi Pengendalian"));
                                    cell11.setColspan(2);
                                    table.addCell(cell11);
                                    PdfPCell cell12 = new PdfPCell(new Phrase("Luas Waspada"));
                                    cell12.setRowspan(2);
                                    table.addCell(cell12);
                                    cell7 = new PdfPCell(new Phrase("R"));
                                    cell7.setColspan(1);
                                    table.addCell(cell7);
                                    cell7 = new PdfPCell(new Phrase("S"));
                                    cell7.setColspan(1);
                                    table.addCell(cell7);
                                    cell7 = new PdfPCell(new Phrase("B"));
                                    cell7.setColspan(1);
                                    table.addCell(cell7);
                                    cell7 = new PdfPCell(new Phrase("P"));
                                    cell7.setColspan(1);
                                    table.addCell(cell7);
                                    cell8 = new PdfPCell(new Phrase("R"));
                                    cell8.setColspan(1);
                                    table.addCell(cell8);
                                    cell8 = new PdfPCell(new Phrase("S"));
                                    cell8.setColspan(1);
                                    table.addCell(cell8);
                                    cell8 = new PdfPCell(new Phrase("B"));
                                    cell8.setColspan(1);
                                    table.addCell(cell8);
                                    cell8 = new PdfPCell(new Phrase("P"));
                                    cell8.setColspan(1);
                                    table.addCell(cell8);
                                    cell9 = new PdfPCell(new Phrase("Kimia"));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell9 = new PdfPCell(new Phrase("Nabati"));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell9 = new PdfPCell(new Phrase("Eradikasi"));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell9 = new PdfPCell(new Phrase("Cara Lain"));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell9 = new PdfPCell(new Phrase("Jumlah"));
                                    cell9.setColspan(1);
                                    table.addCell(cell9);
                                    cell10 = new PdfPCell(new Phrase("R"));
                                    cell10.setColspan(1);
                                    table.addCell(cell10);
                                    cell10 = new PdfPCell(new Phrase("S"));
                                    cell10.setColspan(1);
                                    table.addCell(cell10);
                                    cell10 = new PdfPCell(new Phrase("B"));
                                    cell10.setColspan(1);
                                    table.addCell(cell10);
                                    cell10 = new PdfPCell(new Phrase("P"));
                                    cell10.setColspan(1);
                                    table.addCell(cell10);
                                    cell11 = new PdfPCell(new Phrase("Kimia"));
                                    cell11.setColspan(1);
                                    table.addCell(cell11);
                                    cell11 = new PdfPCell(new Phrase("Nabati"));
                                    cell11.setColspan(1);
                                    table.addCell(cell11);

                                    table.addCell(String.valueOf(nomor));
                                    table.addCell(kecamatan);
                                    table.addCell(desa);
                                    table.addCell(komoditas);
                                    table.addCell(varietas);
                                    table.addCell(String.valueOf(luas_tanaman));
                                    table.addCell(opt);
                                    table.addCell(sisa_ringan);
                                    table.addCell(sisa_sedang);
                                    table.addCell(sisa_berat);
                                    table.addCell(sisa_puso);
                                    table.addCell(String.valueOf(luas_sembuh));
                                    table.addCell(String.valueOf(luas_panen));
                                    table.addCell(tambah_ringan);
                                    table.addCell(tambah_sedang);
                                    table.addCell(tambah_berat);
                                    table.addCell(tambah_puso);
                                    table.addCell(String.valueOf(kimia));
                                    table.addCell(String.valueOf(nabati));
                                    table.addCell(String.valueOf(eradikasi));
                                    table.addCell(String.valueOf(cara_lain));
                                    table.addCell(String.valueOf(jumlah_pengendalian));
                                    table.addCell(keadaan_ringan);
                                    table.addCell(keadaan_sedang);
                                    table.addCell(keadaan_berat);
                                    table.addCell(keadaan_puso);
                                    table.addCell(String.valueOf(frek_kimia));
                                    table.addCell(String.valueOf(frek_nabati));
                                    table.addCell(String.valueOf(luas_waspada));

                                    PdfWriter.getInstance(document, output);
                                    document.open();
                                    document.add(data);
                                    document.add(new Paragraph("Laporan Bulanan", a));
                                    document.add(table);
                                    document.close();
                                    previewPdf();

                                }

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MusimKemarau.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(MusimKemarau.this).add(stringRequest);
    }
}