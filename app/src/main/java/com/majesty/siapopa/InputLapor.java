package com.majesty.siapopa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.majesty.siapopa.adapter.DataDesaAdapter;
import com.majesty.siapopa.adapter.DesaAdapter;
import com.majesty.siapopa.model.DesaModel;
import com.majesty.siapopa.model.ModelLapor;
import com.majesty.siapopa.model.WilayahModel;
import com.rengwuxian.materialedittext.MaterialEditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InputLapor extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MaterialEditText edtDesaKecamatan, edtUmurTanaman, edtLuasSerangan, edtIntensitas, edtPopulasi, edtLuasWaspada, edtRekomendasi, edtKeterangan;
    Spinner spTanaman, spOPT, spDesa;
    ArrayList<String> jenis_tanaman = new ArrayList<>();
    ArrayList<String> jenis_opt = new ArrayList<>();
    ArrayAdapter<String> tanamanAdapter;
    ArrayAdapter<String> optAdapter;
    RequestQueue requestQueue;
    Button btnSignature;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePad;
    Button mClearButton;
    Button mSaveButton;
    Button mCancelButton;
    Button btnSelectPDF;
    Button btnLapor;
    ImageView img_sig;
    Dialog dialog;
    Bitmap signatureBitmap;
    TextView txtBukti, txtLat, txtLong, txtAddress, txtCountry, txtNationality, txtSurat, txtDokumen;
    FusedLocationProviderClient fusedLocationProviderClient;
    private int REQ_PDF = 21;
    String encodedPDF;
    String encodedSignature;
    String userAlamat;
    ProgressDialog progressDialog;
    RecyclerView recyclerView, recyclerView1;
    RecyclerView.LayoutManager layoutManager, layoutManager1;
    List<WilayahModel> listDesa;
    List<DesaModel> listDataDesa;
    private RecyclerView.Adapter adapter, madapter;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_lapor);

        requestQueue = Volley.newRequestQueue(this);

//        edtDesaKecamatan = (MaterialEditText) findViewById(R.id.edtLokasi);
//        edtUmurTanaman = (MaterialEditText) findViewById(R.id.edtUmurTanaman);
        edtLuasSerangan = (MaterialEditText) findViewById(R.id.edtLuasSerangan);
        edtIntensitas = (MaterialEditText) findViewById(R.id.edtIntensitas);
        edtLuasWaspada = (MaterialEditText) findViewById(R.id.edtLuasWaspada);
        spTanaman = (Spinner) findViewById(R.id.tanaman);
        spOPT = (Spinner) findViewById(R.id.opt);
        btnSignature = (Button) findViewById(R.id.signature);
        btnSelectPDF = (Button) findViewById(R.id.btnSelect);
        btnLapor = (Button) findViewById(R.id.btnLapor);
        img_sig = (ImageView) findViewById(R.id.img_sig);
//        txtBukti = (TextView) findViewById(R.id.txtBuktiFoto);
        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLong = (TextView) findViewById(R.id.txtLong);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtSurat = (TextView) findViewById(R.id.txtSurat);
        txtDokumen = (TextView) findViewById(R.id.txtPDF);
        progressDialog = new ProgressDialog(this);
        calendar = Calendar.getInstance();

        buildRecyclerViewDesa();
        buildRecyclerViewDataDesa();
