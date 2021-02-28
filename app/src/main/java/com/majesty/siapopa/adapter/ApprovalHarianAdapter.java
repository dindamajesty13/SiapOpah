package com.majesty.siapopa.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.majesty.siapopa.DetailApprHarian;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.IntensitasModel;

import java.util.ArrayList;
import java.util.List;

public class ApprovalHarianAdapter extends RecyclerView.Adapter<ApprovalHarianAdapter.MyViewHolder> {

    private Context mContext;
    private List<IntensitasModel> laporan = new ArrayList<>();


    public ApprovalHarianAdapter(Context context, List<IntensitasModel> laporan){
        this.mContext = context;
        this.laporan = laporan;
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
            txtVarietas = itemView.findViewById(R.id.txtVarietas);
            txtOPT = itemView.findViewById(R.id.txtOPT);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            mContainer = itemView.findViewById(R.id.cardView);
        }
    }


    @NonNull
    @Override
    public ApprovalHarianAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.lapor_item,parent,false);
        return new ApprovalHarianAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApprovalHarianAdapter.MyViewHolder holder, int position) {

        final IntensitasModel intensitasModel = laporan.get(position);

        holder.txtDate.setText(intensitasModel.getLuas_tanaman()+" Ha");
//        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>OPT: </b></font>"+hasilModel.getOpt()));
        holder.txtDesa.setText(intensitasModel.getJenis_opt());
        holder.txtVarietas.setText(intensitasModel.getBlok());
//        holder.txtVarietas.setText(Html.fromHtml("<font color='#6200EE'><b>Intensitas: </b></font>"+hasilModel.getTotal_intensitas()));
        if (intensitasModel.getKabupaten().equals("mutlak")){
            holder.txtOPT.setText(intensitasModel.getIntensitas_tambah()+" %");
        }else if (intensitasModel.getKabupaten().equals("populasi")){
            holder.txtOPT.setText(intensitasModel.getIntensitas_tambah()+" ekr/rpn");
        }else if (intensitasModel.getKabupaten().equals("populasi(m2)")){
            holder.txtOPT.setText(intensitasModel.getIntensitas_tambah()+" ekr/m2");
        }else if (intensitasModel.getKabupaten().equals("tidak mutlak")){
            holder.txtOPT.setText(intensitasModel.getIntensitas_tambah()+" %");
        }
        holder.txtStatus.setText(" ("+ intensitasModel.getDesa()+")");

        final String id = intensitasModel.getId_intensitas();

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailApprHarian.class);
                intent.putExtra("id_intensitas", id);
                intent.putExtra("id_hasil", intensitasModel.getId_hasil_pengamatan());
                intent.putExtra("status", intensitasModel.getDesa());
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