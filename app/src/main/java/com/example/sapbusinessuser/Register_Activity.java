package com.example.sapbusinessuser;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sapbusinessuser.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Activity extends AppCompatActivity {

    private Button btn_register;

    private Toolbar toolbar;
    private EditText ed_email,ed_password,ed_firstname,ed_lastname,ed_reenter;
    private void initUI(){
        toolbar = findViewById(R.id.toolbar);
        btn_register = findViewById(R.id.btn_register);
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        ed_firstname= findViewById(R.id.ed_firstname);
        ed_lastname= findViewById(R.id.ed_lastname);
        ed_reenter= findViewById(R.id.ed_reenter);
    }
    private DatabaseReference fb_userRef;
    private FirebaseAuth fb_auth;
    private void initFirebase(){
        fb_userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        fb_auth = FirebaseAuth.getInstance();

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();
        initFirebase();

        if(getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = ed_email.getText().toString();
                String password = ed_password.getText().toString();
                String repassword = ed_reenter.getText().toString();
                final String firstname = ed_firstname.getText().toString();
                final String lastname = ed_lastname.getText().toString();
                if(TextUtils.isEmpty(firstname)){
                    showMessage("Failed","Empty firstname");
                }else if(TextUtils.isEmpty(lastname)){
                    showMessage("Failed","Empty lastname");
                }else if(TextUtils.isEmpty(email)){
                    showMessage("Failed","Empty email");
                }else if(TextUtils.isEmpty(password)){
                    showMessage("Failed","Empty password");
                }else if(TextUtils.isEmpty(repassword)){
                    showMessage("Failed","You must type your password twice");
                }else if(password.length()<6) {
                    showMessage("Failed","Password length must be 6 characters long");
                }else if(!password.equals(repassword)){
                    showMessage("Failed","Password doest match");
                }else{
                    fb_auth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        User user = new User();
                                        user.setRole("student");
                                        user.setEmail(email);
                                        user.setFirstname(firstname);
                                        user.setLastname(lastname);
                                        fb_userRef.child(task.getResult().getUser().getUid())
                                                .setValue(user);
                                        showMessage("Success","Your account has been created");

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("Failed",e.getMessage());

                        }
                    });

                }
            }
        });

    }
    private void showMessage(String title,String message){
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intentTo(Login_Activity.class);

    }
    private void intentTo(Class className){
        Intent intent = new Intent(this,className);
        startActivity(intent);
        finish();
    }
}
