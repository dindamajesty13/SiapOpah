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

import com.majesty.siapopa.InputDetailLaporan;
import com.majesty.siapopa.R;
import com.majesty.siapopa.ShowDetailLaporan;
import com.majesty.siapopa.model.DesaModel;

import java.util.ArrayList;
import java.util.List;

public class DataDesaAdapter extends RecyclerView.Adapter<DataDesaAdapter.MyViewHolder>{

    private Context mContext;
    private List<DesaModel> desaModels = new ArrayList<>();


    public DataDesaAdapter(Context context, List<DesaModel> desaModels) {
        this.mContext = context;
        this.desaModels = desaModels;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDesa;
        private ImageView btnShow;
        private ImageView btnEdit;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtDesa = itemView.findViewById(R.id.txtDesa);
            btnShow = itemView.findViewById(R.id.btn_show);
            btnEdit = itemView.findViewById(R.id.btn_edit);

        }
    }

    @NonNull
    @Override
    public DataDesaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.data_desa_item,parent,false);
        return new DataDesaAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataDesaAdapter.MyViewHolder holder, int position) {

        final DesaModel desaModel = desaModels.get(position);

        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>Desa: </b><br></font>"+desaModel.getDesa()));

        holder.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ShowDetailLaporan.class);

                intent.putExtra("id",desaModel.getId_laporan());
                intent.putExtra("desa",desaModel.getDesa());

//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, InputDetailLaporan.class);

                intent.putExtra("id",desaModel.getId_laporan());
                intent.putExtra("desa",desaModel.getDesa());

//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return desaModels.size();
    }
}
