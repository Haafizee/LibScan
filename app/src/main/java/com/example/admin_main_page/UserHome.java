package com.example.admin_main_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserHome extends AppCompatActivity {
    RecyclerView BookRecyclerView;
    RecyclerView UserBooksRecyclerView;
    private ArrayList<Book> Books;
    private ArrayList<Book> UserBooks;
    BookAdapter bookAdapter;
    BookAdapter UserbookAdapter;
    TextView UserName;

    String storedUid;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    DatabaseReference database ;
    ImageButton Logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setContentView(R.layout.activity_user_home);
        UserName= findViewById(R.id.UserNameText);
        Logout = findViewById(R.id.AddLogOutButton);
        storedUid=sharedPreferences.getString("Uid","");
        String name = sharedPreferences.getString("UserName","");
        UserName.setText(name);
        UserBooks();
        AvailableBooks();
        Notification();
        Logout.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        });



    }

    private void Notification()
    {
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        storedUid = sharedPreferences.getString("Uid", "");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("User").child(storedUid).child("Books").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Iterable<DataSnapshot> children =snapshot.getChildren();

                for (DataSnapshot child : children)
                {
                    String dueDate = child.child("Due Date").getValue(String.class);
                    String BookName = child.child("title").getValue(String.class);
                    int IsAccepted = child.child("flagAccepted").getValue(Integer.class);
                    if (dueDate != null && isDueDatePassed(dueDate))
                    {
                        // Send Notification
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        {
                            if (ContextCompat.checkSelfPermission(UserHome.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                            {
                                ActivityCompat.requestPermissions(UserHome.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
                            }
                        }
                        makeNotification("Book Return Reminder","The due date to return the " + BookName +" has passed! \n  Please return it as soon as possible.");
                    }
                    else if(isSameDate(dueDate))
                    {
                        makeNotification("Book Return Reminder","Today is the due date to return the "+ BookName+". \n Don't forget to return it!");
                    }//The response of the request
                    if(IsAccepted == 0)
                    {
                        makeNotification("Book Return Extension Rejected","We regret to inform you that your request to extend the period for returning the book titled "+ BookName +" has been rejected by the admin. The original due date remains unchanged.");

                    }
                }
            }

            private boolean isSameDate(String dueDate) {

                Date currentDate = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                try {
                    Date parsedDueDate = dateFormat.parse(dueDate);

                    Calendar cal1 = Calendar.getInstance();
                    Calendar cal2 = Calendar.getInstance();
                    cal1.setTime(currentDate);
                    cal2.setTime(parsedDueDate);

                    return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
                            && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return false;
            }


            private void makeNotification(String body, String title) {

                String channelID = "CHANNEL_ID_NOTIFICATION";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID);
                builder.setSmallIcon(R.drawable.ic_notifications)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);

                    if (notificationChannel == null) {
                        CharSequence name = "Channel Name";
                        String description = "Channel Description";
                        int importance = NotificationManager.IMPORTANCE_HIGH;

                        notificationChannel = new NotificationChannel(channelID, name, importance);
                        notificationChannel.setDescription(description);
                        notificationChannel.setLightColor(Color.GREEN);
                        notificationChannel.enableVibration(true);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                }

                int notificationId = (int) System.currentTimeMillis(); // Generate a unique notification ID
                notificationManager.notify(notificationId, builder.build());
            }



            private boolean isDueDatePassed(String dueDate) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date currentDate = new Date();

                try {
                    Date parsedDueDate = dateFormat.parse(dueDate);
                    // Compare the due date with the current date
                    return currentDate.after(parsedDueDate);
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        });
    }

    private void UserBooks(){
        UserBooksRecyclerView = findViewById(R.id.UserBooksRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        UserBooksRecyclerView.setLayoutManager(layoutManager);
        UserBooks = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference().child("User").child(storedUid);
        UserbookAdapter = new BookAdapter(this, UserBooks);
        UserBooksRecyclerView.setAdapter(UserbookAdapter);

        database.child("Books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Book book = child.getValue(Book.class);
                    UserBooks.add(book);
                }
                UserbookAdapter.setBookDetailList(UserBooks);
                UserbookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void AvailableBooks() {
        BookRecyclerView = findViewById(R.id.AvailableBooksRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        BookRecyclerView.setLayoutManager(layoutManager);
        Books = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference();
        bookAdapter = new BookAdapter(this, Books); // Move this line above setLayoutManager
        BookRecyclerView.setAdapter(bookAdapter);

        database.child("Book").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Book book = child.getValue(Book.class);
                    Books.add(book);
                }
                bookAdapter.setBookDetailList(Books);
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void  showToast(String message)
    {
        Toast.makeText(this,message , Toast.LENGTH_SHORT).show();
    }
}