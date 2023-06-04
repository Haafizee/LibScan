package com.example.admin_main_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.HashSet;
import java.util.Set;

public class Login extends AppCompatActivity {

    EditText password;
    AutoCompleteTextView email;
    Button login;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    CheckBox rememberMe;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.+[a-z]+";

    // Type of User Shared Preference
  /*  SharedPreferences TypeShared = getSharedPreferences("MySharedPref",MODE_PRIVATE);

    // Creating an Editor object to edit(write to the file)
    SharedPreferences.Editor TypeSharedEdit = sharedPreferences.edit();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.btn_login);
        rememberMe = findViewById(R.id.rememberMe);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ArrayAdapter<String> emailAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        email.setAdapter(emailAdapter);
        Set<String> emailSuggestions = sharedPreferences.getStringSet("EmailSuggestions", null);

        if (emailSuggestions != null)
        {
            emailAdapter.addAll(emailSuggestions);
        }

        email.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                String inputEmail = charSequence.toString();
                emailAdapter.getFilter().filter(inputEmail);

                if (emailAdapter.getPosition(inputEmail) != -1)
                {
                    // Retrieve the corresponding saved password
                    String savedPasswordKey = inputEmail + "_password";//Secret Key
                    // Use the email as a unique key for the saved password
                    String savedPassword = sharedPreferences.getString(savedPasswordKey, "");
                    // Set the password field with the saved password
                    password.setText(savedPassword);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ImageButton BackButton = findViewById(R.id.LoginBackButton);

        BackButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        });
        login.setOnClickListener(view -> {

            if(email.getText().toString().trim().isEmpty())
            {
                email.setError("Missing Required Field");
            }
            if (password.getText().toString().trim().isEmpty()) {
                password.setError("Missing Required Field");
            }
            if(!password.getText().toString().trim().isEmpty() && !email.getText().toString().trim().isEmpty())
                performLogin();
        });
    }
    private void performLogin() {

        String inputEmail = email.getText().toString();
        String pass = password.getText().toString();

        if (pass.length() < 6)
        {
            password.setError("password must be at least 6 characters long");
        }
        else if(!inputEmail.matches(emailPattern))
        {
            email.setError("Enter Proper Email.");
        }
        else
        {
            mAuth.signInWithEmailAndPassword(inputEmail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {



                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        firebaseDatabase.getReference().child("User").addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                Iterable<DataSnapshot> children =snapshot.getChildren();
                                for (DataSnapshot child : children)
                                {
                                    User user = child.getValue(User.class);
                                    if(user.getEmail().equals(inputEmail))
                                    {
                                        String Uid = child.getKey();
                                        firebaseDatabase.getReference().child("User").child(Uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String Name = snapshot.getValue(String.class);
                                                editor.putString("UserName" , Name);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        firebaseDatabase.getReference().child("User").child(Uid).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot)
                                            {
                                                int userType = snapshot.getValue(Integer.class);

                                                //type share
                                                editor.putInt("UserType",userType);
                                                editor.putString("Uid",Uid);
                                                if(userType == 0)
                                                {
                                                    if (rememberMe.isChecked()) {
                                                        try {
                                                            // Save the entered email as a suggestion
                                                            Set<String> emailSuggestions = sharedPreferences.getStringSet("EmailSuggestions", new HashSet<>());
                                                            emailSuggestions.add(inputEmail);
                                                            editor.putStringSet("EmailSuggestions", emailSuggestions);
                                                            String savedPasswordKey = inputEmail + "_password";
                                                            editor.putString(savedPasswordKey, pass);
                                                        }
                                                        catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        editor.clear();
                                                    }
                                                    editor.apply();

                                                    ProgressDialog progressDialog = new ProgressDialog(Login.this);
                                                    progressDialog.setMessage("Logging in...");
                                                    progressDialog.setCancelable(false);
                                                    progressDialog.show();


                                                    Intent intent = new Intent(Login.this, UserHome.class);
                                                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                    showToast("Logged in Successfully");
                                                    progressDialog.dismiss();


                                                }
                                                else
                                                {

                                                    if (rememberMe.isChecked())
                                                    {
                                                        try {
                                                            // Save the entered email as a suggestion
                                                            Set<String> emailSuggestions = sharedPreferences.getStringSet("EmailSuggestions", new HashSet<>());
                                                            emailSuggestions.add(inputEmail);
                                                            editor.putStringSet("EmailSuggestions", emailSuggestions);
                                                            String savedPasswordKey = inputEmail + "_password";
                                                            editor.putString(savedPasswordKey, pass);
                                                        }
                                                        catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        editor.clear();
                                                    }
                                                    editor.apply();

                                                    ProgressDialog progressDialog = new ProgressDialog(Login.this);
                                                    progressDialog.setMessage("Logging in...");
                                                    progressDialog.setCancelable(false);
                                                    progressDialog.show();

                                                    Intent intent = new Intent(Login.this, AdminHome.class);
                                                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();

                                                    showToast("Logged in Successfully");
                                                    progressDialog.dismiss();

                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error)
                                            {

                                            }
                                        });

                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                    }
                    else
                    {
                        Exception exception = task.getException();
                        String errorMessage = exception != null ? exception.getMessage() : "Unknown error occurred";
                        showToast(errorMessage);
                    }

                }
            });
        }
    }
    private void  showToast (String message)
    {
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
    }
}