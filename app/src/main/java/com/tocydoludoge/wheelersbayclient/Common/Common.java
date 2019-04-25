package com.tocydoludoge.wheelersbayclient.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import  com.tocydoludoge.wheelersbayclient.Model.User;


public class Common {
    public static User currentUser;
    public static final String DELETE = "Delete";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    public static final String INTENT_PLATE_ID="PlateId";

    public static String convertCodeToStatus(String status) {

        if(status.equals("0"))
            return "Reserved";
        else if(status.equals("1"))
            return "Vehicle Yet to be taken";
        else
            return "Taken";
    }

    public static boolean isConnectedToInternet(Context context){


        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager !=null){

            NetworkInfo[] infos=connectivityManager.getAllNetworkInfo();

            if(infos!=null){

                for(int i=0;i<=infos.length;i++){


                    if(infos[i].getState()==NetworkInfo.State.CONNECTED)
                        return true;
                }
            }

        }
        return  false;
    }

}
