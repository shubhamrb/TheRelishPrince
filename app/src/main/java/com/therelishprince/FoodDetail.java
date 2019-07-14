package com.therelishprince;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.therelishprince.Common.Common;
import com.therelishprince.Database.Database;
import com.therelishprince.model.Order;


public class FoodDetail extends AppCompatActivity {
    TextView food_name,food_price,food_description;
    ImageView food_image;
    FloatingActionButton btnCart,btnRating;
    ElegantNumberButton numberButton;
    RatingBar ratingBar;
    Button addCartBtn;
    String mCurrentUser;
    String mId;
    FirebaseDatabase database;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);



        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference().child("status");
        numberButton=(ElegantNumberButton) findViewById(R.id.number_button);
        btnCart=(FloatingActionButton)findViewById(R.id.btnCart);

        addCartBtn=(Button)findViewById(R.id.addCartBtn);
        final String mName=getIntent().getStringExtra("name");
        final String mPrice=getIntent().getStringExtra("price");
         mId = getIntent().getStringExtra("id");
        final String mDiscount= getIntent().getStringExtra("discount");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String status= dataSnapshot.child("online").getValue().toString();
                if (status.equals("false"))
                {
                    addCartBtn.setEnabled(false);
                    Toast.makeText(FoodDetail.this,"The Relish Prince is currently Closed",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (Common.isConnectedToInternet(FoodDetail.this))
        {


            btnCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FoodDetail.this, CartActivity.class);
                    startActivity(intent);
                }
            });

            addCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        boolean isExist = new Database(getBaseContext()).checkFoodExist(mId, mCurrentUser);

                        if (!isExist) {
                            new Database(getBaseContext()).addToCart(new Order(
                                    mCurrentUser,
                                    mId,
                                    mName,
                                    numberButton.getNumber(),
                                    mPrice,
                                    mDiscount

                            ));
                        } else {
                            new Database(getBaseContext()).increaseCart(mId, mCurrentUser, numberButton.getNumber());
                        }
                        Toast.makeText(FoodDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();


                }
            });


            food_description = (TextView) findViewById(R.id.food_dis);
            food_name = (TextView) findViewById(R.id.food_name);
            food_price = (TextView) findViewById(R.id.food_price);
            food_image = (ImageView) findViewById(R.id.img_food);

            getIncomingIntent();
        }else
        {
            Toast.makeText(FoodDetail.this,"Please check your Internet Connection!!",Toast.LENGTH_SHORT).show();
            return;
        }

    }
    private void getIncomingIntent()
    {
        if (getIntent().hasExtra("id")&&getIntent().hasExtra("name")&&getIntent().hasExtra("dis")&& getIntent().hasExtra("price")&&getIntent().hasExtra("img"))
        {
            String mId = getIntent().getStringExtra("id");
            String mName=getIntent().getStringExtra("name");
            String mDis=getIntent().getStringExtra("dis");
            String mPrice=getIntent().getStringExtra("price");
            String mImage=getIntent().getStringExtra("img");
            String mDiscount= getIntent().getStringExtra("discount");

            food_name.setText(mName);
            food_description.setText(mDis);
            food_price.setText("₹"+mPrice);
            Glide.with(this).load(mImage).into(food_image);

        }
    }


}
