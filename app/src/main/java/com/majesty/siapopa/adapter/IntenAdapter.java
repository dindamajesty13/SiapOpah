package com.majesty.siapopa.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majesty.siapopa.R;
import com.majesty.siapopa.model.IntensitasModel;

import java.util.ArrayList;
import java.util.List;

public class IntenAdapter extends RecyclerView.Adapter<IntenAdapter.MyViewHolder>{

    private Context mContext;
    private List<IntensitasModel> detailModels = new ArrayList<>();


    public IntenAdapter(Context context, List<IntensitasModel> detailModels) {
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
    public IntenAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.data_desa_item,parent,false);
        return new IntenAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IntenAdapter.MyViewHolder holder, int position) {

        final IntensitasModel detailModel = detailModels.get(position);

        if(detailModel.getPetugas_pengamatan().equals("mutlak")){
            holder.txtRumpun.setText(Html.fromHtml("<font color='#6200EE'><b>OPT: </b></font>"+detailModel.getJenis_opt()+": "+detailModel.getIntensitas_tambah()+" %"));
        }else if (detailModel.getPetugas_pengamatan().equals("tidak mutlak")){
            holder.txtRumpun.setText(Html.fromHtml("<font color='#6200EE'><b>OPT: </b></font>"+detailModel.getJenis_opt()+": "+detailModel.getIntensitas_tambah()+" %"));
        }else if (detailModel.getPetugas_pengamatan().equals("populasi")){
            holder.txtRumpun.setText(Html.fromHtml("<font color='#6200EE'><b>Populasi: </b></font>"+detailModel.getJenis_opt()+": "+detailModel.getIntensitas_tambah()+" e/r"));
        }else if (detailModel.getPetugas_pengamatan().equals("populasi(m2)")){
            holder.txtRumpun.setText(Html.fromHtml("<font color='#6200EE'><b>Populasi: </b></font>"+detailModel.getJenis_opt()+": "+detailModel.getIntensitas_tambah()+" e/m2"));
        }else if (detailModel.getPetugas_pengamatan().equals("MA")){
            holder.txtRumpun.setText(Html.fromHtml("<font color='#6200EE'><b>MA: </b></font>"+detailModel.getJenis_opt()+": "+detailModel.getIntensitas_tambah()+" e/r"));
        }

//        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>Intensitas: </b></font>"++" %"));
        holder.txtDesa.setVisibility(View.GONE);

        holder.btnShow.setVisibility(View.GONE);
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

