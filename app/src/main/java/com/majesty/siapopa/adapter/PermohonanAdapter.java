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

import com.majesty.siapopa.DetailPermohonan;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.ModelPermohonan;

import java.util.ArrayList;
import java.util.List;

public class PermohonanAdapter extends RecyclerView.Adapter<PermohonanAdapter.MyViewHolder> {

    private Context mContext;
    private List<ModelPermohonan> laporan = new ArrayList<>();


    public PermohonanAdapter(Context context, List<ModelPermohonan> laporan){
        this.mContext = context;
        this.laporan = laporan;
    }

//    private PermohonanAdapter.OnItemClickCallback onItemClickCallback;
//
//    public void setOnItemClickCallback(PermohonanAdapter.OnItemClickCallback onItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback;
//    }
//
//    public interface OnItemClickCallback {
//        void onItemClicked(ModelPermohonan modelPermohonan);
//    }

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
    public PermohonanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.approval_item,parent,false);
        return new PermohonanAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PermohonanAdapter.MyViewHolder holder, int position) {

        final ModelPermohonan modelPermohonan = laporan.get(position);

        holder.txtDate.setText(Html.fromHtml("<font color='#6200EE'><b>Tanggal: </b><br></font>" +modelPermohonan.getTanggal_permohonan()));
        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>Kecamatan: </b><br></font>"+modelPermohonan.getKecamatan()));
        holder.txtVarietas.setText(Html.fromHtml("<font color='#6200EE'><b>Jenis OPT: </b><br></font>"+modelPermohonan.getJenis_opt()));
        holder.txtOPT.setText(Html.fromHtml("<font color='#6200EE'><b>Desa: </b><br></font>"+modelPermohonan.getDesa()));
        holder.txtStatus.setText(Html.fromHtml("<font color='#6200EE'><b>Varietas: </b><br></font>"+modelPermohonan.getVarietas()));
        final String id = modelPermohonan.getId_permohonan();

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                onItemClickCallback.onItemClicked(laporan.get(holder.getAdapterPosition()));

                Intent intent = new Intent(mContext, DetailPermohonan.class);
                intent.putExtra("id_permohonan", id);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return laporan.size();
    }
}
