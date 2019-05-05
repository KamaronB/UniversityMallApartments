package com.example.fbe.universitymallapt;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by FB$E on 6/4/2018.
 */

public class FirstFragment extends Fragment implements View.OnClickListener{
    View myView;
    TextView name,apartment;
    ImageView profileImg;
    Button logout;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.displayprofile_layout,container,false);

        mAuth=FirebaseAuth.getInstance();



        return myView;
    }
    public void onViewCreated(View myView, @Nullable Bundle savedInstanceState)
    {
        name =(TextView)getView().findViewById(R.id.MaintReq);
        apartment=(TextView)getView().findViewById(R.id.AptNum);
        profileImg=(ImageView)getView().findViewById(R.id.ProfileImage);
        logout=(Button)getView().findViewById(R.id.logout);
        mAuth= FirebaseAuth.getInstance();
         loadUserInfo();
         //profileImg.setCropToPadding(true);

        logout.setOnClickListener(this);
    }
    private void loadUserInfo() {
        //get current user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(profileImg);
            }
            if (user.getDisplayName() != null) {

                name.setText( user.getDisplayName());


            }

        }
    }

    @Override
    public void onClick(View v) {
        getActivity().finish();
        mAuth.signOut();
        Intent intent = new Intent(FirstFragment.this.getActivity(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

