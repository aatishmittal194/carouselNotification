package com.example.aatishmittal.customnotification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private String[] urls ={"http://www.freedigitalphotos.net/images/img/homepage/87357.jpg","https://pixabay.com/static/uploads/photo/2016/05/12/20/58/water-lilies-1388690_1280.jpg","https://pixabay.com/static/uploads/photo/2013/09/15/18/17/aquatic-plant-182635_1280.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bcustomnotify = (Button) findViewById(R.id.customnotification);

        bcustomnotify.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                CustomNotification();
            }
        });
    }


    public void CustomNotification() {

        NotificationModel model = new NotificationModel();
        model.title = "title";
        model.contentText = "hello hi howru";
        model.doPlaySound = 1;
        model.imageCarouselArray = Arrays.asList(urls);

        CarouselNotificationManager carouselNotificationManager = new CarouselNotificationManager(getApplicationContext());
        carouselNotificationManager.processGCMImageCarousel(1,model);


    }






}