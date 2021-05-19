package com.example.bikenoid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth Auth;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Auth = FirebaseAuth.getInstance();

        FirebaseUser User = Auth.getCurrentUser();
    }

    public void logout(View view) {
        Auth.signOut(); //logout
        startActivity(new Intent(getApplicationContext(),login.class));
        finish();
    }

    public void maps(View view) {
        startActivity(new Intent(getApplicationContext(),Maps.class));
    }
    @Override
    public void onBackPressed(){

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
//            super.onBackPressed();
//            return;
            MainActivity.this.finishAffinity();
        }
        else{
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
