package com.example.aatishmittal.customnotification;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Gaurav Dingolia on 10/10/15.
 */
public class NotificationModel implements Parcelable
{
    public Intent intent;
    public int count;
    public int doPlaySound;
    public int notificationType;
    public long nid;
    public int adType;
    public long customId;
    public String tag;
    public String contentText;
    public String text;
    public String title;
    public String packetId;
    public String imageUrl;
    public String notificationImageUrl;
    public String email;
    public String offerId;
    public String webViewURL;
    public String userId;
    public String popupId;
    public boolean stackNotification;
    public List<String> imageCarouselArray;
    public List<String> deepLinksForCarousel;

    public int messageType;
    /**
     * Currently we have - IMA.VAP.<MetaCatId>.<SubCatId>
     * We could have other scenarios like MyAds, SnB, PAP, etc
     */
    public String scenario;
    public String allowId;
    public String allowEmail;
    public String deeplink;

    public NotificationModel(){};

    protected NotificationModel(Parcel in)
    {
        intent = in.readParcelable(Intent.class.getClassLoader());
        count = in.readInt();
        doPlaySound = in.readInt();
        notificationType = in.readInt();
        nid = in.readLong();
        adType = in.readInt();
        customId = in.readLong();
        tag = in.readString();
        contentText = in.readString();
        text = in.readString();
        title =in.readString();
        packetId = in.readString();
        imageUrl = in.readString();
        notificationImageUrl = in.readString();
        email = in.readString();
        offerId = in.readString();
        webViewURL = in.readString();
        userId = in.readString();
        popupId = in.readString();
        stackNotification = in.readByte() > 0;
        messageType = in.readInt();
        allowEmail = in.readString();
        allowId = in.readString();
        deeplink = in.readString();


        imageCarouselArray = in.createStringArrayList();
        deepLinksForCarousel = in.createStringArrayList();
    }

    public static final Creator<NotificationModel> CREATOR = new Creator<NotificationModel>()
    {
        @Override
        public NotificationModel createFromParcel(Parcel in)
        {
            return new NotificationModel(in);
        }

        @Override
        public NotificationModel[] newArray(int size)
        {
            return new NotificationModel[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(intent, flags);
        dest.writeInt(count);
        dest.writeInt(doPlaySound);
        dest.writeInt(notificationType);
        dest.writeLong(nid);
        dest.writeInt(adType);
        dest.writeLong(customId);
        dest.writeString(tag);
        dest.writeString(contentText);
        dest.writeString(text);
        dest.writeString(title);
        dest.writeString(packetId);
        dest.writeString(imageUrl);
        dest.writeString(notificationImageUrl);
        dest.writeString(email);
        dest.writeString(offerId);
        dest.writeString(webViewURL);
        dest.writeString(userId);
        dest.writeString(popupId);
        dest.writeByte(stackNotification?Byte.MAX_VALUE:0);
        dest.writeInt(messageType);
        dest.writeString(allowEmail);
        dest.writeString(allowId);
        dest.writeString(deeplink);


        dest.writeStringList(imageCarouselArray);
        dest.writeStringList(deepLinksForCarousel);
    }

    public static final String PAYLOAD_ATTR_SCENARIO = "notif_scenario";
}