//        selectSum();

        btnLapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                insertData();
            }
        });

        btnSelectPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePDF();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        dialog = new Dialog(InputLapor.this);
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

        String url = Constants.ROOT_URL+"PopulateTanaman";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("jenis_tanaman");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String tanaman = jsonObject.optString("tanaman");
                        jenis_tanaman.add(tanaman);
                        tanamanAdapter = new ArrayAdapter<>(InputLapor.this, android.R.layout.simple_dropdown_item_1line, jenis_tanaman);
                        tanamanAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spTanaman.setAdapter(tanamanAdapter);
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
        spTanaman.setOnItemSelectedListener(this);
    }

    private void buildRecyclerViewDesa() {
        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(InputLapor.this);
        recyclerView.setLayoutManager(layoutManager);
        listDesa = new ArrayList<>();

        getDesa();
    }

    private void buildRecyclerViewDataDesa() {
        recyclerView1 = (RecyclerView) findViewById(R.id.rvData);
        recyclerView1.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(InputLapor.this);
        recyclerView1.setLayoutManager(layoutManager1);
        listDataDesa = new ArrayList<>();

        getDataDesa();
    }

    private void getDataDesa() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        userAlamat = Common.currentUser.getAlamat();
        String url = Constants.ROOT_URL+"desa?kecamatan="+userAlamat+"&date="+date+"&id_lap=0";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String desa = object.getString("desa");
                                String id = object.getString("id_laporan");

                                DesaModel desaModel = new DesaModel();
                                desaModel.setId_laporan(id);
                                desaModel.setDesa(desa);
                                listDataDesa.add(desaModel);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        madapter = new DataDesaAdapter(InputLapor.this, listDataDesa);
                        recyclerView1.setAdapter(madapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(InputLapor.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(InputLapor.this).add(stringRequest);
    }

    private void getDesa(){
        userAlamat = Common.currentUser.getAlamat();
        String url = Constants.ROOT_URL+"wilayah?lokasi="+userAlamat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String desa = object.getString("desa");
                                String id = object.getString("id_lokasi");

                                WilayahModel wilayahModel = new WilayahModel();
                                wilayahModel.setId_lokasi(id);
                                wilayahModel.setDesa(desa);
                                listDesa.add(wilayahModel);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        adapter = new DesaAdapter(InputLapor.this, listDesa);
                        recyclerView.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(InputLapor.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(InputLapor.this).add(stringRequest);
    }

//    private void insertData() {
//        progressDialog.setTitle("Insert Data");
//        progressDialog.setMessage("Mohon Tunggu Sebentar");
//        progressDialog.show();
//        String url = Constants.ROOT_URL+"ci_app/lengkap";
//        final String kecamatan = Common.currentUser.getAlamat();
//        final String provinsi = Common.currentUser.getProvinsi();
//        final String kabupaten = Common.currentUser.getKabupaten();
//        final String varietas = spTanaman.getSelectedItem().toString().trim();
//        final String opt = spOPT.getSelectedItem().toString().trim();
//        final String total_luas = Common.sum.getLuas_tanaman();
//        final String umur_rata = Common.sum.getDari_umur()+"-"+Common.sum.getHingga_umur();
//        final String total_sembuh = Common.sum.getLuas_sembuh();
//        final String total_panen = Common.sum.getLuas_panen();
//        final String total_serangan_ringan = Common.sum.getJum_serang_ringan();
//        final String total_serangan_sedang = Common.sum.getJum_serang_sedang();
//        final String total_serangan_berat = Common.sum.getJum_serang_berat();
//        final String total_serangan_puso = Common.sum.getJum_serang_puso();
//        final String jum_total_serangan = Common.sum.getLuas_serangan();
//        final String total_tambah_ringan = Common.sum.getJum_tambah_ringan();
//        final String total_tambah_sedang = Common.sum.getJum_tambah_sedang();
//        final String total_tambah_berat = Common.sum.getJum_tambah_berat();
//        final String total_tambah_puso = Common.sum.getJum_tambah_puso();
//        final String jum_total_tambah = Common.sum.getLuas_tambah();
//        final String total_keadaan_ringan = Common.sum.getJum_keadaan_ringan();
//        final String total_keadaan_sedang = Common.sum.getJum_keadaan_sedang();
//        final String total_keadaan_berat = Common.sum.getJum_keadaan_berat();
//        final String total_keadaan_puso = Common.sum.getJum_keadaan_puso();
//        final String jum_total_keadaan = Common.sum.getLuas_keadaan();
//        final String pm = Common.sum.getJum_pm();
//        final String pestisida = "Pestisida";
//        final String total_kimia = Common.sum.getJum_kimia();
//        final String total_nabati = Common.sum.getJum_nabati();
//        final String non_pestisida = "Non Pestisida";
//        final String total_AH = Common.sum.getJum_AH();
//        final String total_CL = Common.sum.getJum_CL();
//        final String jum_total_kendali = Common.sum.getJum_total_kendali();
//        final String total_frek_kimia = Common.sum.getFrek_kimia();
//        final String total_frek_nabati = Common.sum.getFrek_nabati();
//        final String total_luas_waspada = Common.sum.getLuas_waspada();
//        final String approval_kab = "Belum";
//        final String approval_satpel = "Belum";
//        StringRequest request = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (response.contains("berhasil")){
////                            progressDialog.dismiss();
////                            Toast.makeText(InputLapor.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
//                            getLapLengkapID();
////                            Intent home = new Intent(InputLapor.this, Home.class);
////                            home.putExtra("HOME", "homepopt");
////                            startActivity(home);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(InputLapor.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("provinsi", provinsi);
//                params.put("kabupaten", kabupaten);
//                params.put("kecamatan", kecamatan);
//                params.put("varietas", varietas);
//                params.put("opt", opt);
//                params.put("total_luas", total_luas);
//                params.put("umur_rata", umur_rata);
//                params.put("total_sembuh", total_sembuh);
//                params.put("total_panen", total_panen);
//                params.put("total_serangan_ringan", total_serangan_ringan);
//                params.put("total_serangan_sedang", total_serangan_sedang);
//                params.put("total_serangan_berat", total_serangan_berat);
//                params.put("total_serangan_puso", total_serangan_puso);
//                params.put("jum_total_serangan", jum_total_serangan);
//                params.put("total_tambah_ringan", total_tambah_ringan);
//                params.put("total_tambah_sedang", total_tambah_sedang);
//                params.put("total_tambah_berat", total_tambah_berat);
//                params.put("total_tambah_puso", total_tambah_puso);
//                params.put("jum_total_tambah", jum_total_tambah);
//                params.put("total_keadaan_ringan", total_keadaan_ringan);
//                params.put("total_keadaan_sedang", total_keadaan_sedang);
//                params.put("total_keadaan_berat", total_keadaan_berat);
//                params.put("total_keadaan_puso", total_keadaan_puso);
//                params.put("jum_total_keadaan", jum_total_keadaan);
//                params.put("pm", pm);
//                params.put("pestisida", pestisida);
//                params.put("total_kimia", total_kimia);
//                params.put("total_nabati", total_nabati);
//                params.put("non_pestisida", non_pestisida);
//                params.put("total_AH", total_AH);
//                params.put("total_CL", total_CL);
//                params.put("jum_total_kendali", jum_total_kendali);
//                params.put("total_frek_kimia", total_frek_kimia);
//                params.put("total_frek_nabati", total_frek_nabati);
//                params.put("total_luas_waspada", total_luas_waspada);
//                params.put("approval_kab", approval_kab);
//                params.put("approval_satpel", approval_satpel);
//                params.put("suratRekomendasi", encodedPDF);
//                params.put("signature", encodedSignature);
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(InputLapor.this);
//        requestQueue.add(request);
//    }

//    private void updateLapDesa() {
//        final String id = Common.sum.getId_laporan();
//        String url = Constants.ROOT_URL+"siap_opah/UpdateID.php";
//        final String lap_lengkap_id = Common.lapor.getId();
//        StringRequest request = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (response.contains("berhasil")){
//                            progressDialog.dismiss();
//                            Toast.makeText(InputLapor.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
//                            Intent home = new Intent(InputLapor.this, Home.class);
//                            home.putExtra("HOME", "homepopt");
//                            startActivity(home);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(InputLapor.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("lap_lengkap_id", lap_lengkap_id);
//                params.put("id_laporan", id);
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(InputLapor.this);
//        requestQueue.add(request);
//    }

    private void getLapLengkapID() {
        userAlamat = Common.currentUser.getAlamat();
        String varietas = spTanaman.getSelectedItem().toString();
        String opt = spOPT.getSelectedItem().toString();
        String url = Constants.ROOT_URL+"ci_app/lengkap?kecamatan="+userAlamat+"&varietas="+varietas+"&opt="+opt+"&date="+date;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String id = object.getString("id");

                                ModelLapor modelLapor = new ModelLapor();
                                modelLapor.setId(id);
                                Common.lapor = modelLapor;
//                                updateLapDesa();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InputLapor.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(InputLapor.this).add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_PDF && resultCode == RESULT_OK && data != null){

            Uri path = data.getData();


            try {
                InputStream inputStream = InputLapor.this.getContentResolver().openInputStream(path);
                byte[] pdfInBytes = new byte[inputStream.available()];
                inputStream.read(pdfInBytes);
                encodedPDF = android.util.Base64.encodeToString(pdfInBytes, Base64.DEFAULT);

                txtDokumen.setText("Document Selected");
                txtDokumen.setTextColor(Color.BLACK);
                btnSelectPDF.setText("Change Document");

                Toast.makeText(this, "Document Selected", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void saveBitmapSignToJPG(Bitmap bitmap) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] imageBytes = stream.toByteArray();

        encodedSignature = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


    private void choosePDF() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("application/pdf");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, REQ_PDF);
    }

    private void dialog_action() {
        mSignaturePad = (SignaturePad) dialog.findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(InputLapor.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.tanaman){
            jenis_opt.clear();
            String selectedTanaman = parent.getSelectedItem().toString();
            String url = Constants.ROOT_URL+"siap_opah/PopulateDesa?tanaman="+selectedTanaman;
            requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("jenis_opt");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String opt = jsonObject.optString("opt");
                                    jenis_opt.add(opt);
                                    optAdapter = new ArrayAdapter<>(InputLapor.this, android.R.layout.simple_dropdown_item_1line, jenis_opt);
                                    optAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
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
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(InputLapor.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

//    public File getAlbumStorageDir(String albumName) {
//        // Get the directory for the user's public pictures directory.
//        File file = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), albumName);
//        if (!file.mkdirs()) {
//            Log.e("SignaturePad", "Directory not created");
//        }
//        return file;
//    }


//    public boolean addJpgSignatureToGallery(Bitmap signature) {
//        boolean result = false;
//        try {
//            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
//            saveBitmapSignToJPG(signature);
//            scanMediaFile(photo);
//            result = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

//    private void scanMediaFile(File photo) {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri contentUri = Uri.fromFile(photo);
//        mediaScanIntent.setData(contentUri);
//        InputLapor.this.sendBroadcast(mediaScanIntent);
//    }

//    public boolean addSvgSignatureToGallery(String signatureSvg) {
//        boolean result = false;
//        try {
//            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.svg", System.currentTimeMillis()));
//            OutputStream stream = new FileOutputStream(svgFile);
//            OutputStreamWriter writer = new OutputStreamWriter(stream);
//            writer.write(signatureSvg);
//            writer.close();
//            stream.flush();
//            stream.close();
//            scanMediaFile(svgFile);
//            result = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

//    /**
//     * Checks if the app has permission to write to device storage
//     * <p/>
//     * If the app does not has permission then the user will be prompted to grant permissions
//     *
//     * @param activity the activity from which permissions are checked
//     */
//    public static void verifyStoragePermissions(Activity activity) {
//        // Check if we have write permission
//        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }
//    }

//    private void selectSum() {
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        date = dateFormat.format(calendar.getTime());
//        userAlamat = Common.currentUser.getAlamat();
//        String url = Constants.ROOT_URL+"ci_app/sum?kecamatan="+userAlamat+"&date="+date+"&id=0";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray array = new JSONArray(response);
//                            for (int i = 0; i<array.length(); i++){
//                                JSONObject object = array.getJSONObject(i);
//
//                                String id_laporan = object.getString("id_laporan");
//                                String dari_umur = object.getString("dari_umur");
//                                String hingga_umur = object.getString("hingga_umur");
//                                String luas_tanaman = object.getString("luas_tanaman");
//                                String luas_serangan = object.getString("luas_serangan");
//                                String jum_serang_ringan = object.getString("jum_serang_ringan");
//                                String jum_serang_sedang = object.getString("jum_serang_sedang");
//                                String jum_serang_berat = object.getString("jum_serang_berat");
//                                String jum_serang_puso = object.getString("jum_serang_puso");
//                                String luas_sembuh = object.getString("luas_sembuh");
//                                String luas_panen = object.getString("luas_panen");
//                                String luas_tambah = object.getString("luas_tambah");
//                                String jum_tambah_ringan = object.getString("jum_tambah_ringan");
//                                String jum_tambah_sedang = object.getString("jum_tambah_sedang");
//                                String jum_tambah_berat = object.getString("jum_tambah_berat");
//                                String jum_tambah_puso = object.getString("jum_tambah_puso");
//                                String jum_kimia = object.getString("jum_kimia");
//                                String jum_nabati = object.getString("jum_nabati");
//                                String jum_AH = object.getString("jum_AH");
//                                String jum_CL = object.getString("jum_CL");
//                                String luas_keadaan = object.getString("luas_keadaan");
//                                String jum_keadaan_ringan = object.getString("jum_keadaan_ringan");
//                                String jum_keadaan_sedang = object.getString("jum_keadaan_sedang");
//                                String jum_keadaan_berat = object.getString("jum_keadaan_berat");
//                                String jum_keadaan_puso = object.getString("jum_keadaan_puso");
//                                String frek_kimia = object.getString("frek_kimia");
//                                String frek_nabati = object.getString("frek_nabati");
//                                String luas_waspada = object.getString("luas_waspada");
//                                String pm = object.getString("pm");
//                                String jum_luas_pengendalian = object.getString("jum_luas_pengendalian");
//
//                                SumModel sumModel = new SumModel();
//                                sumModel.setId_laporan(id_laporan);
//                                sumModel.setDari_umur(dari_umur);
//                                sumModel.setHingga_umur(hingga_umur);
//                                sumModel.setLuas_tanaman(luas_tanaman);
//                                sumModel.setLuas_serangan(luas_serangan);
//                                sumModel.setJum_serang_ringan(jum_serang_ringan);
//                                sumModel.setJum_serang_sedang(jum_serang_sedang);
//                                sumModel.setJum_serang_berat(jum_serang_berat);
//                                sumModel.setJum_serang_puso(jum_serang_puso);
//                                sumModel.setLuas_sembuh(luas_sembuh);
//                                sumModel.setLuas_panen(luas_panen);
//                                sumModel.setLuas_tambah(luas_tambah);
//                                sumModel.setJum_tambah_ringan(jum_tambah_ringan);
//                                sumModel.setJum_tambah_sedang(jum_tambah_sedang);
//                                sumModel.setJum_tambah_berat(jum_tambah_berat);
//                                sumModel.setJum_tambah_puso(jum_tambah_puso);
//                                sumModel.setJum_kimia(jum_kimia);
//                                sumModel.setJum_nabati(jum_nabati);
//                                sumModel.setJum_AH(jum_AH);
//                                sumModel.setJum_CL(jum_CL);
//                                sumModel.setLuas_keadaan(luas_keadaan);
//                                sumModel.setJum_keadaan_ringan(jum_keadaan_ringan);
//                                sumModel.setJum_keadaan_berat(jum_keadaan_berat);
//                                sumModel.setJum_keadaan_sedang(jum_keadaan_sedang);
//                                sumModel.setJum_keadaan_puso(jum_keadaan_puso);
//                                sumModel.setFrek_kimia(frek_kimia);
//                                sumModel.setFrek_nabati(frek_nabati);
//                                sumModel.setLuas_waspada(luas_waspada);
//                                sumModel.setJum_pm(pm);
//                                sumModel.setJum_total_kendali(jum_luas_pengendalian);
//                                Common.sum = sumModel;
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(InputLapor.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        Volley.newRequestQueue(InputLapor.this).add(stringRequest);
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
