package com.majesty.siapopa.ui;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.majesty.siapopa.Common;
import com.majesty.siapopa.Constants;
import com.majesty.siapopa.R;
import com.majesty.siapopa.adapter.AdapterNotifStgh;
import com.majesty.siapopa.model.ModelPemberitahuan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetengahBulan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetengahBulan extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rvPemberitahuan;
    LinearLayoutManager layoutManager;
    List<ModelPemberitahuan> listPemberitahuan;
    ProgressDialog progressDialog;
    RecyclerView.Adapter mAdapter;

    public SetengahBulan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetengahBulan.
     */
    // TODO: Rename and change types and number of parameters
    public static SetengahBulan newInstance(String param1, String param2) {
        SetengahBulan fragment = new SetengahBulan();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setengah_bulan, container, false);
        rvPemberitahuan = view.findViewById(R.id.rvPemberitahuan);
        progressDialog = new ProgressDialog(getActivity());

        rvPemberitahuan.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvPemberitahuan.setLayoutManager(layoutManager);
        listPemberitahuan = new ArrayList<>();

        if (Common.currentUser.getId_usergroup().equals("1")){
            getPemberitahuan();
        }else if (Common.currentUser.getId_usergroup().equals("2")){
            getPemberitahuanKortikab();
        }else if (Common.currentUser.getId_usergroup().equals("3")){
            getPemberitahuanSatPel();
        }else if (Common.currentUser.getId_usergroup().equals("4")){
            getPemberitahuanProvinsi();
        }

        return view;
    }

    private void getPemberitahuanProvinsi() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String username = Common.currentUser.getProvinsi();
        String urlLaporan = Constants.ROOT_URL+"Pemberitahuan?status=Belum&username="+username+"&tipe_lap=Setengah&to=Provinsi";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String id_laporan = object.getString("id_laporan");
                                String title = object.getString("title");
                                String msg = object.getString("msg");
                                String laporan = object.getString("laporan");
                                String id = object.getString("id");
                                String desa = object.getString("desa");
                                String kecamatan = object.getString("kecamatan");
                                String blok = object.getString("jenis_opt");
                                String tanggal = object.getString("periode");
//                                String bukti_foto = object.getString("bukti_foto");
                                String approval_kab = object.getString("approval_kab");
                                String approval_satpel = object.getString("approval_satpel");
                                String approval_provinsi = object.getString("approval_prov");

                                ModelPemberitahuan pemberitahuan = new ModelPemberitahuan();
                                pemberitahuan.setId_laporan(id_laporan);
                                pemberitahuan.setTitle(title);
                                pemberitahuan.setMsg(msg);
                                pemberitahuan.setId(id);
                                pemberitahuan.setLaporan(laporan);
                                pemberitahuan.setDesa(desa);
                                pemberitahuan.setKecamatan(kecamatan);
                                pemberitahuan.setBlok(blok);
                                pemberitahuan.setTanggal(tanggal);
//                                pemberitahuan.setBukti_foto(bukti_foto);
                                pemberitahuan.setApproval_kab(approval_kab);
                                pemberitahuan.setApproval_satpel(approval_satpel);
                                pemberitahuan.setApproval_prov(approval_provinsi);
                                listPemberitahuan.add(pemberitahuan);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new AdapterNotifStgh(getActivity(), listPemberitahuan);
                        rvPemberitahuan.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void getPemberitahuanSatPel() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String username = Common.currentUser.getKabupaten();
        String urlLaporan = Constants.ROOT_URL+"Pemberitahuan?status=Belum&username="+username+"&tipe_lap=Setengah&to=SatPel";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String id_laporan = object.getString("id_laporan");
                                String title = object.getString("title");
                                String msg = object.getString("msg");
                                String laporan = object.getString("laporan");
                                String id = object.getString("id");
                                String desa = object.getString("desa");
                                String kecamatan = object.getString("kecamatan");
                                String blok = object.getString("jenis_opt");
                                String tanggal = object.getString("periode");
//                                String bukti_foto = object.getString("bukti_foto");
                                String approval_kab = object.getString("approval_kab");
                                String approval_satpel = object.getString("approval_satpel");
                                String approval_provinsi = object.getString("approval_prov");

                                ModelPemberitahuan pemberitahuan = new ModelPemberitahuan();
                                pemberitahuan.setId_laporan(id_laporan);
                                pemberitahuan.setTitle(title);
                                pemberitahuan.setMsg(msg);
                                pemberitahuan.setId(id);
                                pemberitahuan.setLaporan(laporan);
                                pemberitahuan.setDesa(desa);
                                pemberitahuan.setKecamatan(kecamatan);
                                pemberitahuan.setBlok(blok);
                                pemberitahuan.setTanggal(tanggal);
