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

import com.majesty.siapopa.DetailDesa;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.HasilModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterDesaHarian extends RecyclerView.Adapter<AdapterDesaHarian.MyViewHolder>{
    private Context mContext;
    private List<HasilModel> hasilModels = new ArrayList<>();


    public AdapterDesaHarian (Context context,List<HasilModel> hasilModels){
        this.mContext = context;
        this.hasilModels = hasilModels;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDate;
        private TextView txtDesa;
        private TextView txtVarietas;
        //        private TextView txtOPT;
//        private TextView txtStatus;
        private CardView mContainer;

        public MyViewHolder (View itemView){
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtDate);
            txtDesa = itemView.findViewById(R.id.txtDesa);
            txtVarietas = itemView.findViewById(R.id.txtVarietas);
            mContainer = itemView.findViewById(R.id.cardView);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final HasilModel hasilModel = hasilModels.get(position);

//        holder.txtDate.setText(Html.fromHtml("<font color='#6200EE'><b>Tanggal: </b><br></font>" +hasilModel.getTanggal_pengamatan()));
        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>Desa: </b></font>"+hasilModel.getDesa()));
//        holder.txtVarietas.setText(Html.fromHtml("<font color='#6200EE'><b>Vatietas: </b><br></font>"+hasilModel.getVarietas()));
//        holder.txtOPT.setText(Html.fromHtml("<font color='#6200EE'><b>Desa: </b><br></font>"+hasilModel.getDesa()));
//        holder.txtStatus.setText(Html.fromHtml("<font color='#6200EE'><b>Petugas Pengamatan: </b><br></font>"+hasilModel.getPetugas_pengamatan()));

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailDesa.class);

                intent.putExtra("desa",hasilModel.getDesa());
                intent.putExtra("id","harian");
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return hasilModels.size();
    }
}
