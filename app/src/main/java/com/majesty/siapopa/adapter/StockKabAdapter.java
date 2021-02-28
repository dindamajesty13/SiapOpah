package com.majesty.siapopa.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majesty.siapopa.R;
import com.majesty.siapopa.model.ModelStockKab;

import java.util.ArrayList;
import java.util.List;

public class StockKabAdapter extends RecyclerView.Adapter<StockKabAdapter.MyViewHolder> {

    private Context mContext;
    private List<ModelStockKab> stock = new ArrayList<>();


    public StockKabAdapter(Context context, List<ModelStockKab> stock){
        this.mContext = context;
        this.stock = stock;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDesa;
        private TextView txtJumlah;
//        private ImageView btnShow;
//        private ImageView btnEdit;

        public MyViewHolder (View itemView){
            super(itemView);

            txtDesa = itemView.findViewById(R.id.txtDesa);
            txtJumlah = itemView.findViewById(R.id.txtJumlah);
//            btnShow = itemView.findViewById(R.id.btn_show);
//            btnEdit = itemView.findViewById(R.id.btn_edit);
        }
    }


    @NonNull
    @Override
    public StockKabAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        return new StockKabAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockKabAdapter.MyViewHolder holder, int position) {

        final ModelStockKab modelStockKab = stock.get(position);
        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>Kabupaten: </b><br></font>"+modelStockKab.getKabupaten()));
        holder.txtJumlah.setText(Html.fromHtml("<font color='#6200EE'><b>Jumlah: </b><br></font>"+modelStockKab.getJumlah_pestisida()+"Liter/Kg"));


//        holder.btnShow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(mContext, EditStock.class);
//
//                intent.putExtra("id_wilayah",modelStockKab.getId_wilayah());
////                intent.putExtra("desa",desaModel.getDesa());
//
//                mContext.startActivity(intent);
//
//            }
//        });
//
//        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(mContext, EditStock.class);
//
//                intent.putExtra("id_wilayah",modelStockKab.getId_wilayah());
////                intent.putExtra("desa",desaModel.getDesa());
//
//                mContext.startActivity(intent);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return stock.size();
    }
}

