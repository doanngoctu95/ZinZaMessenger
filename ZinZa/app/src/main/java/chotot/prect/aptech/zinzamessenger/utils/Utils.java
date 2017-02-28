package chotot.prect.aptech.zinzamessenger.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by ASUS on 02/20/2017.
 */

public class Utils {
    public static  String TOKEN = "";
    public static final String FCM_KEY = "AAAAxJKOUg8:APA91bFCRClxYF_8Ro_sEvS2gMQmfXzA4eNcSFKFVNwM8TkCJOZTpVGTXecgrJFDKCRqAHDoWpJm1_c5r8Jxt2PmQ5coamI5_um8V0dXfOhNarGGZ20Mi9IqtMNYHof5FVsbKmLBSTnm";
    public static final String FCM_SEND_URL = "https://fcm.googleapis.com";

    public static final String INTERNET = "Turn on internet connection";
    public static final String SIGN_IN_FAIL = "Google Sign In failed.";
    public static final String FB_NAME = "FB_NAME";
    public static final String FB_URL = "FB_URL";


    public static AlertDialog buildAlertDialog(String title, String message, boolean isCancelable, Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title);

        if(isCancelable){
            builder.setPositiveButton(android.R.string.ok, null);
        }else {
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
    public final static void showToast(String alert,Context mContext){
        Toast.makeText(mContext,alert,Toast.LENGTH_SHORT).show();
    }

    public final static void dissmiss(ProgressDialog progressDialog){
        progressDialog.dismiss();
    }

    public  static boolean verifyConnection(Context context) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        conectado = conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected();
        return conectado;
    }
}
