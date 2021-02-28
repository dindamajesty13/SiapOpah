package com.majesty.siapopa.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.majesty.siapopa.Common;
import com.majesty.siapopa.DetailApprHarian;
import com.majesty.siapopa.DetailLapHarian;
import com.majesty.siapopa.DetailLapor;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.ModelPemberitahuan;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterPemberitahuan extends RecyclerView.Adapter<AdapterPemberitahuan.MyViewHolder>{
    private Context mContext;
    private List<ModelPemberitahuan> modelPemberitahuans = new ArrayList<>();


    public AdapterPemberitahuan (Context context,List<ModelPemberitahuan> modelPemberitahuans){
        this.mContext = context;
        this.modelPemberitahuans = modelPemberitahuans;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

//        private TextView txtDesa;
//        private TextView txtJumlah;
//        private TextView txtVarietas;
        //        private TextView txtOPT;
//        private TextView txtStatus;
        private CardView mContainer;
        private AppCompatImageView img_bukti;
        private TextView txtTanggal;
        private TextView txtKecamatan;
        private TextView txtDesa;
        private TextView txtBlok, txtApprovalKab, txtApprovalSatpel, txtApprovalProv;

        public MyViewHolder (View itemView){
            super(itemView);

            txtDesa = itemView.findViewById(R.id.txtDesa);
            txtTanggal = itemView.findViewById(R.id.txtTanggal);
            txtKecamatan = itemView.findViewById(R.id.txtKecamatan);
            txtBlok = itemView.findViewById(R.id.txtBlok);
            txtApprovalKab = itemView.findViewById(R.id.txtApprovalKab);
            txtApprovalSatpel = itemView.findViewById(R.id.txtApprovalSatpel);
            txtApprovalProv = itemView.findViewById(R.id.txtApprovalProv);
            img_bukti = itemView.findViewById(R.id.img_bukti);
            mContainer = itemView.findViewById(R.id.cardView);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final ModelPemberitahuan modelPemberitahuan = modelPemberitahuans.get(position);

        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(modelPemberitahuan.getTanggal());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat newDateFormat= new SimpleDateFormat("dd MMMM yyyy");
        String tanggal = newDateFormat.format(date);

        holder.txtDesa.setText("Desa: "+modelPemberitahuan.getDesa());
        holder.txtTanggal.setText("Status Validasi Laporan Tanggal "+tanggal);
        holder.txtKecamatan.setText("Kec: "+modelPemberitahuan.getKecamatan());
        holder.txtBlok.setText("Blok: "+modelPemberitahuan.getBlok());
        holder.txtApprovalKab.setText("Kortikab: "+modelPemberitahuan.getApproval_kab());
        holder.txtApprovalSatpel.setText("SatPel: "+modelPemberitahuan.getApproval_satpel());
        holder.txtApprovalProv.setText("BPTPH: "+modelPemberitahuan.getApproval_prov());
        Picasso.get().load("https://siapopajabar.info/assets/Images/"+modelPemberitahuan.getBukti_foto()).into(holder.img_bukti);


        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (modelPemberitahuan.getLaporan().equals("Harian")){
                    if (Common.currentUser.getId_usergroup().equals("1")){
                        Intent intent = new Intent(mContext, DetailLapHarian.class);

                        intent.putExtra("id",modelPemberitahuan.getId_laporan());
                        intent.putExtra("id_notif",modelPemberitahuan.getId());
                        intent.putExtra("id_hasil",modelPemberitahuan.getId_hasil());
                        intent.putExtra("status","Sudah");

                        mContext.startActivity(intent);
                    }else if (Common.currentUser.getId_usergroup().equals("2")){
                        Intent intent = new Intent(mContext, DetailApprHarian.class);

                        intent.putExtra("id_intensitas",modelPemberitahuan.getId_laporan());
                        intent.putExtra("id_notif",modelPemberitahuan.getId());
                        intent.putExtra("id_hasil",modelPemberitahuan.getId_hasil());
                        intent.putExtra("status","Belum");

                        mContext.startActivity(intent);
                    }else if (Common.currentUser.getId_usergroup().equals("3")){
                        Intent intent = new Intent(mContext, DetailApprHarian.class);

                        intent.putExtra("id_intensitas",modelPemberitahuan.getId_laporan());
                        intent.putExtra("id_notif",modelPemberitahuan.getId());
                        intent.putExtra("id_hasil",modelPemberitahuan.getId_hasil());
                        intent.putExtra("status","Belum");

                        mContext.startActivity(intent);
                    }else if (Common.currentUser.getId_usergroup().equals("4")){
                        Intent intent = new Intent(mContext, DetailApprHarian.class);

                        intent.putExtra("id_intensitas",modelPemberitahuan.getId_laporan());
                        intent.putExtra("id_notif",modelPemberitahuan.getId());
                        intent.putExtra("id_hasil",modelPemberitahuan.getId_hasil());
                        intent.putExtra("status","Belum");

                        mContext.startActivity(intent);
                    }

                }else {
                    Intent intent = new Intent(mContext, DetailLapor.class);

                    intent.putExtra("id",modelPemberitahuan.getId_laporan());
                    intent.putExtra("id_notif",modelPemberitahuan.getId());
                    intent.putExtra("status","Sudah");

                    mContext.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelPemberitahuans.size();
    }
}
