package com.example.admin_main_page;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Request> requests;
    SharedPreferences sharedPreferences;

    public RequestAdapter()
    {
    }

    public RequestAdapter(Context context, ArrayList<Request> requests) {
        this.context = context;
        this.requests = requests;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.request_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        final Request request = requests.get(position);
        holder.UserID.setText("User ID: "+request.getUserID());
        holder.BookID.setText("Book ID: "+request.getBookID());
        holder.Accept.setOnClickListener(view ->
        {
            ////Start
            String userId = request.getUserID();
            String bookId =  request.getBookID();
            FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("Books").child(bookId).child("flagAccepted").setValue(1);

            FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("Books").child(bookId).child("Due Date").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String currentDueDate = snapshot.getValue(String.class);
                    // Parse the current due date string into a Date object
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    Date dueDate;
                    try {
                        dueDate = dateFormat.parse(currentDueDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return; // Handle the parse exception
                    }

                    // Add 3 days to the current due date
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dueDate);
                    calendar.add(Calendar.DAY_OF_MONTH, 3);

                    // Get the new due date as a formatted string
                    String newDueDate = dateFormat.format(calendar.getTime());

                    // Update the due date value in Firebase Realtime Database
                    FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("Books").child(bookId).child("Due Date").setValue(newDueDate);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //Remove from DataBase
           // String key = sharedPreferences.getString("BookID", "");
            FirebaseDatabase.getInstance().getReference("Request").child(request.getRequestID()).removeValue();//logical
            //Remove from RecyclerView
            // Refresh the page by recreating the activity or fragment
            Context context = view.getContext();
            while (context instanceof ContextWrapper)
            {
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    activity.recreate();
                    break;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }

        });
        holder.Reject.setOnClickListener(view -> {

            String requestID = request.getRequestID();
            String userId = request.getUserID();
            String bookId =  request.getBookID();
            FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("Books").child(bookId).child("flagAccepted").setValue(0);

            //remove the request from database
            FirebaseDatabase.getInstance().getReference("Request").child(requestID).removeValue();//logical

            //remove it from recyclerView

            // Refresh the page by recreating the activity or fragment
            Context context = view.getContext();
            while (context instanceof ContextWrapper)
            {
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    activity.recreate();
                    break;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }

        });
    }
    @Override
    public int getItemCount() {
        return requests.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageButton Accept,Reject;

        TextView UserID,BookID;
        public ViewHolder(View itemView) {
            super(itemView);

            Accept= itemView.findViewById(R.id.Accept);
            Reject= itemView.findViewById(R.id.Reject);
            UserID = itemView.findViewById(R.id.UserId);
            BookID=itemView.findViewById(R.id.BookId);
        }
    }

}
