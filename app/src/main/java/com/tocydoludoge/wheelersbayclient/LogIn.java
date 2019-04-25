package com.tocydoludoge.wheelersbayclient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;
import com.tocydoludoge.wheelersbayclient.Common.Common;
import com.tocydoludoge.wheelersbayclient.Model.User;

import info.hoang8f.widget.FButton;
import io.paperdb.Paper;

public class LogIn extends AppCompatActivity {

    TextView txtPhone, txtPassword,txtForgotPass;
    Button login;
    CheckBox checkBox;

    FirebaseDatabase database;
   DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        txtPhone = (TextView) findViewById(R.id.edtPhone);
        txtPassword = (TextView) findViewById(R.id.edtPassword);
        login = (FButton) findViewById(R.id.btnSignIn);
        checkBox=(CheckBox)findViewById(R.id.ckbRem);
        txtForgotPass=(TextView)findViewById(R.id.txtForgotPass);

        //init paper
        Paper.init(this);

        //firebase initilization
       database= FirebaseDatabase.getInstance();
        users = database.getReference("User");

       txtForgotPass.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showForgotPassDialog();
           }
       });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    //save number and password
                if(checkBox.isChecked()){
                    Paper.book().write(Common.USER_KEY,txtPhone.getText().toString());
                    Paper.book().write(Common.PWD_KEY,txtPassword.getText().toString());

                }

                    final ProgressDialog mDialog = new ProgressDialog(LogIn.this);
                    mDialog.setMessage("Please Wait..");
                    mDialog.show();


                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            //check if user not exist in database
                            if (dataSnapshot.child(txtPhone.getText().toString()).exists()) {
                                //get user information
                                mDialog.dismiss();
                                User user = dataSnapshot.child(txtPhone.getText().toString()).getValue(User.class);
                                user.setPhone(txtPhone.getText().toString());
                                if (user.getPassword().equals(txtPassword.getText().toString())) {

                                    Intent homeIntent = new Intent(LogIn.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();

                                    users.removeEventListener(this);

                                } else {

                                    Toast.makeText(LogIn.this, "Wrong Password !", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(LogIn.this, "User not exist in Database !", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {

                    Toast.makeText(LogIn.this, "Check Internet Connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });

    }

    private void showForgotPassDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");
        builder.setMessage("Enter Your Secure Code");

        LayoutInflater inflater=this.getLayoutInflater();
        View forgotPass=inflater.inflate(R.layout.forgot_pass,null);

        builder.setView(forgotPass);
        builder.setIcon(R.drawable.ic_security_black_24dp);

        final MaterialEditText edtPhone=(MaterialEditText)forgotPass.findViewById(R.id.edtPhone);
        final MaterialEditText edtSecureCode=(MaterialEditText)forgotPass.findViewById(R.id.edtSecureCode);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            //check if user is available
                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                        if(user.getSecureCode().equals(edtSecureCode.getText().toString()))
                            Toast.makeText(LogIn.this, "Your Password: " +user.getPassword(), Toast.LENGTH_SHORT).show();
                    else
                            Toast.makeText(LogIn.this, "Wrong Secure Code!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
});
    builder.show();
    }
}

