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

import com.majesty.siapopa.DetailWilayah;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.WilayahModel;

import java.util.ArrayList;
import java.util.List;

public class WilayahAdapter extends RecyclerView.Adapter<WilayahAdapter.MyViewHolder> {
    private Context mContext;
    private List<WilayahModel> wilayah = new ArrayList<>();


    public WilayahAdapter (Context context,List<WilayahModel> wilayah){
        this.mContext = context;
        this.wilayah = wilayah;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDesa;
        private TextView txtAlamat;
        private ConstraintLayout mContainer;

        public MyViewHolder (View itemView){
            super(itemView);

            txtDesa = itemView.findViewById(R.id.txtDesa);
            txtAlamat = itemView.findViewById(R.id.txtAlamat);
            mContainer = itemView.findViewById(R.id.constLayout);
        }
    }


    @NonNull
    @Override
    public WilayahAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.wilayah_item,parent,false);
        return new WilayahAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WilayahAdapter.MyViewHolder holder, int position) {

        final WilayahModel wilayahModel = wilayah.get(position);

        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>Desa: </b><br></font>"+wilayahModel.getDesa()));
        holder.txtAlamat.setText(Html.fromHtml("<font color='#6200EE'><b>Alamat: </b><br></font>"+wilayahModel.getAlamat()));

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailWilayah.class);

                intent.putExtra("id",wilayahModel.getId_lokasi());
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return wilayah.size();
    }
}
