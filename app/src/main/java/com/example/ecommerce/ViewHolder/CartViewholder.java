package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;

public class CartViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName,txtProductPrice,txtProductQuantity;
    private ItemClickListner itemClickListner;

    public CartViewholder(@NonNull View itemView) {
        super(itemView);

        txtProductName= itemView.findViewById(R.id.product_name);
        txtProductPrice= itemView.findViewById(R.id.product_price);
        txtProductQuantity= itemView.findViewById(R.id.product_quantity);
    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
