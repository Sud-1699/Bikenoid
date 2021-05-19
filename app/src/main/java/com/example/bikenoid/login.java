package com.example.bikenoid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class login extends AppCompatActivity {
    EditText Email,Password;
    Button Login;
    TextView RegisterHere;
    FirebaseAuth Auth;
    ProgressBar ProgressBar;
    private long backPressedTime;
    private Toast backToast;
//   android.widget.ProgressDialog ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        Login = findViewById(R.id.Login);
        RegisterHere = findViewById(R.id.textView3);

        Auth = FirebaseAuth.getInstance();
        ProgressBar = findViewById(R.id.progressBar2);

        if(Auth.getCurrentUser() != null){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
         }


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Email.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Password.setError("Password is required");
                    return;
                }

                if (password.length() < 6) {
                    Password.setError("Password must be greater than 6 characters");
                    return;
                }

                ProgressBar.setVisibility(View.VISIBLE);

                //Authenticate the user

                Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }

                        else {
                            Toast.makeText(login.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            ProgressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });



        RegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), register.class));
            }
        });
    }
    @Override
    public void onBackPressed(){

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
//            super.onBackPressed();
//            return;
            login.this.finishAffinity();
        }
        else{
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

}







