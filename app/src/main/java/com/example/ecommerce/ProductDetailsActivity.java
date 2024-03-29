package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private FloatingActionButton floatingaddToCartBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice,productDescription,productName;
    private String productID = "",state = "Normal";
    private Button addtocartbtn,deletebtn;
    private DatabaseReference cartListref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID=getIntent().getStringExtra("pid");
        addtocartbtn = findViewById(R.id.pd_add_to_cart_btn);





        numberButton=findViewById(R.id.number_btn);
        productImage=findViewById(R.id.product_image_details);
        productName=findViewById(R.id.product_name_details);
        productDescription=findViewById(R.id.product_description_details);
        productPrice=findViewById(R.id.product_price_details);



        getProductDetails(productID);



        addtocartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this, "You can add or purchase more products,once your order is shipped or confirmed", Toast.LENGTH_LONG).show();
                }
                else {
                    addingtocart();
                }
            }
        });








    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();
    }

    private void addingtocart() {

        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentDate.format(calForDate.getTime());

//        productRandomKey = saveCurrentDate + saveCurrentTime;




       final DatabaseReference cartListref = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object>cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");
        cartMap.put("price",productPrice.getText().toString());


        cartListref.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            cartListref.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID).updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())

                                                Toast.makeText(ProductDetailsActivity.this,"Added To Cart",Toast.LENGTH_SHORT).show();

                                                Intent intent= new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                                startActivity(intent);
                                        }
                                    });
                        }
                    }
                });








    }

    private void getProductDetails(String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                   productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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


                    if(shippingState.equals("shipped"))
                    {
                        state = "Order Shipped";

                    }
                    else if(shippingState.equals("not shipped")){

                      state="Order Placed";

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}