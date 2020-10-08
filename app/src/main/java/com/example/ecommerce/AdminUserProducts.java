package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.CartViewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProducts extends AppCompatActivity {

    private RecyclerView productlist;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartlistref;

    private  String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        userID = getIntent().getStringExtra("uid");

        productlist=findViewById(R.id.products_list);
        productlist.setHasFixedSize(true);
       layoutManager = new LinearLayoutManager(this);
       productlist.setLayoutManager(layoutManager);


        cartlistref= FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("Admin View").child(userID).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart>options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartlistref,Cart.class).build();


        FirebaseRecyclerAdapter<Cart, CartViewholder>adapter=
                new FirebaseRecyclerAdapter<Cart, CartViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewholder cartViewholder, int i, @NonNull Cart model) {

                        cartViewholder.txtProductQuantity.setText(model.getQuantity());
                        cartViewholder.txtProductName.setText(model.getPname());
                        cartViewholder.txtProductPrice.setText("Total Amount Rs"+model.getPrice());


                    }

                    @NonNull
                    @Override
                    public CartViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                        CartViewholder cartViewholder= new CartViewholder(view);
                        return  cartViewholder;
                    }
                };

        productlist.setAdapter(adapter);
        adapter.startListening();
    }
}