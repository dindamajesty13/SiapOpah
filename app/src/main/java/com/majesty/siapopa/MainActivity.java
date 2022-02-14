package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.majesty.siapopa.model.SessionManager;
import com.majesty.siapopa.model.User;
import com.vishnusivadas.advanced_httpurlconnection.PutData;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;

    TextInputLayout phoneNumber, password;
    CheckBox rememberMe;
    TextInputEditText phoneNumberEditText, passwordEditText;
    AppCompatButton letTheUserLogIn, btn_forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumber = findViewById(R.id.login_phone_number);
        password = findViewById(R.id.login_password);
        rememberMe = findViewById(R.id.remember_me);
        phoneNumberEditText = findViewById(R.id.login_phone_number_editText);
        passwordEditText = findViewById(R.id.login_password_editText);
        letTheUserLogIn = findViewById(R.id.letTheUserLogIn);


        SessionManager sessionManager = new SessionManager(MainActivity.this,
                SessionManager.SESSION_REMEMMBERME);
        if (sessionManager.checkRememberMe()) {
            HashMap<String, String> rememberMeDetails = sessionManager
                    .getRemeberMeDetailsFromSession();
            phoneNumberEditText.setText(rememberMeDetails.get(SessionManager
                    .KEY_SESSIONPHONENUMBER));
            passwordEditText.setText(rememberMeDetails.get(SessionManager
                    .KEY_SESSIONPASSWORD));
            rememberMe.setChecked(true);
        }else {
            rememberMe.setChecked(false);
        }
        progressDialog = new ProgressDialog(this);
        letTheUserLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Login");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                login();
            }
        });


    }



    private void login() {
        final String username, password;
        username = String.valueOf(phoneNumberEditText.getText());
        password = String.valueOf(passwordEditText.getText());

        if (rememberMe.isChecked()) {
            SessionManager sessionManager = new SessionManager(MainActivity.this, SessionManager.SESSION_REMEMMBERME);
            sessionManager.createRememberMeSession(username, password);
        }
        if (!rememberMe.isChecked()){
            SessionManager sessionManager = new SessionManager(MainActivity.this, SessionManager.SESSION_REMEMMBERME);
            sessionManager.clearRememberMeSession();
        }
        if (!username.equals("") && !password.equals("")){
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String[] field = new String[2];
                    field[0] = "username";
                    field[1] = "password";
                    String[] data = new String[2];
                    data[0] = username;
                    data[1] = password;
                    PutData putData = new PutData("https://siapopa.com/Login", "POST", field, data);
                    if (putData.startPut()){
                        if (putData.onComplete()){
                            String result = putData.getResult();
                            if (result.contains("Login POPT Success")){
                                String urlLaporan = "https://siapopa.com/User?username=" + MainActivity.this.phoneNumberEditText.getText().toString();
                                StringRequest stringRequests = new StringRequest(Request.Method.GET, urlLaporan,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray array = new JSONArray(response);
                                                    for (int i = 0; i<array.length(); i++){
                                                        JSONObject object = array.getJSONObject(i);

                                                        String alamat = object.getString("alamat");
                                                        String provinsi = object.getString("provinsi");
                                                        String kabupaten = object.getString("kabupaten");
                                                        String id_user = object.getString("usergroup_id");
                                                        String nama = object.getString("nama");

                                                        User user = new User();
                                                        user.setUsername(phoneNumberEditText.getText().toString());
                                                        user.setAlamat(alamat);
                                                        user.setProvinsi(provinsi);
                                                        user.setKabupaten(kabupaten);
                                                        user.setId_usergroup(id_user);
                                                        user.setNama(nama);
                                                        Common.currentUser = user;

                                                        progressDialog.dismiss();
                                                        Intent home = new Intent(MainActivity.this, Home.class);
                                                        home.putExtra("HOME", "homepopt");
                                                        startActivity(home);
                                                        finish();

                                                    }
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                                Volley.newRequestQueue(MainActivity.this).add(stringRequests);

                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                            }else if (result.contains("Login PKAB Success")){
                                String urlLaporan = "https://siapopa.com/Kortikab?username=" + MainActivity.this.phoneNumberEditText.getText().toString();
                                StringRequest stringRequests = new StringRequest(Request.Method.GET, urlLaporan,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray array = new JSONArray(response);
                                                    for (int i = 0; i<array.length(); i++){
                                                        JSONObject object = array.getJSONObject(i);

                                                        String alamat = object.getString("kabupaten");
                                                        String provinsi = object.getString("provinsi");
                                                        String kabupaten = object.getString("kabupaten");
                                                        String id_user = object.getString("usergroup_id");
                                                        String nama = object.getString("nama");

                                                        User user = new User();
                                                        user.setUsername(phoneNumberEditText.getText().toString());
                                                        user.setAlamat(alamat);
                                                        user.setProvinsi(provinsi);
                                                        user.setKabupaten(kabupaten);
                                                        user.setId_usergroup(id_user);
                                                        user.setNama(nama);
                                                        Common.currentUser = user;

                                                        progressDialog.dismiss();
                                                        Intent homepkab = new Intent(MainActivity.this, Home.class);
                                                        homepkab.putExtra("HOME", "homepkab");
                                                        startActivity(homepkab);
                                                        finish();

                                                    }
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                                Volley.newRequestQueue(MainActivity.this).add(stringRequests);

                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                            } else if (result.contains("Login SatPel Success")){

                                String urlprov = "https://siapopa.com/Satpel?username=" + MainActivity.this.phoneNumberEditText.getText().toString();
                                StringRequest stringRequests = new StringRequest(Request.Method.GET, urlprov,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray array = new JSONArray(response);
                                                    for (int i = 0; i<array.length(); i++){
                                                        JSONObject object = array.getJSONObject(i);

                                                        String alamat = object.getString("kabupaten");
                                                        String provinsi = object.getString("provinsi");
                                                        String kabupaten = object.getString("kabupaten");
                                                        String id_user = object.getString("usergroup_id");
                                                        String nama = object.getString("nama");

                                                        User user = new User();
                                                        user.setUsername(phoneNumberEditText.getText().toString());
                                                        user.setId_usergroup(id_user);
                                                        user.setAlamat(alamat);
                                                        user.setProvinsi(provinsi);
                                                        user.setKabupaten(kabupaten);
                                                        user.setNama(nama);
                                                        Common.currentUser = user;

                                                        progressDialog.dismiss();
                                                        Intent homepkab = new Intent(MainActivity.this, Home.class);
                                                        homepkab.putExtra("HOME", "homesatpel");
                                                        startActivity(homepkab);
                                                        finish();

                                                    }
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                                Volley.newRequestQueue(MainActivity.this).add(stringRequests);

                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                            } else if (result.contains("Login PProv Success")){

                                String urlprov = "https://siapopa.com/BPTPH?username=" + MainActivity.this.phoneNumberEditText.getText().toString();
                                StringRequest stringRequests = new StringRequest(Request.Method.GET, urlprov,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray array = new JSONArray(response);
                                                    for (int i = 0; i<array.length(); i++){
                                                        JSONObject object = array.getJSONObject(i);

                                                        String alamat = object.getString("provinsi");
                                                        String provinsi = object.getString("provinsi");
                                                        String id_user = object.getString("usergroup_id");
                                                        String nama = object.getString("nama");

                                                        User user = new User();
                                                        user.setUsername(phoneNumberEditText.getText().toString());
                                                        user.setId_usergroup(id_user);
                                                        user.setAlamat(alamat);
                                                        user.setNama(nama);
                                                        user.setProvinsi(provinsi);
                                                        Common.currentUser = user;

                                                        progressDialog.dismiss();
                                                        Intent homepkab = new Intent(MainActivity.this, Home.class);
                                                        homepkab.putExtra("HOME", "homepprov");
                                                        startActivity(homepkab);
                                                        finish();

                                                    }
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                                Volley.newRequestQueue(MainActivity.this).add(stringRequests);

                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                            } else if (result.contains("Login Brigade Success")){
                                String urlLaporan = "https://siapopa.com/Brigade?username=" + MainActivity.this.phoneNumberEditText.getText().toString();
                                StringRequest stringRequests = new StringRequest(Request.Method.GET, urlLaporan,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray array = new JSONArray(response);
                                                    for (int i = 0; i<array.length(); i++){
                                                        JSONObject object = array.getJSONObject(i);

                                                        String provinsi = object.getString("provinsi");
                                                        String kabupaten = object.getString("kabupaten");
                                                        String id_user = object.getString("usergroup_id");
                                                        String nama = object.getString("nama");

                                                        User user = new User();
                                                        user.setUsername(phoneNumberEditText.getText().toString());
                                                        user.setProvinsi(provinsi);
                                                        user.setKabupaten(kabupaten);
                                                        user.setId_usergroup(id_user);
                                                        user.setNama(nama);
                                                        Common.currentUser = user;

                                                        progressDialog.dismiss();
                                                        Intent homepkab = new Intent(MainActivity.this, Home.class);
                                                        homepkab.putExtra("HOME", "brigade");
                                                        startActivity(homepkab);
                                                        finish();

                                                    }
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                                Volley.newRequestQueue(MainActivity.this).add(stringRequests);

                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            progressDialog.dismiss();
            if (TextUtils.isEmpty(password) || TextUtils.isEmpty(username)){
                phoneNumberEditText.setError("Tidak Boleh Kosong");
                passwordEditText.setError("Tidak Boleh Kosong");
                Toast.makeText(MainActivity.this, "Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
            }

        }
    }




}
