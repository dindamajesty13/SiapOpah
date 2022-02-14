package com.majesty.siapopa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.JsonIOException;
import com.majesty.siapopa.model.SessionManager;
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
import java.util.HashMap;
import java.util.Map;

public class InputHarian extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    String encodedImage;
    Spinner spDesa, spTanaman, spKecamatan;
    MaterialEditText edtBlok, edtLuas_Hamparan, edtLuas_Diamati, edtLuas_Panen, edtLuas_Persemaian,
            edtPH, edtVarietas, edtDari_Umur, edtHingga_Umur, edtPola_Tanam;
    ArrayList<String> jenis_tanaman = new ArrayList<>();
    ArrayAdapter<String> tanamanAdapter;
    ArrayList<String> jenis_desa = new ArrayList<>();
    ArrayList<String> jenis_kecamatan = new ArrayList<>();
    ArrayAdapter<String> desaAdapter;
    ArrayAdapter<String> kecamatanAdapter;
    RequestQueue requestQueue;
    private static final int MY_LOCATION_PERMISSION_CODE = 99;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePad;
    Button mClearButton;
    Button mSaveButton;
    Button mCancelButton;
    Button btnLapor;
    ImageView img_sig;
    Dialog dialog;
    ImageView img_bukti;
    Bitmap signatureBitmap;
    String encodedSignature;
    String userAlamat;
    String userKab;
    ProgressDialog progressDialog;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private int REQ_PDF = 21;
    String encodedPDF;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double mLatitude;
    private double mLongitude;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_harian);

        requestQueue = Volley.newRequestQueue(this);
//        checkingGPS();
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        context = InputHarian.this;

//        try {
//            locationMode = Settings.Secure.getInt(context.getContentResolver(),Settings.Secure.LOCATION_MODE);
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }

//        if (locationMode != Settings.Secure.LOCATION_MODE_OFF && locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY){
//            Toast.makeText(this, "benar", Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(this, "salah", Toast.LENGTH_SHORT).show();
//            new AlertDialog.Builder(InputHarian.this)
//                    .setMessage("GPS Mode Salah! Aktifkan Mode High Accuracy")
//                    .setPositiveButton("Aktifkan", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(intent);
//                        }
//                    })
//                    .setNegativeButton("Cancel", null)
//                    .show();
//        }


//        btn_add = (ImageView) findViewById(R.id.btn_add);
        spDesa = (Spinner) findViewById(R.id.spDesa);
        spTanaman = (Spinner) findViewById(R.id.spTanaman);
        spKecamatan = (Spinner) findViewById(R.id.spKecamatan);
        edtBlok = (MaterialEditText) findViewById(R.id.edtBlok);
        edtLuas_Hamparan = (MaterialEditText) findViewById(R.id.edtLuas_Hamparan);
        edtLuas_Diamati = (MaterialEditText) findViewById(R.id.edtLuas_Diamati);
        edtLuas_Panen = (MaterialEditText) findViewById(R.id.edtLuas_Panen);
        edtPH = (MaterialEditText) findViewById(R.id.edt_ph);
        edtVarietas = (MaterialEditText) findViewById(R.id.edtVarietas);
        edtDari_Umur = (MaterialEditText) findViewById(R.id.edtUmurAwal);
        edtHingga_Umur = (MaterialEditText) findViewById(R.id.edtUmurAkhir);
        edtPola_Tanam = (MaterialEditText) findViewById(R.id.edtPola_Tanam);
        edtLuas_Persemaian = (MaterialEditText) findViewById(R.id.edtLuas_Persemaian);
        btnLapor = (Button) findViewById(R.id.btnLapor);
//        btnSignature = (Button) findViewById(R.id.signature);
//        img_sig = (ImageView) findViewById(R.id.img_sig);
        progressDialog = new ProgressDialog(this);
        img_bukti = (ImageView) findViewById(R.id.buktiFoto);
        calendar = Calendar.getInstance();

