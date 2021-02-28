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

import com.majesty.siapopa.DetailLapHarian;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.HasilModel;

import java.util.ArrayList;
import java.util.List;

public class HarianAdapter extends RecyclerView.Adapter<HarianAdapter.MyViewHolder>{
    private Context mContext;
    private List<HasilModel> hasilModels = new ArrayList<>();


    public HarianAdapter (Context context,List<HasilModel> hasilModels){
        this.mContext = context;
        this.hasilModels = hasilModels;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDesa;
        private TextView txtVarietas;
        private TextView txtDate;
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.lapor_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final HasilModel hasilModel = hasilModels.get(position);

        holder.txtDate.setText(hasilModel.getLuas_diamati()+" Ha");
//        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>OPT: </b></font>"+hasilModel.getOpt()));
        holder.txtDesa.setText(hasilModel.getOpt());
        holder.txtVarietas.setText(hasilModel.getBlok());
//        holder.txtVarietas.setText(Html.fromHtml("<font color='#6200EE'><b>Intensitas: </b></font>"+hasilModel.getTotal_intensitas()));
        if (hasilModel.getKabupaten().equals("mutlak")){
            holder.txtOPT.setText(hasilModel.getTotal_intensitas()+" %");
        }else if (hasilModel.getKabupaten().equals("populasi")){
            holder.txtOPT.setText(hasilModel.getTotal_intensitas()+" ekr/rpn");
        }else if (hasilModel.getKabupaten().equals("populasi(m2)")){
            holder.txtOPT.setText(hasilModel.getTotal_intensitas()+" ekr/m2");
        }else if (hasilModel.getKabupaten().equals("tidak mutlak")){
            holder.txtOPT.setText(hasilModel.getTotal_intensitas()+" %");
        }
        holder.txtStatus.setText(" ("+ hasilModel.getPetugas_pengamatan()+")");

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailLapHarian.class);

                intent.putExtra("id",hasilModel.getId_hasil());
                intent.putExtra("id_hasil",hasilModel.getKecamatan());
                intent.putExtra("status",hasilModel.getPetugas_pengamatan());
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
