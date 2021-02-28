package com.majesty.siapopa.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.majesty.siapopa.R;
import com.majesty.siapopa.model.ModelOpt;

import java.util.ArrayList;
import java.util.List;

public class ShowOptAdapter extends RecyclerView.Adapter<ShowOptAdapter.MyViewHolder> {
    private Context mContext;
    private List<ModelOpt> modelOpts = new ArrayList<>();


    public ShowOptAdapter (Context context,List<ModelOpt> modelOpts){
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
    public ShowOptAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        return new ShowOptAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowOptAdapter.MyViewHolder holder, int position) {

        final ModelOpt modelOpt = modelOpts.get(position);

        holder.txtDesa.setText(Html.fromHtml("<font color='#6200EE'><b>OPT: </b><br></font>"+modelOpt.getOpt()));
        holder.txtJumlah.setText(Html.fromHtml("<font color='#6200EE'><b>Jumlah: </b><br></font>"+modelOpt.getJumlah()));

//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(mContext, EditOPT.class);
//
//                intent.putExtra("id",modelOpt.getId_jenisOPT());
////                intent.putExtra("desa",modelOpt.getDesa());
//
//                mContext.startActivity(intent);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return modelOpts.size();
    }
}

