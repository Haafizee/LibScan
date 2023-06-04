package com.example.admin_main_page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private SlideAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        myAdapter = new SlideAdapter(this);
        viewPager.setAdapter(myAdapter);

        Button StartButton = findViewById(R.id.StartButton);

        StartButton.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, HomeLogin.class);
            startActivity(intent);
        });
    }
}