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

import com.majesty.siapopa.DetailLaporan;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.IntensitasModel;

import java.util.ArrayList;
import java.util.List;


public class ApprovalKabAdapter extends RecyclerView.Adapter<ApprovalKabAdapter.MyViewHolder> {

    private Context mContext;
    private List<IntensitasModel> laporan = new ArrayList<>();


    public ApprovalKabAdapter(Context context, List<IntensitasModel> laporan) {
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

        public MyViewHolder(View itemView) {
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
    public ApprovalKabAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.lapor_item, parent, false);
        return new ApprovalKabAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApprovalKabAdapter.MyViewHolder holder, int position) {

        final IntensitasModel intensitasModel = laporan.get(position);

        holder.txtDesa.setText(intensitasModel.getJenis_opt());
        holder.txtVarietas.setText(intensitasModel.getDesa());
        holder.txtDate.setText(intensitasModel.getLuas_tambah_serang()+" Ha");
        holder.txtOPT.setText(intensitasModel.getLuas_keadaan_serang()+" Ha");
        holder.txtStatus.setText(intensitasModel.getPeriode_pengamatan());
        final String id = intensitasModel.getId_intensitas();

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailLaporan.class);
                intent.putExtra("id_intensitas", id);
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
