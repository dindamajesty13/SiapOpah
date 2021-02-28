package com.majesty.siapopa.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majesty.siapopa.R;
import com.majesty.siapopa.ShowDetailPengamatan;
import com.majesty.siapopa.model.DetailModel;

import java.util.ArrayList;
import java.util.List;

public class DetailHarianAdapter extends RecyclerView.Adapter<DetailHarianAdapter.MyViewHolder>{

    private Context mContext;
    private List<DetailModel> detailModels = new ArrayList<>();


    public DetailHarianAdapter(Context context, List<DetailModel> detailModels) {
        this.mContext = context;
        this.detailModels = detailModels;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDesa, txtRumpun;
        private ImageView btnShow;
        private ImageView btnEdit;
        private ImageView btnDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtRumpun = itemView.findViewById(R.id.rumpun);
            txtDesa = itemView.findViewById(R.id.txtDesa);
            btnShow = itemView.findViewById(R.id.btn_show);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);

        }
    }

    @NonNull
    @Override
    public DetailHarianAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.data_desa_item,parent,false);
        return new DetailHarianAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailHarianAdapter.MyViewHolder holder, int position) {

        final DetailModel detailModel = detailModels.get(position);

        holder.txtRumpun.setText(Html.fromHtml("<font color='#6200EE'><b>Rumpun ke- </b></font>"+detailModel.getNomor_rumpun()));
        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>Jumlah Anakan: </b></font>"+detailModel.getJumlah_anakan()));

        holder.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ShowDetailPengamatan.class);

                intent.putExtra("id_detail",detailModel.getId_detail());
//                intent.putExtra("jumlah_anakan",detailModel.getJumlah_anakan());
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);

            }
        });
        holder.btnDelete.setVisibility(View.GONE);

        holder.btnEdit.setVisibility(View.GONE);

//        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(mContext, InputDetailHarian.class);
//
//                intent.putExtra("id_detail",detailModel.getId_detail());
////                intent.putExtra("desa",desaModel.getDesa());
//
//                mContext.startActivity(intent);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return detailModels.size();
    }
}
