package com.therelishprince.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.therelishprince.Remote.APIService;
import com.therelishprince.Remote.RetrofitClient;


public class Common {

    private static final String BASE_URL="https://fcm.googleapis.com/";

    public static APIService getFCMService()
    {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Your Order has been Confirmed*";
        else if (status.equals("1"))
            return "Your Order is on the way*";
        else if (status.equals("2"))
            return "Your Order has been Delivered*";
        else
            return "Your Order is being verified*";
    }


    public static  boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null)
        {
            NetworkInfo[] info=connectivityManager.getAllNetworkInfo();
            if (info!=null)
            {
                for (int i=0; i<info.length; i++)
                {
                    if (info[i].getState()==NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public static final String DELETE="DELETE";
}
