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

import com.majesty.siapopa.DetailLapor;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.IntensitasModel;

import java.util.ArrayList;
import java.util.List;


public class DetailIntensitasAdapter extends RecyclerView.Adapter<DetailIntensitasAdapter.MyViewHolder>{
    private Context mContext;
    private List<IntensitasModel> intensitasModels = new ArrayList<>();


    public DetailIntensitasAdapter (Context context,List<IntensitasModel> intensitasModels){
        this.mContext = context;
        this.intensitasModels = intensitasModels;
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

        View view = LayoutInflater.from(mContext).inflate(R.layout.approval_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final IntensitasModel intensitasModel = intensitasModels.get(position);

        holder.txtDate.setText(Html.fromHtml("<font color='#6200EE'><b>Tanggal: </b><br></font>" +intensitasModel.getTanggal_intensitas()));
        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>Kecamatan: </b><br></font>"+intensitasModel.getKecamatan()));
        holder.txtVarietas.setText(Html.fromHtml("<font color='#6200EE'><b>Jenis OPT: </b><br></font>"+intensitasModel.getJenis_opt()));
        holder.txtStatus.setText(Html.fromHtml("<font color='#6200EE'><b>Desa: </b><br></font>"+intensitasModel.getDesa()));
        holder.txtOPT.setText(Html.fromHtml("<font color='#6200EE'><b>Periode Pengamatan: </b><br></font>"+intensitasModel.getPeriode_pengamatan()));

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailLapor.class);

                intent.putExtra("id_intensitas",intensitasModel.getId_intensitas());
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
