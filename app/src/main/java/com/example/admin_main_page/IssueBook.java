package com.example.admin_main_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class IssueBook extends AppCompatActivity {
    DatabaseReference databaseReference;
    boolean UnitsFlag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_book);


        EditText BookID = findViewById(R.id.BookId);
        EditText UserID = findViewById(R.id.UserId);
        Button IssueButton = findViewById(R.id.issueBookButton);
        ImageButton BackButton = findViewById(R.id.IssueBackButton);

        BackButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, AdminHome.class);
            this.startActivity(intent);
        });

        IssueButton.setOnClickListener(view -> {

            String bookId = BookID.getText().toString();
            String userId = UserID.getText().toString();
            databaseReference = FirebaseDatabase.getInstance().getReference();

            databaseReference.child("Book").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Iterable<DataSnapshot> children =snapshot.getChildren();
                    for (DataSnapshot child : children)
                    {

                        Book book = child.getValue(Book.class);
                        if (bookId.equals(child.getKey()))
                        {
                            FirebaseDatabase.getInstance().getReference("Book").child(bookId).child("units").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {


                                    int units = snapshot.getValue(Integer.class);
                                    if(units>0)
                                    {
                                        units--;
                                        FirebaseDatabase.getInstance().getReference("Book").child(bookId).child("units").setValue(units);
                                    }
                                    else{
                                        UnitsFlag = false;
                                        showToast("No Available Books");
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            if(UnitsFlag == true) {
                                book.setUnits(1);
                                book.setFLagAccepted(-1);
                                databaseReference.child("User").child(userId).child("Books").child(bookId).setValue(book);
                                showToast("The Book has been successfully added");

                                // Get the current timestamp in milliseconds
                                long timestamp = new Date().getTime();
                                // Add 7 days to the timestamp
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(timestamp);
                                calendar.add(Calendar.DAY_OF_MONTH, 7);
                                long newTimestamp = calendar.getTimeInMillis();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                String formattedDate = dateFormat.format(new Date(newTimestamp));
                                databaseReference.child("User").child(userId).child("Books").child(bookId).child("Due Date").setValue(formattedDate);

                            }
                        }


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        });






    }

    private void  showToast (String message)
    {
        Toast.makeText(this,message , Toast.LENGTH_SHORT).show();
    }
}