//        btn_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent detailIntent = new Intent(InputHarian.this, InputDetailHarian.class);
//                int jumlah = listDetail.size() + 1;
//                detailIntent.putExtra("jumlah", String.valueOf(jumlah));
//                startActivity(detailIntent);
//                finish();
//            }
//        });

//        buildRecyclerViewDataHarian();

        dialog = new Dialog(InputHarian.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signature);
        dialog.setCancelable(true);

//        btnSignature.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog_action();
//            }
//        });

        String url = Constants.ROOT_URL + "Populate_Tanaman";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("jenis_tanaman");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String tanaman = jsonObject.optString("tanaman");
                        jenis_tanaman.add(tanaman);
                        tanamanAdapter = new ArrayAdapter<>(InputHarian.this, android.R.layout.simple_dropdown_item_1line, jenis_tanaman);
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


        userAlamat = Common.currentUser.getAlamat();
        userKab = Common.currentUser.getKabupaten();
        String url1 = Constants.ROOT_URL + "Populate_Kecamatan?kecamatan=" + userAlamat + "&kabupaten=" + userKab;

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("lokasi");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String kecamatan = jsonObject.optString("kecamatan");
                        jenis_kecamatan.add(kecamatan);
                        kecamatanAdapter = new ArrayAdapter<>(InputHarian.this, android.R.layout.simple_dropdown_item_1line, jenis_kecamatan);
                        kecamatanAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spKecamatan.setAdapter(kecamatanAdapter);
                        spKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String selectedKec = spKecamatan.getSelectedItem().toString();
                                jenis_desa.clear();
                                String url2 = Constants.ROOT_URL + "Populate_Desa?kecamatan=" + selectedKec + "&kabupaten=" + userKab;
                                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray jsonArray = response.getJSONArray("lokasi");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String desa = jsonObject.optString("desa");
                                                jenis_desa.add(desa);
                                                desaAdapter = new ArrayAdapter<>(InputHarian.this, android.R.layout.simple_dropdown_item_1line, jenis_desa);
                                                desaAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
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
                                requestQueue.add(jsonObjectRequest2);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
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

        btnLapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getLatLong();
//                int totalData = Integer.parseInt(Common.sum.getTotal_data());
                if (TextUtils.isEmpty(edtLuas_Hamparan.getText().toString()) || TextUtils.isEmpty(edtLuas_Diamati.getText().toString()) || TextUtils.isEmpty(edtVarietas.getText().toString())){
                    edtLuas_Hamparan.setError("Tidak Boleh Kosong");
                    edtVarietas.setError("Tidak Boleh Kosong");
                    edtLuas_Diamati.setError("Tidak Boleh Kosong");
                    Toast.makeText(InputHarian.this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else if (img_bukti.getDrawable() == null){
                    Toast.makeText(InputHarian.this, "Bukti Foto Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(InputHarian.this, mLatitude+", "+ mLongitude, Toast.LENGTH_SHORT).show();
//                    getLatLong();
                    insertData();

                }
//                if (listDetail.size() == 30){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(InputHarian.this);
//                    builder.setTitle("Insert Data");
//                    builder.setMessage("Apakah Anda Yakin?");
//                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // TODO: Do something, when user click on the positive button
//
//
//                        }
//                    });
//                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override public void onClick(DialogInterface dialog, int which) {
//                            // TODO: Do something, when user click on the positive button
//                            dialog.cancel();
//                        }
//                    });
//                    builder.create().show();
//
//                }else if (listDetail.size() < 30){
////                    Toast.makeText(InputHarian.this, "Data Tunas Kurang Dari 30", Toast.LENGTH_SHORT).show();
//                    AlertDialog.Builder builder = new AlertDialog.Builder(InputHarian.this);
//                    builder.setTitle("Data Kurang Dari 30");
//                    builder.setMessage("Mohon Inputkan Tambahan Data!");
//                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // TODO: Do something, when user click on the positive button
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.create().show();
//                }

            }
        });

        img_bukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
