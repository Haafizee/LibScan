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
import java.util.Date;
import java.util.Locale;

public class AdminHome extends AppCompatActivity {
    RecyclerView BookrecyclerView;
    RecyclerView UserRecyclerView;
    ArrayList<Book> Books;
    ArrayList<User> Users;
    BookAdapter bookAdapter;
    UserAdapter userAdapter;
    ImageButton AddBook;
    ImageButton IssueBook;
    ImageButton RequestButton;
    ImageButton ReturnButton;
    DatabaseReference database ;
    TextView AdminName;
    String storedUid;
    ImageButton Logout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Logout = findViewById(R.id.AdminLogOutButton);
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        AdminName = findViewById(R.id.AdminName);
        String val = sharedPreferences.getString("UserName","");
        AdminName.setText(val);
        Books();
        User();
        AddBook = findViewById(R.id.AddButton);
        IssueBook =findViewById(R.id.IssueButton);
        RequestButton = findViewById(R.id.RequestButton);
        ReturnButton = findViewById(R.id.ReturnButton);
        ReturnButton.setOnClickListener(view -> {

            Intent intent = new Intent(this, ReturnBook.class);
            this.startActivity(intent);

        });
        RequestButton.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, AdminRequest.class);
            this.startActivity(intent);
        });
        IssueBook.setOnClickListener(view -> {

            Intent intent = new Intent(this, IssueBook.class);
            this.startActivity(intent);

        });
       AddBook.setOnClickListener(view -> {

            Intent intent = new Intent(this, AddBook.class);
            this.startActivity(intent);

        });
        Logout.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        });
        Notification();

    }


    private void Notification()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String dueDate = "" ,bookTitle,UserName;
                Iterable<DataSnapshot> children =snapshot.getChildren();

                for (DataSnapshot child : children)
                {
                    DataSnapshot booksSnapshot = child.child("Books");

                    for (DataSnapshot bookSnapshot : booksSnapshot.getChildren())
                    {
                        UserName = child.child("name").getValue(String.class);
                        storedUid = child.getKey();
                        dueDate = bookSnapshot.child("Due Date").getValue(String.class);
                        bookTitle = bookSnapshot.child("title").getValue(String.class);
                        String k = bookTitle;

                        if (dueDate != null && isDueDatePassed(dueDate))
                        {
                            // Send Notification
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                if (ContextCompat.checkSelfPermission(AdminHome.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(AdminHome.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);

                                }
                            }

                            makeNotification(" Book Return Overdue", "User "+UserName+" (ID: " + storedUid + ") has failed to return the " + bookTitle + "\n Please take immediate action to remind the user to return the book promptly.");

                        }
                    }
                }
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

    private void User()
    {


        UserRecyclerView = findViewById(R.id.UsersRecyclerView);
        Users = new ArrayList<>();
        UserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        database= FirebaseDatabase.getInstance().getReference();
        database.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children =snapshot.getChildren();
                for (DataSnapshot child : children) {
                    User user = child.getValue(User.class);
                    if(user.getUserType() == 0)
                        Users.add(user);
                }
                userAdapter.setUserList(Users);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        userAdapter = new UserAdapter(this, Users);
        UserRecyclerView.setAdapter(userAdapter);
    }

    private void  Books ()
    {

        BookrecyclerView = findViewById(R.id.BooksRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        BookrecyclerView.setLayoutManager(layoutManager);
        Books = new ArrayList<>();
        database= FirebaseDatabase.getInstance().getReference();
        database.child("Book").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children =snapshot.getChildren();
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

        bookAdapter = new BookAdapter(this, Books);
        BookrecyclerView.setAdapter(bookAdapter);

    }
    private void  showToast(String message)
    {
        Toast.makeText(this,message , Toast.LENGTH_SHORT).show();
    }
}