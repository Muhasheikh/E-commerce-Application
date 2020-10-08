package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applychangesbtn,deletebtn;
    private EditText name,price,description;
    private ImageView imageView;
    private String productID = "";
    private DatabaseReference produtsref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        applychangesbtn = findViewById(R.id.apply_changes);
        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        description = findViewById(R.id.product_Description_maintain);
        imageView = findViewById(R.id.product_image_maintain);
        deletebtn = findViewById(R.id.delete_product);

        productID=getIntent().getStringExtra("pid");

        produtsref = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deletethisproduct();
            }
        });



        displaySpecificProductInfot();

        applychangesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applychanges();
            }
        });

    }

    private void deletethisproduct() {

        produtsref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminMaintainProductsActivity.this, "Product Deleted Successfully", Toast.LENGTH_SHORT).show();

            }



        });
    }

    private void applychanges() {

        String pname = name.getText().toString();
        String pprice = price.getText().toString();
        String pdescription = description.getText().toString();

        if(pname.equals("")){

            Toast.makeText(this, "Product Name Required", Toast.LENGTH_SHORT).show();
        }
        else if(pprice.equals(""))
        {
            Toast.makeText(this, "Product Price Required", Toast.LENGTH_SHORT).show();
        }
        else if(pdescription.equals(""))
        {
            Toast.makeText(this, "Product Description Required", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);

            productMap.put("description", pdescription);


            productMap.put("price", pprice);
            productMap.put("pname", pname);


            produtsref.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes Applied Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            });

        }


    }

    private void displaySpecificProductInfot() {

        produtsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                if(datasnapshot.exists()){

                    String pname= datasnapshot.child("pname").getValue().toString();
                    String pprice= datasnapshot.child("price").getValue().toString();
                    String pdescription= datasnapshot.child("description").getValue().toString();
                    String pimage= datasnapshot.child("image").getValue().toString();

                    name.setText(pname);
                    price.setText(pprice);
                    description.setText(pdescription);
                    Picasso.get().load(pimage).into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}