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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class  DetailStghBulan extends AppCompatActivity {
    TextView txtApprovalKab, txtApprovalSatpel, txtApprovalProv, txtProvinsi, txtKabupaten, txtKecamatan, txtDesa,
            txtTanggal, txtPeriode, txtLuasTanaman, txtUmurTanaman, txtOPT,
            txtLuasSembuh, txtLuasSerang, txtIntensitasSerang, txtPusoSisa,
            txtJumlahSisa, txtLuasTambah, txtIntenTambah, txtPusoTambah, txtJumlahTambah,
            txtLuasPanen, txtLuasPengendalian, txtLuasKeadaan, txtIntenKeadaan, txtPusoKeadaan,
            txtJumlahKeadaan, txtKimia, txtNabati, txtEradikasi, txtCaraLain, txtJumlahPengendalian,
            txtFrekKimia, txtFrekNabati, txtLuasWaspada, txtKabOpt, txtSatpelOpt, txtSatpelKab, txtProvOpt,
            txtProvKab, txtProvSatpel, txtBatas, txtRekomendasi, txtBatasSatPel;
    String id_intensitas, ma, musuh_alami, tambah_ringan, tambah_sedang, tambah_berat, tambah_puso, jumlah_ma, kecamatan, kabupaten, opt, blok, komoditas, luas_panen, periode_pengamatan, pola_tanam, petugas_pengamatan, luas_persemaian, luas_tambah_serang, luas_sembuh, rekomendasi, luas_tanaman, umur_tanaman, jenis_opt, luas_serang, id_hasil, desa, luas, varietas, umur, luas_waspada, intensitas, tanggal_intensitas;
    Toolbar toolbar;
    Button btn_send, btn_pdf, btn_cetak;
    ProgressDialog progressDialog;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date, surat_kab, surat_satpel, surat_provinsi, surat_popt;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    TextView txtPeringatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_stgh_bulan);

        txtApprovalProv = (TextView) findViewById(R.id.txtApprovalProv);
//        txtPeringatan = findViewById(R.id.peringatan);
//        txtPeringatan.setVisibility(View.GONE);
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
//        btn_send = (Button) findViewById(R.id.btn_send);
//        btn_pdf = (Button) findViewById(R.id.btn_pdf);
        btn_cetak = (Button) findViewById(R.id.btn_cetak);
