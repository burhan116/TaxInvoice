package com.example.cooltraders1.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cooltraders1.R;
import com.example.cooltraders1.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    EditText emailEditText, passwordEditText;
    Button loginButton;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);


        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEditText.getText() == null || passwordEditText.getText() == null) {
                    Toast.makeText(LoginActivity.this, "please enter all details",
                            Toast.LENGTH_SHORT).show();
                } else if (passwordEditText.getText().length() < 6) {
                    Toast.makeText(LoginActivity.this,
                            "password must at least 6 character", Toast.LENGTH_SHORT).show();
                } else {
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    login(email,password);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

    }

    private void login(String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                } else {
                    Log.e(TAG,task.getException().toString());
                    Toast.makeText(LoginActivity.this,
                            "Fail to signUp the user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}