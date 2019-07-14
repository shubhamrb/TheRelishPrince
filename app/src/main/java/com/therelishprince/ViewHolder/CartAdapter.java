package com.therelishprince.ViewHolder;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.therelishprince.Common.Common;
import com.therelishprince.Database.Database;
import com.therelishprince.Interface.ItemClickListener;
import com.therelishprince.R;
import com.therelishprince.model.Order;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.ViewHolder
{

    public TextView txtCartName,txtCartPrice;
    public ImageView img_cart_count,cart_del;


    private ItemClickListener itemClickListener;

    public void setTxtCartName(TextView txtCartName) {
        this.txtCartName = txtCartName;
    }

    public CartAdapter(View itemView) {
        super(itemView);
        txtCartName=(TextView)itemView.findViewById(R.id.cart_item_name);
        txtCartPrice=(TextView)itemView.findViewById(R.id.cart_item_price);
        img_cart_count=(ImageView)itemView.findViewById(R.id.cart_item_count);
        cart_del=(ImageView)itemView.findViewById(R.id.cart_item_delete);



    }



//    @Override
//    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//        contextMenu.setHeaderTitle("Wanna Delete it??");
//        contextMenu.add(0,0,getAdapterPosition(),Common.DELETE);
//    }
}

