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
import com.majesty.siapopa.DetailStock;
import com.majesty.siapopa.R;
import com.majesty.siapopa.model.StockModel;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.MyViewHolder> {

    private Context mContext;
    private List<StockModel> stock = new ArrayList<>();


    public StockAdapter(Context context, List<StockModel> stock){
        this.mContext = context;
        this.stock = stock;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDate;
        private TextView txtDesa;
        private TextView txtVarietas;
        private TextView txtOPT;
        private TextView txtStatus;
        private ConstraintLayout mContainer;

        public MyViewHolder (View itemView){
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtDate);
            txtDesa = itemView.findViewById(R.id.txtDesa);
            txtVarietas = itemView.findViewById(R.id.txtVarietas);
            txtOPT = itemView.findViewById(R.id.txtOPT);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            mContainer = itemView.findViewById(R.id.constLayout);
        }
    }


    @NonNull
    @Override
    public StockAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.approval_item,parent,false);
        return new StockAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockAdapter.MyViewHolder holder, int position) {

        final StockModel stockModel = stock.get(position);

        holder.txtDate.setText(Html.fromHtml("<font color='#000000'><b>Tanggal: </b><br></font>" +stockModel.getTanggal_stock()));
        holder.txtDesa.setText(Html.fromHtml("<font color='#000000'><b>Provinsi: </b><br></font>"+stockModel.getProvinsi()));
        holder.txtOPT.setText(Html.fromHtml("<font color='#000000'><b>Pestisida: </b><br>"+stockModel.getPestisida()));
        holder.txtStatus.setText(Html.fromHtml("<font color='#000000'><b>Kabupaten: </b><br>"+stockModel.getKabupaten()));
        holder.txtVarietas.setText(Html.fromHtml("<font color='#000000'><b>Stock Akhir: </b><br>"+stockModel.getStock_akhir()+" Liter/Kg"));
        final String id = stockModel.getId_stock();

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailStock.class);
                intent.putExtra("id", id);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return stock.size();
    }
}
