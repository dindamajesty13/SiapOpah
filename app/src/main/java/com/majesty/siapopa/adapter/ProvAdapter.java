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

import com.majesty.siapopa.DetailLaporan;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.IntensitasModel;

import java.util.ArrayList;
import java.util.List;

public class ProvAdapter extends RecyclerView.Adapter<ProvAdapter.MyViewHolder> {

    private Context mContext;
    private List<IntensitasModel> laporan = new ArrayList<>();


    public ProvAdapter(Context context, List<IntensitasModel> laporan){
        this.mContext = context;
        this.laporan = laporan;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtBatas;
        private TextView txtAlamat;
        private TextView txtPetugas;
        private TextView txtOPT;
        private TextView txtIntensitas;
        private TextView txtLuas;
        private CardView mContainer;

        public MyViewHolder (View itemView){
            super(itemView);

            txtBatas = (TextView) itemView.findViewById(R.id.txtBatas);
            txtOPT = (TextView) itemView.findViewById(R.id.txtOPT);
            txtAlamat = (TextView) itemView.findViewById(R.id.txtAlamat);
            txtPetugas = (TextView) itemView.findViewById(R.id.txtPetugas);
            txtIntensitas = (TextView) itemView.findViewById(R.id.txtIntensitas);
            txtLuas = (TextView) itemView.findViewById(R.id.txtLuas);
            mContainer = (CardView) itemView.findViewById(R.id.cvtxt);
        }
    }


    @NonNull
    @Override
    public ProvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.prov_item,parent,false);
        return new ProvAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProvAdapter.MyViewHolder holder, int position) {

        final IntensitasModel intensitasModel = laporan.get(position);

        holder.txtBatas.setText(Html.fromHtml("<font color='#ff0000'><b>Batas Waktu Approval: </b><br></font>" + intensitasModel.getBatas_waktu()));
        holder.txtOPT.setText(Html.fromHtml("<font color='#ff0000'><b>Jenis OPT: </b><br></font>" + intensitasModel.getJenis_opt()));
        holder.txtPetugas.setText(Html.fromHtml("<font color='#ff0000'><b>Petugas Pengamatan: </b><br></font>" + intensitasModel.getPetugas_pengamatan()));
        holder.txtAlamat.setText(Html.fromHtml("<font color='#ff0000'><b>Alamat Lengkap: </b><br></font>" + intensitasModel.getAlamat()));
        holder.txtLuas.setText(Html.fromHtml("<font color='#ff0000'><b>Luas: </b><br></font>" + intensitasModel.getLuas_keadaan_serang()));
        holder.txtIntensitas.setText(Html.fromHtml("<font color='#ff0000'><b>Intensitas: </b><br></font>" + intensitasModel.getIntensitas_keadaan()));

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
