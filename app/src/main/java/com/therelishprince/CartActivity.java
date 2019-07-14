package com.therelishprince;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.therelishprince.Common.Common;
import com.therelishprince.Database.Database;

import com.therelishprince.model.Order;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    TextView txtTotalPrice;
    FButton btnPlaceOrder;
    String mCurrentUser;

    List<Order> cart=new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    CartAdapter adapter;
    String totalPrice;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        recyclerView=(RecyclerView)findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice=(TextView)findViewById(R.id.total);
        btnPlaceOrder=(FButton)findViewById(R.id.btnPlaceOrder);
        database= FirebaseDatabase.getInstance();
        databaseReference=database.getReference().child("status");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String status= dataSnapshot.child("online").getValue().toString();
                if (status.equals("false"))
                {
                    btnPlaceOrder.setEnabled(false);
                    Toast.makeText(CartActivity.this,"The Relish Prince is currently Closed",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        boolean isExist=new Database(getBaseContext()).cartExist(mCurrentUser);

        if (!isExist){
            btnPlaceOrder.setEnabled(false);
        }else {
            btnPlaceOrder.setEnabled(true);
        }

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(CartActivity.this,MapActivity.class);
                intent.putExtra("totalPrice",totalPrice);
                startActivity(intent);

            }
        });


        if (Common.isConnectedToInternet(CartActivity.this))
        {
        loadListFood();
        }
        else
        {
            Toast.makeText(CartActivity.this,"Please check your Internet Connection!!",Toast.LENGTH_SHORT).show();
            return;
        }
    }


    private void loadListFood() {

        cart= new Database(this).getCarts(mCurrentUser);

            adapter=new CartAdapter(cart,this);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);


            //calculate total price
            int total=0;
            for (Order order:cart)
                total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));

            Locale locale=new Locale("en","in");
            NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
            String ttl= fmt.format(total);
            txtTotalPrice.setText(ttl);
            totalPrice=ttl;
    }

    @Override
    public void onClick(View v) {

    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        if (item.getTitle().equals(Common.DELETE))
//            deleteCart(item.getOrder());
//
//        return true;
//    }
//
//    private void deleteCart(int position) {
//        cart.remove(position);
//        new Database(this).cleanCart(mCurrentUser);
//        for (Order item:cart)
//            new Database(this).addToCart(item);
//        loadListFood();
//        Toast.makeText(this,"Item Deleted",Toast.LENGTH_SHORT).show();
//    }

    public class CartAdapter extends RecyclerView.Adapter<com.therelishprince.ViewHolder.CartAdapter> {

        private List<Order> listData= new ArrayList<>();
        private Context context;
        private List<Order> cart;
        private CartAdapter adapter;

        public CartAdapter(List<Order> listData, Context context) {
            this.listData = listData;
            this.context = context;
        }

        @NonNull
        @Override
        public com.therelishprince.ViewHolder.CartAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(context);
            View itemView=inflater.inflate(R.layout.cart_layout,parent,false);
            return new com.therelishprince.ViewHolder.CartAdapter(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final com.therelishprince.ViewHolder.CartAdapter holder, final int position) {

            TextDrawable drawable=TextDrawable.builder()
                    .buildRound(""+listData.get(position).getQuantity(), Color.RED);
            holder.img_cart_count.setImageDrawable(drawable);
            holder.cart_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCart(position);

                }
            });



            Locale locale=new Locale("en","in");
            NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
            int price=(Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
            holder.txtCartPrice.setText(fmt.format(price));
            holder.txtCartName.setText(listData.get(position).getProductName());
        }



        @Override
        public int getItemCount() {
            return listData.size();
        }
    }

    private void deleteCart(int position) {
        cart.remove(position);
        new Database(this).cleanCart(mCurrentUser);
        for (Order item:cart)
            new Database(this).addToCart(item);
        loadListFood();
        Toast.makeText(this,"Item Deleted",Toast.LENGTH_SHORT).show();

    }

}
