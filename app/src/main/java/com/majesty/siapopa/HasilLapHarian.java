package com.majesty.siapopa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HasilLapHarian extends AppCompatActivity {
    TextView txtKabupaten, txtKecamatan, txtDesa,
            txtTanggal, txtBlok, txtLuasTanaman, txtUmurTanaman, txtOPT,
            txtIntensitas, txtKomoditas, txtVarietas, txtLuasSerang, txtLuasWaspada, txtRekomendasi;
    String id_intensitas, petugas_pengamatan, rekomendasi, tanggal_intensitas, kabupaten, kecamatan, desa, blok, intensitas_tambah, luas_hamparan, luas_diamati, komoditas, luas_tanaman, umur_tanaman, jenis_opt, luas_sisa_serang, luas_waspada, varietas;
    Toolbar toolbar;
    Button btn_cetak, btnSimpan;
    ProgressDialog progressDialog;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date, surat_kab, surat_satpel, surat_provinsi, surat_popt;
    ImageView img_bukti;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    TextView txtPeringatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_lap_harian);

        txtKomoditas = (TextView) findViewById(R.id.txtKomoditas);
        txtPeringatan = findViewById(R.id.peringatan);
        txtPeringatan.setVisibility(View.GONE);
        txtRekomendasi = findViewById(R.id.txtRekomendasi);
        txtVarietas = (TextView) findViewById(R.id.txtVarietas);
        txtIntensitas = (TextView) findViewById(R.id.txtIntensitas);
        txtBlok = (TextView) findViewById(R.id.txtBlok);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Laporan Harian");
        setSupportActionBar(toolbar);
        txtLuasWaspada = (TextView) findViewById(R.id.txtLuasWaspada);
        txtKabupaten = (TextView) findViewById(R.id.txtKabupaten);
        txtKecamatan = (TextView) findViewById(R.id.txtKecamatan);
        txtDesa = (TextView) findViewById(R.id.txtDesa);
        txtTanggal = (TextView) findViewById(R.id.txtTanggal);
        txtLuasTanaman = (TextView) findViewById(R.id.txtLuasTanaman);
        txtUmurTanaman = (TextView) findViewById(R.id.txtUmurTanaman);
        txtOPT = (TextView) findViewById(R.id.txtOPT);
        txtLuasSerang = (TextView) findViewById(R.id.txtLuasSerang);
        btn_cetak = (Button) findViewById(R.id.btn_cetak);
