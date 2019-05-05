package com.example.fbe.universitymallapt;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Token;

import com.google.firebase.auth.FirebaseAuth;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by FB$E on 6/4/2018.
 */

public class SecondFragment extends Fragment {
    final String mPublishableKey = "pk_test_fA355iXsfGnwcr6RSURRtqzP";
    String stripeToken;
    Button pay;
    View myView;
FirebaseAuth mAuth;
CardInputWidget mCardInputWidget;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.paypal_layout, container, false);
        return myView;
    }

    public void onViewCreated(View myView, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pay = (Button)getView().findViewById(R.id.pay);



         mCardInputWidget = (CardInputWidget)getView().findViewById(R.id.card_input_widget);
         mAuth = FirebaseAuth.getInstance();

         pay.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 submitCard();
             }
         });








    }

public void submitCard() {
    FirebaseAuth.getInstance().getCurrentUser();

     final Card cardToSave = mCardInputWidget.getCard();

    if (cardToSave == null) {
        Toast.makeText(SecondFragment.this.getActivity(), "Invalid Card Data!", Toast.LENGTH_SHORT).show();
        return;
    } else {
        Stripe stripe = new Stripe(SecondFragment.this.getActivity(), "pk_test_fA355iXsfGnwcr6RSURRtqzP");
        stripe.createToken(
                cardToSave,
                new TokenCallback() {
                    public void onSuccess(Token token) {

                        String userId = mAuth.getCurrentUser().getUid();
                        DatabaseReference userData= FirebaseDatabase.getInstance().getReference().child("stripeTokens").child(userId);//Create child
                        Map newpost = new HashMap();//create hashmap to post user info
                        newpost.put("Your Token", token);
                        userData.setValue(newpost);





                        Toast.makeText(SecondFragment.this.getActivity(), "Successfully Generated a Token!", Toast.LENGTH_SHORT).show();
                    }

                    public void onError(Exception error) {
                        // Show localized error message
                        Toast.makeText(SecondFragment.this.getActivity(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
        );

    }

}
protected void onHandleIntent(Intent intent)
{

}
}
