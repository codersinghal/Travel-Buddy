package com.example.travelbuddy.home;

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

import com.example.travelbuddy.R;
import com.example.travelbuddy.recommendations.PreferencesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private Button _login;
    private TextView _signup;
    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        user=auth.getCurrentUser();
        if(user!=null)
        {
            SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            if(!sharedPreferences.contains("selected"))
            {
                Intent intent = new Intent(LoginActivity.this, PreferencesActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
        email=(EditText)findViewById(R.id.input_email);
        password=(EditText)findViewById(R.id.input_pass);
        _login=(Button) findViewById(R.id.btn_login);
        _signup=(TextView) findViewById(R.id.link_signup);

        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailid=email.getText().toString();
                String pass=password.getText().toString();
                if(pass.isEmpty()||pass==null)
                {
                    password.setError("Required");
                    return;
                }
                if(emailid.isEmpty()||emailid==null)
                {
                    email.setError("Required");
                    return;
                }
                auth.signInWithEmailAndPassword(emailid, pass)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                //progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        password.setError("error");
                                    } else {
                                        Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    if(!sharedPreferences.contains("selected"))
                                    {
                                        Intent intent = new Intent(LoginActivity.this, PreferencesActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        });
            }
        });
        _signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
