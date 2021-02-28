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

import com.majesty.siapopa.DetailHasil;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.HasilModel;
import java.util.ArrayList;
import java.util.List;

public class HasilAdapter extends RecyclerView.Adapter<HasilAdapter.MyViewHolder>{
    private Context mContext;
    private List<HasilModel> hasilModels = new ArrayList<>();


    public HasilAdapter (Context context,List<HasilModel> hasilModels){
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

//        holder.txtDate.setText(Html.fromHtml("<font color='#6200EE'><b>Tanggal: </b></font>" +hasilModel.getTanggal_pengamatan()));
        holder.txtDate.setText(hasilModel.getVarietas());
        holder.txtDesa.setText(hasilModel.getDesa());
        holder.txtVarietas.setText(hasilModel.getBlok());
        holder.txtOPT.setVisibility(View.GONE);
        holder.txtStatus.setVisibility(View.GONE);
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailHasil.class);

                intent.putExtra("id_hasil",hasilModel.getId_hasil());
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
