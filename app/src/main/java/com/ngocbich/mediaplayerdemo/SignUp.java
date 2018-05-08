package com.ngocbich.mediaplayerdemo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngocbich.mediaplayerdemo.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {
    MaterialEditText name,phone,password;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name=(MaterialEditText)findViewById(R.id.Name);
        phone=(MaterialEditText)findViewById(R.id.Phone);
        password=(MaterialEditText)findViewById(R.id.Password);

        signup=findViewById(R.id.SignUp);


        //Init Firebase
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phone.getText().toString().equals("") && !name.getText().toString().equals("")
                        &&!password.getText().toString().equals("")){

                    final ProgressDialog mDialog=new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please waiting...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if user's phone already exist
                            if(dataSnapshot.child(phone.getText().toString()).exists()){
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this,"Phone number already registed!",Toast.LENGTH_SHORT).show();
                            }else {
                                mDialog.dismiss();
                                User user=new User(name.getText().toString(),password.getText().toString());
                                table_user.child(phone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this,"Sign up successfully!",Toast.LENGTH_SHORT).show();
                                finish();
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
