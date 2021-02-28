package com.majesty.siapopa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.majesty.siapopa.model.DesaModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  DetailLaporan extends AppCompatActivity {
    TextView txtPelaporan, txtApprovalKab, txtApprovalSatpel, txtApprovalProv, msg_kab, msg_satpel, msg_prov, txtKR, txtKS, txtKB, txtKP, txtJumlahKeadaan, ctt_popt, ctt_kab, ctt_satpel;
    TextView txtTanggal, txtKecamatan, txtDesa, txtPeriode, txtOPT, txtKomoditas, txtVarietas, txtLuasHamparan, txtLuasWaspada, txtUmurTanaman, txtMusim, txtLuasTambah, txtTR, txtTS, txtTB, txtTP,
            txtJumlahTambah, txtLuasPengendalian, txtKimia, txtNabati, txtEradikasi, txtCaraLain, txtJumlahPengendalian, txtKimiaN, txtNabatiN, txtEradikasiN, txtCaraLainN, txtJumlahPengendalianN, txtFrekuensi, txtFrekKimia, txtFrekNabati,txtFrekKimiaN, txtFrekNabatiN,
            txtLuasSisa, txtSR, txtSS, txtSB, txtSP, txtJumlahSisa, sebelumnya, txtLuasPanen, txtLuasSembuh, txtLuasPanenN, txtLuasSembuhN, txtSRS, txtSSS, txtSBS, txtSPS, txtJumlahSisaS, txtSRN, txtSSN, txtSBN, txtSPN, txtJumlahSisaN, txtTRS, txtTSS, txtTBS, txtTPS, txtJumlahTambahS, sekarang ;
    String luas_tambah_sebelumnya, t_ringan, t_sedang, t_berat, t_puso, luas_sisa_sebelumnya, s_ringan, s_sedang, s_berat, s_puso, panen_sebelumnya, sembuh_sebelumnya, jumlah_keadaan, kimia_seb, nabati_seb, eradikasi_seb, cara_lain_seb, frek_kimia_seb, frek_nabati_seb, luas_pengendalian_seb;
    EditText msg_to_popt, msg_to_kab, msg_to_satpel, msg_to_prov, edtRekomendasi;
    String id_intensitas, puso_tambah_serang;
    Button btnSignature, btnApprove, btnTolak;
    ProgressDialog progressDialog;
    RecyclerView recyclerView1;
    RecyclerView.LayoutManager layoutManager1;
    List<DesaModel> listDataDesa;
    private RecyclerView.Adapter adapter, madapter;
    private SimpleDateFormat dateFormat;
    private String date, substring;
    String intensitas_serang, luas_keadaan_sisa, intensitas_keadaan_sisa, ma, periode, k_ringan, k_sedang, k_berat, k_puso;
    int keadaan_ringan, keadaan_sedang, keadaan_berat, keadaan_puso;
    String opt, kabupaten, kecamatan, desa, umur_tanaman, luas_tambah, periode_pengamatan, luas_tanaman, komoditas, varietas, intensitas_tambah, luas_waspada, now, musim_tanam;
    String tambah_ringan, tambah_sedang, tambah_berat, tambah_puso, blok, luas_panen,  pola_tanam, petugas_pengamatan, luas_persemaian, luas_tambah_serang, luas_sembuh, rekomendasi, jenis_opt, luas_serang, id_hasil,  luas,  umur,  intensitas, tanggal_intensitas;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    LinearLayout linearLayout;
    private int REQ_PDF = 21;
    String encodedPDF, status;
    Calendar calendar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Laporan Setengah Bulan");
        setSupportActionBar(toolbar);
        ctt_popt = (TextView) findViewById(R.id.ctt_popt);
        ctt_kab = (TextView) findViewById(R.id.ctt_kab);
        ctt_satpel = (TextView) findViewById(R.id.ctt_satpel);
        txtKR = (TextView) findViewById(R.id.txtKR);
        txtKB = (TextView) findViewById(R.id.txtKB);
        txtKS = (TextView) findViewById(R.id.txtKS);
        txtKP = (TextView) findViewById(R.id.txtKP);
        txtJumlahKeadaan = (TextView) findViewById(R.id.txtJumlahKeadaan);
        txtKimia = (TextView) findViewById(R.id.txtKimia);
        txtNabati = (TextView) findViewById(R.id.txtNabati);
        txtEradikasi = (TextView) findViewById(R.id.txtEradikasi);
        txtCaraLain = (TextView) findViewById(R.id.txtCaraLain);
        txtJumlahPengendalian = (TextView) findViewById(R.id.txtJumlahPengendalian);
        txtFrekKimia = (TextView) findViewById(R.id.txtFrekKimia);
        txtFrekNabati = (TextView) findViewById(R.id.txtFrekNabati);
        txtKimiaN = (TextView) findViewById(R.id.txtKimiaN);
        txtNabatiN = (TextView) findViewById(R.id.txtNabatiN);
        txtEradikasiN = (TextView) findViewById(R.id.txtEradikasiN);
        txtCaraLainN = (TextView) findViewById(R.id.txtCaraLainN);
        txtJumlahPengendalianN = (TextView) findViewById(R.id.txtJumlahPengendalianN);
        txtFrekKimiaN = (TextView) findViewById(R.id.txtFrekKimiaN);
        txtFrekNabatiN = (TextView) findViewById(R.id.txtFrekNabatiN);
        txtLuasPanenN = (TextView) findViewById(R.id.txtLuasPanenN);
        txtLuasSembuhN = (TextView) findViewById(R.id.txtLuasSembuhN);
        sebelumnya = (TextView) findViewById(R.id.sebelumnya);
        txtLuasPanen = (TextView) findViewById(R.id.txtLuasPanen);
        txtLuasSembuh = (TextView) findViewById(R.id.txtLuasSembuh);
        txtSRS = (TextView) findViewById(R.id.txtSRS);
        txtSSS = (TextView) findViewById(R.id.txtSSS);
        txtSBS = (TextView) findViewById(R.id.txtSBS);
        txtSPS = (TextView) findViewById(R.id.txtSPS);
        txtJumlahSisaS = (TextView) findViewById(R.id.txtJumlahSisaS);
        txtSRN = (TextView) findViewById(R.id.txtSRN);
        txtSSN = (TextView) findViewById(R.id.txtSSN);
        txtSBN = (TextView) findViewById(R.id.txtSBN);
        txtSPN = (TextView) findViewById(R.id.txtSPN);
        txtJumlahSisaN = (TextView) findViewById(R.id.txtJumlahSisaN);
        txtTRS = (TextView) findViewById(R.id.txtTRS);
        txtTSS = (TextView) findViewById(R.id.txtTSS);
        txtTBS = (TextView) findViewById(R.id.txtTBS);
        txtTPS = (TextView) findViewById(R.id.txtTPS);
        txtJumlahTambahS = (TextView) findViewById(R.id.txtJumlahTambahS);
        sekarang = (TextView) findViewById(R.id.sekarang);
        msg_kab = (TextView) findViewById(R.id.msg_kab);
        msg_satpel = (TextView) findViewById(R.id.msg_satpel);
        msg_prov = (TextView) findViewById(R.id.msg_prov);
        txtPelaporan = (TextView) findViewById(R.id.txtPelaporan);
        txtApprovalKab = (TextView) findViewById(R.id.txtApprovalKab);
        txtApprovalSatpel = (TextView) findViewById(R.id.txtApprovalSatpel);
        txtApprovalProv = (TextView) findViewById(R.id.txtApprovalProv);
        txtOPT = (TextView) findViewById(R.id.txtOPT);
        txtJumlahSisa = (TextView) findViewById(R.id.txtJumlahSisa);
        txtSP = (TextView) findViewById(R.id.txtSP);
        txtSB = (TextView) findViewById(R.id.txtSB);
        txtSS = (TextView) findViewById(R.id.txtSS);
        txtSR = (TextView) findViewById(R.id.txtSR);
        txtLuasSisa = (TextView) findViewById(R.id.txtLuasSisa);
        txtJumlahTambah = (TextView) findViewById(R.id.txtJumlahTambah);
        txtTP = (TextView) findViewById(R.id.txtTP);
        txtTB = (TextView) findViewById(R.id.txtTB);
        txtDesa = (TextView) findViewById(R.id.txtDesa);
        txtKomoditas = (TextView) findViewById(R.id.txtKomoditas);
        txtVarietas = (TextView) findViewById(R.id.txtVarietas);
        txtLuasHamparan = (TextView) findViewById(R.id.txtLuasHamparan);
        txtKecamatan = (TextView) findViewById(R.id.txtKecamatan);
        txtPeriode = (TextView) findViewById(R.id.txtPeriode);
        txtMusim = (TextView) findViewById(R.id.txtMusim);
        txtUmurTanaman = (TextView) findViewById(R.id.txtUmurTanaman);
        txtTanggal = (TextView) findViewById(R.id.txtTanggal);
        txtLuasTambah = (TextView) findViewById(R.id.txtLuasTambah);
        txtTR = (TextView) findViewById(R.id.txtTR);
        txtTS = (TextView) findViewById(R.id.txtTS);
        txtLuasWaspada = (TextView) findViewById(R.id.txtLuasWaspada);
        calendar = Calendar.getInstance();
        msg_to_popt = (EditText) findViewById(R.id.msg_to_popt);
        msg_to_kab = (EditText) findViewById(R.id.msg_to_kab);
        msg_to_satpel = (EditText) findViewById(R.id.msg_to_satpel);
        btnApprove = (Button) findViewById(R.id.btnLapor);
        btnTolak = (Button) findViewById(R.id.btnTolak);
        progressDialog = new ProgressDialog(this);

        id_intensitas = getIntent().getStringExtra("id_intensitas");

        if (Common.currentUser.getId_usergroup().equals("2")){
            msg_to_kab.setVisibility(View.GONE);
            msg_to_satpel.setVisibility(View.GONE);
//            edtRekomendasi.setVisibility(View.GONE);
        }else if (Common.currentUser.getId_usergroup().equals("3")) {
            msg_to_satpel.setVisibility(View.GONE);
//            edtRekomendasi.setVisibility(View.GONE);
        }

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
//                if (Common.currentUser.getId_usergroup().equals("2")){
//                    updateStatusApproval();
//                }else if (Common.currentUser.getId_usergroup().equals("3")) {
//                    updateStatusAppSatpel();
//                }else if (Common.currentUser.getId_usergroup().equals("4")) {
//                    updateStatusApprovalProv();
//                }

            }
        });

        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.currentUser.getId_usergroup().equals("2")){
                    updateStatusApprovalKab();
                }else if (Common.currentUser.getId_usergroup().equals("3")) {
                    updateStatusAppSatpelTolak();
                }else if (Common.currentUser.getId_usergroup().equals("4")) {
                    updateStatusApprovalProvTolak();
                }
            }
        });

        getSisa();
        updateStatus();

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
        RequestQueue requestQueue = Volley.newRequestQueue(DetailLaporan.this);
        requestQueue.add(request);
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
            getDatatoPDF();
        }
    }

    private void getDatatoPDF() {
        String url = Constants.ROOT_URL+"Stgh_Bln?id="+id_intensitas;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/siap_opa/setengah_bulan");
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
                            PdfPCell cell5 = new PdfPCell(new Phrase("LPertanaman (Ha)", a));
                            cell5.setRowspan(2);
                            table.addCell(cell5);
                            PdfPCell cell6 = new PdfPCell(new Phrase("OPT", a));
                            cell6.setRowspan(2);
                            table.addCell(cell6);
                            PdfPCell cell7 = new PdfPCell(new Phrase("Luas Sisa Serangan (Ha)", a));
                            cell7.setColspan(5);
                            table.addCell(cell7);
                            PdfPCell cell71 = new PdfPCell(new Phrase("LSem (Ha)", a));
                            cell71.setRowspan(2);
                            table.addCell(cell71);
                            PdfPCell cell72 = new PdfPCell(new Phrase("LPn (Ha)", a));
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
                            PdfPCell cell12 = new PdfPCell(new Phrase("LWas (Ha)", a));
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
                                String tambah_ringan = object.getString("tambah_ringan");
                                String tambah_sedang = object.getString("tambah_sedang");
                                String tambah_berat = object.getString("tambah_berat");
                                String tambah_puso = object.getString("tambah_puso");
                                String keadaan_ringan = object.getString("keadaan_ringan");
                                String keadaan_sedang = object.getString("keadaan_sedang");
                                String keadaan_berat = object.getString("keadaan_berat");
                                String keadaan_puso = object.getString("keadaan_puso");
                                petugas_pengamatan = object.getString("petugas");
                                if (Common.currentUser.getId_usergroup().equals("2")){
                                    status = object.getString("approval_kab");
                                }else if (Common.currentUser.getId_usergroup().equals("3")){
                                    status = object.getString("approval_satpel");
                                }else if (Common.currentUser.getId_usergroup().equals("4")){
                                    status = object.getString("approval_prov");
                                }

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

                            }
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
                            Paragraph title = new Paragraph("Laporan Setengah Bulanan", b);
                            title.setAlignment(Element.ALIGN_CENTER);
                            Paragraph subTitle;
                            if (periode_pengamatan.equals("Periode 1-15")) {
                                subTitle = new Paragraph("Periode 1-"+hari+" "+ bulan +" "+ tahun, c);
                                subTitle.setAlignment(Element.ALIGN_CENTER);
                            } else {
                                subTitle = new Paragraph("Periode 16-"+hari+" "+bulan +" "+ tahun, c);
                                subTitle.setAlignment(Element.ALIGN_CENTER);
                            }
                            document.add(title);
                            document.add(subTitle);
                            document.add(data);
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
                Toast.makeText(DetailLaporan.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailLaporan.this).add(stringRequest);

    }

    private void previewPdf() {
        if (Common.currentUser.getId_usergroup().equals("2")){
            Intent intent = new Intent(DetailLaporan.this, PdfView.class);
            intent.putExtra("validasi_stgh", "Laporan Setengah Bulan "+tanggal_intensitas+".pdf");
            intent.putExtra("id_hsl_stgh", id_intensitas);
            intent.putExtra("status", status);
            intent.putExtra("msg_to_popt", msg_to_popt.getText().toString());
            startActivity(intent);
        }else if (Common.currentUser.getId_usergroup().equals("3")){
            Intent intent = new Intent(DetailLaporan.this, PdfView.class);
            intent.putExtra("validasi_stgh", "Laporan Setengah Bulan "+tanggal_intensitas+".pdf");
            intent.putExtra("id_hsl_stgh", id_intensitas);
            intent.putExtra("status", status);
            intent.putExtra("msg_to_popt", msg_to_popt.getText().toString());
            intent.putExtra("msg_to_kab", msg_to_kab.getText().toString());
            startActivity(intent);
        }else if (Common.currentUser.getId_usergroup().equals("4")){
            Intent intent = new Intent(DetailLaporan.this, PdfView.class);
            intent.putExtra("validasi_stgh", "Laporan Setengah Bulan "+tanggal_intensitas+".pdf");
            intent.putExtra("id_hsl_stgh", id_intensitas);
            intent.putExtra("status", status);
            intent.putExtra("msg_to_popt", msg_to_popt.getText().toString());
            intent.putExtra("msg_to_kab", msg_to_kab.getText().toString());
            intent.putExtra("msg_to_satpel", msg_to_satpel.getText().toString());
            startActivity(intent);
        }
    }

    private void updateStatusApprovalProvTolak() {
        progressDialog.setTitle("Approve Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        final String prov_to_popt = msg_to_popt.getText().toString();
        final String prov_to_kab = msg_to_kab.getText().toString();
        final String prov_to_satpel = msg_to_satpel.getText().toString();
        String url = Constants.ROOT_URL+"ApprovalProv";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(DetailLaporan.this, "Data berhasil ditolak", Toast.LENGTH_SHORT).show();
//                            Intent home = new Intent(DetailLaporan.this, ApprovalProv.class);
//                            home.putExtra("HOME", "homesatpel");
//                            startActivity(home);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailLaporan.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_hsl_stgh", id_intensitas);
                params.put("approval_provinsi", "Ditolak");
                params.put("prov_to_popt", prov_to_popt);
                params.put("prov_to_kab", prov_to_kab);
                params.put("prov_to_satpel", prov_to_satpel);
//                params.put("rekomendasi", rekomendasi);
//                params.put("surat_provinsi", "tidak ada file");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailLaporan.this);
        requestQueue.add(request);
    }

    private void updateStatusAppSatpelTolak() {
        progressDialog.setTitle("Approve Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        final String satpel_to_popt = msg_to_popt.getText().toString();
        final String satpel_to_kab = msg_to_kab.getText().toString();
        String url = Constants.ROOT_URL+"Approvalsat";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(DetailLaporan.this, "Data Berhasil ditolak", Toast.LENGTH_SHORT).show();
//                            Intent home = new Intent(DetailLaporan.this, Home.class);
//                            home.putExtra("HOME", "homesatpel");
//                            startActivity(home);
                            finish();
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
                params.put("approval_satpel", "Ditolak");
                params.put("satpel_to_popt", satpel_to_popt);
                params.put("satpel_to_kab", satpel_to_kab);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailLaporan.this);
        requestQueue.add(request);
    }

    private void updateStatusApprovalKab() {
        progressDialog.setTitle("Approve Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        final String kab_to_popt = msg_to_popt.getText().toString();
        String url = Constants.ROOT_URL+"Approval";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(DetailLaporan.this, "Data Berhasil ditolak", Toast.LENGTH_SHORT).show();
//                            Intent home = new Intent(DetailLaporan.this, Home.class);
//                            home.putExtra("HOME", "homepkab");
//                            startActivity(home);
                            finish();
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
                params.put("approval_kab", "Ditolak");
                params.put("kab_to_popt", kab_to_popt);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailLaporan.this);
        requestQueue.add(request);
    }

    private void choosePDF() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("application/pdf");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, REQ_PDF);
    }

    private void getSisa() {
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        String url = Constants.ROOT_URL + "Stgh_Bln?id="+id_intensitas;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dateFormat = new SimpleDateFormat("dd");
                            String hari = dateFormat.format(calendar.getTime());
                            SimpleDateFormat month = new SimpleDateFormat("MMMM");
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                progressDialog.dismiss();

                                String periode_pengamatan = object.getString("periode");
                                String desa = object.getString("desa");
                                String opt = object.getString("jenis_opt");

                                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                date = dateFormat.format(calendar.getTime());
                                String bulan = date.substring(5,7);
                                String tahun = date.substring(0,4);
                                if (periode_pengamatan.equals("Periode 1-15")){
                                    periode = "Periode 16-31";
                                    int bulan1 = Integer.parseInt(bulan) - 1;
                                    int tahun1 = Integer.parseInt(tahun);
                                    String substring = String.valueOf(tahun1) + "-" + String.valueOf(bulan1);
                                    String urlLaporan = Constants.ROOT_URL+"Update_Inten?periode="+periode+"&date="+substring+"&desa="+desa+"&opt="+opt;
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONArray array = new JSONArray(response);
                                                        for (int i = 0; i<array.length(); i++){
                                                            JSONObject object = array.getJSONObject(i);

                                                            luas_keadaan_sisa = object.getString("jumlah_keadaan");
                                                            k_ringan = object.getString("keadaan_ringan");
                                                            k_sedang = object.getString("keadaan_sedang");
                                                            k_berat = object.getString("keadaan_berat");
                                                            k_puso = object.getString("keadaan_puso");
                                                            intensitas_keadaan_sisa = object.getString("intensitas_keadaan");
                                                            luas_tambah_sebelumnya = object.getString("jumlah_tambah");
                                                            t_ringan = object.getString("tambah_ringan");
                                                            t_sedang = object.getString("tambah_sedang");
                                                            t_berat = object.getString("tambah_berat");
                                                            t_puso = object.getString("tambah_puso");
                                                            luas_sisa_sebelumnya = object.getString("jumlah_sisa");
                                                            s_ringan = object.getString("sisa_ringan");
                                                            s_sedang = object.getString("sisa_sedang");
                                                            s_berat = object.getString("sisa_berat");
                                                            s_puso = object.getString("sisa_puso");
                                                            panen_sebelumnya = object.getString("luas_panen");
                                                            sembuh_sebelumnya = object.getString("luas_sembuh");
                                                            kimia_seb = object.getString("luas_kimia");
                                                            nabati_seb = object.getString("luas_nabati");
                                                            eradikasi_seb = object.getString("luas_eradikasi");
                                                            cara_lain_seb = object.getString("luas_cara_lain");
                                                            frek_kimia_seb = object.getString("frek_kimia");
                                                            frek_nabati_seb = object.getString("frek_nabati");
                                                            luas_pengendalian_seb = object.getString("jumlah_pengendalian");

                                                            getLaporan();


                                                        }
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            luas_keadaan_sisa = "0";
                                            intensitas_keadaan_sisa = "0";
                                            k_ringan = "0";
                                            k_sedang = "0";
                                            k_berat = "0";
                                            k_puso = "0";
                                            luas_tambah_sebelumnya = "0";
                                            t_ringan = "0";
                                            t_sedang = "0";
                                            t_berat = "0";
                                            t_puso = "0";
                                            luas_sisa_sebelumnya = "0";
                                            s_ringan = "0";
                                            s_sedang = "0";
                                            s_berat = "0";
                                            s_puso = "0";
                                            panen_sebelumnya = "0";
                                            sembuh_sebelumnya = "0";
                                            kimia_seb = "0";
                                            nabati_seb = "0";
                                            eradikasi_seb = "0";
                                            cara_lain_seb = "0";
                                            frek_kimia_seb = "0";
                                            frek_nabati_seb = "0";
                                            luas_pengendalian_seb = "0";
                                            getLaporan();
                                        }
                                    });
                                    Volley.newRequestQueue(DetailLaporan.this).add(stringRequest);
                                }else {
                                    periode = "Periode 1-15";
                                    String substring = date.substring(0, 7);

                                    String urlLaporan = Constants.ROOT_URL + "Update_Inten?periode=" + periode + "&date=" + substring + "&desa=" + desa + "&opt=" + opt;
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONArray array = new JSONArray(response);
                                                        for (int i = 0; i < array.length(); i++) {
                                                            JSONObject object = array.getJSONObject(i);

                                                            luas_keadaan_sisa = object.getString("jumlah_keadaan");
                                                            k_ringan = object.getString("keadaan_ringan");
                                                            k_sedang = object.getString("keadaan_sedang");
                                                            k_berat = object.getString("keadaan_berat");
                                                            k_puso = object.getString("keadaan_puso");
                                                            intensitas_keadaan_sisa = object.getString("intensitas_keadaan");
                                                            luas_tambah_sebelumnya = object.getString("jumlah_tambah");
                                                            t_ringan = object.getString("tambah_ringan");
                                                            t_sedang = object.getString("tambah_sedang");
                                                            t_berat = object.getString("tambah_berat");
                                                            t_puso = object.getString("tambah_puso");
                                                            luas_sisa_sebelumnya = object.getString("jumlah_sisa");
                                                            s_ringan = object.getString("sisa_ringan");
                                                            s_sedang = object.getString("sisa_sedang");
                                                            s_berat = object.getString("sisa_berat");
                                                            s_puso = object.getString("sisa_puso");
                                                            panen_sebelumnya = object.getString("luas_panen");
                                                            sembuh_sebelumnya = object.getString("luas_sembuh");
                                                            kimia_seb = object.getString("luas_kimia");
                                                            nabati_seb = object.getString("luas_nabati");
                                                            eradikasi_seb = object.getString("luas_eradikasi");
                                                            cara_lain_seb = object.getString("luas_cara_lain");
                                                            frek_kimia_seb = object.getString("frek_kimia");
                                                            frek_nabati_seb = object.getString("frek_nabati");
                                                            luas_pengendalian_seb = object.getString("jumlah_pengendalian");

                                                            getLaporan();

                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            luas_keadaan_sisa = "0";
                                            intensitas_keadaan_sisa = "0";
                                            k_ringan = "0";
                                            k_sedang = "0";
                                            k_berat = "0";
                                            k_puso = "0";
                                            luas_tambah_sebelumnya = "0";
                                            t_ringan = "0";
                                            t_sedang = "0";
                                            t_berat = "0";
                                            t_puso = "0";
                                            luas_sisa_sebelumnya = "0";
                                            s_ringan = "0";
                                            s_sedang = "0";
                                            s_berat = "0";
                                            s_puso = "0";
                                            panen_sebelumnya = "0";
                                            sembuh_sebelumnya = "0";
                                            kimia_seb = "0";
                                            nabati_seb = "0";
                                            eradikasi_seb = "0";
                                            cara_lain_seb = "0";
                                            frek_kimia_seb = "0";
                                            frek_nabati_seb = "0";
                                            luas_pengendalian_seb = "0";
                                            getLaporan();
                                        }
                                    });
                                    Volley.newRequestQueue(DetailLaporan.this).add(stringRequest);
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(DetailLaporan.this).add(stringRequest);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(DetailLaporan.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQ_PDF && resultCode == RESULT_OK && data != null){
//
//            Uri path = data.getData();
//
//
//            try {
//                InputStream inputStream = DetailLaporan.this.getContentResolver().openInputStream(path);
//                byte[] pdfInBytes = new byte[inputStream.available()];
//                inputStream.read(pdfInBytes);
//                encodedPDF = android.util.Base64.encodeToString(pdfInBytes, Base64.DEFAULT);
//
//                txtPDF.setText("Document Selected");
//                txtPDF.setTextColor(Color.BLACK);
////                btnSelect.setText("Change Document");
//
//                Toast.makeText(this, "Document Selected", Toast.LENGTH_SHORT).show();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

    private void updateStatusApprovalProv() {
        progressDialog.setTitle("Approve Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        final String prov_to_popt = msg_to_popt.getText().toString();
        final String prov_to_kab = msg_to_kab.getText().toString();
        final String prov_to_satpel = msg_to_satpel.getText().toString();
        String url = Constants.ROOT_URL+"ApprovalProv";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(DetailLaporan.this, "Data Berhasil ditolak", Toast.LENGTH_SHORT).show();
//                            Intent home = new Intent(DetailLaporan.this, ApprovalProv.class);
//                            home.putExtra("HOME", "homesatpel");
//                            startActivity(home);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailLaporan.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(DetailLaporan.this);
        requestQueue.add(request);
    }

    private void getLaporan(){
        String url = Constants.ROOT_URL + "Stgh_Bln?id="+id_intensitas;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dateFormat = new SimpleDateFormat("dd");
                            String hari = dateFormat.format(calendar.getTime());
                            SimpleDateFormat month = new SimpleDateFormat("MMMM");
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                progressDialog.dismiss();

                                String luas_sembuh = object.getString("luas_terkendali");
                                String luas_panen = object.getString("luas_panen");
                                String kimia = object.getString("luas_kimia");
                                String nabati = object.getString("luas_nabati");
                                String eradikasi = object.getString("luas_eradikasi");
                                String cara_lain = object.getString("luas_cara_lain");
                                String luas_pengendalian = object.getString("jumlah_pengendalian");
                                String frek_kimia = object.getString("frek_kimia");
                                String frek_nabati = object.getString("frek_nabati");
                                String sisa_ringan = object.getString("sisa_ringan");
                                String sisa_sedang = object.getString("sisa_sedang");
                                String sisa_berat = object.getString("sisa_berat");
                                String sisa_puso = object.getString("sisa_puso");
                                String status_pelaporan = object.getString("status_pelaporan");
                                String approval_kab = object.getString("approval_kab");
                                String approval_satpel = object.getString("approval_satpel");
                                String approval_provinsi = object.getString("approval_prov");
                                String kab_to_popt = object.getString("kab_to_popt");
                                String satpel_to_popt = object.getString("satpel_to_popt");
                                String satpel_to_kab = object.getString("satpel_to_kab");
                                String prov_to_popt = object.getString("prov_to_popt");
                                String prov_to_kab = object.getString("prov_to_kab");
                                String prov_to_satpel = object.getString("prov_to_satpel");
                                String jumlah_sisa = object.getString("jumlah_sisa");
                                String keadaan_ringan = object.getString("keadaan_ringan");
                                String keadaan_sedang = object.getString("keadaan_sedang");
                                String keadaan_berat = object.getString("keadaan_berat");
                                String keadaan_puso = object.getString("keadaan_puso");
                                String jumlah_tambah = object.getString("jumlah_tambah");
                                jumlah_keadaan = object.getString("jumlah_keadaan");
                                kabupaten = object.getString("kabupaten");
                                kecamatan = object.getString("kecamatan");
                                desa = object.getString("desa");
                                periode_pengamatan = object.getString("periode");
                                opt = object.getString("jenis_opt");
                                luas_waspada = object.getString("luas_waspada");
                                komoditas = object.getString("komoditas");
                                varietas = object.getString("varietas");
                                luas_tanaman = object.getString("luas_tanaman");
                                umur_tanaman = object.getString("umur_tanaman");
                                musim_tanam = object.getString("musim_tanam");
                                tambah_ringan = object.getString("tambah_ringan");
                                tambah_sedang = object.getString("tambah_sedang");
                                tambah_berat = object.getString("tambah_berat");
                                tambah_puso = object.getString("tambah_puso");
                                if (Common.currentUser.getId_usergroup().equals("2")){
                                    status = object.getString("approval_kab");
                                    if (status.equals("Sudah") || status.equals("Validasi")){
                                        msg_to_popt.setVisibility(View.GONE);
                                        btnTolak.setVisibility(View.GONE);
                                        ctt_popt.setVisibility(View.VISIBLE);
                                        ctt_popt.setText(Html.fromHtml("<font color='#000000'><b>Catatan Untuk Popt: </b></font>" + kab_to_popt));
                                    }
                                    msg_kab.setVisibility(View.GONE);
                                    msg_prov.setText(Html.fromHtml("<font color='#000000'><b>BPTPH: </b></font>" + prov_to_kab));
                                    msg_satpel.setText(Html.fromHtml("<font color='#000000'><b>SatPel: </b></font>" + satpel_to_kab));
                                }else if (Common.currentUser.getId_usergroup().equals("3")){
                                    status = object.getString("approval_satpel");
                                    if (status.equals("Sudah") || status.equals("Validasi")){
                                        msg_to_popt.setVisibility(View.GONE);
                                        msg_to_kab.setVisibility(View.GONE);
                                        btnTolak.setVisibility(View.GONE);
                                        ctt_popt.setVisibility(View.VISIBLE);
                                        ctt_kab.setVisibility(View.VISIBLE);
                                        ctt_popt.setText(Html.fromHtml("<font color='#000000'><b>Catatan Untuk Popt: </b></font>" + satpel_to_popt));
                                        ctt_kab.setText(Html.fromHtml("<font color='#000000'><b>Catatan Untuk Kortikab: </b></font>" + satpel_to_kab));
                                    }
                                    msg_kab.setVisibility(View.GONE);
                                    msg_prov.setText(Html.fromHtml("<font color='#000000'><b>BPTPH: </b></font>" + prov_to_satpel));
                                    msg_satpel.setVisibility(View.GONE);
                                }else if (Common.currentUser.getId_usergroup().equals("4")){
                                    status = object.getString("approval_prov");
                                    if (status.equals("Sudah") || status.equals("Validasi")){
                                        msg_to_popt.setVisibility(View.GONE);
                                        msg_to_kab.setVisibility(View.GONE);
                                        msg_to_satpel.setVisibility(View.GONE);
                                        ctt_popt.setVisibility(View.VISIBLE);
                                        btnTolak.setVisibility(View.GONE);
                                        ctt_kab.setVisibility(View.VISIBLE);
                                        ctt_satpel.setVisibility(View.VISIBLE);
                                        ctt_popt.setText(Html.fromHtml("<font color='#000000'><b>Catatan Untuk Popt: </b></font>" + prov_to_popt));
                                        ctt_kab.setText(Html.fromHtml("<font color='#000000'><b>Catatan Untuk Kortikab: </b></font>" + prov_to_kab));
                                        ctt_satpel.setText(Html.fromHtml("<font color='#000000'><b>Catatan Untuk SatPel: </b></font>" + prov_to_satpel));
                                    }
                                    msg_kab.setVisibility(View.GONE);
                                    msg_prov.setVisibility(View.GONE);
                                    msg_satpel.setVisibility(View.GONE);
                                }



                                String bulan = month.format(calendar.getTime());
                                if (periode_pengamatan.equals("Periode 1-15")) {
                                    txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Laporan Setengah Bulan (Tanggal 1- </b></font>" + hari + " " + bulan + ")"));
                                } else {
                                    txtTanggal.setText(Html.fromHtml("<font color='#000000'><b>Laporan Setengah Bulan (Tanggal 16- </b></font>" + hari + " " + bulan + ")"));
                                }
                                txtOPT.setText(Html.fromHtml("<font color='#000000'><b>OPT: </b></font>" + opt));
                                txtLuasWaspada.setText(Html.fromHtml("<font color='#000000'><b>Luas Waspada: </b></font>" + luas_waspada));
                                txtMusim.setText(Html.fromHtml("<font color='#000000'><b>Musim: </b></font>" + musim_tanam));
                                txtTR.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + tambah_ringan));
                                txtTS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + tambah_sedang));
                                txtTB.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + tambah_berat));
                                txtTP.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + tambah_puso));
                                txtJumlahTambah.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + jumlah_tambah));
                                txtSR.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + k_ringan));
                                txtSS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + k_sedang));
                                txtSB.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + k_berat));
                                txtSP.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + k_puso));
                                txtJumlahSisa.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_keadaan_sisa));
                                txtKomoditas.setText(Html.fromHtml("<font color='#000000'><b>Komoditas: </b></font>" + komoditas));
                                txtKecamatan.setText(Html.fromHtml("<font color='#000000'><b>Kec: </b></font>" + kecamatan));
                                txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Desa: </b></font>" + desa));
                                txtVarietas.setText(Html.fromHtml("<font color='#000000'><b>Varietas: </b></font>" + varietas));
                                txtPeriode.setText(Html.fromHtml(periode_pengamatan));
                                txtLuasHamparan.setText(Html.fromHtml("<font color='#000000'><b>Luas Pertanaman: </b></font>" + luas_tanaman + " Ha"));
                                txtUmurTanaman.setText(Html.fromHtml("<font color='#000000'><b>Umur Tanaman: </b></font>" + umur_tanaman + " HST"));
                                txtPelaporan.setText(Html.fromHtml("<font color='#000000'><b>Pelaporan: </b></font>" + status_pelaporan));
                                txtApprovalKab.setText(Html.fromHtml("<font color='#000000'><b>Val Kortikab: </b></font>" + approval_kab));
                                txtApprovalSatpel.setText(Html.fromHtml("<font color='#000000'><b>Val SatPel: </b></font>" + approval_satpel));
                                txtApprovalProv.setText(Html.fromHtml("<font color='#000000'><b>Val BPTPH: </b></font>" + approval_provinsi));
                                sebelumnya.setText(Html.fromHtml("<font color='#000000'><b>Laporan : </b></font>" + periode));
                                sekarang.setText(Html.fromHtml("<font color='#000000'><b>Laporan : </b></font>" + periode_pengamatan));
                                txtLuasPanen.setText(Html.fromHtml("<font color='#000000'><b>L Panen: </b></font>" + panen_sebelumnya));
                                txtLuasSembuh.setText(Html.fromHtml("<font color='#000000'><b>L Sembuh: </b></font>" + sembuh_sebelumnya));
                                txtSRS.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + s_ringan));
                                txtSSS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + s_sedang));
                                txtSBS.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + s_berat));
                                txtSPS.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + s_puso));
                                txtJumlahSisaS.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_sisa_sebelumnya));
                                txtTRS.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + t_ringan));
                                txtTSS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + t_sedang));
                                txtTBS.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + t_berat));
                                txtTPS.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + t_puso));
                                txtJumlahTambahS.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_tambah_sebelumnya));
                                txtKimia.setText(Html.fromHtml("<font color='#000000'><b>K: </b></font>" + kimia_seb));
                                txtNabati.setText(Html.fromHtml("<font color='#000000'><b>N: </b></font>" + nabati_seb));
                                txtEradikasi.setText(Html.fromHtml("<font color='#000000'><b>E: </b></font>" + eradikasi_seb));
                                txtCaraLain.setText(Html.fromHtml("<font color='#000000'><b>CL: </b></font>" + cara_lain_seb));
                                txtJumlahPengendalian.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_pengendalian_seb));
                                txtFrekKimia.setText(Html.fromHtml("<font color='#000000'><b>Frek Kimia: </b></font>" + frek_kimia_seb));
                                txtFrekNabati.setText(Html.fromHtml("<font color='#000000'><b>Frek Nabati: </b></font>" + frek_nabati_seb));
                                txtKR.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + keadaan_ringan));
                                txtKS.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + keadaan_sedang));
                                txtKB.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + keadaan_berat));
                                txtKP.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + keadaan_puso));
                                txtJumlahKeadaan.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + jumlah_keadaan));
                                txtSRN.setText(Html.fromHtml("<font color='#000000'><b>R: </b></font>" + sisa_ringan));
                                txtSSN.setText(Html.fromHtml("<font color='#000000'><b>S: </b></font>" + sisa_sedang));
                                txtSBN.setText(Html.fromHtml("<font color='#000000'><b>B: </b></font>" + sisa_berat));
                                txtSPN.setText(Html.fromHtml("<font color='#000000'><b>P: </b></font>" + sisa_puso));
                                txtJumlahSisaN.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + jumlah_sisa));
                                txtKimiaN.setText(Html.fromHtml("<font color='#000000'><b>K: </b></font>" + kimia));
                                txtNabatiN.setText(Html.fromHtml("<font color='#000000'><b>N: </b></font>" + nabati));
                                txtEradikasiN.setText(Html.fromHtml("<font color='#000000'><b>E: </b></font>" + eradikasi));
                                txtCaraLainN.setText(Html.fromHtml("<font color='#000000'><b>CL: </b></font>" + cara_lain));
                                txtJumlahPengendalianN.setText(Html.fromHtml("<font color='#000000'><b>J: </b></font>" + luas_pengendalian));
                                txtFrekKimiaN.setText(Html.fromHtml("<font color='#000000'><b>Frek Kimia: </b></font>" + frek_kimia));
                                txtFrekNabatiN.setText(Html.fromHtml("<font color='#000000'><b>Frek Nabati: </b></font>" + frek_nabati));
                                txtLuasPanenN.setText(Html.fromHtml("<font color='#000000'><b>L Panen: </b></font>" + luas_panen));
                                txtLuasSembuhN.setText(Html.fromHtml("<font color='#000000'><b>L Sembuh: </b></font>" + luas_sembuh));

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        });
        Volley.newRequestQueue(DetailLaporan.this).add(stringRequest);
    }

    private void updateStatusAppSatpel() {
        progressDialog.setTitle("Approve Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        final String satpel_to_popt = msg_to_popt.getText().toString();
        final String satpel_to_kab = msg_to_kab.getText().toString();
        String url = Constants.ROOT_URL+"Approvalsat";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(DetailLaporan.this, "Data Berhasil ditolak", Toast.LENGTH_SHORT).show();
//                            Intent home = new Intent(DetailLaporan.this, Home.class);
//                            home.putExtra("HOME", "homesatpel");
//                            startActivity(home);
                            finish();
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
        RequestQueue requestQueue = Volley.newRequestQueue(DetailLaporan.this);
        requestQueue.add(request);
    }

    private void updateStatusApproval() {
        progressDialog.setTitle("Approve Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        final String kab_to_popt = msg_to_popt.getText().toString();
        String url = Constants.ROOT_URL+"Approval";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(DetailLaporan.this, "Data Berhasil ditolak", Toast.LENGTH_SHORT).show();
//                            Intent home = new Intent(DetailLaporan.this, Home.class);
//                            home.putExtra("HOME", "homepkab");
//                            startActivity(home);
                            finish();
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
        RequestQueue requestQueue = Volley.newRequestQueue(DetailLaporan.this);
        requestQueue.add(request);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

//    private void buildRecyclerViewDataDesa() {
//        recyclerView1 = (RecyclerView) findViewById(R.id.rvList);
//        recyclerView1.setHasFixedSize(true);
//        layoutManager1 = new LinearLayoutManager(DetailLaporan.this);
//        recyclerView1.setLayoutManager(layoutManager1);
//        listDataDesa = new ArrayList<>();
//
//        getDataDesa();
//    }
//
//    private void getDataDesa() {
//        String url = Constants.ROOT_URL+"desa?id_lap="+id_lapor;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray array = new JSONArray(response);
//                            for (int i = 0; i<array.length(); i++){
//                                JSONObject object = array.getJSONObject(i);
//
//                                String desa = object.getString("desa");
//                                String id = object.getString("id_laporan");
//
//                                DesaModel desaModel = new DesaModel();
//                                desaModel.setId_laporan(id);
//                                desaModel.setDesa(desa);
//                                listDataDesa.add(desaModel);
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                        madapter = new DataDesaAdapter(DetailLaporan.this, listDataDesa);
//                        recyclerView1.setAdapter(madapter);
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(DetailLaporan.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        Volley.newRequestQueue(DetailLaporan.this).add(stringRequest);
//    }

//    private void dialog_action() {
//        mSignaturePad = (SignaturePad) dialog.findViewById(R.id.signature_pad);
//        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
//            @Override
//            public void onStartSigning() {
//                Toast.makeText(DetailLaporan.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSigned() {
//                mSaveButton.setEnabled(true);
//                mClearButton.setEnabled(true);
//            }
//
//            @Override
//            public void onClear() {
//                mSaveButton.setEnabled(false);
//                mClearButton.setEnabled(false);
//            }
//        });
//
//        mClearButton = (Button) dialog.findViewById(R.id.clear_button);
//        mSaveButton = (Button) dialog.findViewById(R.id.save_button);
//        mCancelButton = (Button) dialog.findViewById(R.id.cancel_button);
//
//        mClearButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSignaturePad.clear();
//                signatureBitmap = null;
//                img_sig.setImageDrawable(null);
//            }
//        });
//
//        mSaveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                signatureBitmap = mSignaturePad.getSignatureBitmap();
//                img_sig.setImageBitmap(signatureBitmap);
////                try {
////                    saveBitmapSignToJPG(signatureBitmap);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////                if (addJpgSignatureToGallery(signatureBitmap)) {
////                    Toast.makeText(InputLapor.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
////                } else {
////                    Toast.makeText(InputLapor.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
////                }
////                if (addSvgSignatureToGallery(mSignaturePad.getSignatureSvg())) {
////                    Toast.makeText(InputLapor.this, "SVG Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
////                } else {
////                    Toast.makeText(InputLapor.this, "Unable to store the SVG signature", Toast.LENGTH_SHORT).show();
////                }
//            }
//        });
//
//        mCancelButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Log.v("log_tag", "Panel Canceled");
//                if (img_sig.equals(null)){
//                    mSignaturePad.clear();
//                    dialog.dismiss();
//                }else{
//                    dialog.dismiss();
//
//                }
//            }
//        });
//        dialog.show();
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
