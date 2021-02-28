package com.majesty.siapopa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import com.google.android.material.navigation.NavigationView;
import com.majesty.siapopa.adapter.ProvAdapter;
import com.majesty.siapopa.adapter.StockAdapter;
import com.majesty.siapopa.model.BarModel;
import com.majesty.siapopa.model.HasilModel;
import com.majesty.siapopa.model.IntensitasModel;
import com.majesty.siapopa.model.ModelLapor;
import com.majesty.siapopa.model.StockModel;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    TextView txtFullname;
    boolean doubleBackToExitPressedOnce = false;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LinearLayoutManager manager;
    Toolbar toolbar;
    Button btn_detail;
    FloatingActionButton fab;
    List<HasilModel> listHasil;
    List<ModelLapor> listLapor;
    List<IntensitasModel> listApproval, listApprovalKab, listApprovalProv;
    List<StockModel> listStock;
    private RecyclerView.Adapter mAdapter;
    String username;
    String userAlamat;
    BarChart barChart, barChartOPT;
    ArrayList<BarEntry> barEntries;
    ArrayList<BarModel> barModels = new ArrayList<>();
    ArrayList<String> jenis_opt = new ArrayList<>();
    ArrayList<String> listKab = new ArrayList<>();
    ArrayAdapter<String> optAdapter;
    ArrayAdapter<String> kabAdapter;
    RequestQueue requestQueue;
    String selectedOPT, selectedKab;
    ArrayList<String> labels;
    WebView webView;
    String id_intensitas;
    TextView txtBatas, txtOPT, txtIntensitas, txtLuas, txtAlamat, txtPetugas;
    ProgressDialog progressDialog;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    //Drawer Menu
    Spinner spOPT, spKab;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    //Variables
    static final float END_SCALE = 0.7f;
    private GradientDrawable gradient1, gradient2, gradient3, gradient4;
    ImageView menuIcon, loginSignUpBtn;
    LinearLayout contentView;
    TextView txt_total;
    ImageView btn_notif;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String homepkab = getIntent().getStringExtra("HOME");
        assert homepkab != null;
        if (homepkab.equals("homepopt")){
            setContentView(R.layout.activity_home);

            BottomNavigationView navView = findViewById(R.id.nav_view);
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupWithNavController(navView, navController);

            //Menu Hooks
            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.navigation_view);

            //Hooks
            menuIcon = findViewById(R.id.menu_icon);
            contentView = findViewById(R.id.content);
            loginSignUpBtn = findViewById(R.id.login_signup);

            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_beranda);
            drawerLayout.setDrawerElevation(0);

            menuIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerVisible(GravityCompat.START))
                        drawerLayout.closeDrawer(GravityCompat.START);
                    else drawerLayout.openDrawer(GravityCompat.START);
                }
            });
            animateNavigationDrawer();

            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Beranda");
            setSupportActionBar(toolbar);
            requestQueue = Volley.newRequestQueue(this);
            progressDialog = new ProgressDialog(this);
            calendar = Calendar.getInstance();

            checkBatasApprovalSatpel();

        }else if (homepkab.equals("homepkab")){
            setContentView(R.layout.activity_home_pkab);
            BottomNavigationView navView = findViewById(R.id.nav_view);
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupWithNavController(navView, navController);

            //Menu Hooks
            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.navigation_view);

            //Hooks
            menuIcon = findViewById(R.id.menu_icon);
            contentView = findViewById(R.id.content);
            loginSignUpBtn = findViewById(R.id.login_signup);

            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_beranda);
            drawerLayout.setDrawerElevation(0);

            menuIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerVisible(GravityCompat.START))
                        drawerLayout.closeDrawer(GravityCompat.START);
                    else drawerLayout.openDrawer(GravityCompat.START);
                }
            });
            animateNavigationDrawer();

            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Beranda");
            setSupportActionBar(toolbar);
            requestQueue = Volley.newRequestQueue(this);
            progressDialog = new ProgressDialog(this);
            calendar = Calendar.getInstance();

            checkBatasApprovalSatpel();
        }
        else if (homepkab.equals("homesatpel")){
            setContentView(R.layout.activity_home_sat_pel);
            BottomNavigationView navView = findViewById(R.id.nav_view);
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupWithNavController(navView, navController);

            //Menu Hooks
            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.navigation_view);

            //Hooks
            menuIcon = findViewById(R.id.menu_icon);
            contentView = findViewById(R.id.content);
            loginSignUpBtn = findViewById(R.id.login_signup);

            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_beranda);
            drawerLayout.setDrawerElevation(0);

            menuIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerVisible(GravityCompat.START))
                        drawerLayout.closeDrawer(GravityCompat.START);
                    else drawerLayout.openDrawer(GravityCompat.START);
                }
            });
            animateNavigationDrawer();

            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Beranda");
            setSupportActionBar(toolbar);
            requestQueue = Volley.newRequestQueue(this);
            progressDialog = new ProgressDialog(this);
            calendar = Calendar.getInstance();

            checkBatasApprovalSatpel();

        }
        else if (homepkab.equals("homepprov")){
            setContentView(R.layout.activity_home_pprov);
            BottomNavigationView navView = findViewById(R.id.nav_view);
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupWithNavController(navView, navController);

            //Menu Hooks
            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.navigation_view);

            //Hooks
            menuIcon = findViewById(R.id.menu_icon);
            contentView = findViewById(R.id.content);
            loginSignUpBtn = findViewById(R.id.login_signup);

            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_beranda);
            drawerLayout.setDrawerElevation(0);

            menuIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerVisible(GravityCompat.START))
                        drawerLayout.closeDrawer(GravityCompat.START);
                    else drawerLayout.openDrawer(GravityCompat.START);
                }
            });
            animateNavigationDrawer();

            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Beranda");
            setSupportActionBar(toolbar);
            requestQueue = Volley.newRequestQueue(this);
            progressDialog = new ProgressDialog(this);
            calendar = Calendar.getInstance();

            checkBatasApprovalSatpel();

        }
        else if (homepkab.equals("homeadmin")){
            setContentView(R.layout.activity_home_admin);
            //Menu Hooks
            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.navigation_view);

            //Hooks
            menuIcon = findViewById(R.id.menu_icon);
            contentView = findViewById(R.id.content);
            loginSignUpBtn = findViewById(R.id.login_signup);

            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_beranda);
            drawerLayout.setDrawerElevation(0);

            menuIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerVisible(GravityCompat.START))
                        drawerLayout.closeDrawer(GravityCompat.START);
                    else drawerLayout.openDrawer(GravityCompat.START);
                }
            });
            animateNavigationDrawer();

            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Beranda");
            setSupportActionBar(toolbar);
