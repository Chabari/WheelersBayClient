package com.tocydoludoge.wheelersbayclient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;
import com.tocydoludoge.wheelersbayclient.Common.Common;
import com.tocydoludoge.wheelersbayclient.Database.Database;
import com.tocydoludoge.wheelersbayclient.Model.Car;
import com.tocydoludoge.wheelersbayclient.Model.Category;
import com.tocydoludoge.wheelersbayclient.Model.Hire;
import com.tocydoludoge.wheelersbayclient.Model.Rating;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Arrays;

import info.hoang8f.widget.FButton;

public class CarDetails extends AppCompatActivity implements RatingDialogListener {


    TextView car_name, car_rate, car_description;
    ImageView car_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart,btnRating;
    RatingBar ratingBar;

    String carID = "";
    FButton btnComment;

    FirebaseDatabase database;
    DatabaseReference dbRef,ratingTbl;
    Car currentCar;
    ElegantNumberButton numberButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Cars");
        ratingTbl=(database).getReference("Rating");


        numberButton=(ElegantNumberButton)findViewById(R.id.number_button);
        car_name = (TextView) findViewById(R.id.car_name);
        car_rate = (TextView) findViewById(R.id.car_rate);
        car_description = (TextView) findViewById(R.id.car_description);
        car_image = (ImageView) findViewById(R.id.img_car);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        btnRating=(FloatingActionButton)findViewById(R.id.btnRating);

        btnComment=(FButton)findViewById(R.id.btnComments);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cc=new Intent(CarDetails.this,ShowComment.class);
                cc.putExtra(Common.INTENT_PLATE_ID,carID);
                startActivity(cc);
            }
        });

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Hire(carID,
                        currentCar.getName(),
                        numberButton.getNumber(),
                        currentCar.getRate(),
                        currentCar.getDiscount()

                ));

                Toast.makeText(CarDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }

        });




        if(getIntent()!=null)
            carID=getIntent().getStringExtra("carId");
        if(!carID.isEmpty()){


            if(Common.isConnectedToInternet(getBaseContext()))
            {
                getCarDetail(carID);
                getRatingCar(carID);
            }
            else {
                Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show();
                return;
            }
        }


    }

    private void getRatingCar(String carID) {
        Query carRating=ratingTbl.orderByChild("PlateId").equalTo(carID);
        carRating.addValueEventListener(new ValueEventListener() {
            int sum=0,count=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item=postSnapshot.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getRateValue());
                    count++;
                }
          if(count !=0)
          {
              float average=sum/count;
              ratingBar.setRating(average);
          }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {
    new AppRatingDialog.Builder()
            .setPositiveButtonText("Submit")
            .setNegativeButtonText("Cancel")
            .setNoteDescriptions(Arrays.asList("Very Bad","Quite Good","Good","Very Good","Excellent"))
            .setDefaultRating(1)
            .setTitle("Rate This Car")
            .setDescription("Please Select Stars and send us Your FeedBack")
            .setTitleTextColor(R.color.colorPrimary)
            .setDescriptionTextColor(R.color.colorPrimary)
            .setHint("Please write comment here..")
            .setHintTextColor(R.color.colorAccent)
            .setCommentTextColor(android.R.color.white)
            .setCommentBackgroundColor(R.color.colorPrimaryDark)
            .setWindowAnimation(R.style.RatingDialogFadeAnim)
            .create(CarDetails.this)
            .show();

    }

    private void getCarDetail(final String carId) {


        dbRef.child(carId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentCar=dataSnapshot.getValue(Car.class);

                //set Image

                Picasso.with(getBaseContext()).load(currentCar.getImage()).into(car_image);


                collapsingToolbarLayout.setTitle(currentCar.getName());

                car_rate.setText(currentCar.getRate());
                car_name.setText(currentCar.getName());
                car_description.setText(currentCar.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, @NotNull String comments) {


        //get rating and  upload to firebase

        final Rating rating=new Rating(Common.currentUser.getPhone(),
                carID,
                String.valueOf(value),
                comments);
        ratingTbl.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Common.currentUser.getPhone()).exists())
                {
                    //Remove Old value
                    ratingTbl.child(Common.currentUser.getPhone()).removeValue();
                    //update value
                    ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                }
                else
                    {
                        //add value
                        ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                    }

                Toast.makeText(CarDetails.this, "Thank you for Your Rating", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}