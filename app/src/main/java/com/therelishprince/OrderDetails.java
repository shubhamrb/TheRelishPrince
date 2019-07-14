package com.therelishprince;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.therelishprince.ViewHolder.OrderDetailAdapter;
import com.therelishprince.ViewHolder.OrderViewHolder;
import com.therelishprince.model.Request;


public class OrderDetails extends AppCompatActivity {

    TextView order_id,order_phone,order_address,order_total;
    String order_id_value="";
    RecyclerView lstFoods;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;
    FirebaseDatabase request;
    DatabaseReference requestReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        request=FirebaseDatabase.getInstance();
        requestReference=request.getReference("Requests");

        order_id=(TextView)findViewById(R.id.id);
        order_phone=(TextView)findViewById(R.id.phone);
        order_address=(TextView)findViewById(R.id.address);
        order_total=(TextView)findViewById(R.id.total);

        lstFoods=(RecyclerView)findViewById(R.id.lstFood);
        lstFoods.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        lstFoods.setLayoutManager(layoutManager);
        String id= getIntent().getStringExtra("OrderId");

        requestReference.orderByChild("orderId").equalTo(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Request request= dataSnapshot.getValue(Request.class);
                order_id.setText("Order Id #"+request.getOrderId());
                order_address.setText(request.getAddress());
                order_phone.setText(request.getNewPhone());
                order_total.setText(request.getTotal());

                OrderDetailAdapter adapter=new OrderDetailAdapter(request.getFoods());
                adapter.notifyDataSetChanged();
                lstFoods.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
