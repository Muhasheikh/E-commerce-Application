//package com.example.ecommerce;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.example.ecommerce.Model.Cart;
//import com.example.ecommerce.Prevalent.Prevalent;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class CartActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private RecyclerView.LayoutManager layoutManager;
//    private Button nxtprocessbtn;
//    private TextView txttotalamount;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cart);
//
//        recyclerView=findViewById(R.id.Cart_List);
//        recyclerView.setHasFixedSize(true);
//        layoutManager=new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        nxtprocessbtn=findViewById(R.id.next_process_btn);
//        txttotalamount = findViewById(R.id.total_amount);
//
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        final DatabaseReference cartListref = FirebaseDatabase.getInstance().getReference().child("Cart List");
//        FirebaseRecyclerOptions<Cart> options =
//                new FirebaseRecyclerOptions<.Builder<Cart>()
//                .setQuery((cartListref.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").Cart.class)
//                .build();
//    }
//}