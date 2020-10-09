package com.example.ecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.ViewHolder.CartViewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nxtprocessbtn;
    private TextView txttotalamount,ordertext,shoppongcart;

    private int overTotalPrice  = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView=findViewById(R.id.Cart_List);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nxtprocessbtn=findViewById(R.id.next_process_btn);
        txttotalamount = findViewById(R.id.total_amount);
        ordertext = findViewById(R.id.ordertext);
        shoppongcart = findViewById(R.id.shoppingcart);



        txttotalamount.setText("Total Price Rs "+String.valueOf(overTotalPrice));
        nxtprocessbtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {


                Intent intent = new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();

        final DatabaseReference cartListref = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery((cartListref.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products")),Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart,CartViewholder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewholder>(options) {
            @NonNull
            @Override
            public CartViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewholder cartViewholder= new CartViewholder(view);
                return  cartViewholder;

            }

            @Override
            protected void onBindViewHolder(@NonNull CartViewholder cartViewholder, int i, @NonNull final Cart model) {

                cartViewholder.txtProductQuantity.setText(model.getQuantity());
                cartViewholder.txtProductName.setText(model.getPname());
                cartViewholder.txtProductPrice.setText("Rs "+model.getPrice());

//                int oneTypeProductTPrice = ((Integer.parseInt(model.getPrice()))) * Integer.valueOf(model.getQuantity());
//                overTotalPrice =  overTotalPrice + oneTypeProductTPrice;

                int  oneProductPrice = Integer.parseInt(model.getPrice());
                int oneProductQuantity = Integer.parseInt(model.getQuantity());
                int oneTyprProductTPrice = oneProductPrice *oneProductQuantity;
                overTotalPrice = overTotalPrice + oneTyprProductTPrice;
                txttotalamount.setText("Total Amount                 RS"+String.valueOf(overTotalPrice));


                cartViewholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new  CharSequence[]
                                {
                                        "Edit",
                                        "Delete"
                                };
                        AlertDialog.Builder builder= new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener()
                        {


                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                if(i==0)
                                {
                                    Intent intent= new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if(i==1)
                                {

                                    cartListref.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent= new Intent(CartActivity.this,HomeActivity.class);

                                                        startActivity(intent);

                                                    }


                                                }
                                            });


                                }






                            }
                        });
                        builder.show();





                    }
                });



            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    protected void onStop() {
        // without this line of code the total price will double when we
        // minimize the program and open again
        super.onStop();
        overTotalPrice = 0;
    }
    private void CheckOrderState()
    {
        DatabaseReference orderref;
        orderref = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        orderref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                if(datasnapshot.exists()){

                    String shippingState = datasnapshot.child("state").getValue().toString();
                    String userNmae = datasnapshot.child("name").getValue().toString();

                    if(shippingState.equals("shipped"))
                    {
                               txttotalamount.setText("Dear"+userNmae +"\n Order is shipped successfully");
                               recyclerView.setVisibility(View.GONE);

                               ordertext.setVisibility(View.VISIBLE);
                               ordertext.setText("Your Order Has Been Shipped Successfully.You Will Recieve Your Order Soon To Your FootStep");
                        nxtprocessbtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "YOu can purchase More Products, once you recived your first order", Toast.LENGTH_SHORT).show();
                    }
                    else if(shippingState.equals("not shipped")){

                        txttotalamount.setText("               Not Shipped Yet");

                        recyclerView.setVisibility(View.GONE);
                        shoppongcart.setVisibility(View.GONE);

                        ordertext.setVisibility(View.VISIBLE);
                        nxtprocessbtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "YOu can purchase More Products, once you recived your first order", Toast.LENGTH_SHORT).show();


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}