package com.tocydoludoge.wheelersbayclient;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tocydoludoge.wheelersbayclient.Common.Common;
import com.tocydoludoge.wheelersbayclient.Model.Category;
import com.tocydoludoge.wheelersbayclient.Model.Request;
import com.tocydoludoge.wheelersbayclient.ViewHolder.HireViewerHolder;
import com.tocydoludoge.wheelersbayclient.ViewHolder.MenuViewHolder;

public class HireStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public  RecyclerView.LayoutManager layoutManager;



    FirebaseRecyclerAdapter<Request, HireViewerHolder>adapter;
    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_status);



        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        recyclerView=(RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //load hire from common//local

        if (getIntent()==null)
        loadHires(Common.currentUser.getPhone());
        else
            loadHires(getIntent().getStringExtra("userPhone"));
    }


    private void loadHires(String phone) {


        adapter= new FirebaseRecyclerAdapter<Request, HireViewerHolder>(
                Request.class,
                R.layout.hire_layout,
                HireViewerHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(HireViewerHolder viewHolder, Request model, int position) {


                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderphone.setText(model.getPhone());



            }
        };
        recyclerView.setAdapter(adapter);

    }

}
