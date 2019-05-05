package com.example.fbe.universitymallapt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    EditText email, pass;
    Button login;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.editText5);
         pass = findViewById(R.id.editText4);
        login = findViewById(R.id.button4);
           bar = findViewById(R.id.progressBar3);

        // Authentication for login through firebase
        mAuth = FirebaseAuth.getInstance();


        findViewById(R.id.button4).setOnClickListener(this);

    }
    private void userLogin()
            //userLogin method verifies name and password is correct,
            //then signs user in with email and password
    {  String userEmail = email.getText().toString().trim();
       String userPass = pass.getText().toString().trim();
        if(userEmail.isEmpty())
        {
            email.setError("Email is required");
            email.requestFocus();
            return;

        }


        if(userPass.isEmpty())
        {
            pass.setError("Password is Required");
            pass.requestFocus();
            return;
        }
        if(userPass.length()<6)
        {
            pass.setError("Password must be at least 6 characters");
            pass.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
        {
            email.setError("Please enter Valid email");
            email.requestFocus();
            return;
        }
        bar.setVisibility(View.VISIBLE);
        //login through firebase
        mAuth.signInWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                bar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {    //finish  to not go back to login screen
                    finish();
                    //if successful navigate to new activity
                    Intent intent = new Intent(Login.this,Account.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //to clear all previous activities after login
                }
                else{
                    //if unsuccessful show exception
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
//Goes straight to mainprofile when logged in
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() !=null)
        {
            finish();
            startActivity(new Intent(this,Account.class));
        }
    }

    @Override
    public void onClick(View view) {
        //When login is clicked userLogin method is enabled
        switch (view.getId()) {
            case R.id.button4:
                userLogin();
                break;

        }

    }

}

