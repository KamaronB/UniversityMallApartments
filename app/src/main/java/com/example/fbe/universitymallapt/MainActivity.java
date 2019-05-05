package com.example.fbe.universitymallapt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();



        //Set up login and register button

        final Button login = findViewById(R.id.button);
        final Button register = findViewById(R.id.button2);





        //Set on click listeners

        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

				/*
				 * Intent is just like glue which helps to navigate one activity
				 * to another.
				 */
                Intent intent = new Intent(MainActivity.this,
                        Login.class);
                startActivity(intent); // startActivity allow you to move

            }
        });


        //login.setOnClickListener();
        //register.setOnClickListener();

        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

				/*
				 * Intent is just like glue which helps to navigate one activity
				 * to another.
				 */
                Intent intent = new Intent(MainActivity.this,
                        register.class);
                startActivity(intent); // startActivity allow you to move

            }
        });

    }

    //if logged in then go to profile on start up
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() !=null)
        {
            finish();
            startActivity(new Intent(this,Account.class));
        }
    }
}
