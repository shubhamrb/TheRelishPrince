package com.therelishprince;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.therelishprince.Common.Common;
import com.therelishprince.Interface.ItemClickListener;
import com.therelishprince.ViewHolder.MenuViewHolder;
import com.therelishprince.model.Category;


public class FestiveActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String mCurrentUser;
    private ProgressDialog mProgrss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festive);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Festive Foods");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("festive");
        mCurrentUser=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        recyclerView=(RecyclerView)findViewById(R.id.festive_list);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    startActivity(new Intent(FestiveActivity.this,CartActivity.class));

                } catch (ActivityNotFoundException e) {
                    //TODO smth
                }
            }

        });

        if (Common.isConnectedToInternet(getBaseContext()))
        {
            loadFoods();
        }
        else
        {
            Toast.makeText(FestiveActivity.this,"Please check your Internet Connection!!",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void loadFoods() {

        mProgrss= new ProgressDialog(this);
        mProgrss.setMessage("Please wait");
        mProgrss.show();


        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(databaseReference,Category.class)
                .build();
        mProgrss.dismiss();
        adapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final MenuViewHolder holder, final int position, @NonNull final Category model)
                    {

                        holder.mName.setText(model.getName());
                        holder.mDis.setText(model.getDis());
                        holder.mPrice.setText("₹"+model.getPrice());
                        Picasso.with(getBaseContext()).load(model.getImage()).into(holder.mImage);

                        final Category local=model;
                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {

                                Intent foodDetail = new Intent(getBaseContext(),FoodDetail.class);
                                foodDetail.putExtra("id",model.getId());
                                foodDetail.putExtra("name",model.getName());
                                foodDetail.putExtra("dis",model.getDis());
                                foodDetail.putExtra("price",model.getPrice());
                                foodDetail.putExtra("img",model.getImage());
                                foodDetail.putExtra("discount",model.getDiscount());
                                startActivity(foodDetail);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.festiv_menu_item, parent, false);
                        MenuViewHolder viewHolder = new MenuViewHolder(view);
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

}