//            spOPT = (Spinner) findViewById(R.id.spOPT);
            requestQueue = Volley.newRequestQueue(this);
//            barChart = findViewById(R.id.barchart);
            webView = (WebView) findViewById(R.id.maps_view);

            progressDialog = new ProgressDialog(this);

            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false);
            // Baris di bawah untuk menambahkan scrollbar di dalam WebView-nya
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.setWebViewClient(new MyWebViewClient());
            webView.loadUrl("https://siapopajabar.info/admin/Android_Maps");

            recyclerView = (RecyclerView) findViewById(R.id.rvList);
            recyclerView.setHasFixedSize(true);
//            layoutManager = new  LinearLayoutManager(Home.this);
            manager = new LinearLayoutManager(Home.this);
            manager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView.setLayoutManager(manager);
            listApprovalProv = new ArrayList<>();

            calendar = Calendar.getInstance();

            checkBatasApprovalSatpel();

            getDataDetail();
        }
        else if (homepkab.equals("brigade")){
            setContentView(R.layout.activity_home_brigade);
            //Menu Hooks
            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.navigation_view);

            //Hooks
            menuIcon = findViewById(R.id.menu_icon);
            contentView = findViewById(R.id.content);
            loginSignUpBtn = findViewById(R.id.login_signup);

            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_beranda);
            drawerLayout.setDrawerElevation(0);

            menuIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerVisible(GravityCompat.START))
                        drawerLayout.closeDrawer(GravityCompat.START);
                    else drawerLayout.openDrawer(GravityCompat.START);
                }
            });
            animateNavigationDrawer();
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Stock");
            setSupportActionBar(toolbar);

            fab = findViewById(R.id.fab);
            fab.setImageResource(R.drawable.ic_storage_black_24dp);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent cartIntent = new Intent(Home.this, Stock.class);
                    startActivity(cartIntent);
                }
            });
            progressDialog = new ProgressDialog(this);

            recyclerView = (RecyclerView) findViewById(R.id.rvList);
            recyclerView.setHasFixedSize(true);
            layoutManager = new  LinearLayoutManager(Home.this);
            recyclerView.setLayoutManager(layoutManager);
            listStock = new ArrayList<>();

            getStock();
        }

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
//                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_menu, R.id.nav_cart, R.id.nav_order,
//                R.id.nav_logout)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

