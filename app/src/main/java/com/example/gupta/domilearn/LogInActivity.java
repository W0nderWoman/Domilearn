package com.example.gupta.domilearn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LogInActivity extends AppCompatActivity {

    private DatabaseReference dbrefusers,dbrefrefrrs;
    EditText t1,t2,t3;
    String usercode,prikey;
    int bal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbrefusers= FirebaseDatabase.getInstance().getReference("USERS");
        dbrefrefrrs= FirebaseDatabase.getInstance().getReference("REFFERERS");

        t1=(EditText) findViewById(R.id.editText2);
        t2=(EditText) findViewById(R.id.editText3);
        t3=(EditText) findViewById(R.id.editText4);

    }
    public void add(View view){
        //CHECK IF FIELDS ARE EMPTY
        if(t1.getText().toString().isEmpty() || t2.getText().toString().isEmpty())
            Toast.makeText(LogInActivity.this,"Fill the required info appropriately",Toast.LENGTH_LONG).show();

        else{
        bal=100;
        usercode=t1.getText().toString().substring(0,3).concat(t2.getText().toString().substring(0,3));
        //ADD INFO TO FIREBASE
        HashMap<String,String> map=new HashMap<String, String>();
        map.put("NAME",t1.getText().toString());
        map.put("EMAIL",t2.getText().toString());
        map.put("REFFERAL CODE",usercode);
        map.put("ACCOUNT BAL",Integer.toString(bal));
        prikey=dbrefusers.push().getKey();
        dbrefusers.child(prikey).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(LogInActivity.this,"SUCCESSFULLY ADDED",Toast.LENGTH_LONG).show();
                else Toast.makeText(LogInActivity.this,"ERROR",Toast.LENGTH_LONG);
            }
        });

        if(t3.getText().toString().isEmpty()) Toast.makeText(LogInActivity.this,"Signing up without Refferal Code",Toast.LENGTH_LONG).show();
        else updatebal();
        //MOVING TO ANOTHER ACTIVITY
        Intent i=new Intent(LogInActivity.this,UserActivity.class);
        i.putExtra("key",prikey);
        startActivity(i);
        }
    }

    //UPDATE A/C BALANCE IF REFERRAL CODE PRESENT
    private void updatebal(){
        String rfrcode=t3.getText().toString();
        Query query=dbrefusers.orderByChild("REFFERAL CODE").equalTo(rfrcode);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dbrefusers.child(prikey).child("ACCOUNT BAL").setValue(Integer.toString(bal+100));
                HashMap<String,String> map=(HashMap<String,String>) dataSnapshot.getValue();
                int balance=Integer.parseInt(map.get("ACCOUNT BAL"));
                dbrefusers.child(dataSnapshot.getKey()).child("ACCOUNT BAL").setValue(Integer.toString(balance+100));
                dbrefrefrrs.child(prikey).child("REFFERED BY").setValue(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
