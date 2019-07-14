package com.therelishprince;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.therelishprince.Common.Common;
import com.therelishprince.Interface.ItemClickListener;
import com.therelishprince.ViewHolder.OrderViewHolder;
import com.therelishprince.model.Request;

import java.util.Collections;


public class OrderActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference requests;
    String mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");
        mCurrentUser=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        recyclerView=(RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        if (Common.isConnectedToInternet(getBaseContext()))
        {

            loadOrders(mCurrentUser);
        }
        else
        {
            Toast.makeText(OrderActivity.this,"Please check your Internet Connection!!",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.call:
                String phone = "+917470545203";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadOrders(String phone) {

        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(requests.orderByChild("phone")
                        .equalTo(phone)
                        ,Request.class)
                .build();

        final FirebaseRecyclerAdapter<Request,OrderViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
                    @Override

                    protected void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position, @NonNull final Request model)
                    {

                        holder.txtOrderId.setText("Order Id- #"+model.getOrderId());
                        holder.txtOrderPrice.setText("Total- "+model.getTotal());
                        holder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                        holder.txtOrderAddress.setText(model.getAddress());
                        holder.txtOrderPhone.setText(model.getNewPhone());

                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                Intent intent= new Intent(OrderActivity.this,OrderDetails.class);
                                intent.putExtra("OrderId",model.getOrderId());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                        OrderViewHolder viewHolder = new OrderViewHolder(view);
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        firebaseRecyclerAdapter.startListening();

    }


}