//        View headerView = navigationView.getHeaderView(0);
//        txtFullname = headerView.findViewById(R.id.txtFullName);
//        txtFullname.setText(Common.currentUser.getNama());
    }

    private void getDataOPT() {
        String urlOpt = Constants.ROOT_URL + "Beranda";

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, urlOpt, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("jenis_opt");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String opt = jsonObject.optString("opt");
                        jenis_opt.add(opt);
                        optAdapter = new ArrayAdapter<>(Home.this, android.R.layout.simple_dropdown_item_1line, jenis_opt);
                        optAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spOPT.setAdapter(optAdapter);

                        spOPT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View v,
                                                       int pos, long id) {
                                listKab.clear();
                                barChart.setVisibility(View.VISIBLE);
                                selectedOPT = parent.getSelectedItem().toString();


//                                String kabupaten = Common.currentUser.getKabupaten();

                                String urlKab = Constants.ROOT_URL+"Beranda?opt="+selectedOPT;
                                JsonObjectRequest jsonObjectRequest1 = new
                                        JsonObjectRequest(Request.Method.GET, urlKab, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray jsonArray = response.getJSONArray("intensitas_stgh_bln");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                String opt = jsonObject.optString("kabupaten");
                                                listKab.add(opt);
                                                kabAdapter = new ArrayAdapter<>(Home.this, android.R.layout.simple_dropdown_item_1line, listKab);
                                                kabAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                                spKab.setAdapter(kabAdapter);

                                                spKab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View v,
                                                                               int pos, long id) {
                                                        barChart.setVisibility(View.VISIBLE);

                                                        selectedKab = spKab.getSelectedItem().toString();


                                                        String urlLaporan = Constants.ROOT_URL+"Beranda?opt="+selectedOPT+"&kabupaten="+selectedKab;
                                                        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        try {
                                                                            barEntries = new ArrayList<>();
                                                                            labels = new ArrayList<>();
                                                                            final JSONArray array = new JSONArray(response);
                                                                            for (int i = 0; i<array.length(); i++){
                                                                                JSONObject object = array.getJSONObject(i);
                                                                                progressDialog.dismiss();

                                                                                final String ringan = object.getString("ringan");
                                                                                final String sedang = object.getString("sedang");
                                                                                final String berat = object.getString("berat");
                                                                                final String puso = object.getString("puso");
                                                                                final String jenis_opt = object.getString("jenis_opt");

                                                                                barEntries.add(new BarEntry(0, Float.parseFloat(ringan)));
                                                                                barEntries.add(new BarEntry(1, Float.parseFloat(sedang)));
                                                                                barEntries.add(new BarEntry(2, Float.parseFloat(berat)));
                                                                                barEntries.add(new BarEntry(3, Float.parseFloat(puso)));
                                                                                labels.add("ringan");
                                                                                labels.add("sedang");
                                                                                labels.add("berat");
                                                                                labels.add("puso");
                                                                            }
                                                                            BarDataSet barDataSet = new BarDataSet(barEntries, "Kategori");
                                                                            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                                                                            Description description = new Description();
                                                                            description.setText("Data Luas Serangan Berdasarkan Jenis OPT dan Kabupaten");
                                                                            barChartOPT.setDescription(description);

                                                                            BarData barData = new BarData(barDataSet);
                                                                            barChartOPT.setData(barData);

                                                                            XAxis xAxis = barChartOPT.getXAxis();
                                                                            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

                                                                            xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
                                                                            xAxis.setDrawGridLines(false);
                                                                            xAxis.setDrawAxisLine(false);
                                                                            xAxis.setGranularity(1f);
                                                                            xAxis.setLabelCount(labels.size());
                                                                            xAxis.setLabelRotationAngle(35);
                                                                            barChartOPT.animateY(2000);
                                                                            barChartOPT.invalidate();
                                                                        }catch (Exception e){
                                                                            e.printStackTrace();
                                                                        }

                                                                    }
                                                                }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(Home.this, "No Data Found", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                        Volley.newRequestQueue(Home.this).add(stringRequest);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> arg0) {

                                                    }});

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

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {

                            }});

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

    private void getLokasiStock() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        userAlamat = Common.currentUser.getKabupaten();
        dateFormat = new SimpleDateFormat("yyyy-MM");
        final String tanggal = dateFormat.format(calendar.getTime());
        String urlLaporan = Constants.ROOT_URL+"Stock_Popt?kabupaten="+userAlamat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                progressDialog.dismiss();

                                String lokasi_stock = object.getString("lokasi_stock");

                                String urlLaporan = Constants.ROOT_URL+"Stock_Popt?lokasi="+lokasi_stock+"&date="+tanggal;
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    barEntries = new ArrayList<>();
                                                    labels = new ArrayList<>();
                                                    final JSONArray array = new JSONArray(response);
                                                    for (int i = 0; i<array.length(); i++){
                                                        JSONObject object = array.getJSONObject(i);
                                                        progressDialog.dismiss();

                                                        final String jenis_pestisida = object.getString("jenis_pestisida");
                                                        final String total_pestisida = object.getString("total");

                                                        barEntries.add(new BarEntry(i, Float.parseFloat(total_pestisida)));
                                                        labels.add(jenis_pestisida);
                                                    }
                                                    BarDataSet barDataSet = new BarDataSet(barEntries, "Pestisida");
                                                    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                                                    Description description = new Description();
                                                    description.setText("Data Stock Pestisida");
                                                    barChart.setDescription(description);

                                                    BarData barData = new BarData(barDataSet);
                                                    barChart.setData(barData);

                                                    XAxis xAxis = barChart.getXAxis();
                                                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

                                                    xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
                                                    xAxis.setDrawGridLines(false);
                                                    xAxis.setDrawAxisLine(false);
                                                    xAxis.setGranularity(1f);
                                                    xAxis.setLabelCount(labels.size());
                                                    xAxis.setLabelRotationAngle(35);
                                                    barChart.animateY(2000);
                                                    barChart.invalidate();
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Home.this, "No Data Found", Toast.LENGTH_LONG).show();
                                    }
                                });
                                Volley.newRequestQueue(Home.this).add(stringRequest);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Home.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Home.this).add(stringRequest);
    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent (WebView view, KeyEvent event) {
            // Do something with the event here
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading (WebView view, String url) {
            if (Uri.parse(url).equals("https://siapopahirc.herokuapp.com/admin/Maps")) {

                return false;
            }

            // reject anything other
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressDialog.setTitle("Loading..");
            progressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressDialog.dismiss();
        }
    }

    private void checkBatasApproval() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        String urlLaporan = Constants.ROOT_URL+"Batas_Waktu_Satpel?date="+date+"&approval_satpel=Belum";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                id_intensitas = object.getString("id_intensitas");
                                if (!id_intensitas.equals("null")){
                                    updateApprovalSatpel();
                                }else {
                                    Toast.makeText(Home.this, "Tidak Ada Approval Terlambat", Toast.LENGTH_LONG).show(); 
                                }

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Home.this, "Tidak Ada Approval Terlambat", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Home.this).add(stringRequest);
    }

    private void checkBatasApprovalSatpel() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        String urlLaporan = Constants.ROOT_URL+"Batas_Waktu?date="+date+"&approval_satpel=Belum&approval_kab=Belum";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                id_intensitas = object.getString("id_intensitas");

                                if (id_intensitas.equals("null")){
                                    checkBatasApprovalKab();
                                }else {
                                    updateApproval();
                                }

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkBatasApprovalKab();
//                checkBatasApproval();
//                Toast.makeText(Home.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Home.this).add(stringRequest);
    }

    private void updateApproval() {
        String url = Constants.ROOT_URL+"Batas_Waktu";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            Toast.makeText(Home.this, "Approval Kabupaten dan Satpel Terlambat", Toast.LENGTH_SHORT).show();
                            checkBatasApprovalKab();
//                            checkBatasApproval();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkBatasApprovalKab();
//                        checkBatasApproval();
//                        Toast.makeText(InputHarian.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("approval_satpel", "Valid");
                params.put("approval_kab", "Valid");
                params.put("id_intensitas", id_intensitas);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Home.this);
        requestQueue.add(request);
    }

    private void updateApprovalSatpel() {
        String url = Constants.ROOT_URL+"Batas_Waktu_Satpel";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            Toast.makeText(Home.this, "Approval Satpel Terlambat", Toast.LENGTH_SHORT).show();

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
                params.put("approval_satpel", "Valid");
                params.put("id_intensitas", id_intensitas);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Home.this);
        requestQueue.add(request);
    }

    private void checkGPS() {
        LocationManager lm = (LocationManager) Home.this.getSystemService(Context.LOCATION_SERVICE);
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
            new AlertDialog.Builder(Home.this)
                    .setMessage("GPS tidak Aktif")
                    .setPositiveButton("Aktifkan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    private void checkBatasApprovalKab() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        String urlLaporan = Constants.ROOT_URL+"Batas_Waktu_Kab?date="+date+"&approval_kab=Belum";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                id_intensitas = object.getString("id_intensitas");
                                if (id_intensitas.equals("null")){
                                    checkBatasApproval();
                                }else {
                                    updateApprovalKab();
                                }

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                checkBatasApprovalKab();
                checkBatasApproval();
//                Toast.makeText(Home.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Home.this).add(stringRequest);
    }

    private void updateApprovalKab() {
        String url = Constants.ROOT_URL+"Batas_Waktu_Kab";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("berhasil")){
                            Toast.makeText(Home.this, "Approval Kabupaten Terlambat", Toast.LENGTH_SHORT).show();
//                            checkBatasApprovalKab();
                            checkBatasApproval();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkBatasApproval();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("approval_kab", "Valid");
                params.put("id_intensitas", id_intensitas);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Home.this);
        requestQueue.add(request);
    }

    private void getDataDetail() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
