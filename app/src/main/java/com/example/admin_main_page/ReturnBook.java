package com.example.admin_main_page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReturnBook extends AppCompatActivity {
    EditText BookID;
    EditText UserID;
    Button ReturnButton;
    ImageButton BackButton;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);

        BookID = findViewById(R.id.BookId);
        UserID = findViewById(R.id.UserID);
        ReturnButton = findViewById(R.id.ReturnButton);
        database= FirebaseDatabase.getInstance().getReference();
        BackButton = findViewById(R.id.ReturnBackButton);
        ReturnButton.setOnClickListener(view -> {
            String StringBookID = BookID.getText().toString();
            String StringUserID = UserID.getText().toString();
            IncreaseUnits(StringBookID);
            RemoveTheBook(StringBookID,StringUserID);
        });
        BackButton.setOnClickListener(view -> {

            Intent intent = new Intent(this, AdminHome.class);
            this.startActivity(intent);
        });

    }

    private void RemoveTheBook(String BookID,String UserID)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("User").child(UserID).child("Books").child(BookID).removeValue();
    }

    private void IncreaseUnits(String BookID) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("Book").child(BookID).child("units").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                int units = -1;//you should initialize it first
                snapshot.getChildrenCount();
                if(snapshot.getValue() == null)
                {
                    showToast("Failed");
                }
                else
                {
                    units = snapshot.getValue(Integer.class);
                    units++;
                    firebaseDatabase.getReference("Book").child(BookID).child("units").setValue(units);
                    showToast("Success!");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        });
    }

    private void  showToast(String message)
    {
        Toast.makeText(this,message , Toast.LENGTH_SHORT).show();
    }
}