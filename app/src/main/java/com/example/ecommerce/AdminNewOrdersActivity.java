package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference ordersref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersref = FirebaseDatabase.getInstance().getReference().child("Orders");

        orderList=findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders>options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersref,AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder>adapter
                = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {

                holder.userName.setText("Name : "+model.getName());
                holder.userPhoneNumber.setText("Phone : "+model.getPhone());
                holder.userTotalPrice.setText("Total Amount : "+model.getTotalAmount());
                holder.userShipping.setText("Shipping Address : "+model.getAddress()+ ","+model.getCity());
                holder.userDateTime.setText("Order at : "+model.getDate() + "" +model.getTime());

                holder.showorderbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String uID = getRef(position).getKey();

                        Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProducts.class);
                        intent.putExtra("uid",uID);
                        startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Have you Shipped the Products ?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                if(i ==0){
                                    String uID = getRef(position).getKey();
                                    removeorder(uID);

                                }
                                else{
                                    finish();
                                }


                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                return  new AdminOrdersViewHolder(view);
            }
        };

        orderList.setAdapter(adapter);
        adapter.startListening();
    }



    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userShipping;
        public Button showorderbutton;
        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userDateTime = itemView.findViewById(R.id.order_date);
            userShipping = itemView.findViewById(R.id.order_address);
            userTotalPrice = itemView.findViewById(R.id.order_price);
            showorderbutton=itemView.findViewById(R.id.showorder);

        }
    }

    private void removeorder(String uID) {

        ordersref.child(uID).removeValue();


    }
}