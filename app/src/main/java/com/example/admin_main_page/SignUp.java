package com.example.admin_main_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    EditText email,password,confirmPassword,Name;
    Button signup;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        email = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        Name = findViewById(R.id.Name);
        confirmPassword = findViewById(R.id.confirmPassword);
        signup = findViewById(R.id.btn_SignUp);
        ImageButton BackButton = findViewById(R.id.SignUpBackButton);

        BackButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        });
        signup.setOnClickListener(view -> {
            if(email.getText().toString().trim().isEmpty())
            {
                email.setError("Missing Required Field");
            }
            if (password.getText().toString().trim().isEmpty()) {
                password.setError("Missing Required Field");
            }
            if (confirmPassword.getText().toString().trim().isEmpty()) {
                confirmPassword.setError("Missing Required Field");
            }
            if (Name.getText().toString().trim().isEmpty()) {
                Name.setError("Missing Required Field");
            }
            if(!email.getText().toString().trim().isEmpty() && password.getText().toString().trim().isEmpty()&& confirmPassword.getText().toString().trim().isEmpty()&& Name.getText().toString().trim().isEmpty())
                PerforAuth();
            else {
                PerforAuth();
            }
        });
    }
    public static int counter = 0;
    public static String uniqueId;
    private void PerforAuth()
    {
        String inputemail = email.getText().toString();
        String name = Name.getText().toString();
        String pass = password.getText().toString();
        String pass2 = confirmPassword.getText().toString();


        if(!pass.equals(pass2))
        {
            password.setError("Passwords do not match");
        }
        else if (pass.length() < 6)
        {

            password.setError("Password must be at least 6 characters long");
        }
        else if(!inputemail.matches(emailPattern))
        {
            email.setError("Please make sure you enter a valid email address");
        }
        else {
            mAuth.createUserWithEmailAndPassword(inputemail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())
                    {

                        User user = new User(name,inputemail,0);
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        firebaseDatabase.getReference().child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                {
                                    String id = String.valueOf(snapshot.getKey());
                                    if (id.startsWith("1910")) {
                                        String numberString = id.substring(4);
                                        int number = Integer.parseInt(numberString);
                                        counter = Math.max(counter, number);
                                    }
                                }
                                if(counter == 9999)
                                {
                                    showToast("Unable to add more users");
                                }
                                else
                                {
                                    counter++;
                                    // Convert the counter to a 4-digit string
                                    String formattedCounter = String.format("%04d", counter);

                                    // Construct the unique ID with the prefix and the formatted counter
                                    uniqueId = "1910" + formattedCounter;

                                    firebaseDatabase.getReference().child("User").child(uniqueId).setValue(user);
                                    showToast("You have successfully registered. Your ID is " + uniqueId);
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        sendUserToNextActivity();
                    }
                    else
                    {
                        showToast("" + task.getException());

                    }

                }

                private void sendUserToNextActivity() {
                    Intent intent = new Intent(SignUp.this, UserHome.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

            });
        }

    }

    private void  showToast (String message)
    {
        Toast.makeText(this,"Error deleting record" , Toast.LENGTH_SHORT).show();
    }
}