//        username = Common.currentUser.getUsername();
        String urlLaporan = Constants.ROOT_URL+"MapsDetail";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String intensitas = object.getString("intensitas_keadaan");
                                String petugas = object.getString("petugas_pengamatan");
                                String batas_waktu = object.getString("batas_waktu_satpel");
                                String opt = object.getString("jenis_opt");
                                String luas = object.getString("luas_keadaan_serang");
                                String latitude = object.getString("latitude");
                                String longitude = object.getString("longitude");
                                String id = object.getString("id_intensitas");

                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(Home.this, Locale.getDefault());

                                addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                                IntensitasModel intensitasModel = new IntensitasModel();
                                intensitasModel.setBatas_waktu(batas_waktu);
                                intensitasModel.setJenis_opt(opt);
                                intensitasModel.setPetugas_pengamatan(petugas);
                                intensitasModel.setIntensitas_keadaan(intensitas);
                                intensitasModel.setAlamat(address);
                                intensitasModel.setLuas_keadaan_serang(luas);
                                intensitasModel.setId_intensitas(id);
                                listApprovalProv.add(intensitasModel);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new ProvAdapter(Home.this, listApprovalProv);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Home.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Home.this).add(stringRequest);
    }



    private void getStock() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        username = Common.currentUser.getUsername();
        String urlLaporan = Constants.ROOT_URL+"Stock?username="+username;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String pestisida = object.getString("pestisida");
                                String provinsi = object.getString("provinsi");
                                String tanggal = object.getString("tanggal_stock");
                                String kabupaten = object.getString("kabupaten");
                                String stock_akhir = object.getString("stock_akhir");
                                String id = object.getString("id_stock");

                                StockModel stock = new StockModel();
                                stock.setPestisida(pestisida);
                                stock.setProvinsi(provinsi);
                                stock.setTanggal_stock(tanggal);
                                stock.setKabupaten(kabupaten);
                                stock.setStock_akhir(stock_akhir);
                                stock.setId_stock(id);
                                listStock.add(stock);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new StockAdapter(Home.this, listStock);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Home.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Home.this).add(stringRequest);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String home = getIntent().getStringExtra("HOME");
        assert home != null;
        if (home.equals("homepopt")){
            int id = item.getItemId();

            if (id == R.id.nav_beranda){

            }else if (id == R.id.nav_harian_kel){
                Intent cartIntent = new Intent(this, Harian.class);
                startActivity(cartIntent);
            }else if (id == R.id.nav_harian){
                Intent cartIntent = new Intent(this, LaporanHarian.class);
                startActivity(cartIntent);
            }
            else if (id == R.id.nav_laporan){
                Intent cartIntent = new Intent(this, LapBulanan.class);
                startActivity(cartIntent);
            }else if (id == R.id.nav_stock) {
                Intent pdf = new Intent(this, StockPopt.class);
                startActivity(pdf);
            }else if (id == R.id.nav_stgh_bln) {
                Intent pdf = new Intent(this, Lapor.class);
                startActivity(pdf);
            }
            else if (id == R.id.nav_stock_alat) {
                Intent pdf = new Intent(this, StockPopt.class);
                startActivity(pdf);
            }else if (id == R.id.nav_musiman){
                Intent profile = new Intent(this, MusimKemarau.class);
                startActivity(profile);
            }
        } else if (home.equals("homepkab")){
            int id = item.getItemId();

            if (id == R.id.nav_beranda){

            }else if (id == R.id.nav_approval){
                Intent cartIntent = new Intent(this, ApprovalHarian.class);
                startActivity(cartIntent);
            }else if (id == R.id.nav_approval_stgh){
                Intent cartIntent = new Intent(this, Approval.class);
                startActivity(cartIntent);
            }
            else if (id == R.id.nav_laporan){
                Intent cartIntent = new Intent(this, LapBulanan.class);
                startActivity(cartIntent);
            }
            else if (id == R.id.nav_stock_alat) {
                Intent pdf = new Intent(this, StockPopt.class);
                startActivity(pdf);
            }else if (id == R.id.nav_profile){
                Intent profile = new Intent(this, Profile.class);
                startActivity(profile);
            }else if (id == R.id.nav_hujan){
                Intent profile = new Intent(this, MusimKemarau.class);
                startActivity(profile);
            }else if (id == R.id.nav_harian){
                Intent cartIntent = new Intent(this, ListLapHarian.class);
                startActivity(cartIntent);
            }else if (id == R.id.nav_lapor){
                Intent cartIntent = new Intent(this, LaporanHarianKab.class);
                startActivity(cartIntent);
            }else if (id == R.id.nav_stock) {
                Intent pdf = new Intent(this, StockPopt.class);
                startActivity(pdf);
            }

        } else if (home.equals("homesatpel")){
            int id = item.getItemId();

            if (id == R.id.nav_beranda){

            }else if (id == R.id.nav_approval){
                Intent cartIntent = new Intent(this, ApprovalHarian.class);
                startActivity(cartIntent);
            }else if (id == R.id.nav_approval_stgh){
                Intent cartIntent = new Intent(this, Approval.class);
                startActivity(cartIntent);
            }
            else if (id == R.id.nav_laporan){
                Intent cartIntent = new Intent(this, LapBulanan.class);
                startActivity(cartIntent);
            }
            else if (id == R.id.nav_stock_alat) {
                Intent pdf = new Intent(this, StockPopt.class);
                startActivity(pdf);
            }else if (id == R.id.nav_profile){
                Intent profile = new Intent(this, Profile.class);
                startActivity(profile);
            }else if (id == R.id.nav_hujan){
                Intent profile = new Intent(this, MusimKemarau.class);
                startActivity(profile);
            }else if (id == R.id.nav_harian){
                Intent cartIntent = new Intent(this, ListLapHarian.class);
                startActivity(cartIntent);
            }else if (id == R.id.nav_lapor){
                Intent cartIntent = new Intent(this, LaporanHarianKab.class);
                startActivity(cartIntent);
            }else if (id == R.id.nav_stock) {
                Intent pdf = new Intent(this, StockPopt.class);
                startActivity(pdf);
            }

        } else if (home.equals("homepprov")){
            int id = item.getItemId();

            if (id == R.id.nav_beranda){

            }else if (id == R.id.nav_top){
                Intent cartIntent = new Intent(this, Chart.class);
                startActivity(cartIntent);
            }
            else if (id == R.id.nav_harian){
                Intent cartIntent = new Intent(this, ListLapHarian.class);
                startActivity(cartIntent);
            }
            else if (id == R.id.nav_lapor){
                Intent cartIntent = new Intent(this, LaporanHarianKab.class);
                startActivity(cartIntent);
            }
            else if (id == R.id.nav_approval_stgh){
                Intent cartIntent = new Intent(this, Approval.class);
                startActivity(cartIntent);
            }
            else if (id == R.id.nav_approval){
                Intent cartIntent = new Intent(this, ApprovalHarian.class);
                startActivity(cartIntent);
            }
            else if (id == R.id.nav_monitoring){
                Intent cartIntent = new Intent(this, Monitoring.class);
                startActivity(cartIntent);
            }
            else if (id == R.id.nav_laporan){
                Intent cartIntent = new Intent(this, LapBulanan.class);
                startActivity(cartIntent);
            }else if (id == R.id.nav_chart_stock) {
                Intent pdf = new Intent(this, StockPopt.class);
                startActivity(pdf);
            }
            else if (id == R.id.nav_stock){
                Intent stock = new Intent(this, StockProv.class);
                startActivity(stock);
            }
            else if (id == R.id.nav_stock_alat) {
                Intent pdf = new Intent(this, StockPopt.class);
                startActivity(pdf);
            }else if (id == R.id.nav_profile){
                Intent profile = new Intent(this, Profile.class);
                startActivity(profile);
            }else if (id == R.id.nav_hujan){
                Intent profile = new Intent(this, MusimKemarau.class);
                startActivity(profile);
            }

        } else if (home.equals("homeadmin")){
            int id = item.getItemId();

            if (id == R.id.nav_beranda){

            }else if (id == R.id.nav_top){
                Intent cartIntent = new Intent(this, Chart.class);
                startActivity(cartIntent);
            }
            else if (id == R.id.nav_approval){
                Intent cartIntent = new Intent(this, ApprovalPermohonan.class);
                startActivity(cartIntent);
            }
            else if (id == R.id.nav_monitoring){
                Intent cartIntent = new Intent(this, Monitoring.class);
                startActivity(cartIntent);
            }
            else if (id == R.id.nav_laporan){
                Intent cartIntent = new Intent(this, Permohonan.class);
                startActivity(cartIntent);
            }else if (id == R.id.nav_chart_stock) {
                Intent pdf = new Intent(this, StockPopt.class);
                startActivity(pdf);
            }
            else if (id == R.id.nav_stock){
                Intent stock = new Intent(this, StockProv.class);
                startActivity(stock);
            }else if (id == R.id.nav_stgh_bln) {
                Intent pdf = new Intent(this, SetengahBulan.class);
                startActivity(pdf);
            }
            else if (id == R.id.nav_stock_alat) {
                Intent pdf = new Intent(this, MusimKemarau.class);
                startActivity(pdf);
            }else if (id == R.id.nav_profile){
                Intent profile = new Intent(this, Profile.class);
                startActivity(profile);
            }else if (id == R.id.nav_hujan){
                Intent profile = new Intent(this, Profile.class);
                startActivity(profile);
            }else if (id == R.id.nav_kemarau){
                Intent profile = new Intent(this, Profile.class);
                startActivity(profile);
            }

        } else if (home.equals("brigade")){
            int id = item.getItemId();

            if (id == R.id.nav_stock){

            }else if (id == R.id.nav_permintaan){
                Intent stock = new Intent(this, Permohonan.class);
                startActivity(stock);
            }
            else if (id == R.id.nav_logout){
                Intent logout = new Intent(this, MainActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else if (navView.getSelectedItemId() == R.id.navigation_home){
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setTitle("Keluar Aplikasi");
            builder.setMessage("Apakah Anda Yakin?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO: Do something, when user click on the positive button
                    Home.super.onBackPressed();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                    // TODO: Do something, when user click on the positive button
                    dialog.cancel();
                }
            });
            builder.create().show();
        } else if (navView.getSelectedItemId() == R.id.navigation_dashboard){
            Home.super.onBackPressed();
        } else if (navView.getSelectedItemId() == R.id.navigation_favorite){
            Home.super.onBackPressed();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setTitle("Keluar Aplikasi");
            builder.setMessage("Apakah Anda Yakin?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO: Do something, when user click on the positive button
                    Home.super.onBackPressed();

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
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}
