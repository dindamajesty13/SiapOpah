package com.majesty.siapopa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.majesty.siapopa.adapter.AdapterDesa;
import com.majesty.siapopa.model.HasilModel;
import com.majesty.siapopa.model.ModelLapor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Lapor extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    FloatingActionButton fab;
    List<HasilModel> listHasil;
    List<ModelLapor> listLapor;
    private RecyclerView.Adapter mAdapter;
    String username;
    String userAlamat;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Laporan Setengah Bulanan");
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.hide();

        TextView txtPeringatan = findViewById(R.id.peringatan);
        txtPeringatan.setVisibility(View.GONE);

        LinearLayout linearLayout = findViewById(R.id.linerLayout);
        linearLayout.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(Lapor.this);
        recyclerView.setLayoutManager(layoutManager);
        listHasil = new ArrayList<>();
        progressDialog = new ProgressDialog(this);

        getLaporan();
    }

    private void getLaporan(){
        progressDialog.setTitle("Loading");
        progressDialog.show();
        userAlamat = Common.currentUser.getAlamat();
        String urlLaporan = Constants.ROOT_URL+"Desa?kabupaten="+Common.currentUser.getKabupaten()+"&kecamatan="+userAlamat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String desa = object.getString("desa");

                                HasilModel hasilModel = new HasilModel();
                                hasilModel.setDesa(desa);
                                listHasil.add(hasilModel);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new AdapterDesa(Lapor.this, listHasil);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Lapor.this, "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Lapor.this).add(stringRequest);
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
