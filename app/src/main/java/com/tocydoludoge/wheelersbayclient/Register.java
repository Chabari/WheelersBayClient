package com.tocydoludoge.wheelersbayclient;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tocydoludoge.wheelersbayclient.Common.Common;
import com.tocydoludoge.wheelersbayclient.Model.User;

import info.hoang8f.widget.FButton;

public class Register extends AppCompatActivity {

    TextView txtPhone, txtName, txtPassword,edtsecureCode;
    Button register;
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        txtName = (MaterialEditText) findViewById(R.id.edtName);
        txtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        txtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        register = (FButton) findViewById(R.id.btnSignUp);
        edtsecureCode=(MaterialEditText)findViewById(R.id.edtSecureCode);

        //FB Connection

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("User");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    final ProgressDialog mDialog = new ProgressDialog(Register.this);
                    mDialog.setMessage("Please Wating...");
                    mDialog.show();

                    users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if user not exist in database
                            if (dataSnapshot.child(txtPhone.getText().toString()).exists()) {
                                //get user information
                                mDialog.dismiss();
                                User user = dataSnapshot.child(txtPhone.getText().toString()).getValue(User.class);
                                Toast.makeText(Register.this, "Phone number already registered !", Toast.LENGTH_SHORT).show();

                            } else {
                                mDialog.dismiss();
                                User user = new User(txtPhone.getText().toString(), txtPassword.getText().toString()
                                ,edtsecureCode.getText().toString());
                                users.child(txtPhone.getText().toString()).setValue(user);
                                Toast.makeText(Register.this, "Sign Up Successfully !", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else {
                    Toast.makeText(Register.this, "Check Internet Connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


    }
}
