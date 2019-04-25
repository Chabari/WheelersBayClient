package com.tocydoludoge.wheelersbayclient.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tocydoludoge.wheelersbayclient.Interface.ItemClickListerner;
import com.tocydoludoge.wheelersbayclient.R;

public class  CarViewerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView car_name,car_rate;
    public ImageView car_image,fav_image;

    private ItemClickListerner itemClickListener;


    public  void setItemClickListener(ItemClickListerner itemClickListener){

        this.itemClickListener=itemClickListener;
    }


    public CarViewerHolder(@NonNull View itemView) {
        super(itemView);


        car_name = itemView.findViewById(R.id.car_name);
        car_image= itemView.findViewById(R.id.car_image);
        fav_image= itemView.findViewById(R.id.fav);
        car_rate=itemView.findViewById(R.id.car_rate);

        itemView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {


        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
