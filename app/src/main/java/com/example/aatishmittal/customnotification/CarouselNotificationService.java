package com.example.aatishmittal.customnotification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
/**
 * Created by Gaurav Dingolia on 25/04/16.
 */
public class CarouselNotificationService extends Service {

    private static final String TAG = "CarouselNotificationSer";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int id = intent.getIntExtra(Intent.EXTRA_UID, -1);

        NotificationModel model = intent.getParcelableExtra(CarouselNotificationManager.NOTIF_MODEL);
        int currentPos = intent.getIntExtra(CarouselNotificationManager.CURRENT_POS, 0);
        boolean isLeft = intent.getBooleanExtra(CarouselNotificationManager.IS_LEFT, false);

        model.doPlaySound = 0;

        if (isLeft) {
            if (currentPos > 0) {
                currentPos--;
            } else {
                currentPos = model.imageCarouselArray.size() - 1;
            }
        } else {
            if (currentPos == model.imageCarouselArray.size() - 1) {
                currentPos = 0;
            } else {
                currentPos++;
            }
        }

        Log.d(TAG, "current position " + currentPos);

        CarouselNotificationManager carouselNotificationManager = new CarouselNotificationManager(this);
        carouselNotificationManager.publishCarouselNotification(model, id, currentPos);

        return START_NOT_STICKY;
    }

}