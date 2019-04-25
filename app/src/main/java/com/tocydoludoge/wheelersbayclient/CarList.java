package com.tocydoludoge.wheelersbayclient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.tocydoludoge.wheelersbayclient.Common.Common;
import com.tocydoludoge.wheelersbayclient.Database.Database;
import com.tocydoludoge.wheelersbayclient.Interface.ItemClickListerner;
import com.tocydoludoge.wheelersbayclient.Model.Car;
import com.tocydoludoge.wheelersbayclient.ViewHolder.CarViewerHolder;

import java.util.ArrayList;
import java.util.List;


public class CarList extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dbRef;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String categoryId = "";



    FirebaseRecyclerAdapter<Car,CarViewerHolder>adapter;

    SwipeRefreshLayout swipeRefreshLayout;



    //search function
    FirebaseRecyclerAdapter<Car,CarViewerHolder>searchAdapter;
    List<String>suggestList=new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    //Favorite
    Database localDB;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        //Firebase
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Cars");

        //Local db
        localDB= new Database(this);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Get Intent here
                if(getIntent() != null)
                    categoryId = getIntent().getStringExtra("CategoryId");
                if(!categoryId.isEmpty() && categoryId != null){

                    if(Common.isConnectedToInternet(getBaseContext()))

                        loadListFood(categoryId);
                    else {

                        Toast.makeText(CarList.this, "Connection Error!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //default load

                //Get Intent here
                if(getIntent() != null)
                    categoryId = getIntent().getStringExtra("CategoryId");
                if(!categoryId.isEmpty() && categoryId != null){

                    if(Common.isConnectedToInternet(getBaseContext()))

                        loadListFood(categoryId);
                    else {

                        Toast.makeText(CarList.this, "Connection Error!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_cars);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    //search
        materialSearchBar=(MaterialSearchBar)findViewById(R.id.search_bar);
        materialSearchBar.setHint("Enter Car Name");


        //load Suggest

        loadSuggest();//suggestion from firebase
        
    }

    private void loadSuggest() {
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<String>suggest=new ArrayList<String>();
                for(String search:suggestList){

                if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                    suggest.add(search);

                }
            materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

                //restore original search=h when not in use
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //show_comment_layout results after search

                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });


        dbRef.orderByChild("menuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){


                            Car item=postSnapshot.getValue(Car.class);
                            suggestList.add(item.getName());//add name of car to suggestion list
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void startSearch(CharSequence text) {
        searchAdapter=new FirebaseRecyclerAdapter<Car, CarViewerHolder>(

                Car.class,
                R.layout.car_item,
                CarViewerHolder.class,
                dbRef.orderByChild("name").equalTo(text.toString())//compares name alphabetically

        ) {
            @Override
            protected void populateViewHolder(CarViewerHolder viewHolder, Car model, int position) {


                viewHolder.car_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.car_image);

                final Car local=model;

                viewHolder.setItemClickListener(new ItemClickListerner() {
                    @Override
                    public void onClick(View view, int position, boolean islongClick) {
                        Toast.makeText(CarList.this, ""+local.getName(), Toast.LENGTH_SHORT).show();

                        Intent carDetail=new Intent(CarList.this,CarDetails.class);
                        carDetail.putExtra("carId",searchAdapter.getRef(position).getKey());
                        startActivity(carDetail);

                    }
                });

            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadListFood(String categoryId) {
        adapter=new FirebaseRecyclerAdapter<Car, CarViewerHolder>(Car.class,R.layout.car_item,
                 CarViewerHolder.class,dbRef.orderByChild("menuId")
                .equalTo(categoryId))
        {
            @Override
            protected void populateViewHolder(final CarViewerHolder viewHolder, final Car model, final int position) {



                viewHolder.car_name.setText(model.getName());
                viewHolder.car_rate.setText(String.format("Ksh %s", String.format("%s/Day", model.getRate())));
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.car_image);


                //Add Favorite
                    if(localDB.isFavorite(adapter.getRef(position).getKey()))
                        viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);

                    //click to change favorite state
                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!localDB.isFavorite(adapter.getRef(position).getKey()))
                        {

                            localDB.addToFavorite(adapter.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(CarList.this, ""+model.getName()+"was added to Favorites", Toast.LENGTH_SHORT).show();

                        }
                        else
                            {
                                localDB.removeFromCart(adapter.getRef(position).getKey());
                                viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                                Toast.makeText(CarList.this, ""+model.getName()+"was removed from Favorites", Toast.LENGTH_SHORT).show();

                            }
                    }
                });


                final Car local=model;

                viewHolder.setItemClickListener(new ItemClickListerner() {
                    @Override
                    public void onClick(View view, int position, boolean islongClick) {
                        Toast.makeText(CarList.this, ""+local.getName(), Toast.LENGTH_SHORT).show();

                        Intent carDetail=new Intent(CarList.this,CarDetails.class);
                        carDetail.putExtra("carId",adapter.getRef(position).getKey());
                        startActivity(carDetail);

                    }
                });
            }
        };
//adpter kick

        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }


}