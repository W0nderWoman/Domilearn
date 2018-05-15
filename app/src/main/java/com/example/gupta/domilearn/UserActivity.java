package com.example.gupta.domilearn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class UserActivity extends AppCompatActivity {

    private DatabaseReference dbref;
    TextView t0,t1,t2,t3,t4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        dbref= FirebaseDatabase.getInstance().getReference("USERS");
        t0=(TextView)findViewById(R.id.textView2);
        t1=(TextView)findViewById(R.id.textView3);
        t2=(TextView)findViewById(R.id.textView4);
        t3=(TextView)findViewById(R.id.textView5);
        t4=(TextView)findViewById(R.id.textView6);

        final String prikey;
        //RETRIEVE INTENT
        Bundle b=getIntent().getExtras();
        prikey=b.getString("key");
        //SEARCHING BY PRIMARY KEY
        Query query=dbref.orderByKey().equalTo(prikey);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //RETRIEVE DATA
                HashMap<String,String> map=(HashMap<String,String>) dataSnapshot.getValue();
                String name,email,bal,code;

                name=(String)map.get("NAME");
                email=(String)map.get("EMAIL");
                bal=(String)map.get("ACCOUNT BAL");
                code=(String)map.get("REFFERAL CODE");
                t0.setText(prikey);
                t1.setText(name);
                t2.setText(email);
                t3.setText(bal);
                t4.setText(code);
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
