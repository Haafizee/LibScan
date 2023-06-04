package com.example.admin_main_page;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddBook extends AppCompatActivity {
ImageButton BackButton;

    EditText BookID;
    EditText BookTitle;
    EditText BookDescription;
    Spinner BookCategory;
    EditText NoUnits;
    EditText BookAuthor;
    Button AddButton;
    ImageView SelectImage;
    Book book=new Book();
    DatabaseReference databaseReference;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        BackButton = findViewById(R.id.AddBackButton);
        BackButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, AdminHome.class);
            this.startActivity(intent);
        });


        BookID = findViewById(R.id.BookID);
        BookTitle=findViewById(R.id.Title);
        BookDescription = findViewById(R.id.Description);
        BookCategory = findViewById(R.id.category);
        BookAuthor = findViewById(R.id.AuthorName);
        NoUnits = findViewById(R.id.NoOfUnits);
        AddButton = findViewById(R.id.addBookButton);
        SelectImage = findViewById(R.id.SelectImage);



        // Set click listener for the "Upload Image" button
        AddButton.setOnClickListener(view -> {
            insertNewBook();
            uploadImage();
            Intent intent = new Intent(this, AdminHome.class);
            this.startActivity(intent);
        });


        // Set click listener for the "Select Image" button
        SelectImage.setOnClickListener(view ->{
            openGallery();
        });



    }
    private void insertNewBook() {

        book.setId( BookID.getText().toString());
        book.setTitle(BookTitle.getText().toString());
        book.setDescription(BookDescription.getText().toString());
        book.setCategory( BookCategory.getSelectedItem().toString());
        book.setAuthorName( BookAuthor.getText().toString());
        book.setUnits(Integer.parseInt(NoUnits.getText().toString()));
        // Select image from phone using an image picker library or Android's built-in intent
        // showToast("The Book has been successfully added");
    }





    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    private void uploadImage() {
        if (selectedImageUri != null) {
            // Convert image URI to a byte array
            byte[] imageData = getByteArrayFromUri(selectedImageUri);

            // Generate a unique filename for the image
            String filename = generateUniqueFilename(book.getId());

            // Upload image to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + filename);
            UploadTask uploadTask = storageRef.putBytes(imageData);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get the download URL of the uploaded image
                    Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
                    downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            // Save the download URL to the book object
                            book.setImageUrl(downloadUri.toString());

                            // Save the book data to the Realtime Database
                            DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference().child("Book");
                            booksRef.child(book.getId()).setValue(book);

                            showToast("The Book has been successfully added");


                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            SelectImage.setImageURI(selectedImageUri);
        }
    }

    private byte[] getByteArrayFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            Log.e(TAG, "Failed to read image data: " + e.getMessage());
            return null;
        }
    }

    private String generateUniqueFilename(String bookId) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return bookId + "_" + timestamp + ".jpg";
    }

    private void  showToast (String message)
    {
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
    }
}