//        img_bukti = (ImageView) findViewById(R.id.img_bukti);
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
        String url = Constants.ROOT_URL+"Stgh_Bln?id="+id_intensitas;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/siap_opa");
                            if (!docsFolder.exists()) {
                                docsFolder.mkdir();
                            }
                            String pdfname = "Laporan Setengah Bulan "+tanggal_intensitas+".pdf";
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
                            PdfPCell note3 = new PdfPCell(new Paragraph("Periode Laporan: "+periode_pengamatan, a));
                            PdfPCell note4 = new PdfPCell(new Paragraph("Tanggal Laporan: "+tanggal_intensitas, a));
                            note1.setBorder(Rectangle.NO_BORDER);
                            note2.setBorder(Rectangle.NO_BORDER);
                            note3.setBorder(Rectangle.NO_BORDER);
                            note4.setBorder(Rectangle.NO_BORDER);
                            data.addCell(note1);
                            data.addCell(note2);
                            data.addCell(note3);
                            data.addCell(note4);
                            final PdfPTable table = new PdfPTable(32);
                            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.getDefaultCell().setFixedHeight(50);
                            table.setTotalWidth(PageSize.A4.getWidth());
                            table.setWidthPercentage(100);
                            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                            PdfPCell cell = new PdfPCell(new Phrase("No", a));
                            cell.setRowspan(2);
                            table.addCell(cell);
                            PdfPCell cell1 = new PdfPCell(new Phrase("Kec", a));
                            cell1.setRowspan(2);
                            table.addCell(cell1);
                            PdfPCell cell2 = new PdfPCell(new Phrase("Des", a));
                            cell2.setRowspan(2);
                            table.addCell(cell2);
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
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int nomor = i + 1;
                                String kecamatan = object.getString("kecamatan");
                                String desa = object.getString("desa");
                                periode_pengamatan = object.getString("periode");
                                luas_tanaman = object.getString("luas_tanaman");
                                umur_tanaman = object.getString("umur_tanaman");
                                jenis_opt = object.getString("jenis_opt");
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
                                String intensitas_tambah = object.getString("intensitas_tambah");
                                if (Float.parseFloat(intensitas_tambah) <= 25  && luas_waspada.equals("0")){
                                    tambah_ringan = object.getString("luas_tambah");
                                    tambah_sedang = "0";
                                    tambah_berat = "0";
                                    tambah_puso = "0";
                                }else if (Float.parseFloat(intensitas_tambah) > 25 && Float.parseFloat(intensitas_tambah) <= 50){
                                    tambah_ringan = "0";
                                    tambah_sedang = object.getString("luas_tambah");
                                    tambah_berat = "0";
                                    tambah_puso = "0";
                                }else if (Float.parseFloat(intensitas_tambah) > 50 && Float.parseFloat(intensitas_tambah) <= 85){
                                    tambah_ringan = "0";
                                    tambah_sedang = "0";
                                    tambah_berat = object.getString("luas_tambah");
                                    tambah_puso = "0";
                                }else if (Float.parseFloat(intensitas_tambah) > 85){
                                    tambah_ringan = "0";
                                    tambah_sedang = "0";
                                    tambah_berat = "0";
                                    tambah_puso = object.getString("luas_tambah");
                                }
                                String keadaan_ringan = object.getString("keadaan_ringan");
                                String keadaan_sedang = object.getString("keadaan_sedang");
                                String keadaan_berat = object.getString("keadaan_berat");
                                String keadaan_puso = object.getString("keadaan_puso");

                                table.addCell(String.valueOf(nomor));
                                table.addCell(kecamatan);
                                table.addCell(desa);
                                table.addCell(komoditas);
                                table.addCell(varietas);
                                table.addCell(luas_tanaman);
                                table.addCell(jenis_opt);
                                table.addCell(sisa_ringan);
                                table.addCell(sisa_sedang);
                                table.addCell(sisa_berat);
                                table.addCell(sisa_puso);
                                table.addCell(jumlah_sisa);
                                table.addCell(luas_sembuh);
                                table.addCell(luas_panen);
                                table.addCell(tambah_ringan);
                                table.addCell(tambah_sedang);
                                table.addCell(tambah_berat);
                                table.addCell(tambah_puso);
                                table.addCell(jumlah_tambah);
                                table.addCell(kimia);
                                table.addCell(nabati);
                                table.addCell(eradikasi);
                                table.addCell(cara_lain);
                                table.addCell(jumlah_pengendalian);
                                table.addCell(keadaan_ringan);
                                table.addCell(keadaan_sedang);
                                table.addCell(keadaan_berat);
                                table.addCell(keadaan_puso);
                                table.addCell(jumlah_keadaan);
                                table.addCell(frek_kimia);
                                table.addCell(frek_nabati);
                                table.addCell(luas_waspada);

                            }
                            PdfWriter.getInstance(document, output);
                            document.open();
                            document.add(data);
                            document.add(new Paragraph("Laporan Setengah Bulanan", a));
                            document.add(table);
