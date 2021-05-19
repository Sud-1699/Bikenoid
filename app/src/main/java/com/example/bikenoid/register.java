package com.example.bikenoid;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {
    EditText FullName,Email,Password,Phone;
    Button SignUp;
    TextView LoginHere;
    FirebaseAuth Auth;
    ProgressBar ProgressBar;
    private long backPressedTime;
    private Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FullName = findViewById(R.id.FullName);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        Phone = findViewById(R.id.Phone);
        SignUp = findViewById(R.id.SignUp);
        LoginHere = findViewById(R.id.textView3);

        Auth = FirebaseAuth.getInstance();
        ProgressBar = findViewById(R.id.progressBar);

        //ALready Registered

        if(Auth.getCurrentUser() != null){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
         }



        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Email.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Password.setError("Password is required");
                    return;
                }

                if(password.length()< 6){
                    Password.setError("Password must be greater than 6 characters");
                    return;
                }

                ProgressBar.setVisibility(View.VISIBLE);

                //register the user in firebase

                Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(register.this,"User Created",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }

                        else{
                            Toast.makeText(register.this,"Error!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            ProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        LoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });

    }

    @Override
    public void onBackPressed(){

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
//            super.onBackPressed();
//            return;
            register.this.finishAffinity();

        }
        else{
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}

