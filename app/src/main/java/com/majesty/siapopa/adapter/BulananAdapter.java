package com.majesty.siapopa.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.majesty.siapopa.DetailBulanan;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.HasilModel;

import java.util.ArrayList;
import java.util.List;

public class BulananAdapter extends RecyclerView.Adapter<BulananAdapter.MyViewHolder>{
    private Context mContext;
    private List<HasilModel> hasilModels = new ArrayList<>();


    public BulananAdapter (Context context,List<HasilModel> hasilModels){
        this.mContext = context;
        this.hasilModels = hasilModels;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDate;
        private TextView txtDesa;
        private TextView txtVarietas;
        private TextView txtOPT;
        private TextView txtStatus;
        private ConstraintLayout mContainer;

        public MyViewHolder (View itemView){
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtDate);
            txtDesa = itemView.findViewById(R.id.txtDesa);
            txtVarietas = itemView.findViewById(R.id.txtVarietas);
            txtOPT = itemView.findViewById(R.id.txtOPT);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            mContainer = itemView.findViewById(R.id.constLayout);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.lapor_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final HasilModel hasilModel = hasilModels.get(position);

        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>OPT: </b><br></font>"+hasilModel.getOpt()));
        holder.txtDate.setVisibility(View.GONE);
        holder.txtVarietas.setText(Html.fromHtml("<font color='#6200EE'><b>Intensitas: </b><br></font>"+hasilModel.getTotal_intensitas()));

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailBulanan.class);

                intent.putExtra("opt",hasilModel.getOpt());
                intent.putExtra("kabupaten",hasilModel.getKabupaten());
                intent.putExtra("kecamatan",hasilModel.getKecamatan());
                intent.putExtra("desa",hasilModel.getDesa());
                intent.putExtra("umur_tanaman",hasilModel.getDari_umur());
                intent.putExtra("luas_tambah",hasilModel.getLuas_diamati());
                intent.putExtra("periode_pengamatan",hasilModel.getTanggal_pengamatan());
                intent.putExtra("luas_tanaman",hasilModel.getLuas_hamparan());
                intent.putExtra("komoditas",hasilModel.getKomoditas());
                intent.putExtra("varietas",hasilModel.getVarietas());
                intent.putExtra("intensitas_tambah",hasilModel.getTotal_intensitas());
                intent.putExtra("luas_waspada",hasilModel.getLuas_persemaian());

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return hasilModels.size();
    }
}
