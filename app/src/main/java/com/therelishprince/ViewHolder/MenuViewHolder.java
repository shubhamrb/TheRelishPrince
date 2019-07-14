package com.therelishprince.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.therelishprince.Interface.ItemClickListener;
import com.therelishprince.R;


public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



    public TextView mName;
    public TextView mDis;
    public TextView mPrice;
    public ImageView mImage;

    private ItemClickListener itemClickListener;
    public MenuViewHolder(View mView) {
        super(mView);

        mName=mView.findViewById(R.id.menu_name);
        mDis=mView.findViewById(R.id.menu_dis);
        mPrice=mView.findViewById(R.id.menu_price);
        mImage=mView.findViewById(R.id.menu_image);

        mView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }


}
