package com.example.admin_main_page;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DisplayBook extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    DatabaseReference reference;
    DatabaseReference database ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_book);

        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        TextView Description = findViewById(R.id.BookDescription);
        TextView Title = findViewById(R.id.BookTitle);
        TextView Category = findViewById(R.id.BookCategory);
        TextView Units = findViewById(R.id.BookUnits);
        ImageButton Back = findViewById(R.id.DisplayBackButton);
        ImageView Image = findViewById(R.id.BookImageDisplay);
        ImageButton Delete =findViewById(R.id.DisplayRemoveButton);
        TextView BookIdDisplay = findViewById(R.id.BookIdDisplay);
        ImageButton Reissue = findViewById(R.id.DisplayReissueButton);
        Bundle bundle = getIntent().getExtras();
        String BookId = "";
        int UserFlag = -1;

        if (bundle != null)
        {
            BookId = bundle.getString("BookId");
            UserFlag = bundle.getInt("User");
            BookIdDisplay.setText("Book ID: " + BookId);
            Description.setText(bundle.getString("Description"));
            Title.setText(bundle.getString("BookTitle"));
            Category.setText(bundle.getString("Category"));
            Units.setText("Available Units:" + bundle.getInt("Unit"));

            // Load the image using Picasso and resize it to your desired dimensions
            Picasso.get()
                    .load(bundle.getString("Image"))
                    .resize(450, 750) // Set your desired width and height here
                    .centerCrop() // Apply center-crop transformation if needed
                    .placeholder(R.drawable.book_cover)
                    .into(Image);

        }
        int val = sharedPreferences.getInt("UserType",0);
        Back.setOnClickListener(view -> {
            if(val==1) {
                Intent intent = new Intent(this, AdminHome.class);
                this.startActivity(intent);
            }
            else {
                Intent intent1 = new Intent(this, UserHome.class);
                this.startActivity(intent1);
            }

        });
        String finalBookId = BookId;
        if(UserFlag == 0 )
        {
            Reissue.setVisibility(View.GONE);
        }
        else if(UserFlag == 1){
            Reissue.setOnClickListener(view -> {

                //String BookId = bookId.getText().toString();
                sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                String storedUid = sharedPreferences.getString("Uid", "");
                database= FirebaseDatabase.getInstance().getReference();

                database.child("Request").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long maxId=0;
                        for (DataSnapshot child : snapshot.getChildren()) {
                            String id = child.getKey();
                            long currentId = Long.parseLong(id);
                            if (currentId > maxId) {
                                maxId = currentId;
                            }
                        }
                        long newNumber = maxId + 1;
                        database.child("Request").child(newNumber+"").child("bookID").setValue( finalBookId);
                        database.child("Request").child(newNumber+"").child("userID").setValue( storedUid);
                        database.child("Request").child(newNumber+"").child("requestID").setValue(newNumber + "");
                        showToast("The Reissue Request Is Being Processed");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            });
        }


       if( val == 0 )
        {
            Delete.setVisibility(View.GONE);
        }
        else {
            Delete.setOnClickListener(view -> {
                if (finalBookId != null) {
                    delete(finalBookId);
                }
                Intent intent = new Intent(this, AdminHome.class);
                this.startActivity(intent);
            });
        }


    }

    private void delete(String id)
    {
        reference = FirebaseDatabase.getInstance().getReference("Book").child(id);

        Task<Void> mTask = reference.removeValue();

        mTask.addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void unused)
            {
                showToast("Deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Error deleting record");
            }
        });
    }

    private void  showToast (String message)
    {
        Toast.makeText(this,message , Toast.LENGTH_SHORT).show();
    }
}