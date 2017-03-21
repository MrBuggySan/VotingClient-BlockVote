package com.blockvote.votingclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ImageView imageView = (ImageView) findViewById(R.id.electionImage);
        Picasso.with(this)
                .load("http://cdn1-www.dogtime.com/assets/uploads/gallery/30-impossibly-cute-puppies/impossibly-cute-puppy-2.jpg")
//                        .placeholder(R.drawable.dog_placeholder)
//                        .error(R.drawable.dog_error)
//                        .resize(0,(int) (imageView.getWidth()*1.5) )
                .into(imageView);

    }
}