//        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        img_bukti = (ImageView) findViewById(R.id.img_bukti);
        progressDialog = new ProgressDialog(this);
        calendar = Calendar.getInstance();

        id_intensitas = getIntent().getStringExtra("id");

        getLaporan();

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

    private void getData() {
        String url = Constants.ROOT_URL + "Intensitas?id=" + id_intensitas;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/siap_opa");
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
                            final Document document = new Document(PageSize.A4);
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
                            PdfPCell note4 = new PdfPCell(new Paragraph("Tanggal Pengamatan: "+tanggal_intensitas, a));
                            note1.setBorder(Rectangle.NO_BORDER);
                            note2.setBorder(Rectangle.NO_BORDER);
                            note3.setBorder(Rectangle.NO_BORDER);
                            note4.setBorder(Rectangle.NO_BORDER);
                            data.addCell(note1);
                            data.addCell(note2);
                            data.addCell(note3);
                            data.addCell(note4);
                            final PdfPTable table = new PdfPTable(new float[]{1, 2, 2, 2, 3, 2, 2, 2, 2, 3, 2, 3});
                            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.getDefaultCell().setFixedHeight(50);
                            table.setTotalWidth(PageSize.A4.getWidth());
                            table.setWidthPercentage(100);
                            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell("No");
                            table.addCell("Desa");
                            table.addCell("Blok");
                            table.addCell("Komoditas");
                            table.addCell("Varietas");
                            table.addCell("Umur Tanaman");
                            table.addCell("Luas Hamparan");
                            table.addCell("Luas Terserang");
                            table.addCell("Jenis OPT");
                            table.addCell("Intensitas");
                            table.addCell("Luas Waspada");
                            table.addCell("Rekomendasi");
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
                                luas_tanaman = object.getString("luas_tanaman")+" Ha";
                                umur_tanaman = object.getString("umur_tanaman")+" HST";
                                jenis_opt = object.getString("jenis_opt");
                                varietas = object.getString("varietas");
                                intensitas_tambah = object.getString("intensitas_tambah")+" %";
                                luas_sisa_serang = object.getString("luas_tambah_serang")+" Ha";
                                luas_waspada = object.getString("luas_waspada")+" Ha";
                                tanggal_intensitas = object.getString("tanggal_intensitas");
                                rekomendasi = object.getString("rekomendasi");

                                table.addCell(String.valueOf(nomor));
                                table.addCell(desa);
                                table.addCell(blok);
                                table.addCell(komoditas);
                                table.addCell(varietas);
                                table.addCell(umur_tanaman);
                                table.addCell(luas_tanaman);
                                table.addCell(luas_sisa_serang);
                                table.addCell(jenis_opt);
                                table.addCell(intensitas_tambah);
                                table.addCell(luas_waspada);
                                table.addCell(rekomendasi);

                            }
                            PdfWriter.getInstance(document, output);
                            document.open();
                            document.add(data);
                            document.add(new Paragraph("Laporan Harian", a));
                            document.add(table);
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
                Toast.makeText(HasilLapHarian.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(HasilLapHarian.this).add(stringRequest);

    }

    private void previewPdf() {
        Intent intent = new Intent(HasilLapHarian.this, PdfView.class);
        intent.putExtra("path", "Laporan Harian "+tanggal_intensitas+".pdf");
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length <= 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(HasilLapHarian.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
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
                                petugas_pengamatan = object.getString("petugas_pengamatan");
                                String bukti_foto = object.getString("bukti_foto");
                                String url = Constants.ROOT_URL + "assets/Images/" + bukti_foto;
//                                surat_kab = object.getString("surat_kab");
//                                surat_satpel = object.getString("surat_satpel");
//                                surat_provinsi = object.getString("surat_provinsi");
//                                surat_popt = object.getString("surat");
//                                String approval_prov = object.getString("approval_provinsi");

//                                if (Common.currentUser.getId_usergroup().equals("1")) {
//                                    txtKabOpt.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari Kabupaten: </b></font>" + kab_to_popt));
//                                    txtSatpelOpt.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari SatPel: </b></font>" + satpel_to_popt));
//                                    txtProvOpt.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari Provinsi: </b></font>" + prov_to_popt));
//                                    txtSatpelKab.setVisibility(View.GONE);
//                                    txtProvKab.setVisibility(View.GONE);
//                                    txtProvSatpel.setVisibility(View.GONE);
//                                    btn_send.setVisibility(View.GONE);
//                                } else if (Common.currentUser.getId_usergroup().equals("2")) {
//                                    txtKabOpt.setVisibility(View.GONE);
//                                    txtSatpelOpt.setVisibility(View.GONE);
//                                    txtProvOpt.setVisibility(View.GONE);
//                                    txtSatpelKab.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari SatPel: </b></font>" + satpel_to_kab));
//                                    txtProvKab.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari Provinsi: </b></font>" + prov_to_kab));
//                                    txtProvSatpel.setVisibility(View.GONE);
//                                    btn_send.setVisibility(View.GONE);
//                                } else if (Common.currentUser.getId_usergroup().equals("3")) {
//                                    txtKabOpt.setVisibility(View.GONE);
//                                    txtSatpelOpt.setVisibility(View.GONE);
//                                    txtProvOpt.setVisibility(View.GONE);
//                                    txtSatpelKab.setVisibility(View.GONE);
//                                    txtProvKab.setVisibility(View.GONE);
//                                    txtProvSatpel.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari Provinsi: </b></font>" + prov_to_satpel));
//                                    btn_send.setVisibility(View.GONE);
//                                } else if (Common.currentUser.getId_usergroup().equals("4")) {
//                                    txtKabOpt.setVisibility(View.GONE);
//                                    txtSatpelOpt.setVisibility(View.GONE);
//                                    txtProvOpt.setVisibility(View.GONE);
//                                    txtSatpelKab.setVisibility(View.GONE);
//                                    txtProvKab.setVisibility(View.GONE);
//                                    txtProvSatpel.setVisibility(View.GONE);
//                                } else if (Common.currentUser.getId_usergroup().equals("6")) {
//                                    txtKabOpt.setVisibility(View.GONE);
//                                    txtSatpelOpt.setVisibility(View.GONE);
//                                    txtProvOpt.setVisibility(View.GONE);
//                                    txtSatpelKab.setVisibility(View.GONE);
//                                    txtProvKab.setVisibility(View.GONE);
//                                    txtProvSatpel.setVisibility(View.GONE);
//                                    btn_send.setVisibility(View.GONE);
//                                }

                                txtKabupaten.setText(Html.fromHtml("<font color='#000000'><b>Kabupaten: </b></font>" + kabupaten));
                                txtKecamatan.setText(Html.fromHtml("<font color='#000000'><b>Kecamatan: </b></font>" + kecamatan));
                                txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Desa: </b></font>" + desa));
                                txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Tanggal: </b></font>" + tanggal_intensitas));
                                txtLuasTanaman.setText(Html.fromHtml("<font color='#000000'><b>Luas Tanaman: </b></font>" + luas_tanaman + " Ha"));
                                txtUmurTanaman.setText(Html.fromHtml("<font color='#000000'><b>Umur Tanaman: </b></font>" + umur_tanaman + " HST"));
                                txtOPT.setText(Html.fromHtml("<font color='#000000'><b>Jenis OPT: </b></font>" + jenis_opt));
                                txtLuasSerang.setText(Html.fromHtml("<font color='#000000'><b>Luas Tambah Serangan: </b></font>" + luas_sisa_serang + " Ha"));
                                txtLuasWaspada.setText(Html.fromHtml("<font color='#000000'><b>Luas Waspada: </b></font>" + luas_waspada + " Ha"));
                                txtIntensitas.setText(Html.fromHtml("<font color='#000000'><b>Intensitas: </b></font>" + intensitas_tambah + " %"));
                                txtKomoditas.setText(Html.fromHtml("<font color='#000000'><b>Komoditas: </b></font>" + komoditas));
                                txtVarietas.setText(Html.fromHtml("<font color='#000000'><b>Varietas: </b></font>" + varietas));
                                txtBlok.setText(Html.fromHtml("<font color='#000000'><b>Blok: </b></font>" + blok));
                                txtRekomendasi.setText(Html.fromHtml("<font color='#000000'><b>Rekomendasi: </b></font>" + rekomendasi));
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
                Toast.makeText(HasilLapHarian.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(HasilLapHarian.this).add(stringRequest);
    }
}