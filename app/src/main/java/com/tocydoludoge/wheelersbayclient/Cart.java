package com.tocydoludoge.wheelersbayclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tocydoludoge.wheelersbayclient.Common.Common;
import com.tocydoludoge.wheelersbayclient.Database.Database;
import com.tocydoludoge.wheelersbayclient.Model.Hire;
import com.tocydoludoge.wheelersbayclient.Model.Request;
import com.tocydoludoge.wheelersbayclient.ViewHolder.CartAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    FButton btnPlace;

    List<Hire> cart=new ArrayList<>();
    CartAdapter adapter;


    @Override
     protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);


        //FB
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");


        //INit
        recyclerView=(RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice=(TextView)findViewById(R.id.total);
        btnPlace=(FButton)findViewById(R.id.btnPlaceOrder);



        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            if(cart.size()>0)
                requestDialogue();
        else {

                Toast.makeText(Cart.this, "Your Hire List is Empty!!", Toast.LENGTH_SHORT).show();
            }



            }
        });

        loadListCar();
    }

    private void requestDialogue() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter your Destination address");

        LayoutInflater inflater=this.getLayoutInflater();
        View addressComment=inflater.inflate(R.layout.physical_address,null);

        final MaterialEditText edtAddress=(MaterialEditText)addressComment.findViewById(R.id.permanentAddress);
        final MaterialEditText comment=(MaterialEditText)addressComment.findViewById(R.id.comment);

        alertDialog.setView(addressComment);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        "0",//status    21/6:34
                        comment.getText().toString(),

                        cart


                );


                //firebase submition

                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                //delete cart
                new Database(getBaseContext()).cleanCart();

                Toast.makeText(Cart.this, "Thank you,Hire Request Placed", Toast.LENGTH_SHORT).show();
                finish();
            }



        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();
            }


        });


        alertDialog.show();

    }

    private void loadListCar() {


    cart=new Database(this).getCarts();
    adapter=new CartAdapter(cart,this);
    adapter.notifyDataSetChanged();
    recyclerView.setAdapter(adapter);


    //cal total charges

        int total=0;
        for(Hire hire:cart)
            total+=(float)(Integer.parseInt(hire.getRate()))*(Integer.parseInt(hire.getDuration()));
        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
       if(item.getTitle().equals(Common.DELETE))
           deleteCart(item.getOrder());
        return true;

    }

    private void deleteCart(int position) {
        //remove item from hire list
        cart.remove(position);
        //clear sdqlite data
        new Database(this).cleanCart();
        //update new data to sqlite
        for(Hire item:cart)
            new Database(this).addToCart(item);
        //Refresh
        loadListCar();
    }
}
