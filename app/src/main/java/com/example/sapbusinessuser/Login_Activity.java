package com.example.sapbusinessuser;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sapbusinessuser.model.AppPreference;
import com.example.sapbusinessuser.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Activity extends AppCompatActivity {

    private Button btn_login;
    private TextView btn_register;
    private EditText ed_email,ed_password;
    private ProgressDialog progressDialog;
    private void initUI(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in..");
        progressDialog.setCancelable(false);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
    }
    private DatabaseReference fb_userRef;
    private FirebaseAuth fb_auth;
    private AppPreference appPreference;
    private void initFirebase(){
        fb_userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        fb_auth = FirebaseAuth.getInstance();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appPreference = new AppPreference(this);
        if(!appPreference.getId().equals("")){
            intentTo(MainActivity.class);
        }
        initUI();
        initFirebase();
        //create teacher


        //create student
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentTo(Register_Activity.class);

            }
        });
        //login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ed_email.getText().toString();
                String password = ed_password.getText().toString();
                if(TextUtils.isEmpty(email)){
                    showMessage("Failed","Empty email");
                }else if(TextUtils.isEmpty(password)){
                    showMessage("Failed", "Empty password");
                }else{
                    progressDialog.show();
                    fb_auth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull final Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        fb_userRef.child(task.getResult().getUser().getUid())
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        progressDialog.dismiss();
                                                        User user = dataSnapshot.getValue(User.class);
                                                        if(user.getRole()!=null){
                                                            if(user.getRole().equals("student")){
                                                                appPreference.SetUser(task.getResult().getUser().getUid());
                                                                intentTo(MainActivity.class);
                                                            }

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        progressDialog.dismiss();
                                                        showMessage("Failed",databaseError.getMessage());
                                                    }
                                                });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            showMessage("Failed",e.getMessage());
                        }
                    });

                }
            }
        });

    }
    private void intentTo(Class className){
        Intent intent = new Intent(this,className);
        startActivity(intent);
        finish();
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
}
//qwqweqwe
