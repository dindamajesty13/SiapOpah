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

import com.majesty.siapopa.EditPopulasi;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.ModelOpt;

import java.util.ArrayList;
import java.util.List;

public class PopulasiAdapter extends RecyclerView.Adapter<PopulasiAdapter.MyViewHolder> {
    private Context mContext;
    private List<ModelOpt> modelOpts = new ArrayList<>();


    public PopulasiAdapter(Context context, List<ModelOpt> modelOpts){
        this.mContext = context;
        this.modelOpts = modelOpts;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDesa;
        private ImageView btnShow;
        private ImageView btnEdit;

        public MyViewHolder (View itemView){
            super(itemView);

            txtDesa = itemView.findViewById(R.id.txtDesa);
            btnShow = itemView.findViewById(R.id.btn_show);
            btnEdit = itemView.findViewById(R.id.btn_edit);
        }
    }

    @NonNull
    @Override
    public PopulasiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.opt_item,parent,false);
        return new PopulasiAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopulasiAdapter.MyViewHolder holder, int position) {

        final ModelOpt modelOpt = modelOpts.get(position);

        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>OPT: </b><br></font>"+modelOpt.getOpt()));

        holder.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, EditPopulasi.class);

                intent.putExtra("id",modelOpt.getId_jenisOPT());
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("desa",modelOpt.getDesa());

                mContext.startActivity(intent);

            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, EditPopulasi.class);

                intent.putExtra("id",modelOpt.getId_jenisOPT());
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("desa",modelOpt.getDesa());

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelOpts.size();
    }
}
