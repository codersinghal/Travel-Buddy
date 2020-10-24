package com.example.travelbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText name,email,password;
    private TextView _login;
    private Button _signup;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        name=findViewById(R.id.input_name);
        email=findViewById(R.id.input_email);
        password=findViewById(R.id.input_password);
        _signup=findViewById(R.id.btn_signup);
        _login=findViewById(R.id.link_login);

        _signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailid=email.getText().toString();
                String pass=password.getText().toString();
                String username=name.getText().toString();
                if(emailid.isEmpty()||emailid==null)
                {
                    email.setError("Required");
                    return ;
                }
                if(pass.isEmpty()||pass==null)
                {
                    password.setError("Required");
                    return;
                }
                if(username.isEmpty()||username==null)
                {
                    name.setError("Required");
                    return ;
                }
                auth.createUserWithEmailAndPassword(emailid, pass)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseUser user=auth.getCurrentUser();
                                    Profile pf=new Profile("abcd",emailid,"India","","");
                                    DatabaseReference mref= FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("profile");
                                    mref.setValue(pf);
                                    SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    if(!sharedPreferences.contains("selected"))
                                    {
                                        Intent intent = new Intent(SignUpActivity.this, PreferencesActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            }
                        });
            }
        });
        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });
    }
}
