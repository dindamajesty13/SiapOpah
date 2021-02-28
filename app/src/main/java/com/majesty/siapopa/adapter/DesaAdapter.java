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
import com.majesty.siapopa.model.WilayahModel;
import java.util.ArrayList;
import java.util.List;

public class DesaAdapter extends RecyclerView.Adapter<DesaAdapter.MyViewHolder> {
    private Context mContext;
    private List<WilayahModel> wilayah = new ArrayList<>();
    private OnItemClickListener mListener;


    public DesaAdapter(Context context, List<WilayahModel> wilayah) {
        this.mContext = context;
        this.wilayah = wilayah;

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = (OnItemClickListener) listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDesa;
        private ImageView btnAdd;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            txtDesa = itemView.findViewById(R.id.txtDesa);
            btnAdd = itemView.findViewById(R.id.btn_add);

        }
    }

    @NonNull
    @Override
    public DesaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.desa_item,parent,false);
        return new DesaAdapter.MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DesaAdapter.MyViewHolder holder, int position) {

        final WilayahModel wilayahModel = wilayah.get(position);

        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>Desa: </b><br></font>"+wilayahModel.getDesa()));

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, InputDetailLaporan.class);

                intent.putExtra("id_lokasi",wilayahModel.getId_lokasi());
                intent.putExtra("desa",wilayahModel.getDesa());
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
