package com.therelishprince;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class BeveragesFragment extends Fragment {

    private RecyclerView beveragesList;

    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference BeveragesReference;
    private View mView;
    private Button addBtn;


    public BeveragesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_beverages, container, false);

        beveragesList=(RecyclerView)mView.findViewById(R.id.beverages_list);

        BeveragesReference= FirebaseDatabase.getInstance().getReference().child("beverages");


        beveragesList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        beveragesList.setLayoutManager(linearLayoutManager);

        addBtn=(Button)mView.findViewById(R.id.add);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Common.isConnectedToInternet(getActivity()))
        {

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(BeveragesReference,Category.class)
                .build();

        FirebaseRecyclerAdapter<Category,MenuViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull final Category model) {
                        holder.mName.setText(model.getName());
                        holder.mDis.setText(model.getDis());
                        holder.mPrice.setText("₹"+model.getPrice());
                        Picasso.with(getContext()).load(model.getImage()).into(holder.mImage);

                        final Category local = model;
                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {

                                Intent foodDetail = new Intent(getActivity(), FoodDetail.class);
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
        beveragesList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

        }
        else
        {
            Toast.makeText(getActivity(),"Please check your Internet Connection!!",Toast.LENGTH_SHORT).show();
            return;
        }

    }


}
