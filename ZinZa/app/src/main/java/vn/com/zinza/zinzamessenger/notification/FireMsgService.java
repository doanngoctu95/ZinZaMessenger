package vn.com.zinza.zinzamessenger.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import vn.com.zinza.zinzamessenger.R;
import vn.com.zinza.zinzamessenger.MainActivity;
import vn.com.zinza.zinzamessenger.utils.Utils;

/**
 * Created by ASUS on 02/28/2017.
 */

public class FireMsgService extends FirebaseMessagingService {
    private String senDerID;
    private String senderToken;
    public String senderName;
    public static String SENDER_ID = "SENDER_ID";
    public static String SENDER_TOKEN = "SENDER_TOKEN";
    public static String SENDER_NAME = "SENDER_NAME";
    private String type;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Msg", "Message received [" + remoteMessage + "]");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        senDerID = remoteMessage.getData().get("id");
        senderToken = remoteMessage.getData().get("token");
        type = remoteMessage.getData().get("type");
        String urlAvatar = remoteMessage.getData().get("avatarURL");
        if(type.equals(Utils.TYPE_ADD)){
            showNotification(title, body, urlAvatar, Utils.ACCEPT_ACTION);
        } else if(type.equals(Utils.TYPE_ANSWER)){
            showNotification(title, body, urlAvatar, Utils.REJECT_ACTION);
        }

    }

    private void showNotification(String title, String message, String url,String actionAccept) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification_add_friend);
        Intent t = new Intent(this, MainActivity.class);

        PendingIntent pdIntent = PendingIntent.getActivity(this, 0, t, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mbBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.zinza_icon)
                .setContentTitle(title)
                .setAutoCancel(false)
                .setContentText(message + " muốn gửi lời mời kết bạn đến bạn.Bạn có đồng ý không ?");
        android.app.Notification notification = mbBuilder.build();
        remoteViews.setImageViewBitmap(R.id.imgAvatarNoti, getBitmapFromUrl(url));
        remoteViews.setTextViewText(R.id.txtMessageNoti, message);
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification.bigContentView = remoteViews;
        }
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mbBuilder.setSound(alarmSound);
        Intent rejectIntent = new Intent(this, SwitchButtonListener.class);
        rejectIntent.setAction(Utils.REJECT_ACTION);
        PendingIntent pendingRejectClick = PendingIntent.getBroadcast(this, 0, rejectIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.btnReject, pendingRejectClick);

        Intent acceptIntent = new Intent(this, SwitchButtonListener.class);
        acceptIntent.putExtra(SENDER_ID, senDerID);
        acceptIntent.putExtra(SENDER_TOKEN, senderToken);
        acceptIntent.putExtra(SENDER_NAME, senderName);
        acceptIntent.setAction(actionAccept);
        PendingIntent pendingAcceptClick = PendingIntent.getBroadcast(this, 1234, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btnAccept, pendingAcceptClick);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(Utils.NOTIFICATION_ID, notification);
    }

    private Bitmap getBitmapFromUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
            return mBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
