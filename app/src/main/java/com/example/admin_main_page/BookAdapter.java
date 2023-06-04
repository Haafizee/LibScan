package com.example.admin_main_page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Book> bookDetailList;


    public BookAdapter() {
    }

    public BookAdapter(Context context, ArrayList<Book> bookDetailList) {
        this.context = context;
        this.bookDetailList = bookDetailList;
    }

    public ArrayList<Book> getBookDetailList() {
        return bookDetailList;
    }

    public void setBookDetailList(ArrayList<Book> bookDetailList) {
        this.bookDetailList = bookDetailList;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.activity_book_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Book book = bookDetailList.get(position);

        holder.bind(book);

        holder.Image.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            RecyclerView recyclerView = ((RecyclerView) holder.itemView.getParent());
            Intent intent = new Intent(context, DisplayBook.class);
            int recyclerViewId = recyclerView.getId();
            // Handle the click event with the RecyclerView ID
            if (recyclerViewId == R.id.AvailableBooksRecyclerView) {
                // Click event in AvailableBooksRecyclerView
                bundle.putInt("User",0);
            } else if (recyclerViewId == R.id.UserBooksRecyclerView) {
                // Click event in UserBooksRecyclerView
                bundle.putInt("User",1);
            }



            bundle.putString("BookId",book.getId());
            bundle.putString("BookTitle", book.getTitle());
            bundle.putString("Description",book.getDescription());
            bundle.putString("Category",book.getCategory());
            bundle.putInt("Unit",book.getUnits());
            bundle.putString("Image", book.getImageUrl());
            intent.putExtras(bundle);

            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return bookDetailList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton Image;
        public ViewHolder(View itemView) {
            super(itemView);

            this.Image= itemView.findViewById(R.id.imageView);
        }
        public void bind(Book book){
            // Load the image using Picasso and resize it to your desired dimensions
            Picasso.get()
                    .load(book.getImageUrl())
                    .resize(450, 700) // Set your desired width and height here
                    .centerCrop()// Apply center-crop transformation if needed
                    .placeholder(R.drawable.book_cover)
                    .into(Image);
        }
    }

}
