package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreatePdf extends AppCompatActivity {

    MaterialEditText edtMantan, edtTempatMantan, edtKoorPOPT, edtTempatKoor, edtTanggalAwal,
            edtTanggalAkhir, edtBulan, edtTahun, edtNamaPOPT, edtNIP, edtLokasi, edtBPTPHP;
    Button btn_create, btnSignature;
    ImageView img_sig;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    String date;
    private SignaturePad mSignaturePad;
    Button mClearButton;
    Button mSaveButton;
    Button mCancelButton;
    Dialog dialog;
    Bitmap signatureBitmap;
    String encodedSignature;
    Bitmap newBitmap, scaledBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pdf);
//        edtMantan = (MaterialEditText) findViewById(R.id.edtMantan);
        edtTempatMantan = (MaterialEditText) findViewById(R.id.edtTempatMantan);
        edtKoorPOPT = (MaterialEditText) findViewById(R.id.edtKoorPOPT);
        edtTempatKoor = (MaterialEditText) findViewById(R.id.edtTempatKoor);
        edtTanggalAwal = (MaterialEditText) findViewById(R.id.edtTanggalAwal);
        edtTanggalAkhir = (MaterialEditText) findViewById(R.id.edtTanggalAkhir);
        edtBulan = (MaterialEditText) findViewById(R.id.edtBulan);
        edtTahun = (MaterialEditText) findViewById(R.id.edtTahun);
        edtNamaPOPT = (MaterialEditText) findViewById(R.id.edtNamaPOPT);
        edtNIP = (MaterialEditText) findViewById(R.id.edtNIP);
        edtLokasi = (MaterialEditText) findViewById(R.id.edtLokasi);
        edtBPTPHP = (MaterialEditText) findViewById(R.id.edtBPTPH);
        btn_create = (Button) findViewById(R.id.btn_create);
        btnSignature = (Button) findViewById(R.id.signature);
        img_sig = (ImageView) findViewById(R.id.img_sig);
        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        if (Common.currentUser.getId_usergroup().equals("1")){
            edtBPTPHP.setVisibility(View.GONE);
        }else if (Common.currentUser.getId_usergroup().equals("2")) {
//            edtMantan.setVisibility(View.GONE);
            edtKoorPOPT.setVisibility(View.GONE);
        }else if (Common.currentUser.getId_usergroup().equals("3")) {
//            edtMantan.setVisibility(View.GONE);
            edtKoorPOPT.setVisibility(View.GONE);
        }

        dialog = new Dialog(CreatePdf.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signature);
        dialog.setCancelable(true);

        btnSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_action();
            }
        });

        buildPDF();

    }

    public void saveBitmapSignToJPG(Bitmap bitmap) throws IOException {
        newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] imageBytes = stream.toByteArray();

        encodedSignature = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void dialog_action() {
        mSignaturePad = (SignaturePad) dialog.findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(CreatePdf.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });

        mClearButton = (Button) dialog.findViewById(R.id.clear_button);
        mSaveButton = (Button) dialog.findViewById(R.id.save_button);
        mCancelButton = (Button) dialog.findViewById(R.id.cancel_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
                signatureBitmap = null;
                img_sig.setImageDrawable(null);
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                signatureBitmap = mSignaturePad.getSignatureBitmap();
                img_sig.setImageBitmap(signatureBitmap);
                try {
                    saveBitmapSignToJPG(signatureBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                if (addJpgSignatureToGallery(signatureBitmap)) {
//                    Toast.makeText(InputLapor.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(InputLapor.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
//                }
//                if (addSvgSignatureToGallery(mSignaturePad.getSignatureSvg())) {
//                    Toast.makeText(InputLapor.this, "SVG Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(InputLapor.this, "Unable to store the SVG signature", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                if (img_sig.equals(null)){
                    mSignaturePad.clear();
                    dialog.dismiss();
                }else{
                    dialog.dismiss();

                }
            }
        });
        dialog.show();
    }

    private void buildPDF() {
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();

                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(500, 650, 1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                Canvas canvas = page.getCanvas();
                if (Common.currentUser.getId_usergroup().equals("1")){
                    edtBPTPHP.setVisibility(View.GONE);
                    signatureBitmap = mSignaturePad.getSignatureBitmap();
                    scaledBitmap = Bitmap.createScaledBitmap(signatureBitmap, 80, 80, false);
                    paint.setTextSize(12);
                    canvas.drawText("Hal    : Laporan Setengah Bulanan/Bulanan dari POPT-PHP", 40, 50, paint);
                    canvas.drawText("Sifat  : Penting", 40, 65, paint);
                    canvas.drawText("Yth.", 40, 100, paint);
                    canvas.drawText("1. Bp/Ibu/Sdr.Koordinator POPT-PHP "+edtKoorPOPT.getText().toString(), 40, 160, paint);
                    canvas.drawText("   di", 40, 175, paint);
                    canvas.drawText("     "+edtTempatKoor.getText().toString(), 40, 190, paint);
                    canvas.drawText("       Dengan ini kami laporkan kepada Bapak/Ibu/Sdr. hasil pengamatan", 40, 220, paint);
                    canvas.drawText("tetap dan keliling selama periode pengamatan tanggal "+edtTanggalAwal.getText().toString()+" s.d. tanggal "+edtTanggalAkhir.getText().toString(), 40, 235, paint);
                    canvas.drawText("bulan "+edtBulan.getText().toString()+" tahun "+edtTahun.getText().toString()+", seperti terlampir.", 40, 250, paint);
                    canvas.drawText("       Atas perhatian Bapak/Ibu/Saudara kami ucapkan terima kasih.", 40, 270, paint);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(edtLokasi.getText().toString()+", "+ date, 420, 300, paint);
                    canvas.drawText("POPT-PHP", 395, 320, paint);
                    canvas.drawBitmap(scaledBitmap, 335, 325, paint);
                    canvas.drawText(edtNamaPOPT.getText().toString(), 400, 400, paint);
                    canvas.drawText("NIP."+edtNIP.getText().toString(), 400, 415, paint);
                    pdfDocument.finishPage(page);
                }else if (Common.currentUser.getId_usergroup().equals("2")){
                    edtMantan.setVisibility(View.GONE);
                    edtKoorPOPT.setVisibility(View.GONE);
                    signatureBitmap = mSignaturePad.getSignatureBitmap();
                    scaledBitmap = Bitmap.createScaledBitmap(signatureBitmap, 80, 80, false);
                    paint.setTextSize(12);
                    canvas.drawText("Hal    : Laporan Setengah Bulanan/Bulanan dari POPT-PHP", 40, 50, paint);
                    canvas.drawText("Sifat  : Penting", 40, 65, paint);
                    canvas.drawText("Yth.", 40, 100, paint);
                    canvas.drawText("1. Bp/Ibu Kepala Dinas Pertanian Kabupaten/Kota", 40, 115, paint);
                    canvas.drawText("   di "+edtTempatMantan.getText().toString(), 40, 130, paint);
                    canvas.drawText("2. Bp/Ibu Kepala BPTPH/LPHP/LAH "+edtBPTPHP.getText().toString(), 40, 160, paint);
                    canvas.drawText("   di "+edtTempatKoor.getText().toString(), 40, 175, paint);
                    canvas.drawText("       Dengan ini kami laporkan kepada Bapak/Ibu hasil rekapitulasi luas", 40, 220, paint);
                    canvas.drawText("tambah dan keadaan serangan OPT, luas pengendalian dan bencana alam", 40, 235, paint);
                    canvas.drawText("untuk periode pengamatan "+edtTanggalAwal.getText().toString()+"-"+edtTanggalAkhir.getText().toString()+" bulan "+edtBulan.getText().toString()+" tahun "+edtTahun.getText().toString()+"di wilayah ", 40, 250, paint);
                    canvas.drawText( "Kabupaten/Kota "+edtLokasi.getText().toString(), 40, 265, paint);
                    canvas.drawText("       Atas perhatian Bapak/Ibu/Saudara kami ucapkan terima kasih.", 40, 285, paint);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(edtLokasi.getText().toString()+", "+ date, 420, 310, paint);
                    canvas.drawText("Koordinator POPT-PHP", 410, 330, paint);
                    canvas.drawBitmap(scaledBitmap, 320, 335, paint);
                    canvas.drawText(edtNamaPOPT.getText().toString(), 400, 410, paint);
                    canvas.drawText("NIP."+edtNIP.getText().toString(), 400, 420, paint);
                    pdfDocument.finishPage(page);
                }else if (Common.currentUser.getId_usergroup().equals("3")){
                    edtMantan.setVisibility(View.GONE);
                    edtKoorPOPT.setVisibility(View.GONE);
                    signatureBitmap = mSignaturePad.getSignatureBitmap();
                    scaledBitmap = Bitmap.createScaledBitmap(signatureBitmap, 80, 80, false);
                    paint.setTextSize(12);
                    canvas.drawText("Hal    : Laporan Setengah Bulanan/Bulanan dari LPHP/LAH", 40, 50, paint);
                    canvas.drawText("Sifat  : Penting", 40, 65, paint);
                    canvas.drawText("Yth.", 40, 100, paint);
                    canvas.drawText("1. Bp/Ibu Kepala Dinas Pertanian Kabupaten/Kota", 40, 115, paint);
                    canvas.drawText("   di "+edtTempatMantan.getText().toString(), 40, 130, paint);
                    canvas.drawText("2. Bp/Ibu Kepala BPTPH/LPHP/LAH "+edtBPTPHP.getText().toString(), 40, 160, paint);
                    canvas.drawText("   di "+edtTempatKoor.getText().toString(), 40, 175, paint);
                    canvas.drawText("       Dengan ini kami laporkan kepada Bapak/Ibu hasil rekapitulasi luas", 40, 220, paint);
                    canvas.drawText("tambah dan keadaan serangan OPT, luas pengendalian dan bencana alam,", 40, 235, paint);
                    canvas.drawText("kecenderungan perubahan-perubahan kepadatan populaso OPT berdasarkan", 40, 250, paint);
                    canvas.drawText("pengamatan pada petak dan tangkapan perangkap lampu, keadaan curah", 40, 265, paint);
                    canvas.drawText("hujan dan kasus pestisida yang terjadi di wilayah kerja LPHP/LAH "+edtTempatKoor.getText().toString(), 40, 280, paint);
                    canvas.drawText("selama periode pengamatan "+edtTanggalAwal.getText().toString()+"-"+edtTanggalAkhir.getText().toString()+" bulan "+edtBulan.getText().toString()+" tahun "+edtTahun.getText().toString()+"seperti terlampir", 40, 295, paint);
                    canvas.drawText("       Atas perhatian Bapak/Ibu/Saudara kami ucapkan terima kasih.", 40, 320, paint);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(edtLokasi.getText().toString()+", "+ date, 420, 340, paint);
                    canvas.drawText("Koordinator POPT-PHP", 420, 355, paint);
                    canvas.drawBitmap(scaledBitmap, 320, 360, paint);
                    canvas.drawText(edtNamaPOPT.getText().toString(), 400, 430, paint);
                    canvas.drawText("NIP."+edtNIP.getText().toString(), 400, 440, paint);
                    pdfDocument.finishPage(page);
                }

                File file = new File(Environment.getExternalStorageDirectory(), "/surat pengantar.pdf");

                try {
                    pdfDocument.writeTo(new FileOutputStream(file));
                    Toast.makeText(CreatePdf.this, "berhasil", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                pdfDocument.close();
            }
        });

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
