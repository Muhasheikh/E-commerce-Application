package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private  String check = "";
    private TextView pagetitle,titlequestions;
    private EditText phoneNumber,question1,question2;
    private Button verifybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

         pagetitle=findViewById(R.id.text2);
        titlequestions=findViewById(R.id.text1);
        phoneNumber=findViewById(R.id.find_phone_number);
        question1=findViewById(R.id.question_1);
        question2=findViewById(R.id.question_2);
        verifybtn=findViewById(R.id.verify_btn);


        check= getIntent().getStringExtra("check");
    }

    @Override
    protected void onStart() {
        super.onStart();
        phoneNumber.setVisibility(View.GONE);
        if(check.equals("settings"))
        {
            pagetitle.setText("Set Questions");
            titlequestions.setText("Set Answers For The Following Security Questions");


            verifybtn.setText("Set");
            displayPreviousAnswers();

            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setAnswers();


                }
            });

        }

        else if(check.equals("login"))
        {
            phoneNumber.setVisibility(View.VISIBLE);

            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });
        }
    }

    private  void setAnswers()
    {
        String answer1 =  question1.getText().toString().toLowerCase();
        String answer2 =  question2.getText().toString().toLowerCase();

        if(question1.equals("") && question2.equals(""))
        {
            Toast.makeText(ResetPasswordActivity.this, "Please Answer Both Questions", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2", answer2);

            ref.child("security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ResetPasswordActivity.this, "Security Questions Set Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }

    }
    private void displayPreviousAnswers()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                if(datasnapshot.exists())
                {
                    String ans1 = datasnapshot.child("answer1").getValue().toString();
                    String ans2 = datasnapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private  void verifyUser()
    {
        final String phone = phoneNumber.getText().toString();
        final String answer1 =  question1.getText().toString().toLowerCase();
        final String answer2 =  question2.getText().toString().toLowerCase();

        if(!phone.equals("") && !answer1.equals("") && !answer2.equals(""))
        {
            final DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                    if(datasnapshot.exists())
                    {
                        String mphone = datasnapshot.child("phone").getValue().toString();

                        if(datasnapshot.hasChild("security Questions"))
                        {
                            String ans1 = datasnapshot.child("security Questions").child("answer1").getValue().toString();
                            String ans2 = datasnapshot.child("security Questions").child("answer2").getValue().toString();

                            if(!ans1.equals(answer1))
                            {
                                Toast.makeText(ResetPasswordActivity.this, "One Of Your Security Answer Is Incorrect", Toast.LENGTH_SHORT).show();
                            }
                            else if(!ans2.equals(answer2))
                            {
                                Toast.makeText(ResetPasswordActivity.this, "One Of Your Security Answer Is Incorrect", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this,R.style.Widget_AppCompat_ButtonBar_AlertDialog);
                                builder.setTitle("New Password");

                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("    Enter Password ");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change,", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(!newPassword.getText().toString().equals(""))
                                        {
                                            ref.child("password").setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){

                                                                Toast.makeText(ResetPasswordActivity.this, "Password Changed Successfuly", Toast.LENGTH_SHORT).show();
                                                                 Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                                                                 startActivity(intent);
                                                            }

                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();

                                    }
                                });

                                builder.show();
                            }
                        }
                        else
                        {
                            Toast.makeText(ResetPasswordActivity.this, "You Have Not Set The Security Questions.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "This Phone  Number Does Not Exist", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            Toast.makeText(this, "Please Enter Missing Fields", Toast.LENGTH_SHORT).show();
        }


    }
}