package com.tocydoludoge.wheelersbayclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tocydoludoge.wheelersbayclient.Common.Common;
import com.tocydoludoge.wheelersbayclient.Model.User;

import info.hoang8f.widget.FButton;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {


    Button login,register;
    TextView slogan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login=(FButton)findViewById(R.id.btnSignIn);
        register=(FButton)findViewById(R.id.btnSignUp);
        slogan=(TextView)findViewById(R.id.txtSlogan);

        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/Nabila.ttf");
        slogan.setTypeface(typeface);

        //paper init
        Paper.init(this);


       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent vic=new Intent(MainActivity.this,LogIn.class);
               startActivity(vic);



           }
       });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg=new Intent(MainActivity.this,Register.class);
                startActivity(reg);

            }
        });

        //check remember
        String user=Paper.book().read(Common.USER_KEY);
        String pwd=Paper.book().read(Common.PWD_KEY);
        if(user !=null && pwd !=null){

            if(!user.isEmpty()&& !pwd.isEmpty())
                autoLogin(user,pwd);
        }



    }

    private void autoLogin(final String phone, final String pwd) {



        //firebase initilization
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("User");


        if (Common.isConnectedToInternet(getBaseContext())) {


            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please Wait..");
            mDialog.show();


            users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    //check if user not exist in database
                    if (dataSnapshot.child(phone).exists()) {
                        //get user information
                        mDialog.dismiss();
                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);
                        if (user.getPassword().equals(pwd)) {

                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();

                        } else {


                            Toast.makeText(MainActivity.this, "Wrong Password !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "User not exist in Database !", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {

            Toast.makeText(MainActivity.this, "Check Internet Connection!", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
