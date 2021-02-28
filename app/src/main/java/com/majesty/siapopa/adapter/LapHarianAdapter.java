package com.majesty.siapopa.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.majesty.siapopa.HasilLapHarian;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.IntensitasModel;

import java.util.ArrayList;
import java.util.List;

public class LapHarianAdapter extends RecyclerView.Adapter<LapHarianAdapter.MyViewHolder>{
    private Context mContext;
    private List<IntensitasModel> intensitasModels = new ArrayList<>();


    public LapHarianAdapter (Context context,List<IntensitasModel> intensitasModels){
        this.mContext = context;
        this.intensitasModels = intensitasModels;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDate;
        private TextView txtDesa;
        private TextView txtVarietas;
        private TextView txtOPT;
        private TextView txtStatus;
        private CardView mContainer;

        public MyViewHolder (View itemView){
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtDate);
            txtDesa = itemView.findViewById(R.id.txtDesa);
            txtOPT = itemView.findViewById(R.id.txtOPT);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtVarietas = itemView.findViewById(R.id.txtVarietas);
            mContainer = itemView.findViewById(R.id.cardView);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.harian_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final IntensitasModel intensitasModel = intensitasModels.get(position);

        holder.txtDate.setText(Html.fromHtml("<font color='#6200EE'><b>Tanggal: </b></font>" +intensitasModel.getTanggal_intensitas()));
        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>Jenis OPT: </b><br></font>"+intensitasModel.getJenis_opt()));
        holder.txtVarietas.setText(Html.fromHtml("<font color='#6200EE'><b>Intensitas: </b><br></font>"+intensitasModel.getIntensitas_tambah()+" %"));
        holder.txtOPT.setText(Html.fromHtml("<font color='#6200EE'><b>Desa: </b><br></font>"+intensitasModel.getDesa()));
        if (intensitasModel.getKecamatan() != null){
            holder.txtStatus.setText(Html.fromHtml("<font color='#6200EE'><b>Kecamatan: </b><br></font>"+intensitasModel.getKecamatan()));
        }else {
            holder.txtStatus.setText(Html.fromHtml("<font color='#6200EE'><b>Status Validasi: </b><br></font>"+intensitasModel.getPetugas_pengamatan()));
        }


        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, HasilLapHarian.class);

                intent.putExtra("id",intensitasModel.getId_intensitas());
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return intensitasModels.size();
    }
}

