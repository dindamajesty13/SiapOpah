package com.majesty.siapopa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailWilayah extends AppCompatActivity {
    Button btnLocation, btnSaveData;
    FusedLocationProviderClient fusedLocationProviderClient;
    String latitude, longitude, alamat, id_lokasi;
    TextView txtProvinsi, txtKabupaten, txtKecamatan, txtDesa, txtLatitude, txtLongitude, txtAlamat;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wilayah);

        btnLocation = (Button) findViewById(R.id.btn_location);
        btnSaveData = (Button) findViewById(R.id.btn_save_data);
        txtProvinsi = (TextView) findViewById(R.id.txtProvinsi);
        txtKabupaten = (TextView) findViewById(R.id.txtKabupaten);
        txtKecamatan = (TextView) findViewById(R.id.txtKecamatan);
        txtDesa = (TextView) findViewById(R.id.txtDesa);
        txtLatitude = (TextView) findViewById(R.id.txtLat);
        txtLongitude = (TextView) findViewById(R.id.txtLong);
        txtAlamat = (TextView) findViewById(R.id.txtAddress);
        progressDialog = new ProgressDialog(this);

        id_lokasi = getIntent().getStringExtra("id");

        String urlLaporan = Constants.ROOT_URL+"Wilayah?id="+id_lokasi;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String desa = object.getString("desa");
                                String kecamatan = object.getString("kecamatan");
                                String kabupaten = object.getString("kabupaten");
                                String provinsi = object.getString("provinsi");
                                String latitude = object.getString("latitude");
                                String longitude = object.getString("longitude");
                                String alamat = object.getString("alamat");

                                if (latitude.equals("")){
                                    txtLatitude.setText(Html.fromHtml("<font color='#000000'><b>Latitude: </b><br><b>-</b></font>"));
                                    txtLongitude.setText(Html.fromHtml("<font color='#000000'><b>Longitude: </b><br><b>-</b></font>"));
                                    txtAlamat.setText(Html.fromHtml("<font color='#000000'><b>Alamat: </b><br><b>-</b></font>"));
                                    txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Desa: </b><br></font>" + desa));
                                    txtKecamatan.setText(Html.fromHtml("<font color='#000000'><b>Kecamatan: </b><br></font>" + kecamatan));
                                    txtKabupaten.setText(Html.fromHtml("<font color='#000000'><b>Kabupaten: </b><br></font>" + kabupaten));
                                    txtProvinsi.setText(Html.fromHtml("<font color='#000000'><b>Provinsi: </b><br></font>" + provinsi));
                                } else {
                                    txtLatitude.setText(Html.fromHtml("<font color='#000000'><b>Latitude: </b><br></font>" + latitude));
                                    txtLongitude.setText(Html.fromHtml("<font color='#000000'><b>Longitude: </b><br></font>" + longitude));
                                    txtAlamat.setText(Html.fromHtml("<font color='#000000'><b>Alamat: </b><br></font>" + alamat));
                                    txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Desa: </b><br></font>" + desa));
                                    txtKecamatan.setText(Html.fromHtml("<font color='#000000'><b>Kecamatan: </b><br></font>" + kecamatan));
                                    txtKabupaten.setText(Html.fromHtml("<font color='#000000'><b>Kabupaten: </b><br></font>" + kabupaten));
                                    txtProvinsi.setText(Html.fromHtml("<font color='#000000'><b>Provinsi: </b><br></font>" + provinsi));
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(DetailWilayah.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(DetailWilayah.this).add(stringRequest);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(DetailWilayah.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }else {
                    ActivityCompat.requestPermissions(DetailWilayah.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataLokasi();
            }
        });
    }

    private void updateDataLokasi() {
        progressDialog.setTitle("Update Data");
        progressDialog.setMessage("Mohon Tunggu Sebentar");
        progressDialog.show();
        String url = Constants.ROOT_URL+"UpdateDesa";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("berhasil")){
                            progressDialog.dismiss();
                            Toast.makeText(DetailWilayah.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                            Intent home = new Intent(DetailWilayah.this, Wilayah.class);
                            home.putExtra("HOME", "homepopt");
                            startActivity(home);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(DetailWilayah.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_lokasi", id_lokasi);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("alamat", alamat);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailWilayah.this);
        requestQueue.add(request);
    }

    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null){
                    try {
                        Geocoder geocoder = new Geocoder(DetailWilayah.this, Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        latitude = String.valueOf(addresses.get(0).getLatitude());
                        longitude = String.valueOf(addresses.get(0).getLongitude());
                        alamat = String.valueOf(addresses.get(0).getAddressLine(0));

                        txtLatitude.setText(Html.fromHtml("<font color='#000000'><b>Latitude: </b><br></font>" + addresses.get(0).getLatitude()));
                        txtLongitude.setText(Html.fromHtml("<font color='#000000'><b>Longitude: </b><br></font>" + addresses.get(0).getLongitude()));
                        txtAlamat.setText(Html.fromHtml("<font color='#000000'><b>Alamat: </b><br></font>" + addresses.get(0).getAddressLine(0))) ;
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
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