//                checkGPS();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }


    public void stopLocationUpdates()
    {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
            Toast.makeText(this, "Please install Google Play services.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 9000)
                        .show();
            } else
                finish();

            return false;
        }
        return true;
    }


    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Enable Permissions", Toast.LENGTH_LONG).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);


    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder() .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    InputHarian.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });

        //mLocationRequest.setSmallestDisplacement(0.1F);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null){
            mLatitude = mLastLocation.getLatitude();
            mLongitude = mLastLocation.getLongitude();
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }else {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        //no need to do a null check here:
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();

//        remove location updates if you just need one location:
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void checkingGPS() {
        LocationManager lm = (LocationManager) InputHarian.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(InputHarian.this)
                    .setMessage("GPS tidak Aktif")
                    .setPositiveButton("Aktifkan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    private void takePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            }
            else
            {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }

//    private void checkGPS() {
//        LocationManager lm = (LocationManager) InputHarian.this.getSystemService(Context.LOCATION_SERVICE);
//        boolean gps_enabled = false;
//        boolean network_enabled = false;
//
//        try {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        } catch(Exception ex) {}
//
//        try {
//            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        } catch(Exception ex) {}
//
//        if(!gps_enabled && !network_enabled) {
//            // notify user
//            new AlertDialog.Builder(InputHarian.this)
//                    .setMessage("GPS tidak Aktif")
//                    .setPositiveButton("Aktifkan", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(intent);
//                        }
//                    })
//                    .setNegativeButton("Cancel",null)
//                    .show();
//        }else {
//            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(InputHarian.this);
//
//            if (ActivityCompat.checkSelfPermission(InputHarian.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        Location location = task.getResult();
//                        if (location != null){
//                            try {
//                                Geocoder geocoder = new Geocoder(InputHarian.this, Locale.getDefault());
//
//                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                                kecamatan = String.valueOf(addresses.get(0).getAddressLine(0));
//                                if (kecamatan.contains(Common.currentUser.getAlamat())){
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//                                        {
//                                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
//                                        }
//                                        else
//                                        {
//                                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
//                                        }
//                                    }
//                                }else{
//                                    Toast.makeText(InputHarian.this, kecamatan, Toast.LENGTH_SHORT).show();
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(InputHarian.this);
//                                    builder.setTitle("Lokasi Diluar Wilayah Kerja");
//                                    builder.setMessage("System mendeteksi lokasi perangkat anda diluar wilayah kerja");
//                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            // TODO: Do something, when user click on the positive button
//                                            dialog.cancel();
//                                        }
//                                    });
//                                    builder.create().show();
//                                }
//                            }catch (IOException e){
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
//                });
//            }else if (ActivityCompat.checkSelfPermission(InputHarian.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        Location location = task.getResult();
//                        if (location != null){
//                            try {
//                                Geocoder geocoder = new Geocoder(InputHarian.this, Locale.getDefault());
//
//                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                                kecamatan = String.valueOf(addresses.get(0).getAddressLine(0));
//                                if (kecamatan.contains(Common.currentUser.getAlamat())){
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//                                        {
//                                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
//                                        }
//                                        else
//                                        {
//                                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
//                                        }
//                                    }
//                                }else{
//                                    Toast.makeText(InputHarian.this, kecamatan, Toast.LENGTH_SHORT).show();
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(InputHarian.this);
//                                    builder.setTitle("Lokasi Diluar Wilayah Kerja");
//                                    builder.setMessage("System mendeteksi lokasi perangkat anda diluar wilayah kerja");
//                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            // TODO: Do something, when user click on the positive button
//                                            dialog.cancel();
//                                        }
//                                    });
//                                    builder.create().show();
//                                }
//                            }catch (IOException e){
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
//                });
//            }else {
//                ActivityCompat.requestPermissions(InputHarian.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//            }
//        }
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(InputHarian.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
            case MY_LOCATION_PERMISSION_CODE: {
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(InputHarian.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(InputHarian.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_PDF && resultCode == RESULT_OK && data != null){

            Uri path = data.getData();

            try {
                InputStream inputStream = InputHarian.this.getContentResolver().openInputStream(path);
                byte[] pdfInBytes = new byte[inputStream.available()];
                inputStream.read(pdfInBytes);
                encodedPDF = android.util.Base64.encodeToString(pdfInBytes, Base64.DEFAULT);

//                txtDokumen.setText("Document Selected");
//                txtDokumen.setTextColor(Color.BLACK);
//                btnPDf.setText("Change Document");

                Toast.makeText(this, "Document Selected", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img_bukti.setImageBitmap(photo);
            saveBitmapToJPG(photo);
        }
    }

    private void saveBitmapToJPG(Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 0, stream);

        byte[] imageBytes = stream.toByteArray();

        encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void choosePDF() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("application/pdf");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, REQ_PDF);
    }





//    private void buildRecyclerViewDataHarian() {
//        recyclerView = (RecyclerView) findViewById(R.id.rvData);
//        recyclerView.setHasFixedSize(true);
//        layoutManager1 = new LinearLayoutManager(InputHarian.this);
//        recyclerView.setLayoutManager(layoutManager1);
//        listDetail = new ArrayList<>();
//
//        getDataHarian();
//    }
//
//    private void getDataHarian() {
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        date = dateFormat.format(calendar.getTime());
//        userAlamat = Common.currentUser.getAlamat();
//        String url = Constants.ROOT_URL+"Detail_Pengamatan?kecamatan="+userAlamat+"&date="+date+"&id_hasil=0";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray array = new JSONArray(response);
//                            for (int i = 0; i<array.length(); i++){
//                                JSONObject object = array.getJSONObject(i);
//
//                                String jumlah_anakan = object.getString("jumlah_anakan");
//                                String id = object.getString("id_detail");
//                                int nomor_rumpun = i + 1;
//
//                                DetailModel detailModel = new DetailModel();
//                                detailModel.setId_detail(id);
//                                detailModel.setJumlah_anakan(jumlah_anakan);
//                                detailModel.setNomor_rumpun(String.valueOf(nomor_rumpun));
//                                listDetail.add(detailModel);
//
//                                if (listDetail.size() == 30){
//                                    btn_add.setVisibility(View.GONE);
//                                    Toast.makeText(InputHarian.this, "Data Telah Mencapai Batas Maximum", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                        adapter = new DetailAdapter(InputHarian.this, listDetail);
//                        recyclerView.setAdapter(adapter);
//                        adapter.setOnItemClickCallback(new DetailAdapter.OnItemClickCallback() {
//                            @Override
//                            public void onItemClicked(final DetailModel detailModel) {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(InputHarian.this);
//                                builder.setTitle("Delete Data");
//                                builder.setMessage("Apakah Anda Yakin?");
//                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        progressDialog.setTitle("Delete Data");
//                                        progressDialog.setMessage("Mohon Tunggu Sebentar");
//                                        progressDialog.show();
//                                        final String id_detail = detailModel.getId_detail();
//                                        String url = Constants.ROOT_URL+"Detail_Pengamatan";
//                                        StringRequest request = new StringRequest(Request.Method.POST, url,
//                                                new Response.Listener<String>() {
//                                                    @Override
//                                                    public void onResponse(String response) {
//                                                        if (response.contains("berhasil")){
//                                                            progressDialog.dismiss();
//                                                            finish();
//                                                            overridePendingTransition(0, 0);
//                                                            startActivity(getIntent());
//                                                            overridePendingTransition(0, 0);
//                                                            Toast.makeText(InputHarian.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    }
//                                                },
//                                                new Response.ErrorListener() {
//                                                    @Override
//                                                    public void onErrorResponse(VolleyError error) {
//                                                    }
//                                                })
//                                        {
//                                            @Override
//                                            protected Map<String, String> getParams() throws AuthFailureError {
//                                                Map<String, String> params = new HashMap<>();
//                                                params.put("id_detail", id_detail);
//                                                return params;
//                                            }
//                                        };
//                                        RequestQueue requestQueue = Volley.newRequestQueue(InputHarian.this);
//                                        requestQueue.add(request);
//
//                                    }
//                                });
//                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                    @Override public void onClick(DialogInterface dialog, int which) {
//                                        // TODO: Do something, when user click on the positive button
//                                        dialog.cancel();
//                                    }
//                                });
//                                builder.create().show();
//
//                            }
//                        });
//
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
////                Toast.makeText(InputHarian.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        Volley.newRequestQueue(InputHarian.this).add(stringRequest);
//    }

//    private void deleteData() {
//
//    }

    public void saveBitmapSignToJPG(Bitmap bitmap) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);

        byte[] imageBytes = stream.toByteArray();

        encodedSignature = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void dialog_action() {
        mSignaturePad = (SignaturePad) dialog.findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(InputHarian.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
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

    private void insertData() {
        progressDialog.setTitle("Insert Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = Constants.ROOT_URL+"Hasil_Pengamatan";
        final String kecamatan = Common.currentUser.getAlamat();
        final String provinsi = Common.currentUser.getProvinsi();
        final String kabupaten = Common.currentUser.getKabupaten();
        final String komoditas = spTanaman.getSelectedItem().toString().trim();
        final String desa = spDesa.getSelectedItem().toString().trim();
        final String blok = edtBlok.getText().toString().trim();
        final String luas_hamparan = edtLuas_Hamparan.getText().toString().trim();
        final String luas_diamati = edtLuas_Diamati.getText().toString().trim();
        final String luas_panen = edtLuas_Panen.getText().toString().trim();
        final String luas_persemaian = edtLuas_Persemaian.getText().toString().trim();
        final String ph_tanah = edtPH.getText().toString().trim();
        final String varietas = edtVarietas.getText().toString().trim();
        final String pola_tanam = edtPola_Tanam.getText().toString().trim();
        final String petugas_pengamatan = Common.currentUser.getNama();
        final String dari_umur = edtDari_Umur.getText().toString();
        final String hingga_umur = edtHingga_Umur.getText().toString();
        final String username = Common.currentUser.getUsername();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        final String tanggal = date;
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            getIDHasil();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(InputHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("provinsi", provinsi);
                params.put("kabupaten", kabupaten);
                params.put("kecamatan", kecamatan);
                params.put("varietas", varietas);
                params.put("desa", desa);
                params.put("komoditas", komoditas);
                params.put("blok", blok);
                params.put("luas_hamparan", luas_hamparan);
                params.put("luas_diamati", luas_diamati);
                params.put("luas_hasil_panen", luas_panen);
                params.put("luas_persemaian", luas_persemaian);
                params.put("ph_tanah", ph_tanah);
                params.put("pola_tanam", pola_tanam);
                params.put("petugas_pengamatan", petugas_pengamatan);
                params.put("dari_umur", dari_umur);
                params.put("hingga_umur", hingga_umur);
                params.put("tanggal_pengamatan", tanggal);
                params.put("latitude", String.valueOf(mLatitude));
                params.put("longitude", String.valueOf(mLongitude));
                params.put("bukti_foto", encodedImage);
                params.put("username", username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InputHarian.this);
        requestQueue.add(request);
    }

    private void getIDHasil() {
        userAlamat = Common.currentUser.getAlamat();
        String desa = spDesa.getSelectedItem().toString();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());
        String url = Constants.ROOT_URL+"Hasil_Pengamatan?kecamatan="+userAlamat+"&desa="+desa+"&date="+date;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String id = object.getString("id_hasil");

                                SessionManager sessionManager = new SessionManager(InputHarian.this, SessionManager.SESSION_ID);
                                sessionManager.createIdHasilSession(id, date);
                                Intent intent = new Intent(InputHarian.this, InputRumpun.class);
                                startActivity(intent);
                                finish();
//                                selectIDSum();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(InputHarian.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(InputHarian.this).add(stringRequest);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
