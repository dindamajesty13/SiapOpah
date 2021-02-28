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

import com.majesty.siapopa.DetailLapor;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.HasilModel;

import java.util.ArrayList;
import java.util.List;

public class IntensitasAdapter extends RecyclerView.Adapter<IntensitasAdapter.MyViewHolder>{
    private Context mContext;
    private List<HasilModel> hasilModels = new ArrayList<>();


    public IntensitasAdapter (Context context,List<HasilModel> hasilModels){
        this.mContext = context;
        this.hasilModels = hasilModels;
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.lapor_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final HasilModel hasilModel = hasilModels.get(position);

        holder.txtDesa.setText(hasilModel.getOpt());
        holder.txtVarietas.setText(hasilModel.getDesa());
        holder.txtStatus.setText(hasilModel.getLuas_diamati()+" Ha");
        if (hasilModel.getFrek_kimia().equals("mutlak")){
            holder.txtOPT.setText(hasilModel.getTotal_intensitas()+" %");
        }else if (hasilModel.getFrek_kimia().equals("populasi")){
            holder.txtOPT.setText(hasilModel.getTotal_intensitas()+" ekr/rpn");
        }else if (hasilModel.getFrek_kimia().equals("populasi(m2)")){
            holder.txtOPT.setText(hasilModel.getTotal_intensitas()+" ekr/m2");
        }else if (hasilModel.getFrek_kimia().equals("tidak mutlak")){
            holder.txtOPT.setText(hasilModel.getTotal_intensitas()+" %");
        }
//        holder.txtOPT.setText(hasilModel.getTotal_intensitas()+" %");
        holder.txtDate.setText(hasilModel.getVarietas());

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailLapor.class);

                intent.putExtra("opt",hasilModel.getOpt());
                intent.putExtra("kabupaten",hasilModel.getKabupaten());
                intent.putExtra("kecamatan",hasilModel.getKecamatan());
                intent.putExtra("desa",hasilModel.getDesa());
                intent.putExtra("umur_tanaman",hasilModel.getDari_umur());
                intent.putExtra("luas_tambah",hasilModel.getLuas_diamati());
                intent.putExtra("periode_pengamatan",hasilModel.getTanggal_pengamatan());
                intent.putExtra("luas_tanaman",hasilModel.getLuas_hamparan());
                intent.putExtra("komoditas",hasilModel.getKomoditas());
                intent.putExtra("varietas",hasilModel.getVarietas());
                intent.putExtra("intensitas_tambah",hasilModel.getTotal_intensitas());
                intent.putExtra("luas_waspada",hasilModel.getLuas_persemaian());
                intent.putExtra("tambah_ringan",hasilModel.getBlok());
                intent.putExtra("tambah_berat",hasilModel.getProvinsi());
                intent.putExtra("tambah_sedang",hasilModel.getHingga_umur());
                intent.putExtra("tambah_puso",hasilModel.getPola_tanam());
                intent.putExtra("status",hasilModel.getFrek_kimia());
//                intent.putExtra("kimia",hasilModel.getKimia());
//                intent.putExtra("nabati",hasilModel.getNabati());
//                intent.putExtra("eradikasi",hasilModel.getEradikasi());
//                intent.putExtra("cara_lain",hasilModel.getCara_lain());
//                intent.putExtra("frek_kimia",hasilModel.getFrek_kimia());
//                intent.putExtra("frek_nabati",hasilModel.getFrek_nabati());
//                intent.putExtra("jumlah_pengendalian",hasilModel.getJumlah_pengendalian());


                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return hasilModels.size();
    }
}
