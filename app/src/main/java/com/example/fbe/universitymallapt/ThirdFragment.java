package com.example.fbe.universitymallapt;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by FB$E on 6/4/2018.
 */

public class ThirdFragment extends Fragment implements View.OnClickListener{
    View myView;
    EditText maintReq;
    Button sendReq;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.maintenencerequest_layout,container,false);
        return myView;


    }
    public void onViewCreated(View myView, @Nullable Bundle savedInstanceState)
    {
        maintReq= (EditText)getView().findViewById(R.id.requestBox);
        sendReq=(Button)getView().findViewById(R.id.sendMaintanenceRequest);
        mAuth= FirebaseAuth.getInstance();



        sendReq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String emailMessage = maintReq.getText().toString().trim();
        if(emailMessage.isEmpty())
        {
            maintReq.setError("Please enter a message");
            maintReq.requestFocus();
            return;
        }
        else
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kamaron.bickham@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Maintenance Request");
            intent.putExtra(Intent.EXTRA_TEXT, emailMessage);
            try
            {
                startActivity(Intent.createChooser(intent,"Send mail"));
            }
            catch (android.content.ActivityNotFoundException ex)
            {
                Toast.makeText(ThirdFragment.this.getActivity(), "There are no email clients Installed", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