//                            document.add(new Paragraph("POPT-PHP", a));
//                                                document.add(image);
//                            document.add(new Paragraph(petugas_pengamatan, a));
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
                Toast.makeText(DetailStghBulan.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailStghBulan.this).add(stringRequest);

    }

    private void previewPdf() {
        Intent intent = new Intent(DetailStghBulan.this, PdfView.class);
        intent.putExtra("path", "Laporan Setengah Bulan "+tanggal_intensitas+".pdf");
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length <= 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(DetailStghBulan.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
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

    private void getLaporan(){
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String urlLaporan = Constants.ROOT_URL+"Stgh_Bln?id="+id_intensitas;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

//                                String wilayah_pengamatan = object.getString("provinsi");
                                String batas_waktu_kab = object.getString("batas_waktu_kab");
                                String batas_waktu_satpel = object.getString("batas_waktu_satpel");
                                kabupaten = object.getString("kabupaten");
                                String kecamatan = object.getString("kecamatan");
                                String desa = object.getString("desa");
                                periode_pengamatan = object.getString("periode");
                                luas_tanaman = object.getString("luas_tanaman");
                                umur_tanaman = object.getString("umur_tanaman");
                                komoditas = object.getString("komoditas");
                                varietas = object.getString("varietas");
                                jenis_opt = object.getString("jenis_opt");
                                luas_sembuh = object.getString("luas_terkendali");
                                String luas_sisa_serang = object.getString("luas_sisa");
                                String intensitas_serang = object.getString("intensitas_sisa");
                                String jumlah_sisa_serang = object.getString("jumlah_sisa");
                                String intensitas_tambah = object.getString("intensitas_tambah");
                                String jumlah_tambah_serang = object.getString("jumlah_tambah");
                                luas_tambah_serang = object.getString("luas_tambah");
                                String luas_keadaan_serang = object.getString("luas_keadaan");
                                String intensitas_keadaan = object.getString("intensitas_keadaan");
                                String jumlah_keadaan_serang = object.getString("jumlah_keadaan");
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
                                String approval_kab = object.getString("approval_kab");
                                String approval_satpel = object.getString("approval_satpel");
                                String approval_provinsi = object.getString("approval_prov");
//                                String kab_to_popt = object.getString("kab_to_popt");
//                                String satpel_to_popt = object.getString("satpel_to_popt");
//                                String satpel_to_kab = object.getString("satpel_to_kab");
//                                String prov_to_kab = object.getString("prov_to_kab");
//                                String prov_to_satpel = object.getString("prov_to_satpel");
//                                String prov_to_popt = object.getString("prov_to_popt");
//                                String rekomendasi = object.getString("rekomendasi");
//                                String bukti_foto = object.getString("bukti_foto");
//                                String url = Constants.ROOT_URL+"assets/Images/"+bukti_foto;
//                                surat_kab = object.getString("surat_kab");
//                                surat_satpel = object.getString("surat_satpel");
//                                surat_provinsi = object.getString("surat_provinsi");
//                                surat_popt = object.getString("surat");
//                                String approval_prov = object.getString("approval_provinsi");

//                                if (Common.currentUser.getId_usergroup().equals("1")){
//                                    txtKabOpt.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari Kabupaten: </b></font>" + kab_to_popt));
//                                    txtSatpelOpt.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari SatPel: </b></font>" + satpel_to_popt));
//                                    txtProvOpt.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari Provinsi: </b></font>" + prov_to_popt));
//                                    txtSatpelKab.setVisibility(View.GONE);
//                                    txtProvKab.setVisibility(View.GONE);
//                                    txtProvSatpel.setVisibility(View.GONE);
//                                    btn_send.setVisibility(View.GONE);
//                                }else if (Common.currentUser.getId_usergroup().equals("2")){
//                                    txtKabOpt.setVisibility(View.GONE);
//                                    txtSatpelOpt.setVisibility(View.GONE);
//                                    txtProvOpt.setVisibility(View.GONE);
//                                    txtSatpelKab.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari SatPel: </b></font>" + satpel_to_kab));
//                                    txtProvKab.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari Provinsi: </b></font>" + prov_to_kab));
//                                    txtProvSatpel.setVisibility(View.GONE);
//                                    btn_send.setVisibility(View.GONE);
//                                }else if (Common.currentUser.getId_usergroup().equals("3")){
//                                    txtKabOpt.setVisibility(View.GONE);
//                                    txtSatpelOpt.setVisibility(View.GONE);
//                                    txtProvOpt.setVisibility(View.GONE);
//                                    txtSatpelKab.setVisibility(View.GONE);
//                                    txtProvKab.setVisibility(View.GONE);
//                                    txtProvSatpel.setText(Html.fromHtml("<font color='#000000'><b>Catatan Dari Provinsi: </b></font>" + prov_to_satpel));
//                                    btn_send.setVisibility(View.GONE);
//                                }else if (Common.currentUser.getId_usergroup().equals("4")){
//                                    txtKabOpt.setVisibility(View.GONE);
//                                    txtSatpelOpt.setVisibility(View.GONE);
//                                    txtProvOpt.setVisibility(View.GONE);
//                                    txtSatpelKab.setVisibility(View.GONE);
//                                    txtProvKab.setVisibility(View.GONE);
//                                    txtProvSatpel.setVisibility(View.GONE);
//                                }
//                                else if (Common.currentUser.getId_usergroup().equals("6")){
//                                    txtKabOpt.setVisibility(View.GONE);
//                                    txtSatpelOpt.setVisibility(View.GONE);
//                                    txtProvOpt.setVisibility(View.GONE);
//                                    txtSatpelKab.setVisibility(View.GONE);
//                                    txtProvKab.setVisibility(View.GONE);
//                                    txtProvSatpel.setVisibility(View.GONE);
//                                    btn_send.setVisibility(View.GONE);
//                                }

                                txtBatas.setText(Html.fromHtml("<font color='#ff0000'><b>Batas Waktu Validasi Kabupaten: </b><br></font>" + batas_waktu_kab));
                                txtBatasSatPel.setText(Html.fromHtml("<font color='#ff0000'><b>Batas Waktu Validasi SatPel: </b><br></font>" + batas_waktu_satpel));
                                txtApprovalKab.setText(Html.fromHtml("<font color='#000000'><b>Validasi Kabupaten: </b></font>" + approval_kab));
                                txtApprovalSatpel.setText(Html.fromHtml("<font color='#000000'><b>Validasi Satpel: </b></font>" + approval_satpel));
                                txtApprovalProv.setText(Html.fromHtml("<font color='#000000'><b>Validasi Provinsi: </b></font>" + approval_provinsi));
//                                txtProvinsi.setText(Html.fromHtml("<font color='#000000'><b>Provinsi: </b></font>" + wilayah_pengamatan));
                                txtKabupaten.setText(Html.fromHtml("<font color='#000000'><b>Kabupaten: </b></font>" + kabupaten));
                                txtKecamatan.setText(Html.fromHtml("<font color='#000000'><b>Kecamatan: </b></font>" + kecamatan));
                                txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Desa: </b></font>" + desa));
                                txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Tanggal: </b></font>" + tanggal_intensitas));
                                txtPeriode.setText(Html.fromHtml("<font color='#000000'><b>Periode Pengamatan: </b></font>" + periode_pengamatan));
                                txtLuasTanaman.setText(Html.fromHtml("<font color='#000000'><b>Luas Tanaman: </b></font>" + luas_tanaman+ " Ha"));
                                txtUmurTanaman.setText(Html.fromHtml("<font color='#000000'><b>Umur Tanaman: </b></font>" + umur_tanaman+ " HST"));
                                txtOPT.setText(Html.fromHtml("<font color='#000000'><b>Jenis OPT: </b></font>" + jenis_opt));
                                txtLuasSembuh.setText(Html.fromHtml("<font color='#000000'><b>Luas Sembuh: </b></font>" + luas_sembuh+ " Ha"));
                                txtLuasSerang.setText(Html.fromHtml("<font color='#000000'><b>Luas Sisa Serangan: </b></font>" + luas_sisa_serang+ " Ha"));
                                txtIntensitasSerang.setText(Html.fromHtml("<font color='#000000'><b>Intensitas Serangan: </b></font>" + intensitas_serang+ " %"));
//                                txtPusoSisa.setText(Html.fromHtml("<font color='#000000'><b>Puso Sisa Serangan: </b></font>" + puso_sisa_serang+ " Ha"));
                                txtJumlahSisa.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Sisa Serangan: </b></font>" + jumlah_sisa_serang+ " Ha"));
                                txtLuasTambah.setText(Html.fromHtml("<font color='#000000'><b>Luas Tambah Serangan: </b></font>" + luas_tambah_serang+ " Ha"));
                                txtIntenTambah.setText(Html.fromHtml("<font color='#000000'><b>Intensitas Tambah Serangan: </b></font>" + intensitas_tambah+ " %"));
                                txtJumlahTambah.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Tambah Serangan: </b></font>" + jumlah_tambah_serang+ " Ha"));
//                                txtLuasPengendalian.setText(Html.fromHtml("<font color='#000000'><b>Luas Pengendalian: </b></font>" + luas_pengendalian+ " Ha"));
                                txtLuasKeadaan.setText(Html.fromHtml("<font color='#000000'><b>Luas Keadaan Serangan: </b></font>" + luas_keadaan_serang+ " Ha"));
                                txtIntenKeadaan.setText(Html.fromHtml("<font color='#000000'><b>Intensitas Keadaan Serangan: </b></font>" + intensitas_keadaan+ " %"));
                                txtJumlahKeadaan.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Keadaan Serangan: </b></font>" + jumlah_keadaan_serang+ " Ha"));
                                txtLuasWaspada.setText(Html.fromHtml("<font color='#000000'><b>Luas Waspada: </b></font>" + luas_waspada+ " Ha"));
                                txtLuasPanen.setText(Html.fromHtml("<font color='#000000'><b>Luas Panen: </b></font>" + luas_panen+ " Ha"));
                                txtKimia.setText(Html.fromHtml("<font color='#000000'><b>Kimia: </b></font>" + kimia+ " Ha"));
                                txtNabati.setText(Html.fromHtml("<font color='#000000'><b>Nabati: </b></font>" + nabati+ " Ha"));
                                txtEradikasi.setText(Html.fromHtml("<font color='#000000'><b>Eradikasi: </b></font>" + eradikasi+ " Ha"));
                                txtCaraLain.setText(Html.fromHtml("<font color='#000000'><b>Cara Lain: </b></font>" + cara_lain+ " Ha"));
                                txtJumlahPengendalian.setText(Html.fromHtml("<font color='#000000'><b>Jumlah Pengendalian: </b></font>" + jumlah_pengendalian+ " Ha"));
                                txtFrekKimia.setText(Html.fromHtml("<font color='#000000'><b>Frekuensi Kimia: </b></font>" + frek_kimia+ " Kali"));
                                txtFrekNabati.setText(Html.fromHtml("<font color='#000000'><b>Frekuensi Nabati: </b></font>" + frek_nabati+ " Kali"));
//                                Picasso.get().load(url).into(img_bukti);
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
                Toast.makeText(DetailStghBulan.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailStghBulan.this).add(stringRequest);
    }
}