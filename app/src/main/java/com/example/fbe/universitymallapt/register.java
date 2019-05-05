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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class register extends AppCompatActivity implements View.OnClickListener {
ProgressBar bar;
EditText email, password;
Button register;

private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



         email = findViewById(R.id.reg_Email);
         password = findViewById(R.id.reg_password);
         register = findViewById(R.id.Register);
         bar = findViewById(R.id.progressBar2);


        findViewById(R.id.Register).setOnClickListener(this);

        mAuth= FirebaseAuth.getInstance();




    }
    private void registerUser()
    { String userEmail = email.getText().toString().trim();
        String userPass = password.getText().toString().trim();


        if(userEmail.isEmpty())
        {
            email.setError("Email is required");
            email.requestFocus();
            return;

        }


        if(userPass.isEmpty())
        {
            password.setError("Password is Required");
            password.requestFocus();
            return;
        }
        if(userPass.length()<6)
        {
            password.setError("Password must be at least 6 characters");
            password.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
        {
            email.setError("Please enter Valid email");
            email.requestFocus();
            return;
        }
        bar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                bar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {   finish();
                    Intent intent = new Intent(register.this,Account.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"User registered Successfully",Toast.LENGTH_SHORT).show();
                }
                else
                    {
                       if(task.getException() instanceof FirebaseAuthUserCollisionException)
                       {
                           Toast.makeText(getApplicationContext(),"This Email Is Already In Use.",Toast.LENGTH_SHORT).show();
                       }
                       else
                           {
                               Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                           }
                    }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.Register:
                registerUser();
                break;



        }
    }
}
