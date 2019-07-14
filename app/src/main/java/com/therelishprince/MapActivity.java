package com.therelishprince;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.therelishprince.Common.Common;
import com.therelishprince.Database.Database;
import com.therelishprince.Remote.APIService;
import com.therelishprince.ViewHolder.CartAdapter;
import com.therelishprince.model.MyResponse;
import com.therelishprince.model.Notification;
import com.therelishprince.model.Order;
import com.therelishprince.model.Request;
import com.therelishprince.model.Sender;
import com.therelishprince.model.Token;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity {

    TextInputEditText mCity,mLocality,mFlatNumber,mLandmark,mName,mPhone;
    FButton mPlaceBtn;
    String mCurrentUser;

    FirebaseDatabase database;
    DatabaseReference requestReference;
    DatabaseReference statusReference;

    List<Order> cart=new ArrayList<>();

    CartAdapter adapter;
    APIService mService;
    private ProgressDialog mProgrss;

    private static final String TAG ="MapActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mService=Common.getFCMService();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Location");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database=FirebaseDatabase.getInstance();
        requestReference=database.getReference("Requests");


        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        mCity=(TextInputEditText)findViewById(R.id.txtCity);
        mLocality=(TextInputEditText)findViewById(R.id.txtLocality);
        mFlatNumber=(TextInputEditText)findViewById(R.id.txtFlatNumber);
        mLandmark=(TextInputEditText)findViewById(R.id.txtLandmark);
        mName=(TextInputEditText)findViewById(R.id.txtName);
        mPhone=(TextInputEditText)findViewById(R.id.txtPhone);
        mPlaceBtn=(FButton)findViewById(R.id.btnPlace);
        mPlaceBtn.setEnabled(false);


        statusReference=database.getReference().child("status");

        statusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String status= dataSnapshot.child("online").getValue().toString();
                if (status.equals("false"))
                {
                    mPlaceBtn.setEnabled(false);
                    Toast.makeText(MapActivity.this,"The Relish Prince is currently Closed",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter autocompleteFilter=new AutocompleteFilter.Builder().setCountry("IND").build();
        autocompleteFragment.setFilter(autocompleteFilter);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener()
        {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                String placeName= place.getName().toString();
                String addressName=place.getAddress().toString();


                placeName=placeName.replaceAll("'", "\''");
                addressName=addressName.replaceAll("'", "\''");


                boolean isExist=new Database(getBaseContext()).checkAddress(placeName,addressName);

                if (Common.isConnectedToInternet(MapActivity.this))
                {
                    if (!isExist) {

                        showAlertDialog(placeName);
                        //Toast.makeText(MapActivity.this,"Sorry to Say! The Relish Prince Doesn't Deliver Here..",Toast.LENGTH_LONG).show();
                    } else {
                        mCity.setVisibility(View.VISIBLE);
                        mLocality.setVisibility(View.VISIBLE);
                        mFlatNumber.setVisibility(View.VISIBLE);
                        mLandmark.setVisibility(View.VISIBLE);
                        mName.setVisibility(View.VISIBLE);
                        mPhone.setVisibility(View.VISIBLE);
                        mCity.setText("Bhopal");
                        mLocality.setText(addressName);
                        mPhone.setText(mCurrentUser);
                        mPlaceBtn.setEnabled(true);

                    }

                }
                else
                {
                    Toast.makeText(MapActivity.this,"Please check your Internet Connection!!",Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);

            }

        });
        final String mPrice=getIntent().getStringExtra("totalPrice");
        cart= new Database(this).getCarts(mCurrentUser);

        mPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgrss= new ProgressDialog(MapActivity.this);
                mProgrss.setMessage("Please wait");
                mProgrss.setCanceledOnTouchOutside(false);
                mProgrss.show();
                String locality=mLocality.getText().toString().trim();
                String flat=mFlatNumber.getText().toString().trim();
                String landmark=mLandmark.getText().toString().trim();
                String name=mName.getText().toString().trim();

                Random rnd = new Random();
                String n = String.valueOf(100000 + rnd.nextInt(900000));


                String fullAddress= name+", "+flat+", "+locality+", Near- "+landmark;

                if (TextUtils.isEmpty(landmark)&&TextUtils.isEmpty(name)){
                    Toast.makeText(MapActivity.this,"Fields can not be Emplty!!",Toast.LENGTH_SHORT).show();
                }else {
                    Request request= new Request(
                            n,
                            mCurrentUser,
                            mPhone.getText().toString(),
                            mName.getText().toString(),
                            fullAddress,
                            mPrice,
                            "3",
                            cart
                    );

                    String order_number=String.valueOf(System.currentTimeMillis());
                    requestReference.child(order_number)
                            .setValue(request);
                    new Database(getBaseContext()).cleanCart(mCurrentUser);

                    sendNotificationOrder(n);
                }
            }
        });

    }

    private void sendNotificationOrder(final String n) {
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query data=tokens.orderByChild("isServerToken").equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    Token serverToken=postSnapShot.getValue(Token.class);
                    Notification notification= new Notification("You have a new Order #"+n,"New Order");
                    Sender content= new Sender(serverToken.getToken(),notification);
                    mService.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.body().success==1)
                                    {
                                        mProgrss.dismiss();
                                        Intent intent= new Intent(MapActivity.this,FinalActivity.class);
                                        intent.putExtra("orderId",n);
                                        startActivity(intent);
                                        finish();
                                    }

                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    mProgrss.dismiss();
                                    Log.e("ERROR",t.getMessage());
                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showAlertDialog(String placeName) {
        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(MapActivity.this);
        alertDialog.setTitle(placeName);
        alertDialog.setMessage("The Relish Prince Can't Reach at your area, Please try an alternate location.");

        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        alertDialog.setIcon(R.drawable.sad);
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
