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

import com.majesty.siapopa.EditMa;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.ModelOpt;

import java.util.ArrayList;
import java.util.List;


public class MaAdapter extends RecyclerView.Adapter<MaAdapter.MyViewHolder> {
    private Context mContext;
    private List<ModelOpt> modelOpts = new ArrayList<>();


    public MaAdapter (Context context,List<ModelOpt> modelOpts){
        this.mContext = context;
        this.modelOpts = modelOpts;
    }

    private MaAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(MaAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(ModelOpt modelOpt);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDesa;
        private ImageView btnShow;
        private ImageView btnEdit;
        private ImageView btnDelete;

        public MyViewHolder (View itemView){
            super(itemView);

            txtDesa = itemView.findViewById(R.id.txtDesa);
            btnShow = itemView.findViewById(R.id.btn_show);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    @NonNull
    @Override
    public MaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.opt_item,parent,false);
        return new MaAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MaAdapter.MyViewHolder holder, int position) {

        final ModelOpt modelOpt = modelOpts.get(position);

        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>OPT: </b><br></font>"+modelOpt.getOpt()));

        holder.btnShow.setVisibility(View.GONE);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onItemClickCallback.onItemClicked(modelOpts.get(holder.getAdapterPosition()));

                Intent intent = new Intent(mContext, EditMa.class);

                intent.putExtra("id",modelOpt.getId_jenisOPT());
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("desa",modelOpt.getDesa());

                mContext.startActivity(intent);

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(modelOpts.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelOpts.size();
    }
}
