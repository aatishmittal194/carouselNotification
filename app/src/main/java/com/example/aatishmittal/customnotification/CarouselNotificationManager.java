package com.example.aatishmittal.customnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class CarouselNotificationManager {

	private Context _oContext;
	public static final String NOTIF_MODEL = "notif_model";
	public static final String IS_LEFT = "is_left";
	public static final String CURRENT_POS = "current_pos";

	public interface ImageDownloadCallback{
		void onImageDownloadComplete(Bitmap b);
		void onError();
	}

	public CarouselNotificationManager(Context context){
		_oContext = context;
	}


	private int totalImageCount;
	private int totalDownloadCount;
	private List<String> removedUrls = new ArrayList<>();

	public void processGCMImageCarousel(final int notifId, final NotificationModel model){

		if(model.imageCarouselArray == null || model.imageCarouselArray.size() == 0)
			return;

		totalImageCount = model.imageCarouselArray.size();

		for(final String url : model.imageCarouselArray)
		{
			downloadImage(url, new ImageDownloadCallback() {
				@Override
				public void onImageDownloadComplete(Bitmap bitmap) {
					totalDownloadCount++;
					checkIfAllImagesDownloaded(model, notifId);
				}

				@Override
				public void onError() {
					totalDownloadCount++;
					removedUrls.add(url);
					checkIfAllImagesDownloaded(model, notifId);
				}
			});
		}

	}

	private void checkIfAllImagesDownloaded(NotificationModel model, int id)
	{
		if(totalImageCount == totalDownloadCount)
		{
			for(String url :removedUrls)
			{
				model.imageCarouselArray.remove(url);
			}

			publishCarouselNotification(model, id, 0);
		}

	}

	public void publishCarouselNotification(NotificationModel model, final int id, int currentPos)
	{
		final NotificationCompat.Builder builder = CarouselNotificationManager.getNotification(_oContext, model);

		if(hasJellyBean()) {
			final RemoteViews remoteViews = new RemoteViews(_oContext.getPackageName(),
					R.layout.custom_caraousel_notification_view);

			final Intent leftButtonIntent = new Intent(_oContext, CarouselNotificationService.class);
			leftButtonIntent.putExtra(Intent.EXTRA_UID, id);
			leftButtonIntent.putExtra(NOTIF_MODEL, model);
			leftButtonIntent.putExtra(IS_LEFT, true);
			leftButtonIntent.putExtra(CURRENT_POS, currentPos);

			final Intent rightButtonIntent = new Intent(_oContext, CarouselNotificationService.class);
			rightButtonIntent.putExtra(Intent.EXTRA_UID, id);
			rightButtonIntent.putExtra(NOTIF_MODEL, model);
			rightButtonIntent.putExtra(IS_LEFT, false);
			rightButtonIntent.putExtra(CURRENT_POS, currentPos);


			builder.setCustomBigContentView(remoteViews);

			downloadImage(model.imageCarouselArray.get(currentPos), new ImageDownloadCallback() {
				@Override
				public void onImageDownloadComplete(Bitmap b) {
					remoteViews.setImageViewBitmap(R.id.image_view,b);
					remoteViews.setOnClickPendingIntent(R.id.left, PendingIntent.getService(_oContext,10,leftButtonIntent,PendingIntent.FLAG_UPDATE_CURRENT));
					remoteViews.setOnClickPendingIntent(R.id.right, PendingIntent.getService(_oContext,15,rightButtonIntent,PendingIntent.FLAG_UPDATE_CURRENT));
					buildNotification(builder,id);
				}

				@Override
				public void onError() {

				}
			});
		}else
		{
			buildNotification(builder,id);
		}

	}

	private void buildNotification(NotificationCompat.Builder builder, int id)
	{
		Notification notif = builder.build();
		notif.flags |= Notification.FLAG_AUTO_CANCEL;

		NotificationManager notificationManager = (NotificationManager) _oContext.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(id, notif);
	}

	private void downloadImage(String url, final ImageDownloadCallback imageDownloadCallback)
	{
		VolleyManager.getInstance(_oContext).getImageLoader().get(url, new ImageLoader.ImageListener() {
			@Override
			public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
				if(response.getBitmap() != null)
					imageDownloadCallback.onImageDownloadComplete(response.getBitmap());
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				imageDownloadCallback.onError();
			}
		});
	}


	public static NotificationCompat.Builder getNotification(Context context, NotificationModel model){

		Uri uri = null;
		if (model.doPlaySound == 1) {
			uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		}

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
		.setContentTitle(model.title)
		.setContentText(model.contentText)
		.setSmallIcon(R.mipmap.ic_launcher);

		if(uri != null)
			builder.setSound(uri);

		return builder;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}


}
