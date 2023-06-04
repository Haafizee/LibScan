package com.example.admin_main_page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class HomeLogin extends AppCompatActivity {
    Button login_u,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_login);
        login_u = findViewById(R.id.login_u);

        signup = findViewById(R.id.signup);

        login_u.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);

        });

        signup.setOnClickListener(view ->
        {
            Intent intent = new Intent(this,SignUp.class);
            startActivity(intent);

        });
    }
}