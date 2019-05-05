package com.example.fbe.universitymallapt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Account extends AppCompatActivity {
    private static final int CHOOSE_IMAGE =101 ;
    ImageView profile;
EditText name;
EditText apartment;
EditText phoneNumber;
Button save;
Uri profileImage;
String imageUrl;
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth = FirebaseAuth.getInstance();
        profile = findViewById(R.id.placeholder);
        name = findViewById(R.id.Name);
        apartment = findViewById(R.id.Apartment);
        save = findViewById(R.id.SaveInfo);
        phoneNumber= findViewById(R.id.phone_number);
        //load user info at startup
        loadUserInfo();


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfomation();}
        });




    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() ==null)
        {
            finish();
            startActivity(new Intent(this,register.class));
        }
//        else if(mAuth.getCurrentUser() != null)
//        {
//            finish();
//            startActivity(new Intent(this, Main3Activity.class));
//        }
    }


    private void loadUserInfo() {
        //get current user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                .load(user.getPhotoUrl().toString())
                .into(profile);
            }
            if (user.getDisplayName() != null) {
                name.setText( user.getDisplayName());

                apartment.setText(apartment.getText());

            }

        }
    }
    //save input information

    private void saveUserInfomation() {
        String displayName = name.getText().toString();
        String apartmentNum= apartment.getText().toString();
        String phone= phoneNumber.getText().toString();
        if(displayName.isEmpty())
        {
            name.setError("Name is Required");
            name.requestFocus();
            return;

        }
        if (apartmentNum.isEmpty())
        {
            apartment.setError("Apartment Required");
            apartment.requestFocus();
            return;
        }
        if(phone.isEmpty())
        {
            phoneNumber.setError("Phone Number Required");
            phoneNumber.requestFocus();
            return;
        }
        else
            {
                String user_Id = mAuth.getCurrentUser().getUid(); //get current User ID
                DatabaseReference userData= FirebaseDatabase.getInstance().getReference().child("Users").child(user_Id);//Create child
                Map newpost = new HashMap();//create hashmap to post user info
                newpost.put("Name", displayName);
                newpost.put("Apartment", apartmentNum);
                newpost.put("Phone Number", phone);
                userData.setValue(newpost);
            }
        //get current user logged in and change profile info accordingly
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && profileImage != null)
        {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(imageUrl))
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(Account.this, "User info Saved", Toast.LENGTH_SHORT).show();
                        //if successful navigate to new activity


                    }

                }

            });

        }

        //Save user Info to Database


        //Start profile activity if everything is correct , then clear the top

        Intent intent = new Intent(Account.this,Main3Activity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    //overide method for image choosen to save image as new image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Make sure that image is different is ok, and not null
        if(requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data != null && data.getData() != null)
            //save image uri(location)
            profileImage = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),profileImage);
            profile.setImageBitmap(bitmap);
            uploadImageToFirebaseStorage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Store image to firebase

    private void uploadImageToFirebaseStorage() {
        //Make reference to image
        StorageReference imageReference =
                FirebaseStorage.getInstance().getReference("profilePics/" + System.currentTimeMillis() + ".jpg");
        if(profileImage != null)
        {
            imageReference.putFile(profileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageUrl=taskSnapshot.getDownloadUrl().toString();

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(Account.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    //make image chooser intent
    private void showImageChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Select profile image"),CHOOSE_IMAGE);

    }
}
