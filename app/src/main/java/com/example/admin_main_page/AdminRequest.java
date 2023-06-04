package com.example.admin_main_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminRequest extends AppCompatActivity {

    RecyclerView RequestRecyclerView;

    SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    ArrayList<Request> requests;

    DatabaseReference database ;

    RequestAdapter requestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        editor = sharedPreferences.edit();


        RequestRecyclerView = findViewById(R.id.RequestRecyclerView);
        requests = new ArrayList<>();
        RequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = FirebaseDatabase.getInstance().getReference();
        database.child("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Iterable<DataSnapshot> children =snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    //  String key = child.getKey();
                    //  editor.putString("BookID",key);
                    Request request = child.getValue(Request.class);
                    // Add the request object to the list
                    requests.add(request);
                }
                requestAdapter.setRequests(requests);
                requestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        requestAdapter = new RequestAdapter(this, requests);
        RequestRecyclerView.setAdapter(requestAdapter);

    }
}