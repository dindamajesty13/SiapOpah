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
import com.majesty.siapopa.model.ModelMa;
import com.majesty.siapopa.model.ModelOpt;

import java.util.ArrayList;
import java.util.List;

public class AdapterMa extends RecyclerView.Adapter<AdapterMa.MyViewHolder> {
    private Context mContext;
    private List<ModelMa> modelOpts = new ArrayList<>();


    public AdapterMa (Context context,List<ModelMa> modelOpts){
        this.mContext = context;
        this.modelOpts = modelOpts;
    }

    private AdapterMa.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(AdapterMa.OnItemClickCallback onItemClickCallback) {
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
    public AdapterMa.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.opt_item,parent,false);
        return new AdapterMa.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterMa.MyViewHolder holder, int position) {

        final ModelMa modelOpt = modelOpts.get(position);

        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>Musuh Alami: </b><br></font>"+modelOpt.getMa()+ " ("+modelOpt.getJumlah()+" ekor/rumpun)"));

        holder.btnShow.setVisibility(View.GONE);

        holder.btnEdit.setVisibility(View.GONE);

        holder.btnDelete.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return modelOpts.size();
    }
}
