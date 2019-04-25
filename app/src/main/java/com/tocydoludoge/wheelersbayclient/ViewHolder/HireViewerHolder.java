package com.tocydoludoge.wheelersbayclient.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tocydoludoge.wheelersbayclient.Interface.ItemClickListerner;
import com.tocydoludoge.wheelersbayclient.R;

public class HireViewerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId, txtOrderStatus, txtOrderphone, txtOrderAddress;

    private ItemClickListerner itemClickListener;


    public HireViewerHolder(@NonNull View itemView) {
        super(itemView);



        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderphone = itemView.findViewById(R.id.order_address);

        itemView.setOnClickListener(this);

    }


    public void setItemClickListener(ItemClickListerner itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);


    }
}
