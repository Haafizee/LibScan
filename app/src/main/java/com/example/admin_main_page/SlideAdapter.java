package com.example.admin_main_page;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SlideAdapter extends PagerAdapter {


    Context context;
    LayoutInflater inflater;



    //List of images
    public int[]  images = {

            R.drawable.third_removebg_preview,
            R.drawable.welcome_removebg_preview,
            R.drawable.second_removebg_preview,
            R.drawable.first_removebg_preview


    };

    //list of titles
    public String[] titles ={

            "Hello and Welcome here!",
            "",
            "",
            ""
            // "Unlock, Explore, Discover, Read!"
            //  "Journey Through Literary Worlds."

    };

    //list of descriptions

    public String [] descriptions = {

            "unlock the power of books and explore new horizons with us!",
            //"Where Books Ignite Imagination!",
            "Embrace the Joy of Reading.",
            "Knowledge Awaits Within Pages.",
            "Discover Boundless Book Treasures."

    };

    //list of background colors
    public int [] BackgroundColor = {

            Color.rgb(22,38,52),
            Color.rgb(255,217,102),
            Color.rgb(155,229,255),
            Color.rgb(247,197,163)

    };

    public SlideAdapter(Context context ){
        this.context = context;
    }



    @Override
    public int getCount() {
        return titles.length;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide,container,false);
        LinearLayout layoutSlide = (LinearLayout)  view.findViewById(R.id.slideLinearLayout);
        ImageView imgSlide = (ImageView)  view.findViewById(R.id.slideimg);
        TextView titletxt =(TextView)  view.findViewById(R.id.titletxt);
        TextView desciptiontxt = (TextView) view.findViewById(R.id.desciptiontxt);
        //layoutSlide.setBackground(BackgroundColor[position]);
        ColorDrawable colorDrawable = new ColorDrawable(BackgroundColor[position]);
        layoutSlide.setBackground(colorDrawable);
        imgSlide.setImageResource(images[position]);
        titletxt.setText(titles[position]);
        desciptiontxt.setText(descriptions[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}

