package com.therelishprince;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.therelishprince.Common.Common;
import com.therelishprince.Interface.ItemClickListener;
import com.therelishprince.ViewHolder.MenuViewHolder;
import com.therelishprince.model.Category;


/**
 * A simple {@link Fragment} subclass.
 */
public class PizzaFragment extends Fragment {
    private RecyclerView pizzaList;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference PizzaReference;
    private View mView;

    private ProgressDialog mProgrss;



    public PizzaFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_pizza, container, false);

        pizzaList=(RecyclerView)mView.findViewById(R.id.pizza_list);

        PizzaReference=FirebaseDatabase.getInstance().getReference().child("pizza");

        pizzaList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        pizzaList.setLayoutManager(linearLayoutManager);


        return mView;
    }



    @Override
    public void onStart() {
        super.onStart();
        if (Common.isConnectedToInternet(getActivity()))
        {
            mProgrss= new ProgressDialog(getActivity());
            mProgrss.setMessage("Please wait");
            mProgrss.setCanceledOnTouchOutside(false);
            mProgrss.show();

            FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                    .setQuery(PizzaReference,Category.class)
                    .build();

            final FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter=
                    new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull final MenuViewHolder holder, final int position, @NonNull final Category model)
                        {
                            mProgrss.dismiss();
                            holder.mName.setText(model.getName());
                            holder.mDis.setText(model.getDis());
                            holder.mPrice.setText("₹"+model.getPrice());
                            Picasso.with(getContext()).load(model.getImage()).into(holder.mImage);



                            final Category local=model;
                            holder.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onClick(View view, int position, boolean isLongClick) {

                                    Intent foodDetail = new Intent(getActivity(),FoodDetail.class);
                                    foodDetail.putExtra("id",model.getId());
                                    foodDetail.putExtra("name",model.getName());
                                    foodDetail.putExtra("dis",model.getDis());
                                    foodDetail.putExtra("price",model.getPrice());
                                    foodDetail.putExtra("img",model.getImage());
                                    foodDetail.putExtra("discount",model.getDiscount());
                                    getActivity().startActivity(foodDetail);
                                }
                            });


                        }

                        @NonNull
                        @Override
                        public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
                            MenuViewHolder viewHolder = new MenuViewHolder(view);
                            return viewHolder;
                        }
                    };
            pizzaList.setAdapter(adapter);
            adapter.startListening();

        }
        else
        {
            Toast.makeText(getActivity(),"Please check your Internet Connection!!",Toast.LENGTH_SHORT).show();
            return;
        }

    }

}
