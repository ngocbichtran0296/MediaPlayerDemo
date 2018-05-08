package com.ngocbich.mediaplayerdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngocbich.mediaplayerdemo.Common.Common;
import com.ngocbich.mediaplayerdemo.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    EditText phone,pass;
    Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        pass=(MaterialEditText)findViewById(R.id.Password);
        phone=(MaterialEditText)findViewById(R.id.Phone);


        signin=findViewById(R.id.SignIn);


        //Init Firebase
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phone.getText().toString().equals("")
                        && !pass.getText().toString().equals("")) {

                    final ProgressDialog mDialog=new ProgressDialog(SignIn.this);
                    mDialog.setMessage("Please waiting...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if user dosen't exist in database
                            if (dataSnapshot.child(phone.getText().toString()).exists()) {

                                //get user information
                                mDialog.dismiss();
                                User user = dataSnapshot.child(phone.getText().toString()).getValue(User.class);
                                if (user.getPassword()!=null && user.getPassword().equals(pass.getText().toString())) {

                                    Intent home=new Intent(SignIn.this,Home.class);
                                    Common.currentUser=user;
                                    startActivity(home);
                                } else {
                                    Toast.makeText(SignIn.this, "Sign in failed!", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "User not exist!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
