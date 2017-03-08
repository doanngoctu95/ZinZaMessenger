package chotot.prect.aptech.zinzamessenger.utils;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ASUS on 02/20/2017.
 */

public class Utils {
    public static int NOTIFICATION_ID = 0;
    public static final String FCM_KEY = "AAAAxJKOUg8:APA91bFCRClxYF_8Ro_sEvS2gMQmfXzA4eNcSFKFVNwM8TkCJOZTpVGTXecgrJFDKCRqAHDoWpJm1_c5r8Jxt2PmQ5coamI5_um8V0dXfOhNarGGZ20Mi9IqtMNYHof5FVsbKmLBSTnm";
    public static final String FCM_SEND_URL = "https://fcm.googleapis.com";

    public static String REJECT_ACTION = "REJECT";
    public static String ACCEPT_ACTION = "ACCEPT";
    public static final String INTERNET = "Turn on internet connection";
    public static final String SIGN_IN_FAIL = "Google Sign In failed.";
    public static final String FB_NAME = "FB_NAME";
    public static final String FB_URL = "FB_URL";
    //Current USER
    public static String USER_ID = "";
    public static String AVATAR_URL = "";
    public static String USER_NAME = "";
    public static String USER_TOKEN = getToken();
    //Type
    public static String TYPE_ADD = "ADD";
    public static String TYPE_ANSWER = "ANSWER";
    //Type chat
    public static String TEXT = "TEXT";
    public static String IMAGE = "IMAGE";
    public static String FILE = "FILE";
    public static String INTRO_ACCEPT = "Hai bạn đã là bạn của nhau. Hãy bắt đầu trò chuyện";
    //Chating
    public static String FR_USER = "FR_USER";
    public static String FORMAT_TIME = "dd-MM-yyyy hh:mm:ss";
    public static String SENDER_ID = "SENDER_ID";
    public static String RECIPIENT_ID = "RECIPIENT_ID";


    public static AlertDialog buildAlertDialog(String title, String message, boolean isCancelable, Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title);

        if (isCancelable) {
            builder.setPositiveButton(android.R.string.ok, null);
        } else {
            builder.setCancelable(false);
        }
        return builder.create();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final static void showToast(String alert, Context mContext) {
        Toast.makeText(mContext, alert, Toast.LENGTH_SHORT).show();
    }

    public final static void dissmiss(ProgressDialog progressDialog) {
        progressDialog.dismiss();
    }

    public static boolean verifyConnection(Context context) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        conectado = conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected();
        return conectado;
    }

    public static void cancelNotification(Context ctx) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public static String getToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    public static String createAt() {
        return new SimpleDateFormat(Utils.FORMAT_TIME).format(new Date());
    }
}