//                                pemberitahuan.setBukti_foto(bukti_foto);
                                pemberitahuan.setApproval_kab(approval_kab);
                                pemberitahuan.setApproval_satpel(approval_satpel);
                                pemberitahuan.setApproval_prov(approval_provinsi);
                                listPemberitahuan.add(pemberitahuan);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new AdapterNotifStgh(getActivity(), listPemberitahuan);
                        rvPemberitahuan.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void getPemberitahuanKortikab() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String username = Common.currentUser.getKabupaten();
        String urlLaporan = Constants.ROOT_URL+"Pemberitahuan?status=Belum&username="+username+"&tipe_lap=Setengah&to=Kortikab";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String id_laporan = object.getString("id_laporan");
                                String title = object.getString("title");
                                String msg = object.getString("msg");
                                String laporan = object.getString("laporan");
                                String id = object.getString("id");
                                String desa = object.getString("desa");
                                String kecamatan = object.getString("kecamatan");
                                String blok = object.getString("jenis_opt");
                                String tanggal = object.getString("periode");
//                                String bukti_foto = object.getString("bukti_foto");
                                String approval_kab = object.getString("approval_kab");
                                String approval_satpel = object.getString("approval_satpel");
                                String approval_provinsi = object.getString("approval_prov");

                                ModelPemberitahuan pemberitahuan = new ModelPemberitahuan();
                                pemberitahuan.setId_laporan(id_laporan);
                                pemberitahuan.setTitle(title);
                                pemberitahuan.setMsg(msg);
                                pemberitahuan.setId(id);
                                pemberitahuan.setLaporan(laporan);
                                pemberitahuan.setDesa(desa);
                                pemberitahuan.setKecamatan(kecamatan);
                                pemberitahuan.setBlok(blok);
                                pemberitahuan.setTanggal(tanggal);
//                                pemberitahuan.setBukti_foto(bukti_foto);
                                pemberitahuan.setApproval_kab(approval_kab);
                                pemberitahuan.setApproval_satpel(approval_satpel);
                                pemberitahuan.setApproval_prov(approval_provinsi);
                                listPemberitahuan.add(pemberitahuan);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new AdapterNotifStgh(getActivity(), listPemberitahuan);
                        rvPemberitahuan.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void getPemberitahuan() {
        progressDialog.setTitle("Loading..");
        progressDialog.show();
        String username = Common.currentUser.getUsername();
        String urlLaporan = Constants.ROOT_URL+"Pemberitahuan?status=Belum&username="+username+"&tipe_lap=Setengah";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLaporan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                String id_laporan = object.getString("id_laporan");
                                String title = object.getString("title");
                                String msg = object.getString("msg");
                                String laporan = object.getString("laporan");
                                String id = object.getString("id");
                                String desa = object.getString("desa");
                                String kecamatan = object.getString("kecamatan");
                                String blok = object.getString("jenis_opt");
                                String tanggal = object.getString("periode");
//                                String bukti_foto = object.getString("bukti_foto");
                                String approval_kab = object.getString("approval_kab");
                                String approval_satpel = object.getString("approval_satpel");
                                String approval_provinsi = object.getString("approval_prov");

                                ModelPemberitahuan pemberitahuan = new ModelPemberitahuan();
                                pemberitahuan.setId_laporan(id_laporan);
                                pemberitahuan.setTitle(title);
                                pemberitahuan.setMsg(msg);
                                pemberitahuan.setId(id);
                                pemberitahuan.setLaporan(laporan);
                                pemberitahuan.setDesa(desa);
                                pemberitahuan.setKecamatan(kecamatan);
                                pemberitahuan.setBlok(blok);
                                pemberitahuan.setTanggal(tanggal);
//                                pemberitahuan.setBukti_foto(bukti_foto);
                                pemberitahuan.setApproval_kab(approval_kab);
                                pemberitahuan.setApproval_satpel(approval_satpel);
                                pemberitahuan.setApproval_prov(approval_provinsi);
                                listPemberitahuan.add(pemberitahuan);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        mAdapter = new AdapterNotifStgh(getActivity(), listPemberitahuan);
                        rvPemberitahuan.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}