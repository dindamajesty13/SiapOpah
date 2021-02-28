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

import com.majesty.siapopa.KecamatanList;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.KecamatanModel;

import java.util.ArrayList;
import java.util.List;

public class KabAdapter extends RecyclerView.Adapter<KabAdapter.MyViewHolder> {
    private Context mContext;
    private List<KecamatanModel> modelOpts = new ArrayList<>();


    public KabAdapter (Context context,List<KecamatanModel> modelOpts){
        this.mContext = context;
        this.modelOpts = modelOpts;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDesa;
        private TextView txtJumlah;
        private CardView cardView;

        public MyViewHolder (View itemView){
            super(itemView);

            txtDesa = itemView.findViewById(R.id.txtDesa);
            txtJumlah = itemView.findViewById(R.id.txtJumlah);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    @NonNull
    @Override
    public KabAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        return new KabAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KabAdapter.MyViewHolder holder, int position) {

        final KecamatanModel modelOpt = modelOpts.get(position);

        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>Kabupaten: </b><br></font>"+modelOpt.getKabupaten()));
        holder.txtJumlah.setVisibility(View.GONE);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, KecamatanList.class);
                intent.putExtra("kabupaten",modelOpt.getKabupaten());
                intent.putExtra("id", modelOpt.getId());
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);
            }
        });

//
    }

    @Override
    public int getItemCount() {
        return modelOpts.size();
    }
}

